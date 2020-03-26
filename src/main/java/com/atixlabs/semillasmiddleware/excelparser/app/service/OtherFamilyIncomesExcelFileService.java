package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.OtherFamilyIncomesInfo;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

public class OtherFamilyIncomesExcelFileService extends CategoryExcelFileService{
    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {
        try{
            OtherFamilyIncomesInfo otherFamilyIncomesInfo = new OtherFamilyIncomesInfo(row);
            if (otherFamilyIncomesInfo.isValid()){
                processExcelFileResult.addValidRows();
                // Credentials generation: do something with the dto
            }
            processExcelFileResult.addTotalRow();
        } catch (Exception e){
            processExcelFileResult.addRowError(e.getMessage());
        }
    }
}
