package kombyte;

import kombyte.tokenbucket.TokenBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public abstract class RateLimiterTest {

    protected NanoClock clock;

    @BeforeEach
    public void setUp() {
        clock = Mockito.mock(NanoClock.class);
    }

    // allows no more than 1 request per minute
    protected abstract RateLimiter createRateLimiter();

    @Test
    public void testRateLimiterAllowsRequest() {
        Mockito.when(clock.nanoTime()).thenReturn(System.nanoTime());
        RateLimiter rateLimiter = createRateLimiter();
        assertTrue(rateLimiter.allowRequest());
    }

    @Test
    public void testRateLimiterRejectesRequest() {
        Mockito.when(clock.nanoTime()).thenReturn(System.nanoTime());
        RateLimiter rateLimiter = createRateLimiter();
        rateLimiter.allowRequest();
        assertFalse(rateLimiter.allowRequest());
    }

    @Test
    public void testRateLimiterResetsAfterInterval() {
        Mockito.when(clock.nanoTime()).thenReturn(System.nanoTime() - 61 * 1_000_000_000L);
        RateLimiter rateLimiter = createRateLimiter();
        rateLimiter.allowRequest();
        rateLimiter.allowRequest();
        Mockito.when(clock.nanoTime()).thenReturn(System.nanoTime());
        assertTrue(rateLimiter.allowRequest());
    }

    @Test
    public void testAllowConstantRate() {
        long now = System.nanoTime();
        Mockito.when(clock.nanoTime()).thenReturn(now);
        RateLimiter rateLimiter = createRateLimiter();
        for (int i = 0; i < 100; i++) {
            long elapsedSeconds = i * 1_000_000_001L;   // a bit more than 1 second
            Mockito.when(clock.nanoTime()).thenReturn(now + elapsedSeconds);
            assertTrue(rateLimiter.allowRequest());
        }
    }

}
