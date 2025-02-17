package kombyte.tokenbucket;

import kombyte.RateLimiter;
import kombyte.RateLimiterTest;
import kombyte.leakybucket.LeakyBucket;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenBucketTest extends RateLimiterTest {
    @Override
    protected RateLimiter createRateLimiter() {
        return new TokenBucket(1, 1, clock);
    }

    @Test
    public void testAllowBurst() {
        Mockito.when(clock.nanoTime()).thenReturn(System.nanoTime());
        RateLimiter rateLimiter = new TokenBucket(10, 1, clock);
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiter.allowRequest());
        }
        assertFalse(rateLimiter.allowRequest());
    }

}
