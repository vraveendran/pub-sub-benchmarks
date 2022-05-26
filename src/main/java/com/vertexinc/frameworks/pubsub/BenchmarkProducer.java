package com.vertexinc.frameworks.pubsub;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BenchmarkProducer extends AutoCloseable {

    /**
     * Publish a message and return a callback to track the completion of the operation.
     *
     * @param key
     *            the key associated with this message
     * @param payload
     *            the message payload
     * @return a future that will be triggered when the message is successfully published
     */
    CompletableFuture<Void> sendAsync(Optional<String> key, byte[] payload);

}
