package kombyte.leakybucket;

import java.util.concurrent.CompletableFuture;

public sealed interface Result permits Result.Rejected, Result.Scheduled {
    record Rejected() implements Result {}
    record Scheduled(CompletableFuture<Void> delay) implements Result {}
}
