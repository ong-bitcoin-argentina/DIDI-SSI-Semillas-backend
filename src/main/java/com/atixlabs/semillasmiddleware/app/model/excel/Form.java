package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class Form {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

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

//    @ExcelCellName("Genero_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Genero_Otro_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Tipo_Documento_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Numero_DNI_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Tipo_Documento_Otro_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Numero_Documento_Otro_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Dom1_Calle_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Dom1_Numero_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Dom1_Entre_Calles_Beneficiario")
//    private String nombre;
//    @ExcelCellName("Dom1_Municipio_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom1_Localidad_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom1_Barrio_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Calle_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Numero_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Entre_Calles_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Municipio_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Localidad_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Barrio_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Telefono_Fijo_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Telefono_Celular_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Apellido_Referente")
//            private String nombre;
//            @ExcelCellName("Nombre_Referente")
//            private String nombre;
//            @ExcelCellName("Parentesco_Referente")
//            private String nombre;
//            @ExcelCellName("Parentesco_Otro_Referente")
//            private String nombre;
//            @ExcelCellName("Telefono_Referente")
//            private String nombre;
//            @ExcelCellName("Instruccion_Otros")
//            private String nombre;
//            @ExcelCellName("Instruccion_Comentario")
//            private String nombre;
//            @ExcelCellName("Apellido_Conyuge")
//            private String nombre;
//            @ExcelCellName("Nombre_Conyuge")
//            private String nombre;
//            @ExcelCellName("Fecha_Nacimiento_Conyuge")
//            private String nombre;
//            @ExcelCellName("Edad_Conyuge")
//            private String nombre;
//            @ExcelCellName("Genero_Conyuge")
//            private String nombre;
//            @ExcelCellName("Genero_Otro_Conyuge")
//            private String nombre;
//            @ExcelCellName("Tipo_Documento_Conyuge")
//            private String nombre;
//            @ExcelCellName("Numero_DNI_Conyuge")
//            private String nombre;
//            @ExcelCellName("Tipo_Documento_Otro_Conyuge")
//            private String nombre;
//            @ExcelCellName("Numero_Documento_Otro_Conyuge")
//            private String nombre;
//            @ExcelCellName("Ocupacion_Conyuge")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Salario_Origen")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Salario_Tipo")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Salario_Monto")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Subsidio_Origen")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Subsidio_Tipo")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Subsidio_Monto")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Pension_Origen")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Pension_Tipo")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Pension_Monto")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Otros_Origen")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Otros_Tipo")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Otros_Monto")
//            private String nombre;
//            @ExcelCellName("Ingreso_Solicitante_Total")
//            private String nombre;
//            @ExcelCellName("Ingreso_Familiar_Total")
//            private String nombre;
//            @ExcelCellName("Ingreso_Total_Solicitante_Familiar")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Tenencia")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Tenencia_Otro")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Vivienda")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Vivienda_Otro")
//            private String nombre;
//
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
////            "Vivienda_Materiales"
//            @ExcelCellName("Vivienda_Materiales_Otro")
//            private String nombre;
//            @ExcelCellName("Vivienda_General_Otro")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Barrio")
//            private String nombre;
//            @ExcelCellName("Vivienda_Tipo_Barrio_Otro")
//            private String nombre;
//            @ExcelCellName("Actividad_Nombre")
//            private String nombre;
//            @ExcelCellName("Actividad_Tipo")
//            private String nombre;
//            @ExcelCellName("Actividad_Tipo_Otro")
//            private String nombre;
//
//            @ExcelCellName("Actividad_Desarrollo")
//            private String nombre;
//            @ExcelCellName("Actividad_Desarrollo_Otro")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Calle")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Numero")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Entre_Calles")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Municipio")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Localidad")
//            private String nombre;
//            @ExcelCellName("Actividad_Dir_Barrio")
//            private String nombre;
//            @ExcelCellName("Actividad_Telefono")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Alquiler")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Agua")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Luz")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Telefono")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Impuestos")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Compras")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Transporte")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Mantenimiento")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Empleados")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Otros")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Tiene_Creditos")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Credito_Total")
//            private String nombre;
//            @ExcelCellName("Egreso_Actividad_Egreso_Total")
//            private String nombre;
//            @ExcelCellName("Actividad_Comentario")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Alimentacion")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Higiene")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Gas")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Agua")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Luz")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Telefono")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Educacion")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Transporte")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Salud")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Vestimenta")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Impuestos")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Alquiler")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Combustible")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Esparcimiento")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Apuestas")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Cable")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Internet")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Seguros")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Otros")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Tiene_Creditos")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Credito_Parcial")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Credito_Total")
//            private String nombre;
//            @ExcelCellName("Egreso_Familiar_Total")
//    private String nombre;
//            @ExcelCellName("Excedente_Familiar_Mensual")
//    private String nombre;
//            @ExcelCellName("Excedente_Familiar_Quincenal")
//    private String nombre;
//            @ExcelCellName("Credito_Familiar_Impago")
//            private String nombre;
//            @ExcelCellName("Credito_Familiar_Impago_Monto")
//            private String nombre;
//            @ExcelCellName("Credito_Familiar_Impago_Fecha")
//            private String nombre;
//            @ExcelCellName("Credito_Familiar_Impago_Motivo")
//            private String nombre;
//            @ExcelCellName("Numero_DNI_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom1_Numero_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Dom2_Numero_Beneficiario")
//            private String nombre;
//            @ExcelCellName("Numero_DNI_Conyuge")
//            private String nombre;
//            @ExcelCellName("Tipo_Documento_Otro_Conyuge")
//            private String nombre;
//

}
