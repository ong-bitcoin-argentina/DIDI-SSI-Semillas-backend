package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FamilyCredit {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "Egreso_Familiar_Detalle", mandatory = false)
    private String egresoFamiliarDetalle;

    @ExcelCellName(value = "Egreso_Familiar_Monto", mandatory = false)
    private String egresoFamiliarMonto;

    @ExcelCellName(value = "_parent_index", mandatory = false)
    private int parentIndex;
}
