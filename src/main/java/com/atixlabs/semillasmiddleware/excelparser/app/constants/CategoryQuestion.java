package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public interface CategoryQuestion {
    Class<?> getDataType();
    boolean isRequired();
    String getQuestionName();
    static CategoryQuestion get(){return null;};
    String name();

}
