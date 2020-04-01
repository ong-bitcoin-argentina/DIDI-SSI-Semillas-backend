package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyInfo;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import org.apache.poi.ss.usermodel.Row;

public class SurveyExcelFileService extends ExcelParseService {


    //@Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {
        SurveyInfo surveyInfo = new SurveyInfo(row);

        try{
            if (surveyInfo.isValid()){
                processExcelFileResult.addValidRows();
                // do something with the dto?
            }
            processExcelFileResult.addTotalRow();
        } catch (Exception e){
            processExcelFileResult.addRowError(e.getMessage());
        }
    }
}
