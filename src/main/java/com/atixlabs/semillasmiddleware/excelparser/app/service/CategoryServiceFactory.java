package com.atixlabs.semillasmiddleware.excelparser.app.service;

public class CategoryServiceFactory {
    public CategoryExcelFileService create(String category){
        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)
        switch (category.toUpperCase().replaceAll("[0123456789]","")) {
            case "EMPRENDIMIENTO":
                return new EntrepreneurshipExcelFileService();
            case "DATOS DEL BENEFICIARIO":
                return new BeneficiaryExcelFileService();
            case "OTROS INGRESOS FAMILIARES":
                return new OtherFamilyIncomesExcelFileService();
            case "VIVIENDA":
                return new DwellingExcelFileService();
            case "DATOS ENTREVISTA":
                return new InterviewExcelFileService();
            case "FOTOS ADICIONALES":
                return new PicturesExcelFileService();
            case "SITUACIÓN PATRIMONIAL":
                return new PatrimonialSituationExcelFileService();
            case "FINANZAS FAMILIARES":
                return new FamilyFinancesExcelFileService();
            case "INGRESOS SOLICITANTE":
                return new ApplicantIncomesExcelFileService();
            case "DATOS DEL CONYUGE":
                return new KinsmanExcelFileService("Conyuge");
            case "DATOS HIJO":
                return new KinsmanExcelFileService("Hijo");
            case "OTRO MIEMBRO DE LA FAMILIA":
                return new KinsmanExcelFileService(null);
        }
        return null;
    }
}
