package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.BeneficiaryInfo;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

public class BeneficiaryExcelFileService extends CategoryExcelFileService{
    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {
        try{
            BeneficiaryInfo beneficiaryInfo = new BeneficiaryInfo(row);
            if (beneficiaryInfo.isValid()){
                processExcelFileResult.addValidRows();
                // do something with the dto?
            }
            processExcelFileResult.addTotalRow();
        } catch (Exception e){
            processExcelFileResult.addRowError(e.getMessage());
        }
    }
}
