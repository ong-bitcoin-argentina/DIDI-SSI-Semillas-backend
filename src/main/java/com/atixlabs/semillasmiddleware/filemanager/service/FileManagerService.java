package com.atixlabs.semillasmiddleware.filemanager.service;

import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileManagerService {

    public FileManagerService(){/* ** */}

    //TODO do unique name file
    public File uploadFile(MultipartFile file) throws FileManagerException {
        try{
            File tmpFile = new File(Constants.TMPPATH.concat(File.separator + file.getOriginalFilename()));
            if (!tmpFile.exists()) createTmpFile(tmpFile, file);
            return tmpFile;
        }catch (Exception ex){
            log.error("Error en la carga del archivo. Error: {}", ex.getMessage());
            throw new FileManagerException("Error creating file.");
        }
    }

    private void createTmpFile(File tmpFile, MultipartFile file) throws IOException{
        if (tmpFile.createNewFile()){
            file.transferTo(tmpFile);
        }else{
            log.error("Error al crear archivo temporal.");
            throw new IOException();
        }
    }

    public File getFileFromTmp(String fileName){
        return new File(Constants.TMPPATH.concat(File.separator + fileName));
    }

    private void writeFileToZip(File file, ZipOutputStream zipStream)
            throws IOException {

        log.info("add file to zip, file name ["+file.getName()+"]");
        try(FileInputStream fis = new FileInputStream(file)){
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipStream.write(bytes, 0, length);
            }
            log.info("file added.");
        }catch (IOException ex){
            log.error("Se produjo un error al armar el archivo .zip. Error: {}", ex.getMessage());
            throw new IOException("Error en la generación del archivo .zip descargable.");
        }finally{
            zipStream.closeEntry();
        }
    }

    public String zipAll(List<String> fileNames, String zipSuffix) throws IOException {
        log.info("Starting to zip "+ fileNames.toString());
        File tmpFile = createTmpZipFile(zipSuffix, ".zip");
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tmpFile))) {
            for (String fileName : fileNames){
                this.writeFileToZip(this.getFileFromTmp(fileName), out);
                Files.deleteIfExists(this.getFileFromTmp(fileName).toPath());
            }
            log.info("Files zipped correctly, location ["+tmpFile.getAbsolutePath()+"]");
            return tmpFile.getName();
        }catch (Exception ex){
            log.error("There has been an error zipping files msg["+ex.getMessage()+"]");
            return "";
        }
    }

    private File createTmpZipFile(String zipSuffix, String fileExtension) throws IOException{
        try{
            return File.createTempFile(zipSuffix, fileExtension, new File(Constants.TMPPATH));
        }catch (IOException ex){
            log.error("Error al generar el archivo temporal. Error: {}", ex.getMessage());
            throw new IOException("Error al momento de generar el archivo.");
        }
    }

    private static class Constants{
        public static final String TMPPATH = System.getProperty("java.io.tmpdir");
    }

}
