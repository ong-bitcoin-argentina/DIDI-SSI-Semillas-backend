package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DwellingInfo extends ExcelRow{
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Descripción de materiales", Double.class);
        put("Ladrillo", Boolean.class);
        put("Chapa", Boolean.class);
        put("Madera", Boolean.class);
        put("Cartón", Boolean.class);
        put("Condiciones de vivienda", String.class);
        put("Tipo de Tenencia", String.class);
        put("Vivienda", String.class);
        put("Instalación de luz", String.class);
        put("Condiciones grales", String.class);
        put("Tipo de barrio", String.class);
        put("Servicios Básicos", String.class);
        put("Red de gas", String.class);
        put("Garrafa", String.class);
        put("Red de agua", String.class);
        put("Pozo/ Bomba", String.class);
        put("Croquis Mapa", String.class);
        put("Cantidad de ambientes", String.class);
        put("Monto Alquiler", String.class);
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
