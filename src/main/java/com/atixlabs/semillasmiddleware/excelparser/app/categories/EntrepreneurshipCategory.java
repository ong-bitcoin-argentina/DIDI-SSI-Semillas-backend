package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.DwellingQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.EntrepreneurshipQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class EntrepreneurshipCategory implements Category {
    AnswerDto type;
    AnswerDto activityStartDate;
    AnswerDto mainActivity;
    AnswerDto name;
    AnswerDto address;
    AnswerDto activityEndingDate;

    public EntrepreneurshipCategory() {
        this.type = new AnswerDto(EntrepreneurshipQuestion.TYPE);
        this.activityStartDate = new AnswerDto(EntrepreneurshipQuestion.ACTIVITY_START_DATE);
        this.mainActivity = new AnswerDto(EntrepreneurshipQuestion.MAIN_ACTIVITY);
        this.name = new AnswerDto(EntrepreneurshipQuestion.NAME);
        this.address = new AnswerDto(EntrepreneurshipQuestion.ADDRESS);
        this.activityEndingDate = new AnswerDto(EntrepreneurshipQuestion.ACTIVITY_ENDING_DATE);
    }

    public void loadData(AnswerRow answerRow){
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        switch (EntrepreneurshipQuestion.get(question)){
            case TYPE:
                this.type.setAnswer(answerRow);
                break;
            case ACTIVITY_START_DATE:
                this.activityStartDate.setAnswer(answerRow);
                break;
            case MAIN_ACTIVITY:
                this.mainActivity.setAnswer(answerRow);
                break;
            case NAME:
                this.name.setAnswer(answerRow);
                break;
            case ADDRESS:
                this.address.setAnswer(answerRow);
                break;
            //check final form
            case ACTIVITY_ENDING_DATE:
                this.activityEndingDate.setAnswer(answerRow);
                break;
        }
    };

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.type);
        answers.add(this.activityStartDate);
        answers.add(this.mainActivity);
        answers.add(this.name);
        answers.add(this.address);
        answers.add(this.activityEndingDate);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }
}
