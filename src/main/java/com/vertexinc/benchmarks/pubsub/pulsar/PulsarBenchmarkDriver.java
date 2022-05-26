package com.vertexinc.benchmarks.pubsub.pulsar;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.bookkeeper.stats.StatsLogger;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminBuilder;
import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.pulsar.shade.com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vertexinc.frameworks.pubsub.BenchmarkConsumer;
import com.vertexinc.frameworks.pubsub.BenchmarkDriver;
import com.vertexinc.frameworks.pubsub.BenchmarkProducer;
import com.vertexinc.frameworks.pubsub.ConsumerCallback;

public class PulsarBenchmarkDriver implements BenchmarkDriver {

	private PulsarClient client;
	private PulsarAdmin adminClient;

	private PulsarConfig config;	
    private String namespace = "benchmark";

	private ProducerBuilder<byte[]> producerBuilder;

	private static final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
	private static final Logger log = LoggerFactory.getLogger(PulsarBenchmarkDriver.class);

	public void initialize(File configurationFile, StatsLogger statsLogger) throws IOException {
		
		this.config = readConfig(configurationFile);
        log.info("Pulsar driver configuration: {}", writer.writeValueAsString(config));

		ClientBuilder clientBuilder = PulsarClient.builder().ioThreads(config.client.ioThreads)
				.connectionsPerBroker(config.client.connectionsPerBroker).statsInterval(0, TimeUnit.SECONDS)
				.serviceUrl(config.client.serviceUrl).maxConcurrentLookupRequests(50000).maxLookupRequests(100000)
				.listenerThreads(Runtime.getRuntime().availableProcessors());

		PulsarAdminBuilder pulsarAdminBuilder = PulsarAdmin.builder().serviceHttpUrl(config.client.httpUrl);

		client = clientBuilder.build();

		log.info("Created Pulsar client for service URL {}", config.client.serviceUrl);

		adminClient = pulsarAdminBuilder.build();

		log.info("Created Pulsar admin client for HTTP URL {}", config.client.httpUrl);

		producerBuilder = client.newProducer().enableBatching(config.producer.batchingEnabled)
				.batchingMaxPublishDelay(config.producer.batchingMaxPublishDelayMs, TimeUnit.MILLISECONDS)
				.blockIfQueueFull(config.producer.blockIfQueueFull).batchingMaxBytes(config.producer.batchingMaxBytes)
				.maxPendingMessages(config.producer.pendingQueueSize).batchingMaxMessages(Integer.MAX_VALUE)
				.maxPendingMessagesAcrossPartitions(Integer.MAX_VALUE);
	}

	public CompletableFuture<Void> createTopic(String topic, int partitions) {
		if (partitions == 1) {
			// No-op
			return CompletableFuture.completedFuture(null);
		}

		return adminClient.topics().createPartitionedTopicAsync(topic, partitions);
	}

	@Override
	public CompletableFuture<Void> notifyTopicCreation(String topic, int partitions) {
		// No-op
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<BenchmarkProducer> createProducer(String topic) {
		return producerBuilder.topic(topic).createAsync()
				.thenApply(pulsarProducer -> new PulsarBenchmarkProducer(pulsarProducer));
	}

	@Override
	public CompletableFuture<BenchmarkConsumer> createConsumer(String topic, String subscriptionName,
			Optional<Integer> partition, ConsumerCallback consumerCallback) {
		return client.newConsumer().priorityLevel(0).subscriptionType(SubscriptionType.Failover)
				.messageListener((consumer, msg) -> {
					consumerCallback.messageReceived(msg.getData(),
							TimeUnit.MILLISECONDS.toNanos(msg.getPublishTime()));
					consumer.acknowledgeAsync(msg);
				}).topic(topic).subscriptionName(subscriptionName).subscribeAsync()
				.thenApply(consumer -> new PulsarBenchmarkConsumer(consumer));

	}

	@Override
	public void close() throws Exception {
		log.info("Shutting down Pulsar benchmark driver");

		if (client != null) {
			client.close();
		}

		if (adminClient != null) {
			adminClient.close();
		}

		log.info("Pulsar benchmark driver successfully shut down");
	}

	private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private static PulsarConfig readConfig(File configurationFile) throws IOException {
		return mapper.readValue(configurationFile, PulsarConfig.class);
	}

	@Override
	public String getTopicNamePrefix() {
        return config.client.topicType + "://" + namespace + "/test";
	}

}
