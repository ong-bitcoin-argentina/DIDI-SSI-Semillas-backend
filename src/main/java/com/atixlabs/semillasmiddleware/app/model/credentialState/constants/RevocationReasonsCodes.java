package com.atixlabs.semillasmiddleware.app.model.credentialState.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RevocationReasonsCodes {

    //this reasons are internal reasons
    UPDATE_INTERNAL("UPDATE", "OTHER"),
    CANCELLED("CANCELLED", "OTHER"),
    DEFAULT("DEFAULT", "OTHER"),
    //----
    EXPIRED_INFO("Expiracion de datos", "EXPIRATION"),
    UNLINKING("Desvinculacion", "UNLINKING");


    private String code;
    private String didiCode;


    RevocationReasonsCodes(String code, String didiCode) {
        this.code = code;
        this.didiCode = didiCode;
    }

    public static Optional<RevocationReasonsCodes> getByCode(String code){
        return Arrays.stream(RevocationReasonsCodes.values())
                .filter( status_code -> status_code.getCode().equals(code))
                .findFirst();
    }

    public String getCode() {
        return this.code;
    }
    public String getDidiCode() { return this.didiCode; }

}
