package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;

public class InterviewInfo extends ExcelRow {
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Datos Generales", String.class);
        put("Grupo Solidiario", String.class);
        put("Asesor/Asesora", String.class);
    }};

    public InterviewInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Datos de entrevista pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Datos de entrevista respuesta"));
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
