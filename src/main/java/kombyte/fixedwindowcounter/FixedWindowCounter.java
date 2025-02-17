package kombyte.fixedwindowcounter;

import kombyte.NanoClock;
import kombyte.RateLimiter;

/**
 * 1. Time is divided into fixed windows
 * 2. Each window allows a maximum number of requests
 * 3. Once the limit is reached, no more requests are allowed until the next window starts
 * Cons: There’s a chance for a burst of requests right at the boundary.
 * Cons: Users have to wait until the reset to make a request.
 */
public class FixedWindowCounter implements RateLimiter {

    private int maxRequestsPerWindow;
    private long windowLengthNanos;
    private final NanoClock clock;

    private long lastWindowStart;
    private int requestsInCurrentWindow;

    public FixedWindowCounter(int maxRequestsPerWindow, long windowLengthNanos, NanoClock clock) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowLengthNanos = windowLengthNanos;
        this.clock = clock;
        this.lastWindowStart = clock.nanoTime();
        this.requestsInCurrentWindow = 0;
    }

    public boolean allowRequest() {
        updateWindow();
        if (requestsInCurrentWindow < maxRequestsPerWindow) {
            requestsInCurrentWindow++;
            return true;
        }
        return false;
    }

    private void updateWindow() {
        long now = clock.nanoTime();
        if (now - lastWindowStart > windowLengthNanos) {
            lastWindowStart = now;
            requestsInCurrentWindow = 0;
        }
    }

}
