/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import mjson.Json;

import java.time.Instant;
import java.util.Objects;

public class DateTimePeriod {
    private Instant startDateTime;
    private Instant endDateTime;

    public DateTimePeriod() {
    }

    public DateTimePeriod(Instant startDateTime, Instant endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public DateTimePeriod(Json json) {
        startDateTime = Instant.ofEpochMilli(json.at("startDateTime").asLong());
        endDateTime = Instant.ofEpochMilli(json.at("endDateTime").asLong());
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimePeriod that = (DateTimePeriod) o;
        return Objects.equals(startDateTime, that.startDateTime) &&
                Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime);
    }

    @Override
    public String toString() {
        return "DateTimePeriod{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
