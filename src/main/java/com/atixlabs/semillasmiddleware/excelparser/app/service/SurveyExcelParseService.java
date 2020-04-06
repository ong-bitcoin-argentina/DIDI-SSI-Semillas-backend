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
    public ProcessExcelFileResult processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws Exception, InvalidCategoryException {

        //todo: comprobar que la clave indica que es el mismo formulario.
        //todo: si es el mismo continuar, sino inciar nuevo surveryForm.

        //visualizo cada linea procesada en formato simil tabla.

        AnswerRow answerRow = new AnswerRow(row);
        //log.info(answerRow.toString()); //por ahora no logueo las celdas nulas

        processExcelFileResult.addTotalRow();

        if (answerRow.isValid()) {
            //todo: answerRow.isValid deberia verificar si pudo parsear los datos ok. verificación minima viable.
            log.info(answerRow.toString());
            processExcelFileResult.addValidRows();
            processExcelFileResult.addInsertedRows();

            //todo: inicializar formulario antes de crear categoria pasandole datos clave y verificar que sea el mismo o iniciar nuevo hilo.
            if (!surveyForm.isInitialized())
                surveyForm.initialize(answerRow);

            if (!surveyForm.isRowFromSameForm(answerRow))
                surveyForm.clearForm();//todo: agregar persistir datos o llamar a crear credenciales, etc.

                Category category = answerCategoryFactory.get(answerRow.getCategory());
                category.loadData(answerRow);

                surveyForm.addCategory(category);
                //log.info(surveyForm.toString());

        } else {
            processExcelFileResult.addRowError("todo: enviar error cacheado");
        }
        return processExcelFileResult;
    }


}
