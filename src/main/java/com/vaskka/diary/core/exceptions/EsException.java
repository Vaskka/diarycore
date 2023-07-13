package com.vaskka.diary.core.exceptions;

public class EsException extends BaseDiaryException {
    public EsException() {
    }

    public EsException(String message) {
        super(message);
    }

    public EsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsException(Throwable cause) {
        super(cause);
    }
}
