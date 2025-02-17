package kombyte.tokenbucket;

import kombyte.NanoClock;
import kombyte.RateLimiter;

/**
 * 1. Tokens are added steadily
 * 2. Each request consumes a token
 * 3. If no tokens, request is denied
 */
public class TokenBucket implements RateLimiter {

    private final long capacity;
    private final long refillRate;
    private final NanoClock clock;

    private long tokens;
    private long lastRefillTimestamp;

    public TokenBucket(long capacity, long refillRate, NanoClock clock) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.clock = clock;
        this.tokens = capacity;
        this.lastRefillTimestamp = clock.nanoTime();
    }

    // not thread safe
    public boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = clock.nanoTime();
        long nanosSinceLastRefill = now - lastRefillTimestamp;
        long newTokens = refillRate * nanosSinceLastRefill / 1_000_000_000L;
        if (newTokens > 0) {
            lastRefillTimestamp = now;
            tokens = Math.min(capacity, tokens + newTokens);
        }
    }

}
