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

    @Override
    protected void parseRow(Row row) {
            if(isEmpty(row))
                log.info("["+row.getRowNum()+"]-"+row.getFirstCellNum());


            //7 formulario //9 fecha relevamiento //10 PDV
            this.surveyFormCode = getCellStringValue(row, 7, "Formulario");
            this.surveyDate = getCellStringToDate(row, 9, "Fecha de relevamiento");
            this.pdv = getCellLongValue(row, 10, "PDV");

            //14 categoria //15 pregunta //16 respuesta
            this.category = getCellStringValue(row, 14, "Categoria");
            this.question = getCellStringValue(row, 15, "Pregunta");
            this.answer = getCellStringValue(row, 16, "Respuesta");

            //if (isRowValid())
            //    isValid = true;
    }

    private boolean isEmpty(Row row){
        return row.getFirstCellNum() < 0;
    }

    public boolean isValid(){
        if (surveyFormCode == null || surveyFormCode.isEmpty() || surveyDate == null || pdv == null || category == null || question == null || answer == null){
            this.errorMessage = "A required field is null or empty";
            return false;
        }
        return true;
    }


    /*

    private String getCleanCategory(String category) {
        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)

        if (category == null)
            return "CATEGORY NULL";

        category = StringUtil.cleanString(category);

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
                return "CATEGORY UNKNOWN: "+category;
        }
    }
*/

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
            log.info("["+this.rowNum+"]:String to Date conversion failed on: "+answer);
            this.errorMessage = "["+this.rowNum+"]:String to Date conversion failed on: "+answer;}
        return null;
    }

    public LocalDate getCellStringToDate(Row row, int cellindex, String descripcion){
        String dateString = null;
        try {
            dateString = getCellStringValue(row, 9, "fecha relevamiento").replaceAll("'", "").trim();
            return this.surveyDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yy"));
        }
        catch (Exception e){
            log.info("["+cellIndex+"]:String to Date conversion failed on: "+dateString);
            this.errorMessage = "["+cellIndex+"]:String to Date conversion failed on: "+dateString;
        }
        return null;
        }


}
