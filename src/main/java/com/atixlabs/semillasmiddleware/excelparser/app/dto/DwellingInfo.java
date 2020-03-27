package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DwellingInfo extends ExcelRow{
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Tipo de Tenencia", String.class);
        put("Vivienda", String.class);
        //missing "Distrito de residencia"
    }};

    public DwellingInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Vivienda pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Vivienda respuesta"));
        }
        else {
            throw new InvalidQuestionException(question);
        }
    }

    @Override
    public boolean isValid() {
        return true;
        //TODO Validate
    }
}
