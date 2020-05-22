package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialStatesCodes {
    //IMPORTANT!! This keys and values are important to the front. For ex. "Credential_Active" is the key to get the value. In front this key is in a constant to use the filters.
    //be careful on any change here.

    CREDENTIAL_ACTIVE("Vigente"),
    CREDENTIAL_REVOKE("Revocada"),
    PENDING_DIDI("Pendiente-didi");


    private String code;

    CredentialStatesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
