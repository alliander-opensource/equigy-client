/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth;

import com.alliander.equigy.client.oauth.sensitive.SensitiveOperations;
import mjson.Json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;

public class OAuthClient {

    final private Clock clock;
    final private URI baseUri;

    public OAuthClient() {
        this.clock = Clock.systemDefaultZone();
        this.baseUri = URI.create("https://eu-de.appid.cloud.ibm.com/oauth/v4");
    }

    public OAuthClient(Clock clock, URI baseUri) {
        this.clock = clock;
        this.baseUri = baseUri;
    }

    public OAuthToken retrieveToken(EquigyCredentials credentials) throws OAuthException {
        final Instant retrievedAt = clock.instant();
        final String basicAuthorizationHeader = SensitiveOperations.createBasicAuthorizationHeader(credentials)
                .unsafeMap(header -> StandardCharsets.UTF_8.decode(header).toString());

        return SensitiveOperations.createPasswordGrantTokenRequest(credentials)
                .unsafeMap(tokenRequest -> {
                    try {
                        final HttpURLConnection connection = (HttpURLConnection) tokenUrlFor(credentials).openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setFixedLengthStreamingMode(tokenRequest.remaining());
                        connection.setRequestProperty("Authorization", basicAuthorizationHeader);
                        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                        try (final OutputStream outputStream = connection.getOutputStream()) {
                            outputStream.write(tokenRequest.array(), 0, tokenRequest.remaining());
                            outputStream.flush();
                        }

                        if (connection.getResponseCode() != 200) {
                            throw new OAuthException("Unexpected HTTP status code: " + connection.getResponseCode());
                        } else {
                            final String contentType = connection.getHeaderField("Content-Type");
                            if (contentType == null || !contentType.startsWith("application/json") || !contentType.contains("charset=utf-8")) {
                                throw new OAuthException("Unexpected Content-Type in response: " + contentType);
                            }
                        }

                        final StringBuilder data = new StringBuilder();
                        try (final InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                            final char[] buf = new char[1024];
                            for (int n = reader.read(buf); n > -1; n = reader.read(buf)) {
                                data.append(buf, 0, n);
                            }
                        }

                        return new OAuthToken(Json.read(data.toString()), retrievedAt);
                    } catch (IOException | Json.MalformedJsonException e) {
                        throw new OAuthException(e);
                    }
                });
    }

    private URL tokenUrlFor(EquigyCredentials credentials) throws MalformedURLException {
        return URI.create(baseUri.toString() + "/" + credentials.getTenantId().toString() + "/token").normalize().toURL();
    }

}
