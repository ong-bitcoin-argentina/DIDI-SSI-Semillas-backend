package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;

import java.time.LocalDate;

public class EntrepreneurshipCategory implements Category {
    String type;
    LocalDate activityStartDate;
    String mainActivity;
    String name;
    String address;
    LocalDate activityEndingDate;


    public void loadData(AnswerRow answerRow){
        switch (StringUtil.cleanString(answerRow.getQuestion())){
            case "TIPO DE EMPRENDIMIENTO":
                this.type = answerRow.getAnswerAsString();
                break;
            case "FECHA DE INICIO / REINICIO":
                this.activityStartDate = answerRow.getAnswerAsDate("dd/MM/yy");
                break;
            case "ACTIVIDAD PRINCIPAL":
                this.mainActivity = answerRow.getAnswerAsString();
                break;
            case "NOMBRE EMPRENDIMIENTO":
                this.name = answerRow.getAnswerAsString();
                break;
            case "DIRECCION":
                this.address = answerRow.getAnswerAsString();
                break;
            //check final form
            case "FIN DE LA ACTIVIDAD":
                this.activityEndingDate = answerRow.getAnswerAsDate("dd/MM/yy");
                break;
        }
    };
}
