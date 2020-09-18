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

    public static String getTemplate(String templateName){
        Resource resource = new DefaultResourceLoader().getResource(TEMPLATES_ROUTE+templateName);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(bdata, StandardCharsets.UTF_8);
            log.info("Template ["+templateName+"] read correctly");
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
