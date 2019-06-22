package com.paraett.usersservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidUserTypeException extends RuntimeException {
    public InvalidUserTypeException(String message) {
        super(message);
    }
}
