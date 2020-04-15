package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.DwellingQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

public class DwellingCategory implements Category {
    String dwellingType;
    String holdingType;
    String district;

    @Override
    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());

        DwellingQuestion questionMatch = DwellingQuestion.get(question);

        if(questionMatch==null)
            return;

        switch (questionMatch){
            case DWELLING_TYPE:
                this.dwellingType = answerRow.getAnswerAsString();
                break;
            case HOLDING_TYPE:
                this.holdingType = answerRow.getAnswerAsString();
                break;
            case DISTRICT:
                this.district = answerRow.getAnswerAsString();
        }
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<Boolean> validations = new LinkedList<>();
        validations.add(isFilledIfRequired(dwellingType, DwellingQuestion.DWELLING_TYPE,processExcelFileResult));
        validations.add(isFilledIfRequired(holdingType, DwellingQuestion.HOLDING_TYPE, processExcelFileResult));
        validations.add(isFilledIfRequired(district, DwellingQuestion.DISTRICT, processExcelFileResult));
        return validations.stream().allMatch(v->v);
    }

    @Override
    public Category getData() {
        return this;
    }
}
