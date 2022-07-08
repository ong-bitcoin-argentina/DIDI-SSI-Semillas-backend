package com.atixlabs.semillasmiddleware.app.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CredentialNotExistsException extends CredentialException {

    public CredentialNotExistsException(String s) {
        super(s);
    }
}
