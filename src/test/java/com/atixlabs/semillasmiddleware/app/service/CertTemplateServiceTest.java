package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.repository.CertTemplateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CertTemplateServiceTest {

    private final String didiTemplateCodeCredit = "code didiTemplateCodeCredit";

    @Mock
    private CertTemplateRepository certTemplateRepository;

    @InjectMocks
    private CertTemplateService certTemplateService;

    @Before
    public void setupMocks(){

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(certTemplateService, "didiTemplateCodeCredit", didiTemplateCodeCredit);
    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndNotExistInDB_thenReturnDefault(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.CREDIT);

        Assert.assertEquals("Semillas Crediticia", result);

    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndNotExistInDB_thenReturnBenefit(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.BENEFIT);

        Assert.assertEquals("Semillas Beneficio", result);

    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndNotExistInDB_thenReturnIdentity(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.IDENTITY);

        Assert.assertEquals("Semillas Identidad", result);

    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndNotExistInDB_thenReturnDwelling(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.DWELLING);

        Assert.assertEquals("Semillas Vivienda", result);

    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndNotExistInDB_thenReturnEntrepreneurship(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.ENTREPRENEURSHIP);

        Assert.assertEquals("Semillas Emprendimiento", result);

    }

    @Test
    public void whenGetTemplateDescriptionCreditCertAndExistInDB_thenReturnDBVAlue(){

        CertTemplate certTemplate = new CertTemplate();
        certTemplate.setTemplateDescription("CERT DB VALUE TEST");

        when(certTemplateRepository.findByCredentialCategoriesCodes(CredentialCategoriesCodes.CREDIT)).thenReturn(Optional.of(certTemplate));

        String result = certTemplateService.getCertTemplateDescription(CredentialCategoriesCodes.CREDIT);

        Assert.assertEquals("CERT DB VALUE TEST", result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndNotExistInDB_thenReturnDefault(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.CREDIT);

        Assert.assertEquals(didiTemplateCodeCredit, result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndExistInDB_thenReturnDBVAlue(){

        CertTemplate certTemplate = new CertTemplate();
        certTemplate.setTemplateCode("CERT CODE DB VALUE TEST");

        when(certTemplateRepository.findByCredentialCategoriesCodes(CredentialCategoriesCodes.CREDIT))
                .thenReturn(Optional.of(certTemplate));

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.CREDIT);

        Assert.assertEquals("CERT CODE DB VALUE TEST", result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndExistInDB_thenReturnBenefit(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.BENEFIT);

        Assert.assertEquals(null, result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndExistInDB_thenReturnIdentity(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.IDENTITY);

        Assert.assertEquals(null, result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndExistInDB_thenReturnDwelling(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.DWELLING);

        Assert.assertEquals(null, result);

    }

    @Test
    public void whenGetTemplateCodeCreditCertAndExistInDB_thenReturnEntrepreneurship(){

        when(certTemplateRepository.findByCredentialCategoriesCodes(any())).thenReturn(Optional.empty());

        String result = certTemplateService.getCertTemplateCode(CredentialCategoriesCodes.ENTREPRENEURSHIP);

        Assert.assertEquals(null, result);

    }
}
