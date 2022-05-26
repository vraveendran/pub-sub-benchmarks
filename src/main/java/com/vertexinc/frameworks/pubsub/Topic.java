package com.vertexinc.frameworks.pubsub;

public class Topic {
    public String name;
    public int partitions;

    public Topic() {
    }

    public Topic(String name, int partitions) {
        this.name = name;
        this.partitions = partitions;
    }
}