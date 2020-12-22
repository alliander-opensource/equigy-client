package com.alliander.equigy.client.api;

import com.alliander.equigy.client.oauth.OAuthToken;
import mjson.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        final HttpRequest httpRequest = HttpRequest.newBuilder(getByQueryUri(accountingPointId, queryDate))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            final HttpResponse<String> httpResponse = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (httpResponse.statusCode() != 200) {
                throw new EquigyException("Unexpected HTTP status code: " + httpResponse.statusCode());
            } else {
                final String contentType = httpResponse.headers().firstValue("Content-Type").orElseThrow(() -> new EquigyException("Content-Type header missing in response"));
                if (!contentType.startsWith("application/json") || !contentType.contains("charset=utf-8")) {
                    throw new EquigyException("Unexpected Content-Type in response: " + contentType);
                }
            }

            return Json.read(httpResponse.body()).asJsonList().stream()
                    .map(ActivatedEnergy::new)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException | NullPointerException | Json.MalformedJsonException e) {
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

    private URI getByQueryUri(EAN18 accountingPointId, LocalDate queryDate) {
        // Querystring parameters don't need encoding, since EAN18 and LocalDate only contain characters 0-9 and '-'
        return URI.create(baseUri.toString() + "/activatedEnergy/getByQuery"
                + "?accountingPointId=" + accountingPointId.toString()
                + "&queryDate=" + queryDate.toString()
        );
    }

    public static ActivatedEnergyClient acceptance() {
        return new ActivatedEnergyClient(URI.create("https://activated-energy-querying-tennet.cbp-acceptance-tennet.ams03.containers.appdomain.cloud/api"));
    }
}
