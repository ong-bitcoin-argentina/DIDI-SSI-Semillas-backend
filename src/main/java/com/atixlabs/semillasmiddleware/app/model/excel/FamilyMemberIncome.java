package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FamilyMemberIncome {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName(value = "Ingreso_Familiar_Parentesco", mandatory = false)
    private String ingresoFamiliarParentesco;

    @ExcelCellName(value = "Ingreso_Familiar_Parentesco_Otros", mandatory = false)
    private String ingresoFamiliarParentescoOtros;

    @ExcelCellName(value = "Ingreso_Familiar_Salario_Origen", mandatory = false)
    private String ingresoFamiliarSalarioOrigen;

    @ExcelCellName(value = "Ingreso_Familiar_Salario_Tipo", mandatory = false)
    private String ingresoFamiliarSalarioTipo;

    @ExcelCellName(value = "Ingreso_Familiar_Salario_Monto", mandatory = false)
    private float ingresoFamiliarSalarioMonto;

    @ExcelCellName(value = "Ingreso_Familiar_Subsidio_Origen", mandatory = false)
    private String ingresoFamiliarSubsidioOrigen;

    @ExcelCellName(value = "Ingreso_Familiar_Subsidio_Tipo", mandatory = false)
    private String ingresoFamiliarSubsidioTipo;

    @ExcelCellName(value = "Ingreso_Familiar_Subsidio_Monto", mandatory = false)
    private float ingresoFamiliarSubsidioMonto;

    @ExcelCellName(value = "Ingreso_Familiar_Pension_Origen", mandatory = false)
    private String ingresoFamiliarPensioOrigen;

    @ExcelCellName(value = "Ingreso_Familiar_Pension_Tipo", mandatory = false)
    private String ingresoFamiliarPensionTipo;

    @ExcelCellName(value = "Ingreso_Familiar_Pension_Monto", mandatory = false)
    private float ingresoFamiliarPensionMonto;

    @ExcelCellName(value = "Ingreso_Familiar_Otros_Origen", mandatory = false)
    private String ingresoFamiliarOtrosOrigen;

    @ExcelCellName(value = "Ingreso_Familiar_Otros_Tipo", mandatory = false)
    private String ingresoFamiliarOtrosTipo;

    @ExcelCellName(value = "Ingreso_Familiar_Otros_Monto", mandatory = false)
    private float ingresoFamiliarOtrosMonto;

    @ExcelCellName(value = "TotalIngresoFamilia", mandatory = false)
    private float totalIngresoFamilia;

    //TODO: Validar si este campo es necesario, al momento de generar el pdf no es util y genera datos vacios.
    /*@ExcelCellName(value = "Ingreso_Familiar_Parcial", mandatory = false)
    private float ingresoFamiliarParcial;*/

    @ExcelCellName(value = "_parent_index", mandatory = false)
    private int parentIndex;

    //TODO: add missing form columns

}
