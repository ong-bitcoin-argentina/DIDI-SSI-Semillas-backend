package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.EntrepreneurshipQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter

public class EntrepreneurshipCategory implements Category {

    private String categoryOriginalName;
    private Categories categoryName;

    private AnswerDto type;
    private AnswerDto activityStartDate;
    private AnswerDto mainActivity;
    private AnswerDto name;
    private AnswerDto address;
    private AnswerDto activityEndingDate;
    private AnswerDto phone;
    private AnswerDto reset;
    private AnswerDto seniority;

    private AnswerDto outpatient;
    private AnswerDto fair;
    private AnswerDto storeOrHome;

    private AnswerDto daysPerWeek;
    private AnswerDto hoursPerWeek;

    private AnswerDto firstFortnight;
    private AnswerDto secondFortnight;
    private AnswerDto totalMonthlyEntry;

    private AnswerDto entryWeek1;
    private AnswerDto entryWeek2;
    private AnswerDto entryWeek3;
    private AnswerDto entryWeek4;
    private AnswerDto entryPerMonth;

    private AnswerDto exitRent;
    private AnswerDto exitWater;
    private AnswerDto exitElectricity;
    private AnswerDto exitShopping;
    private AnswerDto exitPhone;
    private AnswerDto exitTaxes;
    private AnswerDto exitTransport;
    private AnswerDto exitMaintenance;
    private AnswerDto exitEmployees;
    private AnswerDto exitOthers;
    private AnswerDto totalExit;

    private AnswerDto totalEntry;
    private AnswerDto totalExitRel;
    private AnswerDto entryExitRelationship;
    private AnswerDto entryExitRelationshipFortnight;
    private AnswerDto projection;
    private AnswerDto facebook;
    private AnswerDto photo;


    public EntrepreneurshipCategory(String categoryOriginalName) {
        this.type = new AnswerDto(EntrepreneurshipQuestion.TYPE);
        this.activityStartDate = new AnswerDto(EntrepreneurshipQuestion.ACTIVITY_START_DATE);
        this.mainActivity = new AnswerDto(EntrepreneurshipQuestion.MAIN_ACTIVITY);
        this.name = new AnswerDto(EntrepreneurshipQuestion.NAME);
        this.address = new AnswerDto(EntrepreneurshipQuestion.ADDRESS);
        this.activityEndingDate = new AnswerDto(EntrepreneurshipQuestion.ACTIVITY_ENDING_DATE);
        this.phone = new AnswerDto(EntrepreneurshipQuestion.PHONE);
        this.reset = new AnswerDto(EntrepreneurshipQuestion.RESET);
        this.seniority = new AnswerDto(EntrepreneurshipQuestion.SENIORITY);

        this.outpatient = new AnswerDto(EntrepreneurshipQuestion.OUTPATIENT);
        this.fair = new AnswerDto(EntrepreneurshipQuestion.FAIR);
        this.storeOrHome = new AnswerDto(EntrepreneurshipQuestion.STORE_OR_HOME);
        this.daysPerWeek = new AnswerDto(EntrepreneurshipQuestion.DAYS_PER_WEEK);
        this.hoursPerWeek = new AnswerDto(EntrepreneurshipQuestion.HOURS_PER_WEEK);
        this.firstFortnight = new AnswerDto(EntrepreneurshipQuestion.FIRST_FORTNIGHT);
        this.secondFortnight = new AnswerDto(EntrepreneurshipQuestion.SECOND_FORTNIGHT);

        this.totalMonthlyEntry = new AnswerDto(EntrepreneurshipQuestion.TOTAL_MONTHLY_ENTRY);
        this.entryWeek1 = new AnswerDto(EntrepreneurshipQuestion.ENTRY_WEEK_1);
        this.entryWeek2 = new AnswerDto(EntrepreneurshipQuestion.ENTRY_WEEK_2);
        this.entryWeek3 = new AnswerDto(EntrepreneurshipQuestion.ENTRY_WEEK_3);
        this.entryWeek4 = new AnswerDto(EntrepreneurshipQuestion.ENTRY_WEEK_4);
        this.entryPerMonth = new AnswerDto(EntrepreneurshipQuestion.ENTRY_PER_MONTH);
        this.exitRent = new AnswerDto(EntrepreneurshipQuestion.EXIT_RENT);
        this.exitWater = new AnswerDto(EntrepreneurshipQuestion.EXIT_WATER);
        this.exitElectricity = new AnswerDto(EntrepreneurshipQuestion.EXIT_ELECTRICITY);
        this.exitShopping = new AnswerDto(EntrepreneurshipQuestion.EXIT_SHOPPING);
        this.exitPhone = new AnswerDto(EntrepreneurshipQuestion.EXIT_PHONE);
        this.exitTaxes = new AnswerDto(EntrepreneurshipQuestion.EXIT_TAXES);
        this.exitTransport = new AnswerDto(EntrepreneurshipQuestion.EXIT_TRANSPORT);
        this.exitMaintenance = new AnswerDto(EntrepreneurshipQuestion.EXIT_MAINTENANCE);
        this.exitEmployees = new AnswerDto(EntrepreneurshipQuestion.EXIT_EMPLOYEES);
        this.exitOthers = new AnswerDto(EntrepreneurshipQuestion.EXIT_OTHERS);
        this.totalExit = new AnswerDto(EntrepreneurshipQuestion.TOTAL_EXIT);
        this.totalEntry = new AnswerDto(EntrepreneurshipQuestion.TOTAL_ENTRY);
        this.totalExitRel = new AnswerDto(EntrepreneurshipQuestion.TOTAL_EXIT_REL);
        this.entryExitRelationship = new AnswerDto(EntrepreneurshipQuestion.ENTRY_EXIT_RELATIONSHIP);
        this.entryExitRelationshipFortnight = new AnswerDto(EntrepreneurshipQuestion.ENTRY_EXIT_RELATIONSHIP_FORTNIGHT);
        this.projection = new AnswerDto(EntrepreneurshipQuestion.PROJECTION);
        this.facebook = new AnswerDto(EntrepreneurshipQuestion.FACEBOOK);
        this.photo = new AnswerDto(EntrepreneurshipQuestion.PHOTO);

        this.categoryOriginalName = categoryOriginalName;
        this.categoryName = Categories.ENTREPRENEURSHIP_CATEGORY_NAME;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        EntrepreneurshipQuestion questionMatch = null;

        questionMatch = EntrepreneurshipQuestion.getEnumByStringValue(question);

        if (questionMatch==null)
            return;

        switch (questionMatch){
            case TYPE:
                this.type.setAnswer(answerRow, processExcelFileResult);
                break;
            case ACTIVITY_START_DATE:
                //possible data: dd/mm/yyyy or only date: 2020
                this.activityStartDate.setAnswer(answerRow, processExcelFileResult);
               // this.activityStartDate.setAnswer();
                break;
            case MAIN_ACTIVITY:
                this.mainActivity.setAnswer(answerRow, processExcelFileResult);
                break;
            case NAME:
                this.name.setAnswer(answerRow, processExcelFileResult);
                break;
            case ADDRESS:
                this.address.setAnswer(answerRow, processExcelFileResult);
                break;
            //check final form
            case ACTIVITY_ENDING_DATE:
                this.activityEndingDate.setAnswer(answerRow, processExcelFileResult);
                break;
            case PHONE:
                this.phone.setAnswer(answerRow, processExcelFileResult);
                break;
            case RESET:
                this.reset.setAnswer(answerRow, processExcelFileResult);
                break;
            case SENIORITY:
                this.seniority.setAnswer(answerRow, processExcelFileResult);
                break;
            case OUTPATIENT:
                this.outpatient.setAnswer(answerRow, processExcelFileResult);
                break;
            case STORE_OR_HOME:
                this.storeOrHome.setAnswer(answerRow, processExcelFileResult);
                break;
            case DAYS_PER_WEEK:
                this.daysPerWeek.setAnswer(answerRow, processExcelFileResult);
                break;
            case HOURS_PER_WEEK:
                this.hoursPerWeek.setAnswer(answerRow, processExcelFileResult);
                break;
            case FIRST_FORTNIGHT:
                this.firstFortnight.setAnswer(answerRow, processExcelFileResult);
                break;
            case SECOND_FORTNIGHT:
                this.secondFortnight.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_ENTRY:
                this.totalMonthlyEntry.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_WEEK_1:
                this.entryWeek1.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_WEEK_2:
                this.entryWeek2.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_WEEK_3:
                this.entryWeek3.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_WEEK_4:
                this.entryWeek4.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_PER_MONTH:
                this.entryPerMonth.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_RENT:
                this.exitRent.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_WATER:
                this.exitWater.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_ELECTRICITY:
                this.exitElectricity.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_SHOPPING:
                this.exitShopping.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_PHONE:
                this.exitPhone.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_TAXES:
                this.exitTaxes.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_TRANSPORT:
                this.exitTransport.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_MAINTENANCE:
                this.exitMaintenance.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_EMPLOYEES:
                this.exitEmployees.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_OTHERS:
                this.exitOthers.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_EXIT:
                this.totalExit.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_ENTRY:
                this.totalEntry.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_EXIT_REL:
                this.totalExitRel.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_EXIT_RELATIONSHIP:
                this.entryExitRelationship.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_EXIT_RELATIONSHIP_FORTNIGHT:
                this.entryExitRelationshipFortnight.setAnswer(answerRow, processExcelFileResult);
                break;
            case PROJECTION:
                this.projection.setAnswer(answerRow, processExcelFileResult);
                break;
            case FACEBOOK:
                this.facebook.setAnswer(answerRow, processExcelFileResult);
                break;
            case PHOTO:
                this.photo.setAnswer(answerRow, processExcelFileResult);
                break;
        }
    }

    @Override
    public String getCategoryUniqueName(){
        return categoryOriginalName;
    }

    @Override
    public Categories getCategoryName(){return categoryName;}

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.type);
        answers.add(this.activityStartDate);
        answers.add(this.mainActivity);
        answers.add(this.name);
        answers.add(this.address);
        answers.add(this.activityEndingDate);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult, categoryOriginalName)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }

    @Override
    public boolean isEmpty() {
        return type.answerIsEmpty() && activityStartDate.answerIsEmpty() && mainActivity.answerIsEmpty() && name.answerIsEmpty() && address.answerIsEmpty() && activityEndingDate.answerIsEmpty();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    public String getType(){
        return (String) this.type.getAnswer();
    }
    public LocalDate getActivityStartDate(){
        return (LocalDate) this.activityStartDate.getAnswer();
    }
    public String getMainActivity(){
        return (String) this.mainActivity.getAnswer();
    }
    public String getName(){
        return (String) this.name.getAnswer();
    }
    public String getAddress(){
        return (String) this.address.getAnswer();
    }
    public LocalDate getActivityEndingDate(){
        return (LocalDate) this.activityEndingDate.getAnswer();
    }

    @Override
    public String toString() {
        return "EntrepreneurshipCategory{" +
                "categoryOriginalName='" + categoryOriginalName + '\'' +
                ", type=" + type +
                ", activityStartDate=" + activityStartDate +
                ", mainActivity=" + mainActivity +
                ", name=" + name +
                ", address=" + address +
                ", activityEndingDate=" + activityEndingDate +
                ", phone=" + phone +
                ", reset=" + reset +
                ", seniority=" + seniority +
                ", outpatient=" + outpatient +
                ", fair=" + fair +
                ", storeOrHome=" + storeOrHome +
                ", daysPerWeek=" + daysPerWeek +
                ", hoursPerWeek=" + hoursPerWeek +
                ", firstFortnight=" + firstFortnight +
                ", secondFortnight=" + secondFortnight +
                ", totalMonthlyEntry=" + totalMonthlyEntry +
                ", entryWeek1=" + entryWeek1 +
                ", entryWeek2=" + entryWeek2 +
                ", entryWeek3=" + entryWeek3 +
                ", entryWeek4=" + entryWeek4 +
                ", entryPerMonth=" + entryPerMonth +
                ", exitRent=" + exitRent +
                ", exitWater=" + exitWater +
                ", exitElectricity=" + exitElectricity +
                ", exitShopping=" + exitShopping +
                ", exitPhone=" + exitPhone +
                ", exitTaxes=" + exitTaxes +
                ", exitTransport=" + exitTransport +
                ", exitMaintenance=" + exitMaintenance +
                ", exitEmployees=" + exitEmployees +
                ", exitOthers=" + exitOthers +
                ", totalExit=" + totalExit +
                ", totalEntry=" + totalEntry +
                ", totalExitRel=" + totalExitRel +
                ", entryExitRelationship=" + entryExitRelationship +
                ", entryExitRelationshipFortnight=" + entryExitRelationshipFortnight +
                ", projection=" + projection +
                ", facebook=" + facebook +
                ", photo=" + photo +
                '}';
    }

    @Override
    public List<AnswerDto> getAnswersList(){
        return Arrays.asList(type, activityStartDate, mainActivity, name, address, activityEndingDate, phone, reset, seniority, outpatient,
                fair, storeOrHome, daysPerWeek, hoursPerWeek, firstFortnight, secondFortnight, totalMonthlyEntry,
                entryWeek1, entryWeek2, entryWeek3, entryWeek4, entryPerMonth, exitRent, exitWater, exitElectricity,
                exitShopping, exitPhone, exitTaxes, exitTransport, exitMaintenance, exitEmployees, exitOthers,
                totalExit, totalEntry, totalExitRel, entryExitRelationship, entryExitRelationshipFortnight,
                projection, facebook, photo
        );
    }
}
