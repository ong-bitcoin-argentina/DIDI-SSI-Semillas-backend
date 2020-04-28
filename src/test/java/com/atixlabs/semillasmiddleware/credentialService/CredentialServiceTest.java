package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
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

    @Mock
    private DIDHistoricRepository didHistoricRepository;

    @Mock
    private CredentialBenefitsRepository credentialBenefitsRepository;

    @Mock
    private CredentialStateRepository credentialStateRepository;

    @Captor
    private ArgumentCaptor<CredentialCredit> credentialCreditCaptor;

    @Captor
    private ArgumentCaptor<CredentialBenefits> credentialBenefitCaptor;

    @Captor
    private ArgumentCaptor<Loan> loanCaptor;


    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }


    private Person getBeneficiaryMock() {
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber(29302594L);
        person.setName("Pepito");
        return person;
    }

    private Optional<CredentialBenefits> getCredentialBenefitMock(){
        CredentialBenefits benefits = new CredentialBenefits();
        benefits.setId(1L);
        benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
        return  Optional.of(benefits);
    }

    private List<Credential> credentialsMock() {
        List<Credential> credentials = new ArrayList<>();

        Person beneficiary = getBeneficiaryMock();


        CredentialCredit credential1 = new CredentialCredit();
        credential1.setId(1L);
        credential1.setIdDidiCredential(2L);
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credential1.setDateOfIssue(LocalDateTime.now());
        //credential1.setDateOfExpiry(LocalDateTime.now().plusDays(14));
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

    private DIDHisotoric getDIDHistoricMock() {
        DIDHisotoric didi = new DIDHisotoric();
        didi.setId(1L);
        didi.setActive(true);
        didi.setIdDidiReceptor(1234L);
        didi.setIdPerson(1245L);
        return didi;
    }

    private Optional<Person> getPersonMock() {
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber((long) 123456);
        person.setName("Pepito");
        person.setDIDIsHisotoric(List.of(getDIDHistoricMock()));
        return Optional.of(person);
    }

    private Optional<CredentialState> getCredentialActiveState(){
        CredentialState state = new CredentialState();
        state.setId(1L);
        state.setStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        return Optional.of(state);
    }

    private Optional<CredentialState> getCredentialPendingState(){
        CredentialState state = new CredentialState();
        state.setId(1L);
        state.setStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
        return Optional.of(state);
    }
    private List<Credential> credentialsFilteredActiveMock() {
        return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode())).collect(Collectors.toList());
    }

    private List<Credential> credentialsFilteredRevokedMock() {
        return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode())).collect(Collectors.toList());
    }


    @Test
    public void getActiveCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null, null, null, null, null, null, Arrays.asList("Vigente"))).thenReturn((List<Credential>) credentialsFilteredActiveMock());

        List<Credential> credentials = credentialService.findCredentials(null, null, null, null, null, null, Arrays.asList("Vigente"));

        verify(credentialRepository).findCredentialsWithFilter(null, null, null, null, null, null, Arrays.asList("Vigente"));

        //List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
        log.info("credenciales " + credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredActiveMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getId(), credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getBeneficiary().getDocumentNumber(), credentials.get(0).getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getIdDidiCredential(), credentials.get(0).getIdDidiCredential());
        //Assertions.assertTrue(credentials.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getBeneficiary().getName(), credentials.get(0).getBeneficiary().getName());
    }


    @Test
    public void getRevokedCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null, null, null, null, null, null, Arrays.asList("Revocada"))).thenReturn((List<Credential>) credentialsFilteredRevokedMock());

        List<Credential> credentials = credentialService.findCredentials(null, null, null, null, null, null, Arrays.asList("Revocada"));

        verify(credentialRepository).findCredentialsWithFilter(null, null, null, null, null, null, Arrays.asList("Revocada"));

        log.info("credenciales " + credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredRevokedMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getId(), credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getBeneficiary().getDocumentNumber(), credentials.get(0).getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getIdDidiCredential(), credentials.get(0).getIdDidiCredential());
        //Assertions.assertTrue(credentials.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getBeneficiary().getName(), credentials.get(0).getBeneficiary().getName());
    }


    @Test
    public void createCredentialCreditAndBenefitWithActiveDID() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.empty());
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMock());
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.of(getDIDHistoricMock()));
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialActiveState());
        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(Optional.empty());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(1)).save(credentialCreditCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(1)).save(credentialBenefitCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = credentialCreditCaptor.getValue();
        CredentialBenefits credentialBenefits = credentialBenefitCaptor.getValue();

        log.info("credit created " + creditSaved.toString());
        log.info("beneficiary created " + credentialBenefits.toString());

        Assertions.assertEquals(true, savedLoan.getHasCredential());
        Assertions.assertEquals(loan.getIdBondareaLoan(), creditSaved.getIdBondareaCredit());
        Assertions.assertEquals(loan.getDniPerson(), creditSaved.getDniBeneficiary());
        Assertions.assertEquals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), creditSaved.getCredentialState().getStateName());
        Assertions.assertEquals(0, creditSaved.getAmountExpiredCycles());
        Assertions.assertEquals(loan.getCreationDate(), creditSaved.getCreationDate());
        Assertions.assertNotNull(creditSaved.getDateOfIssue());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), creditSaved.getIdDidiCredential());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), creditSaved.getIdDidiReceptor());
        Assertions.assertEquals(loan.getStatusDescription(), creditSaved.getCreditState());

        //benefit
        Assertions.assertEquals(PersonTypesCodes.HOLDER.getCode(), credentialBenefits.getBeneficiaryType());
        Assertions.assertEquals(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode(), credentialBenefits.getCredentialDescription());
        Assertions.assertEquals(getPersonMock().get().getDocumentNumber(), credentialBenefits.getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), credentialBenefits.getIdDidiCredential());
        Assertions.assertEquals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialBenefits.getCredentialState().getStateName());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), credentialBenefits.getIdDidiReceptor());
    }

    @Test
    public void createCredentialCreditAndBenefitWithPendingDIDIState() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.empty());
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMock());
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.empty());
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialPendingState());
        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(Optional.empty());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(1)).save(credentialCreditCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(1)).save(credentialBenefitCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = credentialCreditCaptor.getValue();
        CredentialBenefits credentialBenefits = credentialBenefitCaptor.getValue();

        log.info("credit created " + creditSaved.toString());
        log.info("beneficiary created " + credentialBenefits.toString());

        Assertions.assertEquals(true, savedLoan.getHasCredential());
        Assertions.assertEquals(loan.getIdBondareaLoan(), creditSaved.getIdBondareaCredit());
        Assertions.assertEquals(loan.getDniPerson(), creditSaved.getDniBeneficiary());
        Assertions.assertEquals(CredentialStatesCodes.PENDING_DIDI.getCode(), creditSaved.getCredentialState().getStateName());
        Assertions.assertEquals(0, creditSaved.getAmountExpiredCycles());
        Assertions.assertEquals(loan.getCreationDate(), creditSaved.getCreationDate());
        Assertions.assertNotNull(creditSaved.getDateOfIssue());
        Assertions.assertEquals(null, creditSaved.getIdDidiCredential());
        Assertions.assertEquals(null, creditSaved.getIdDidiReceptor());
        Assertions.assertEquals(loan.getStatusDescription(), creditSaved.getCreditState());

        //benefit
        Assertions.assertEquals(PersonTypesCodes.HOLDER.getCode(), credentialBenefits.getBeneficiaryType());
        Assertions.assertEquals(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode(), credentialBenefits.getCredentialDescription());
        Assertions.assertEquals(getPersonMock().get().getDocumentNumber(), credentialBenefits.getDniBeneficiary());
        Assertions.assertEquals(null, credentialBenefits.getIdDidiCredential());
        Assertions.assertEquals(null, credentialBenefits.getIdDidiReceptor());
        Assertions.assertEquals(CredentialStatesCodes.PENDING_DIDI.getCode(), credentialBenefits.getCredentialState().getStateName());
    }

    @Test
    public void createCredentialCreditAndHavingBenefitExistence() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.empty());
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMock());
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.of(getDIDHistoricMock()));
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialActiveState());
        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(getCredentialBenefitMock());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(1)).save(credentialCreditCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(0)).save(credentialBenefitCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = credentialCreditCaptor.getValue();

        log.info("credit created " + creditSaved.toString());

        Assertions.assertEquals(true, savedLoan.getHasCredential());
        Assertions.assertEquals(loan.getIdBondareaLoan(), creditSaved.getIdBondareaCredit());
        Assertions.assertEquals(loan.getDniPerson(), creditSaved.getDniBeneficiary());
        Assertions.assertEquals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), creditSaved.getCredentialState().getStateName());
        Assertions.assertEquals(0, creditSaved.getAmountExpiredCycles());
        Assertions.assertEquals(loan.getCreationDate(), creditSaved.getCreationDate());
        Assertions.assertNotNull(creditSaved.getDateOfIssue());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), creditSaved.getIdDidiCredential());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), creditSaved.getIdDidiReceptor());
        Assertions.assertEquals(loan.getStatusDescription(), creditSaved.getCreditState());
    }
}
