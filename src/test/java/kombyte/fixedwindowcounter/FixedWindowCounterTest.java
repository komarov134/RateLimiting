package kombyte.fixedwindowcounter;

import kombyte.RateLimiter;
import kombyte.RateLimiterTest;
import kombyte.leakybucket.LeakyBucket;

public class FixedWindowCounterTest extends RateLimiterTest {
    @Override
    protected RateLimiter createRateLimiter() {
        return new FixedWindowCounter(1, 1_000_000_000L, clock);
    }
}
