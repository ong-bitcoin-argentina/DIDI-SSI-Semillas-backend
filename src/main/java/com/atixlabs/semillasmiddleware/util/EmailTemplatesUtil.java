package com.atixlabs.semillasmiddleware.util;

import com.atixlabs.semillasmiddleware.app.exceptions.EmailNotSentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class EmailTemplatesUtil {

    private static final String TEMPLATES_ROUTE = "classpath:templates/";
    private static final String STYLE_SHEET_ROUTE = "classpath:css/";
    private static final String IMAGES_ROUTE = "classpath:img/";

    public static String getTemplate(String templateName){
        return getFile(templateName, TEMPLATES_ROUTE);
    }

    public static String getStyleSheet(String styleSheetName){
        return getFile(styleSheetName, STYLE_SHEET_ROUTE);
    }

    public static String getImage(String imageName){
        return getFile(imageName, IMAGES_ROUTE);
    }

    private static String getFile(String fileName, String route) {
        Resource resource = new DefaultResourceLoader().getResource(route+fileName);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(bdata, StandardCharsets.UTF_8);
            log.info("Template ["+fileName+"] read correctly");
            return data;
        }catch (IOException ioe){
            throw new EmailNotSentException(ioe.getMessage());
        }
    }

    public static String replaceParams(String html, Map<String, String> parameters ){

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            html = html.replace(entry.getKey(), entry.getValue());
        }

        return html;
    }
}
