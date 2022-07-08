package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.List;

public enum SubCategories {
    BENEFICIARIO("Domicilio", "Otro Domicilio", "Contacto Referente", "Nivel de Instrucción"),
    GRUPOFAMILIAR("Datos Cónyuge"),
    CHILD("Hijo"),
    FAMILYMEMBER("Otro Miembro de la Familia"),
    FAMILYMEMBERINCOME("Ingresos Miembro Familiar"),
    FAMILYCREDIT("Crédito Familiar Actual"),
    ENTREPRENEURSHIPCREDIT("Crédito del Emprendimiento"),

    INGRESOS("Ingresos del Solicitante" ,"Total de Ingresos de la Familia", "Ingresos Totales del Solicitante y su Familia"),
    VIVIENDA("Servicios Básicos"),
    DATOSEMPRENDIMIENTO("Dirección"),
    INGRESOS_EGRESOS("Situación Patrimonial", "Ingresos", "Egresos", "Relación Ingresos/Egresos", "Resumen de Egresos"),
    FINANZASFAMILIARES("Ingresos Mensuales", "Egresos Familiares", "Créditos Familiares Impagos", "Excedente Familiar", "Resumen Créditos Familiares Actuales"
            , "Egresos Mensuales");

    private final List<String> subCategoriesLst;

    private SubCategories(String... fields){
        subCategoriesLst = Arrays.asList(fields);
    }

    public String getSubCategories(int index){
        return subCategoriesLst.get(index);
    }
}
