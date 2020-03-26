package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidQuestionException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BeneficiaryInfo extends ExcelRow {
    private static Map<String, Class<?>> QUESTIONS = new HashMap<>() {{
        put("Tiempo de residencia en el país", String.class);
        put("Nivel Educativo", String.class);
        put("Talleres", String.class);
        put("Cursos", String.class);
        put("Datos Domicilio 1", String.class);
        put("Dirección", String.class);
        put("Localidad", String.class);
        put("Barrio", String.class);
        put("Datos domicilio 2", String.class);
        put("Datos Redes", String.class);
        put("Facebook", String.class);
        put("Correo Electrónico", String.class);
        put("Nombre", String.class);
        put("Apellido", String.class);
        put("Nacionalidad", String.class);
        put("Número de documento", Integer.class);
        put("Nivel de Institución", String.class);
        put("Primaria", String.class);
        put("Secundaria", String.class);
        put("Terciario", String.class);
        put("Universitario", String.class);
        put("Otro", String.class);
        put("Tipo de documento", String.class);
        put("Datos Teléfono", String.class);
        put("Teléfono Fijo", String.class);
        put("Teléfono Celular", String.class);
        put("Teléfono Referente", String.class);
        put("Fecha de Nacimiento", LocalDate.class);
        put("Ultimo año de estudio", Integer.class);
        put("Estado Civil", String.class);
        put("Edad", Integer.class);
    }};

    public BeneficiaryInfo(Row row) throws InvalidRowException {
        super(row);
    }

    @Override
    protected void parseRow(Row row) throws InvalidQuestionException {
        String question = getCellStringValue(row,15,"Datos del beneficiario pregunta");
        if (answers.containsKey(question)){
            answers.put(question,getCellStringValue(row,16,"Datos del beneficiario respuesta"));
        }
        else {
            //IGNORAR TITULOS!
            throw new InvalidQuestionException(question);
        }
    }

    public boolean isValid() {
        return true;
        //TODO validate
    }
}
