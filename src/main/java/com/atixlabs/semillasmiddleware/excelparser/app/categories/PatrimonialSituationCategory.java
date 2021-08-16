package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PatrimonialSituationQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter

public class PatrimonialSituationCategory implements Category {
    private String categoryOriginalName;
    private Categories categoryName;

    private AnswerDto patrimonyData;
    private AnswerDto cash;
    private AnswerDto spun;
    private AnswerDto stock;
    private AnswerDto machinery;
    private AnswerDto property;
    private AnswerDto total;

    public PatrimonialSituationCategory(String categoryUniqueName, Categories category) {

        this.patrimonyData = new AnswerDto(PatrimonialSituationQuestion.PATRIMONY_DATA);
        this.cash = new AnswerDto(PatrimonialSituationQuestion.CASH);
        this.spun = new AnswerDto(PatrimonialSituationQuestion.SPUN);
        this.stock = new AnswerDto(PatrimonialSituationQuestion.STOCK);
        this.machinery = new AnswerDto(PatrimonialSituationQuestion.MACHINERY);
        this.property = new AnswerDto(PatrimonialSituationQuestion.PROPERTY);
        this.total = new AnswerDto(PatrimonialSituationQuestion.TOTAL);

        this.categoryOriginalName = categoryUniqueName;
        this.categoryName = category;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        PatrimonialSituationQuestion questionMatch = null;

        questionMatch = PatrimonialSituationQuestion.getEnumByStringValue(question);

        if (questionMatch == null)
            return;
        Optional<AnswerDto> optionalAnswer = getAnswerType(questionMatch, answerRow);
        optionalAnswer.ifPresent(param -> param.setAnswer(answerRow, processExcelFileResult));
    }

    private Optional<AnswerDto> getAnswerType(PatrimonialSituationQuestion questionMatch, AnswerRow answerRow){
        switch (questionMatch) {
            case PATRIMONY_DATA:
                answerRow.setAnswer("SUBCATEGORY");
                return Optional.of(this.patrimonyData);
            case CASH:
                return Optional.of(this.cash);
            case SPUN:
                return Optional.of(this.spun);
            case STOCK:
                return Optional.of(this.stock);
            case MACHINERY:
                return Optional.of(this.machinery);
            //check final form
            case PROPERTY:
                return Optional.of(this.property);
            case TOTAL:
                return Optional.of(this.total);
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
                ", patrimonyData=" + patrimonyData +
                ", cash=" + cash +
                ", spun=" + spun +
                ", stock=" + stock +
                ", machinery=" + machinery +
                ", property=" + property +
                ", total=" + total +
                "}";
    }
    @Override
    public List<AnswerDto> getAnswersList() {
        return Arrays.asList(patrimonyData, cash, spun, stock, machinery, property, total);
    }

    public Boolean isModification() { return true; }
}
