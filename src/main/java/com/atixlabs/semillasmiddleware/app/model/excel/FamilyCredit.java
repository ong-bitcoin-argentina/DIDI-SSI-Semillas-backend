package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class FamilyCredit {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Egreso_Familiar_Detalle")
    private String egresoFamiliarDetalle;

    @ExcelCellName("Egreso_Familiar_Monto")
    private String egresoFamiliarMonto;

    @ExcelCellName("_parent_index")
    private int parentIndex;
}
