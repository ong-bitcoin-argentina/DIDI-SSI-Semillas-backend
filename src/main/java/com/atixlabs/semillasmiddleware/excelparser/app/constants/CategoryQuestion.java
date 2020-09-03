package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public interface CategoryQuestion {
    Class<?> getDataType();
    boolean isRequired();
    String getQuestionName();
    String name();

    default public boolean isEmpty() {
        return false;
    }
}
