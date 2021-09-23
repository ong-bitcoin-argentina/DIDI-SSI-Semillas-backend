package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialBenefitSancorRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import com.atixlabs.semillasmiddleware.app.sancor.service.SancorPolicyService;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitSancorService;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitService;
import com.atixlabs.semillasmiddleware.app.service.CredentialStateService;
import com.atixlabs.semillasmiddleware.app.service.PersonService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CredentialBenefitSancorServiceTest {

    @InjectMocks
    private CredentialBenefitSancorService credentialBenefitSancorService;

    @Mock
    private PersonService personService;

    @Mock
    private ParameterConfigurationRepository parameterConfigurationRepository;

    @Mock
    private CredentialStateService credentialStateService;

    @Mock
    private CredentialIdentityRepository credentialIdentityRepository;

    @Mock
    private CredentialBenefitSancorRepository credentialBenefitSancorRepository;

    @Mock
    private SancorPolicyService sancorPolicyService;

    @Captor
    private ArgumentCaptor<CredentialBenefitSancor> credentialBenefitSancorCaptor;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = CredentialException.class)
    public void createNewCrendentialBenefitsForHolderWhenNotExists_LoanToReviewOk() throws CredentialException {

        when(personService.findByDocumentNumber(anyLong())).thenReturn(Optional.empty());

        Loan loan = this.getMockLoan();

        credentialBenefitSancorService.createCredentialsBenefitsHolderForNewLoan(loan);

    }

    @Test
    public void createNewCrendentialBenefitsForHolderWhenIsNotOnDefaultAndCredentialNotExist() throws CredentialException {

        Loan loan = this.getMockLoan();
        Optional<Person> opHolder = this.getPersonMockWithDid();
        Person holder = opHolder.get();
        Optional<CredentialState>  StatePendingDidi = createCredentialStatePendingDidiMock();

        when(credentialStateService.getCredentialPendingDidiState()).thenReturn(StatePendingDidi.get());
        when(parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode())).thenReturn(getParameterConfigurationDidiIssuerMock());
        when(personService.findByDocumentNumber(anyLong())).thenReturn(opHolder);
        when(credentialIdentityRepository.findDistinctBeneficiaryFamilyByHolder(any(Person.class))).thenReturn(null);
        when(credentialBenefitSancorRepository.findTopByCreditHolderDniAndBeneficiaryDniOrderByIdDesc(holder.getDocumentNumber(), holder.getDocumentNumber())).thenReturn(Optional.empty());
        when(credentialBenefitSancorRepository.save(any(CredentialBenefitSancor.class))).thenAnswer(new Answer<CredentialBenefitSancor>() {
            @Override
            public CredentialBenefitSancor answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (CredentialBenefitSancor) args[0];
            }
        });
        when(sancorPolicyService.findByCertificateClientDni(anyLong())).thenReturn(Optional.empty());

        try {
            credentialBenefitSancorService.createCredentialsBenefitsHolderForNewLoan(loan);
        } catch (CredentialException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }
        verify(credentialBenefitSancorRepository, times(2)).save(credentialBenefitSancorCaptor.capture());
        CredentialBenefitSancor credentialBenefits = credentialBenefitSancorCaptor.getValue();

        Assertions.assertEquals(holder, credentialBenefits.getCreditHolder());
        Assertions.assertEquals(holder.getDocumentNumber(), credentialBenefits.getCreditHolderDni());
        Assertions.assertEquals(holder.getFirstName(), credentialBenefits.getCreditHolderFirstName());
        Assertions.assertEquals(holder.getLastName(), credentialBenefits.getCreditHolderLastName());

        Assertions.assertEquals(holder, credentialBenefits.getBeneficiary());
        Assertions.assertEquals(holder.getDocumentNumber(), credentialBenefits.getBeneficiaryDni());
        Assertions.assertEquals(holder.getFirstName(), credentialBenefits.getBeneficiaryFirstName());
        Assertions.assertEquals(holder.getLastName(), credentialBenefits.getBeneficiaryLastName());

        Assertions.assertEquals(StatePendingDidi.get(), credentialBenefits.getCredentialState());

    }

    @Test
    public void createNewCrendentialBenefitsForHolderWhenIsNotOnDefaultAndCredentialExistsAndIsRevoked() throws CredentialException {

        Loan loan = this.getMockLoan();
        Optional<Person> opHolder = this.getPersonMockWithDid();
        Person holder = opHolder.get();
        Optional<CredentialState>  StateRevoke = createCredentialStateRevokeMock();
        CredentialBenefitSancor credentialBenefitsSaved = new CredentialBenefitSancor();
        credentialBenefitsSaved.setCredentialState(StateRevoke.get());
        credentialBenefitsSaved.setIdHistorical(99L);


        Optional<CredentialState>  StatePendingDidi = createCredentialStatePendingDidiMock();
        when(credentialStateService.getCredentialPendingDidiState()).thenReturn(StatePendingDidi.get());
        when(credentialStateService.getCredentialRevokeState()).thenReturn(StateRevoke);
        when(parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode())).thenReturn(getParameterConfigurationDidiIssuerMock());
        when(personService.findByDocumentNumber(anyLong())).thenReturn(opHolder);
        when(credentialBenefitSancorRepository.findTopByCreditHolderDniAndBeneficiaryDniOrderByIdDesc(holder.getDocumentNumber(), holder.getDocumentNumber())).thenReturn(Optional.of(credentialBenefitsSaved));
        when(credentialBenefitSancorRepository.save(any(CredentialBenefitSancor.class))).thenAnswer(new Answer<CredentialBenefitSancor>() {
            @Override
            public CredentialBenefitSancor answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (CredentialBenefitSancor) args[0];
            }
        });


        try {
            credentialBenefitSancorService.createCredentialsBenefitsHolderForNewLoan(loan);
        } catch (CredentialException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }

        verify(credentialBenefitSancorRepository, times(1)).save(credentialBenefitSancorCaptor.capture());
        CredentialBenefitSancor credentialBenefits = credentialBenefitSancorCaptor.getValue();

        Assertions.assertEquals(holder, credentialBenefits.getCreditHolder());
        Assertions.assertEquals(holder.getDocumentNumber(), credentialBenefits.getCreditHolderDni());
        Assertions.assertEquals(holder.getFirstName(), credentialBenefits.getCreditHolderFirstName());
        Assertions.assertEquals(holder.getLastName(), credentialBenefits.getCreditHolderLastName());

        Assertions.assertEquals(holder, credentialBenefits.getBeneficiary());
        Assertions.assertEquals(holder.getDocumentNumber(), credentialBenefits.getBeneficiaryDni());
        Assertions.assertEquals(holder.getFirstName(), credentialBenefits.getBeneficiaryFirstName());
        Assertions.assertEquals(holder.getLastName(), credentialBenefits.getBeneficiaryLastName());

        Assertions.assertEquals(StatePendingDidi.get(), credentialBenefits.getCredentialState());
        Assertions.assertEquals(credentialBenefitsSaved.getIdHistorical(), credentialBenefits.getIdHistorical());

        //Credential saved is not modified
        Assertions.assertEquals(99L, credentialBenefitsSaved.getIdHistorical());
        Assertions.assertEquals(StateRevoke.get(), credentialBenefitsSaved.getCredentialState());

    }

    @Test
    public void getHolderCredentialBenefitTest(){
        CredentialBenefitSancor credentialBenefitSancor = mockCredentialBenefitSancorMock();

        when(credentialBenefitSancorRepository.findTopByCreditHolderDniAndBeneficiaryDniOrderByIdDesc(any(),any()))
        .thenReturn(Optional.ofNullable(credentialBenefitSancor));

        Optional<CredentialBenefitSancor> result = credentialBenefitSancorService.getHolderCredentialBenefit(
                getPersonMockWithDid().get());

        Assertions.assertEquals(credentialBenefitSancor, result.get());
        verify(credentialBenefitSancorRepository, times(1))
                .findTopByCreditHolderDniAndBeneficiaryDniOrderByIdDesc(any(), any());

    }

    @Test
    public void buildHolderBenefitsCredentialTest(){
        CredentialBenefitSancor credentialBenefitSancor = mockCredentialBenefitSancorMock();
        SancorPolicy opSancorPolicy = new SancorPolicy();
        CredentialState credentialState = createCredentialStatePendingDidiMock().get();

        when(credentialStateService.getCredentialPendingDidiState()).thenReturn(credentialState);

        CredentialBenefitSancor result = credentialBenefitSancorService.buildHolderBenefitsCredential(
                credentialBenefitSancor, Optional.ofNullable(opSancorPolicy)
        );
        credentialBenefitSancor.setDateOfIssue(result.getDateOfIssue());

        Assertions.assertEquals(credentialBenefitSancor.toString(), result.toString());
        verify(credentialStateService, times(1)).getCredentialPendingDidiState();
    }

    @Test
    public void handleLoanFinalizedTest(){
        Loan loanFinalized = getMockLoan();
        List<Loan> otherLoansActiveForHolder = new ArrayList<>();

        when(personService.findByDocumentNumber(any())).thenReturn(getPersonMockWithDid());

        List<Loan> result = credentialBenefitSancorService.handleLoanFinalized(loanFinalized,
                otherLoansActiveForHolder);

        Assertions.assertNotNull(result);
        verify(personService, times(1)).findByDocumentNumber(any());
    }

    @Test
    public void handleLoanFinalizedElseTest(){
        Loan loanFinalized = getMockLoan();
        List<Loan> otherLoansActiveForHolder = Arrays.asList(loanFinalized);

        when(personService.findByDocumentNumber(any())).thenReturn(getPersonMockWithDid());

        List<Loan> result = credentialBenefitSancorService.handleLoanFinalized(loanFinalized,
                otherLoansActiveForHolder);

        Assertions.assertNotNull(result);
        verify(personService, times(0)).findByDocumentNumber(any());
    }

    @Test
    public void getCredentialBenefitSancorsOnPendingDidiStateTest(){

        CredentialState pendingDidiState = createCredentialStatePendingDidiMock().get();
        List<CredentialBenefitSancor> credentialBenefitSancors = Collections.singletonList(mockCredentialBenefitSancorMock());

        when(credentialStateService.getCredentialPendingDidiState()).thenReturn(pendingDidiState);
        when(credentialBenefitSancorRepository.findByCredentialState(any())).thenReturn(credentialBenefitSancors);

        List<CredentialBenefitSancor> result = credentialBenefitSancorService.getCredentialBenefitSancorsOnPendindDidiState();

        Assertions.assertNotNull(result);
        verify(credentialStateService, times(1)).getCredentialPendingDidiState();
        verify(credentialBenefitSancorRepository, times(1)).findByCredentialState(any());
    }

    @Test
    public void saveTest(){

        CredentialBenefitSancor credentialBenefitSancor = mockCredentialBenefitSancorMock();

        when(credentialBenefitSancorRepository.save(any())).thenReturn(credentialBenefitSancor);
        CredentialBenefitSancor result = credentialBenefitSancorService.save(credentialBenefitSancor);

        Assertions.assertEquals(result.toString(),credentialBenefitSancor.toString());
        verify(credentialBenefitSancorRepository, times(1)).save(any());
    }

    @Test
    public void getCredentialBenefitSancorActiveForDniTest(){

        CredentialBenefitSancor credentialBenefitSancor = mockCredentialBenefitSancorMock();
        Optional<CredentialState> activeDidiState = createCredentialStateActiveDidiMock();

        when(credentialStateService.getCredentialActiveState()).thenReturn(activeDidiState);
        when(credentialBenefitSancorRepository.findByCreditHolderDniAndCredentialState(any(),any()))
                .thenReturn(Collections.singletonList(credentialBenefitSancor));
        List<CredentialBenefitSancor> result = credentialBenefitSancorService
                .getCredentialBenefitSancorActiveForDni(1L);

        Assertions.assertEquals(result.size(), 1);
        verify(credentialStateService, times(1)).getCredentialActiveState();
        verify(credentialBenefitSancorRepository, times(1))
                .findByCreditHolderDniAndCredentialState(any(),any());
    }

    @Test
    public void buildNewOnPendidgDidiTest(){

        CredentialBenefitSancor credentialBenefitSancor = mockCredentialBenefitSancorMock();
        DidiAppUser newDidiAppUser = mockDidiUserApp();
        CredentialState credentialStatePending = createCredentialStatePendingDidiMock().get();

        when(credentialStateService.getCredentialPendingDidiState()).thenReturn(credentialStatePending);

        CredentialBenefitSancor result = credentialBenefitSancorService.buildNewOnPendidgDidi(credentialBenefitSancor,
                newDidiAppUser);

        Assertions.assertEquals(result.getBeneficiaryDni(), credentialBenefitSancor.getBeneficiaryDni());
        verify(credentialStateService, times(1)).getCredentialPendingDidiState();
    }

    private Loan getMockLoan() {
        Loan loan = new Loan();
        loan.setDniPerson(123456L);
        loan.setIdBondareaLoan("1a");
        loan.setIdGroup("group1");
        loan.setIdProductLoan("idProduction");
        loan.setUserId("userId");
        loan.setCycleDescription("Ciclo 1");
        loan.setStatus(LoanStatusCodes.ACTIVE.getCode());
        loan.setState(LoanStateCodes.OK.getCode());
        loan.setHasCredential(true);
        loan.setExpiredAmount(BigDecimal.valueOf(0));
        loan.setCreationDate(DateUtil.getLocalDateTimeNow().toLocalDate());
        return loan;
    }

    private Optional<Person> getPersonMockWithDid() {
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber((long) 123456);
        person.setFirstName("Pepito");
        //person.setDIDIsHisotoric(List.of(getDIDHistoricMock()));
        return Optional.of(person);
    }

    private Optional<CredentialState> createCredentialStateRevokeMock() {
        CredentialState credentialState = new CredentialState();
        credentialState.setId(3L);
        credentialState.setStateName(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode());
        return Optional.of(credentialState);
    }

    private Optional<CredentialState> createCredentialStatePendingDidiMock() {
        CredentialState credentialState = new CredentialState();
        credentialState.setId(2L);
        credentialState.setStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
        return Optional.of(credentialState);
    }

    private Optional<CredentialState> createCredentialStateActiveDidiMock() {
        CredentialState credentialState = new CredentialState();
        credentialState.setId(2L);
        credentialState.setStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        return Optional.of(credentialState);
    }

    private Optional<ParameterConfiguration> getParameterConfigurationDidiIssuerMock(){
        ParameterConfiguration parameterConfiguration = new ParameterConfiguration();
        parameterConfiguration.setConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());
        parameterConfiguration.setValue("1234567890");
        return Optional.of(parameterConfiguration);
    }

    private CredentialBenefitSancor mockCredentialBenefitSancorMock(){
        CredentialBenefitSancor credentialBenefitSancor = new CredentialBenefitSancor();
        credentialBenefitSancor.setCredentialState(createCredentialStatePendingDidiMock().get());
        credentialBenefitSancor.setBeneficiaryDni(34669543L);
        return credentialBenefitSancor;
    }

    private DidiAppUser mockDidiUserApp(){
        DidiAppUser didiAppUser = new DidiAppUser(1L, "23434234324", "PENDING");
        return didiAppUser;
    }


}
