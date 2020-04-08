package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.DwellingQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;

public class DwellingCategory implements Category {
    String dwellingType;
    String holdingType;
    String district;

    @Override
    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        switch (DwellingQuestion.get(question)){
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
        return(
                isFilledIfRequired(dwellingType, DwellingQuestion.DWELLING_TYPE,processExcelFileResult) &&
                isFilledIfRequired(holdingType, DwellingQuestion.HOLDING_TYPE, processExcelFileResult) &&
                isFilledIfRequired(district, DwellingQuestion.DISTRICT, processExcelFileResult)
        );
    }

}
