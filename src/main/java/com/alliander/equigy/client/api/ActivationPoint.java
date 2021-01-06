/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import mjson.Json;

import java.util.Objects;

public class ActivationPoint {
    private int position;
    private int quantity;

    public ActivationPoint() {
    }

    public ActivationPoint(int position, int quantity) {
        this.position = position;
        this.quantity = quantity;
    }

    public ActivationPoint(Json json) {
        position = json.at("position").asInteger();
        quantity = json.at("quantity").asInteger();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationPoint that = (ActivationPoint) o;
        return position == that.position &&
                quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, quantity);
    }

    @Override
    public String toString() {
        return "ActivationPoint{" +
                "position=" + position +
                ", quantity=" + quantity +
                '}';
    }
}
