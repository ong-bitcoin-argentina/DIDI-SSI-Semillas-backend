package com.atixlabs.semillasmiddleware.app.model.excel;

import java.util.Date;

public class ExcelUtils {
    //Relacionadas a la vivienda
    public static String beneficiaryAddress(Form form){
        return (form.getViviendaDireccionCalle()+" N° "+form.getViviendaDireccionNumero()+" e/ "+form.getViviendaDireccionEntreCalles());
    }

    //Relacionadas con el emprendimiento
    public static String formatAddress(Form form){
        return (form.getActividadDirCalle()+" N° "+form.getActividadDirNumero()+" e/ "+form.getActividadDirEntreCalles()+", "+
                form.getActividadDirBarrio()+", "+form.getActividadDirLocalidad()+", "+form.getActividadDirMunicipio());
    }

}