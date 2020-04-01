package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Service
@Slf4j
@NoArgsConstructor
public class SurveyExcelParseService extends ExcelParseService {

    String surveyName;
    String category;

    //@Autowired
    //FileUtil fileUtil;

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public ProcessExcelFileResult processSingleSheetFile(String filePath) throws IOException, InvalidRowException {
        String currentSurveyName;
        String currentCategory;

        log.info("Validation for file "+filePath+" begins");

        File xlsxFile = this.getFileByPath(filePath);

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


    //public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) throws InvalidRowException;

    public File getFileByPath(String path) throws FileNotFoundException {
        Optional<File> optionalFile;
        File file = new File(path);
        optionalFile = file.exists() ? Optional.of(file) : Optional.empty();
        //TODO make File manager exeception
        return optionalFile.orElseThrow(() -> new FileNotFoundException(String.format("File %s not exist.",path)));
    }


}
