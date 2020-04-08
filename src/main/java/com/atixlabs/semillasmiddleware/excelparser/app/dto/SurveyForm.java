package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.BeneficiaryCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import javassist.expr.Instanceof;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Getter
@Setter
@Slf4j
public class SurveyForm {

    @Autowired
    //CredentialCreditRepository credentialCreditRepository;
    CredentialService credentialService;

    //key form:
    @DateTimeFormat(pattern = "dd/MM/yy")
    @Temporal(TemporalType.DATE)
    private LocalDate surveyDate = null;
    private String surveyFormCode = null;
    private Long pdv = null;

    ArrayList<Category> categoryList = new ArrayList<>();


    @Override
    public String toString() {
        return "SurveyForm{" +
                "surveyDate=" + surveyDate +
                ", surveyFormCode='" + surveyFormCode + '\'' +
                ", pdv=" + pdv +
                ", categoryList=" + categoryList.toString() +
                '}';
    }

    public boolean isEmpty(){
        return this.pdv == null || this.surveyDate == null || this.surveyFormCode == null;
    }

    public void initialize(AnswerRow answerRow){
        log.info("Initializing a new form");
        this.surveyFormCode = answerRow.getSurveyFormCode();
        this.surveyDate = answerRow.getSurveyDate();
        this.pdv = answerRow.getPdv();
    }

    public boolean isRowFromSameForm(AnswerRow answerRow){
        return this.pdv.equals(answerRow.getPdv())
                && this.surveyDate.isEqual(answerRow.getSurveyDate())
                && this.surveyFormCode.equals(answerRow.getSurveyFormCode());
    }

    public void clearForm(){
        this.surveyFormCode = null;
        this.surveyDate = null;
        this.pdv = null;
        categoryList.clear();
    }

    public void addCategory(Category category) {
        if(!categoryList.contains(category))
            categoryList.add(category);
    }

    public Integer findCategoryInList(Class<?> classToFind) {
        for(int i=0; i<categoryList.size(); i++){
            if(categoryList.get(i).getClass() == classToFind)//BeneficiaryCategory
                return i;
        }
        return -1;
    }
    public boolean findCategoryInListDummy(){
        for(Category categoryListElement : this.categoryList)
            if(categoryListElement instanceof BeneficiaryCategory)
                return true;
        return false;
    }




    public void buildCredentials()
    {
        log.info("buildCredentials: "+this.toString());
        CredentialCredit credentialCredit = new CredentialCredit();

        Integer categoryIndex = this.findCategoryInList(BeneficiaryCategory.class);
        if(categoryIndex >=0) {
            log.info("containsCategory = true");
            BeneficiaryCategory beneficiaryCategory = (BeneficiaryCategory) categoryList.get(categoryIndex).getData();
            credentialCredit.setDniBeneficiary(beneficiaryCategory.getNumeroDocumento());

            log.info("credencial: "+credentialCredit.toString());
            //credentialService.addCredentialCreditFromExcel(credentialCredit);
            credentialService.addCredentialCredit();
        }
        if(this.findCategoryInListDummy()){
            log.info("findCategoryInListDummy = true");
        }
    }

}
