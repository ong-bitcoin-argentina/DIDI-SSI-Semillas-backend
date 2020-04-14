package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
public class PersonCategory implements Category {
    String nameAndSurname;
    Long idNumber;
    String gender;
    LocalDate birthdate;
    String relation;
    PersonType personType;

    public PersonCategory(String personType){
        personType = StringUtil.removeNumbers(StringUtil.toUpperCaseTrimAndRemoveAccents(personType.replaceAll("DATOS|DEL","")));
        try{
            this.personType = PersonType.get(personType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());

        PersonQuestion questionMatch = PersonQuestion.get(question);

        if(questionMatch == null)
            return;

        switch (questionMatch) {
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
                this.birthdate = answerRow.getAnswerAsDate("dd/MM/yyyy");
                break;
            case RELATION:
                this.relation = answerRow.getAnswerAsString();
                break;
        }
    }

    @Override
    public Category getData() {
        return this;
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<Boolean> validations = new LinkedList<>();
        validations.add(isFilledIfRequired(nameAndSurname, PersonQuestion.NAME_AND_SURNAME,processExcelFileResult));
        validations.add(isFilledIfRequired(idNumber, PersonQuestion.ID_NUMBER, processExcelFileResult));
        validations.add(isFilledIfRequired(gender, PersonQuestion.GENDER, processExcelFileResult));
        validations.add(isFilledIfRequired(birthdate, PersonQuestion.BIRTHDATE, processExcelFileResult));
        validations.add(isFilledIfRequired(relation, PersonQuestion.RELATION, processExcelFileResult));
        return validations.stream().allMatch(v->v);
    }

    @Override
    public boolean isFilledIfRequired(Object attribute, CategoryQuestion questionType, ProcessExcelFileResult processExcelFileResult) {
        return false;
    }
}
