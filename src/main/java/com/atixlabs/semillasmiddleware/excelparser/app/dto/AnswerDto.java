package com.atixlabs.semillasmiddleware.excelparser.app.dto;

//import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AnswerDto {
    private Object answer;
    private CategoryQuestion question;
    private String cellLocation;

    public AnswerDto(CategoryQuestion question){
        this.question = question;
    }

    public void setAnswer(AnswerRow answerRow){
        this.cellLocation = "("+answerRow.getCellIndexName()+"): ";
        try{
            this.answer = answerRow.getAnswerAs(question.getDataType());
        }
        catch (Exception e){
            //excelfileresult add error row
        }
        //this.answer = answerRow.getAnswer();
        //VER COMO PASARLE EL EXCELFILERESULT AL GET ANSWERAS ???
    }

    public boolean isValid(ProcessExcelFileResult processExcelFileResult){
        if (question.isRequired() && answerIsEmpty()){
            processExcelFileResult.addRowError(cellLocation + question.getQuestionName() + " es un campo requerido");
            return false;
        }
        return true;
    }


    private boolean answerIsEmpty(){
        return answer.toString().trim().replace(" ","").equals("") || answer == null;
    }
}
