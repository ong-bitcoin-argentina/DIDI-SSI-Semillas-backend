package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
@Slf4j
@NoArgsConstructor
public class SurveyExcelParseService extends ExcelParseService {

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param row
     * @return
     * @throws FileNotFoundException
     */

    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {

        //visualizo cada linea procesada en formato simil tabla.

        AnswerRow answerRow = new AnswerRow(row);
        log.info(answerRow.toString());
        log.info(answerRow.toString(row));

        if(answerRow.isExists()){

            processExcelFileResult.addTotalRow();

            //answerRow.validateType();//2 metodos privados invocados x isValid
            //answerRow.validateData();

            if(answerRow.isValid()){
                processExcelFileResult.addValidRows();

                //TODO: LLAMAR AL FORM Y AGREGAR LA FILA
                //TODO: DEBERIA HABER metodo que agregue una fila 1 a 1.
                //surveyForm.addValidRow(answerRow);
            }
            else{
                processExcelFileResult.addRowError("todo: enviar error cacheado");
            }
        }

        //return processExcelFileResult;


    }


}
