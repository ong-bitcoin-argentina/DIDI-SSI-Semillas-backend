package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class Child {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Apellido_Hijo")
    private String apellidoHijo;

    @ExcelCellName("Nombre_Hijo")
    private String nombreHijo;

    @ExcelCellName("Fecha_Nacimiento_Hijo")
    private String fechaNacimientoHijo;

    @ExcelCellName("Edad_Hijo")
    private String edadHijo;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns
}
