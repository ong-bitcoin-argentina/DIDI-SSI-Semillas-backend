package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class FamilyMemberIncome {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Ingreso_Familiar_Parentesco")
    private String ingresoFamiliarParentesco;

    @ExcelCellName("Ingreso_Familiar_Parentesco_Otros")
    private String ingresoFamiliarParentescoOtros;

    @ExcelCellName("Ingreso_Familiar_Salario_Origen")
    private String ingresoFamiliarSalarioOrigen;

    @ExcelCellName("Ingreso_Familiar_Salario_Tipo")
    private String ingresoFamiliarSalarioTipo;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns

}
