package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class Child {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

    @ExcelCellName("Nombre")
    private String nombre;


}
