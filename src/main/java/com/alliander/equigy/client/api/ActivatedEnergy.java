/*
SPDX-License-Identifier: MPL-2.0
Copyright 2020 Alliander N.V.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.alliander.equigy.client.api;

import mjson.Json;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ActivatedEnergy {
    private List<ActivationTimeSeries> timeSeries;

    public ActivatedEnergy() {
    }

    public ActivatedEnergy(List<ActivationTimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public ActivatedEnergy(Json json) {
        timeSeries = json.at("timeSeries").asJsonList().stream()
                .map(ActivationTimeSeries::new)
                .collect(Collectors.toList());
    }

    public List<ActivationTimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<ActivationTimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivatedEnergy that = (ActivatedEnergy) o;
        return Objects.equals(timeSeries, that.timeSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeSeries);
    }

    @Override
    public String toString() {
        return "ActivatedEnergy{" +
                "timeSeries=" + timeSeries +
                '}';
    }
}
