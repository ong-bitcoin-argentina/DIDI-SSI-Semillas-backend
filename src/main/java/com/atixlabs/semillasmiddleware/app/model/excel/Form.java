package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Form {

    @ExcelRow
    private int rowIndex;

    // Datos de la encuesta y de las asesoras
    @ExcelCellName("_index")
    private int index;

    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

    //Datos del beneficiario
    @ExcelCellName("Nombre_Beneficiario")
    private String nombreBeneficiario;

    @ExcelCellName("Apellido_Beneficiario")
    private String apellidoBeneficiario;

    @ExcelCellName("Fecha_Nacimiento_Beneficiario")
    private String fechaNacimientoBeneficiario;

    @ExcelCellName("Edad_Beneficiario")
    private String edadBeneficiario;

    @ExcelCellName("Estado_Civil_Beneficiario")
    private String estadoCivilBeneficiario;

    @ExcelCellName("Estado_Civil_Otro_Beneficiario")
    private String estadoCivilOtroBeneficiario;

    @ExcelCellName("Genero_Beneficiario")
    private String generoBeneficiario;

    @ExcelCellName("Genero_Otro_Beneficiario")
    private String generoOtroBeneficiario;

    @ExcelCellName("Numero_Doc_Beneficiario")
    private Long numeroDniBeneficiario;

    @ExcelCellName("Tienen_Hijos")
    private String tieneHijos;

    @ExcelCellName("Hay_Mas_Miembros_Familia")
    private String tieneMasFamilia;

    // Datos del conyuge
    @ExcelCellName("Apellido_Conyuge")
    private String apellidoConyuge;

    @ExcelCellName("Nombre_Conyuge")
    private String nombreConyuge;

    @ExcelCellName("Fecha_Nacimiento_Conyuge")
    private String fechaNacimientoConyuge;

    @ExcelCellName("Edad_Conyuge")
    private String edadConyuge;

    @ExcelCellName("Genero_Conyuge")
    private String generoConyuge;

    @ExcelCellName("Genero_Otro_Conyuge")
    private String generoOtroConyuge;

    @ExcelCellName("Tipo_Documento_Conyuge")
    private String tipoDocumentoConyuge;

    @ExcelCellName("Numero_Doc_Conyuge")
    private Long numeroDniConyuge;

}
