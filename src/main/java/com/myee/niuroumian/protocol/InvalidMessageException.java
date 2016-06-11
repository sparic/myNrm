package com.myee.niuroumian.protocol;

public class InvalidMessageException extends RuntimeException {
    private static final long serialVersionUID = -6417575885268808920L;

    public InvalidMessageException() {
    }

    public InvalidMessageException(String message) {
        super(message);
    }

    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMessageException(Throwable cause) {
        super(cause);
    }

}
