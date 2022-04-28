package com.atixlabs.semillasmiddleware.excelparser.service;

import com.atixlabs.semillasmiddleware.app.model.excel.*;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorDetail;
import com.atixlabs.semillasmiddleware.excelparser.dto.ExcelErrorType;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.app.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.filemanager.util.FileUtil;
import com.poiji.bind.Poiji;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public abstract class ExcelParseService {

    /**
     *
     * Levanta el archivo y lee linea por linea
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */

    @Autowired
    FileUtil fileUtil;


    public ProcessExcelFileResult processSheetFileFormKobo(String filePath) throws Exception {

        log.info("Validation for file "+ fileUtil.getFileByPath(filePath));

        File xlsxFile = fileUtil.getFileByPath(filePath);
        FileInputStream fileInput = new FileInputStream(xlsxFile);
        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
        try{
        XSSFSheet worksheet = workbook.getSheetAt(0);
        XSSFSheet childGroupSheet = workbook.getSheet("grupo_hijos");
        XSSFSheet familyMemberGroupSheet = workbook.getSheet("grupo_datos_miembro");
        XSSFSheet familyMemberIncomeGroupSheet = workbook.getSheet("grupo_ingresos_miembro");
        XSSFSheet entrepreneurshipCreditSheet = workbook.getSheet("grupo_creditos_emprendimiento");
        XSSFSheet familyCreditGroupSheet = workbook.getSheet("grupo_creditos_familiares");

        ExcelUtils.formatHeader(worksheet);
        ExcelUtils.formatHeader(childGroupSheet);
        ExcelUtils.formatHeader(familyMemberGroupSheet);
        ExcelUtils.formatHeader(familyMemberIncomeGroupSheet);
        ExcelUtils.formatHeader(entrepreneurshipCreditSheet);
        ExcelUtils.formatHeader(familyCreditGroupSheet);

        List<Form> formList = Poiji.fromExcel(worksheet,Form.class);
        List<Child> childList = childGroupSheet!=null?Poiji.fromExcel(childGroupSheet,Child.class): new ArrayList<>();
        List<FamilyMember> familyMemberList = familyMemberGroupSheet!=null?Poiji.fromExcel(familyMemberGroupSheet, FamilyMember.class): new ArrayList<>();

        List<FamilyMemberIncome> familyMemberIncomeList =
                familyMemberIncomeGroupSheet!=null?Poiji.fromExcel(familyMemberIncomeGroupSheet, FamilyMemberIncome.class):new ArrayList<>();
        List<EntrepreneurshipCredit> entrepreneurshipCreditList =
                entrepreneurshipCreditSheet!=null?Poiji.fromExcel(entrepreneurshipCreditSheet, EntrepreneurshipCredit.class):new ArrayList<>();
        List<FamilyCredit> familyCreditList =
                familyCreditGroupSheet!=null?Poiji.fromExcel(familyCreditGroupSheet, FamilyCredit.class): new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowsIterator = sheet.rowIterator();

            //está eliminando la linea de cabeceras que por ahora quiero ver:
            if (rowsIterator.hasNext())
                rowsIterator.next();

            while (rowsIterator.hasNext()) {
                // Se creara un nuevo metodo para este punto, dentro de la rama SEM-116/implementacion_pdf
                //processRow(rowsIterator.next(), rowsIterator.hasNext(), processExcelFileResult);
            }

        return processExcelFileResult;
    } catch (NotOfficeXmlFileException c) {
        log.error("Invalid file format: " + filePath);
        processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                .errorHeader("Error en el archivo")
                .errorBody("Por favor, verificá que el formato del archivo sea correcto.")
                .errorType(ExcelErrorType.OTHER)
                .build()
        );
        return processExcelFileResult;
    } finally {
            workbook.close();
    }
    }

    public ProcessExcelFileResult processSingleSheetFile(String filePath, boolean createCredentials,
                                                         boolean skipIdentityCredentials,
                                                         boolean pdfValidation) throws Exception {
        log.info("Validation for file "+filePath+" begins");

        File xlsxFile = fileUtil.getFileByPath(filePath);

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        processExcelFileResult.setFileName(filePath);

        FileInputStream fileInput = new FileInputStream(xlsxFile);

        try {
            Workbook workbook = new XSSFWorkbook(fileInput);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowsIterator = sheet.rowIterator();

            //está eliminando la linea de cabeceras que por ahora quiero ver:
            if (rowsIterator.hasNext())
                rowsIterator.next();

            while (rowsIterator.hasNext()) {
                processRow(rowsIterator.next(), rowsIterator.hasNext(), processExcelFileResult, createCredentials,
                        skipIdentityCredentials, pdfValidation);
            }
            return processExcelFileResult;
        } catch (NotOfficeXmlFileException c) {
            log.error("Invalid file format: " + filePath);
            processExcelFileResult.addRowError(ExcelErrorDetail.builder()
                    .errorHeader("Error en el archivo")
                    .errorBody("Por favor, verificá que el formato del archivo sea correcto.")
                    .errorType(ExcelErrorType.OTHER)
                    .build()
            );
            return processExcelFileResult;
        } finally {
            fileInput.close();
        }
    }


    public abstract ProcessExcelFileResult processRow(Row currentRow, boolean hasNext,
                                                      ProcessExcelFileResult processExcelFileResult,
                                                      boolean createCredentials,
                                                      boolean skipIdentityCredentials,
                                                      boolean pdfValidation) throws Exception;

}
