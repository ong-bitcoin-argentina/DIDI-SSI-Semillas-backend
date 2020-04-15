package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PersonQuestion implements CategoryQuestion {

    NAME("NOMBRE"),
    SURNAME("APELLIDO"),
    ID_TYPE("TIPO DOCUMENTO"),
    ID_NUMBER("NUMERO DE DOCUMENTO"),
    GENDER("GENERO"),
    BIRTHDATE("FECHA DE NACIMIENTO"),
    RELATION("PARENTESCO"){
        @Override
        public boolean isRequired() {
            return false;
        }
    };

    private String questionName;
    static final Map<String, PersonQuestion> questionsMap = Arrays.stream(values()).collect(Collectors.toMap(PersonQuestion::getQuestionName, q->q));

    PersonQuestion(String questionName){
        this.questionName = questionName;
    }

    public static PersonQuestion get(String questionName) {
        return questionsMap.get(questionName);
    }

    public String getQuestionName(){
        return this.questionName;
    }

    public boolean isRequired(){
        return true;
    }
}
