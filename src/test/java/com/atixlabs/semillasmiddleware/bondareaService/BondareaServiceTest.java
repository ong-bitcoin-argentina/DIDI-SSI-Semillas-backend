package com.atixlabs.semillasmiddleware.bondareaService;


import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.exceptions.BondareaSyncroException;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import com.atixlabs.semillasmiddleware.app.bondarea.service.LoanService;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BondareaServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private BondareaService bondareaService;

    @Captor
    private ArgumentCaptor<Loan> captor;

    @Mock
    private LoanService loanService;

    @Mock
    private ProcessControlService processControlService;

    @Mock
    private ParameterConfigurationRepository parameterConfigurationRepository;

    @Mock
    private PersonRepository personRepository;

    @Before
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    public static final String ID_GROUP = "idGroup";
    public static final String ID_LOAN_NON_DEFAULT = "nonDefaultLoanId";
    public static final String ID_LOAN_DEFAULT = "defaultLoanId";
    public List<Loan> loans = new ArrayList<>();

    public BondareaLoanDto getMockBondareaLoan(){
        BondareaLoanDto loan = new BondareaLoanDto();
        loan.setDni(123456L);
        loan.setIdBondareaLoan("1a");
        //loan.setStatus(LoanStatusCodes.ACTIVE.getCode());
        loan.setExpiredAmount(BigDecimal.valueOf(0));
        loan.setCreationDate(DateUtil.getLocalDateTimeNow().toLocalDate().toString());
        loan.setDateFirstInstalment("01/01/2020");
        loan.setFeeDuration("1_s");

        return loan;
    }

    private Loan getMockLoan(){
        Loan loan = new Loan();
        loan.setDniPerson(123456L);
        loan.setCreationDate(DateUtil.getLocalDateTimeNow().toLocalDate());
        return loan;
    }

    private  List<BondareaLoanDto> firstBondareaLoansData(){
        List<BondareaLoanDto> loans = new ArrayList<>();

        BondareaLoanDto loan = getMockBondareaLoan();
        loans.add(loan);

        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2a");
        loans.add(loan2);

        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3a");
        loans.add(loan3);

        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4a");
        loans.add(loan4);

        return loans;
    }

    private void initializeLoans(){
        Loan loan1 = getMockLoan();
        loan1.setDniPerson(22222222L);
        loan1.setIdBondareaLoan(ID_LOAN_NON_DEFAULT);
        loan1.setIdProductLoan("prod1");
        loan1.setIdGroup(ID_GROUP);
        loan1.setUserId("user1");
        loan1.setStatus(LoanStatusCodes.ACTIVE.getCode());
        loan1.setState(LoanStateCodes.OK.getCode());
        loan1.setCycleDescription("cycle2");
        loan1.setExpiredAmount(new BigDecimal(0));
        loans.add(loan1);

        Loan loan2 = getMockLoan();
        loan2.setDniPerson(33333333L);
        loan2.setIdBondareaLoan(ID_LOAN_DEFAULT);
        loan2.setTagBondareaLoan("nuevo tag");
        loan2.setIdProductLoan("prod2");
        loan2.setIdGroup(ID_GROUP);
        loan2.setUserId("user2");
        loan2.setStatus(LoanStatusCodes.ACTIVE.getCode());
        loan2.setState(LoanStateCodes.OK.getCode());
        loan2.setCycleDescription("cycle3");
        loan2.setExpiredAmount(new BigDecimal(1500));
        loans.add(loan2);
    }

    private  List<BondareaLoanDto> secondLoansData(){
        List<BondareaLoanDto> loans = new ArrayList<>();

        //loan 2 is the same
        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setDni(22222222L);
        loan2.setIdBondareaLoan("2a");
        loan2.setIdProductLoan("prod2");
        loan2.setIdGroup("idgroup2");
        loan2.setUserId("user2");
        loan2.setStatus(55);
        loan2.setCycle("cycle2");
        loan2.setExpiredAmount(new BigDecimal(0));
        loan2.setCreationDate("20/06/2020");
        loans.add(loan2);

        //loan 3 modified tag
        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setDni(33333333L);
        loan3.setIdBondareaLoan("3a");
        loan3.setTagBondareaLoan("nuevo tag");
        loan3.setIdProductLoan("prod3");
        loan3.setIdGroup("idgroup3");
        loan3.setUserId("user3");
        loan3.setStatus(55);
        loan3.setCycle("cycle3");
        loan3.setExpiredAmount(new BigDecimal(0));
        loan3.setCreationDate("20/06/2020");
        loans.add(loan3);

        //loan 4 is in default
        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setDni(44444444L);
        loan4.setIdBondareaLoan("4a");
        loan4.setIdProductLoan("prod4");
        loan4.setIdGroup("idgroup4");
        loan4.setUserId("user4");
        loan4.setStatus(55);
        loan4.setCycle("cycle4");
        loan4.setExpiredAmount(BigDecimal.valueOf(100));
        loan4.setCreationDate("20/06/2020");
        loans.add(loan4);

        //new loan
        BondareaLoanDto loan5 = getMockBondareaLoan();
        loan5.setDni(55555555L);
        loan5.setIdBondareaLoan("5a");
        loan5.setIdProductLoan("prod5");
        loan5.setIdGroup("idgroup5");
        loan5.setUserId("user5");
        loan5.setStatus(55);
        loan5.setCycle("cycle5");
        loan5.setCreationDate("20/06/2020");
        loans.add(loan5);

        return loans;
    }

    private  List<BondareaLoanDto> secondLoansDataAllNew(){
        List<BondareaLoanDto> loans = new ArrayList<>();

        //all new loans
        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("5a");
        loans.add(loan2);

        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("6a");
        loans.add(loan3);

        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("7a");
        loans.add(loan4);


        return loans;
    }

    @Test
    public void loansInDefaultFromExpiredAmount() throws Exception {
        initializeLoans();
        when(processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA)).thenReturn(true);
        when(processControlService.isProcessRunning(ProcessNamesCodes.CHECK_DEFAULTERS)).thenReturn(false);
        ProcessControl processControlMock = Mockito.mock(ProcessControl.class);
        when(processControlService.setStatusToProcess(Mockito.any(ProcessNamesCodes.class), Mockito.any())).thenReturn(processControlMock);
        when(loanService.findLastLoansModified(Mockito.any(), Mockito.any())).thenReturn(loans);
        ParameterConfiguration maxAmountParameterConfiguration = new ParameterConfiguration();
        maxAmountParameterConfiguration.setValue("800");
        Optional<ParameterConfiguration> opParameterConfiguration = Optional.of(maxAmountParameterConfiguration);
        when(parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode())).thenReturn(opParameterConfiguration);
        when(loanRepository.findAllByIdGroupAndStatus(ID_GROUP, LoanStatusCodes.ACTIVE.getCode())).thenReturn(loans);

        Assertions.assertTrue(loans.stream().allMatch(m -> !m.isDefault()));
        bondareaService.checkCreditsForDefault();
        Assertions.assertFalse(loans.stream().allMatch(m -> m.isDefault()));
    }


    @Test
    public void saveNewDataOnLoan() throws InvalidProcessException {
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        List<BondareaLoanDto> loans = firstBondareaLoansData();
        LocalDateTime processStart = DateUtil.getLocalDateTimeNow();
        bondareaService.createAndUpdateLoans(loans, processStart);

        verify(loanRepository,times(4)).save(captor.capture());

        List<Loan> loansSaves = captor.getAllValues();
        Loan firstLoan = loansSaves.get(0);

        Assertions.assertEquals(firstBondareaLoansData().get(0).getIdBondareaLoan(), firstLoan.getIdBondareaLoan());
        Assertions.assertTrue(firstLoan.getDniPerson() != null);

    }

    /**
     * This test will test that: the service takes an existing loans data from database compare with the new loans data and merge it.
     * Mix of all the different conditions
     */
    @Test
    public void updateAllLoans() throws InvalidProcessException {
        initializeLoans();
        when(loanRepository.findByIdBondareaLoan(ID_LOAN_DEFAULT)).thenReturn(Optional.of(loans.get(0)));
        when(loanRepository.findByIdBondareaLoan(ID_LOAN_NON_DEFAULT)).thenReturn(Optional.of(loans.get(1)));
        when(loanRepository.findByIdBondareaLoan("5a")).thenReturn(Optional.empty());
        when(loanRepository.updateStateBySynchroTimeLessThanAndActive(any(), eq(LoanStatusCodes.PENDING.getCode()), eq(LoanStatusCodes.ACTIVE.getCode()))).thenReturn(0);

        List<BondareaLoanDto> loans = secondLoansData();
        LocalDateTime processStart = DateUtil.getLocalDateTimeNow();
        bondareaService.createAndUpdateLoans(loans, processStart);

        verify(loanRepository,times(4)).save(captor.capture());

        List<Loan> loansSaves = captor.getAllValues();

        Assertions.assertTrue(loansSaves.size() == firstBondareaLoansData().size());
        Assertions.assertEquals(LoanStatusCodes.ACTIVE.getCode(), loansSaves.get(3).getStatus());
        Assertions.assertFalse(loansSaves.get(1).getTagBondareaLoan().equals(firstBondareaLoansData().get(2).getTagBondareaLoan()));
    }

    @Test
    //TODO refactor
    public void updateLoansOldToPending() throws InvalidProcessException {
        when(loanRepository.findByIdBondareaLoan("5a")).thenReturn(Optional.empty());
        when(loanRepository.findByIdBondareaLoan("6a")).thenReturn(Optional.empty());
        when(loanRepository.findByIdBondareaLoan("7a")).thenReturn(Optional.empty());

        when(loanRepository.updateStateBySynchroTimeLessThanAndActive(any(), eq(LoanStatusCodes.PENDING.getCode()), eq(LoanStatusCodes.ACTIVE.getCode()))).thenReturn(0);

        List<BondareaLoanDto> loans = secondLoansDataAllNew();
        LocalDateTime processStart = DateUtil.getLocalDateTimeNow();
        bondareaService.createAndUpdateLoans(loans, processStart);

       // verify(loanRepository,times(7)).save(captor.capture());

        List<Loan> loansSaves = captor.getAllValues();

        //Assertions.assertTrue(loansSaves.size() > firstBondareaLoansData().size());

        //the active loans will be all. But some are pending -> 3
      //  List<Loan> activeLoans = loansSaves.stream().filter(loan -> loan.getStatus().equals(LoanStatusCodes.ACTIVE.getCode())).collect(Collectors.toList());
       // Assertions.assertEquals(3, activeLoans.size() );

        // the pending loans will be the older loans in this case -> 4
       // List<Loan> pendingLoans = loansSaves.stream().filter(loan -> loan.getStatus().equals(LoanStatusCodes.PENDING.getCode())).collect(Collectors.toList());
       // Assertions.assertEquals(4, pendingLoans.size() );

    }

    @Test
    public void determinatePendingLoanToFinish(){
      //  when(loanRepository.findAllByStatus(LoanStatusCodes.PENDING.getCode())).thenReturn(pendientes);
        //when(bondareaService.getLoans(BondareaLoanStatusCodes.FINALIZED.getCode(), anyString(),"")).thenReturn(vuelve 1 l)
    //TODO


    }

    /*@Test
    public void updateLoansAllExpired() {
        when(loanRepository.findAll()).thenReturn(firstBondareaLoansData());

        List<Loan> loans = secondLoansDataAllExpired();
        bondareaService.createAndUpdateLoans(loans);

        verify(loanRepository,times(4)).save(captor.capture());

        List<Loan> loansSaves = captor.getAllValues();


        //the active loans will be any.
        List<Loan> activeLoans = loansSaves.stream().filter(loan -> loan.getIsActive() == true).collect(Collectors.toList());
        Assertions.assertEquals(0, activeLoans.size() );

        // the pending loans will be any.
        List<Loan> pendingLoans = loansSaves.stream().filter(loan -> loan.getPending() == true).collect(Collectors.toList());
        Assertions.assertEquals(0, pendingLoans.size() );

    }
    */


    @Test
    public void whenQuantityDayIsTypeWeek_thengetQuantityDayForTypeFeeReturnSeven() throws BondareaSyncroException {

        Integer quantityExpected = 7;

        Integer quantity = this.bondareaService.getQuantityDayForTypeFee("s");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    public void whenQuantityDayIsTypeMonth_thengetQuantityDayForTypeFeeReturn30() throws BondareaSyncroException {

        Integer quantityExpected = 30;

        Integer quantity = this.bondareaService.getQuantityDayForTypeFee("m");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test(expected = BondareaSyncroException.class)
    public void whenQuantityDayIsTypeUnknow_thenExeption() throws BondareaSyncroException {

        Integer quantity = this.bondareaService.getQuantityDayForTypeFee("g");

    }


    @Test
    public void whenQuantityDayIsTypeWeekAndQuantityOne_thenReturnSeven() throws BondareaSyncroException {

        Double quantityExpected = 7D;

        Double quantity = this.bondareaService.getTcDays("1_s");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    public void whenQuantityDayIsTypeWeekAndQuantityHalf_thenReturnthree() throws BondareaSyncroException {

        Double quantityExpected = 3D;

        Double quantity = this.bondareaService.getTcDays("0.5_s");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    public void whenQuantityDayIsTypeWeekAndQuantity3_thenReturnTwentyone() throws BondareaSyncroException {

        Double quantityExpected = 21D;

        Double quantity = this.bondareaService.getTcDays("3_s");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    public void whenQuantityDayIsTypeMonthAndQuantityOne_thenReturnSeven() throws BondareaSyncroException {

        Double quantityExpected = 30D;

        Double quantity = this.bondareaService.getTcDays("1_m");

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    @Ignore
    public void whenQuantityDayIsTypeWeekAndQuantityOne_thenCalculateFeeNumberReturn5() throws BondareaSyncroException {


        BondareaLoanDto bondareaLoanDto = new BondareaLoanDto();
        bondareaLoanDto.setFeeDuration("1_s");
        bondareaLoanDto.setDateFirstInstalment("01/07/2020");

       // when(DateUtil.getLocalDateNow()).thenReturn(LocalDate.of(2020,7,27));

        Integer quantityExpected = 4;

        Integer quantity = this.bondareaService.calculateFeeNumber(bondareaLoanDto);

        Assert.assertEquals(quantityExpected,quantity);;
    }

    @Test
    @Ignore
    public void whenQuantityDayIsTypeMonthAndQuantityOne_thenCalculateFeeNumberReturn5() throws BondareaSyncroException {


        BondareaLoanDto bondareaLoanDto = new BondareaLoanDto();
        bondareaLoanDto.setFeeDuration("1_m");
        bondareaLoanDto.setDateFirstInstalment("01/07/2020");

        // when(DateUtil.getLocalDateNow()).thenReturn(LocalDate.of(2020,7,27));

        Integer quantityExpected = 1;

        Integer quantity = this.bondareaService.calculateFeeNumber(bondareaLoanDto);

        Assert.assertEquals(quantityExpected,quantity);;
    }
}
