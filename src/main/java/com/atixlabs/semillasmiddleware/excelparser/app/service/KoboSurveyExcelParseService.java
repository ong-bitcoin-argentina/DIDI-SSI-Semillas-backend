package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.model.excel.*;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.filemanager.service.FileManagerService;
import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import com.atixlabs.semillasmiddleware.pdfparser.surveyPdfParser.service.PdfParserService;
import com.atixlabs.semillasmiddleware.pdfparser.util.PDFUtil;
import com.poiji.bind.Poiji;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KoboSurveyExcelParseService{
    /**
     * Levanta el archivo y lee linea por linea
     *
     * @param row
     * @return
     * @throws FileNotFoundException
     */

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private PdfParserService pdfParserService;

    @Autowired
    private FileManagerService fileManagerService;

    public ProcessExcelFileResult processSheetFileFormKobo(String filePath) throws Exception {

        log.info("Validation for file "+ fileUtil.getFileByPath(filePath));

        File xlsxFile = fileUtil.getFileByPath(filePath);// Sin cambios
        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();// Sin cambios, me gusta este response
        processExcelFileResult.setFileName(filePath);// Sin Cambios

        FileInputStream fileInput = new FileInputStream(xlsxFile);//Sin Cambios

        XSSFWorkbook workbook = new XSSFWorkbook(fileInput);//Sin Cambios

        try{
            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFSheet childGroupSheet = workbook.getSheet("grupo_hijos");
            XSSFSheet familyMemberGroupSheet = workbook.getSheet("grupo_datos_miembro");
            XSSFSheet familyMemberIncomeGroupSheet = workbook.getSheet("grupo_ingresos_miembro");
            XSSFSheet entrepreneurshipCreditSheet = workbook.getSheet("grupo_creditos_emprendimiento");
            XSSFSheet familyCreditGroupSheet = workbook.getSheet("grupo_creditos_familiares");

        /* Aca podemos cambiar el metodo formatHeader por otro,o bien agregarle un nuevo parametro para poder generar las preguntas correspondientes
            que se mostrararan en el excel, de esta forma podemos reutilizar un poco la logica que armaba el anterior PDF.
         */
            ExcelUtils.formatHeader(worksheet);
            ExcelUtils.formatHeader(childGroupSheet);
            ExcelUtils.formatHeader(familyMemberGroupSheet);
            ExcelUtils.formatHeader(familyMemberIncomeGroupSheet);
            ExcelUtils.formatHeader(entrepreneurshipCreditSheet);
            ExcelUtils.formatHeader(familyCreditGroupSheet);

            List<FormPDF> formList = Poiji.fromExcel(worksheet,FormPDF.class);
            List<Child> childList = childGroupSheet!=null?Poiji.fromExcel(childGroupSheet, Child.class): new ArrayList<>();
            List<FamilyMember> familyMemberList = familyMemberGroupSheet!=null?Poiji.fromExcel(familyMemberGroupSheet, FamilyMember.class): new ArrayList<>();

            List<FamilyMemberIncome> familyMemberIncomeList =
                    familyMemberIncomeGroupSheet!=null?Poiji.fromExcel(familyMemberIncomeGroupSheet, FamilyMemberIncome.class):new ArrayList<>();
            List<EntrepreneurshipCredit> entrepreneurshipCreditList =
                    entrepreneurshipCreditSheet!=null?Poiji.fromExcel(entrepreneurshipCreditSheet, EntrepreneurshipCredit.class):new ArrayList<>();
            List<FamilyCredit> familyCreditList =
                    familyCreditGroupSheet!=null?Poiji.fromExcel(familyCreditGroupSheet, FamilyCredit.class): new ArrayList<>();

            PDFUtil.setPDFData(formList, childList, familyMemberList, familyMemberIncomeList, entrepreneurshipCreditList, familyCreditList);

            setDownloadableFileName(pdfParserService.generatePDFFromKobo(formList), processExcelFileResult);
        } catch (NotOfficeXmlFileException c) {
            log.error("Invalid file format: " + filePath);
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader("Error en el archivo")
                    .errorBody("Por favor, verificá que el formato del archivo sea correcto.")
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
        } finally {
            workbook.close();
            fileInput.close();
        }

        return processExcelFileResult;
    }

    private static final String ZIP_SUFFIX = "Encuesta_form-";

    private void setDownloadableFileName(List<String> pdfsGenerated, ProcessExcelFileResult processExcelFileResult) throws IOException {
        if(pdfsGenerated.size() > 1)
            processExcelFileResult.setDownloadableFileName(fileManagerService.zipAll(pdfsGenerated, ZIP_SUFFIX));
        else
            processExcelFileResult.setDownloadableFileName(pdfsGenerated.get(0));
    }
}
