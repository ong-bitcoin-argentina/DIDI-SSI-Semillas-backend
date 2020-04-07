package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PersonCategory implements Category {
    String nameAndSurname;
    Long idNumber;
    String gender;
    LocalDate birthdate;
    String relation;
    PersonType personType;

    public PersonCategory(String personType){
        personType = StringUtil.cleanString(personType.replaceAll("DATOS|DEL",""));
        try{
            this.personType = PersonType.get(personType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.cleanString(answerRow.getQuestion());
        switch (PersonQuestion.get(question)) {
            case ID_NUMBER:
                this.idNumber = answerRow.getAnswerAsLong();
                break;
            case NAME_AND_SURNAME:
                this.nameAndSurname = answerRow.getAnswerAsString();
                break;
            case GENDER:
                this.gender = answerRow.getAnswerAsString();
                break;
            case BIRTHDATE:
                this.birthdate = answerRow.getAnswerAsDate("dd/MM/yy");
                break;
            case RELATION:
                this.relation = answerRow.getAnswerAsString();
                break;
        }

    }
}
