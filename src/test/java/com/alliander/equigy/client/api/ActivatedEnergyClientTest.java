/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import com.alliander.equigy.client.oauth.OAuthToken;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import mjson.Json;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ActivatedEnergyClientTest {

    private static final String GET_BY_QUERY_ENDPOINT = "/api/activatedEnergy/getByQuery";
    private static final String TEST_EAN18 = "130539057492609625";
    private static final String ACCESS_TOKEN = "accessMe";
    private static final ActivatedEnergyClient CLIENT = new ActivatedEnergyClient(URI.create("http://localhost:8072/api"));

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8072);

    @Test
    public void whenGetByQueryWithBearerTokenSuccessfulThenReturnActivatedEnergy() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .withHeader("Authorization", equalTo("Bearer accessMe"))
                .withQueryParam("accountingPointId", equalTo(TEST_EAN18))
                .withQueryParam("queryDate", equalTo("2020-12-20"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(readTestFile("response01.json"))));

        final List<ActivatedEnergy> activatedEnergies = CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), ACCESS_TOKEN);

        assertThat(activatedEnergies).isEqualTo(expectedActivatedEnergies());
    }

    @Test
    public void whenGetByQueryWithOAuthTokenSuccessfulThenReturnActivatedEnergy() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .withHeader("Authorization", equalTo("Bearer " + ACCESS_TOKEN))
                .withQueryParam("accountingPointId", equalTo(TEST_EAN18))
                .withQueryParam("queryDate", equalTo("2020-12-20"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(readTestFile("response01.json"))));

        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken(ACCESS_TOKEN);
        oAuthToken.setRefreshToken("refreshMe");
        oAuthToken.setIdToken("thisIsMe");
        oAuthToken.setScope(Arrays.asList("scope1", "scope2"));
        oAuthToken.setTokenType("Bearer");
        oAuthToken.setExpires(OffsetDateTime.now().plusMinutes(5).toInstant());

        final List<ActivatedEnergy> activatedEnergies = CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), oAuthToken);

        assertThat(activatedEnergies).isEqualTo(expectedActivatedEnergies());
    }

    @Test
    public void whenGetByQueryWithNonBearerTokenTypeThenThrowEquigyException() {
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken(ACCESS_TOKEN);
        oAuthToken.setRefreshToken("refreshMe");
        oAuthToken.setIdToken("thisIsMe");
        oAuthToken.setScope(Arrays.asList("scope1", "scope2"));
        oAuthToken.setTokenType("Unknown");
        oAuthToken.setExpires(OffsetDateTime.now().plusMinutes(5).toInstant());

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), oAuthToken))
                .isInstanceOf(EquigyException.class)
                .hasMessage("OAuth token of unknown token type");
    }

    @Test
    public void whenGetByQueryWithExpiredOAuthTokenThenThrowEquigyException() {
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken(ACCESS_TOKEN);
        oAuthToken.setRefreshToken("refreshMe");
        oAuthToken.setIdToken("thisIsMe");
        oAuthToken.setScope(Arrays.asList("scope1", "scope2"));
        oAuthToken.setTokenType("Bearer");
        oAuthToken.setExpires(OffsetDateTime.now().minusMinutes(5).toInstant());

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), oAuthToken))
                .isInstanceOf(EquigyException.class)
                .hasMessage("OAuth token has expired");
    }

    @Test
    public void whenGetByQueryFailsWithConnectionResetByPeerThenThrowEquigyException() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .willReturn(aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), ACCESS_TOKEN))
                .isInstanceOf(EquigyException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    public void whenGetByQueryFailsWithUnexpectedStatusCodeThenThrowEquigyException() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "text/html; charset=utf-8")
                        .withBody("<html><head><title>Not Found</title><body><h1>Not Found</h1></body></html>")
                ));

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), ACCESS_TOKEN))
                .isInstanceOf(EquigyException.class)
                .hasMessage("Unexpected HTTP status code: 404");
    }

    @Test
    public void whenGetByQueryFailsWithUnexpectedContentTypeThenThrowEquigyException() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html; charset=utf-8")
                        .withBody("<html><head><title>Your Token</title><body><h1>Yeah, not in HTML!</h1></body></html>")));

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), ACCESS_TOKEN))
                .isInstanceOf(EquigyException.class)
                .hasMessage("Unexpected Content-Type in response: text/html;charset=utf-8");
    }

    @Test
    public void whenGetByQueryFailsWithMalformedJsonInBodyThenThrowEquigyException() {
        stubFor(get(urlPathEqualTo(GET_BY_QUERY_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(readTestFile("response01.json").replace(',', '%'))));

        assertThatThrownBy(() -> CLIENT.getByQuery(new EAN18(TEST_EAN18), LocalDate.of(2020, 12, 20), ACCESS_TOKEN))
                .isInstanceOf(EquigyException.class)
                .hasCauseInstanceOf(Json.MalformedJsonException.class);
    }

    private String readTestFile(String filename) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = classLoader.getResourceAsStream(ActivatedEnergyClientTest.class.getPackage().getName().replaceAll("\\.", File.separator) + File.separator + filename);
        return new Scanner(stream).useDelimiter("\\A").next();
    }

    private static List<ActivatedEnergy> expectedActivatedEnergies() {
        return Collections.singletonList(
                new ActivatedEnergy(
                        Collections.singletonList(
                                new ActivationTimeSeries(
                                        ActivationDirection.UPWARD,
                                        new EAN18(TEST_EAN18),
                                        Collections.singletonList(
                                                new ActivationSeriesPeriod(
                                                        new DateTimePeriod(
                                                                OffsetDateTime.of(2020, 12, 20, 23, 0, 0, 0, ZoneOffset.UTC).toInstant(),
                                                                OffsetDateTime.of(2020, 12, 21, 23, 0, 0, 0, ZoneOffset.UTC).toInstant()
                                                        ),
                                                        Duration.ofMinutes(5),
                                                        Collections.singletonList(
                                                                new ActivationPoint(
                                                                        10,
                                                                        OffsetDateTime.of(2020, 12, 20, 23, 45, 0, 0, ZoneOffset.UTC).toInstant(),
                                                                        5.7d
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
