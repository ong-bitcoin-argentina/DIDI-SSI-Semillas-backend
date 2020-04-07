package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.awt.event.WindowFocusListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Getter
@Setter
@Slf4j
public class SurveyForm {

    //key form:
    @DateTimeFormat(pattern = "dd/MM/yy")
    @Temporal(TemporalType.DATE)
    private LocalDate surveyDate = null;
    private String surveyFormCode = null;
    private Long pdv = null;

    Set<Category> categorySet = new HashSet<>();


    public void addCategory(Category category) {
        //verificar si repite categorias o el set ya lo maneja.
        categorySet.add(category);
    }

    @Override
    public String toString() {
        return "SurveyForm{" +
                "surveyDate=" + surveyDate +
                ", surveyFormCode='" + surveyFormCode + '\'' +
                ", pdv=" + pdv +
                ", categoryList=" + categorySet.toString() +
                '}';
    }

    public void firstLoadForm(Category category){
        this.surveyFormCode = "category.formCode";
        this.surveyDate = LocalDate.parse("06/04/20", DateTimeFormatter.ofPattern("dd/MM/yy"));
        this.pdv = 12345L;
    }

    public boolean isInitialized(){
        return this.pdv != null && this.surveyDate != null && this.surveyFormCode != null;
    }

    public void initialize(AnswerRow answerRow){
        log.info("Initializing a new form");
        this.surveyFormCode = answerRow.getSurveyFormCode();
        this.surveyDate = answerRow.getSurveyDate();
        this.pdv = answerRow.getPdv();
    }

    public boolean isRowFromSameForm(AnswerRow answerRow){
        boolean comparison = this.pdv.equals(answerRow.getPdv())
                && this.surveyDate.isEqual(answerRow.getSurveyDate())
                && this.surveyFormCode.equals(answerRow.getSurveyFormCode());

        //log.info("isRowFromSameForm: "+comparison);
        return comparison;
    }



    public void clearForm(){
        this.surveyFormCode = null;
        this.surveyDate = null;
        this.pdv = null;
        categorySet.clear();
    }

    public void buildCredentials(){
        log.info("buildCredentials: "+this.toString());
    }

}
