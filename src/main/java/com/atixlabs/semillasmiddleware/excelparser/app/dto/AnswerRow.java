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
import org.springframework.util.StringUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
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
    public String toString() {
        return "AnswerRow{" +
                "surveyDate=" + surveyDate +
                ", surveyFormCode='" + surveyFormCode + '\'' +
                ", pdv=" + pdv +
                ", category='" + category + '\'' +
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

    @Override
    protected void parseRow(Row row) {
            if(!isInputRowEmpty(row))
                return;

            //7 formulario //9 fecha relevamiento //10 PDV
            this.surveyFormCode = getCellStringValue(row, 7, "formulario");
            String dateString = getCellStringValue(row, 9, "fecha relevamiento").replaceAll("'", "").trim();
            this.surveyDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yy"));
            this.pdv = getCellStringToLongValue(row, 10, "PDV");

            //14 categoria //15 pregunta //16 respuesta
            this.category = getCellStringValue(row, 14, "category");
            this.question = getCellStringValue(row, 15, "question");
            this.answer = getCellStringValue(row, 16, "answer");
    }

    private boolean isInputRowEmpty(Row row){
        //log.info("parseRow->firstCellData: "+String.valueOf(row.getFirstCellNum()));
        if(row.getFirstCellNum()<0) {
            this.isValid = false;
            this.errorMessage ="Row is empty";
            return false;
        }
        //todo: review other possible validations
        return true;
    }

    public String getAnswerAsString(){
        return answer;
    }
    public Double getAnswerAsDouble(){
        try { return Double.valueOf(answer);}
        catch (NumberFormatException e){
            log.info("String to Double conversion failed on: "+answer);
            this.errorMessage = "String to Double conversion failed on: "+answer;
        }
        return null;
    }
    public Long getAnswerAsLong(){
        try {return Long.valueOf(answer);}
        catch (NumberFormatException e){
            log.info("String to Long conversion failed on: "+answer);
            this.errorMessage = "String to Long conversion failed on: "+answer;
        }
        return null;
    }
    public Integer getAnswerAsInteger(){
        try {return Integer.valueOf(answer);}
        catch (NumberFormatException e){
            log.info("String to Integer conversion failed on: "+answer);
            this.errorMessage = "String to Integer conversion failed on: "+answer;}
        return null;
    }
    public LocalDate getAnswerAsDate(String datePattern){
        //"dd/MM/yy"
        try {return LocalDate.parse(answer, DateTimeFormatter.ofPattern(datePattern));}
        catch (NumberFormatException e){
            log.info("String to Date conversion failed on: "+answer);
            this.errorMessage = "String to Date conversion failed on: "+answer;}
        return null;
    }



}
