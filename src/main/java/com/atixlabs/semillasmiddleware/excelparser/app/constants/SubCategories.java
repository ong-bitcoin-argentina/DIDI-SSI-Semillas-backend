package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.util.Arrays;
import java.util.List;

public enum SubCategories {
    beneficiario("Domicilio", "Otro Domicilio", "Contacto Referente", "Nivel de Instrucción"),
    grupoFamiliar("Datos Cónyuge"),
    Child("Hijo"),
    FamilyMember("Otro Miembro de la Familia"),
    FamilyMemberIncome("Ingresos Miembro Familiar"),
    FamilyCredit("Crédito Familiar Actual"),
    EntrepreneurshipCredit("Crédito del Emprendimiento"),

    ingresos("Ingresos del Solicitante" ,"Total de Ingresos de la Familia", "Ingresos Totales del Solicitante y su Familia"),
    vivienda("Servicios Básicos"),
    datosEmprendimiento("Dirección"),
    ingresos_egresos("Situación Patrimonial", "Ingresos", "Egresos", "Relación Ingresos/Egresos", "Resumen de Egresos"),
    finanzasFamiliares("Ingresos Mensuales", "Egresos Familiares", "Créditos Familiares Impagos", "Excedente Familiar", "Resumen Créditos Familiares Actuales"
            , "Egresos Mensuales");

    private final List<String> subCategories;

    private SubCategories(String... fields){
        subCategories = Arrays.asList(fields);
    }

    public String getSubCategories(int index){
        return subCategories.get(index);
    }
}
