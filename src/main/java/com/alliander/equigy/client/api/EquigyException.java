package com.alliander.equigy.client.api;

public class EquigyException extends RuntimeException {
    EquigyException(Throwable cause) {
        super(cause);
    }

    EquigyException(String message) {
        super(message);
    }
}
