package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum BeneficiaryQuestion implements CategoryQuestion{

    NAME_AND_SURNAME("NOMBRE Y APELLIDO"),
    ID_NUMBER("NUMERO DE DOCUMENTO");

    private String pregunta;
    BeneficiaryQuestion(String pregunta) {
        this.pregunta = pregunta;
    }

    public boolean isRequired(){
        return true;
    }
}
