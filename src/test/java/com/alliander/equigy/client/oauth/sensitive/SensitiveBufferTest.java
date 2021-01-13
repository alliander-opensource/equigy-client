/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth.sensitive;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

public class SensitiveBufferTest {

    @Test
    public void whenUnsafeMappedThenMapperValueIsReturned() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 20).flip();

        final int out = new SensitiveBuffer(in).unsafeMap(b -> b.get() + 4);

        assertThat(out).isEqualTo(24);
    }

    @Test
    public void whenUnsafeMappedThenInputByteBufferIsCleared() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 20).flip();

        new SensitiveBuffer(in).unsafeMap(b -> b.get() + 4);

        assertThat(in.get(0)).isEqualTo((byte) 0);
    }

    @Test
    public void whenMappedThenMapperValueIsReturned() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 70).flip();

        final byte out = new SensitiveBuffer(in).map(b -> ByteBuffer.allocate(1).put((byte) (b.get() + 4)).flip())
                .unsafeMap(ByteBuffer::get);

        assertThat(out).isEqualTo((byte) 74);
    }

    @Test
    public void whenMappedThenInputByteBufferIsCleared() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 70).flip();

        new SensitiveBuffer(in).map(b -> ByteBuffer.allocate(1).put((byte) (b.get() + 4)));

        assertThat(in.get(0)).isEqualTo((byte) 0);
    }
}
