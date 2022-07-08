package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.repository.CertTemplateRepository;
import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CertTemplateService {

    private CertTemplateRepository certTemplateRepository;

    @Value("${didi.semillas.template_code_identity}")
    private String didiTemplateCodeIdentity;

    @Value("${didi.semillas.template_code_entrepreneurship}")
    private String didiTemplateCodeEntrepreneurship;

    @Value("${didi.semillas.template_code_dwelling}")
    private String didiTemplateCodeDwelling;

    @Value("${didi.semillas.template_code_benefit}")
    private String didiTemplateCodeBenefit;

    @Value("${didi.semillas.template_code_credit}")
    private String didiTemplateCodeCredit;

    public CertTemplateService(CertTemplateRepository certTemplateRepository){
        this.certTemplateRepository = certTemplateRepository;
    }

    public String getCertTemplateDescription(CredentialCategoriesCodes credentialCategoriesCodes){
        Optional<CertTemplate> certTemplate = certTemplateRepository.findByCredentialCategoriesCodes(credentialCategoriesCodes);
        if(certTemplate.isPresent()){
            return certTemplate.get().getTemplateDescription();
        }else{
            log.error("Cant't found template for {}",credentialCategoriesCodes.getCode());
            return this.getDefaultTemplateDescription(credentialCategoriesCodes);
        }
    }

    public String getDefaultTemplateDescription(CredentialCategoriesCodes credentialCategoriesCodes){

        switch (credentialCategoriesCodes){
            case CREDIT:
                return "Semillas Crediticia";
            case BENEFIT:
                return "Semillas Beneficio";
            case IDENTITY:
                return "Semillas Identidad";
            case DWELLING:
                return "Semillas Vivienda";
            case ENTREPRENEURSHIP:
                return "Semillas Emprendimiento";
            default:
                return "";

        }

    }

    public String getCertTemplateCode(CredentialCategoriesCodes credentialCategoriesCodes){
        log.debug("Geting template code for credential {}",credentialCategoriesCodes.getCode());
        Optional<CertTemplate> certTemplate = certTemplateRepository.findByCredentialCategoriesCodes(credentialCategoriesCodes);
        if(certTemplate.isPresent()){
            return certTemplate.get().getTemplateCode();
        }else{
            log.error("Cant't found template for {}",credentialCategoriesCodes.getCode());
            return this.getDefaultTemplateCode(credentialCategoriesCodes);
        }
    }

    public String getDefaultTemplateCode(CredentialCategoriesCodes credentialCategoriesCodes){

        switch (credentialCategoriesCodes){
            case CREDIT:
                return didiTemplateCodeCredit;
            case BENEFIT:
                return didiTemplateCodeBenefit;
            case IDENTITY:
                return didiTemplateCodeIdentity;
            case DWELLING:
                return didiTemplateCodeDwelling;
            case ENTREPRENEURSHIP:
                return didiTemplateCodeEntrepreneurship;
            default:
                return "";

        }

    }

    public List<CertTemplate> getAllTemplates(){
        return certTemplateRepository.findAll();
    }
}
