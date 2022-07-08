package com.atixlabs.semillasmiddleware.excelparser.app.constants;

public enum PDFQuestions {

    //Datos Encuesta
    GRUPOSOLIDARIO("Grupo Solidario"),
    ASESORA("Asesor/a"),

    //Datos Beneficiario
    NOMBREBENEFICIARIO(Constants.NAME),
    APELLIDOBENEFICIARIO(Constants.LASTNAME),
    FECHANACIMIENTOBENEFICIARIO(Constants.BIRTHDAYDATE),
    EDADBENEFICIARIO(Constants.AGE),
    ESTADOCIVILBENEFICIARIO("Estado Civil"),
    ESTADOCIVILOTROBENEFICIARIO("Otro Estado Civil"),
    GENEROBENEFICIARIO(Constants.GENRE),
    GENEROOTROBENEFICIARIO(Constants.OTHERGENRE),
    TIPODOCUMENTOBENEFICIARIO(Constants.DOCUMENTTYPE),
    NUMERODNIBENEFICIARIO(Constants.DOCUMENTNUMBER),
    NACIONALIDADBENEFICIARIO("Nacionalidad"),
    TIEMPOENELPAISBENEFICIARIO("Tiempo en el País"),

    //Datos Domicilio
    VIVIENDADIRECCIONCALLE(Constants.ADDRESSSTREET),
    VIVIENDADIRECCIONNUMERO(Constants.ADDRESSNUMBER),
    VIVIENDADIRECCIONENTRECALLES(Constants.ADDRESSBETWEENSTREETS),
    VIVIENDADIRECCIONMUNICIPIO(Constants.ADDRESSMUNICIPALITY),
    VIVIENDADOMICILIOLOCALIDAD(Constants.ADDRESSLOCATION),
    VIVIENDADOMICILIOBARRIO(Constants.ADDRESSNEIGHBORHOOD),

    //Datos Otro Domicilio
    OTRODOMICILIOBENEFICIARIO("¿Tiene otro Domicilio?"),
    OTRODOMICILIODIRECCIONCALLE(Constants.ADDRESSSTREET),
    OTRODOMICILIODIRECCIONNUMERO(Constants.ADDRESSNUMBER),
    OTRODOMICILIODIRECCIONENTRECALLES(Constants.ADDRESSBETWEENSTREETS),
    OTRODOMICILIODIRECCIONMUNICIPIO(Constants.ADDRESSMUNICIPALITY),
    OTRODOMICILIODOMICILIOLOCALIDAD(Constants.ADDRESSLOCATION),
    OTRODOMICILIODOMICILIOBARRIO(Constants.ADDRESSNEIGHBORHOOD),

    //Datos Referente
    APELLIDOREFERENTE(Constants.LASTNAME),
    NOMBREREFERENTE(Constants.NAME),
    PARENTESCOREFERENTE(Constants.KINSMAN),
    PARENTESCOOTROREFERENTE(Constants.OTHERKINSMAN),
    TELEFONOREFERENTE(Constants.PHONENUMBER),

    //Datos Instruccion
    PRIMARIABENEFICIARIO("Primaria"),
    SECUNDARIABENEFICIARIO("Secundaria"),
    TERCIARIOBENEFICIARIO("Terciario"),
    UNIVERSITARIOBENEFICIARIO("Universidad"),
    TIENETALLERESBENEFICIARIO("¿Realizo Talleres?"),
    TALLERREALIZADOBENEFICIARIO("Talleres Realizados"),
    TIENECURSOSREALIZADOSBENEFICIARIO("¿Realizo Cursos?"),
    CURSOREALIZADOBENEFICIARIO("Cursos Realizados"),
    INSTRUCCIONOTROS("Otras Instrucciones"),
    INSTRUCCIONCOMENTARIO("Comentarios"),

    //Datos Beneficiario - Finales
    TELEFONOFIJOBENEFICIARIO(Constants.PHONENUMBER),
    TELEFONOCELULARBENEFICIARIO("Teléfono Celular"),
    EMAILBENEFICIARIO("E-Mail"),
    FACEBOOKBENEFICIARIO("Facebook"),

    //Datos Familiares
    //Datos Conyuge
    APELLIDOCONYUGE(Constants.LASTNAME),
    NOMBRECONYUGE(Constants.NAME),
    FECHANACIMIENTOCONYUGE(Constants.BIRTHDAYDATE),
    EDADCONYUGE(Constants.AGE),
    GENEROCONYUGE(Constants.GENRE),
    GENEROOTROCONYUGE(Constants.OTHERGENRE),
    TIPODOCUMENTOCONYUGE(Constants.DOCUMENTTYPE),
    NUMERODNICONYUGE(Constants.DOCUMENTNUMBER),
    OCUPACIONCONYUGE("Ocupación"),

    //Hijos
    TIENEHIJOS("¿Tiene Hijos?"),
    CANTIDADHIJOS("Cantidad Hijos"),
    //Estos campos se repertirian x cantidad, segun hijos se declaren.
    APELLIDOHIJO(Constants.LASTNAME),
    NOMBREHIJO(Constants.NAME),
    FECHANACIMIENTOHIJO(Constants.BIRTHDAYDATE),
    EDADHIJO(Constants.AGE),
    TIPODOCUMENTOHIJO(Constants.DOCUMENTTYPE),
    NUMERODOCUMENTOHIJO(Constants.DOCUMENTNUMBER),
    GENEROHIJO(Constants.GENRE),
    GENEROOTROHIJO(Constants.OTHERGENRE),
    TRABAJAHIJO("¿Trabaja?"),
    ENQUETRABAJAHIJO("¿En que Trabaja?"),
    ESTUDIAHIJO("¿Estudia?"),

    //Otros Miembros Familiares
    TIENEMASFAMILIA("¿Hay más Miembros en la Familia?"),
    //Estos campos se repertirian x cantidad, segun miembros se declaren.
    PARENTESCOFAMILIAR(Constants.KINSMAN),
    PARENTESCOOTROFAMILIAR(Constants.OTHERKINSMAN),
    APELLIDOFAMILIAR(Constants.LASTNAME),
    NOMBREFAMILIAR(Constants.NAME),
    FECHANACIMIENTOFAMILIAR(Constants.BIRTHDAYDATE),
    EDADFAMILIAR(Constants.AGE),
    GENEROFAMILIA(Constants.GENRE),
    GENEROOTROFAMILIAR(Constants.OTHERGENRE),
    TIPODOCUMENTOFAMILIAR("Tipo de Documento"),
    NUMERODNIFAMILIAR("Numero de Documento"),

    //Datos de Ingresos
    INGRESOSOLICITANTESALARIOORIGEN("Origen Salario"),
    INGRESOSOLICITANTESALARIOTIPO("Tipo Salario"),
    INGRESOSOLICITANTESALARIOMONTO("Monto Salario"),
    INGRESOSOLICITANTESUBSIDIOORIGEN("Origen Subsidio"),
    INGRESOSOLICITANTESUBSIDIOTIPO("Tipo Subsidio"),
    INGRESOSOLICITANTESUBSIDIOMONTO("Monto Subsidio"),
    INGRESOSOLICITANTEPENSIONORIGEN("Origen Pensión"),
    INGRESOSOLICITANTEPENSIONTIPO("Tipo Pensión"),
    INGRESOSOLICITANTEPENSIONMONTO("Monto Pensión"),
    INGRESOSOLICITANTEOTROSORIGEN("Origen Otros Ingresos"),
    INGRESOSOLICITANTEOTROSTIPO("Tipo Otros Ingresos"),
    INGRESOSOLICITANTEOTROSMONTO("Monto Otros Ingresos"),
    TOTALINGRESOSSOLICITANTE("Total de Ingresos Solicitante ($)"),
    INGRESOSOLICITANTETOTAL(""),

    //Ingresos Otros Miembros de la Familia
    //Estos campos se repertirian x cantidad, segun miembros se declaren.
    INGRESOFAMILIARPARENTESCO(Constants.KINSMAN),
    INGRESOFAMILIARPARENTESCOOTROS(Constants.OTHERKINSMAN),
    INGRESOFAMILIARSALARIOORIGEN("Origen Salario"),
    INGRESOFAMILIARSALARIOTIPO("Tipo Salario"),
    INGRESOFAMILIARSALARIOMONTO("Monto Salario"),
    INGRESOFAMILIARSUBSIDIOORIGEN("Origen Subsidio"),
    INGRESOFAMILIARSUBSIDIOTIPO("Tipo Subsidio"),
    INGRESOFAMILIARSUBSIDIOMONTO("Monto Subsidio"),
    INGRESOFAMILIARPENSIOORIGEN("Origen Pensión"),
    INGRESOFAMILIARPENSIONTIPO("Tipo Pensión"),
    INGRESOFAMILIARPENSIONMONTO("Monto Pensión"),
    INGRESOFAMILIAROTROSORIGEN("Origen Otros Ingresos"),
    INGRESOFAMILIAROTROSTIPO("Tipo Otros Ingresos"),
    INGRESOFAMILIAROTROSMONTO("Monto Otros Ingresos"),
    INGRESOSFAMILIARES("Total de Ingresos Familiares ($)"),
    INGRESOFAMILIARTOTAL("Ingreso Familiar Total"),
    TOTALINGRESOFAMILIA("Total de Ingreso"),

    INGRESOSTOTALES("Suma de Ingresos Solicitante + Familia ($)"),

    INGRESOTOTALSOLICITANTEFAMILIAR(""),

    //Datos de Vivienda
    HUBOCAMBIOSVIVIENDA("¿Hubo cambios en la vivienda respecto del último relevamiento o es una encuesta nueva?"),
    VIVIENDAANTIGUEDAD("¿Desde qué año vivís en esta vivienda?"),
    VIVIENDATIPOTENENCIA("Tipo de Tenencia"),
    VIVIENDATIPOTENENCIAOTRO("Otro Tipo de Tenenecia"),
    VIVIENDATIPOVIVIENDA("Tipo de Vivienda"),
    VIVIENDATIPOVIVIENDAOTRO("Otro Tipo de Vivienda"),
    VIVIENDAMATERIALESCHAPA("¿Posee Construcciones en Chapa?"),
    VIVIENDAMATERIALESCARTON("¿Posee Construcciones en Cartón?"),
    VIVIENDAMATERIALESMADERA("¿Posee Construcciones en Madera?"),
    VIVIENDAMATERIALESADOBE("¿Posee Construcciones en Adobe?"),
    VIVIENDAMATERIALESLADRILLO("¿Posee Construcciones en Ladrillo?"),
    VIVIENDAMATERIALESOTRO("¿Posee Construcciones en Otro Tipo de Material?"),
    VIVIENDAOTROMATERIALES("Descripción del Material de Construcción"),

    VIVIENDAREDDEGAS("¿Posee Red de Gas?"),
    VIVIENDAGARRAFA("¿Utiliza Garrafas de Gas?"),
    VIVIENDAREDDEAGUA("¿Posee Red de Agua?"),
    VIVIENDABOMBA("¿Utiliza Bombas de Agua?"),
    VIVIENDAINSTALACIONLUZ("Estado de la Instalación Eléctrica"),
    VIVIENDACANTAMBIENTES("Cantidad de Ambientes"),
    VIVIENDACONDICIONESGENERALES("Condiciones Generales de la Vivienda"),
    VIVIENDACONDICIONESGENERALESOTRO("Otro Tipo de Condiciones Generales"),
    VIVIENDATIPOBARRIO("Tipo de Barrio"),
    VIVIENDATIPOBARRIOOTRO("Otro Tipo de Barrio"),

    //Datos del Emprendimiento
    HUBOCAMBIOSACTIVIDAD("¿Cambió la actividad principal o es una nueva encuesta?"),
    ACTIVIDADNOMBRE("Nombre del Emprendimiento"),
    ACTIVIDADPRINCIPAL("Actividad Principal"),
    ACTIVIDADTIPO("Tipo de Actividad"),
    ACTIVIDADTIPOCOMERCIO(""),
    ACTIVIDADTIPOPRODUCCION(""),
    ACTIVIDADTIPOSERVICIO(""),
    ACTIVIDADTIPOOTRO(""),
    ACTIVIDADOTROTIPO("Descripción del Tipo de Actividad"),
    ACTIVIDADCARACTERISTICA("Característica del Emprendimiento"),
    ACTIVIDADANTIGUEDAD("Antigüedad del Emprendimiento"),
    ACTIVIDADDESARROLLOAMBULANTE("¿Lo Desarrolla de Forma Ambulante?"),
    ACTIVIDADDESARROLLOFERIA("¿Lo Desarrolla en una Feria?"),
    ACTIVIDADDESARROLLOLOCALCASA("¿Lo Desarrolla en un Local o en Casa?"),
    ACTIVIDADDESARROLLOCOMERCIO("¿Lo Desarrolla en una Zona Comercial?"),
    ACTIVIDADDESARROLLOOTRO("¿Se Desarrolla en Otro Ambiente?"),
    ACTIVIDADOTRODESARROLLO("Descripción de la Forma de Desarrollo"),

    ACTIVIDADDIRCALLE(Constants.ADDRESSSTREET),
    ACTIVIDADDIRNUMERO(Constants.ADDRESSNUMBER),
    ACTIVIDADDIRENTRECALLES(Constants.ADDRESSBETWEENSTREETS),
    ACTIVIDADDIRMUNICIPIO(Constants.ADDRESSMUNICIPALITY),
    ACTIVIDADDIRLOCALIDAD(Constants.ADDRESSLOCATION),
    ACTIVIDADDIRBARRIO(Constants.ADDRESSNEIGHBORHOOD),
    ACTIVIDADTELEFONO(Constants.PHONENUMBER),
    ACTIVIDADCANTIDADDIAS("Cantidad de Dias por Semana"),
    ACTIVIDADCANTIDADHORAS("Cantidad de Horas por Semana"),

    // Ingresos y Egresos del Emprendimiento o Actividad Actual
    SPEFECTIVO("Efectivo"),
    SPFIADO("Fiado"),
    SPSTOCK("Stock"),
    SPMAQUINARIA("Maquinaria"),
    SPINMUEBLES("Inmuebles"),
    TOTALSITUACIONPATRIMONIAL("Total de Situación Patrimonial"),
    PATRIMONIOTOTAL("Patrimonio Total"),

    COMOCOMPLETARVENTAS("¿Cómo quiere completar las ventas?"),
    VENTASLUNES("Lunes"),
    VENTASMARTES("Martes"),
    VENTASMIERCOLES("Miércoles"),
    VENTASJUEVES("Jueves"),
    VENTASVIERNES("Viernes"),
    VENTASSABADO("Sábado"),
    VENTASDOMINGO("Domingo"),
    VENTASENUNASEMANA("Total de Ventas por Semana"),
    CALCULATION_X_SEMANA_001("Total Semanal"),
    CALCULOVENTASDIAMES("Total de Ventas por Mes"),
    CALCULATION("Total Mensual"),
    VENTASSEMANA1("Semana 1"),
    VENTASSEMANA2("Semana 2"),
    VENTASSEMANA3("Semana 3"),
    VENTASSEMANA4("Semana 4"),
    CALCULATION_POR_SEMANA_001("Total de Ventas en una Semana"),
    CALCULOVENTASSEMANAMES("Total de Ventas Semana-Mes"),
    VENTASPORMES("Total de Ventas Mensuales"),
    CALCULATION_TOTAL("Total Ingresos del Emprendimiento"),

    EGRESSOACTIVIDADALQUILER("Egreso Alquiler"),
    EGRESSOACTIVIDADAGUA("Egreso Agua"),
    EGRESSOACTIVIDADLUZ("Egreso Electricidad"),
    EGRESSOACTIVIDADTELEFONO("Egreso Teléfono"),
    EGRESSOACTIVIDADIMPUESTOS("Egreso Impuestos"),
    EGRESSOACTIVIDADCOMPRAS("Egreso Compras"),
    EGRESSOACTIVIDADTRANSPORTE("Egreso Transporte"),
    EGRESSOACTIVIDADMANTENIMIENTO("Egreso Mantenimiento"),
    EGRESSOACTIVIDADEMPLEADOS("Egreso Empleados"),
    EGRESSOACTIVIDADOTROS("Otros Egresos"),

    // Creditos del Emprendimiento
    //Estos datos se repiten de acuerdo a los creditos declarados
    EGRESOACTIVIDADTIENECREDITOS("¿Posee Créditos por el Emprendimiento?"),
    EGRESOACTIVIDADCREDITOINSTITUCION("Institución que da el Crédito"),
    EGRESOACTIVIDADCREDITOMONTO(Constants.CREDITMOUNT),

    TOTALCREDITOSEMPRENDIMIENTO("Total de Créditos del Emprendimiento ($)"),
    EGRESOACTIVIDADCREDITOTOTAL(""),

    TOTALEGRESOSEMPRENDIMIENTO("Total de Egresos del Emprendimiento ($)"),
    EGRESOACTIVIDADEGRESOTOTAL(""),

    INGRESOEGRESOMES("Diferencia Ingresos/Egresos"),
    INGRESOMENOSEGRESOS("Total Ingresos-Egresos"),
    INGRESOEGRESOQUINCENA("Diferencia Ingresos/Egresos por Quincena"),
    NETOSPORQUINCENA("Total Ingresos-Egresos por Quincena"),
    ACTIVIDADCOMENTARIO("Comentario del Emprendimiento"),

    //Finanzas Familiares
    // Ingresos Mensuales
    INGRESOMENSUALEMPRENDIMIENTO("Ingresos del Emprendimiento"),
    INGRESOMENSUALSOLICITANTE("Ingreso Mensual del Solicitante"),
    INGRESOMENSUALFAMILIAR("Ingreso Mensual de Familia"),
    SUMAINGRESOSTOTALES("Ingresos Totales"),
    INGRESOMENSUALTOTAL("Suma de Ingresos"),

    // Egresos Mensuales
    EGRESOFAMILIARALIMENTACION("Gastos Alimentacion"),
    EGRESOFAMILIARHIGIENE("Gastos Higiene y Limpieza"),
    EGRESOFAMILIARGAS("Gastos Servicio de Gas"),
    EGRESOFAMILIARAGUA("Gastos Servicio de Agua"),
    EGRESOFAMILIARLUZ("Gastos Servicio de Luz"),
    EGRESOFAMILIARTELEFONO("Gastos de Teléfono"),
    EGRESOFAMILIAREDUCACION("Gastos en Educación"),
    EGRESOFAMILIARTRANSPORTE("Gastos en Transporte"),
    EGRESOFAMILIARSALUD("Gastos en Salud"),
    EGRESOFAMILIARVESTIMENTA("Gastos en Vestimenta"),
    EGRESOFAMILIARIMPUESTOS("Gasto en Otros Impuestos"),
    VIVIENDAMONTOALQUILER("Gastos en Alquiler"),
    EGRESOFAMILIARCOMBUSTIBLE("Gastos en Combustible"),
    EGRESOFAMILIARESPARCIMIENTO("Gastos de Esparcimiento"),
    EGRESOFAMILIARAPUESTAS("Gastos de Apuestas"),
    EGRESOFAMILIARCABLE("Gastos Servicio de Cable"),
    EGRESOFAMILIARINTERNET("Gastos Servicio de Internet"),
    EGRESOFAMILIARSEGURO("Gastos de Seguro"),
    EGRESOFAMILIAROTROS("Otros Gastos"),

    //Creditos Familiares
    // Estos datos se repitaran de acuerdo a los creditos declarados
    EGRESOFAMILIARTIENECREDITOS("¿Posee algún Crédito?"),
    EGRESOFAMILIARDETALLE("Detalles del Crédito"),
    EGRESOFAMILIARMONTO(Constants.CREDITMOUNT),
    CALCULATIONEGRESOSCREDITOS("Monto de los Créditos Registrados ($)"),
    EGRESOFAMILIARCREDITOPARCIAL("Monto de los Créditos Registrados"),
    TOTALCREDITOSFAMILIARES("Total en Créditos Actuales ($)"),
    EGRESOFAMILIARCREDITOTOTAL("Total de Créditos"),


    CALCULATIONTOTALEGRESOSMENSUAL("Total de Egresos Mensuales"),
    EGRESOFAMILIARTOTAL("Total Egreso Familiar"),
    CALCULATIONINGRESOEGRESOMES("Excedente Mensual"),
    CALCULATION_001_QUINCENA("Excedente Quincenal"),
    EXCEDENTEFAMILIARQUINCENAL("Excedente Quincenal"),

    CREDITOFAMILIARIMPAGO("¿Tuvo algún crédito que no pudo pagar?"),
    CREDITOFAMILIARIMPAGOMONTO(Constants.CREDITMOUNT),
    CREDITOFAMILIARIMPAGOFECHA("Fecha del Crédito"),
    CREDITOFAMILIARIMPAGOMOTIVO("Motivo del Crédito");


    private final String question;

    // Constantes

    private static class Constants{
        public static final String NAME = "Nombre";
        public static final String LASTNAME = "Apellido";

        public static final String AGE = "Edad ";
        public static final String GENRE = "Género";
        public static final String OTHERGENRE = "Otro Género";
        public static final String DOCUMENTTYPE = "Tipo Documento";
        public static final String DOCUMENTNUMBER = "Número de Documento";
        public static final String ADDRESSSTREET = "Calle";
        public static final String ADDRESSNUMBER = "Número";
        public static final String ADDRESSBETWEENSTREETS = "Entre Calles";
        public static final String ADDRESSMUNICIPALITY = "Municipio";
        public static final String ADDRESSLOCATION = "Localidad";
        public static final String ADDRESSNEIGHBORHOOD = "Barrio";
        public static final String KINSMAN = "Parentesco ";
        public static final String OTHERKINSMAN = "Otro Parentesco ";
        public static final String PHONENUMBER = "Número de Teléfono";
        public static final String BIRTHDAYDATE = "Fecha de Nacimiento";
        public static final String CREDITMOUNT = "Monto del Crédito";
    }


    PDFQuestions(String question) { this.question = question; }

    public String getQuestion(){ return question; }
}
