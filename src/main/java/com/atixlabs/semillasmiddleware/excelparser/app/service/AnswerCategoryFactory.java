package com.atixlabs.semillasmiddleware.excelparser.app.service;

public class AnswerCategoryFactory {

    public String create(String category){
        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)
        if (category == null)
            return "CATEGORY NULL";

        category = category.toUpperCase().replaceAll("[0123456789]", "").trim();

        switch (category) {
            case "DATOS DEL BENEFICIARIO":
            case "DATOS DEL CONYUGE":
            case "DATOS ENTREVISTA":
            case "DATOS HIJO":
            case "EMPRENDIMIENTO":
            case "FINANZAS FAMILIARES":
            case "FOTOS ADICIONALES":
            case "INGRESOS SOLICITANTE":
            case "OTRO MIEMBRO DE LA FAMILIA":
            case "OTROS INGRESOS FAMILIARES":
            case "SITUACION PATRIMONIAL":
            case "VIVIENDA":
                return category;
            default:
                //log.info("CATEGORY UNKNOWN: "+category);
                return "CATEGORY UNKNOWN: "+category;
        }

    }
}
