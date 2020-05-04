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
import java.util.*;
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


    private Person getBeneficiaryMockWithoutDID() {
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber(123456L);
        person.setName("Pepito");
        return person;
    }

    private Optional<CredentialBenefits> getCredentialHolderBenefitMock(Person beneficiary){
        CredentialBenefits benefits = new CredentialBenefits();
        benefits.setId(1L);
        benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
        benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        benefits.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        benefits.setBeneficiary(beneficiary);
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());
        benefits.setIdDidiCredential(1234L);
        benefits.setIdDidiReceptor(1234L);

        return  Optional.of(benefits);
    }

    private CredentialBenefits getPendingCredentialHolderBenefitMock(Person beneficiary){
        CredentialBenefits benefits = new CredentialBenefits();
        benefits.setId(1L);
        benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
        benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        benefits.setCredentialState(new CredentialState(CredentialStatesCodes.PENDING_DIDI.getCode()));
        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        benefits.setBeneficiary(beneficiary);
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());
        return  benefits;
    }

    private CredentialBenefits getRevokedCredentialHolderBenefitMock(Person beneficiary){
        CredentialBenefits benefits = new CredentialBenefits();
        benefits.setId(1L);
        benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
        benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        benefits.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        benefits.setBeneficiary(beneficiary);
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());
        return  benefits;
    }

    private List<Credential> credentialsMock() {
        List<Credential> credentials = new ArrayList<>();

        Person beneficiary = getBeneficiaryMockWithoutDID();


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

    private CredentialCredit getActiveCreditMock(Loan loan, Person personMock){
        CredentialCredit credential1 = new CredentialCredit();
        credential1.setId(1L);
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credential1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));

        credential1.setIdBondareaCredit(loan.getIdBondareaLoan());
        credential1.setIdGroup(loan.getIdGroup());
        credential1.setCurrentCycle(loan.getCycleDescription());
        credential1.setAmountExpiredCycles(0);
        credential1.setCreditState(loan.getStatusDescription());
        credential1.setExpiredAmount(loan.getExpiredAmount());
        credential1.setCreationDate(loan.getCreationDate());
        credential1.setDniBeneficiary(personMock.getDocumentNumber());
        credential1.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credential1.setBeneficiary(personMock);


        credential1.setIdDidiCredential(1234L);
        credential1.setIdDidiReceptor(1234L);

        credential1.setIdHistorical(1L);

        return credential1;
    }

    private CredentialCredit getPendingCreditMock(Loan loan, Person personMock){
        CredentialCredit credential1 = new CredentialCredit();
        credential1.setId(1L);
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credential1.setCredentialState(new CredentialState(CredentialStatesCodes.PENDING_DIDI.getCode()));

        credential1.setIdBondareaCredit(loan.getIdBondareaLoan());
        credential1.setIdGroup(loan.getIdGroup());
        credential1.setCurrentCycle(loan.getCycleDescription());
        credential1.setAmountExpiredCycles(0);
        credential1.setCreditState(loan.getStatusDescription());
        credential1.setExpiredAmount(loan.getExpiredAmount());
        credential1.setCreationDate(loan.getCreationDate());
        credential1.setDniBeneficiary(personMock.getDocumentNumber());
        credential1.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credential1.setBeneficiary(personMock);

        credential1.setIdHistorical(1L);

        return credential1;
    }

    private DIDHisotoric getDIDHistoricMock() {
        DIDHisotoric didi = new DIDHisotoric();
        didi.setId(1L);
        didi.setActive(true);
        didi.setIdDidiReceptor(1234L);
        didi.setIdPerson(1245L);
        return didi;
    }

    private Optional<Person> getPersonMockWithDid() {
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
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMockWithDid());
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.of(getDIDHistoricMock()));
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialActiveState());
        when(credentialCreditRepository.save(any(CredentialCredit.class))).thenReturn(getActiveCreditMock(BondareaServiceTest.getMockLoan(), getPersonMockWithDid().get()));
        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(Collections.emptyList());
        when(credentialBenefitsRepository.save(any(CredentialBenefits.class))).thenReturn(getCredentialHolderBenefitMock(getPersonMockWithDid().get()).get());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(2)).save(credentialCreditCaptor.capture()); //because the id historic
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(2)).save(credentialBenefitCaptor.capture()); //because the id historic

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = credentialCreditCaptor.getAllValues().get(1);
        CredentialBenefits credentialBenefits = credentialBenefitCaptor.getAllValues().get(1);

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
        Assertions.assertTrue(creditSaved.getIdHistorical() == creditSaved.getId());

        //benefit
        Assertions.assertEquals(PersonTypesCodes.HOLDER.getCode(), credentialBenefits.getBeneficiaryType());
        Assertions.assertEquals(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode(), credentialBenefits.getCredentialDescription());
        Assertions.assertEquals(getPersonMockWithDid().get().getDocumentNumber(), credentialBenefits.getBeneficiary().getDocumentNumber());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), credentialBenefits.getIdDidiCredential());
        Assertions.assertEquals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialBenefits.getCredentialState().getStateName());
        Assertions.assertEquals(getDIDHistoricMock().getIdDidiReceptor(), credentialBenefits.getIdDidiReceptor());
    }

    @Test
    public void createCredentialCreditAndBenefitWithPendingDIDIState() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.empty());
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(Optional.of(getBeneficiaryMockWithoutDID()));
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.empty());
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialPendingState());
        when(credentialCreditRepository.save(any(CredentialCredit.class))).thenReturn(getPendingCreditMock(BondareaServiceTest.getMockLoan(), getBeneficiaryMockWithoutDID()));
        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(Collections.emptyList());
        when(credentialBenefitsRepository.save(any(CredentialBenefits.class))).thenReturn(getPendingCredentialHolderBenefitMock(getPersonMockWithDid().get()));

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(2)).save(credentialCreditCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(2)).save(credentialBenefitCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        CredentialCredit creditSaved = credentialCreditCaptor.getAllValues().get(1);
        CredentialBenefits credentialBenefits = credentialBenefitCaptor.getAllValues().get(1);

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
        Assertions.assertEquals(getPersonMockWithDid().get().getDocumentNumber(), credentialBenefits.getDniBeneficiary());
        Assertions.assertEquals(null, credentialBenefits.getIdDidiCredential());
        Assertions.assertEquals(null, credentialBenefits.getIdDidiReceptor());
        Assertions.assertEquals(CredentialStatesCodes.PENDING_DIDI.getCode(), credentialBenefits.getCredentialState().getStateName());
    }

    @Test
    public void createCredentialCreditAndHavingBenefitsCredential() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.empty());
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMockWithDid());
        when(didHistoricRepository.findByIdPersonAndIsActive(anyLong(), anyBoolean())).thenReturn(Optional.of(getDIDHistoricMock()));
        when(credentialStateRepository.findByStateName(anyString())).thenReturn(getCredentialActiveState());
        when(credentialCreditRepository.save(any(CredentialCredit.class))).thenReturn(getActiveCreditMock(BondareaServiceTest.getMockLoan(), getPersonMockWithDid().get()));

        //credential benefits
        when(credentialBenefitsRepository.findByDniBeneficiary(anyLong())).thenReturn(List.of(getCredentialHolderBenefitMock(getPersonMockWithDid().get()).get()));

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(2)).save(credentialCreditCaptor.capture());
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

    @Test
    public void tryToCreateCredentialCreditButHasAlready() throws Exception {
        when(credentialCreditRepository.findByIdBondareaCredit(anyString())).thenReturn(Optional.of(getActiveCreditMock(BondareaServiceTest.getMockLoan(), getPersonMockWithDid().get())));
        when(personRepository.findByDocumentNumber(anyLong())).thenReturn(getPersonMockWithDid());

        Loan loan = BondareaServiceTest.getMockLoan();
        credentialService.createNewCreditCredentials(loan);

        verify(credentialCreditRepository, times(0)).save(credentialCreditCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(credentialBenefitsRepository, times(0)).save(credentialBenefitCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();

        Assertions.assertEquals(true, savedLoan.getHasCredential());
    }

    //TODO try to create benefit with revoke benefits. Or with familiar


}
