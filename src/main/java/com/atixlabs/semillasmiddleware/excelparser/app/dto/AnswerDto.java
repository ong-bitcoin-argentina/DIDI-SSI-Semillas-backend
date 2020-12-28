package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.exception.InvalidAnswerCastException;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class AnswerDto {

    private Object answer;
    private CategoryQuestion question;
    private String cellLocation;

    public AnswerDto(CategoryQuestion question){
        this.question = question;
    }

    public void setAnswer(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        this.cellLocation = answerRow.getCellIndexName();
        try{
            this.answer = answerRow.getAnswerAs(question.getDataType());
        }
        catch (InvalidAnswerCastException e){
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader("Celda "+cellLocation)
                    .errorBody("Pregunta: "+question.getQuestionName() +" - "+ e.getMessage())
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
            log.info(e.getMessage());
        }
    }

    public boolean isValid(ProcessExcelFileResult processExcelFileResult, String categoryBeingChecked){

        if (question.isRequired() && answerIsEmpty()){
            //If the question does not exist in the form:
            if (cellLocation == null){
                processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                        .errorHeader(String.format("Categoría %s", categoryBeingChecked))
                        .errorBody(String.format("la pregunta %s no existe y es obligatoria", question.getQuestionName()))
                        .build()
                );
            }
            //If the question does exists in the form but its answer is empty:
            else{
                processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                        .errorHeader("Celda "+cellLocation)
                        .errorBody(String.format("la pregunta %s esta vacía y es obligatoria", question.getQuestionName()))
                        .errorType(ExcelErrorType.OTHER)
                        .build()
                );
            }
            return false;
        }
        return true;
    }


    public boolean answerIsEmpty(){
        return answer == null || answer.toString().trim().replace(" ","").equals("");
    }

    @Override
    public String toString() {
        return "" + answer +"";
    }
}
