package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialTypesCodes {

    CREDENTIAL_CREDIT("Crediticia"),
    CREDENTIAL_DWELLING("Vivienda"),
    CREDENTIAL_IDENTITY("Identidad - Titular"),
    CREDENTIAL_IDENTITY_FAMILY("Identidad - Familiar"),
    CREDENTIAL_ENTREPRENEURSHIP("Emprendimiento"),
    CREDENTIAL_BENEFITS("Beneficios Sembrando Titular"),
    CREDENTIAL_BENEFITS_FAMILY("Beneficios Sembrando Familiar");

    private String code;

    CredentialTypesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
