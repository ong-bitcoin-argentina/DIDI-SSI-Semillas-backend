package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
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

    //AnswerCategoryFactory answerCategoryFactory = new AnswerCategoryFactory();
    //SurveyForm surveyForm = new SurveyForm();

    @Autowired
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    private SurveyForm surveyForm;

    public SurveyExcelParseService(){
        surveyForm = new SurveyForm();
    }



    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult) throws Exception, InvalidCategoryException {

        AnswerRow answerRow = new AnswerRow(currentRow);
        processExcelFileResult.addTotalRow();

        if (!answerRow.isValid()) {
            log.info("Invalid row: "+answerRow.toString());
            processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): "+ answerRow.getErrorMessage());
        }
        else {
            log.info(answerRow.toString());
            processExcelFileResult.addValidRows();
            processExcelFileResult.addInsertedRows();

            //Inicializa formulario con datos clave
            if (!surveyForm.isInitialized())
                surveyForm.initialize(answerRow);

            Category category = answerCategoryFactory.get(answerRow.getCategory());
            category.loadData(answerRow);

            surveyForm.addCategory(category);

            //Verifica que row sea del mismo form
            if (!surveyForm.isRowFromSameForm(answerRow)) {
                log.info("End of form");
                surveyForm.buildCredentials();
                surveyForm.clearForm();
            }
        }
        if(!hasNext){
            log.info("End of file");
            surveyForm.buildCredentials();
        }
        return processExcelFileResult;
    }


}
