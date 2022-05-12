package com.vertexinc.frameworks.pubsub;

public interface PubSubConsumer {

	void read();
	void createTopic(String topicName);
	void deleteTopic(String topicName);
}
