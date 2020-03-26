package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class EntrepreneurshipInfo extends ExcelRow {
    private static Map<String, Class<?>> questions = new HashMap<>() {{
        put("INGRESOS - Por día", Double.class);
        put("INGRESOS - Por semana", Double.class);
        put("INGRESOS - Por mes", Double.class);
        put("INGRESOS - Total", Double.class);
        put("EGRESOS - Alquiler", Double.class);
        put("EGRESOS - Agua", Double.class);
        put("EGRESOS - Luz", Double.class);
        put("EGRESOS - Compras", Double.class);
        put("EGRESOS - Teléfono", Double.class);
        put("EGRESOS - Impuestos", Double.class);
        put("EGRESOS - Transporte", Double.class);
        put("EGRESOS - Mantenimiento", Double.class);
        put("EGRESOS - Empleados", Double.class);
        put("EGRESOS - Otros", Double.class);
        put("EGRESOS - Credito vigente (Con quién prox. pregunta)", Double.class);
        put("EGRESOS - Total", Double.class);
        put("RELACIÓN - Ingresos / Egresos Mes", Double.class);
        put("RELACIÓN - Ingresos / Egresos Quincena", Double.class);
        put("Nombre emprendimiento", String.class);
        put("Dirección", String.class);
        put("Teléfono", String.class);
        put("Actividad principal", String.class);
        put("Antiguedad", Double.class);
        put("Facebook", String.class);
        put("Institución del crédito vigente", String.class);
        put("Tipo de emprendimiento", String.class);
        put("Comercio", String.class);
        put("Producción", String.class);
        put("Servicio", String.class);
        put("Desarrollo de la Actividad", String.class);
        put("Ambulante", String.class);
        put("Feria", String.class);
        put("Local o Casa", String.class);
        put("Fecha de Inicio / Reinicio", LocalDate.class);
        put("Tiempo de trabajo", String.class);
        put("Días por semana", String.class);
        put("Horas por semana", String.class);
    }};

    public EntrepreneurshipInfo(Row row) throws InvalidRowException {
        super(row);
        try {
            parseRow(row);
        } catch (Exception e){
            log.error(getStringError(), e);
            throw new InvalidRowException(getStringError() + e.getMessage());
        }
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Emprendimiento pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Emprendimiento respuesta"));
        }
        else {
            //IGNORAR TITULOS! EJEMPLO: "Ingresos y Egresos", "Datos del emprendimiento"
            throw new InvalidQuestionException(question);
        }
    }

    @Override
    public boolean isValid() {
        return true;
        //TODO validate
    }
}
