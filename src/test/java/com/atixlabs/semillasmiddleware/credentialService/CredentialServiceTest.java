package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.bondareaService.BondareaServiceTest;
import com.atixlabs.semillasmiddleware.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CredentialServiceTest {


    @InjectMocks
    private CredentialService credentialService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private CredentialCreditRepository credentialCreditRepository;

    @Mock
    private LoanRepository loanRepository;

    @Captor
    private ArgumentCaptor<CredentialCredit> creditCaptor;

    @Captor
    private ArgumentCaptor<Loan> loanCaptor;


    @Before
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }


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
        //credential1.setDateOfExpiry(LocalDateTime.now().plusDays(14));
        credential1.setDniBeneficiary(29302594L);
        credential1.setCreditState("Estado");
        credential1.setBeneficiary(beneficiary);
        credential1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        credentials.add(credential1);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setId(2L);
        credentialIdentity.setDniCreditHolder(34534534L);
        credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
        credentialIdentity.setNameBeneficiary("Pepito");
        //credentialIdentity.setDateOfExpiry(util.getLocalDateTimeNow());
        credentialIdentity.setDateOfIssue(DateUtil.getLocalDateTimeNow().minusDays(14));
        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        credentials.add(credentialIdentity);

        CredentialIdentity credentialIdentity2 = new CredentialIdentity();
        credentialIdentity2.setId(3L);
        credentialIdentity2.setDniCreditHolder(34534534L);
        credentialIdentity2.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
        credentialIdentity2.setNameBeneficiary("Pepito");
        //credentialIdentity2.setDateOfExpiry(util.getLocalDateTimeNow());
        credentialIdentity2.setDateOfIssue(DateUtil.getLocalDateTimeNow().minusDays(14));
        credentialIdentity2.setBeneficiary(beneficiary);
        credentialIdentity2.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        credentials.add(credentialIdentity2);



        return credentials;
    }

    private Optional<DIDHisotoric> getDIDHistoricMock(){
        DIDHisotoric didi = new DIDHisotoric();

    }

    private Optional<Person> getPersonMock(){
        Person person = new Person();
        person.setDocumentNumber((long) 12345);
        person.setName("Pepito");
        person.setIdDidi((long) 1);
        return Optional.of(person);
    }

    private List<Credential> credentialsFilteredActiveMock(){
       return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode())).collect(Collectors.toList());
    }

    private List<Credential> credentialsFilteredRevokedMock(){
        return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode())).collect(Collectors.toList());
    }




    @Test
    public void getActiveCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null, Arrays.asList("Vigente"), null)).thenReturn((List<Credential>) credentialsFilteredActiveMock());

        List<Credential> credentials = credentialService.findCredentials(null,null,null, null, null, null, Arrays.asList("Vigente"), null);

        verify(credentialRepository).findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Vigente"), null);

        //List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
        log.info("credenciales " +credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredActiveMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getId() ,credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getBeneficiary().getDocumentNumber() ,credentials.get(0).getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getIdDidiCredential() ,credentials.get(0).getIdDidiCredential());
        //Assertions.assertTrue(credentials.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getBeneficiary().getName() ,credentials.get(0).getBeneficiary().getName());
    }


    @Test
    public void getRevokedCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null, Arrays.asList("Revocada"), null)).thenReturn((List<Credential>) credentialsFilteredRevokedMock());

        List<Credential> credentials = credentialService.findCredentials(null,null,null, null, null, null, Arrays.asList("Revocada"), null);

        verify(credentialRepository).findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Revocada"), null);

        log.info("credenciales " +credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredRevokedMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getId() ,credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getBeneficiary().getDocumentNumber() ,credentials.get(0).getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getIdDidiCredential() ,credentials.get(0).getIdDidiCredential());
        //Assertions.assertTrue(credentials.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getBeneficiary().getName() ,credentials.get(0).getBeneficiary().getName());
    }


    @Test
    public void createCredentialCreditWithActiveDID(){
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMock());

        idHistoricRepository.findByIdPersonAndIsActive(anyLong(), true);

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository,times(1)).save(creditCaptor.capture());
        verify(loanRepository,times(1)).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = creditCaptor.getValue();

        log.info("credit created " +creditSaved.toString());

        Assertions.assertEquals(true ,savedLoan.getHasCredential());
        Assertions.assertEquals(loan.getDniPerson() ,creditSaved.getDniBeneficiary());
        Assertions.assertEquals("Vigente" ,creditSaved.getCredentialState().getStateName());
        Assertions.assertEquals(loan.getStatusDescription(),creditSaved.getCreditState());



    }

    @Test
    public void createCredentialCreditWithNOActiveDID(){

        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMock());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository,times(1)).save(creditCaptor.capture());
        verify(loanRepository,times(1)).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = creditCaptor.getValue();

        log.info("credit created " +creditSaved.toString());

        Assertions.assertEquals(true ,savedLoan.getHasCredential());
        Assertions.assertEquals(loan.getDniPerson() ,creditSaved.getDniBeneficiary());
        Assertions.assertEquals("Vigente" ,creditSaved.getCredentialState().getStateName());
        Assertions.assertEquals(loan.getStatusDescription(),creditSaved.getCreditState());
}
