package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum BeneficiaryQuestions {

    NAME_AND_SURNAME("NOMBRE Y APELLIDO"),
    ID_NUMBER("NUMERO DE DOCUMENTO");

    private String pregunta;
    BeneficiaryQuestions(String pregunta) {
        this.pregunta = pregunta;
    }
}
