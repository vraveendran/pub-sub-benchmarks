package com.vertexinc.frameworks.pubsub;

//import javax.annotation.concurrent.NotThreadSafe;

//@NotThreadSafe
public class KeyRoundRobin extends KeyDistributor {

    private int currentIndex = 0;

    @Override
    public String next() {
        if (++currentIndex >= getLength()) {
            currentIndex = 0;
        }
        return get(currentIndex);
    }
}
