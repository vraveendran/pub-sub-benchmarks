package com.vertexinc.benchmarks.pubsub.pulsar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.pulsar.client.api.HashingScheme;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.MessageRouter;
import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.TopicMetadata;

public class PulsarProducer {

	public static void main(final String[] args) throws IOException {
		
		PulsarProducer producer = new PulsarProducer();
		
		producer.singleMessage();
		producer.multipleMessages();
		producer.multipleTopics();
		producer.batching();
		producer.chunking();
		producer.routing();
	}
	
	private void singleMessage() throws PulsarClientException {

		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();

		Producer<byte[]> producer = client.newProducer().topic("pulsar-test-topic").create();

		producer.send("My message".getBytes());

		producer.flush();

		producer.close();

		client.close();
	}
	
	
	private void multipleMessages() throws PulsarClientException {
		
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();

		Producer<byte[]> producer = client.newProducer().topic("pulsar-test-topic").create();
		
	
		IntStream.range(1, 5).forEach(i -> {
		    String content = String.format("hi-pulsar-%d", i);		   
		    try {
				MessageId msgId = producer.send(content.getBytes());
			} catch (PulsarClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		producer.flush();

		producer.close();

		client.close();
	}
	
	private void multipleTopics() throws PulsarClientException {
		
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		
		for(int topic = 0; topic<= 1000; topic++) {
			Producer<byte[]> producer  = client.newProducer().topic("pulsar-test-topic-"+topic).create();
		
			IntStream.range(1, 5).forEach(i -> {
			    String content = String.format("hi-pulsar-%d", i);		   
			    try {
					MessageId msgId = producer.send(content.getBytes());
				} catch (PulsarClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			producer.flush();
	
			producer.close();		
		}

		client.close();
	}
	
	private void batching() throws PulsarClientException {
		
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		
		Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("pulsar-test-topic-batching")
                .producerName("test-producer")
                .enableBatching(true)
                .blockIfQueueFull(true)
                .batchingMaxMessages(10000)
                .batchingMaxBytes(10000000)
                .create();
		
	}
	
	private void chunking() throws PulsarClientException {
		
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		
		Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("pulsar-test-topic-chunking")
                .producerName("test-producer")
                .enableBatching(false)
                .enableChunking(true)
                .sendTimeout(120, TimeUnit.SECONDS)
                .create();
		
	}
	
	private void routing() throws PulsarClientException {
		
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		
		Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("pulsar-test-topic-routing")
                .producerName("test-producer")
                .blockIfQueueFull(true)
                .messageRoutingMode(MessageRoutingMode.CustomPartition)
                .hashingScheme(HashingScheme.Murmur3_32Hash)
                               .messageRouter(new MessageRouter() {
                    @Override
                    public int choosePartition(Message<?> msg, TopicMetadata metadata) {
                        String key = msg.getKey();
                        return Integer.parseInt(key) % metadata.numPartitions();
                    }
                })
                .create();
		
	}
}
