package com.vertexinc.frameworks.pubsub;

/**
 * Callback that the driver implementation calls when a message is received
 */
public interface ConsumerCallback {
    /**
     * Driver should invoke this method once for each message received
     * 
     * @param payload
     *            the received message payload
     * @param publishTimestamp
     *            the publish timestamp in milliseconds
     */
    void messageReceived(byte[] payload, long publishTimestamp);
}
