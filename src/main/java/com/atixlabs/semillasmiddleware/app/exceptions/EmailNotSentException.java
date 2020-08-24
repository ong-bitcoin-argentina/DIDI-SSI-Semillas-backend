package com.atixlabs.semillasmiddleware.app.exceptions;

public class EmailNotSentException extends RuntimeException {

    public EmailNotSentException(String message){
        super(message);
    }
}
