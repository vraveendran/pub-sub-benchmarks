package com.vertexinc.frameworks.pubsub;

public class NoKeyDistributor extends KeyDistributor {

    @Override
    public String next() {
        return null;
    }
}
