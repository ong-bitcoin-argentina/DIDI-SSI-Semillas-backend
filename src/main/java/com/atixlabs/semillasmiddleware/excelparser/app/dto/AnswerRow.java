package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Slf4j
public class AnswerRow extends ExcelRow {

    //key form:
    @DateTimeFormat(pattern = "dd/MM/yy")
    @Temporal(TemporalType.DATE)
    private LocalDate surveyDate;

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
        //7 formulario
        //9 fecha relevamiento
        //10 PDV
        this.surveyFormCode = getCellStringValue(row, 7, "formulario");

        String dateString = getCellStringValue(row, 9, "fecha relevamiento");
        dateString = dateString.replaceAll("'", "").trim();
        this.surveyDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yy"));

        this.pdv = getCellStringToLongValue(row, 10, "PDV");


        //14 categoria
        //15 pregunta
        //16 respuesta
        this.category = getCleanCategory(getCellStringValue(row, 14, "category"));
        this.question = getCellStringValue(row, 15, "question");
        this.answer = getCellStringValue(row, 16, "answer");

        //this.cellIndex = 1;
        //this.cellIndexName = "compl";
        //this.cellIndexDescription = "completar";
    }


    public String getCleanCategory(String category) {
        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)

        if (category == null)
            return "CATEGORY NULL";

        category = category.toUpperCase().replaceAll("[0123456789]", "").trim();

        switch (category) {
            case "DATOS DEL BENEFICIARIO":
            case "DATOS DEL CONYUGE":
            case "DATOS ENTREVISTA":
            case "DATOS HIJO":
            case "EMPRENDIMIENTO":
            case "FINANZAS FAMILIARES":
            case "FOTOS ADICIONALES":
            case "INGRESOS SOLICITANTE":
            case "OTRO MIEMBRO DE LA FAMILIA":
            case "OTROS INGRESOS FAMILIARES":
            case "SITUACION PATRIMONIAL":
            case "VIVIENDA":
                return category;
            default:
                //log.info("CATEGORY UNKNOWN: "+category);
                return "CATEGORY UNKNOWN: "+category;
        }
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

    public String getAnswerAsString(){
        return answer;
    }
    public Double getAnswerAsDouble(){
        try {
            return Double.valueOf(answer);
        }
        catch (NumberFormatException e){
            log.info("String to Double conversion failed on: "+answer);
        }
        return null;
    }
    public Long getAnswerAsLong(){
        return Long.valueOf(answer);
    }
    public Integer getAnswerAsInteger(){
        return Integer.valueOf(answer);
    }
    public LocalDate getAnswerAsDate(String datePattern){
        //"dd/MM/yy"
        return LocalDate.parse(answer, DateTimeFormatter.ofPattern(datePattern));
    }



}
