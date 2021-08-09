package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.service.CertTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(TemplateController.URL_BASE)
@CrossOrigin(origins = "*")
@Slf4j
public class TemplateController {

    public static final String URL_BASE = "/diditemplate";

    private CertTemplateService certTemplateService;

    private DidiService didiService;

    @Autowired
    public TemplateController(CertTemplateService certTemplateService, DidiService didiService){

        this.certTemplateService = certTemplateService;
        this.didiService = didiService;

    }



    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> verifyTemplates(){
        Map<String, String> jsonMessage = new HashMap<>();

        List<CertTemplate> templates = certTemplateService.getAllTemplates();

        if(templates==null || templates.isEmpty()){
            jsonMessage.put("message", "no exiaten templates para verificar");
        }else{
            for(CertTemplate certTemplate : templates){
                Boolean result = didiService.isTemplateRegistered(certTemplate);
                jsonMessage.put(certTemplate.getTemplateDescription(),(result ? "OK":"FAIL"));
            }
        }

        return jsonMessage;
    }
}
