package com.alliander.equigy.client.oauth.sensitive;

import com.alliander.equigy.client.oauth.Constants;
import mjson.Json;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class SensitiveOperationsTest {

    @Test
    public void whenCreateBasicAuthorizationHeaderThenReturnValueForAuthorizationHeader() {
        final SensitiveBuffer basicAuthorizationHeader = SensitiveOperations.createBasicAuthorizationHeader(Constants.credentials());

        final String headerAsString = basicAuthorizationHeader.unsafeMap(buffer -> StandardCharsets.UTF_8.decode(buffer).toString());
        assertThat(headerAsString).isEqualTo("Basic ZGNkY2JhYmEtZmVmZS1mZWZlLWZlZmUtZmVkY2JhOTg3NjU0OnNlY3JldA==");
    }

    @Test
    public void whenCreatePasswordGrantTokenRequestThenReturnTokenRequestAsJson() {
        final SensitiveBuffer tokenRequest = SensitiveOperations.createPasswordGrantTokenRequest(Constants.credentials());

        final String tokenRequestAsString = tokenRequest.unsafeMap(buffer -> StandardCharsets.UTF_8.decode(buffer).toString());
        final Json tokenRequestAsJson = Json.read(tokenRequestAsString);
        assertThat(tokenRequestAsJson.at("grant_type").asString()).isEqualTo("password");
        assertThat(tokenRequestAsJson.at("username").asString()).isEqualTo("theUser");
        assertThat(tokenRequestAsJson.at("password").asString()).isEqualTo("thePassword");
    }
}
