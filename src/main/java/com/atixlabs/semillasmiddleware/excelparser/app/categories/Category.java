package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.CategoryQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;


public interface Category {
     void loadData(AnswerRow answerRow);

     Category getData();

     boolean isValid(ProcessExcelFileResult processExcelFileResult);
}
