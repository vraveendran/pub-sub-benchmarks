package com.vertexinc.benchmarks.pubsub.pulsar;

import java.io.IOException;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

public class PulsarProducer {

	public static void main(final String[] args) throws IOException {

		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();

		Producer<byte[]> producer = client.newProducer().topic("pulsar-test-topic").create();

		producer.send("My message".getBytes());

		producer.flush();

		producer.close();

		client.close();
	}
}
