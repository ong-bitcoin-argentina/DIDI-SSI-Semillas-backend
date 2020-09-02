package com.atixlabs.semillasmiddleware.filemanager.service;

import com.atixlabs.semillasmiddleware.filemanager.configuration.FileManagerConfigurationProperties;
import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import com.atixlabs.semillasmiddleware.pdfparser.surveyPdfParser.service.PdfParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
@Slf4j
public class FileManagerService {

    private FileManagerConfigurationProperties fileManagerConfigurationProperties;
    private PdfParserService pdfParserService;

    public FileManagerService(FileManagerConfigurationProperties fileManagerConfigurationProperties, PdfParserService pdfParserService){
        this.fileManagerConfigurationProperties = fileManagerConfigurationProperties;
        this.pdfParserService = pdfParserService;
    }

    public File getWorkFolder() throws FileManagerException, IOException {
        File workFolder = new File(fileManagerConfigurationProperties.getWorkPathDirectory());

        if (!workFolder.exists())
            workFolder.mkdirs();
        if (!workFolder.exists())
            throw new FileManagerException(String.format("Folder %s not exist.", fileManagerConfigurationProperties.getWorkPathDirectory()));

        return workFolder;
    }

    //TODO do unique name file
    public File uploadFile(MultipartFile file) throws FileManagerException {
        try {
            File workMainFolder = getWorkFolder();
            File newFile = new File(workMainFolder.getAbsolutePath() + "/" + file.getOriginalFilename());
            if (!newFile.exists())
                newFile.createNewFile();
            file.transferTo(newFile);
            return newFile;
        } catch (IOException e) {
            log.error("Error creating file", e);
            throw new FileManagerException("Error creating file.");
        }
    }

    public File getPdfFromTmp(String fileName){
        String tmpPath = System.getProperty("java.io.tmpdir");
        return new File(tmpPath + "/" + fileName);
    }

}
