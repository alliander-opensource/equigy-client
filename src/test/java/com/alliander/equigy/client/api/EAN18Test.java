/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EAN18Test {

    @Test
    public void whenCreatingWithValidEan18ThenOk() {
        final EAN18 ean = new EAN18("130539057492609625");

        assertThat(ean.toString()).isEqualTo("130539057492609625");
    }

    @Test
    public void whenCreatingWithNullThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new EAN18(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenCreatingWithEan13ThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new EAN18("6599347744172"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenCreatingWithEan18WithInvalidChecksumThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new EAN18("130539057492609627"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
