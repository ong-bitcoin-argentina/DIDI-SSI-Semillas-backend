package com.atixlabs.semillasmiddleware.filemanager.service;

import com.atixlabs.semillasmiddleware.filemanager.configuration.FileManagerConfigurationProperties;
import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import com.atixlabs.semillasmiddleware.pdfparser.surveyPdfParser.service.PdfParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileManagerService {

    private FileManagerConfigurationProperties fileManagerConfigurationProperties;
    private PdfParserService pdfParserService;
    public static final String tmpPath = System.getProperty("java.io.tmpdir");

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

    public File getFileFromTmp(String fileName){
        return new File(tmpPath + "/" + fileName);
    }

    private static void writeFileToZip(File file, ZipOutputStream zipStream)
            throws IOException {

        log.info("add file to zip, file name ["+file.getName()+"]");
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipStream.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }

        zipStream.closeEntry();
        fis.close();
        log.info("file added.");
    }

    public String zipAll(List<String> fileNames, String zipSuffix){
        log.info("Starting to zip "+ fileNames.toString());
        File zip;
        try {
            zip = File.createTempFile(zipSuffix, ".zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
            for (String fileName : fileNames){
                this.writeFileToZip(this.getFileFromTmp(fileName), out);
            }
            out.close();
            log.info("Files zipped correctly, location ["+zip.getAbsolutePath()+"]");
            return zip.getName();
        }catch (Exception ex){
            log.error("There has been an error zipping files msg["+ex.getMessage()+"]");
            return "";
        }
    }


}
