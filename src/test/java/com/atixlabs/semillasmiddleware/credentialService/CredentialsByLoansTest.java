package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.service.LoanService;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.RevocationReason;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitSancorService;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitService;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.app.service.CredentialStateService;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
public class CredentialsByLoansTest {

    @InjectMocks
    private CredentialService credentialService;

    @Mock
    private ProcessControlService processControlService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private LoanService loanService;

    @Mock
    private CredentialBenefitService credentialBenefitService;

    @Mock
    private CredentialBenefitSancorService credentialBenefitSancorService;

    @Mock
    private CredentialCreditRepository credentialCreditRepository;

    @Mock
    private RevocationReasonRepository revocationReasonRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private CredentialStateService credentialStateService;

    @Captor
    private ArgumentCaptor<Credential> credentialCaptor;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @Mock
    private ParameterConfigurationRepository parameterConfigurationRepository;

    @Mock
    private DidiService didiService;

    private List<Loan> defaultLoans = new ArrayList<>();

    private final String ID_GROUP = "idGroup";
    private final String ID_BONDAREA_LOAN_1 = "idBondareaLoan1";
    private final String ID_BONDAREA_LOAN_2 = "idBondareaLoan2";
    private final Long dni1 = 22222222L;
    private final Long dni2 = 33333333L;
    private Person holder = new Person();
    private CredentialCredit creditLoan1 = new CredentialCredit();
    private Long ID_CREDIT_1 = 999L;
    private String ID_DIDI_CREDENTIAL = "idDidiCredential";

    private void prepareMocks() throws InvalidProcessException {
        when(processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA)).thenReturn(false);
        when(processControlService.isProcessRunning(ProcessNamesCodes.CHECK_DEFAULTERS)).thenReturn(false);
        ProcessControl processCredentialControlMock = Mockito.mock(ProcessControl.class);
        when(processControlService.setStatusToProcess(ProcessNamesCodes.CREDENTIALS, ProcessControlStatusCodes.RUNNING)).thenReturn(processCredentialControlMock);
        when(processControlService.getProcessTimeByProcessCode(ProcessNamesCodes.CREDENTIALS.getCode())).thenReturn(LocalDateTime.MAX);
        when(loanService.findLastLoansModifiedInDefault(Mockito.any())).thenReturn(defaultLoans);

        Optional<Person> optionalPerson1 = Optional.of(holder);
        when(personRepository.findByDocumentNumber(dni1)).thenReturn(optionalPerson1);
        creditLoan1.setIdBondareaCredit(ID_BONDAREA_LOAN_1);
        creditLoan1.setCreditState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        creditLoan1.setId(ID_CREDIT_1);
        Optional<CredentialCredit> optionalCreditLoan1 = Optional.of(creditLoan1);
        when(credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(ID_BONDAREA_LOAN_1)).thenReturn(optionalCreditLoan1);
        Optional<RevocationReason> optionalRevocationReason = Optional.of(new RevocationReason(RevocationReasonsCodes.DEFAULT.getCode()));
        when(revocationReasonRepository.findByReason(RevocationReasonsCodes.DEFAULT.getCode())).thenReturn(optionalRevocationReason);
        Optional<Credential> optionalCredentialLoan1 = Optional.of(creditLoan1);
        when(credentialRepository.findById(ID_CREDIT_1)).thenReturn(optionalCredentialLoan1);
        Optional<CredentialState> optionalRevokeState= Optional.of(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        when(credentialStateService.getCredentialRevokeState()).thenReturn(optionalRevokeState);
        ParameterConfiguration parameterConfiguration = new ParameterConfiguration();
        parameterConfiguration.setValue(ConfigurationCodes.ID_DIDI_ISSUER.getCode());
        Optional<ParameterConfiguration> optionalParameterConfiguration = Optional.of(parameterConfiguration);
        when(parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode())).thenReturn(optionalParameterConfiguration);
        when(didiService.didiDeleteCertificate(anyString(), anyString())).thenReturn(true);

    }

    private Loan getLoan(Long dni, String bondareaLoanId, String idProductLoan, String idGroup, String userId, String status, String state, BigDecimal expiredAmount){
        Loan loan = new Loan();
        loan.setDniPerson(dni);
        loan.setIdBondareaLoan(bondareaLoanId);
        loan.setIdProductLoan(idProductLoan);
        loan.setIdGroup(idGroup);
        loan.setUserId(userId);
        loan.setStatus(status);
        loan.setState(state);
        loan.setInstalmentTotalQuantity(20);
        loan.setExpiredAmount(expiredAmount);
        loan.setCreationDate(DateUtil.getLocalDateTimeNow().toLocalDate());
        return loan;
    }

    private void initializeLoans(){
        defaultLoans.add( getLoan(dni1, ID_BONDAREA_LOAN_1, "prod1", ID_GROUP, "user1",
                LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.DEFAULT.getCode(), new BigDecimal(0))
        );
        defaultLoans.add( getLoan(dni2, ID_BONDAREA_LOAN_2, "prod2", ID_GROUP, "user2",
                LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.DEFAULT.getCode(), new BigDecimal(800))
        );
    }

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void revokeCredentialsInActiveNotEmitted_AfterChangeInLoan() throws InvalidProcessException {
        prepareMocks();
        initializeLoans();
        creditLoan1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        creditLoan1.setIdDidiCredential(null);
        credentialService.generateCreditAndBenefitsCredentialsByLoans();

        //Checking revocation of benefits credential for the holder and they family
        verify(credentialBenefitService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitService, times(1)).revokeFamilyCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitSancorService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        //Checking the revocation of the credit credential itself
        verify(credentialRepository, times(1)).save(credentialCaptor.capture());
        Assertions.assertEquals(credentialCaptor.getValue().getCredentialState().getStateName(), CredentialStatesCodes.CREDENTIAL_REVOKE.getCode());
        //Checking if the person was updated with the credits in default
        verify(personRepository, times(1)).save(personCaptor.capture());
        Person person = personCaptor.getValue();
        Assertions.assertEquals(ID_BONDAREA_LOAN_1, person.getDefaults().get(0).getIdBondareaLoan());

    }

    @Test
    public void revokeCredentialsInActiveEmitted_AfterChangeInLoan() throws InvalidProcessException {
        prepareMocks();
        initializeLoans();
        creditLoan1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        creditLoan1.setIdDidiCredential(ID_DIDI_CREDENTIAL);
        credentialService.generateCreditAndBenefitsCredentialsByLoans();

        //Checking revocation of benefits credential for the holder and they family
        verify(credentialBenefitService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitService, times(1)).revokeFamilyCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitSancorService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        //Checking the revocation of the credit credential itself
        verify(credentialRepository, times(1)).save(credentialCaptor.capture());
        Assertions.assertEquals(credentialCaptor.getValue().getCredentialState().getStateName(), CredentialStatesCodes.CREDENTIAL_REVOKE.getCode());
        //Checking if the person was updated with the credits in default
        verify(personRepository, times(1)).save(personCaptor.capture());
        Person person = personCaptor.getValue();
        Assertions.assertEquals(ID_BONDAREA_LOAN_1, person.getDefaults().get(0).getIdBondareaLoan());
        //Checking that it was eliminated in DIDI
        verify(didiService, times(1)).didiDeleteCertificate(ID_DIDI_CREDENTIAL, RevocationReasonsCodes.DEFAULT.getCode());

    }

    @Test
    public void revokeCredentialsInRevoked_AfterChangeInLoan() throws InvalidProcessException {
        prepareMocks();
        initializeLoans();
        creditLoan1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        credentialService.generateCreditAndBenefitsCredentialsByLoans();

        //Checking revocation of benefits credential for the holder and they family
        verify(credentialBenefitService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitService, times(1)).revokeFamilyCredentialsBenefitsForLoan(holder);
        verify(credentialBenefitSancorService, times(1)).revokeHolderCredentialsBenefitsForLoan(holder);
        //Checking that the credit wasn't revoked
        verify(credentialRepository, times(0)).save(credentialCaptor.capture());
        //Checking if the person was updated with the credits in default
        verify(personRepository, times(1)).save(personCaptor.capture());
        Person person = personCaptor.getValue();
        Assertions.assertEquals(ID_BONDAREA_LOAN_1, person.getDefaults().get(0).getIdBondareaLoan());

    }
}
