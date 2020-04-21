package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;

public class AnswerRow extends ExcelRow {

    //key form:
    private Date fechaRelevamiento;
    private String surveyFormCode;
    private Long pdv;

    private String category;
    private String question;
    private String answer;

    public AnswerRow(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) {

        //7
        //14 categoria
        //15 pregunta
        //16 respuesta
        //this.fechaRelevamiento = getCellStringValue()


        this.category = getCellStringValue(row, 14, "category");
        this.question = getCellStringValue(row, 15, "question");
        this.answer   = getCellStringValue(row, 16, "answer");

        //this.cellIndex = 1;
        //this.cellIndexName = "compl";
        //this.cellIndexDescription = "completar";



    }

    @Override
    public String toString() {
        return "AnswerRow{" +
                "category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", rowNum=" + rowNum +
                ", errorMessage='" + errorMessage + '\'' +
                ", isValid=" + isValid +
                ", exists=" + exists +
                ", cellIndex=" + cellIndex +
                ", cellIndexName='" + cellIndexName + '\'' +
                ", cellIndexDescription='" + cellIndexDescription + '\'' +
                '}';
    }
}
