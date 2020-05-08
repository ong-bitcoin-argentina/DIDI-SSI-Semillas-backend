package com.atixlabs.semillasmiddleware.app.model.credential.constants;

public enum CredentialCategoriesCodes {

    FINANCE("Finanzas"),
    LABOR("Laboral"),
    EDUCATION("Educación"),
    IDENTITY("Identidad"),
    ENTREPRENEURSHIP("Emprendimiento"),
    DWELLING("Vivienda"),
    BENEFIT("Beneficio Semillas");


    private String code;

    CredentialCategoriesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
