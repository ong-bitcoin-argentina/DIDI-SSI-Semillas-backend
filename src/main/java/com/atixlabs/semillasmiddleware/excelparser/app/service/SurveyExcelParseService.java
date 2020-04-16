package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.service.CredentialService;
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
import java.util.ArrayList;
import java.util.List;

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
    CredentialService credentialService;

    /**
     * currentForm is shared between the whole Excel file.
     * it must be @Autowired to keep state of previous row key data,
     * in order to allow currentForm.isRowFromSameForm() to work properly.
     */
    @Autowired
    private SurveyForm currentForm;

    private List<SurveyForm> surveyFormList = new ArrayList<>();

    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult){

        AnswerRow answerRow = null;
        try {
            answerRow = new AnswerRow(currentRow);
        } catch (InvalidRowException e) {
            processExcelFileResult.addRowError("("+currentRow.getRowNum()+"): "+ e.toString());
        }

        processExcelFileResult.addTotalRow();

        if (answerRow != null) {
            if (answerRow.hasFormKeyValues()) {
                processExcelFileResult.addValidRows();
                processExcelFileResult.addInsertedRows();

                if (currentForm.isEmpty())
                    currentForm.initialize(answerRow);

                if (!currentForm.isRowFromSameForm(answerRow)) {
                    endOfFormHandler(processExcelFileResult);
                    currentForm.reset();
                    currentForm.initialize(answerRow);
                    //Also reset Factory:
                    answerCategoryFactory.reset();
                }

                addCategoryDataIntoForm(answerRow, processExcelFileResult);
                log.info("OK:" + answerRow.toString());
            } else {
                processExcelFileResult.addRowError("["+answerRow.getRowNum()+"]:Empty Row");// ("+answerRow.getStringError()+")");
            }
        }
        if(!hasNext)
            endOfFileHandler(processExcelFileResult);

        return processExcelFileResult;
    }

    private void addCategoryDataIntoForm(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){

        try {
            Category category = answerCategoryFactory.get(answerRow.getCategory());
            category.loadData(answerRow);
            if (!answerRow.getErrorMessage().isEmpty()){
                processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): " + answerRow.getErrorMessage());
            }
            currentForm.addCategory(category);
        }
        catch (Exception | InvalidCategoryException e) {
            processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): "+ e.toString());
        }
    }

    private void endOfFormHandler(ProcessExcelFileResult processExcelFileResult){
        log.info("endOfFormHandler -> add form to surveyFormList");
        processExcelFileResult.addProcessedForms();
        surveyFormList.add(currentForm);
    }
    private void endOfFileHandler(ProcessExcelFileResult processExcelFileResult){
        this.endOfFormHandler(processExcelFileResult);
        log.info("endOfFileHandler -> checking errors and building credentials");

        boolean allFormValid = true;

        /*Temporarily disable validations to test repository save
        for (SurveyForm surveyForm : surveyFormList) {
            if (!surveyForm.isValid(processExcelFileResult))
                allFormValid = false;
        }
         */

        if(allFormValid) {
            log.info("endOfFileHandler -> all forms are ok: building credentials");
            for (SurveyForm surveyForm : surveyFormList) {
                    credentialService.buildAllCredentialsFromForm(surveyForm);
            }
        }
        else
            log.info("endOfFileHandler -> there are forms with errors: stopping import");

    }
}
