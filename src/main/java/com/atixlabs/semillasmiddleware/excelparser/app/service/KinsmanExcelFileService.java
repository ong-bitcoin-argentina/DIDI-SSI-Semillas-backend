package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.KinsmanInfo;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

public class KinsmanExcelFileService extends CategoryExcelFileService {
    private final String kinship;

    public KinsmanExcelFileService(String kinship){
        this.kinship = kinship;
    }

    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException {
        try{
            KinsmanInfo kinsmanInfo = new KinsmanInfo(row, kinship);
            if (kinsmanInfo.isValid()){
                processExcelFileResult.addValidRows();
                // Credentials creation: do something with the dto?
            }
            processExcelFileResult.addTotalRow();
        } catch (Exception e){
            processExcelFileResult.addRowError(e.getMessage());
        }
    }
}
