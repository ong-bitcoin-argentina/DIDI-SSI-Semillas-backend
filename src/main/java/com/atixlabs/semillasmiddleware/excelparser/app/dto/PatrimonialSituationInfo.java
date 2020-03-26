package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;

public class PatrimonialSituationInfo extends ExcelRow {
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Efectivo", Double.class);
        put("Fiado", Double.class);
        put("Stock", Double.class);
        put("Maquinaria", Double.class);
        put("Inmuebles", Double.class);
        put("Total", Double.class);
    }};

    public PatrimonialSituationInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Situación patrimonial pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Situación patrimonial respuesta"));
        }
        else {
            throw new InvalidQuestionException(question);
        }
    }

    @Override
    public boolean isValid() {
        return true;
        //TODO validate
    }
}
