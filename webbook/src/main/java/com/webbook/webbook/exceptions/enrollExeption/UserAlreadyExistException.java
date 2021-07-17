package com.webbook.webbook.exceptions.enrollExeption;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
