package com.atixlabs.semillasmiddleware.app.model.credential.constants;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum CredentialCategoriesCodes {

    IDENTITY("Identidad"),
    DWELLING("Vivienda"),
    ENTREPRENEURSHIP("Emprendimiento"),
    BENEFIT("Beneficio Semillas"),
    CREDIT("Crediticia");

    private String code;

    CredentialCategoriesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    static final Map<String, CredentialCategoriesCodes> codeMap = Arrays.stream(values()).collect(Collectors.toMap(CredentialCategoriesCodes::getCode, p->p));
    public static CredentialCategoriesCodes getEnumByStringValue(String codeString) {
        return codeMap.get(codeString);
    }
}
