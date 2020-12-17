package com.alliander.equigy.client.oauth;

import mjson.Json;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OAuthToken {
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private List<String> scope;
    private String tokenType;
    private Instant expires;

    public OAuthToken() {
    }

    public OAuthToken(Json json) {
        this(json, Instant.now());
    }

    public OAuthToken(Json json, Instant retrievedAt) {
        accessToken = json.at("access_token").asString();
        refreshToken = json.at("refresh_token").asString();
        scope = parseScope(json.at("scope").asString());
        idToken = json.at("id_token").asString();
        tokenType = json.at("token_type").asString();
        expires = retrievedAt.plusSeconds(json.at("expires_in").asInteger());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuthToken that = (OAuthToken) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(refreshToken, that.refreshToken) &&
                Objects.equals(idToken, that.idToken) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(tokenType, that.tokenType) &&
                Objects.equals(expires, that.expires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken, idToken, scope, tokenType, expires);
    }

    @Override
    public String toString() {
        return "OAuthToken{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", idToken='" + idToken + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expires=" + expires +
                '}';
    }

    private static List<String> parseScope(String scope) {
        if (scope == null || scope.isEmpty()) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(scope.split(" "));
        }
    }
}
