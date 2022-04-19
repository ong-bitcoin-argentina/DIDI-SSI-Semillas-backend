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

    @ExcelCellName("Ingreso_Familiar_Salario_Monto")
    private float ingresoFamiliarSalarioMonto;

    @ExcelCellName("Ingreso_Familiar_Subsidio_Origen")
    private String ingresoFamiliarSubsidioOrigen;

    @ExcelCellName("Ingreso_Familiar_Subsidio_Tipo")
    private String ingresoFamiliarSubsidioTipo;

    @ExcelCellName("Ingreso_Familiar_Subsidio_Monto")
    private float ingresoFamiliarSubsidioMonto;

    @ExcelCellName("Ingreso_Familiar_Pension_Origen")
    private String ingresoFamiliarPensioOrigen;

    @ExcelCellName("Ingreso_Familiar_Pension_Tipo")
    private String ingresoFamiliarPensionTipo;

    @ExcelCellName("Ingreso_Familiar_Pension_Monto")
    private float ingresoFamiliarPensionMonto;

    @ExcelCellName("Ingreso_Familiar_Otros_Origen")
    private String ingresoFamiliarOtrosOrigen;

    @ExcelCellName("Ingreso_Familiar_Otros_Tipo")
    private String ingresoFamiliarOtrosTipo;

    @ExcelCellName("Ingreso_Familiar_Otros_Monto")
    private float ingresoFamiliarOtrosMonto;

    @ExcelCellName("TotalIngresoFamilia")
    private float totalIngresoFamilia;

    @ExcelCellName("Ingreso_Familiar_Parcial")
    private float ingresoFamiliarParcial;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns

}
