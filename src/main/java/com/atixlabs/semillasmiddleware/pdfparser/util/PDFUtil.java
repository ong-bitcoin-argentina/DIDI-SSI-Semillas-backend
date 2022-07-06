package com.atixlabs.semillasmiddleware.pdfparser.util;

import com.atixlabs.semillasmiddleware.app.model.excel.*;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PDFQuestions;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.SubCategories;
import com.atixlabs.semillasmiddleware.pdfparser.exception.PdfCannotBeCreatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


public class PDFUtil {
    private PDFUtil(){}
    public static final String TEMPLATE_NAME = "excell_survey_data.html";
    public static final String IMG_LOGO_NAME = "logo-semillas.svg";
    public static final String PDF_SUFFIX = "Encuesta_";
    public static final String CATEGORY_NAME_PARAM = "{{categoryName}}";
    public static final String TABLE_CONTENT_PARAM = "{{tableContent}}";
    public static final String CREATE_DATE_PARAM = "{{creationDate}}";
    public static final String SEMILLAS_IMAGE_BASE64_PARAM = "{{semillasImgB64}}";
    public static final String QUESTION_PARAM = "{{question}}";
    public static final String ANSWER_PARAM = "{{answer}}";
    public static final String SUBCATEGORY_PARAM = "{{subCategory}}";

    public static final String ROWTEMPLATE = "        <tr>\n" +
            "            <td >{{question}}</td>\n" +
            "            <td >{{answer}}</td>\n" +
            "        </tr>";

    public static final String SUBCATEGORYTEMPLATE = "   <tr>\n" +
            "            <th colspan=\"2\">\n" +
            "              {{subCategory}}</th>\n" +
            "        </tr>";
    public static final String HEADERTEMPLATE = "        <tr>\n" +
            "            <th colspan=\"2\">\n" +
            "                <br /><u/><i/>"+CATEGORY_NAME_PARAM+"\n" +
            "            </th>\n" +
            "        </tr>\n";

    public static  String createTemporaryPDF(String prefixName, String html){
        File file;
        try{
            file = File.createTempFile(prefixName, ".pdf", new File(System.getProperty("java.io.tmpdir")));
            writePdfFromHtml(file, html);
        }catch (IOException ex ){
            throw new PdfCannotBeCreatedException(ex.getMessage());
        }
        return file.getName();
    }

    private static void writePdfFromHtml(File file, String html) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), new FileOutputStream(file), converterProperties);
    }

    public static List<String> generateData(Field[] fields){
        List<String> tmpLst = new ArrayList<>();
        for (Field field: fields) {
            tmpLst.add(field.getName());
        }

        return tmpLst;
    }

    private static String generateHtml(List<Object> lst){
        StringBuilder html = new StringBuilder();
        // Obtengo las keys relacionadas a la clase.
        List<String> structure = generateData(lst.get(0).getClass().getDeclaredFields());

        // Limpia variables Innecesarias
        structure.remove("rowIndex");
        structure.remove("parentIndex");

        for (int i=0; i<lst.size(); i++) {
            html.append(SUBCATEGORYTEMPLATE.replace(SUBCATEGORY_PARAM, String.format(SubCategories.valueOf(lst.get(0).getClass().getSimpleName().toUpperCase()).getSubCategories(0).concat(" "+(i+1)))));
            Map<String, Object> info = PDFUtil.generateKeys(lst.get(i));
            html.append(generateSlot(structure, info));
        }

        return html.toString();
    }

    private static String generateSlot(List<String> structure, Map<String, Object> data){
        StringBuilder html = new StringBuilder();
        for (String slot: structure) {
            if (data.get(StringUtils.unCapitalize(slot)) != null) html.append(ROWTEMPLATE.replace(QUESTION_PARAM, PDFQuestions.valueOf(slot.toUpperCase()).getQuestion())
                    .replace(ANSWER_PARAM, data.get(StringUtils.unCapitalize(slot)).toString()));
        }
        return html.toString();
    }

    public static Map<String, Object> generateKeys(Object data){
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(data, TreeMap.class);
    }

    public static void setPDFData(List<FormPDF> pdfData, List<Child> childList, List<FamilyMember> familyList, List<FamilyMemberIncome> familyMembers,
                                  List<EntrepreneurshipCredit> eCredits, List<FamilyCredit> fCredits){
        for (FormPDF form: pdfData) {
            // Otro Domicilio
            if (form.getOtroDomicilioBeneficiario().equals("Si")) form.setSubCat_OtroDomicilio(SubCategories.BENEFICIARIO.getSubCategories(1));

            // Hijos
            if (validateList(childList.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList()))
                    && form.getTieneHijos().equals("Si")) form.setHijos(generateHtml(childList.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList())));

            // Miembros Familiares
            if(validateList(familyList.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList()))
                    && form.getTieneMasFamilia().equals("Si")) form.setFamilyMembers(generateHtml(familyList.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList())));

            // Ingresos Miembros Familiares
            if(validateList(familyMembers.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList()))) form.setFamilyMemberIncome(generateHtml(familyMembers.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList())));

            // Créditos Del Emprendimiento
            if(validateList(eCredits.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList()))
                    && form.getEgresoActividadTieneCreditos().equals("Si")) form.setCreditosEmprendimiento(generateHtml(eCredits.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList())));

            // Créditos Familiares
            if(validateList(fCredits.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList()))
                    && form.getEgresoFamiliarTieneCreditos().equals("Si")) form.setCreditosFamiliares(generateHtml(fCredits.stream().filter(p -> p.getParentIndex() == form.getIndex()).collect(Collectors.toList())));

            // Créditos Impagos
            if (form.getCreditoFamiliarImpago() != null && form.getCreditoFamiliarImpago().equals("Si")) form.setSubCat_CreditosImpagos(SubCategories.FINANZASFAMILIARES.getSubCategories(2));
        }
    }

    private static <T> boolean validateList(List<T> lst){
        return lst != null && !lst.isEmpty();
    }
}
