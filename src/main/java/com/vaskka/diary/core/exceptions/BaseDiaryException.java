package com.vaskka.diary.core.exceptions;

public class BaseDiaryException extends RuntimeException {
    public BaseDiaryException() {
    }

    public BaseDiaryException(String message) {
        super(message);
    }

    public BaseDiaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseDiaryException(Throwable cause) {
        super(cause);
    }
}
