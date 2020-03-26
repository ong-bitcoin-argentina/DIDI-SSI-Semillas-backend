package com.atixlabs.semillasmiddleware.excelparser.service;

import com.atixlabs.semillasmiddleware.excelparser.app.service.CategoryExcelFileService;
import com.atixlabs.semillasmiddleware.excelparser.app.service.CategoryServiceFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.service.SurveyExcelFileService;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
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

@Service
@Slf4j
public abstract class ExcelParseService {

    String surveyName;
    String category;

    @Autowired
    FileUtil fileUtil;

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public ProcessExcelFileResult processSingleSheetFile(String filePath) throws IOException, InvalidRowException {
        String currentSurveyName;
        String currentCategory;

        log.info("Validation for file "+filePath+" begins");

        File xlsxFile = fileUtil.getFileByPath(filePath);

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        FileInputStream fileInput = new FileInputStream(xlsxFile);

        Workbook workbook = new XSSFWorkbook(fileInput);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowsIterator = sheet.rowIterator();

        if (rowsIterator.hasNext())
            rowsIterator.next();

        //process survey specific data
        if (rowsIterator.hasNext()){
            Row currentRow = rowsIterator.next();
            SurveyExcelFileService surveyExcelFileService = new SurveyExcelFileService();
            surveyExcelFileService.processRow(currentRow, processExcelFileResult);
            surveyName = getSurveyName(currentRow);
            category = getCategory(currentRow);

            currentRow = rowsIterator.next();
            currentSurveyName = getSurveyName(currentRow);
            currentCategory = getCategory(currentRow);

            while (rowsIterator.hasNext()) {
                while (rowsIterator.hasNext() && surveyName == currentSurveyName) {
                    while (rowsIterator.hasNext() && surveyName == currentSurveyName && category == currentCategory) {
                        CategoryServiceFactory categoryServiceFactory = new CategoryServiceFactory();
                        CategoryExcelFileService categoryService = categoryServiceFactory.create(category);
                        categoryService.processRow(currentRow, processExcelFileResult);

                        currentRow = rowsIterator.next();
                        currentCategory = getCategory(currentRow);
                        currentSurveyName = getSurveyName(currentRow);
                    }
                    category = currentCategory;
                }
                surveyName = currentSurveyName;
            }

            //if processExcelFileResult no tiene errores --> crear credenciales
        }

        return processExcelFileResult;
    }

    private String getSurveyName(Row row){
        return row.getCell(7).getStringCellValue();
    }

    private String getCategory(Row row){
        return row.getCell(14).getStringCellValue();
    }


    public abstract void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException;


}
