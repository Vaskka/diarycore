package com.vaskka.diary.core.exceptions;

public class AuthorNotExistException extends BaseDiaryException {
    public AuthorNotExistException() {
    }

    public AuthorNotExistException(String message) {
        super(message);
    }

    public AuthorNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorNotExistException(Throwable cause) {
        super(cause);
    }
}
