package com.alliander.equigy.client.oauth;

public class OAuthException extends RuntimeException {
    OAuthException(Throwable cause) {
        super(cause);
    }

    OAuthException(String message) {
        super(message);
    }
}
