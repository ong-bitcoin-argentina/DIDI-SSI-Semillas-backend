package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import java.time.LocalDate;

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

    @ExcelCellName("Fecha_Nacimiento_Familiar")
    private LocalDate fechaNacimientoFamiliar;

    @ExcelCellName("Edad_Familiar")
    private String edadFamiliar;

    @ExcelCellName("Genero_Familiar")
    private String generoFamiliar;

    @ExcelCellName("Genero_Otro_Familiar")
    private String generoOtroFamiliar;

    @ExcelCellName("Tipo_Documento_Familiar")
    private String tipoDocumentoFamiliar;

    @ExcelCellName("Numero_Dni_Familiar")
    private Long numeroDniFamiliar;

    @ExcelCellName("Tipo_Documento_Otro_Familiar")
    private String tipoDocumentoOtroFamiliar;

    @ExcelCellName("Numero_Documento_Otro_Familiar")
    private String numeroDocumentoOtroFamiliar;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns
}
