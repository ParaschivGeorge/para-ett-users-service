package com.paraett.usersservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ActivationCodeInvalidException extends RuntimeException {
    public ActivationCodeInvalidException(String message) {
        super(message);
    }
}
