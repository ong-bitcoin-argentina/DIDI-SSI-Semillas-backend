package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
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

    @ExcelCellName("Tipo_Documento_Hijo")
    private String tipoDocumentoHijo;

    @ExcelCellName("Numero_Doc_Hijo")
    private Long numeroDocumentoHijo;

    @ExcelCellName("Genero_Hijo")
    private String generoHijo;

    @ExcelCellName("Genero_Otro_Hijo")
    private String generoOtroHijo;

    @ExcelCellName("_parent_index")
    private int parentIndex;

    //TODO: add missing form columns
}
