package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class EntrepreneurshipCredit {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Egreso_Actividad_Credito_Institucion")
    private String egresoActividadCreditoInstitucion;

    @ExcelCellName("Egreso_Actividad_Credito_Monto")
    private String egresoActividadCreditoMonto;

    @ExcelCellName("_parent_index")
    private int parentIndex;

}
