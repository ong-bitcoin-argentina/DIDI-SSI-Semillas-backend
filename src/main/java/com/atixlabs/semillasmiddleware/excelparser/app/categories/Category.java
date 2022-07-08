package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;

import java.util.ArrayList;
import java.util.List;


public interface Category {

     void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult);

     String getCategoryUniqueName();
     Categories getCategoryName();

     boolean isValid(ProcessExcelFileResult processExcelFileResult);
     boolean isEmpty();
     boolean isRequired();
     Boolean isModification();
     public void setIsModification(AnswerDto isModification);

     default String getHtmlFromTemplate(String rowTemplate, String subCategoryTemplate, String subcategoryParam, String questionParam, String answerParam) {
          StringBuilder html= new StringBuilder();

          List<AnswerDto> answerDtos = this.getAnswersList();

          for (AnswerDto answer : answerDtos) {
               if (answer.getQuestion() != null && answer.getAnswer() != null) {
                    if (answer.getAnswer().equals("SUBCATEGORY")) {
                         html.append(subCategoryTemplate
                                 .replace(subcategoryParam, answer.getQuestion().getQuestionName()));
                    } else html.append(rowTemplate
                                 .replace(questionParam, answer.getQuestion().getQuestionName())
                                 .replace(answerParam, answer.getAnswer().toString()));
               }
          }
          return html.toString();
     }

     default List<AnswerDto> getAnswersList(){
          return new ArrayList<>();
     }

     default Boolean getBooleanFromAnswer(AnswerDto answerDto){
          return answerDto.getAnswer().toString().equalsIgnoreCase("si");
     }
}
