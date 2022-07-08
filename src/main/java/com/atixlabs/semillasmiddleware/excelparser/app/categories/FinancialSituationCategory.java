package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.FinancialSituationQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Setter
@Getter

public class FinancialSituationCategory implements Category {
    private String categoryOriginalName;
    private Categories categoryName;

    private AnswerDto previousCredits;
    private AnswerDto previousUnpaidCredit;
    private AnswerDto name;
    private AnswerDto reason;
    private AnswerDto year;

    public FinancialSituationCategory(String categoryUniqueName, Categories category) {

        this.previousCredits = new AnswerDto(FinancialSituationQuestion.PREVIOUS_CREDITS);
        this.previousUnpaidCredit = new AnswerDto(FinancialSituationQuestion.PREVIOUS_UNPAID_CREDIT);
        this.name = new AnswerDto(FinancialSituationQuestion.NAME);
        this.reason = new AnswerDto(FinancialSituationQuestion.REASON);
        this.year = new AnswerDto(FinancialSituationQuestion.YEAR);

        this.categoryOriginalName = categoryUniqueName;
        this.categoryName = category;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        FinancialSituationQuestion questionMatch = null;

        questionMatch = FinancialSituationQuestion.getEnumByStringValue(question);

        if (questionMatch==null)
            return;
        Optional<AnswerDto> optionalAnswer = getAnswerType(questionMatch, answerRow);
        optionalAnswer.ifPresent(param -> param.setAnswer(answerRow, processExcelFileResult));
    }

    private Optional<AnswerDto> getAnswerType(FinancialSituationQuestion questionMatch, AnswerRow answerRow) {
        switch (questionMatch) {
            case PREVIOUS_CREDITS:
                answerRow.setAnswer("SUBCATEGORY");
                return Optional.of(this.previousCredits);
            case PREVIOUS_UNPAID_CREDIT:
                return Optional.of(this.previousUnpaidCredit);
            case NAME:
                return Optional.of(this.name);
            case REASON:
                return Optional.of(this.reason);
            case YEAR:
                return Optional.of(this.year);
            default:
                return Optional.empty();
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
                ", previousCredits=" + previousCredits +
                ", previousUnpaidCredit=" + previousUnpaidCredit +
                ", name=" + name +
                ", reason=" + reason +
                ", year=" + year +
                "}";
    }
    @Override
    public List<AnswerDto> getAnswersList() {
        return Arrays.asList(previousCredits, previousUnpaidCredit, name, reason, year);
    }

    public Boolean isModification() { return true; }

    public void setIsModification(AnswerDto isModification) {/* ** */}
}