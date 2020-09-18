package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum FamiliarFinanceQuestion implements CategoryQuestion {

    //Headers
    MONTHLY_ENTRIES("INGRESOS MENSUALES"),
    MONTHLY_EXITS("EGRESOS MENSUALES"),
        //Questions
    ENTRY_ENTREPRENEURSHIP("INGRESOS - EMPRENDIMIENTO"),
    ENTRY_APPLICANT("INGRESOS - SOLICITANTE"),
    ENTRY_FAMILY("INGRESOS - FAMILIARES"),
    TOTAL_MONTHLY_ENTRY("+ TOTAL INGRESOS MENSUALES"),
    FAMILIAR_SURPLUS_FORTNIGHT("EXCEDENTE - FAMILIAR QUINCENAL"),
    EXIT_FEEDING("EGRESOS - ALIMENTACION"),
    EXIT_GAS("EGRESOS - GAS"),
    EXIT_EDUCATION("EGRESOS - EDUCACION"),
    EXIT_TRANSPORT("EGRESOS - TRANSPORTE"),
    EXIT_WATER("EGRESOS - AGUA"),
    EXIT_ELECTRICITY("EGRESOS - LUZ"),
    EXIT_PHONE("EGRESOS - TELEFONO"),
    EXIT_FIT("EGRESOS - SALUD"),
    EXIT_TAXES("EGRESOS - IMPUESTOS"),
    EXIT_CLOTHING("EGRESOS - VESTIMENTA"),
    EXIT_RENT("EGRESOS - ALQUILER"),
    EXIT_OIL("EGRESOS - COMBUSTIBLE"),
    EXIT_CREDITS("EGRESOS - CREDITOS EXISTENTES"),
    EXIT_LEISURE("EGRESOS - ESPARCIMIENTO"),
    EXIT_GAMBLING("EGRESOS - APUESTAS JUEGO"),
    EXIT_TV("EGRESOS - CABLE"),
    EXIT_INTERNET("EGRESOS - WIFI"),
    EXIT_OTHERS("EGRESOS - OTROS"),
    TOTAL_MONTHLY_EXIT("+ TOTAL EGRESOS MENSUALES"),
    TOTAL_MONTHLY_ENTRY_2("TOTAL INGRESOS MENSUALES"),
    TOTAL_MONTHLY_EXIT_2("TOTAL EGRESOS MENSUALES"),
    TOTAL_MONTHLY_FAMILIAR_SURPLUS("+ TOTAL EXCEDENTE FAMILIAR MENSUAL");


    private String questionName;
    static final Map<String, FamiliarFinanceQuestion> questionsMap = Arrays.stream(values()).collect(Collectors.toMap(FamiliarFinanceQuestion::getQuestionName, p->p));

    FamiliarFinanceQuestion(String questionName) { this.questionName = questionName; }

    public static FamiliarFinanceQuestion getEnumByStringValue(String questionName){
        return questionsMap.get(questionName);
    }

    public String getQuestionName(){
        return this.questionName;
    }

    public boolean isRequired() {
        return true;
    }

    public Class<?> getDataType() {
        return String.class;
    }
}

