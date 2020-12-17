package com.alliander.equigy.client.oauth;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import mjson.Json;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OAuthClientTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(OffsetDateTime.parse("2020-12-01T14:00:00+01:00").toInstant(), ZoneId.of("Europe/Amsterdam"));
    private static final String AUTHORIZATION_HEADER = "Basic ZGNkY2JhYmEtZmVmZS1mZWZlLWZlZmUtZmVkY2JhOTg3NjU0OnNlY3JldA==";
    private static final String TOKEN_ENDPOINT = "/oauth/87654321-4321-4321-4321-aaaa87654321/token";
    private static final String TOKEN_RESPONSE = "{\"access_token\":\"accessMe\",\"refresh_token\":\"refreshMe\",\"id_token\":\"thisIsMe\",\"scope\":\"scope1 scope2\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
    private static final OAuthClient CLIENT = new OAuthClient(FIXED_CLOCK, URI.create("http://localhost:8072/oauth"));

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8072);

    @Test
    public void whenRetrieveTokenSuccessfulThenReturnOAuthToken() {
        stubFor(post(urlEqualTo(TOKEN_ENDPOINT))
                .withHeader("Authorization", equalTo(AUTHORIZATION_HEADER))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(TOKEN_RESPONSE)));

        final OAuthToken actualToken = CLIENT.retrieveToken(Constants.credentials());

        final OAuthToken expectedToken = new OAuthToken();
        expectedToken.setAccessToken("accessMe");
        expectedToken.setRefreshToken("refreshMe");
        expectedToken.setIdToken("thisIsMe");
        expectedToken.setScope(List.of("scope1", "scope2"));
        expectedToken.setTokenType("Bearer");
        expectedToken.setExpires(OffsetDateTime.parse("2020-12-01T15:00:00+01:00").toInstant());
        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @Test
    public void whenRetrieveTokenFailsWithConnectionResetByPeerThenThrowOAuthException() {
        stubFor(post(urlEqualTo(TOKEN_ENDPOINT))
                .willReturn(aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        assertThatThrownBy(() -> CLIENT.retrieveToken(Constants.credentials()))
                .isInstanceOf(OAuthException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    public void whenRetrieveTokenFailsWithUnexpectedStatusCodeThenThrowOAuthException() {
        stubFor(post(urlEqualTo(TOKEN_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "text/html; charset=utf-8")
                        .withBody("<html><head><title>Not Found</title><body><h1>Not Found</h1></body></html>")
                ));

        assertThatThrownBy(() -> CLIENT.retrieveToken(Constants.credentials()))
                .isInstanceOf(OAuthException.class)
                .hasMessage("Unexpected HTTP status code: 404");
    }

    @Test
    public void whenRetrieveTokenFailsWithUnexpectedContentTypeThenThrowOAuthException() {
        stubFor(post(urlEqualTo(TOKEN_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html; charset=utf-8")
                        .withBody("<html><head><title>Your Token</title><body><h1>Yeah, not in HTML!</h1></body></html>")));

        assertThatThrownBy(() -> CLIENT.retrieveToken(Constants.credentials()))
                .isInstanceOf(OAuthException.class)
                .hasMessage("Unexpected Content-Type in response: text/html;charset=utf-8");
    }

    @Test
    public void whenRetrieveTokenFailsWithMalformedJsonInBodyThenThrowOAuthException() {
        stubFor(post(urlEqualTo(TOKEN_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(TOKEN_RESPONSE.replace(',', '%'))));

        assertThatThrownBy(() -> CLIENT.retrieveToken(Constants.credentials()))
                .isInstanceOf(OAuthException.class)
                .hasCauseInstanceOf(Json.MalformedJsonException.class);
    }
}
