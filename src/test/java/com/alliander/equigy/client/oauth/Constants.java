/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth;

import java.util.UUID;

public class Constants {

    private Constants() {
        // Prevent instantiation.
    }

    public static EquigyCredentials credentials() {
        final EquigyCredentials credentials = new EquigyCredentials();
        credentials.setTenantId(UUID.fromString("87654321-4321-4321-4321-aaaa87654321"));
        credentials.setOrganizationClientId(UUID.fromString("dcdcbaba-fefe-fefe-fefe-fedcba987654"));
        credentials.setOrganizationClientSecret(new char[] { 's', 'e', 'c', 'r', 'e', 't' });
        credentials.setUsername("theUser");
        credentials.setPassword(new char[] { 't', 'h', 'e', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd' });
        return credentials;
    }
}
