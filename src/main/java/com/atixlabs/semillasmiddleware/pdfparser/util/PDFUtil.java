package com.atixlabs.semillasmiddleware.pdfparser.util;

import com.atixlabs.semillasmiddleware.pdfparser.exception.PdfCannotBeCreatedException;
import com.atixlabs.semillasmiddleware.util.EmailTemplatesUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

public class PDFUtil {

    public static String createTemporaryPdf(String prefixName, String html) throws PdfCannotBeCreatedException{
        File file;
        try{
            file = File.createTempFile(prefixName, ".pdf");
            writePdfFromHtml(file, html);
        }catch (DocumentException | IOException ex ){
            throw new PdfCannotBeCreatedException(ex.getMessage());
        }
        return file.getName();
    }


    private static void writePdfFromHtml(File file, String html) throws DocumentException, IOException{
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(file));
        document.open();

        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(html.getBytes()));

        document.close();
    }

}
