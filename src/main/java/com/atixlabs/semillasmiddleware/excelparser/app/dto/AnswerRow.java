package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class AnswerRow extends ExcelRow {

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
            //7 formulario //9 fecha relevamiento //10 PDV
            this.surveyFormCode = getCellStringValue(row, 7, "Formulario");
            this.surveyDate = getCellStringToDate(row, 9, "Fecha de relevamiento");
            this.pdv = getCellLongValue(row, 10, "PDV");

            //14 categoria //15 pregunta //16 respuesta
            this.category = getCellStringValue(row, 14, "Categoria");
            this.question = getCellStringValue(row, 15, "Pregunta");
            this.answer = getCellStringValue(row, 16, "Respuesta");
    }

    private boolean isEmpty(Row row){
        return row.getFirstCellNum() < 0;
    }

    public boolean hasFormKeyValues(){
        return surveyFormCode != null && !surveyFormCode.isEmpty() && surveyDate != null && pdv != null && category != null && question != null;
    }

    public String getAnswerAsString(){
        return answer;
    }
    public Double getAnswerAsDouble(){
        if (answer == null)
            return null;
        try { return Double.valueOf(answer);}
        catch (NumberFormatException e){this.errorMessage = "String to Double conversion failed on: "+answer;}
        return null;
    }
    public Long getAnswerAsLong(){
        if (answer == null)
            return null;
        try {return Long.valueOf(answer);}
        catch (NumberFormatException e){this.errorMessage = "String to Long conversion failed on: "+answer;}
        return null;
    }
    public Integer getAnswerAsInteger(){
        if (answer == null)
            return null;
        try {return Integer.valueOf(answer);}
        catch (NumberFormatException e){this.errorMessage = "String to Integer conversion failed on: "+answer;}
        return null;
    }
    public LocalDate getAnswerAsDate(String datePattern){
        if (answer == null)
            return null;
        try {return LocalDate.parse(answer, DateTimeFormatter.ofPattern(datePattern));}
        catch (NumberFormatException e){this.errorMessage = "String to Date conversion failed from "+answer+" to "+datePattern;}
        return null;
    }

    public LocalDate getCellStringToDate(Row row, int cellIndex, String description){
        String dateString = null;
        String datePattern = "dd/MM/yy";
        try {
            dateString = getCellStringValue(row, cellIndex, description).replaceAll("'", "").trim();
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(datePattern));
        }
        catch (Exception e){
            this.errorMessage = "String to Date conversion failed from "+dateString+" to "+datePattern;
        }
        return null;
    }


    @Override
    public String toString() {
        return "["+this.rowNum +"]{" +
                "surveyDate=" + surveyDate +
                ", surveyFormCode='" + surveyFormCode + '\'' +
                ", pdv=" + pdv +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", cellIndex=" + cellIndex +
                ", cellIndexName='" + cellIndexName + '\'' +
                ", cellIndexDescription='" + cellIndexDescription + '\'' +
                '}';
    }
}
