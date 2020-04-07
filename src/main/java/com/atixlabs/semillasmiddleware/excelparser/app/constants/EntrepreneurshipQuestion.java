package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.time.LocalDate;

public enum EntrepreneurshipQuestion implements CategoryQuestion{
    TYPE("TIPO DE EMPRENDIMIENTO"),
    ACTIVITY_START_DATE("FECHA DE INICIO / REINICIO"),
    MAIN_ACTIVITY("ACTIVIDAD PRINCIPAL"),
    NAME("NOMBRE EMPRENDIMIENTO"),
    ADDRESS("DIRECCION"),
    ACTIVITY_ENDING_DATE("FIN DE LA ACTIVIDAD"){
        @Override
        public boolean isRequired() {
            return false;
        }
    };

    private String question;
    EntrepreneurshipQuestion(String question) {
        this.question = question;
    }

    public boolean isRequired() {
        return true;
    }
}
