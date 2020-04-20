package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepositoryCustom;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CredentialServiceTest {

    @Mock
    private CredentialRepository credentialRepository;

    @InjectMocks
    private CredentialService credentialService;

    @Autowired
    private DateUtil util;

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
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credential1.setDateOfIssue(LocalDateTime.now());
        credential1.setDateOfExpiry(LocalDateTime.now().plusDays(1));
        credential1.setBeneficiaryDocumentNumber(29302594L);
        credential1.setCreditState("Estado");
        credential1.setBeneficiary(beneficiary);
        credential1.setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        credentials.add(credential1);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setId(2L);
        credentialIdentity.setDniCreditHolder(34534534L);
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
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
        when(credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null, Arrays.asList("Vigente"))).thenReturn((List<Credential>) credentialsMock());

        List<Credential> credentials = credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Vigente"));

        verify(credentialRepository,times(1)).findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Vigente"));

        //List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
        log.info("credenciales " +credentials.toString());


        Assertions.assertTrue(credentials.size() > 0);
        Assertions.assertEquals(credentialsMock().get(0).getId() ,credentials.get(0).getId());
        Assertions.assertEquals(credentialsMock().get(0).getCredentialState(), credentials.get(0).getCredentialState());
        Assertions.assertEquals(credentialsMock().get(0).getBeneficiary().getDocumentNumber() ,credentials.get(0).getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(credentialsMock().get(0).getIdDidiCredential() ,credentials.get(0).getIdDidiCredential());
        Assertions.assertTrue(credentials.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsMock().get(0).getBeneficiary().getName() ,credentials.get(0).getBeneficiary().getName());
    }

}
