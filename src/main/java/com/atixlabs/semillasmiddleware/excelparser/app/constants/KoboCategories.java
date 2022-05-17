package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum KoboCategories {
    encuesta("DATOS DE LA ENCUESTA"),
    beneficiario("DATOS DEL BENEFICIARIO/A"),
    grupoFamiliar("GRUPO FAMILIAR"),
    ingresos("INGRESOS"),
    vivienda("VIVIENDA"),
    datosEmprendimiento("DATOS DEL EMPRENDIMIENTO"),
    ingresos_egresos("INGRESOS Y EGRESOS DEL EMPRENDIMIENTO O ACTIVIDAD ACTUAL"),
    finanzasFamiliares("FINANZAS FAMILIARES");

    private final String name;

    KoboCategories(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
