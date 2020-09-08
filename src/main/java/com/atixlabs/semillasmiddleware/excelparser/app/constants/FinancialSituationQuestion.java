package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum FinancialSituationQuestion implements CategoryQuestion {
    PREVIOUS_UNPAID_CREDIT("¿TIENE ALGUN CREDITO ANTERIOR IMPAGO?"),
    NAME("NOMBRE DE ENTIDAD FINANCIERA"),
    REASON("MOTIVO"),
    YEAR("ANO");

    private String questionName;
    static final Map<String, FinancialSituationQuestion> questionsMap = Arrays.stream(values()).collect(Collectors.toMap(FinancialSituationQuestion::getQuestionName, p->p));

    FinancialSituationQuestion(String questionName) { this.questionName = questionName; }

    public static FinancialSituationQuestion getEnumByStringValue(String questionName){
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