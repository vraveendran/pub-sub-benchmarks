package com.vertexinc.frameworks.pubsub;


import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import static java.nio.file.Files.readAllBytes;

public class FilePayloadReader implements PayloadReader {

    private final int expectedLength;

    public FilePayloadReader(int expectedLength) {
        this.expectedLength = expectedLength;
    }

    @Override
    public byte[] load(String resourceName) {
        byte[] payload;
        try {
            payload = readAllBytes(new File(resourceName).toPath());
            checkPayloadLength(payload);
            return payload;
        } catch (IOException e) {
            throw new PayloadException(e.getMessage());
        }
    }

    private void checkPayloadLength(byte[] payload) {
        if (expectedLength != payload.length) {
            throw new PayloadException(MessageFormat.format("Payload length mismatch. Actual is: {0}, but expected: {1} ",
                    payload.length, expectedLength));
        }
    }
}