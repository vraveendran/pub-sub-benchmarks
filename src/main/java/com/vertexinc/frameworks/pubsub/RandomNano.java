package com.vertexinc.frameworks.pubsub;

//import org.apache.pulsar.shade.javax.annotation.concurrent.ThreadSafe;

//@ThreadSafe
public class RandomNano extends KeyDistributor {

    public String next() {
        int randomIndex = Math.abs((int) System.nanoTime() % getLength());
        return get(randomIndex);
    }
}
