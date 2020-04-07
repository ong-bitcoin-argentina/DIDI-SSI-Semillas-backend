package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialTypesCodes {

    CREDENTIAL_CREDIT("Creditos Semillas"),
    CREDENTIAL_DWELLING("Credito Viviendo"),
    CREDENTIAL_IDENTITY("Identidad - Titular"),
    CREDENTIAL_ENTREPRENEURSHIP("Credito Emprendedor"),
    CREDENTIAL_ACADEMIC("Credito Escolar");

    private String code;

    CredentialTypesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
