package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PatrimonialSituationQuestion implements CategoryQuestion {
    PATRIMONY_DATA("DATOS PATRIMONIO"),
    CASH("EFECTIVO"),
    SPUN("FIADO"),
    STOCK("STOCK"),
    MACHINERY("MAQUINARIA"),
    PROPERTY("INMUEBLES"),
    TOTAL("+ TOTAL");

    private String questionName;
    static final Map<String, PatrimonialSituationQuestion> questionsMap = Arrays.stream(values()).collect(Collectors.toMap(PatrimonialSituationQuestion::getQuestionName, p->p));

    PatrimonialSituationQuestion(String questionName) { this.questionName = questionName; }

    public static PatrimonialSituationQuestion getEnumByStringValue(String questionName){
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
