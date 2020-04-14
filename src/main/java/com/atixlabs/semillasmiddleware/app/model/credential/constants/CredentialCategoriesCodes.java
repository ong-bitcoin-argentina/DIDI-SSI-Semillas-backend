package com.atixlabs.semillasmiddleware.app.model.credential.constants;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialAcademic;

public enum CredentialTypesCodes {

    CREDENTIAL_CREDIT("Creditos Semillas"),
    CREDENTIAL_DWELLING("Credito Viviendo"),
    CREDENTIAL_IDENTITY("Identidad - Titular"),
    CREDENTIAL_IDENTITY_FAMILIAR("Identidad - Familiar"),
    CREDENTIAL_ENTREPRENEURSHIP("Credito Emprendedor"),
    CREDENTIAL_ACADEMIC_CREDIT("Credito Escolar"),
    CREDENTIAL_OPORTUNITY("Sembrando Oportunidades"),
    CREDENTIAL_OPORTUNITY_FAMILIAR("Sembrando Oportunidades Familiar"),
    CREDENTIAL_HEALTH("Sembrando Salud Titular"),
    CREDENTIAL_HEALTH_FAMILIA("Sembrando Salud Familiar"),
    CREDENTIAL_ACADEMIC("Sembrando Saberes Titular"),
    CREDENTIAL_ACADEMIC_FAMILIAR("Sembrando Saberes Familiar"),
    CREDENTIAL_PERSONAL_DATA("Datos Personales Titular"),
    CREDENTIAL_PERSONAL_DATA_FAMILIAR("Datos Personales Familiar"),
    CREDENTIAL_TRAINING("Capacitacion Titular"),
    CREDENTIAL_TRAINING_FAMILIAR("Capacitación Familiar");

    private String code;

    CredentialTypesCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
