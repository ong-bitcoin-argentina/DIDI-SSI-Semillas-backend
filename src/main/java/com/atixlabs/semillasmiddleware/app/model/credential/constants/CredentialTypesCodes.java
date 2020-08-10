package com.atixlabs.semillasmiddleware.app.model.credential.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum CredentialTypesCodes {

    CREDENTIAL_CREDIT("Crediticia"),
    CREDENTIAL_DWELLING("Vivienda"),
    CREDENTIAL_IDENTITY("Identidad - Titular"),
    CREDENTIAL_IDENTITY_FAMILY("Identidad - Familiar"),
    CREDENTIAL_ENTREPRENEURSHIP("Emprendimiento"),
    CREDENTIAL_BENEFITS("Beneficios Sembrando Titular"),
    CREDENTIAL_BENEFITS_FAMILY("Beneficios Sembrando Familiar"),
    CREDENTIAL_BENEFITS_SANCOR("Sancor Salud");

    private String code;

    CredentialTypesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    static final Map<String, CredentialTypesCodes> codeMap = Arrays.stream(values()).collect(Collectors.toMap(CredentialTypesCodes::getCode, p->p));

    public static CredentialTypesCodes getEnumByStringValue(String codeString) {
        return codeMap.get(codeString);
    }
}
