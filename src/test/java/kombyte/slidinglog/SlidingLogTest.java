package kombyte.slidinglog;

import kombyte.RateLimiter;
import kombyte.RateLimiterTest;
import kombyte.fixedwindowcounter.FixedWindowCounter;

public class SlidingLogTest extends RateLimiterTest {
    @Override
    protected RateLimiter createRateLimiter() {
        return new SlidingLog(1, 1_000_000_000L, clock);
    }
}
