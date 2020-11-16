package com.atixlabs.semillasmiddleware.app.exceptions;

public class DidiEmmitCredentialException extends RuntimeException {
    public DidiEmmitCredentialException(String s) {
        super(s);
    }
    public DidiEmmitCredentialException(Exception e) {
        super(e);
    }
}
