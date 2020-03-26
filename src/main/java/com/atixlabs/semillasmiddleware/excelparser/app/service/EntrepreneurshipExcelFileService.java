package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.EntrepreneurshipInfo;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

public class EntrepreneurshipExcelFileService extends CategoryExcelFileService {
    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {
        try{
            EntrepreneurshipInfo entrepreneurshipInfo = new EntrepreneurshipInfo(row);
            if (entrepreneurshipInfo.isValid()){
                processExcelFileResult.addValidRows();
                // Credentials creation: do something with the dto
            }
            processExcelFileResult.addTotalRow();
        } catch (Exception e){
            processExcelFileResult.addRowError(e.getMessage());
        }
    }
}
