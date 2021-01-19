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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ActivationSeriesPeriod {
    private DateTimePeriod interval;
    private Duration resolution;
    private List<ActivationPoint> point;

    public ActivationSeriesPeriod() {
    }

    public ActivationSeriesPeriod(DateTimePeriod interval, Duration resolution, List<ActivationPoint> point) {
        this.interval = interval;
        this.resolution = resolution;
        this.point = point;
    }

    public ActivationSeriesPeriod(Json json) {
        interval = new DateTimePeriod(json.at("interval"));
        resolution = Duration.parse(json.at("resolution").asString());
        point = json.at("point").asJsonList().stream()
                .map(pointJson -> new ActivationPoint(pointJson, interval.getStartDateTime(), resolution))
                .collect(Collectors.toList());
    }

    public DateTimePeriod getInterval() {
        return interval;
    }

    public void setInterval(DateTimePeriod interval) {
        this.interval = interval;
    }

    public Duration getResolution() {
        return resolution;
    }

    public void setResolution(Duration resolution) {
        this.resolution = resolution;
    }

    public List<ActivationPoint> getPoint() {
        return point;
    }

    public void setPoint(List<ActivationPoint> point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationSeriesPeriod that = (ActivationSeriesPeriod) o;
        return Objects.equals(interval, that.interval) &&
                Objects.equals(resolution, that.resolution) &&
                Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interval, resolution, point);
    }

    @Override
    public String toString() {
        return "ActivationSeriesPeriod{" +
                "interval=" + interval +
                ", resolution=" + resolution +
                ", point=" + point +
                '}';
    }
}
