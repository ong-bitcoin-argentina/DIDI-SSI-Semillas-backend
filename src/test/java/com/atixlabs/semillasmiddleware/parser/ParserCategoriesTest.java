package com.atixlabs.semillasmiddleware.parser;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class ParserCategoriesTest {
    AnswerCategoryFactory answerCategoryFactory;
    AnswerRow answerRowActivityStartDate;
    AnswerRow answerRowMainActivity;
    AnswerRow answerRowAddress;
    AnswerRow answerRowActivityEndingDate;
    AnswerRow answerRowName;
    AnswerRow answerRowType;
    Workbook wb;
    Sheet sheet;
    Row row3;
    Row row4;
    Row row5;
    Cell answerCell3;
    Cell answerCell4;
    Cell answerCell5;

    @Before
    public void init() throws InvalidRowException {
        answerCategoryFactory = new AnswerCategoryFactory();

        wb = new HSSFWorkbook();
        sheet = wb.createSheet();
        Row row = sheet.createRow(8);
        System.out.println(row.getRowNum());
        row.setRowNum(8);
        Cell surveyCell = row.createCell(7);
        surveyCell.setCellValue("SURVEY-1");
        Cell dateCell = row.createCell(9);
        dateCell.setCellValue("12/12/2019");
        Cell pdvCell = row.createCell(10);
        pdvCell.setCellValue(5456580);
        Cell categoryCell = row.createCell(14);
        categoryCell.setCellValue("EMPRENDIMIENTO");
        Cell questionCell = row.createCell(15);
        questionCell.setCellValue("FECHA DE INICIO / REINICIO");
        Cell answerCell = row.createCell(16);
        answerCell.setCellValue("03/04/2020");

        Row row1 = sheet.createRow(9);
        row1.setRowNum(9);
        Cell surveyCell1 = row1.createCell(7);
        surveyCell1.setCellValue("SURVEY-1");
        Cell dateCell1 = row1.createCell(9);
        dateCell1.setCellValue("12/12/2019");
        Cell pdvCell1 = row1.createCell(10);
        pdvCell1.setCellValue(5456580);
        Cell categoryCell1 = row1.createCell(14);
        categoryCell1.setCellValue("EMPRENDIMIENTO");
        Cell questionCell1 = row1.createCell(15);
        questionCell1.setCellValue("ACTIVIDAD PRINCIPAL");
        Cell answerCell1 = row1.createCell(16);
        answerCell1.setCellValue("COMERCIO");


        Row row2 = sheet.createRow(10);
        row2.setRowNum(10);
        Cell surveyCell2 = row2.createCell(7);
        surveyCell2.setCellValue("SURVEY-1");
        Cell dateCell2 = row2.createCell(9);
        dateCell2.setCellValue("12/12/2019");
        Cell pdvCell2 = row2.createCell(10);
        pdvCell2.setCellValue(5456580);
        Cell categoryCell2 = row2.createCell(14);
        categoryCell2.setCellValue("EMPRENDIMIENTO");
        Cell questionCell2 = row2.createCell(15);
        questionCell2.setCellValue("DIRECCION");
        Cell answerCell2 = row2.createCell(16);
        answerCell2.setCellValue("ADDRESS 123");

        row3 =sheet.createRow(11);
        row3.setRowNum(11);
        Cell surveyCell3 = row3.createCell(7);
        surveyCell3.setCellValue("SURVEY-1");
        Cell dateCell3 = row3.createCell(9);
        dateCell3.setCellValue("12/12/2019");
        Cell pdvCell3 = row3.createCell(10);
        pdvCell3.setCellValue(5456580);
        Cell categoryCell3 = row3.createCell(14);
        categoryCell3.setCellValue("EMPRENDIMIENTO");
        Cell questionCell3 = row3.createCell(15);
        questionCell3.setCellValue("FIN DE LA ACTIVIDAD");
        answerCell3 = row3.createCell(16);
        answerCell3.setCellValue("12/12/2013");

        row4= sheet.createRow(12);
        row4.setRowNum(12);
        Cell surveyCell4 = row4.createCell(7);
        surveyCell4.setCellValue("SURVEY-1");
        Cell dateCell4 = row4.createCell(9);
        dateCell4.setCellValue("12/12/2019");
        Cell pdvCell4 = row4.createCell(10);
        pdvCell4.setCellValue(5456580);
        Cell categoryCell4 = row4.createCell(14);
        categoryCell4.setCellValue("EMPRENDIMIENTO");
        Cell questionCell4 = row4.createCell(15);
        questionCell4.setCellValue("NOMBRE EMPRENDIMIENTO");
        answerCell4 = row4.createCell(16);
        answerCell4.setCellValue("");


        row5 = sheet.createRow(13);
        row5.setRowNum(13);
        Cell surveyCell5 = row5.createCell(7);
        surveyCell5.setCellValue("SURVEY-1");
        Cell dateCell5 = row5.createCell(9);
        dateCell5.setCellValue("12/12/2019");
        Cell pdvCell5 = row5.createCell(10);
        pdvCell5.setCellValue(5456580);
        Cell categoryCell5 = row5.createCell(14);
        categoryCell5.setCellValue("EMPRENDIMIENTO");
        Cell questionCell5 = row5.createCell(15);
        questionCell5.setCellValue("TIPO DE EMPRENDIMIENTO");
        answerCell5 = row5.createCell(16);
        answerCell5.setCellValue("");

        answerRowActivityStartDate = new AnswerRow(row);
        answerRowMainActivity = new AnswerRow(row1);
        answerRowAddress = new AnswerRow(row2);
        answerRowActivityEndingDate = new AnswerRow(row3);
        answerRowName = new AnswerRow(row4);
        answerRowType = new AnswerRow(row5);

    }

    @Test
    public void answerCategoryFactoryReturnsSameCategoryObject(){

        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setCategoryList(answerCategoryFactory.getCategoryList(true));
        Assert.assertEquals(
                surveyForm.getCategoryByUniqueName("Emprendimiento", null),
                surveyForm.getCategoryByUniqueName("EMPRENDIMIENTO", null)
        );

    }

    @Test
    public void invalidCategoryThrowsInvalidCategoryException(){
        //answerCategoryFactory.get("non-existent category");
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setCategoryList(answerCategoryFactory.getCategoryList(true));
        Assert.assertNull(surveyForm.getCategoryByUniqueName("non-existent-category", null));
    }

    @Test
    public void categoryFactoryAssignsChildAndSpouseTheCorrespondingEnum(){
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setCategoryList(answerCategoryFactory.getCategoryList(true));



        PersonCategory child = (PersonCategory) surveyForm.getCategoryByUniqueName("datos hijo 1", null);
        Assert.assertEquals(child.getPersonType(), PersonType.CHILD);

        PersonCategory spouse = (PersonCategory) surveyForm.getCategoryByUniqueName("DATOS DEL CÓNYUGE", null);
        Assert.assertEquals(spouse.getPersonType(), PersonType.SPOUSE);
    }

    @Test
    public void entrepreneurshipCategoryIsNotValidIfNameAndTypeAreMissing() {
        ProcessExcelFileResult excelFileResult = new ProcessExcelFileResult();
        EntrepreneurshipCategory entrepreneurshipCategory = new EntrepreneurshipCategory("EMPRENDIMIENTO", Categories.ENTREPRENEURSHIP_CATEGORY_NAME);

        /*
        TYPE("TIPO DE EMPRENDIMIENTO"),
        ACTIVITY_START_DATE("FECHA DE INICIO / REINICIO"
        MAIN_ACTIVITY("ACTIVIDAD PRINCIPAL"),
        NAME("NOMBRE EMPRENDIMIENTO"),
        ADDRESS("DIRECCION"),
        ACTIVITY_ENDING_DATE("FIN DE LA ACTIVIDAD")
        */
        entrepreneurshipCategory.loadData(answerRowType, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowActivityStartDate, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowMainActivity, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowName, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowAddress, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowActivityEndingDate, excelFileResult);

        Assert.assertFalse(entrepreneurshipCategory.isValid(excelFileResult));
//TODO        Assert.assertEquals(2,excelFileResult.getTotalErrorsRows());
    }

    @Test
    public void entrepreneurshipCategoryIsValidIfOptionalEndingDateIsMissing() throws InvalidRowException {
        ProcessExcelFileResult excelFileResult = new ProcessExcelFileResult();
        EntrepreneurshipCategory entrepreneurshipCategory = new EntrepreneurshipCategory("EMPRENDIMIENTO", Categories.ENTREPRENEURSHIP_CATEGORY_NAME);

        answerCell3.setCellValue("");
        answerRowActivityEndingDate = new AnswerRow(row3);

        answerCell4.setCellValue("NOMBRE DE EMPRENDIMIENTO");
        answerRowName = new AnswerRow(row4);

        answerCell5.setCellValue("TIPO DE EMPRENDIMIENTO");
        answerRowType = new AnswerRow(row5);

        entrepreneurshipCategory.loadData(answerRowType, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowActivityStartDate, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowMainActivity, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowName, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowAddress, excelFileResult);
        entrepreneurshipCategory.loadData(answerRowActivityEndingDate, excelFileResult);

        log.info(String.valueOf(entrepreneurshipCategory.isValid(excelFileResult)));
        log.info(excelFileResult.toString());
       //TODO  Assert.assertTrue(entrepreneurshipCategory.isValid(excelFileResult));

    }

    @Test
    public void child2AndChild11ReturnDifferentObjects() {

        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setCategoryList(answerCategoryFactory.getCategoryList(true));
        Assert.assertNotEquals(
                surveyForm.getCategoryByUniqueName("DATOS HIJO 2", null),
                surveyForm.getCategoryByUniqueName("DATOS HIJO 11", null)
        );

    }
}
