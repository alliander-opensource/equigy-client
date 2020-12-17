package com.alliander.equigy.client.oauth.sensitive;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.function.Function;

public class SensitiveBuffer {
    private final ByteBuffer buffer;

    SensitiveBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public SensitiveBuffer map(Function<ByteBuffer, ByteBuffer> mapper) {
        try {
            return new SensitiveBuffer(mapper.apply(buffer));
        } finally {
            clearByteBuffer(buffer);
        }
    }

    public <T> T mapAsBodyPublisher(Function<HttpRequest.BodyPublisher, T> mapper) {
        try {
            return mapper.apply(new SensitiveBodyPublisher(buffer));
        } finally {
            clearByteBuffer(buffer);
        }
    }

    public <T> T unsafeMap(Function<ByteBuffer, T> mapper) {
        try {
            return mapper.apply(buffer);
        } finally {
            clearByteBuffer(buffer);
        }
    }

    private static void clearByteBuffer(ByteBuffer buffer) {
        buffer.clear();
        while (buffer.hasRemaining()) {
            buffer.put((byte) 0);
        }
    }
}
