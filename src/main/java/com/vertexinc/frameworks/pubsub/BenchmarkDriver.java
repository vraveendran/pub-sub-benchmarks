package com.vertexinc.frameworks.pubsub;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.bookkeeper.stats.StatsLogger;

/**
 * Base driver interface
 */
public interface BenchmarkDriver extends AutoCloseable {
	/**
	 * Driver implementation can use this method to initialize the client libraries,
	 * with the provided configuration file.
	 * <p>
	 * The format of the configuration file is specific to the driver
	 * implementation.
	 * 
	 * @param configurationFile
	 * @param statsLogger       stats logger to collect stats from benchmark driver
	 * @throws IOException
	 */
	void initialize(File configurationFile, StatsLogger statsLogger) throws IOException;

	/**
	 * Get a driver specific prefix to be used in creating multiple topic names
	 */
	String getTopicNamePrefix();

	/**
	 * Create a new topic with a given number of partitions
	 */
	CompletableFuture<Void> createTopic(String topic, int partitions);

	/**
	 * Notification of new topic creation with the given number of partitions
	 */
	CompletableFuture<Void> notifyTopicCreation(String topic, int partitions);

	/**
	 * Create a producer for a given topic
	 */
	CompletableFuture<BenchmarkProducer> createProducer(String topic);

	/**
	 * Create a benchmark consumer relative to one particular topic and
	 * subscription.
	 * 
	 * It is responsibility of the driver implementation to invoke the
	 * <code>consumerCallback</code> each time a message is received.
	 * 
	 * @param topic
	 * @param subscriptionName
	 * @param consumerCallback
	 * @return
	 */
	CompletableFuture<BenchmarkConsumer> createConsumer(String topic, String subscriptionName,
			Optional<Integer> partition, ConsumerCallback consumerCallback);

}
