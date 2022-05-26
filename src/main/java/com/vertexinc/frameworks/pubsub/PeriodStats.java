package com.vertexinc.frameworks.pubsub;

import java.util.concurrent.TimeUnit;

import org.HdrHistogram.Histogram;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PeriodStats {
    public long messagesSent = 0;
    public long bytesSent = 0;

    public long messagesReceived = 0;
    public long bytesReceived = 0;

    public long totalMessagesSent = 0;
    public long totalMessagesReceived = 0;

    @JsonIgnore
    public Histogram publishLatency = new Histogram(TimeUnit.SECONDS.toMicros(60), 5);
    public byte[] publishLatencyBytes;

    @JsonIgnore
    public Histogram endToEndLatency = new Histogram(TimeUnit.HOURS.toMicros(12), 5);
    public byte[] endToEndLatencyBytes;
}
