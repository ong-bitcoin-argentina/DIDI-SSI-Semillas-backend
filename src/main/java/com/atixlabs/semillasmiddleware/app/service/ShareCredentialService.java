package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.exceptions.PersonDoesNotExistsException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialFilterDto;
import com.atixlabs.semillasmiddleware.app.model.credential.ShareCredentialRequest;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentProviderException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.service.ProviderService;
import com.atixlabs.semillasmiddleware.app.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ShareCredentialService {

    @Autowired
    public ShareCredentialService(ProviderService providerService,
                                  MailService mailService,
                                  PersonService personService,
                                  CredentialService credentialService){
        this.providerService = providerService;
        this.mailService = mailService;
        this.personService = personService;
        this.credentialService = credentialService;
    }

    private static final String TEMPLATE_NAME = "share_credentials_template.html";
    private static final String PROVIDER_NAME_PARAM ="{providerName}";
    private static final String BENEFICIARY_NAME_PARAM ="{name}";
    private static final String BENEFICIARY_LASTNAME_PARAM ="{lastname}";
    private static final String BENEFICIARY_DNI_PARAM ="{dni}";
    private static final String BENEFICIARY_PHONE_PARAM ="{phone}";
    private static final String BENEFICIARY_EMAIL_PARAM ="{email}";
    private static final String BENEFICIARY_BIRTHDATE_PARAM ="{birthdate}";
    private static final String BENEFICIARY_CHARACTER_PARAM ="{character}";
    private static final String OWNER_NAME_PARAM ="{nameOwner}";
    private static final String OWNER_LASTNAME_PARAM ="{lastnameOwner}";

    private static final String FAMILY_BENEFIT_TEXT = "Integrante del grupo familiar";
    private static final String OWN_BENEFIT_TEXT = "Beneficio propio";

    private static final String EMAIL_SUBJECT = "Solicitud de turno - Beneficio Semillas";

    private ProviderService providerService;
    private MailService mailService;
    private PersonService personService;
    private CredentialService credentialService;

    public boolean shareCredential(ShareCredentialRequest credentialRequest){
        try{
            Email email = Email.builder()
                    .subject(EMAIL_SUBJECT)
                    .to(getTo(credentialRequest))
                    .template(getTemplate(credentialRequest))
                    .build();

            mailService.send(email);
            return true;
        }catch (PersonDoesNotExistsException pdnee){
            log.error("Person with dni %s does not exist", credentialRequest.getDni());
        }catch (InexistentProviderException ipe){
            log.error("Provider with id %s does not exist", credentialRequest.getProviderId());
        }catch (IOException ioe){
            log.error("Could not get template, ex: %s", ioe.getMessage());
        }
        return false;
    }

    private String getTo(ShareCredentialRequest credentialRequest){
        return providerService.findById(credentialRequest.getProviderId()).getEmail();
    }


    private String getTemplate(ShareCredentialRequest credentialRequest) throws IOException{

        String html = getHtml();
        return replaceParams(html, credentialRequest);
    }

    private String getHtml() throws IOException {
        Resource resource = new DefaultResourceLoader().getResource("classpath:templates/"+TEMPLATE_NAME);
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        String data = new String(bdata, StandardCharsets.UTF_8);
        log.info("Template read correctly");
        return data;
    }

    private String replaceParams(String html, ShareCredentialRequest credentialRequest ){
        Provider provider = providerService.findById(credentialRequest.getProviderId());
        Person person = personService.findByDocumentNumber(credentialRequest.getDni()).orElseThrow(() -> new PersonDoesNotExistsException(""));

        CredentialFilterDto credentialFilterDto = CredentialFilterDto
                .builder()
                .category(Optional.of(CredentialCategoriesCodes.BENEFIT.getCode()))
                .beneficiaryDni(Optional.of(credentialRequest.getDni()))
                .did(Optional.of(credentialRequest.getDid()))
                .build();

        List<Credential> credentials = credentialService.findAll(credentialFilterDto);
        Credential cred = credentials.stream().findFirst().orElseThrow(RuntimeException::new);

        String character;
        if (!cred.getBeneficiaryDni().equals(cred.getCreditHolderDni())) {
            character = FAMILY_BENEFIT_TEXT;
        }else{
            character = OWN_BENEFIT_TEXT;
        }

        return html.replace(PROVIDER_NAME_PARAM, provider.getName())
                .replace(BENEFICIARY_NAME_PARAM, person.getFirstName())
                .replace(BENEFICIARY_LASTNAME_PARAM, person.getLastName() )
                .replace(BENEFICIARY_DNI_PARAM ,person.getDocumentNumber().toString())
                .replace(BENEFICIARY_BIRTHDATE_PARAM, person.getBirthDate().toString())
                .replace(BENEFICIARY_PHONE_PARAM, credentialRequest.getPhone())
                .replace(BENEFICIARY_EMAIL_PARAM, credentialRequest.getEmail())
                .replace(OWNER_NAME_PARAM, credentials.stream().findFirst().get().getCreditHolderFirstName())
                .replace(OWNER_LASTNAME_PARAM, credentials.stream().findFirst().get().getCreditHolderLastName())
                .replace( BENEFICIARY_CHARACTER_PARAM, character);


    }



}
