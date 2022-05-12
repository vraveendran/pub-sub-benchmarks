package com.vertexinc.frameworks.pubsub;

public interface PubSubProducer {
	
	void send();
	void createTopic(String topicName);
	void deleteTopic(String topicName);
}