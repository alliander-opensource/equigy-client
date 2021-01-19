/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import mjson.Json;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class ActivationPoint {
    private int position;
    private Instant instant;
    private double quantityInKwh;

    public ActivationPoint() {
    }

    public ActivationPoint(int position, Instant instant, double quantityInKwh) {
        this.position = position;
        this.instant = instant;
        this.quantityInKwh = quantityInKwh;
    }

    public ActivationPoint(Json json, Instant offset, Duration resolution) {
        position = json.at("position").asInteger();
        instant = offset.plus(resolution.multipliedBy(position - 1));
        quantityInKwh = json.at("quantity").asDouble();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public double getQuantityInKwh() {
        return quantityInKwh;
    }

    public void setQuantityInKwh(double quantityInKwh) {
        this.quantityInKwh = quantityInKwh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationPoint that = (ActivationPoint) o;
        return position == that.position &&
                Double.compare(that.quantityInKwh, quantityInKwh) == 0 &&
                Objects.equals(instant, that.instant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, instant, quantityInKwh);
    }

    @Override
    public String toString() {
        return "ActivationPoint{" +
                "position=" + position +
                ", instant=" + instant +
                ", quantityInKwh=" + quantityInKwh +
                '}';
    }
}
