package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class KinsmanInfo extends ExcelRow {
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Nombre", String.class);
        put("Apellido", String.class);
        put("Dni", Integer.class);
        put("Estudios y trabajo", String.class);
        put("Estudia", Boolean.class);
        put("Trabaja", Boolean.class);
        put("Género", String.class);
        put("Cantidad de Hijos", Integer.class);
        put("Fecha de Nacimiento", LocalDate.class);
        put("Edad", Integer.class);
        put("Ocupación", String.class);
        put("Kinship", String.class);
    }};

    public KinsmanInfo(Row row, String kinship) throws InvalidRowException {
        super(row);
        answers.put("Kinship",kinship);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Datos del pariente pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Datos del pariente respuesta"));
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
