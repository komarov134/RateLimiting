package kombyte.leakybucket;

import kombyte.NanoClock;
import kombyte.RateLimiter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 1. Requests enter the bucket
 * 2. Requests are processed at a constant rate
 * 3. If the requests come in too fast, the bucket overflows and excess requests are discarded
 * Note: The main advantage of LeakyBucket is it smooths out outcome request load.
 */
public class LeakyBucket implements RateLimiter {

    private final long capacity;
    private final long leakRate;
    private final NanoClock clock;

    private long waterLevel;
    private long lastCheckTime;

    /**
     * @param capacity kinda request queue size.
     * @param leakRate how fast requests leak from the bucket. Minimum is 1 rps.
     */
    public LeakyBucket(long capacity, long leakRate, NanoClock clock) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.clock = clock;
        this.waterLevel = 0;
        this.lastCheckTime = clock.nanoTime();
    }

    /**
     * not thread safe
     * @return Rejected if request queue is full (determined by waterLevel)
     *         Scheduled if the request has been accepted. It also returns Future which completes when it's time to make a request.
     */
    public Result scheduleRequest() {
        leak();
        if (waterLevel < capacity) {
            long delayNs = 1_000_000_000L * waterLevel / capacity;
            waterLevel++;
            return new Result.Scheduled(
                    CompletableFuture.supplyAsync(() -> null, CompletableFuture.delayedExecutor(delayNs, TimeUnit.NANOSECONDS))
            );
        }
        return new Result.Rejected();
    }

    @Override
    public boolean allowRequest() {
        return scheduleRequest() instanceof Result.Scheduled;
    }

    private void leak() {
        long now = clock.nanoTime();
        long nanosSinceLastCheck = now - lastCheckTime;
        long leakedDrops = leakRate * nanosSinceLastCheck / 1_000_000_000L;
        if (leakedDrops > 0) {
            lastCheckTime = now;
            waterLevel = Math.max(0, waterLevel - leakedDrops);
        }
    }

}
