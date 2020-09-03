package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;

import java.util.ArrayList;
import java.util.List;


public interface Category {

     void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult);

     String getCategoryUniqueName();
     Categories getCategoryName();

     boolean isValid(ProcessExcelFileResult processExcelFileResult);
     boolean isEmpty();
     boolean isRequired();

     default String getHtmlFromTemplate(String template, String questionParam, String answerParam) {
          String html="";

          List<AnswerDto> answerDtos = this.getAnswersList();

          for (AnswerDto answer : answerDtos){
               if (answer.getQuestion() != null && answer.getAnswer() != null)

                    html += template
                            .replace(questionParam, answer.getQuestion().getQuestionName())
                            .replace(answerParam, answer.getAnswer().toString());
          }
          return html;
     }

     default List<AnswerDto> getAnswersList(){
          return new ArrayList<>();
     }
}
