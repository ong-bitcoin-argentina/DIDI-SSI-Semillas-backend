package com.atixlabs.semillasmiddleware.parser;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import org.junit.Assert;
import org.junit.Test;

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

}
