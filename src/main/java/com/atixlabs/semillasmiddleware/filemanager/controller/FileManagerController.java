package com.atixlabs.semillasmiddleware.filemanager.controller;

import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.excelparser.app.service.KoboSurveyExcelParseService;
import com.atixlabs.semillasmiddleware.excelparser.app.service.SancorSaludExcelParseService;
import com.atixlabs.semillasmiddleware.excelparser.app.service.SurveyExcelParseService;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.filemanager.exception.EmptyFileException;
import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import com.atixlabs.semillasmiddleware.filemanager.service.FileManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileManagerController {

    @Autowired
    private FileManagerService fileManagerService;

    @Autowired
    private SurveyExcelParseService surveyExcelParseService;

    @Autowired
    private KoboSurveyExcelParseService koboSurveyExcelParseService;

    @Autowired
    private SancorSaludExcelParseService sancorSaludExcelParseService;


    @PostMapping("/reportPDF")
    @ResponseBody
    public ResponseEntity <ProcessExcelFileResult> reportPDF(
            @RequestParam("file") MultipartFile file)
        throws Exception, CredentialException {
        log.info("report pdf executed");
        if (file.isEmpty()) {
            throw new EmptyFileException(Constants.EMPTY_FILE);
        }

        File receivedFile = fileManagerService.uploadFile(file);
        ProcessExcelFileResult processExcelFileResult;

        try{
            processExcelFileResult = koboSurveyExcelParseService.processSheetFileFormKobo(receivedFile.getPath());
        }catch (EmptyFileException ex){
            log.error(Constants.UPLOAD_ERROR.concat(Constants.ERROR), ex.getMessage());
            throw new FileManagerException(Constants.UPLOAD_ERROR);
        }finally {
            Files.deleteIfExists(receivedFile.toPath());
        }

        return ResponseEntity.ok(processExcelFileResult);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<ProcessExcelFileResult> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "true") boolean createCredentials,
            @RequestParam(required = false, defaultValue = "true") boolean skipIdentityCredentials,
            @RequestParam(required = false, defaultValue = "false") boolean pdfValidation)
            throws Exception{

        log.info("uploadFile executed");
        if (file.isEmpty()) {
            throw new EmptyFileException(Constants.EMPTY_FILE);
        }

        File receivedFile = null;
        ProcessExcelFileResult processExcelFileResult = null;

        try{
            receivedFile = fileManagerService.uploadFile(file);

            surveyExcelParseService.clearFormRelatedVariables();
            processExcelFileResult = surveyExcelParseService.processSingleSheetFile(
                    receivedFile.getPath(), createCredentials, skipIdentityCredentials, pdfValidation);

        }catch (EmptyFileException ex){
            log.error(Constants.UPLOAD_ERROR.concat(Constants.ERROR) , ex.getMessage());
            throw new FileManagerException(Constants.UPLOAD_ERROR);
        }finally{
            if(receivedFile != null && receivedFile.exists()) receivedFile.deleteOnExit();
        }

        return ResponseEntity.ok(processExcelFileResult);
    }

    @GetMapping("/download")
    public @ResponseBody byte[] downloadDocument(@RequestParam String fileName) throws IOException {
        File file = fileManagerService.getFileFromTmp(fileName);
        try(FileInputStream fis = new FileInputStream(file)){
            return IOUtils.toByteArray(fis);
        }catch (IOException ex){
            log.error(Constants.DOWNLOAD_ERROR.concat(Constants.ERROR), ex.getMessage());
            throw new IOException(Constants.DOWNLOAD_ERROR);
        }finally {
            Files.deleteIfExists(file.toPath());
        }

    }


    @PostMapping("/sancorsalud/validate")
    @ResponseBody
    public ResponseEntity<ProcessExcelFileResult> validateSancorSaludFile(@RequestParam("file") MultipartFile file) throws Exception {

        log.info("sancorsalud uploadFile executed");

        if (file.isEmpty()) {
            throw new EmptyFileException(Constants.EMPTY_FILE);
        }

        File receivedFile = fileManagerService.uploadFile(file);

        ProcessExcelFileResult processExcelFileResult = sancorSaludExcelParseService
                .processSingleSheetFile(receivedFile.getPath(), true, false,
                        false);

        return ResponseEntity.ok(processExcelFileResult);

    }

    @PostMapping("/sancorsalud/upload")
    @ResponseBody
    public ResponseEntity <ProcessExcelFileResult>uploadSancorSaludFile(@RequestParam("file") MultipartFile file) throws Exception {

        log.info("sancorsalud uploadFile executed");

        if (file.isEmpty()) {
            throw new EmptyFileException(Constants.EMPTY_FILE);
        }

        File receivedFile = fileManagerService.uploadFile(file);

        ProcessExcelFileResult processExcelFileResult = sancorSaludExcelParseService.processAndSaveSingleSheetFile(receivedFile.getPath());

        return ResponseEntity.ok(processExcelFileResult);

    }

    private static class Constants{
        public static final String EMPTY_FILE = "Empty File";
        public static final String DOWNLOAD_ERROR = "Error al obtener el archivo descargable.";
        public static final String UPLOAD_ERROR = "Hubo en error en la carga del archivo.";
        public static final String ERROR = "Error: {}";
    }

}
