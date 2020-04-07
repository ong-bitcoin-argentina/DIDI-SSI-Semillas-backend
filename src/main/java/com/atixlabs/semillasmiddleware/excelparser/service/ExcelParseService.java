package com.atixlabs.semillasmiddleware.excelparser.service;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
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
    public ProcessExcelFileResult processSingleSheetFile(String filePath) throws Exception, InvalidCategoryException {
        log.info("Validation for file "+filePath+" begins");

        File xlsxFile = this.getFileByPath(filePath);

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        FileInputStream fileInput = new FileInputStream(xlsxFile);

        Workbook workbook = new XSSFWorkbook(fileInput);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowsIterator = sheet.rowIterator();

        //está eliminando la linea de cabeceras que por ahora quiero ver:
        if (rowsIterator.hasNext())
            rowsIterator.next();

        while (rowsIterator.hasNext()) {
            processExcelFileResult = processRow(rowsIterator.next(), rowsIterator.hasNext(), processExcelFileResult);
        }

        return processExcelFileResult;
    }


    public abstract ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult) throws Exception, InvalidCategoryException;

    private File getFileByPath(String path) throws FileNotFoundException {
        Optional<File> optionalFile;
        File file = new File(path);
        optionalFile = file.exists() ? Optional.of(file) : Optional.empty();
        //TODO make File manager exeception
        return optionalFile.orElseThrow(() -> new FileNotFoundException(String.format("File %s not exist.",path)));
    }


}
