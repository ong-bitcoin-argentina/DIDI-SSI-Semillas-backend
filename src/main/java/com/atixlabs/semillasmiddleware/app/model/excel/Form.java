package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.hql.spi.id.cte.AbstractCteValuesListBulkIdHandler;

import java.time.LocalDate;

@Getter
@Setter
public class Form {

    @ExcelRow
    private int rowIndex;

    // Datos de la encuesta y de las asesoras
    @ExcelCellName("_index")
    private int index;

    @ExcelCellName("_validation_status")
    private String estadoEncuesta;

    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

    @ExcelCellName("Fecha")
    private String fecha;

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

    @ExcelCellName("Tipo_Documento_Beneficiario")
    private String tipoDocumentoBeneficiario;

    @ExcelCellName("Numero_Doc_Beneficiario")
    private Long numeroDniBeneficiario;

    @ExcelCellName("Nacionalidad_Beneficiario")
    private String nacionalidadBeneficiario;

    @ExcelCellName("Tiempo_En_El_Pais_Beneficiario")
    private String tiempoEnElPaisBeneficiario;

    @ExcelCellName("Tienen_Hijos")
    private String tieneHijos;

    @ExcelCellName("Cantidad_Hijos")
    private Integer cantidadHijos;

    @ExcelCellName("Hay_Mas_Miembros_Familia")
    private String tieneMasFamilia;

    @ExcelCellName("Vivienda_Cambios")
    private String huboCambiosVivienda;

    @ExcelCellName("Cambio_Actividad")
    private String huboCambiosActividad;

    @ExcelCellName("Otro_Domicilio_Beneficiario")
    private String otroDomicilioBeneficiario;

    //Domicilio 2
    @ExcelCellName("Dom2_Calle_Beneficiario")
    private String otroDomicilioDireccionCalle;

    @ExcelCellName("Dom2_Num_Beneficiario")
    private String otroDomicilioDireccionNumero;

    @ExcelCellName("Dom2_Entre_Calles_Beneficiario")
    private String otroDomicilioDireccionEntreCalles;

    @ExcelCellName("Dom2_Municipio_Beneficiario") //Distrito
    private String otroDomicilioDireccionMunicipio;

    @ExcelCellName("Dom2_Localidad_Beneficiario")
    private String otroDomicilioDomicilioLocalidad;

    @ExcelCellName("Dom2_Barrio_Beneficiario")
    private String otroDomicilioDomicilioBarrio;

    @ExcelCellName("Telefono_Fijo_Beneficiario")
    private String telefonoFijoBeneficiario;

    @ExcelCellName("Telefono_Celular_Beneficiario")
    private String telefonoCelularBeneficiario;

    @ExcelCellName("Email_Beneficiario")
    private String emailBeneficiario;

    @ExcelCellName("Facebook_Beneficiario")
    private String facebookBeneficiario;

   //Referente
    @ExcelCellName("Apellido_Referente")
    private String apellidoReferente;

    @ExcelCellName("Nombre_Referente")
    private String nombreReferente;

    @ExcelCellName("Parentesco_Referente")
    private String parentescoReferente;

    @ExcelCellName("Parentesco_Otro_Referente")
    private String parentescoOtroReferente;

    @ExcelCellName("Telefono_Referente")
    private String telefonoReferente;

    //Instruccion
    @ExcelCellName("Primaria_Beneficiario")
    private String primariaBeneficiario;

    @ExcelCellName("Secundaria_Beneficiario")
    private String secundariaBeneficiario;

    @ExcelCellName("Terciario_Beneficiario")
    private String terciarioBeneficiario;

    @ExcelCellName("Universitario_Beneficiario")
    private String universitarioBeneficiario;

    @ExcelCellName("Talleres_Beneficiario")
    private String tieneTalleresBeneficiario;

    @ExcelCellName("Taller_Realizado_Beneficiario")
    private String tallerRealizadoBeneficiario;

    @ExcelCellName("Cursos_Beneficiario")
    private String tieneCursosRealizadosBeneficiario;

    @ExcelCellName("Curso_Realizado_Beneficiario")
    private  String cursoRealizadoBeneficiario;

    @ExcelCellName("Instruccion_Otros")
    private String instruccionOtros;

    @ExcelCellName("Instruccion_Comentario")
    private String instruccionComentario;

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

    @ExcelCellName("Ocupacion_Conyuge")
    private String ocupacionConyuge;

    //Datos ingreso Beneficiario
    @ExcelCellName("Ingreso_Solicitante_Salario_Origen")
    private String ingresoSolicitanteSalarioOrigen;

    @ExcelCellName("Ingreso_Solicitante_Salario_Tipo")
    private String ingresoSolicitanteSalarioTipo;

    @ExcelCellName("Ingreso_Solicitante_Salario_Monto")
    private Float ingresoSolicitanteSalarioMonto;

    @ExcelCellName("Ingreso_Solicitante_Subsidio_Origen")
    private String ingresoSolicitanteSubsidioOrigen;

    @ExcelCellName("Ingreso_Solicitante_Subsidio_Tipo")
    private String ingresoSolicitanteSubsidioTipo;

    @ExcelCellName("Ingreso_Solicitante_Subsidio_Monto")
    private Float ingresoSolicitanteSubsidioMonto;

    @ExcelCellName("Ingreso_Solicitante_Pension_Origen")
    private String ingresoSolicitantePensionOrigen;

    @ExcelCellName("Ingreso_Solicitante_Pension_Tipo")
    private String ingresoSolicitantePensionTipo;

    @ExcelCellName("Ingreso_Solicitante_Pension_Monto")
    private Float ingresoSolicitantePensionMonto;

    @ExcelCellName("Ingreso_Solicitante_Otros_Origen")
    private String ingresoSolicitanteOtrosOrigen;

    @ExcelCellName("Ingreso_Solicitante_Otros_Tipo")
    private String ingresoSolicitanteOtrosTipo;

    @ExcelCellName("Ingreso_Solicitante_Otros_Monto")
    private Float ingresoSolicitanteOtrosMonto;

    @ExcelCellName("totalIngresosSolicitante")
    private Float totalIngresosSolicitante;

    @ExcelCellName("Ingreso_Solicitante_Total")
    private Float ingresoSolicitanteTotal;

    @ExcelCellName("IngresosFamiliares")
    private Float ingresosFamiliares;

    @ExcelCellName("Ingreso_Familiar_Total")
    private Float ingresoFamiliarTotal;

    @ExcelCellName("ingresosTotales")
    private Float ingresosTotales;

    @ExcelCellName("Ingreso_Total_Solicitante_Familiar")
    private Float ingresoTotalSolicitanteFamiliar;

    //Datos de Vivienda
    @ExcelCellName("Vivienda_Tipo_Tenencia")
    private String viviendaTipoTenencia;

    @ExcelCellName("Vivienda_Otro_Tipo_Tenencia")
    private String viviendaTipoTenenciaOtro;

    @ExcelCellName("Vivienda_Tipo_Vivienda")
    private String viviendaTipoVivienda;

    @ExcelCellName("Vivienda_Tipo_Otro_Vivienda")
    private String viviendaTipoViviendaOtro;

    @ExcelCellName("Vivienda_Gas_Red de gas")
    private Integer viviendaRedDeGas;

    @ExcelCellName("Vivienda_Gas_Garrafa")
    private Integer viviendaGarrafa;

    @ExcelCellName("Vivienda_Agua_Red de Agua")
    private Integer viviendaRedDeAgua;

    @ExcelCellName("Vivienda_Agua_ Bomba")
    private Integer viviendaBomba;

    @ExcelCellName("Vivienda_Instalacion_Luz")
    private String viviendaInstalacionLuz;

    @ExcelCellName("Vivienda_General")
    private String viviendaCondicionesGenerales;

    @ExcelCellName("Vivienda_Otro_General")
    private String viviendaCondicionesGeneralesOtro;

    @ExcelCellName("Vivienda_Tipo_Barrio")
    private String viviendaTipoBarrio;

    @ExcelCellName("Vivienda_Tipo_Otro_Barrio")
    private String viviendaTipoBarrioOtro;

    @ExcelCellName("Dom1_Calle_Beneficiario")
    private String viviendaDireccionCalle;

    @ExcelCellName("Dom1_Num_Beneficiario")
    private String viviendaDireccionNumero;

    @ExcelCellName("Dom1_Entre_Calles_Beneficiario")
    private String viviendaDireccionEntreCalles;

    @ExcelCellName("Dom1_Municipio_Beneficiario") //Distrito
    private String viviendaDireccionMunicipio;

    @ExcelCellName("Dom1_Localidad_Beneficiario")
    private String viviendaDomicilioLocalidad;

    @ExcelCellName("Dom1_Barrio_Beneficiario")
    private String viviendaDomicilioBarrio;

    @ExcelCellName("Vivienda_Cantidad_Ambientes")
    private Integer viviendaCantAmbientes;

    @ExcelCellName("Vivienda_Antiguedad")
    private Integer viviendaAntiguedad;

    @ExcelCellName("Egreso_Familiar_Alquiler")
    private Long viviendaMontoAlquiler;

    @ExcelCellName("Vivienda_Materiales_Chapa")
    private Integer viviendaMaterialesChapa;

    @ExcelCellName("Vivienda_Materiales_Cartón")
    private Integer viviendaMaterialesCarton;

    @ExcelCellName("Vivienda_Materiales_Madera")
    private Integer viviendaMaterialesMadera;

    @ExcelCellName("Vivienda_Materiales_Adobe")
    private Integer viviendaMaterialesAdobe;

    @ExcelCellName("Vivienda_Materiales_Ladrillo sin reboque")
    private Integer viviendaMaterialesLadrillo;

    @ExcelCellName("Vivienda_Materiales_Otro")
    private Integer viviendaMaterialesOtro;

    @ExcelCellName("Vivienda_Otro_Materiales")
    private String viviendaOtroMateriales;


    //Datos Emprendimiento
    @ExcelCellName("Actividad_Nombre")
    private String actividadNombre;

    @ExcelCellName("Actividad_Principal")
    private String actividadPrincipal;

    @ExcelCellName("Actividad_Tipo")
    private String actividadTipo;

    @ExcelCellName("Actividad_Tipo_Comercio")
    private String actividadTipoComercio;

    @ExcelCellName("Actividad_Tipo_Producción")
    private String actividadTipoProduccion;

    @ExcelCellName("Actividad_Tipo_Servicio")
    private String actividadTipoServicio;

    @ExcelCellName("Actividad_Tipo_Otro")
    private String actividadTipoOtro;

    @ExcelCellName("Actividad_Otro_Tipo")
    private String actividadOtroTipo;

    @ExcelCellName("Actividad_Caracteristica")
    private String actividadCaracteristica;

    @ExcelCellName("Actividad_Antiguedad_Años")
    private Integer actividadAntiguedad;

    @ExcelCellName("Actividad_Desarrollo-Ambulante")
    private Integer actividadDesarrolloAmbulante;

    @ExcelCellName("Actividad_Desarrollo-Feria")
    private Integer actividadDesarrolloFeria;

    //ojo esto
    @ExcelCellName("Actividad_Desarrollo-Local / Casa")
    private Integer actividadDesarrolloLocalCasa;

    @ExcelCellName("Actividad_Desarrollo-Comercio")
    private Integer actividadDesarrolloComercio;

    @ExcelCellName("Actividad_Desarrollo-Otro")
    private Integer actividadDesarrolloOtro;

    @ExcelCellName("Actividad_Otro_Desarrollo")
    private String actividadOtroDesarrollo;

    @ExcelCellName("Actividad_Dir_Calle")
    private String actividadDirCalle;

    @ExcelCellName("Actividad_Dir_Numero")
    private String actividadDirNumero;

    @ExcelCellName("Actividad_Dir_Entre_Calles")
    private String actividadDirEntreCalles;

    @ExcelCellName("Actividad_Dir_Municipio")
    private String actividadDirMunicipio;

    @ExcelCellName("Actividad_Dir_Localidad")
    private String actividadDirLocalidad;

    @ExcelCellName("Actividad_Dir_Barrio")
    private String actividadDirBarrio;

    @ExcelCellName("Actividad_Telefono")
    private String actividadTelefono;

    @ExcelCellName("Actividad_Cantidad_Dias")
    private Integer actividadCantidadDias;

    @ExcelCellName("Actividad_Cantidad_Horas")
    private Integer actividadCantidadHoras;

    @ExcelCellName("SP_Efectivo")
    private Float spEfectivo;

    @ExcelCellName("SP_Fiado")
    private Float spFiado;

    @ExcelCellName("SP_Stock")
    private Float spStock;

    @ExcelCellName("SP_Maquinaria")
    private Float spMaquinaria;

    @ExcelCellName("SP_Inmuebles")
    private Float spInmuebles;

    @ExcelCellName("Total_situacion_patrimonial")
    private Float totalSituacionPatrimonial;

    @ExcelCellName("Patrimonio_Total")
    private Float patrimonioTotal;

    @ExcelCellName("Como_Completar_Ventas")
    private String comoCompletarVentas ;

    @ExcelCellName("Ventas_Lunes")
    private Float ventasLunes;

    @ExcelCellName("Ventas_Martes")
    private Float ventasMartes;

    @ExcelCellName("Ventas_Miercoles")
    private Float ventasMiercoles;

    @ExcelCellName("Ventas_Jueves")
    private Float ventasJueves;

    @ExcelCellName("Ventas_Viernes")
    private Float ventasViernes;

    @ExcelCellName("Ventas_Sabado")
    private Float ventasSabado;

    @ExcelCellName("Ventas_Domingo")
    private Float ventasDomingo;

    @ExcelCellName("calculation_x_semana_001")
    private Float calculation_x_semana_001;

    @ExcelCellName("Ventas_En_Una_Semana")
    private Float ventasEnUnaSemana;

    @ExcelCellName("calculation")
    private Float calculation;

    @ExcelCellName("Calculo_Ventas_Dia_Mes")
    private Float calculoVentasDiaMes;

    @ExcelCellName("Ventas_Semana1")
    private Float ventasSemana1;

    @ExcelCellName("Ventas_Semana2")
    private Float ventasSemana2;

    @ExcelCellName("Ventas_Semana3")
    private Float ventasSemana3;

    @ExcelCellName("Ventas_Semana4")
    private Float ventasSemana4;

    @ExcelCellName("calculation_por_semana_001")
    private Float calculation_por_semana_001;

    @ExcelCellName("Calculo_Ventas_Semana_Mes")
    private Float calculoVentasSemanaMes;

    @ExcelCellName("Ventas_Por_Mes")
    private Float ventasPorMes;

    @ExcelCellName("calculation_total")
    private Float calculation_total;

    @ExcelCellName("Egreso_Actividad_Alquiler")
    private Float egressoActividadAlquiler;

    @ExcelCellName("Egreso_Actividad_Agua")
    private Float egressoActividadAgua;

    @ExcelCellName("Egreso_Actividad_Luz")
    private Float egressoActividadLuz;

    @ExcelCellName("Egreso_Actividad_Telefono")
    private Float egressoActividadTelefono;

    @ExcelCellName("Egreso_Actividad_Impuestos")
    private Float egressoActividadImpuestos;

    @ExcelCellName("Egreso_Actividad_Compras")
    private Float egressoActividadCompras;

    @ExcelCellName("Egreso_Actividad_Transporte")
    private Float egressoActividadTransporte;

    @ExcelCellName("Egreso_Actividad_Mantenimiento")
    private Float egressoActividadMantenimiento;

    @ExcelCellName("Egreso_Actividad_Empleados")
    private Float egressoActividadEmpleados;

    @ExcelCellName("Egreso_Actividad_Otros")
    private Float egressoActividadOtros;

    @ExcelCellName("Egreso_Actividad_Tiene_Creditos")
    private String egresoActividadTieneCreditos ;

    @ExcelCellName("Total_creditos_emprendimiento")
    private Float totalCreditosEmprendimiento;

    @ExcelCellName("Egreso_Actividad_Credito_Total")
    private Float egresoActividadCreditoTotal;

    @ExcelCellName("TotalEgresosEmprendimiento")
    private Float totalEgresosEmprendimiento;

    @ExcelCellName("Egreso_Actividad_Egreso_Total")
    private Float egresoActividadEgresoTotal;

    @ExcelCellName("IngresoEgresoMes")
    private Float ingresoEgresoMes;

    @ExcelCellName("Ingreso_Menos_Egresos")
    private Float ingresoMenosEgresos;

    @ExcelCellName("IngresoEgresoQuincena")
    private Float ingresoEgresoQuincena;

    @ExcelCellName("Netos_Por_Quincena")
    private Float netosPorQuincena;

    @ExcelCellName("Actividad_Comentario")
    private String actividadComentario;

    @ExcelCellName("Ingreso_Mensual_Emprendimiento")
    private Float ingresoMensualEmprendimiento;

    @ExcelCellName("Ingreso_Mensual_Solicitante")
    private Float ingresoMensualSolicitante;

    @ExcelCellName("Ingreso_Mensual_Familiar")
    private Float ingresoMensualFamiliar;

    @ExcelCellName("SumaIngresosTotales")
    private Float sumaIngresosTotales;

    @ExcelCellName("Ingreso_Mensual_Total")
    private Float ingresoMensualTotal;

    @ExcelCellName("Egreso_Familiar_Alimentacion")
    private Float egresoFamiliarAlimentacion;

    @ExcelCellName("Egreso_Familiar_Higiene")
    private Float egresoFamiliarHigiene;

    @ExcelCellName("Egreso_Familiar_Gas")
    private Float egresoFamiliarGas;

    @ExcelCellName("Egreso_Familiar_Agua")
    private Float egresoFamiliarAgua;

    @ExcelCellName("Egreso_Familiar_Luz")
    private Float egresoFamiliarLuz;

    @ExcelCellName("Egreso_Familiar_Telefono")
    private Float egresoFamiliarTelefono;

    @ExcelCellName("Egreso_Familiar_Educacion")
    private Float egresoFamiliarEducacion;

    @ExcelCellName("Egreso_Familiar_Transporte")
    private Float egresoFamiliarTransporte;

    @ExcelCellName("Egreso_Familiar_Salud")
    private Float egresoFamiliarSalud;

    @ExcelCellName("Egreso_Familiar_Vestimenta")
    private Float egresoFamiliarVestimenta;

    @ExcelCellName("Egreso_Familiar_Impuestos")
    private Float egresoFamiliarImpuestos;

    @ExcelCellName("Egreso_Familiar_Combustible")
    private Float egresoFamiliarCombustible;

    @ExcelCellName("Egreso_Familiar_Esparcimiento")
    private Float egresoFamiliarEsparcimiento;

    @ExcelCellName("Egreso_Familiar_Apuestas")
    private Float egresoFamiliarApuestas;

    @ExcelCellName("Egreso_Familiar_Cable")
    private Float egresoFamiliarCable;

    @ExcelCellName("Egreso_Familiar_Internet")
    private Float egresoFamiliarInternet;

    @ExcelCellName("Egreso_Familiar_Seguros")
    private Float egresoFamiliarSeguro;

    @ExcelCellName("Egreso_Familiar_Otros")
    private Float egresoFamiliarOtros;

    @ExcelCellName("Egreso_Familiar_Tiene_Creditos")
    private String egresoFamiliarTieneCreditos;

    @ExcelCellName("calculation_egresos_creditos")
    private Float calculationEgresosCreditos;

    @ExcelCellName("Egreso_Familiar_Credito_Parcial")
    private Float egresoFamiliarCreditoParcial;

    @ExcelCellName("Total_creditos_familiares")
    private Float totalCreditosFamiliares;

    @ExcelCellName("Egreso_Familiar_Credito_Total")
    private Float egresoFamiliarCreditoTotal;

    @ExcelCellName("calculationTotalEgresosMensual")
    private Float calculationTotalEgresosMensual;

    @ExcelCellName("Egreso_Familiar_Total")
    private Float egresoFamiliarTotal;

    @ExcelCellName("calculation_ingreso_egreso_mes")
    private Float calculationIngresoEgresoMes;

    @ExcelCellName("Excedente_Familiar_Mensual")
    private Float excedenteFamiliarMensual;

    @ExcelCellName("calculation_001_quincena")
    private Float calculation_001_quincena;

    @ExcelCellName("Excedente_Familiar_Quincenal")
    private Float excedenteFamiliarQuincenal;

    @ExcelCellName("Credito_Familiar_Impago")
    private String creditoFamiliarImpago;

    @ExcelCellName("Credito_Familiar_Impago_Monto")
    private Float creditoFamiliarImpagoMonto;

    @ExcelCellName("Credito_Familiar_Impago_Fecha")
    private Float creditoFamiliarImpagoFecha;

    @ExcelCellName("Credito_Familiar_Impago_Motivo")
    private Float creditoFamiliarImpagoMotivo;

}
