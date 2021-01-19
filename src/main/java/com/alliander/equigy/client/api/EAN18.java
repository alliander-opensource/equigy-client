/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import java.util.Objects;

public class EAN18 {
    private final String ean;

    public EAN18(String ean) {
        if (ean == null || ean.length() != 18) {
            throw new IllegalArgumentException("Invalid EAN");
        }

        int checksum = 0;
        final char[] chars = ean.toCharArray();
        for (int i = 0; i < 9; i++) {
            checksum += 3 * (chars[2 * i] - '0') + (chars[2 * i + 1] - '0');
        }

        if (checksum % 10 != 0) {
            throw new IllegalArgumentException("Invalid EAN");
        }

        this.ean = ean;
    }

    public String getEan() {
        return ean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EAN18 ean18 = (EAN18) o;
        return ean.equals(ean18.ean);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ean);
    }

    @Override
    public String toString() {
        return ean;
    }
}
