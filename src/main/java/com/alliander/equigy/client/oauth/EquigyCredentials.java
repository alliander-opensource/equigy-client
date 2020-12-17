package com.alliander.equigy.client.oauth;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class EquigyCredentials {
    private UUID tenantId;
    private UUID organizationClientId;
    private char[] organizationClientSecret;
    private String username;
    private char[] password;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getOrganizationClientId() {
        return organizationClientId;
    }

    public void setOrganizationClientId(UUID organizationClientId) {
        this.organizationClientId = organizationClientId;
    }

    public char[] getOrganizationClientSecret() {
        return organizationClientSecret;
    }

    public void setOrganizationClientSecret(char[] organizationClientSecret) {
        this.organizationClientSecret = organizationClientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquigyCredentials that = (EquigyCredentials) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(organizationClientId, that.organizationClientId) &&
                Arrays.equals(organizationClientSecret, that.organizationClientSecret) &&
                Objects.equals(username, that.username) &&
                Arrays.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tenantId, organizationClientId, username);
        result = 31 * result + Arrays.hashCode(organizationClientSecret);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }

    @Override
    public String toString() {
        return "EquigyCredentials{" +
                "tenantId=" + tenantId +
                ", organizationClientId=" + organizationClientId +
                ", username='" + username + '\'' +
                '}';
    }
}
