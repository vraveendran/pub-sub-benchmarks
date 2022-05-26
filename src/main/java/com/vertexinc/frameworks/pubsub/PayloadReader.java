package com.vertexinc.frameworks.pubsub;

public interface PayloadReader {

    byte[] load(String resourceName);
}
