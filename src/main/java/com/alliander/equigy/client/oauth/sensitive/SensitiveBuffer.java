/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth.sensitive;

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
