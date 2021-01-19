/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import com.alliander.equigy.client.oauth.OAuthToken;
import mjson.Json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ActivatedEnergyClient {

    final private URI baseUri;

    public ActivatedEnergyClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    public List<ActivatedEnergy> getByQuery(EAN18 accountingPointId, LocalDate queryDate, String bearerToken) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) getByQueryUrl(accountingPointId, queryDate).openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new EquigyException("Unexpected HTTP status code: " + connection.getResponseCode());
            } else {
                final String contentType = connection.getHeaderField("Content-Type");
                if (contentType == null || !contentType.startsWith("application/json") || !contentType.contains("charset=utf-8")) {
                    throw new EquigyException("Unexpected Content-Type in response: " + contentType);
                }
            }

            final StringBuilder data = new StringBuilder();
            try (final InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                final char[] buf = new char[1024];
                for (int n = reader.read(buf); n > -1; n = reader.read(buf)) {
                    data.append(buf, 0, n);
                }
            }

            return Json.read(data.toString()).asJsonList().stream()
                    .map(ActivatedEnergy::new)
                    .collect(Collectors.toList());
        } catch (IOException | NullPointerException | Json.MalformedJsonException e) {
            throw new EquigyException(e);
        }
    }

    public List<ActivatedEnergy> getByQuery(EAN18 accountingPointId, LocalDate queryDate, OAuthToken token) {
        if (!token.getTokenType().equals("Bearer")) {
            throw new EquigyException("OAuth token of unknown token type");
        }
        if (token.getExpires().isBefore(Instant.now())) {
            throw new EquigyException("OAuth token has expired");
        }
        return getByQuery(accountingPointId, queryDate, token.getAccessToken());
    }

    private URL getByQueryUrl(EAN18 accountingPointId, LocalDate queryDate) throws MalformedURLException {
        // Querystring parameters don't need encoding, since EAN18 and LocalDate only contain characters 0-9 and '-'
        return URI.create(baseUri.toString() + "/activatedEnergy/getByQuery"
                + "?accountingPointId=" + accountingPointId.toString()
                + "&queryDate=" + queryDate.toString()
        ).normalize().toURL();
    }

    public static ActivatedEnergyClient acceptance() {
        return new ActivatedEnergyClient(URI.create("https://activated-energy-querying-tennet.cbp-acceptance-tennet.ams03.containers.appdomain.cloud/api"));
    }
}
