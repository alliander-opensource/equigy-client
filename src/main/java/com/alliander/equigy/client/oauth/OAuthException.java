/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.oauth;

public class OAuthException extends RuntimeException {
    OAuthException(Throwable cause) {
        super(cause);
    }

    OAuthException(String message) {
        super(message);
    }
}
