/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth.sensitive;

import com.alliander.equigy.client.oauth.EquigyCredentials;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SensitiveOperations {

    private static final byte[] BASIC_PREFIX = {'B', 'a', 's', 'i', 'c', ' '};

    private static final byte[] GRANT_PREFIX = "{\"grant_type\": \"password\",\"username\": \"".getBytes();
    private static final byte[] GRANT_INFIX = "\",\"password\":\"".getBytes();
    private static final byte[] GRANT_SUFFIX = "\"}".getBytes();

    private SensitiveOperations() {
        // PreventInstantiation
    }

    public static SensitiveBuffer createBasicAuthorizationHeader(EquigyCredentials credentials) {
        final ByteBuffer clientId = wrapAndEncode(credentials.getOrganizationClientId().toString());
        return wrapAndEncode(credentials.getOrganizationClientSecret())
                .map(clientSecret -> (ByteBuffer) ByteBuffer.allocate(clientId.remaining() + clientSecret.remaining() + 1)
                        .put(clientId)
                        .put((byte) ':')
                        .put(clientSecret)
                        .flip())
                .map(basicCredential -> Base64.getEncoder().encode(basicCredential))
                .map(encodedAuthorization -> (ByteBuffer) ByteBuffer.allocate(encodedAuthorization.remaining() + BASIC_PREFIX.length)
                        .put(BASIC_PREFIX)
                        .put(encodedAuthorization)
                        .flip());
    }

    public static SensitiveBuffer createPasswordGrantTokenRequest(EquigyCredentials credentials) {
        final ByteBuffer username = escapeAsJsonString(wrapAndEncode(credentials.getUsername()));
        return wrapAndEncode(credentials.getPassword())
                .map(SensitiveOperations::escapeAsJsonString)
                .map(password -> (ByteBuffer) ByteBuffer.allocate(username.remaining() + password.remaining() + GRANT_PREFIX.length + GRANT_INFIX.length + GRANT_SUFFIX.length)
                        .put(GRANT_PREFIX)
                        .put(username)
                        .put(GRANT_INFIX)
                        .put(password)
                        .put(GRANT_SUFFIX)
                        .flip());
    }

    private static ByteBuffer wrapAndEncode(String s) {
        return StandardCharsets.UTF_8.encode(CharBuffer.wrap(s));
    }

    private static SensitiveBuffer wrapAndEncode(char[] c) {
        return new SensitiveBuffer(StandardCharsets.UTF_8.encode(CharBuffer.wrap(c)));
    }

    private static ByteBuffer escapeAsJsonString(ByteBuffer in) {
        final ByteBuffer out = ByteBuffer.allocate(2 * in.remaining());
        while (in.hasRemaining()) {
            final byte b = in.get();
            if (b == '\\' || b == '"') {
                out.put((byte) '\\');
            }
            out.put(b);
        }
        return (ByteBuffer) out.flip();
    }
}
