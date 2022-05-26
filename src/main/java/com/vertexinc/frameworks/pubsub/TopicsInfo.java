package com.vertexinc.frameworks.pubsub;

public class TopicsInfo {
    public int numberOfTopics;
    public int numberOfPartitionsPerTopic;

    public TopicsInfo() {
    }

    public TopicsInfo(int numberOfTopics, int numberOfPartitionsPerTopic) {
        this.numberOfTopics = numberOfTopics;
        this.numberOfPartitionsPerTopic = numberOfPartitionsPerTopic;
    }
}