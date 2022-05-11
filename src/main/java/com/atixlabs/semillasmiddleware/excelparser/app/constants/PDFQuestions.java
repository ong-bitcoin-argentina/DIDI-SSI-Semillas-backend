package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum PDFQuestions {
    //Datos Encuesta
    grupoSolidario("Grupo Solidario"),
    asesora("Asesor/a"),

    //Datos Beneficiario
    nombreBeneficiario("Nombre"),
    apellidoBeneficiario("Apellido"),
    fechaNacimientoBeneficiario("Fecha de nacimiento"),
    edadBeneficiario("Edad"),
    estadoCivilBeneficiario("Estado Civil"),
    estadoCivilOtroBeneficiario("Otro Estado Civil"),
    generoBeneficiario("Género"),
    generoOtroBeneficiario("Otro Género"),
    tipoDocumentoBeneficiario("Tipo de Documento"),
    numeroDniBeneficiario("Número de Documento"),
    nacionalidadBeneficiario("Nacionalidad"),
    tiempoEnElPaisBeneficiario("Tiempo en el País"),

    //Datos Domicilio
    viviendaDireccionCalle("Calle"),
    viviendaDireccionNumero("Número"),
    viviendaDireccionEntreCalles("Entre Calles"),
    viviendaDireccionMunicipio("Municipio"),
    viviendaDomicilioLocalidad("Localidad"),
    viviendaDomicilioBarrio("Barrio"),

    //Datos Otro Domicilio
    otroDomicilioBeneficiario("¿Tiene otro Domicilio?"),
    otroDomicilioDireccionCalle("Calle"),
    otroDomicilioDireccionNumero("Número"),
    otroDomicilioDireccionEntreCalles("Entre Calles"),
    otroDomicilioDireccionMunicipio("Municipio"),
    otroDomicilioDomicilioLocalidad("Localidad"),
    otroDomicilioDomicilioBarrio("Barrio"),

    //Datos Referente
    apellidoReferente("Apellido"),
    nombreReferente("Nombre"),
    parentescoReferente("Parentesco"),
    parentescoOtroReferente("Otro Parentesco"),
    telefonoReferente("Teléfono"),

    //Datos Instruccion
    primariaBeneficiario("Primaria"),
    secundariaBeneficiario("Secundaria"),
    terciarioBeneficiario("Terciario"),
    universitarioBeneficiario("Universidad"),
    tieneTalleresBeneficiario("¿Realizo Talleres?"),
    tallerRealizadoBeneficiario("Talleres Realizados"),
    tieneCursosRealizadosBeneficiario("¿Realizo Cursos?"),
    cursoRealizadoBeneficiario("Cursos Realizados"),
    instruccionOtros("Otras Instrucciones"),
    instruccionComentario("Comentarios"),

    //Datos Beneficiario - Finales
    telefonoFijoBeneficiario("Teléfono"),
    telefonoCelularBeneficiario("Teléfono Celular"),
    emailBeneficiario("E-Mail"),
    facebookBeneficiario("Facebook"),

    //Datos Familiares
    //Datos Conyuge
    apellidoConyuge("Apellido"),
    nombreConyuge("Nombre"),
    fechaNacimientoConyuge("Fecha de Nacimiento"),
    edadConyuge("Edad"),
    generoConyuge("Género"),
    generoOtroConyuge("Otro Género"),
    tipoDocumentoConyuge("Tipo de Documento"),
    numeroDniConyuge("Número de Documento"),
    ocupacionConyuge("Ocupación"),

    //Hijos
    tieneHijos("¿Tiene Hijos?"),
    cantidadHijos("Cantidad Hijos"),
    //Estos campos se repertirian x cantidad, segun hijos se declaren.
    apellidoHijo("Apellido"),
    nombreHijo("Nombre"),
    fechaNacimientoHijo("Fecha de Nacimiento"),
    edadHijo("Edad"),
    tipoDocumentoHijo("Tipo de Documento"),
    numeroDocumentoHijo("Número de Documento"),
    generoHijo("Género"),
    generoOtroHijo("Otro Género"),
    trabajaHijo("¿Trabaja?"),
    enQueTrabajaHijo("¿En que Trabaja?"),
    estudiaHijo("¿Estudia?"),

    //Otros Miembros Familiares
    tieneMasFamilia("¿Hay más Miembros en la Familia?"),
    //Estos campos se repertirian x cantidad, segun miembros se declaren.
    parentescoFamiliar("Parentesco"),
    parentescoOtroFamiliar("Otro Parentesco"),
    apellidoFamiliar("Apellido"),
    nombreFamiliar("Nombre"),
    fechaNacimientoFamiliar("Fecha de Nacimiento"),
    edadFamiliar("Edad"),
    generoFamilia("Género"),
    generoOtroFamiliar("Otro Género"),
    tipoDocumentoFamiliar("Tipo de Documento"),
    numeroDniFamiliar("Numero de Documento"),

    //Datos de Ingresos
    ingresoSolicitanteSalarioOrigen("Origen Salario"),
    ingresoSolicitanteSalarioTipo("Tipo Salario"),
    ingresoSolicitanteSalarioMonto("Monto Salario"),
    ingresoSolicitanteSubsidioOrigen("Origen Subsidio"),
    ingresoSolicitanteSubsidioTipo("Tipo Subsidio"),
    ingresoSolicitanteSubsidioMonto("Monto Subsidio"),
    ingresoSolicitantePensionOrigen("Origen Pensión"),
    ingresoSolicitantePensionTipo("Tipo Pensión"),
    ingresoSolicitantePensionMonto("Monto Pensión"),
    ingresoSolicitanteOtrosOrigen("Origen Otros Ingresos"),
    ingresoSolicitanteOtrosTipo("Tipo Otros Ingresos"),
    ingresoSolicitanteOtrosMonto("Monto Otros Ingresos"),
    totalIngresosSolicitante("Total de Ingresos Solicitante ($)"),
    ingresoSolicitanteTotal(""),

    //Ingresos Otros Miembros de la Familia
    //Estos campos se repertirian x cantidad, segun miembros se declaren.
    ingresoFamiliarParentesco("Parentesco"),
    ingresoFamiliarParentescoOtros("Otro Parentesco"),
    ingresoFamiliarSalarioOrigen("Origen Salario"),
    ingresoFamiliarSalarioTipo("Tipo Salario"),
    ingresoFamiliarSalarioMonto("Monto Salario"),
    ingresoFamiliarSubsidioOrigen("Origen Subsidio"),
    ingresoFamiliarSubsidioTipo("Tipo Subsidio"),
    ingresoFamiliarSubsidioMonto("Monto Subsidio"),
    ingresoFamiliarPensioOrigen("Origen Pensión"),
    ingresoFamiliarPensionTipo("Tipo Pensión"),
    ingresoFamiliarPensionMonto("Monto Pensión"),
    ingresoFamiliarOtrosOrigen("Origen Otros Ingresos"),
    ingresoFamiliarOtrosTipo("Tipo Otros Ingresos"),
    ingresoFamiliarOtrosMonto("Monto Otros Ingresos"),
    ingresosFamiliares("Total de Ingresos Familiares ($)"),
    ingresoFamiliarTotal("Ingreso Familiar Total"),
    totalIngresoFamilia("Total de Ingreso"),

    ingresosTotales("Suma de Ingresos Solicitante + Familia ($)"),

    ingresoTotalSolicitanteFamiliar(""),

    //Datos de Vivienda
    huboCambiosVivienda("¿Hubo cambios en la vivienda respecto del último relevamiento o es una encuesta nueva?"),
    viviendaAntiguedad("¿Desde qué año vivís en esta vivienda?"),
    viviendaTipoTenencia("Tipo de Tenencia"),
    viviendaTipoTenenciaOtro("Otro Tipo de Tenenecia"),
    viviendaTipoVivienda("Tipo de Vivienda"),
    viviendaTipoViviendaOtro("Otro Tipo de Vivienda"),
    viviendaMaterialesChapa("¿Posee Construcciones en Chapa?"),
    viviendaMaterialesCarton("¿Posee Construcciones en Cartón?"),
    viviendaMaterialesMadera("¿Posee Construcciones en Madera?"),
    viviendaMaterialesAdobe("¿Posee Construcciones en Adobe?"),
    viviendaMaterialesLadrillo("¿Posee Construcciones en Ladrillo?"),
    viviendaMaterialesOtro("¿Posee Construcciones en Otro Tipo de Material?"),
    viviendaOtroMateriales("Descripción del Material de Construcción"),

    viviendaRedDeGas("¿Posee Red de Gas?"),
    viviendaGarrafa("¿Utiliza Garrafas de Gas?"),
    viviendaRedDeAgua("¿Posee Red de Agua?"),
    viviendaBomba("¿Utiliza Bombas de Agua?"),
    viviendaInstalacionLuz("Estado de la Instalación Eléctrica"),
    viviendaCantAmbientes("Cantidad de Ambientes"),
    viviendaCondicionesGenerales("Condiciones Generales de la Vivienda"),
    viviendaCondicionesGeneralesOtro("Otro Tipo de Condiciones Generales"),
    viviendaTipoBarrio("Tipo de Barrio"),
    viviendaTipoBarrioOtro("Otro Tipo de Barrio"),

    //Datos del Emprendimiento
    huboCambiosActividad("¿Cambió la actividad principal o es una nueva encuesta?"),
    actividadNombre("Nombre del Emprendimiento"),
    actividadPrincipal("Actividad Principal"),
    actividadTipo("Tipo de Actividad"),
    actividadTipoComercio(""),
    actividadTipoProduccion(""),
    actividadTipoServicio(""),
    actividadTipoOtro(""),
    actividadOtroTipo("Descripción del Tipo de Actividad"),
    actividadCaracteristica("Característica del Emprendimiento"),
    actividadAntiguedad("Antigüedad del Emprendimiento"),
    actividadDesarrolloAmbulante("¿Lo Desarrolla de Forma Ambulante?"),
    actividadDesarrolloFeria("¿Lo Desarrolla en una Feria?"),
    actividadDesarrolloLocalCasa("¿Lo Desarrolla en un Local o en Casa?"),
    actividadDesarrolloComercio("¿Lo Desarrolla en una Zona Comercial?"),
    actividadDesarrolloOtro("¿Se Desarrolla en Otro Ambiente?"),
    actividadOtroDesarrollo("Descripción de la Forma de Desarrollo"),

    actividadDirCalle("Calle"),
    actividadDirNumero("Número"),
    actividadDirEntreCalles("Entre Calles"),
    actividadDirMunicipio("Municipio"),
    actividadDirLocalidad("Localidad"),
    actividadDirBarrio("Barrio"),
    actividadTelefono("Teléfono"),
    actividadCantidadDias("Cantidad de Dias por Semana"),
    actividadCantidadHoras("Cantidad de Horas por Semana"),

    // Ingresos y Egresos del Emprendimiento o Actividad Actual
    spEfectivo("Efectivo"),
    spFiado("Fiado"),
    spStock("Stock"),
    spMaquinaria("Maquinaria"),
    spInmuebles("Inmuebles"),
    totalSituacionPatrimonial("Total de Situación Patrimonial"),
    patrimonioTotal("Patrimonio Total"),

    comoCompletarVentas("¿Cómo quiere completar las ventas?"),
    ventasLunes("Lunes"),
    ventasMartes("Martes"),
    ventasMiercoles("Miércoles"),
    ventasJueves("Jueves"),
    ventasViernes("Viernes"),
    ventasSabado("Sábado"),
    ventasDomingo("Domingo"),
    ventasEnUnaSemana("Total de Ventas por Semana"),
    calculation_x_semana_001("Total Semanal"),
    calculoVentasDiaMes("Total de Ventas por Mes"),
    calculation("Total Mensual"),
    ventasSemana1("Semana 1"),
    ventasSemana2("Semana 2"),
    ventasSemana3("Semana 3"),
    ventasSemana4("Semana 4"),
    calculation_por_semana_001("Total de Ventas en una Semana"),
    calculoVentasSemanaMes("Total de Ventas Semana-Mes"),
    ventasPorMes("Total de Ventas Mensuales"),
    calculation_total("Total Ingresos del Emprendimiento"),

    egressoActividadAlquiler("Egreso Alquiler"),
    egressoActividadAgua("Egreso Agua"),
    egressoActividadLuz("Egreso Electricidad"),
    egressoActividadTelefono("Egreso Teléfono"),
    egressoActividadImpuestos("Egreso Impuestos"),
    egressoActividadCompras("Egreso Compras"),
    egressoActividadTransporte("Egreso Transporte"),
    egressoActividadMantenimiento("Egreso Mantenimiento"),
    egressoActividadEmpleados("Egreso Empleados"),
    egressoActividadOtros("Otros Egresos"),

    // Creditos del Emprendimiento
    //Estos datos se repiten de acuerdo a los creditos declarados
    egresoActividadTieneCreditos("¿Posee Créditos por el Emprendimiento?"),
    egresoActividadCreditoInstitucion("Institución que da el Crédito"),
    egresoActividadCreditoMonto("Monto del Crédito"),

    totalCreditosEmprendimiento("Total de Créditos del Emprendimiento ($)"),
    egresoActividadCreditoTotal(""),

    totalEgresosEmprendimiento("Total de Egresos del Emprendimiento ($)"),
    egresoActividadEgresoTotal(""),

    ingresoEgresoMes("Diferencia Ingresos/Egresos"),
    ingresoMenosEgresos("Total Ingresos-Egresos"),
    ingresoEgresoQuincena("Diferencia Ingresos/Egresos por Quincena"),
    netosPorQuincena("Total Ingresos-Egresos por Quincena"),
    actividadComentario("Comentario del Emprendimiento"),

    //Finanzas Familiares
    // Ingresos Mensuales
    ingresoMensualEmprendimiento("Ingresos del Emprendimiento"),
    ingresoMensualSolicitante("Ingreso Mensual del Solicitante"),
    ingresoMensualFamiliar("Ingreso Mensual de Familia"),
    sumaIngresosTotales("Ingresos Totales"),
    ingresoMensualTotal("Suma de Ingresos"),

    // Egresos Mensuales
    egresoFamiliarAlimentacion("Gastos Alimentacion"),
    egresoFamiliarHigiene("Gastos Higiene y Limpieza"),
    egresoFamiliarGas("Gastos Servicio de Gas"),
    egresoFamiliarAgua("Gastos Servicio de Agua"),
    egresoFamiliarLuz("Gastos Servicio de Luz"),
    egresoFamiliarTelefono("Gastos de Teléfono"),
    egresoFamiliarEducacion("Gastos en Educación"),
    egresoFamiliarTransporte("Gastos en Transporte"),
    egresoFamiliarSalud("Gastos en Salud"),
    egresoFamiliarVestimenta("Gastos en Vestimenta"),
    egresoFamiliarImpuestos("Gasto en Otros Impuestos"),
    viviendaMontoAlquiler("Gastos en Alquiler"),
    egresoFamiliarCombustible("Gastos en Combustible"),
    egresoFamiliarEsparcimiento("Gastos de Esparcimiento"),
    egresoFamiliarApuestas("Gastos de Apuestas"),
    egresoFamiliarCable("Gastos Servicio de Cable"),
    egresoFamiliarInternet("Gastos Servicio de Internet"),
    egresoFamiliarSeguro("Gastos de Seguro"),
    egresoFamiliarOtros("Otros Gastos"),

    //Creditos Familiares
    // Estos datos se repitaran de acuerdo a los creditos declarados
    egresoFamiliarTieneCreditos("¿Posee algún Crédito?"),
    egresoFamiliarDetalle("Detalles del Crédito"),
    egresoFamiliarMonto("Monto del Crédito"),
    calculationEgresosCreditos("Monto de los Créditos Registrados ($)"),
    egresoFamiliarCreditoParcial("Monto de los Créditos Registrados"),
    totalCreditosFamiliares("Total en Créditos Actuales ($)"),
    egresoFamiliarCreditoTotal("Total de Créditos"),


    calculationTotalEgresosMensual("Total de Egresos Mensuales"),
    egresoFamiliarTotal("Total Egreso Familiar"),
    calculationIngresoEgresoMes("Excedente Mensual"),
    calculation_001_quincena("Excedente Quincenal"),
    excedenteFamiliarQuincenal("Excedente Quincenal"),

    creditoFamiliarImpago("¿Tuvo algún crédito que no pudo pagar?"),
    creditoFamiliarImpagoMonto("Monto del Crédito"),
    creditoFamiliarImpagoFecha("Fecha del Crédito"),
    creditoFamiliarImpagoMotivo("Motivo del Crédito");


    private final String question;

    PDFQuestions(String question) { this.question = question; }

    public String getQuestion(){ return question; }
}
