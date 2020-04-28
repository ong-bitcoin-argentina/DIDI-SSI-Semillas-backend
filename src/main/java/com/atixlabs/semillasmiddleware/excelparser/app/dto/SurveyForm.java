package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

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
        log.info("calling non-parameter constructor for surveyForm");
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
        Category category = this.getCategoryByName(answerRow.getCategory(), processExcelFileResult);
        if (category != null)
            category.loadData(answerRow, processExcelFileResult);
    }

    public Category getCategoryByName(String categoryToFind, ProcessExcelFileResult processExcelFileResult) {

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

    public ArrayList<Category> getCompletedCategoriesByClass(Class<?> classToFind) {
        ArrayList<Category> classArrayList = new ArrayList<>();

        for (Category category : categoryList) {
            if (category.getClass() == classToFind && !category.isEmpty())
                classArrayList.add(category);
        }
        return classArrayList;
    }
/*
    public Integer findCategoryInList(Class<?> classToFind) {
        for(int i = 0; i<categoryList.size(); i++){
            if(categoryList.get(i).getClass() == classToFind)
                return i;
        }
        return -1;
    }

    public Category getCategoryData(Class<?> classToFind){
        Integer categoryIndex = this.findCategoryInList(classToFind);
        if(categoryIndex >=0)
            return this.getCategoryList().get(categoryIndex).getData();
        return null;
    }
    */

}
