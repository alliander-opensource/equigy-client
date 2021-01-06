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

public class ActivationTimeSeries {
    private String direction;
    private EAN18 accountingPointId;
    private List<ActivationSeriesPeriod> seriesPeriod;

    public ActivationTimeSeries() {
    }

    public ActivationTimeSeries(String direction, EAN18 accountingPointId, List<ActivationSeriesPeriod> seriesPeriod) {
        this.direction = direction;
        this.accountingPointId = accountingPointId;
        this.seriesPeriod = seriesPeriod;
    }

    public ActivationTimeSeries(Json json) {
        direction = json.at("direction").asString();
        accountingPointId = new EAN18(json.at("accountingPointId").asString());
        seriesPeriod = json.at("seriesPeriod").asJsonList().stream()
                .map(ActivationSeriesPeriod::new)
                .collect(Collectors.toList());
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public EAN18 getAccountingPointId() {
        return accountingPointId;
    }

    public void setAccountingPointId(EAN18 accountingPointId) {
        this.accountingPointId = accountingPointId;
    }

    public List<ActivationSeriesPeriod> getSeriesPeriod() {
        return seriesPeriod;
    }

    public void setSeriesPeriod(List<ActivationSeriesPeriod> seriesPeriod) {
        this.seriesPeriod = seriesPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationTimeSeries that = (ActivationTimeSeries) o;
        return Objects.equals(direction, that.direction) &&
                Objects.equals(accountingPointId, that.accountingPointId) &&
                Objects.equals(seriesPeriod, that.seriesPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, accountingPointId, seriesPeriod);
    }

    @Override
    public String toString() {
        return "ActivationTimeSeries{" +
                "direction='" + direction + '\'' +
                ", accountingPointId=" + accountingPointId +
                ", seriesPeriod=" + seriesPeriod +
                '}';
    }
}
