package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Iterator;

@Service
@Slf4j
@NoArgsConstructor
public class SurveyExcelParseService extends ExcelParseService {

    String surveyName;
    String category;

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */

    public String getSurveyName(Row row){
        return row.getCell(7).getStringCellValue();
    }

    public String getCategory(Row row){
        return row.getCell(14).getStringCellValue();
    }

    @Override
    public void processRow(Row row, ProcessExcelFileResult processExcelFileResult) {

        //visualizo cada linea procesada en formato simil tabla.
        log.info(stringifyRow(row));
    }

    public String stringifyRow(Row row){
        Iterator<Cell> cellIterator = row.cellIterator();
        String cellString = "";

        while (cellIterator.hasNext()){
            cellString += " | " + cellIterator.next().toString();
        }

        return cellString;
    }
}
