package com.atixlabs.semillasmiddleware.excelparser.app.service;

public class CategoryServiceFactory {
    public CategoryExcelFileService create(String category){
        switch (category.toUpperCase()){
            case "EMPRENDIMIENTO":
                return new EntrepreneurshipExcelFileService();
            case "DATOS DEL BENEFICIARIO":
                return new BeneficiaryExcelFileService();
        }
        return null;
    }
}
