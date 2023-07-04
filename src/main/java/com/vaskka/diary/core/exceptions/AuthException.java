package com.vaskka.diary.core.exceptions;

public class AuthException extends BaseDiaryException {
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
