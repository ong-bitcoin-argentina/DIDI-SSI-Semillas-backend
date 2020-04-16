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
    AnswerDto dwellingType;
    AnswerDto holdingType;
    AnswerDto district;

    public DwellingCategory(){
        this.dwellingType = new AnswerDto(DwellingQuestion.DWELLING_TYPE);
        this.holdingType = new AnswerDto(DwellingQuestion.HOLDING_TYPE);
        this.district = new AnswerDto(DwellingQuestion.DISTRICT);
    }

    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        switch (DwellingQuestion.get(question)){
            case DWELLING_TYPE:
                this.dwellingType.setAnswer(answerRow);
                break;
            case HOLDING_TYPE:
                this.holdingType.setAnswer(answerRow);
                break;
            case DISTRICT:
                this.district.setAnswer(answerRow);
        }
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.dwellingType);
        answers.add(this.holdingType);
        answers.add(this.district);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
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

}
