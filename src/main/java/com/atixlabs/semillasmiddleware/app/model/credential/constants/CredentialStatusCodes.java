package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatusCodes {

    CREDENTIAL_PENDING_BONDAREA("Pendiente-bondarea"),
    CREDENTIAL_PENDING_DIDI("Pendiente-didi");
    //CREDENTIAL_PENDING_SURVEY("Pendiente-encuesta");

    // Todo: Solicitud revocada cuando es pendiente bondarea -> que pasa en este estado ? dodne va ?

    private String code;

    CredentialStatusCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
