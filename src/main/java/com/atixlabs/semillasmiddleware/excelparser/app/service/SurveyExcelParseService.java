package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import com.atixlabs.semillasmiddleware.filemanager.service.FileManagerService;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.pdfparser.surveyPdfParser.service.PdfParserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private CredentialService credentialService;

    @Autowired
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    private PdfParserService pdfParserService;

    @Autowired
    private FileManagerService fileManagerService;

    private static final String ZIP_SUFFIX = "Encuesta_form";
    private SurveyForm currentForm;
    private List<SurveyForm> surveyFormList;

    public void resetFormRelatedVariables(boolean createCredentials){
        if (currentForm == null){
            log.info("Building all form categories:");
            currentForm = new SurveyForm();
            currentForm.setCategoryList(answerCategoryFactory.getCategoryList(createCredentials));
        }

        if (surveyFormList == null)
            surveyFormList = new ArrayList<>();
    }

    public void clearFormRelatedVariables(){
        currentForm = null;
        if (Objects.nonNull(surveyFormList))
            surveyFormList.clear();
        surveyFormList = null;
    }

    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext,
                                             ProcessExcelFileResult processExcelFileResult, boolean createCredentials,
                                             boolean skipIdentityCredentials,
                                             boolean pdfValidation) throws FileManagerException, IOException {

        AnswerRow answerRow = null;
        try {
            answerRow = new AnswerRow(currentRow);
        } catch (InvalidRowException e) {
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader(String.valueOf(currentRow.getRowNum()))
                    .errorBody(e.toString())
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
        }

        if (answerRow != null) {
            if(!answerRow.isEmpty(currentRow)){
                resetFormRelatedVariables(createCredentials);
                processExcelFileResult.addTotalReadRow();
                if (answerRow.hasFormKeyValues()) {

                    if (!currentForm.isRowFromSameForm(answerRow)) {
                        endOfFormHandler(processExcelFileResult);
                        currentForm = new SurveyForm(answerRow);
                        currentForm.setCategoryList(answerCategoryFactory.getCategoryList( createCredentials));
                    }

                    currentForm.setCategoryData(answerRow, processExcelFileResult);
                    log.info("OK:" + answerRow.toString());
                }
                else{
                    processExcelFileResult.addEmptyRow();
                }
            }
        }
        if(!hasNext)
            endOfFileHandler(processExcelFileResult, createCredentials, skipIdentityCredentials, pdfValidation);

        return processExcelFileResult;
    }


    private void endOfFormHandler(ProcessExcelFileResult processExcelFileResult) {
        log.info("endOfFormHandler -> add form to surveyFormList");
        processExcelFileResult.addTotalProcessedForms();
        surveyFormList.add(currentForm);
    }
    private void endOfFileHandler(ProcessExcelFileResult processExcelFileResult, boolean createCredentials,
                                  boolean skipIdentityCredentials, boolean pdfValidation) throws FileManagerException, IOException {
        List<String> pdfsGenerated = new ArrayList<>();
        this.endOfFormHandler(processExcelFileResult);
        log.info("endOfFileHandler -> checking errors and building credentials");

        boolean allFormValid = true;

        for (SurveyForm surveyForm : surveyFormList) {
            if (!surveyForm.isValid(processExcelFileResult))
                allFormValid = false;
        }

        if(allFormValid) {
            log.info("endOfFileHandler -> all forms are ok: building credentials");
            for (SurveyForm surveyForm : surveyFormList) {
                pdfsGenerated.add(pdfParserService.generatePdfFromSurvey(surveyForm));
                if (createCredentials){
                    credentialService.buildAllCredentialsFromForm(surveyForm, skipIdentityCredentials);
                } else {
                    credentialService.validateAllCredentialsFromForm(surveyForm, processExcelFileResult,
                            skipIdentityCredentials, pdfValidation);
                }
            }

            setDownloadableFileName(pdfsGenerated, processExcelFileResult);
        }
        else
            log.info("endOfFileHandler -> there are forms with errors: stopping import");

        //todo: rows with multiple errors must be considered in next sprint
        processExcelFileResult.setTotalValidRows(processExcelFileResult.getTotalReadRows() - processExcelFileResult.getTotalErrorsRows());
        clearFormRelatedVariables();
    }

    private void setDownloadableFileName(List<String> pdfsGenerated, ProcessExcelFileResult processExcelFileResult) throws IOException {
        if(pdfsGenerated.size() > 1)
            processExcelFileResult.setDownloadableFileName(fileManagerService.zipAll(pdfsGenerated, ZIP_SUFFIX));
        else
            processExcelFileResult.setDownloadableFileName(pdfsGenerated.get(0));
    }
}
