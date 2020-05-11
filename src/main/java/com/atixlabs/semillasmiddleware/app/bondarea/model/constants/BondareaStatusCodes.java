package com.atixlabs.semillasmiddleware.app.bondarea.model.constants;

public enum BondareaStatusCodes {

    ACTIVE("55"),
    FINALIZED("60");

    private String code;

    BondareaStatusCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
