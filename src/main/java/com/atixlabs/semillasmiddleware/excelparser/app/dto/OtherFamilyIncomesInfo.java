package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;

public class OtherFamilyIncomesInfo extends ExcelRow{
    private static Map<String, Class<?>> questions = new HashMap<>() {{
        put("Totales", Double.class);
    }};

    public OtherFamilyIncomesInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Otros ingresos familiares pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Otros ingresos familiares respuesta"));
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
