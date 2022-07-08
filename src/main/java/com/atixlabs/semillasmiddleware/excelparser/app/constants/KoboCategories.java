package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum KoboCategories {
    ENCUESTA("DATOS DE LA ENCUESTA"),
    BENEFICIARIO("DATOS DEL BENEFICIARIO/A"),
    GRUPOFAMILIAR("GRUPO FAMILIAR"),
    INGRESOS("INGRESOS"),
    VIVIENDA("VIVIENDA"),
    DATOSEMPRENDIMIENTO("DATOS DEL EMPRENDIMIENTO"),
    INGRESOS_EGRESOS("INGRESOS Y EGRESOS DEL EMPRENDIMIENTO O ACTIVIDAD ACTUAL"),
    FINANZASFAMILIARES("FINANZAS FAMILIARES");

    private final String name;

    KoboCategories(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
