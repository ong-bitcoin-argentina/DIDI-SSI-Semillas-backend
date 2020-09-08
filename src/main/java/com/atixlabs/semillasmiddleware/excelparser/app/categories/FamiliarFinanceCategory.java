package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.FamiliarFinanceQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter

public class FamiliarFinanceCategory implements Category {
    private String categoryOriginalName;
    private Categories categoryName;

    private AnswerDto entryEntrepreneurship;
    private AnswerDto entryApplicant;
    private AnswerDto entryFamily;
    private AnswerDto totalMonthlyEntry;
    private AnswerDto familiarSurplusFortnight;
    private AnswerDto exitFeeding;
    private AnswerDto exitGas;
    private AnswerDto exitEducation;
    private AnswerDto exitTransport;
    private AnswerDto exitWater;
    private AnswerDto exitElectricity;
    private AnswerDto exitPhone;
    private AnswerDto exitFit;
    private AnswerDto exitTaxes;
    private AnswerDto exitClothing;
    private AnswerDto exitRent;
    private AnswerDto exitOil;
    private AnswerDto exitCredits;
    private AnswerDto exitLeisure;
    private AnswerDto exitGambling;
    private AnswerDto exitTv;
    private AnswerDto exitInternet;
    private AnswerDto exitOthers;
    private AnswerDto totalMonthlyExit;
    private AnswerDto totalMonthlyEntry2;
    private AnswerDto totalMonthlyExit2;
    private AnswerDto totalMonthlyFamiliarSurplus;

    public FamiliarFinanceCategory(String categoryOriginalName) {

        this.entryEntrepreneurship = new AnswerDto(FamiliarFinanceQuestion.ENTRY_ENTREPRENEURSHIP);
        this.entryApplicant = new AnswerDto(FamiliarFinanceQuestion.ENTRY_APPLICANT);
        this.entryFamily = new AnswerDto(FamiliarFinanceQuestion.ENTRY_FAMILY);
        this.totalMonthlyEntry = new AnswerDto(FamiliarFinanceQuestion.TOTAL_MONTHLY_ENTRY);
        this.familiarSurplusFortnight = new AnswerDto(FamiliarFinanceQuestion.FAMILIAR_SURPLUS_FORTNIGHT);
        this.exitFeeding = new AnswerDto(FamiliarFinanceQuestion.EXIT_FEEDING);
        this.exitGas = new AnswerDto(FamiliarFinanceQuestion.EXIT_GAS);
        this.exitEducation = new AnswerDto(FamiliarFinanceQuestion.EXIT_EDUCATION);
        this.exitTransport = new AnswerDto(FamiliarFinanceQuestion.EXIT_TRANSPORT);
        this.exitWater = new AnswerDto(FamiliarFinanceQuestion.EXIT_WATER);
        this.exitElectricity = new AnswerDto(FamiliarFinanceQuestion.EXIT_ELECTRICITY);
        this.exitPhone = new AnswerDto(FamiliarFinanceQuestion.EXIT_PHONE);
        this.exitFit = new AnswerDto(FamiliarFinanceQuestion.EXIT_FIT);
        this.exitTaxes = new AnswerDto(FamiliarFinanceQuestion.EXIT_TAXES);
        this.exitClothing = new AnswerDto(FamiliarFinanceQuestion.EXIT_CLOTHING);
        this.exitRent = new AnswerDto(FamiliarFinanceQuestion.EXIT_RENT);;
        this.exitOil = new AnswerDto(FamiliarFinanceQuestion.EXIT_OIL);
        this.exitCredits = new AnswerDto(FamiliarFinanceQuestion.EXIT_CREDITS);
        this.exitLeisure = new AnswerDto(FamiliarFinanceQuestion.EXIT_LEISURE);
        this.exitGambling = new AnswerDto(FamiliarFinanceQuestion.EXIT_GAMBLING);
        this.exitTv = new AnswerDto(FamiliarFinanceQuestion.EXIT_TV);
        this.exitInternet = new AnswerDto(FamiliarFinanceQuestion.EXIT_INTERNET);
        this.exitOthers = new AnswerDto(FamiliarFinanceQuestion.EXIT_OTHERS);
        this.totalMonthlyExit = new AnswerDto(FamiliarFinanceQuestion.TOTAL_MONTHLY_EXIT);
        this.totalMonthlyEntry2 = new AnswerDto(FamiliarFinanceQuestion.TOTAL_MONTHLY_ENTRY_2);
        this.totalMonthlyExit2 = new AnswerDto(FamiliarFinanceQuestion.TOTAL_MONTHLY_EXIT_2);
        this.totalMonthlyFamiliarSurplus = new AnswerDto(FamiliarFinanceQuestion.TOTAL_MONTHLY_FAMILIAR_SURPLUS);

        this.categoryOriginalName = categoryOriginalName;
        this.categoryName = Categories.FAMILIAR_FINANCE_CATEGORY_NAME;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        FamiliarFinanceQuestion questionMatch = null;

        questionMatch = FamiliarFinanceQuestion.getEnumByStringValue(question);

        if (questionMatch == null)
            return;

        switch (questionMatch) {
            case ENTRY_ENTREPRENEURSHIP:
                this.entryEntrepreneurship.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_APPLICANT:
                this.entryApplicant.setAnswer(answerRow, processExcelFileResult);
                break;
            case ENTRY_FAMILY:
                this.entryFamily.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_ENTRY:
                this.totalMonthlyEntry.setAnswer(answerRow, processExcelFileResult);
                break;
            case FAMILIAR_SURPLUS_FORTNIGHT:
                this.familiarSurplusFortnight.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_FEEDING:
                this.exitFeeding.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_GAS:
                this.exitGas.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_EDUCATION:
                this.exitEducation.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_TRANSPORT:
                this.exitTransport.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_WATER:
                this.exitWater.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_ELECTRICITY:
                this.exitElectricity.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_PHONE:
                this.exitPhone.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_FIT:
                this.exitFit.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_TAXES:
                this.exitTaxes.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_CLOTHING:
                this.exitClothing.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_RENT:
                this.exitRent.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_OIL:
                this.exitOil.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_CREDITS:
                this.exitCredits.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_LEISURE:
                this.exitLeisure.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_GAMBLING:
                this.exitGambling.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_TV:
                this.exitTv.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_INTERNET:
                this.exitInternet.setAnswer(answerRow, processExcelFileResult);
                break;
            case EXIT_OTHERS:
                this.exitOthers.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_EXIT:
                this.totalMonthlyExit.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_ENTRY_2:
                this.totalMonthlyEntry2.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_EXIT_2:
                this.totalMonthlyExit2.setAnswer(answerRow, processExcelFileResult);
                break;
            case TOTAL_MONTHLY_FAMILIAR_SURPLUS:
                this.totalMonthlyFamiliarSurplus.setAnswer(answerRow, processExcelFileResult);
                break;
        }
    }

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getCategoryUniqueName() {
        return categoryOriginalName;
    }

    @Override
    public Categories getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return "FinancialSituationCategory{" +
                "categoryOriginalName='" + categoryOriginalName + '\'' +
                ", entryEntrepreneurship=" + entryEntrepreneurship +
                ", entryApplicant=" + entryApplicant +
                ", entryFamily=" + entryFamily +
                ", totalMonthlyEntry=" + totalMonthlyEntry +
                ", familiarSurplusFortnight=" + familiarSurplusFortnight +
                ", exitFeeding=" + exitFeeding +
                ", exitGas=" + exitGas +
                ", exitEducation=" + exitEducation +
                ", exitTransport=" + exitTransport +
                ", exitWater=" + exitWater +
                ", exitElectricity=" + exitElectricity +
                ", exitPhone=" + exitPhone +
                ", exitFit=" + exitFit +
                ", exitTaxes=" + exitTaxes +
                ", exitClothing=" + exitClothing +
                ", exitRent=" + exitRent +
                ", exitOil=" + exitOil +
                ", exitCredits=" + exitCredits +
                ", exitLeisure=" + exitLeisure +
                ", exitGambling=" + exitGambling +
                ", exitTv=" + exitTv +
                ", exitInternet=" + exitInternet +
                ", exitOthers=" + exitOthers +
                ", totalMonthlyExit=" + totalMonthlyExit +
                ", totalMonthlyEntry2=" + totalMonthlyEntry2 +
                ", totalMonthlyExit2=" + totalMonthlyExit2 +
                ", totalMonthlyFamiliarSurplus=" + totalMonthlyFamiliarSurplus +
                "}";
    }

    @Override
    public List<AnswerDto> getAnswersList() {
        return Arrays.asList(
                entryEntrepreneurship, entryApplicant, entryFamily, totalMonthlyEntry, familiarSurplusFortnight, exitFeeding, exitGas,
                exitEducation, exitTransport, exitWater, exitElectricity, exitPhone, exitFit, exitTaxes, exitClothing, exitRent, exitOil,
                exitCredits, exitLeisure, exitGambling, exitTv, exitInternet, exitOthers, totalMonthlyExit, totalMonthlyEntry2, totalMonthlyExit2,
                totalMonthlyFamiliarSurplus
        );
    }
}