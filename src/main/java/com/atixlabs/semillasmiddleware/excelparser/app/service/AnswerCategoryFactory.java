package com.atixlabs.semillasmiddleware.excelparser.app.service;

public class AnswerCategoryFactory {

    public String create(String category){
        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)

        category = category.toUpperCase().replaceAll("[0123456789]","");
        category = category.trim();

        switch (category) {
            case "EMPRENDIMIENTO":
            case "DATOS DEL BENEFICIARIO":
            case "OTROS INGRESOS FAMILIARES":
            case "VIVIENDA":
            case "DATOS ENTREVISTA":
            case "FOTOS ADICIONALES":
            case "SITUACIÓN PATRIMONIAL":
            case "FINANZAS FAMILIARES":
            case "INGRESOS SOLICITANTE":
            case "DATOS DEL CONYUGE":
            case "DATOS HIJO":
            case "OTRO MIEMBRO DE LA FAMILIA":
                return category;
            default:
                return "CATEGORY UNKNOWN";
        }

    }
}
