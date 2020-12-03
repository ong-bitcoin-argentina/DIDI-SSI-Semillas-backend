package com.atixlabs.semillasmiddleware.pdfparser.util;

import com.atixlabs.semillasmiddleware.pdfparser.exception.PdfCannotBeCreatedException;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.DocumentException;
import java.io.*;


public class PDFUtil {

    public static String createTemporaryPdf(String prefixName, String html){
        File file;
        try{
            file = File.createTempFile(prefixName, ".pdf");
            writePdfFromHtml(file, html);
        }catch (DocumentException | IOException ex ){
            throw new PdfCannotBeCreatedException(ex.getMessage());
        }
        return file.getName();
    }

    private static void writePdfFromHtml(File file, String html) throws DocumentException, IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), new FileOutputStream(file), converterProperties);
    }

}
