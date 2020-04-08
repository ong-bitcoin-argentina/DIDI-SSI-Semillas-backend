package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatesCodes {

    //CREDENTIAL_PENDING_BONDAREA("Pendiente-bondarea"),
    //CREDENTIAL_PENDING_DIDI("Pendiente-didi"),
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
