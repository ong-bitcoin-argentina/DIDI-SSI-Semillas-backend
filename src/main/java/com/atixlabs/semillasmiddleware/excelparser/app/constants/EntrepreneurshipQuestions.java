package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;

public enum EntrepreneurshipQuestions{
    TYPE("TIPO DE EMPRENDIMIENTO"),
    ACTIVITY_START_DATE("FECHA DE INICIO / REINICIO"),
    MAIN_ACTIVITY("ACTIVIDAD PRINCIPAL"),
    NAME("NOMBRE EMPRENDIMIENTO"),
    ADDRESS("DIRECCION"),
    ACTIVITY_ENDING_DATE("FIN DE LA ACTIVIDAD");

    private String question;
    EntrepreneurshipQuestions(String question) {
        this.question = question;
    }
}
