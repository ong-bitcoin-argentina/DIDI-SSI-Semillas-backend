package com.atixlabs.semillasmiddleware.app.bondarea.model.constants;

public enum BondareaLoanStatusCodes {

    ACTIVE("55"),
    FINALIZED("60");

    private String code;

    BondareaLoanStatusCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
