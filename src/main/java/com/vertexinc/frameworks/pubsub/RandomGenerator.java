package com.vertexinc.frameworks.pubsub;

import com.google.common.io.BaseEncoding;
import java.util.Random;

public class RandomGenerator {

    private static final Random random = new Random();

    public static final String getRandomString() {
        byte[] buffer = new byte[5];
        random.nextBytes(buffer);
        return BaseEncoding.base64Url().omitPadding().encode(buffer);
    }
}
