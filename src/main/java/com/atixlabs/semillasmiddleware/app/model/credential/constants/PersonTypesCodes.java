package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum PersonTypesCodes {

    HOLDER("TITULAR"),
    FAMILY("FAMILIAR");

    private String code;

    PersonTypesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
