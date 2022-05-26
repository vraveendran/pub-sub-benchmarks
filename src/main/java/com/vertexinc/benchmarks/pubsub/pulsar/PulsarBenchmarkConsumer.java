package com.vertexinc.benchmarks.pubsub.pulsar;

import org.apache.pulsar.client.api.Consumer;

import com.vertexinc.frameworks.pubsub.BenchmarkConsumer;

public class PulsarBenchmarkConsumer implements BenchmarkConsumer {

    private final Consumer<byte[]> consumer;

    public PulsarBenchmarkConsumer(Consumer<byte[]> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void close() throws Exception {
        consumer.close();
    }

}
