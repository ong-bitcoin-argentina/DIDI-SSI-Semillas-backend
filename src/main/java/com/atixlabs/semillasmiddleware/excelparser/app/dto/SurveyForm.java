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
        Category category = this.getCategoryByUniqueName(answerRow.getCategory(), processExcelFileResult);
        if (category != null)
            category.loadData(answerRow, processExcelFileResult);
    }

    public Category getCategoryByUniqueName(String categoryToFind, ProcessExcelFileResult processExcelFileResult) {

        if (categoryToFind == null)
            return null;

        categoryToFind = StringUtil.toUpperCaseTrimAndRemoveAccents(categoryToFind);

        for (Category value : categoryList) {
            if (value.getCategoryUniqueName().equals(categoryToFind))
                return value;
        }
        if (processExcelFileResult!=null)
            processExcelFileResult.addRowDebug("Categoría "+categoryToFind, "No fue definida en meta-data: será ignorada");
        return null;
    }

    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        //return categoryList.stream().allMatch(providerCategory -> providerCategory.isValid(processExcelFileResult));

        boolean allValid = true;
        String msg;

        for (Category category : categoryList) {
            if (category.isEmpty()) {
                if (category.isRequired()) {
                    allValid = false;
                    msg = "Empty and Required";
                    processExcelFileResult.addRowError(String.format("Categoría %s",category.getCategoryUniqueName()), "la categoria esta vacia o no completa y es obligatoria :"+ category.toString());
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
            log.info("SurveyForm -> isValid: " + category.getCategoryUniqueName() + " "+msg);
        }
        return allValid;
    }

    public ArrayList<Category> getAllCompletedCategories() {
        ArrayList<Category> classArrayList = new ArrayList<>();

        for (Category category : categoryList) {
            if (!category.isEmpty())
                classArrayList.add(category);
        }
        return classArrayList;
    }

}
