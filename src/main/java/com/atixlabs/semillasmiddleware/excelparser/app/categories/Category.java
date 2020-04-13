package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;


public interface Category {
     void loadData(AnswerRow answerRow);
     boolean isValid(ProcessExcelFileResult processExcelFileResult);
     //TODO: No le estoy pasando la fila con error al processExcelFileResult
     default boolean isFilledIfRequired(Object attribute, CategoryQuestion questionType, ProcessExcelFileResult processExcelFileResult){
          if (questionType.isRequired()  && attribute==null){
               processExcelFileResult.addRowError("El campo " + questionType.getQuestionName() + " es obligatorio.");
               return false;
          }
          return true;
     }
}
