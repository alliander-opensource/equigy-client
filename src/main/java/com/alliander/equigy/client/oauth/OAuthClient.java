package com.alliander.equigy.client.oauth;

import com.alliander.equigy.client.oauth.sensitive.SensitiveOperations;
import mjson.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        final HttpResponse<String> httpResponse = SensitiveOperations.createPasswordGrantTokenRequest(credentials)
                .mapAsBodyPublisher(tokenRequest -> {
                    final HttpRequest httpRequest = HttpRequest.newBuilder(tokenUriFor(credentials))
                            .header("Authorization", basicAuthorizationHeader)
                            .header("Content-Type", "application/json; charset=utf-8")
                            .POST(tokenRequest)
                            .build();

                    try {
                        return HttpClient.newHttpClient()
                                .send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                    } catch (IOException | InterruptedException e) {
                        throw new OAuthException(e);
                    }
                });

        if (httpResponse.statusCode() != 200) {
            throw new OAuthException("Unexpected HTTP status code: " + httpResponse.statusCode());
        } else {
            final String contentType = httpResponse.headers().firstValue("Content-Type").orElseThrow(() -> new OAuthException("Content-Type header missing in response"));
            if (!contentType.startsWith("application/json") || !contentType.contains("charset=utf-8")) {
                throw new OAuthException("Unexpected Content-Type in response: " + contentType);
            }
        }

        try {
            return new OAuthToken(Json.read(httpResponse.body()), retrievedAt);
        } catch (Json.MalformedJsonException e) {
            throw new OAuthException(e);
        }
    }

    private URI tokenUriFor(EquigyCredentials credentials) {
        return URI.create(baseUri.toString() + "/" + credentials.getTenantId().toString() + "/token");
    }

}
