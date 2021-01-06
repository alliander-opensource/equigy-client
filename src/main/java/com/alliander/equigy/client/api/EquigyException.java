/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

public class EquigyException extends RuntimeException {
    EquigyException(Throwable cause) {
        super(cause);
    }

    EquigyException(String message) {
        super(message);
    }
}
