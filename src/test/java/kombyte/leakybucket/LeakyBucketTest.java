package kombyte.leakybucket;

import kombyte.RateLimiter;
import kombyte.RateLimiterTest;

public class LeakyBucketTest extends RateLimiterTest {
    @Override
    protected RateLimiter createRateLimiter() {
        return new LeakyBucket(1, 1, clock);
    }
}
