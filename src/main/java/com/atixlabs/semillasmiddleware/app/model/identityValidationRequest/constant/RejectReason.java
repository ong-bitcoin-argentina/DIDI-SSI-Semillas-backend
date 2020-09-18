package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant;

import lombok.Getter;

@Getter
public enum RejectReason {
    INCONSISTENT_DATA("Datos Inconsistentes"),
    NOT_BENEFICIARY("No es Beneficiario de Semillas"),
    IMPOSSIBLE_TO_COMMUNICATE("No es posible comunicarse");

    private String description;

    RejectReason(String description){
        this.description = description;
    }

}
