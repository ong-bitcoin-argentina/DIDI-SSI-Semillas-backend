package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.BeneficiaryQuestions;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;

public class BeneficiaryCategory implements Category {
    Long idNumber;
    String nameAndSurname;

    @Override
    public void loadData(AnswerRow answerRow) {
        String question = StringUtil.cleanString(answerRow.getQuestion());
        switch (BeneficiaryQuestions.valueOf(question)){
            case ID_NUMBER:
                this.idNumber = answerRow.getAnswerAsLong();
                break;
            case NAME_AND_SURNAME:
                this.nameAndSurname = answerRow.getAnswerAsString();
                break;
        }

    }

    @Override
    public String toString() {
        return "BeneficiaryCategory{" +
                "idNumber='" + idNumber + '\'' +
                ", nameAndSurname='" + nameAndSurname +
                '}';
    }
}
