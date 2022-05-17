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

    @ExcelCellName(value = "Apellido_Hijo", mandatory = false)
    private String apellidoHijo;

    @ExcelCellName(value = "Nombre_Hijo",mandatory = false)
    private String nombreHijo;

    @ExcelCellName(value = "Fecha_Nacimiento_Hijo",mandatory = false)
    private String fechaNacimientoHijo;

    @ExcelCellName(value = "Edad_Hijo", mandatory = false)
    private String edadHijo;

    @ExcelCellName(value = "Tipo_Documento_Hijo", mandatory = false)
    private String tipoDocumentoHijo;

    @ExcelCellName(value = "Numero_Doc_Hijo", mandatory = false)
    private Long numeroDocumentoHijo;

    @ExcelCellName(value = "Genero_Hijo", mandatory = false)
    private String generoHijo;

    @ExcelCellName(value = "Genero_Otro_Hijo", mandatory = false)
    private String generoOtroHijo;

    @ExcelCellName(value = "Trabaja_Hijo", mandatory = false)
    private String trabajaHijo;

    @ExcelCellName(value = "En_Que_Trabaja_Hijo", mandatory = false)
    private String enQueTrabajaHijo;

    @ExcelCellName(value = "Estudia_Hijo", mandatory = false)
    private String estudiaHijo;

    @ExcelCellName(value = "_parent_index", mandatory = false)
    private int parentIndex;
}
