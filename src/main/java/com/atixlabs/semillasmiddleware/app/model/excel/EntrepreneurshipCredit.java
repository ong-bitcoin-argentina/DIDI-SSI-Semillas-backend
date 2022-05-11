package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntrepreneurshipCredit {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "Egreso_Actividad_Credito_Institucion", mandatory = false)
    private String egresoActividadCreditoInstitucion;

    @ExcelCellName(value = "Egreso_Actividad_Credito_Monto", mandatory = false)
    private String egresoActividadCreditoMonto;

    @ExcelCellName(value = "_parent_index", mandatory = false)
    private int parentIndex;

}
