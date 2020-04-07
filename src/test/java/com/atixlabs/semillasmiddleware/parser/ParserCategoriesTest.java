package com.atixlabs.semillasmiddleware.parser;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;

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
    public void categoryFactoryAssignsChildItsCorrespondingEnum() throws Exception, InvalidCategoryException {
        AnswerCategoryFactory categoryFactory = new AnswerCategoryFactory();
        PersonCategory child = (PersonCategory) categoryFactory.get("DATOS HIJO 1");
        Assert.assertEquals(child.getPersonType(), PersonType.CHILD);

        PersonCategory spouse = (PersonCategory) categoryFactory.get("DATOS DEL CÓNYUGE");
        Assert.assertEquals(spouse.getPersonType(), PersonType.SPOUSE);
    }

}
