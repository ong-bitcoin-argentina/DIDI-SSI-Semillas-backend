package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;


public interface Category {
     void loadData(AnswerRow answerRow);

     Category getData();

}
