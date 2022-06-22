package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.*;

@Getter
@Setter
public class Form {

    @ExcelRow
    private int rowIndex;

    // Datos de la encuesta y de las asesoras
    @ExcelCellName(value = "_index", mandatory = false)
    private int index;

    @ExcelCellName(value ="_validation_status", mandatory = false)
    private String estadoEncuesta;

    @ExcelCellName(value = "Grupo Solidario", mandatory = false)
    @NotBlank @NotEmpty
    private String grupoSolidario;

    @ExcelCellName(value = "Asesora", mandatory = false)
    @NotBlank @NotEmpty
    private String asesora;

    @ExcelCellName(value = "Fecha", mandatory = false)
    @NotBlank @NotEmpty
    private String fecha;

    //Datos del beneficiario
    @ExcelCellName(value = "Nombre_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String nombreBeneficiario;

    @ExcelCellName(value = "Apellido_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String apellidoBeneficiario;

    @ExcelCellName(value = "Fecha_Nacimiento_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String fechaNacimientoBeneficiario;

    @ExcelCellName(value = "Edad_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty @Min(value = 18)
    private String edadBeneficiario;

    @ExcelCellName(value = "Estado_Civil_Beneficiario",mandatory = false)
    @NotBlank @NotEmpty
    private String estadoCivilBeneficiario;

    @ExcelCellName(value = "Estado_Civil_Otro_Beneficiario", mandatory = false)
    private String estadoCivilOtroBeneficiario;

    @ExcelCellName(value = "Genero_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String generoBeneficiario;

    @ExcelCellName(value = "Genero_Otro_Beneficiario", mandatory = false)
    private String generoOtroBeneficiario;

    @ExcelCellName(value = "Tipo_Documento_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String tipoDocumentoBeneficiario;

    @ExcelCellName(value = "Numero_Doc_Beneficiario", mandatory = false)
    @Min(value = 000000001) @Max(value = 999999999)
    private Long numeroDniBeneficiario;

    @ExcelCellName(value = "Nacionalidad_Beneficiario", mandatory = false)
    private String nacionalidadBeneficiario;

    @ExcelCellName(value = "Tiempo_En_El_Pais_Beneficiario", mandatory = false)
    private String tiempoEnElPaisBeneficiario;

    @ExcelCellName(value = "Tienen_Hijos", mandatory = false)
    @NotBlank @NotEmpty
    private String tieneHijos;

    @ExcelCellName(value = "Cantidad_Hijos", mandatory = false)
    private Integer cantidadHijos;

    @ExcelCellName(value = "Hay_Mas_Miembros_Familia", mandatory = false)
    @NotBlank @NotEmpty
    private String tieneMasFamilia;

    @ExcelCellName(value = "Vivienda_Cambios", mandatory = false)
    @NotBlank @NotEmpty
    private String huboCambiosVivienda;

    @ExcelCellName(value = "Cambio_Actividad", mandatory = false)
    @NotBlank @NotEmpty
    private String huboCambiosActividad;

    @ExcelCellName(value = "Otro_Domicilio_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String otroDomicilioBeneficiario;

    //Domicilio 2
    @ExcelCellName(value = "Dom2_Calle_Beneficiario", mandatory = false)
    private String otroDomicilioDireccionCalle;

    @ExcelCellName(value = "Dom2_Num_Beneficiario", mandatory = false)
    private String otroDomicilioDireccionNumero;

    @ExcelCellName(value = "Dom2_Entre_Calles_Beneficiario", mandatory = false)
    private String otroDomicilioDireccionEntreCalles;

    @ExcelCellName(value = "Dom2_Municipio_Beneficiario", mandatory = false) //Distrito
    private String otroDomicilioDireccionMunicipio;

    @ExcelCellName(value = "Dom2_Localidad_Beneficiario", mandatory = false)
    private String otroDomicilioDomicilioLocalidad;

    @ExcelCellName(value = "Dom2_Barrio_Beneficiario", mandatory = false)
    private String otroDomicilioDomicilioBarrio;

    @ExcelCellName(value = "Telefono_Fijo_Beneficiario", mandatory = false)
    private String telefonoFijoBeneficiario;

    @ExcelCellName(value = "Telefono_Celular_Beneficiario", mandatory = false)
    private String telefonoCelularBeneficiario;

    @ExcelCellName(value = "Email_Beneficiario", mandatory = false)
    private String emailBeneficiario;

    @ExcelCellName(value = "Facebook_Beneficiario", mandatory = false)
    private String facebookBeneficiario;

   //Referente
    @ExcelCellName(value = "Apellido_Referente", mandatory = false)
    @NotBlank @NotEmpty
    private String apellidoReferente;

    @ExcelCellName(value = "Nombre_Referente", mandatory = false)
    @NotBlank @NotEmpty
    private String nombreReferente;

    @ExcelCellName(value = "Parentesco_Referente", mandatory = false)
    @NotBlank @NotEmpty
    private String parentescoReferente;

    @ExcelCellName(value = "Parentesco_Otro_Referente", mandatory = false)
    @NotBlank @NotEmpty
    private String parentescoOtroReferente;

    @ExcelCellName(value = "Telefono_Referente", mandatory = false)
    @NotBlank @NotEmpty
    private String telefonoReferente;

    //Instruccion
    @ExcelCellName(value = "Primaria_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String primariaBeneficiario;

    @ExcelCellName(value = "Secundaria_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String secundariaBeneficiario;

    @ExcelCellName(value = "Terciario_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String terciarioBeneficiario;

    @ExcelCellName(value = "Universitario_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String universitarioBeneficiario;

    @ExcelCellName(value = "Talleres_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String tieneTalleresBeneficiario;

    @ExcelCellName(value = "Taller_Realizado_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String tallerRealizadoBeneficiario;

    @ExcelCellName(value = "Cursos_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private String tieneCursosRealizadosBeneficiario;

    @ExcelCellName(value = "Curso_Realizado_Beneficiario", mandatory = false)
    @NotBlank @NotEmpty
    private  String cursoRealizadoBeneficiario;

    @ExcelCellName(value = "Instruccion_Otros", mandatory = false)
    private String instruccionOtros;

    @ExcelCellName(value = "Instruccion_Comentario", mandatory = false)
    private String instruccionComentario;

    // Datos del conyuge
    @ExcelCellName(value = "Apellido_Conyuge", mandatory = false)
    private String apellidoConyuge;

    @ExcelCellName(value = "Nombre_Conyuge", mandatory = false)
    private String nombreConyuge;

    @ExcelCellName(value = "Fecha_Nacimiento_Conyuge", mandatory = false)
    private String fechaNacimientoConyuge;

    @ExcelCellName(value = "Edad_Conyuge", mandatory = false)
    private String edadConyuge;

    @ExcelCellName(value = "Genero_Conyuge", mandatory = false)
    private String generoConyuge;

    @ExcelCellName(value = "Genero_Otro_Conyuge", mandatory = false)
    private String generoOtroConyuge;

    @ExcelCellName(value = "Tipo_Documento_Conyuge", mandatory = false)
    private String tipoDocumentoConyuge;

    @ExcelCellName(value = "Numero_Doc_Conyuge", mandatory = false)
    private Long numeroDniConyuge;

    @ExcelCellName(value = "Ocupacion_Conyuge", mandatory = false)
    private String ocupacionConyuge;

    //Datos ingreso Beneficiario
    @ExcelCellName(value = "Ingreso_Solicitante_Salario_Origen", mandatory = false)
    private String ingresoSolicitanteSalarioOrigen;

    @ExcelCellName(value = "Ingreso_Solicitante_Salario_Tipo", mandatory = false)
    private String ingresoSolicitanteSalarioTipo;

    @ExcelCellName(value = "Ingreso_Solicitante_Salario_Monto", mandatory = false)
    private Float ingresoSolicitanteSalarioMonto;

    @ExcelCellName(value = "Ingreso_Solicitante_Subsidio_Origen", mandatory = false)
    private String ingresoSolicitanteSubsidioOrigen;

    @ExcelCellName(value = "Ingreso_Solicitante_Subsidio_Tipo", mandatory = false)
    private String ingresoSolicitanteSubsidioTipo;

    @ExcelCellName(value = "Ingreso_Solicitante_Subsidio_Monto", mandatory = false)
    private Float ingresoSolicitanteSubsidioMonto;

    @ExcelCellName(value = "Ingreso_Solicitante_Pension_Origen", mandatory = false)
    private String ingresoSolicitantePensionOrigen;

    @ExcelCellName(value = "Ingreso_Solicitante_Pension_Tipo", mandatory = false)
    private String ingresoSolicitantePensionTipo;

    @ExcelCellName(value = "Ingreso_Solicitante_Pension_Monto", mandatory = false)
    private Float ingresoSolicitantePensionMonto;

    @ExcelCellName(value = "Ingreso_Solicitante_Otros_Origen", mandatory = false)
    private String ingresoSolicitanteOtrosOrigen;

    @ExcelCellName(value = "Ingreso_Solicitante_Otros_Tipo", mandatory = false)
    private String ingresoSolicitanteOtrosTipo;

    @ExcelCellName(value = "Ingreso_Solicitante_Otros_Monto", mandatory = false)
    private Float ingresoSolicitanteOtrosMonto;

    @ExcelCellName(value = "totalIngresosSolicitante", mandatory = false)
    private Float totalIngresosSolicitante;

    @ExcelCellName(value = "Ingreso_Solicitante_Total", mandatory = false)
    private Float ingresoSolicitanteTotal;

    @ExcelCellName(value = "ingresosFamiliares", mandatory = false)
    private Float ingresosFamiliares;

    @ExcelCellName(value = "Ingreso_Familiar_Total", mandatory = false)
    private Float ingresoFamiliarTotal;

    @ExcelCellName(value = "ingresosTotales", mandatory = false)
    private Float ingresosTotales;

    @ExcelCellName(value = "Ingreso_Total_Solicitante_Familiar", mandatory = false)
    private Float ingresoTotalSolicitanteFamiliar;

    //Datos de Vivienda
    @ExcelCellName(value = "Vivienda_Tipo_Tenencia", mandatory = false)
    private String viviendaTipoTenencia;

    @ExcelCellName(value = "Vivienda_Otro_Tipo_Tenencia", mandatory = false)
    private String viviendaTipoTenenciaOtro;

    @ExcelCellName(value = "Vivienda_Tipo_Vivienda", mandatory = false)
    private String viviendaTipoVivienda;

    @ExcelCellName(value = "Vivienda_Tipo_Otro_Vivienda", mandatory = false)
    private String viviendaTipoViviendaOtro;

    @ExcelCellName(value = "Vivienda_Gas_Red de gas", mandatory = false)
    private Integer viviendaRedDeGas;

    @ExcelCellName(value = "Vivienda_Gas_Garrafa", mandatory = false)
    private Integer viviendaGarrafa;

    @ExcelCellName(value = "Vivienda_Agua_Red de Agua", mandatory = false)
    private Integer viviendaRedDeAgua;

    @ExcelCellName(value = "Vivienda_Agua_Pozo _ Bomba", mandatory = false)
    private Integer viviendaBomba;

    @ExcelCellName(value = "Vivienda_Instalacion_Luz", mandatory = false)
    private String viviendaInstalacionLuz;

    @ExcelCellName(value = "Vivienda_General", mandatory = false)
    private String viviendaCondicionesGenerales;

    @ExcelCellName(value = "Vivienda_Otro_General", mandatory = false)
    private String viviendaCondicionesGeneralesOtro;

    @ExcelCellName(value = "Vivienda_Tipo_Barrio", mandatory = false)
    private String viviendaTipoBarrio;

    @ExcelCellName(value = "Vivienda_Tipo_Otro_Barrio", mandatory = false)
    private String viviendaTipoBarrioOtro;

    @ExcelCellName(value = "Dom1_Calle_Beneficiario", mandatory = false)
    private String viviendaDireccionCalle;

    @ExcelCellName(value = "Dom1_Num_Beneficiario", mandatory = false)
    private String viviendaDireccionNumero;

    @ExcelCellName(value = "Dom1_Entre_Calles_Beneficiario", mandatory = false)
    private String viviendaDireccionEntreCalles;

    @ExcelCellName(value = "Dom1_Municipio_Beneficiario", mandatory = false) //Distrito
    private String viviendaDireccionMunicipio;

    @ExcelCellName(value = "Dom1_Localidad_Beneficiario", mandatory = false)
    private String viviendaDomicilioLocalidad;

    @ExcelCellName(value = "Dom1_Barrio_Beneficiario", mandatory = false)
    private String viviendaDomicilioBarrio;

    @ExcelCellName(value = "Vivienda_Cantidad_Ambientes", mandatory = false)
    private Integer viviendaCantAmbientes;

    @ExcelCellName(value = "Vivienda_Antiguedad", mandatory = false)
    private Integer viviendaAntiguedad;

    @ExcelCellName(value = "Egreso_Familiar_Alquiler", mandatory = false)
    private Long viviendaMontoAlquiler;

    @ExcelCellName(value = "Vivienda_Materiales_Chapa", mandatory = false)
    private Integer viviendaMaterialesChapa;

    @ExcelCellName(value = "Vivienda_Materiales_Cartón", mandatory = false)
    private Integer viviendaMaterialesCarton;

    @ExcelCellName(value = "Vivienda_Materiales_Madera", mandatory = false)
    private Integer viviendaMaterialesMadera;

    @ExcelCellName(value = "Vivienda_Materiales_Adobe", mandatory = false)
    private Integer viviendaMaterialesAdobe;

    @ExcelCellName(value = "Vivienda_Materiales_Ladrillo sin reboque", mandatory = false)
    private Integer viviendaMaterialesLadrillo;

    @ExcelCellName(value = "Vivienda_Materiales_Otro", mandatory = false)
    private Integer viviendaMaterialesOtro;

    @ExcelCellName(value = "Vivienda_Otro_Materiales", mandatory = false)
    private String viviendaOtroMateriales;


    //Datos Emprendimiento
    @ExcelCellName(value = "Actividad_Nombre", mandatory = false)
    private String actividadNombre;

    @ExcelCellName(value = "Actividad_Principal", mandatory = false)
    private String actividadPrincipal;

    @ExcelCellName(value = "Actividad_Tipo", mandatory = false)
    private String actividadTipo;

    @ExcelCellName(value = "Actividad_Tipo_Comercio", mandatory = false)
    private String actividadTipoComercio;

    @ExcelCellName(value = "Actividad_Tipo_Producción", mandatory = false)
    private String actividadTipoProduccion;

    @ExcelCellName(value = "Actividad_Tipo_Servicio", mandatory = false)
    private String actividadTipoServicio;

    @ExcelCellName(value = "Actividad_Tipo_Otro", mandatory = false)
    private String actividadTipoOtro;

    @ExcelCellName(value = "Actividad_Otro_Tipo", mandatory = false)
    private String actividadOtroTipo;

    @ExcelCellName(value = "Actividad_Caracteristica", mandatory = false)
    private String actividadCaracteristica;

    @ExcelCellName(value = "Actividad_Antiguedad_Años", mandatory = false)
    private Integer actividadAntiguedad;

    @ExcelCellName(value = "Actividad_Desarrollo_Ambulante", mandatory = false)
    private Integer actividadDesarrolloAmbulante;

    @ExcelCellName(value = "Actividad_Desarrollo_Feria", mandatory = false)
    private Integer actividadDesarrolloFeria;

    //ojo esto
    @ExcelCellName(value = "Actividad_Desarrollo_Local _ Casa", mandatory = false)
    private Integer actividadDesarrolloLocalCasa;

    @ExcelCellName(value = "Actividad_Desarrollo_Comercio", mandatory = false)
    private Integer actividadDesarrolloComercio;

    @ExcelCellName(value = "Actividad_Desarrollo_Otro", mandatory = false)
    private Integer actividadDesarrolloOtro;

    @ExcelCellName(value = "Actividad_Otro_Desarrollo", mandatory = false)
    private String actividadOtroDesarrollo;

    @ExcelCellName(value = "Actividad_Dir_Calle", mandatory = false)
    private String actividadDirCalle;

    @ExcelCellName(value = "Actividad_Dir_Numero", mandatory = false)
    private String actividadDirNumero;

    @ExcelCellName(value = "Actividad_Dir_Entre_Calles", mandatory = false)
    private String actividadDirEntreCalles;

    @ExcelCellName(value = "Actividad_Dir_Municipio", mandatory = false)
    private String actividadDirMunicipio;

    @ExcelCellName(value = "Actividad_Dir_Localidad", mandatory = false)
    private String actividadDirLocalidad;

    @ExcelCellName(value = "Actividad_Dir_Barrio", mandatory = false)
    private String actividadDirBarrio;

    @ExcelCellName(value = "Actividad_Telefono", mandatory = false)
    private String actividadTelefono;

    @ExcelCellName(value = "Actividad_Cantidad_Dias", mandatory = false)
    private Integer actividadCantidadDias;

    @ExcelCellName(value = "Actividad_Cantidad_Horas", mandatory = false)
    private Integer actividadCantidadHoras;

    @ExcelCellName(value = "SP_Efectivo", mandatory = false)
    private Float spEfectivo;

    @ExcelCellName(value = "SP_Fiado", mandatory = false)
    private Float spFiado;

    @ExcelCellName(value = "SP_Stock", mandatory = false)
    private Float spStock;

    @ExcelCellName(value = "SP_Maquinaria", mandatory = false)
    private Float spMaquinaria;

    @ExcelCellName(value = "SP_Inmuebles", mandatory = false)
    private Float spInmuebles;

    @ExcelCellName(value = "Total_situacion_patrimonial", mandatory = false)
    private Float totalSituacionPatrimonial;

    @ExcelCellName(value = "Patrimonio_Total", mandatory = false)
    private Float patrimonioTotal;

    @ExcelCellName(value = "Como_Completar_Ventas", mandatory = false)
    private String comoCompletarVentas ;

    @ExcelCellName(value = "Ventas_Lunes", mandatory = false)
    private Float ventasLunes;

    @ExcelCellName(value = "Ventas_Martes", mandatory = false)
    private Float ventasMartes;

    @ExcelCellName(value = "Ventas_Miercoles", mandatory = false)
    private Float ventasMiercoles;

    @ExcelCellName(value = "Ventas_Jueves", mandatory = false)
    private Float ventasJueves;

    @ExcelCellName(value = "Ventas_Viernes", mandatory = false)
    private Float ventasViernes;

    @ExcelCellName(value = "Ventas_Sabado", mandatory = false)
    private Float ventasSabado;

    @ExcelCellName(value = "Ventas_Domingo", mandatory = false)
    private Float ventasDomingo;

    @ExcelCellName(value = "calculation_x_semana_001", mandatory = false)
    private Float calculation_x_semana_001;

    @ExcelCellName(value = "Ventas_En_Una_Semana", mandatory = false)
    private Float ventasEnUnaSemana;

    @ExcelCellName(value = "calculation", mandatory = false)
    private Float calculation;

    @ExcelCellName(value = "Calculo_Ventas_Dia_Mes", mandatory = false)
    private Float calculoVentasDiaMes;

    @ExcelCellName(value = "Ventas_Semana1", mandatory = false)
    private Float ventasSemana1;

    @ExcelCellName(value = "Ventas_Semana2", mandatory = false)
    private Float ventasSemana2;

    @ExcelCellName(value = "Ventas_Semana3", mandatory = false)
    private Float ventasSemana3;

    @ExcelCellName(value = "Ventas_Semana4", mandatory = false)
    private Float ventasSemana4;

    @ExcelCellName(value = "calculation_por_semana_001", mandatory = false)
    private Float calculation_por_semana_001;

    @ExcelCellName(value = "Calculo_Ventas_Semana_Mes", mandatory = false)
    private Float calculoVentasSemanaMes;

    @ExcelCellName(value = "Ventas_Por_Mes", mandatory = false)
    private Float ventasPorMes;

    @ExcelCellName(value = "calculation_total", mandatory = false)
    private Float calculation_total;

    @ExcelCellName(value = "Egreso_Actividad_Alquiler", mandatory = false)
    private Float egressoActividadAlquiler;

    @ExcelCellName(value = "Egreso_Actividad_Agua", mandatory = false)
    private Float egressoActividadAgua;

    @ExcelCellName(value = "Egreso_Actividad_Luz", mandatory = false)
    private Float egressoActividadLuz;

    @ExcelCellName(value = "Egreso_Actividad_Telefono", mandatory = false)
    private Float egressoActividadTelefono;

    @ExcelCellName(value = "Egreso_Actividad_Impuestos", mandatory = false)
    private Float egressoActividadImpuestos;

    @ExcelCellName(value = "Egreso_Actividad_Compras", mandatory = false)
    private Float egressoActividadCompras;

    @ExcelCellName(value = "Egreso_Actividad_Transporte", mandatory = false)
    private Float egressoActividadTransporte;

    @ExcelCellName(value = "Egreso_Actividad_Mantenimiento", mandatory = false)
    private Float egressoActividadMantenimiento;

    @ExcelCellName(value = "Egreso_Actividad_Empleados", mandatory = false)
    private Float egressoActividadEmpleados;

    @ExcelCellName(value = "Egreso_Actividad_Otros", mandatory = false)
    private Float egressoActividadOtros;

    @ExcelCellName(value = "Egreso_Actividad_Tiene_Creditos", mandatory = false)
    private String egresoActividadTieneCreditos ;

    @ExcelCellName(value = "Total_creditos_emprendimiento", mandatory = false)
    private Float totalCreditosEmprendimiento;

    @ExcelCellName(value = "Egreso_Actividad_Credito_Total", mandatory = false)
    private Float egresoActividadCreditoTotal;

    @ExcelCellName(value = "TotalEgresosEmprendimiento", mandatory = false)
    private Float totalEgresosEmprendimiento;

    @ExcelCellName(value = "Egreso_Actividad_Egreso_Total", mandatory = false)
    private Float egresoActividadEgresoTotal;

    @ExcelCellName(value = "IngresoEgresoMes", mandatory = false)
    private Float ingresoEgresoMes;

    @ExcelCellName(value = "Ingreso_Menos_Egresos", mandatory = false)
    private Float ingresoMenosEgresos;

    @ExcelCellName(value = "IngresoEgresoQuincena", mandatory = false)
    private Float ingresoEgresoQuincena;

    @ExcelCellName(value = "Netos_Por_Quincena", mandatory = false)
    private Float netosPorQuincena;

    @ExcelCellName(value = "Actividad_Comentario", mandatory = false)
    private String actividadComentario;

    @ExcelCellName(value = "Ingreso_Mensual_Emprendimiento", mandatory = false)
    private Float ingresoMensualEmprendimiento;

    @ExcelCellName(value = "Ingreso_Mensual_Solicitante", mandatory = false)
    private Float ingresoMensualSolicitante;

    @ExcelCellName(value = "Ingreso_Mensual_Familiar", mandatory = false)
    private Float ingresoMensualFamiliar;

    @ExcelCellName(value = "SumaIngresosTotales", mandatory = false)
    private Float sumaIngresosTotales;

    @ExcelCellName(value = "Ingreso_Mensual_Total", mandatory = false)
    private Float ingresoMensualTotal;

    @ExcelCellName(value = "Egreso_Familiar_Alimentacion", mandatory = false)
    private Float egresoFamiliarAlimentacion;

    @ExcelCellName(value = "Egreso_Familiar_Higiene", mandatory = false)
    private Float egresoFamiliarHigiene;

    @ExcelCellName(value = "Egreso_Familiar_Gas", mandatory = false)
    private Float egresoFamiliarGas;

    @ExcelCellName(value = "Egreso_Familiar_Agua", mandatory = false)
    private Float egresoFamiliarAgua;

    @ExcelCellName(value = "Egreso_Familiar_Luz", mandatory = false)
    private Float egresoFamiliarLuz;

    @ExcelCellName(value = "Egreso_Familiar_Telefono", mandatory = false)
    private Float egresoFamiliarTelefono;

    @ExcelCellName(value = "Egreso_Familiar_Educacion", mandatory = false)
    private Float egresoFamiliarEducacion;

    @ExcelCellName(value = "Egreso_Familiar_Transporte", mandatory = false)
    private Float egresoFamiliarTransporte;

    @ExcelCellName(value = "Egreso_Familiar_Salud", mandatory = false)
    private Float egresoFamiliarSalud;

    @ExcelCellName(value = "Egreso_Familiar_Vestimenta", mandatory = false)
    private Float egresoFamiliarVestimenta;

    @ExcelCellName(value = "Egreso_Familiar_Impuestos", mandatory = false)
    private Float egresoFamiliarImpuestos;

    @ExcelCellName(value = "Egreso_Familiar_Combustible", mandatory = false)
    private Float egresoFamiliarCombustible;

    @ExcelCellName(value = "Egreso_Familiar_Esparcimiento", mandatory = false)
    private Float egresoFamiliarEsparcimiento;

    @ExcelCellName(value = "Egreso_Familiar_Apuestas", mandatory = false)
    private Float egresoFamiliarApuestas;

    @ExcelCellName(value = "Egreso_Familiar_Cable", mandatory = false)
    private Float egresoFamiliarCable;

    @ExcelCellName(value = "Egreso_Familiar_Internet", mandatory = false)
    private Float egresoFamiliarInternet;

    @ExcelCellName(value = "Egreso_Familiar_Seguros", mandatory = false)
    private Float egresoFamiliarSeguro;

    @ExcelCellName(value = "Egreso_Familiar_Otros", mandatory = false)
    private Float egresoFamiliarOtros;

    @ExcelCellName(value = "Egreso_Familiar_Tiene_Creditos", mandatory = false)
    private String egresoFamiliarTieneCreditos;

    @ExcelCellName(value = "calculation_egresos_creditos", mandatory = false)
    private Float calculationEgresosCreditos;

    @ExcelCellName(value = "Egreso_Familiar_Credito_Parcial", mandatory = false)
    private Float egresoFamiliarCreditoParcial;

    @ExcelCellName(value = "Total_creditos_familiares", mandatory = false)
    private Float totalCreditosFamiliares;

    @ExcelCellName(value = "Egreso_Familiar_Credito_Total", mandatory = false)
    private Float egresoFamiliarCreditoTotal;

    @ExcelCellName(value = "calculationTotalEgresosMensual", mandatory = false)
    private Float calculationTotalEgresosMensual;

    @ExcelCellName(value = "Egreso_Familiar_Total", mandatory = false)
    private Float egresoFamiliarTotal;

    @ExcelCellName(value = "calculation_ingreso_egreso_mes", mandatory = false)
    private Float calculationIngresoEgresoMes;

    @ExcelCellName(value = "Excedente_Familiar_Mensual", mandatory = false)
    private Float excedenteFamiliarMensual;

    @ExcelCellName(value = "calculation_001_quincena", mandatory = false)
    private Float calculation_001_quincena;

    @ExcelCellName(value = "Excedente_Familiar_Quincenal", mandatory = false)
    private Float excedenteFamiliarQuincenal;

    @ExcelCellName(value = "Credito_Familiar_Impago", mandatory = false)
    private String creditoFamiliarImpago;

    @ExcelCellName(value = "Credito_Familiar_Impago_Monto", mandatory = false)
    private Float creditoFamiliarImpagoMonto;

    @ExcelCellName(value = "Credito_Familiar_Impago_Fecha", mandatory = false)
    private Float creditoFamiliarImpagoFecha;

    @ExcelCellName(value = "Credito_Familiar_Impago_Motivo", mandatory = false)
    private Float creditoFamiliarImpagoMotivo;

}
