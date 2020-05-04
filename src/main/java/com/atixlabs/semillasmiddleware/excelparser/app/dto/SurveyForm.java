package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import ch.qos.logback.core.joran.action.IADataForComplexProperty;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.naming.ldap.PagedResultsControl;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@Slf4j
public class SurveyForm {
    @DateTimeFormat(pattern = "dd/MM/yy")
    @Temporal(TemporalType.DATE)
    private LocalDate surveyDate = null;
    private String surveyFormCode = null;
    private Long pdv = null;

    private ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    public String toString() {
        return "SurveyForm{" +
                "surveyDate=" + surveyDate +
                ", surveyFormCode='" + surveyFormCode + '\'' +
                ", pdv=" + pdv +
                ", categoryList=" + categoryList.toString() +
                '}';
    }

    public SurveyForm(){
    }

    public SurveyForm(AnswerRow answerRow){
        initialize(answerRow);
    }


    public void initialize(AnswerRow answerRow){
        log.info("Initializing a new form");
        this.surveyFormCode = answerRow.getSurveyFormCode();
        this.surveyDate = answerRow.getSurveyDate();
        this.pdv = answerRow.getPdv();
    }

    public boolean isEmpty(){
        return this.pdv == null || this.surveyDate == null || this.surveyFormCode == null;
    }

    public boolean isRowFromSameForm(AnswerRow answerRow){

        if(this.isEmpty()) {
            this.initialize(answerRow);
            return true;
        }

        return this.pdv.equals(answerRow.getPdv())
                && this.surveyDate.isEqual(answerRow.getSurveyDate())
                && this.surveyFormCode.equals(answerRow.getSurveyFormCode());
    }

    public void setCategoryData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        Category category = this.getCategoryFromForm(answerRow.getCategory(), processExcelFileResult);
        if (category != null)
            category.loadData(answerRow, processExcelFileResult);
    }

    public Category getCategoryFromForm(String categoryToFind, ProcessExcelFileResult processExcelFileResult) {

        if (categoryToFind == null)
            return null;

        categoryToFind = StringUtil.toUpperCaseTrimAndRemoveAccents(categoryToFind);

        for (Category value : categoryList) {
            if (value.getCategoryOriginalName().equals(categoryToFind))
                return value;
        }
        if (processExcelFileResult!=null)
            processExcelFileResult.addRowDebug("Categoría "+categoryToFind, "No fue definida en meta-data: será ignorada");
        return null;
    }

    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        //return categoryList.stream().allMatch(category -> category.isValid(processExcelFileResult));

        boolean allValid = true;
        String msg;

        for (Category category : categoryList) {
            if (category.isEmpty()) {
                if (category.isRequired()) {
                    allValid = false;
                    msg = "Empty and Required";
                }
                else
                    msg = "Empty but not Required";
            }
            else {
                if (!category.isValid(processExcelFileResult)) {
                    allValid = false;
                    msg = "Completed with errors";
                }
                else
                    msg = "Completed OK";
            }
            log.info("SurveyForm -> isValid: " + category.getCategoryOriginalName() + " "+msg);
        }

        return allValid;
    }
}
