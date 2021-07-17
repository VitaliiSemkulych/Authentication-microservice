package com.webbook.webbook.exceptions.enrollExeption;


public class BadActivationCodeException extends RuntimeException{
    private final String activationCode;


    public BadActivationCodeException(String activationCode) {
        super(String.format("Activation code %s is not correct",activationCode));
        this.activationCode = activationCode;
    }
}
