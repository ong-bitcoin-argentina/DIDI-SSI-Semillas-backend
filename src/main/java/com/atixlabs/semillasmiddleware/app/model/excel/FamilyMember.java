package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class FamilyMember {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "Parentesco_Familiar",mandatory = false)
    private String parentescoFamiliar;

    @ExcelCellName(value = "Parentesco_Otro_Familiar", mandatory = false)
    private String parentescoOtroFamiliar;

    @ExcelCellName(value = "Apellido_Familiar", mandatory = false)
    private String apellidoFamiliar;

    @ExcelCellName(value = "Nombre_Familiar", mandatory = false)
    private String nombreFamiliar;

    @ExcelCellName(value = "Fecha_Nacimiento_Familiar", mandatory = false)
    private String fechaNacimientoFamiliar;

    @ExcelCellName(value = "Edad_Familiar", mandatory = false)
    private String edadFamiliar;

    @ExcelCellName(value = "Genero_Familia", mandatory = false)
    private String generoFamilia;

    @ExcelCellName(value = "Genero_Otro_Familiar", mandatory = false)
    private String generoOtroFamiliar;

    @ExcelCellName(value = "Tipo_Documento_Familiar", mandatory = false)
    private String tipoDocumentoFamiliar;

    @ExcelCellName(value = "Numero_Doc_Familiar", mandatory = false)
    private Long numeroDniFamiliar;

    @ExcelCellName(value = "_parent_index", mandatory = false)
    private int parentIndex;

}
