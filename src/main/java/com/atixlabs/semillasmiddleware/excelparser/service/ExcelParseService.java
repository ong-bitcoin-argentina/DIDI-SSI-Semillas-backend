package com.atixlabs.semillasmiddleware.excelparser.service;

import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.app.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

@Service
@Slf4j
public abstract class ExcelParseService {

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */

    @Autowired
    FileUtil fileUtil;


    public ProcessExcelFileResult processSingleSheetFile(String filePath, boolean createCredentials) throws Exception {
        log.info("Validation for file "+filePath+" begins");

        File xlsxFile = fileUtil.getFileByPath(filePath);

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        FileInputStream fileInput = new FileInputStream(xlsxFile);

        try {
            Workbook workbook = new XSSFWorkbook(fileInput);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowsIterator = sheet.rowIterator();

            //está eliminando la linea de cabeceras que por ahora quiero ver:
            if (rowsIterator.hasNext())
                rowsIterator.next();

            while (rowsIterator.hasNext()) {
                processRow(rowsIterator.next(), rowsIterator.hasNext(), processExcelFileResult, createCredentials);
            }
            return processExcelFileResult;
        } catch (NotOfficeXmlFileException c) {
            log.error("Invalid file format: " + filePath);
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader("Error en el archivo")
                    .errorBody("Por favor, verificá que el formato del archivo sea correcto.")
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
            return processExcelFileResult;
        } finally {
            fileInput.close();
        }
    }


    public abstract ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult, boolean createCredentials) throws Exception;

}
