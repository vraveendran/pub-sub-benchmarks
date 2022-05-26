package com.vertexinc.benchmarks.pubsub.pulsar;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class PulsarConsumer {
    public static void main(String[] args) throws PulsarClientException {
    	
    		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();

    		Consumer<byte[]> consumer = client.newConsumer()
    				.topic("pulsar-test-topic")
    				.subscriptionName("pulsar-test-subscription")
    				.subscribe();

    		Message<byte[]> message = consumer.receive();
    		
    		System.out.println(new String(message.getData()));

    		consumer.close();

    		client.close();
    	
    }    
}