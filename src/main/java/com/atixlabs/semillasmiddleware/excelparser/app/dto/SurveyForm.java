package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyForm {

    //key form:
    @DateTimeFormat(pattern = "dd/MM/yy")
    @Temporal(TemporalType.DATE)
    private LocalDate surveyDate;

    private String surveyFormCode;
    private Long pdv;

    List<AnswerRow> answerRowList = new ArrayList<>();


}
