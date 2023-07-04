package com.vaskka.diary.core.exceptions;

public class UserCenterException extends BaseDiaryException {
    public UserCenterException() {
    }

    public UserCenterException(String message) {
        super(message);
    }

    public UserCenterException(String message, Throwable cause) {
        super(message, cause);
    }
}
