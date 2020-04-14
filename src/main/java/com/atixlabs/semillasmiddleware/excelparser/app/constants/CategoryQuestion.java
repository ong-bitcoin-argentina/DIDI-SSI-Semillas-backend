package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public interface CategoryQuestion {
    public boolean isRequired();
    public String getQuestionName();
    public static CategoryQuestion get(String questionName){
        return null;
    };
    public String name();

}
