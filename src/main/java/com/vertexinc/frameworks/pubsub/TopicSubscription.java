package com.vertexinc.frameworks.pubsub;

public class TopicSubscription {
    public String topic;
    public String subscription;
    public int partition;

    public TopicSubscription() {
    }

    public TopicSubscription(String topic, String subscription, int partition) {
        this.topic = topic;
        this.subscription = subscription;
        this.partition = partition;
    }

}
