package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum DwellingQuestion implements CategoryQuestion {
    DWELLING_TYPE("VIVIENDA"),
    HOLDING_TYPE("TIPO DE TENENCIA"),
    DISTRICT("DISTRITO DE RESIDENCIA");

    private String type;
    DwellingQuestion(String type) {
        this.type=type;
    }

    public boolean isRequired() {
        return true;
    }

}
