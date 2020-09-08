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
import java.util.List;

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

    public PatrimonialSituationCategory(String categoryOriginalName) {

        this.patrimonyData = new AnswerDto(PatrimonialSituationQuestion.PATRIMONY_DATA);
        this.cash = new AnswerDto(PatrimonialSituationQuestion.CASH);
        this.spun = new AnswerDto(PatrimonialSituationQuestion.SPUN);
        this.stock = new AnswerDto(PatrimonialSituationQuestion.STOCK);
        this.machinery = new AnswerDto(PatrimonialSituationQuestion.MACHINERY);
        this.property = new AnswerDto(PatrimonialSituationQuestion.PROPERTY);
        this.total = new AnswerDto(PatrimonialSituationQuestion.TOTAL);

        this.categoryOriginalName = categoryOriginalName;
        this.categoryName = Categories.PATRIMONIAL_SITUATION_CATEGORY_NAME;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        PatrimonialSituationQuestion questionMatch = null;

        questionMatch = PatrimonialSituationQuestion.getEnumByStringValue(question);

        if (questionMatch==null)
            return;

        switch (questionMatch) {
            case PATRIMONY_DATA:
                this.patrimonyData.setAnswer(answerRow, processExcelFileResult);
                break;
            case CASH:
                this.cash.setAnswer(answerRow, processExcelFileResult);
                break;
            case SPUN:
                this.spun.setAnswer(answerRow, processExcelFileResult);
                break;
            case STOCK:
                this.stock.setAnswer(answerRow, processExcelFileResult);
                break;
            case MACHINERY:
                this.machinery.setAnswer(answerRow, processExcelFileResult);
                break;
            //check final form
            case PROPERTY:
                this.property.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL:
                this.total.setAnswer(answerRow, processExcelFileResult);
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
}
