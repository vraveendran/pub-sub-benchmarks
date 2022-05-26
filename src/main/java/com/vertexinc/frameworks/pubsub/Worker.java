package com.vertexinc.frameworks.pubsub;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Worker extends AutoCloseable {

	void initializeDriver(File configurationFile) throws IOException;

	List<Topic> createTopics(TopicsInfo topicsInfo) throws IOException;

	// Let other workers know when a new topic(s) is created
	void notifyTopicCreation(List<Topic> topics) throws IOException;

	void createProducers(List<String> topics) throws IOException;

	void createConsumers(ConsumerAssignment consumerAssignment) throws IOException;

	void probeProducers() throws IOException;

	void startLoad(ProducerWorkAssignment producerWorkAssignment) throws IOException;

	void adjustPublishRate(double publishRate) throws IOException;

	void pauseConsumers() throws IOException;

	void resumeConsumers() throws IOException;

	void pauseProducers() throws IOException;

	void resumeProducers() throws IOException;

	CountersStats getCountersStats() throws IOException;

	PeriodStats getPeriodStats() throws IOException;

	CumulativeLatencies getCumulativeLatencies() throws IOException;

	void resetStats() throws IOException;

	void stopAll() throws IOException;

}
