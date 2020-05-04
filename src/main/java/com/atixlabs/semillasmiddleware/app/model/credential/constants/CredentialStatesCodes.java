package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatesCodes {

    CREDENTIAL_ACTIVE("Vigente"),
    //REDENTIAL_OUT_OF_DATE("Caducada"), -> va a ser una razon de revocacion
    CREDENTIAL_REVOKE("Revocada"),
    PENDING_DIDI("Pendiente-didi");
    //PENDING_BONDAREA("Pendiente-bondarea"),

    // Todo: Solicitud revocada cuando es pendiente bondarea -> que pasa en este estado ? dodne va ?

    private String code;

    CredentialStatesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
