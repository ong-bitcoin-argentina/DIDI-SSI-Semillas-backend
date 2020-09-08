package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.FinancialSituationQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PatrimonialSituationQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter

public class FinancialSituationCategory implements Category {
    private String categoryOriginalName;
    private Categories categoryName;

    private AnswerDto previousUnpaidCredit;
    private AnswerDto name;
    private AnswerDto reason;
    private AnswerDto year;

    public FinancialSituationCategory(String categoryOriginalName) {

        this.previousUnpaidCredit = new AnswerDto(FinancialSituationQuestion.PREVIOUS_UNPAID_CREDIT);
        this.name = new AnswerDto(FinancialSituationQuestion.NAME);
        this.reason = new AnswerDto(FinancialSituationQuestion.REASON);
        this.year = new AnswerDto(FinancialSituationQuestion.YEAR);

        this.categoryOriginalName = categoryOriginalName;
        this.categoryName = Categories.FINANCIAL_SITUATION_CATEGORY_NAME;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        FinancialSituationQuestion questionMatch = null;

        questionMatch = FinancialSituationQuestion.getEnumByStringValue(question);

        if (questionMatch==null)
            return;

        switch (questionMatch) {
            case PREVIOUS_UNPAID_CREDIT:
                this.previousUnpaidCredit.setAnswer(answerRow, processExcelFileResult);
                break;
            case NAME:
                this.name.setAnswer(answerRow, processExcelFileResult);
                break;
            case REASON:
                this.reason.setAnswer(answerRow, processExcelFileResult);
                break;
            case YEAR:
                this.year.setAnswer(answerRow, processExcelFileResult);
                break;
        }
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        return true;
    }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getCategoryUniqueName(){
        return categoryOriginalName;
    }

    @Override
    public Categories getCategoryName(){return categoryName;}

    @Override
    public String toString() {
        return "FinancialSituationCategory{" +
                "categoryOriginalName='" + categoryOriginalName + '\'' +
                ", previousUnpaidCredit=" + previousUnpaidCredit +
                ", name=" + name +
                ", reason=" + reason +
                ", year=" + year +
                "}";
    }
    @Override
    public List<AnswerDto> getAnswersList() {
        return Arrays.asList(previousUnpaidCredit, name, reason, year);
    }
}