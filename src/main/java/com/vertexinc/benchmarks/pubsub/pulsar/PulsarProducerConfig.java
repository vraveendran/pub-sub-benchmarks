package com.vertexinc.benchmarks.pubsub.pulsar;

public class PulsarProducerConfig {

	public boolean batchingEnabled = true;
	public boolean blockIfQueueFull = true;
	public int batchingMaxPublishDelayMs = 1;
	public int batchingMaxBytes = 128 * 1024;
	public int pendingQueueSize = 1000;
}
