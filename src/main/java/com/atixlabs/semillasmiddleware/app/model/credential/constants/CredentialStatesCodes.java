package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatesCodes {

    CREDENTIAL_PENDING("Pendiente"),
    CREDENTIAL_ACTIVE("Vigente"),
    CREDENTIAL_EXPIRED("Vencida"),
    CREDENTIAL_OUT_OF_DATE("Caducada"),
    CREDENTIAL_REVOKE("Revocada");

    private String code;

    CredentialStatesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
