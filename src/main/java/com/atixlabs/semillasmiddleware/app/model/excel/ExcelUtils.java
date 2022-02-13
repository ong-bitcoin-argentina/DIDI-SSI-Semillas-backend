package com.atixlabs.semillasmiddleware.app.model.excel;

public class ExcelUtils {

    //Relacionadas a la vivienda
    public static String beneficiaryAddress(Form form){
        return (form.getViviendaDireccionCalle()+" N° "+form.getViviendaDireccionNumero()+" e/ "+form.getViviendaDireccionEntreCalles());
    }
}
