package kombyte.slidinglog;

import kombyte.NanoClock;
import kombyte.RateLimiter;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 1. Log every request along with its timestamp
 * 2. Window slides continuously, capturing recent requests
 * 3. If the count exceeds the limit, deny the request
 */
public class SlidingLog implements RateLimiter {

    private int maxRequestsInWindow;
    private long windowLengthNanos;
    private final NanoClock clock;

    // contains timestamps (nanos) of recent requests
    private Queue<Long> tsQueue;

    public SlidingLog(int maxRequestsInWindow, long windowLengthNanos, NanoClock clock) {
        this.maxRequestsInWindow = maxRequestsInWindow;
        this.windowLengthNanos = windowLengthNanos;
        this.clock = clock;
        this.tsQueue = new ArrayDeque<>(maxRequestsInWindow);    // hello, boxing
    }

    public boolean allowRequest() {
        long now = clock.nanoTime();
        cleanupOutOfWindowRequests(now);
        if (tsQueue.size() < maxRequestsInWindow) {
            return tsQueue.offer(now);
        }
        return false;
    }

    private void cleanupOutOfWindowRequests(long now) {
        while (!tsQueue.isEmpty() && tsQueue.peek() < now - windowLengthNanos) tsQueue.remove();
    }

}
