package com.vertexinc.benchmarks.pubsub.pulsar;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.TypedMessageBuilder;

import com.vertexinc.frameworks.pubsub.BenchmarkProducer;

public class PulsarBenchmarkProducer implements BenchmarkProducer {

    private final Producer<byte[]> producer;

    public PulsarBenchmarkProducer(Producer<byte[]> producer) {
        this.producer = producer;
    }

    @Override
    public void close() throws Exception {
        producer.close();
    }

    @Override
    public CompletableFuture<Void> sendAsync(Optional<String> key, byte[] payload) {
        TypedMessageBuilder<byte[]> msgBuilder = producer.newMessage().value(payload);
        if (key.isPresent()) {
            msgBuilder.key(key.get());
        }

        return msgBuilder.sendAsync().thenApply(msgId -> null);
    }

}
