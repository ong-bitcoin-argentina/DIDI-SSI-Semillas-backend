package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.DwellingQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DwellingCategory implements Category {

    String categoryOriginalName;

    AnswerDto dwellingType;
    AnswerDto holdingType;
    AnswerDto district;

    public DwellingCategory(String categoryOriginalName){
        this.dwellingType = new AnswerDto(DwellingQuestion.DWELLING_TYPE);
        this.holdingType = new AnswerDto(DwellingQuestion.HOLDING_TYPE);
        this.district = new AnswerDto(DwellingQuestion.DISTRICT);

        this.categoryOriginalName = categoryOriginalName;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());

        DwellingQuestion questionMatch = DwellingQuestion.getEnumByStringValue(question);

        if(questionMatch==null)
            return;

        switch (questionMatch){
            case DWELLING_TYPE:
                this.dwellingType.setAnswer(answerRow, processExcelFileResult);
                break;
            case HOLDING_TYPE:
                this.holdingType.setAnswer(answerRow, processExcelFileResult);
                break;
            case DISTRICT:
                this.district.setAnswer(answerRow, processExcelFileResult);
        }
    }

    @Override
    public Category getData() {
        return this;
    }

    @Override
    public  String getCategoryOriginalName(){
        return categoryOriginalName;
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.dwellingType);
        answers.add(this.holdingType);
        answers.add(this.district);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult, categoryOriginalName)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }

    @Override
    public boolean isEmpty(){
        return dwellingType.answerIsEmpty() && holdingType.answerIsEmpty() && district.answerIsEmpty();
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    public String getDwellingType(){
        return (String) this.dwellingType.getAnswer();
    }

    public String getHoldingType(){
        return (String) this.holdingType.getAnswer();
    }

    public String getDistrict(){
        return (String) this.district.getAnswer();
    }

    @Override
    public String toString() {
        return "DwellingCategory{" +
                "categoryOriginalName='" + categoryOriginalName + '\'' +
                ", dwellingType=" + dwellingType +
                ", holdingType=" + holdingType +
                ", district=" + district +
                '}';
    }
}
