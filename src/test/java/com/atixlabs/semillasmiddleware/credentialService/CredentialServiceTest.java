package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;

import io.restassured.internal.assertion.Assertion;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CredentialServiceTest {

    @Mock
    CredentialRepository credentialRepository;

    @Autowired
    DateUtil util;

    private Person getBeneficiaryMock(){
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber(29302594L);
        person.setName("Pepito");
        return person;
    }
    private List<Credential> credentialsMock(){
        List<Credential> credentials = new ArrayList<>();

        Person beneficiary = getBeneficiaryMock();

        CredentialCredit credential1 = new CredentialCredit();
        credential1.setId(1L);
        credential1.setIdDidiCredential(2L);
        credential1.setDateOfIssue(LocalDateTime.now());
        credential1.setDateOfExpiry(LocalDateTime.now().plusDays(1));
        credential1.setDniBeneficiary(29302594L);
        credential1.setCreditState("Estado");
        credential1.setBeneficiary(beneficiary);
        credential1.setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        credentials.add(credential1);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setId(2L);
        credentialIdentity.setDniCreditHolder(34534534L);
        credentialIdentity.setNameBeneficiary("Pepito");
        credentialIdentity.setDateOfExpiry(util.getLocalDateTimeNow());
        credentialIdentity.setDateOfIssue(util.getLocalDateTimeNow().minusDays(1));
        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        credentials.add(credentialIdentity);



        return credentials;
    }

    @Test
    public void getActiveCredentials() {
        when(credentialRepository.findAllByCredentialState("Vigente")).thenReturn((List<Credential>) credentialsMock());

        List<Credential> credentials = credentialRepository.findAllByCredentialState("Vigente");

        verify(credentialRepository).findAllByCredentialState("Vigente");

        List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
        log.info("credenciales " +credentialsDto.toString());


        Assertions.assertTrue(credentialsDto.size() > 0);
        Assertions.assertEquals(credentialsMock().get(0).getId() ,credentialsDto.get(0).getId());
        Assertions.assertEquals(credentialsMock().get(0).getCredentialState(), credentialsDto.get(0).getCredentialState());
        //Assertions.assertEquals(credentialsMock().get(0).getDniBeneficiary() ,credentialsDto.get(0).getDniBeneficiary());
        Assertions.assertEquals(credentialsMock().get(0).getIdDidiCredential() ,credentialsDto.get(0).getIdDidiCredential());
        Assertions.assertTrue(credentialsDto.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentialsDto.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsMock().get(0).getBeneficiary().getName() ,credentialsDto.get(0).getName());
    }

}
