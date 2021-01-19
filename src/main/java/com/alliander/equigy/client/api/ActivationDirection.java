package com.alliander.equigy.client.api;

import java.util.stream.Stream;

public enum ActivationDirection {
    UPWARD("A01"), DOWNWARD("A02");

    private final String jsonValue;

    ActivationDirection(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    static ActivationDirection forJson(String jsonValue) {
        return Stream.of(ActivationDirection.values())
                .filter(direction -> direction.jsonValue.equals(jsonValue))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid direction: " + jsonValue));
    }
}
