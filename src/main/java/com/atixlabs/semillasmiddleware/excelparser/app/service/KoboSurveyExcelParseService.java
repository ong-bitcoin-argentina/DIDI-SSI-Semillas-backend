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
            List<FormPDF> formList = ExcelUtils.parseKoboSurveyIntoList(workbook.getSheetAt(0), FormPDF.class);
            List<Child> childList = ExcelUtils.parseKoboSurveyIntoList(workbook.getSheet("grupo_hijos"), Child.class);
            List<FamilyMember> familyMemberList = ExcelUtils.parseKoboSurveyIntoList(workbook.getSheet("grupo_datos_miembro"), FamilyMember.class);
            List<FamilyMemberIncome> familyMemberIncomeList =
                    ExcelUtils.parseKoboSurveyIntoList(workbook.getSheet("grupo_ingresos_miembro"), FamilyMemberIncome.class);
            List<EntrepreneurshipCredit> entrepreneurshipCreditList =
                    ExcelUtils.parseKoboSurveyIntoList(workbook.getSheet("grupo_creditos_emprendimiento"), EntrepreneurshipCredit.class);
            List<FamilyCredit> familyCreditList =
                    ExcelUtils.parseKoboSurveyIntoList(workbook.getSheet("grupo_creditos_familiares"), FamilyCredit.class);

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
