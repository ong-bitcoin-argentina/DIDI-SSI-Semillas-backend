package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import org.apache.poi.ss.usermodel.Row;

public abstract class CategoryExcelFileService extends ExcelParseService {
    public abstract void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException;
}
