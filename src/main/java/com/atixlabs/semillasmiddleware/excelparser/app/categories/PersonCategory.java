package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PersonCategory implements Category {
    AnswerDto name;
    AnswerDto surname;
    AnswerDto idType;
    AnswerDto idNumber;
    AnswerDto gender;
    AnswerDto birthdate;
    AnswerDto relation;
    PersonType personType;

    public PersonCategory(String personType){
        this.name = new AnswerDto(PersonQuestion.NAME);
        this.surname = new AnswerDto(PersonQuestion.SURNAME);
        this.idType = new AnswerDto(PersonQuestion.ID_TYPE);
        this.idNumber = new AnswerDto(PersonQuestion.ID_NUMBER);
        this.gender = new AnswerDto(PersonQuestion.GENDER);
        this.birthdate = new AnswerDto(PersonQuestion.BIRTHDATE);
        this.relation = new AnswerDto(PersonQuestion.RELATION);

        personType = StringUtil.removeNumbers(StringUtil.toUpperCaseTrimAndRemoveAccents(personType.replaceAll("DATOS|DEL","")));
        try{
            this.personType = PersonType.get(personType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        switch (PersonQuestion.get(question)) {
            case ID_TYPE:
                this.idType.setAnswer(answerRow);
                break;
            case ID_NUMBER:
                this.idNumber.setAnswer(answerRow);
                break;
            case NAME:
                this.name.setAnswer(answerRow);
                break;
            case SURNAME:
                this.surname.setAnswer(answerRow);
                break;
            case GENDER:
                this.gender.setAnswer(answerRow);
                break;
            case BIRTHDATE:
                this.birthdate.setAnswer(answerRow);
                break;
            case RELATION:
                this.relation.setAnswer(answerRow);
                break;
        }
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.name);
        answers.add(this.surname);
        answers.add(this.idNumber);
        answers.add(this.gender);
        answers.add(this.birthdate);
        answers.add(this.relation);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }
}
