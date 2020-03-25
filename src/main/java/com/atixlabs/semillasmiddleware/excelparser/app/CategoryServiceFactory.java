package com.atixlabs.semillasmiddleware.excelparser.app;

public class CategoryServiceFactory {
    public CategoryExcelFileService create(String category){
        switch (category.toUpperCase()){
            case "EMPRENDIMIENTO":
                return new EntrepreneurshipExcelFileService();
        }
        return null;
    }
}
