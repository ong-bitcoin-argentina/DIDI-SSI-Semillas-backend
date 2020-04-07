package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
@Slf4j
public class SurveyExcelParseService extends ExcelParseService {

    /**
     * Levanta el archivo y lee linea por linea
     *
     * @param row
     * @return
     * @throws FileNotFoundException
     */

    @Autowired
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    private SurveyForm surveyForm;

    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult){

        AnswerRow answerRow = null;
        try {
            answerRow = new AnswerRow(currentRow);
        } catch (InvalidRowException e) {
            processExcelFileResult.addRowError("("+currentRow.getRowNum()+"): "+ e.toString());
        }

        processExcelFileResult.addTotalRow();

        if (answerRow.isValid()){
            processExcelFileResult.addValidRows();
            processExcelFileResult.addInsertedRows();

            if (surveyForm.isEmpty())
                surveyForm.initialize(answerRow);

            if (!surveyForm.isRowFromSameForm(answerRow)) {
                endOfFormHandler();
                surveyForm.clearForm();
                surveyForm.initialize(answerRow);
            }

            addCategoryDataIntoForm(answerRow, processExcelFileResult);
            log.info(answerRow.toString());
        }
        else
            processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): "+ answerRow.getErrorMessage());

        if(!hasNext)
            endOfFileHandler();

        return processExcelFileResult;
    }

    private void addCategoryDataIntoForm(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){
        try {
            Category category = answerCategoryFactory.get(answerRow.getCategory());
            category.loadData(answerRow);
            surveyForm.addCategory(category);
        }
        catch (Exception | InvalidCategoryException e) {
            processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): "+ e.toString());
        }
    }

    private void endOfFormHandler(){
        log.info("endOfFormHandler");
        //log.info(surveyForm.toString());
        surveyForm.buildCredentials();
    }
    private void endOfFileHandler(){
        log.info("endOfFileHandler");
        //log.info(surveyForm.toString());
        surveyForm.buildCredentials();
    }
}
