package com.atixlabs.semillasmiddleware.excelparser.service;

import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Iterator;

@Service
@Slf4j
public abstract class ExcelParseService {

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     */

    @Autowired
    FileUtil fileUtil;

    public ProcessExcelFileResult processSingleSheetFile(String filePath, boolean createCredentials,
                                                         boolean skipIdentityCredentials,
                                                         boolean pdfValidation) throws Exception {
        log.info("Validation for file " + filePath + " begins");

        File xlsxFile = fileUtil.getFileByPath(filePath);

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        try (FileInputStream fileInput = new FileInputStream(xlsxFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowsIterator = sheet.rowIterator();

            //está eliminando la linea de cabeceras que por ahora quiero ver:
            if (rowsIterator.hasNext())
                rowsIterator.next();

            while (rowsIterator.hasNext()) {
                processRow(rowsIterator.next(), rowsIterator.hasNext(), processExcelFileResult, createCredentials,
                        skipIdentityCredentials, pdfValidation);
            }
            workbook.close();
        } catch (NotOfficeXmlFileException c) {
            log.error("Invalid file format: " + filePath);
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader("Error en el archivo")
                    .errorBody("Por favor, verificá que el formato del archivo sea correcto.")
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
        }

        return processExcelFileResult;
    }

    public abstract ProcessExcelFileResult processRow(Row currentRow, boolean hasNext,
                                                      ProcessExcelFileResult processExcelFileResult,
                                                      boolean createCredentials,
                                                      boolean skipIdentityCredentials,
                                                      boolean pdfValidation) throws Exception;

}
