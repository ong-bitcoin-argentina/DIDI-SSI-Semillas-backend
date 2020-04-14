package com.atixlabs.semillasmiddleware.parser;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class ParserCategoriesTest {
    @Test
    public void answerCategoryFactoryReturnsSameCategoryObject() throws InvalidCategoryException, Exception {
        AnswerCategoryFactory answerCategoryFactory = new AnswerCategoryFactory();
        Assert.assertEquals(answerCategoryFactory.get("Emprendimiento"), answerCategoryFactory.get("EMPRENDIMIENTO"));
    }

    @Test(expected = InvalidCategoryException.class)
    public void invalidCategoryThrowsInvalidCategoryException() throws InvalidCategoryException, Exception {
        AnswerCategoryFactory answerCategoryFactory = new AnswerCategoryFactory();
        answerCategoryFactory.get("non-existent category");
    }

    @Test
    public void categoryFactoryAssignsChildAndSpouseTheCorrespondingEnum() throws Exception, InvalidCategoryException {
        AnswerCategoryFactory categoryFactory = new AnswerCategoryFactory();
        PersonCategory child = (PersonCategory) categoryFactory.get("datos hijo 1");
        Assert.assertEquals(child.getPersonType(), PersonType.CHILD);

        PersonCategory spouse = (PersonCategory) categoryFactory.get("DATOS DEL CÓNYUGE");
        Assert.assertEquals(spouse.getPersonType(), PersonType.SPOUSE);
    }

    @Test
    public void entrepreneurshipCategoryIsNotValidIfNameAndTypeAreMissing(){
        ProcessExcelFileResult excelFileResult = new ProcessExcelFileResult();
        EntrepreneurshipCategory entrepreneurshipCategory = new EntrepreneurshipCategory();
        entrepreneurshipCategory.setActivityStartDate(LocalDate.parse("2020-01-01"));
        entrepreneurshipCategory.setMainActivity("COMERCIO");
        entrepreneurshipCategory.setAddress("ADDRESS");
        entrepreneurshipCategory.setActivityEndingDate(LocalDate.parse("2021-01-01"));

        Assert.assertFalse(entrepreneurshipCategory.isValid(excelFileResult));
        System.out.println(excelFileResult.toString());
        Assert.assertEquals(2,excelFileResult.getTotalErrorsRows());
    }

    @Test
    public void entrepreneurshipCategoryIsValidIfOptionalEndingDateIsMissing() {
        ProcessExcelFileResult excelFileResult = new ProcessExcelFileResult();
        EntrepreneurshipCategory entrepreneurshipCategory = new EntrepreneurshipCategory();
        entrepreneurshipCategory.setType("COMERCIO");
        entrepreneurshipCategory.setActivityStartDate(LocalDate.parse("2020-01-01"));
        entrepreneurshipCategory.setMainActivity("COMERCIO");
        entrepreneurshipCategory.setAddress("ADDRESS");
        entrepreneurshipCategory.setName("NAME");

        Assert.assertTrue(entrepreneurshipCategory.isValid(excelFileResult));
    }

    @Test
    public void child2AndChild11ReturnDifferentObjects() throws InvalidCategoryException, Exception {
        AnswerCategoryFactory answerCategoryFactory = new AnswerCategoryFactory();
        Assert.assertNotEquals(answerCategoryFactory.get("DATOS HIJO 2"),answerCategoryFactory.get("DATOS HIJO 11"));
    }
}
