package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyForm {

    private Date fechaRelevamiento;
    private String surveyFormCode;
    private Long pdv;

    List<AnswerRow> answerRowList = new ArrayList<>();


}
