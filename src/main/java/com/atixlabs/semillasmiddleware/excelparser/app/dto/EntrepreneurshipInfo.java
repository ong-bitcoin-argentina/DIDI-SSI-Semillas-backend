package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class EntrepreneurshipInfo extends ExcelRow {
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Tipo de emprendimiento", String.class);
        put("Actividad principal", String.class);
        put("Nombre emprendimiento", String.class);
        put("Dirección", String.class);
        put("Fecha de Inicio / Reinicio", LocalDate.class);
        put("Fecha de Fin", LocalDate.class);
    }};

    public EntrepreneurshipInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Emprendimiento pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Emprendimiento respuesta"));
            //IGNORAR TITULOS! EJEMPLO: "Ingresos y Egresos", "Datos del emprendimiento"
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
