package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class FamilyMember {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Parentesco_Familiar")
    private String parentescoFamiliar;

    @ExcelCellName("Parentesco_Otro_Familiar")
    private String parentescoOtroFamiliar;

    @ExcelCellName("Apellido_Familiar")
    private String apellidoFamiliar;

    @ExcelCellName("Nombre_Familiar")
    private String nombreFamiliar;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns
}
