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

    @ExcelCellName("Vivienda_Cambios")
    private String huboCambiosVivienda;

    @ExcelCellName("Cambio_Actividad")
    private String huboCambiosActividad;

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

    @ExcelCellName("Actividad_Caracteristica")
    private String actividadCaracteristica;

    @ExcelCellName("Actividad_Antiguedad_Años")
    private Integer actividadAntiguedad;

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




}
