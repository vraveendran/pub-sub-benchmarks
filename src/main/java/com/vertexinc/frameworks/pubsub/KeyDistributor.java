package com.vertexinc.frameworks.pubsub;

import com.google.common.io.BaseEncoding;

import java.util.Random;

public abstract class KeyDistributor {

    private static final int UNIQUE_COUNT = 10_000;
    private static final int KEY_BYTE_SIZE = 7;

    private static final String[] randomKeys = new String[UNIQUE_COUNT];
    private static final Random random = new Random();

    static {
        // Generate a number of random keys to be used when publishing
        byte[] buffer = new byte[KEY_BYTE_SIZE];
        for (int i = 0; i < randomKeys.length; i++) {
            random.nextBytes(buffer);
            randomKeys[i] = BaseEncoding.base64Url().omitPadding().encode(buffer);
        }
    }

    protected String get(int index) {
        return randomKeys[index];
    }

    protected int getLength() {
        return UNIQUE_COUNT;
    }

    public abstract String next();

    public static KeyDistributor build(KeyDistributorType keyType) {
        KeyDistributor keyDistributor = null;
        switch (keyType) {
            case NO_KEY:
                keyDistributor = new NoKeyDistributor();
                break;
            case KEY_ROUND_ROBIN:
                keyDistributor = new KeyRoundRobin();
                break;
            case RANDOM_NANO:
                keyDistributor = new RandomNano();
                break;
        }
        return keyDistributor;
    }

}
