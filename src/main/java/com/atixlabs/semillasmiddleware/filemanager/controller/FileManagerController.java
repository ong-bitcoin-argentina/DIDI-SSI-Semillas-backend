package com.atixlabs.semillasmiddleware.filemanager.controller;

import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.excelparser.app.service.SancorSaludExcelParseService;
import com.atixlabs.semillasmiddleware.excelparser.app.service.SurveyExcelParseService;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.app.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.filemanager.exception.EmptyFileException;
import com.atixlabs.semillasmiddleware.filemanager.service.FileManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileManagerController {

    @Autowired
    private FileManagerService fileManagerService;

    @Autowired
    private SurveyExcelParseService surveyExcelParseService;

    @Autowired
    private SancorSaludExcelParseService sancorSaludExcelParseService;


    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "true") boolean createCredentials,
            @RequestParam(required = false, defaultValue = "true") boolean skipIdentityCredentials) throws Exception, CredentialException {

        log.info("uploadFile executed");

        if (file.isEmpty()) {
            throw new EmptyFileException("Empty file");
        }

        File receivedFile = fileManagerService.uploadFile(file);
        surveyExcelParseService.clearFormRelatedVariables();
        ProcessExcelFileResult processExcelFileResult = surveyExcelParseService.processSingleSheetFile(receivedFile.getPath(), createCredentials, skipIdentityCredentials);

        return ResponseEntity.ok(processExcelFileResult);
    }

    @GetMapping("/download")
    public @ResponseBody byte[] downloadDocument(@RequestParam String fileName) throws IOException {

        InputStream in = new FileInputStream(fileManagerService.getFileFromTmp(fileName));
        return IOUtils.toByteArray(in);
    }


    @PostMapping("/sancorsalud/validate")
    @ResponseBody
    public ResponseEntity validateSancorSaludFile(@RequestParam("file") MultipartFile file) throws Exception, InvalidCategoryException {

        log.info("sancorsalud uploadFile executed");

        if (file.isEmpty()) {
            throw new EmptyFileException("Empty file");
        }

        File receivedFile = fileManagerService.uploadFile(file);

        ProcessExcelFileResult processExcelFileResult = sancorSaludExcelParseService.processSingleSheetFile(receivedFile.getPath(), true, false);

        return ResponseEntity.ok(processExcelFileResult);

    }

    @PostMapping("/sancorsalud/upload")
    @ResponseBody
    public ResponseEntity uploadSancorSaludFile(@RequestParam("file") MultipartFile file) throws Exception, InvalidCategoryException {

        log.info("sancorsalud uploadFile executed");

        if (file.isEmpty()) {
            throw new EmptyFileException("Empty file");
        }

        File receivedFile = fileManagerService.uploadFile(file);

        ProcessExcelFileResult processExcelFileResult = sancorSaludExcelParseService.processAndSaveSingleSheetFile(receivedFile.getPath());

        return ResponseEntity.ok(processExcelFileResult);

    }



}
