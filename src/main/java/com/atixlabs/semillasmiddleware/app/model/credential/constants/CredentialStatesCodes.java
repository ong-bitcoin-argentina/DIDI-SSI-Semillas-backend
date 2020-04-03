package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatesCodes {

    CREDENTIAL_PENDING("PENDIENTE"),
    CREDENTIAL_ACTIVE("VIGENTE"),
    CREDENTIAL_EXPIRED("VENCIDA"),
    CREDENTIAL_OUT_OF_DATE("CADUCADA"),
    CREDENTIAL_REVOKE("REVOCADA");

    private String code;

    CredentialStatesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
