package com.atixlabs.semillasmiddleware.app.bondarea.model.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum LoanStatusCodes {

    ACTIVE("Active", "Al dia"), //this constant is used in LoanRepository for query!
    FINALIZED("Finalized", "Finalizado"),
    PENDING("Pending", "Pendiente"),
    CANCELLED("Cancelled", "Cancelado");


    private String code;
    private String description;

    LoanStatusCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Optional<LoanStatusCodes> getByCode(String code){
        return Arrays.stream(LoanStatusCodes.values())
                .filter( status_code -> status_code.getCode().equals(code))
                .findFirst();
    }
}
