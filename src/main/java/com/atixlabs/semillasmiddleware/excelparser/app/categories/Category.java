package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;


public interface Category {

     void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult);

     Category getData();
     String getCategoryOriginalName();
     Categories getCategoryName();
     Class<?> getCategoryClass();

     boolean isValid(ProcessExcelFileResult processExcelFileResult);
     boolean isEmpty();
     boolean isRequired();
}
