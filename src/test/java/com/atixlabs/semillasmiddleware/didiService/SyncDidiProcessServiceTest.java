package com.atixlabs.semillasmiddleware.didiService;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
public class SyncDidiProcessServiceTest {

    @Mock
    private CredentialCreditService credentialCreditService;

    @Mock
    private CredentialBenefitService credentialBenefitService;

    @Mock
    private CredentialIdentityService credentialIdentityService;

    @Mock
    private CredentialDwellingService credentialDwellingService;

    @Mock
    private CredentialEntrepreneurshipService credentialEntrepreneurshipService;

    @Mock
    private DidiAppUserService didiAppUserService;

    @Mock
    private DidiService didiService;

    @InjectMocks
    private SyncDidiProcessService syncDidiProcessService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenEmmitCredentialCreditsAndCredentialCreditsToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialCreditService.getCredentialCreditsOnPendindDidiState()).thenReturn(new ArrayList<CredentialCredit>());

        syncDidiProcessService.emmitCredentialsCredit();


    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialCreditNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialCredit credentialCredit = this.getCredentialCreditMock();
        credentialCredit.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialCredit(credentialCredit);

        Assert.assertNull(credentialCredit.getIdDidiReceptor());

    }


    @Test
    public void whenHolderHaveDIDRegister_thenEmmitCredentialCredit(){


        CredentialCredit credentialCredit = this.getCredentialCreditMock();
        credentialCredit.setIdDidiReceptor(null);

        DidiAppUser didiAppUser = this.getDidiAppUserMock();

        when(didiAppUserService.getDidiAppUserByDni(credentialCredit.getCreditHolderDni())).thenReturn(Optional.of(didiAppUser));
        when(credentialCreditService.save(credentialCredit)).thenReturn(credentialCredit);

        syncDidiProcessService.emmitCredentialCredit(credentialCredit);

        verify(didiService, times(1)).createAndEmmitCertificateDidi(credentialCredit);


    }

    //BENEFITS
    @Test
    public void whenEmmitCredentialBenefitsAndCredentialBenefitsToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialBenefitService.getCredentialBenefitsOnPendindDidiState()).thenReturn(new ArrayList<CredentialBenefits>());

        syncDidiProcessService.emmitCredentialsBenefit();

        verify(didiService, times(0)).createAndEmmitCertificateDidi(any());

    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialBenefitsNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialBenefits credentialBenefits = this.getCredentialBenefitsMock();
        credentialBenefits.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialBenefit(credentialBenefits);

        Assert.assertNull(credentialBenefits.getIdDidiReceptor());

    }

    @Test
    public void whenHolderHaveDIDRegisterAndCredentialBenefitPendindDidi_thenEmmitCredentialBenefit(){


        CredentialBenefits credentialBenefits = this.getCredentialBenefitsMock();
        credentialBenefits.setIdDidiReceptor(null);

        DidiAppUser didiAppUser = this.getDidiAppUserMock();

        when(didiAppUserService.getDidiAppUserByDni(credentialBenefits.getBeneficiaryDni())).thenReturn(Optional.of(didiAppUser));
        when(credentialBenefitService.save(credentialBenefits)).thenReturn(credentialBenefits);

        syncDidiProcessService.emmitCredentialBenefit(credentialBenefits);

        verify(didiService, times(1)).createAndEmmitCertificateDidi(credentialBenefits);


    }

    //IDENTITY
    @Test
    public void whenEmmitCredentialIdentityAndCredentialsIdentitiesToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialIdentityService.getCredentialIdentityOnPendindDidiState()).thenReturn(new ArrayList<CredentialIdentity>());

        syncDidiProcessService.emmitCredentialsIdentity();

        verify(didiService, times(0)).createAndEmmitCertificateDidi(any());

    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialIdentityNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialIdentity credentialIdentity = this.getCredentialIdentityMock();
        credentialIdentity.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialIdentity(credentialIdentity);

        Assert.assertNull(credentialIdentity.getIdDidiReceptor());

    }


    @Test
    public void whenHolderHaveDIDRegisterAndCredentialIdentityPendindDidi_thenEmmitCredentialIdentity(){


        CredentialIdentity credentialIdentity = this.getCredentialIdentityMock();
        credentialIdentity.setIdDidiReceptor(null);

        DidiAppUser didiAppUser = this.getDidiAppUserMock();

        when(didiAppUserService.getDidiAppUserByDni(credentialIdentity.getBeneficiaryDni())).thenReturn(Optional.of(didiAppUser));
        when(credentialIdentityService.save(credentialIdentity)).thenReturn(credentialIdentity);

        syncDidiProcessService.emmitCredentialIdentity(credentialIdentity);

        verify(didiService, times(1)).createAndEmmitCertificateDidi(credentialIdentity);


    }


    //DWELLING
    @Test
    public void whenEmmitCredentialDWellingAndCredentialsDwellingToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialDwellingService.getCredentialDwellingOnPendindDidiState()).thenReturn(new ArrayList<CredentialDwelling>());

        syncDidiProcessService.emmitCredentialsDwelling();

        verify(didiService, times(0)).createAndEmmitCertificateDidi(any());

    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialDwellingNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialDwelling credentialDwelling = this.getCredentialDwellingMock();

        credentialDwelling.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialDwelling(credentialDwelling);

        Assert.assertNull(credentialDwelling.getIdDidiReceptor());

    }


    @Test
    public void whenHolderHaveDIDRegisterAndCredentialDwellingPendindDidi_thenEmmitCredentialDwelling(){


        CredentialDwelling credentialDwelling = this.getCredentialDwellingMock();
        credentialDwelling.setIdDidiReceptor(null);

        DidiAppUser didiAppUser = this.getDidiAppUserMock();

        when(didiAppUserService.getDidiAppUserByDni(credentialDwelling.getBeneficiaryDni())).thenReturn(Optional.of(didiAppUser));
        when(credentialDwellingService.save(credentialDwelling)).thenReturn(credentialDwelling);

        syncDidiProcessService.emmitCredentialDwelling(credentialDwelling);

        verify(didiService, times(1)).createAndEmmitCertificateDidi(credentialDwelling);

    }


    //Entrepreneurship
    @Test
    public void whenEmmitCredentialEntrepreneurshipAndCredentialsEntrepreneurshipToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialEntrepreneurshipService.getCredentialEntrepreneurshipOnPendindDidiState()).thenReturn(new ArrayList<CredentialEntrepreneurship>());

        syncDidiProcessService.emmitCredentialsEntrepreneurship();

        verify(didiService, times(0)).createAndEmmitCertificateDidi(any());

    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialEntrepreneurshipNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialEntrepreneurship credentialEntrepreneurship = this.getCredentialEntrepreneurshipMock();

        credentialEntrepreneurship.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialEntrepreneurship(credentialEntrepreneurship);

        Assert.assertNull(credentialEntrepreneurship.getIdDidiReceptor());

    }

    @Test
    public void whenHolderHaveDIDRegisterAndCredentialEntrepreneurshipPendindDidi_thenEmmitCredentialEntrepreneurship(){


        CredentialEntrepreneurship credentialEntrepreneurship = this.getCredentialEntrepreneurshipMock();
        credentialEntrepreneurship.setIdDidiReceptor(null);

        DidiAppUser didiAppUser = this.getDidiAppUserMock();

        when(didiAppUserService.getDidiAppUserByDni(credentialEntrepreneurship.getBeneficiaryDni())).thenReturn(Optional.of(didiAppUser));
        when(credentialEntrepreneurshipService.save(credentialEntrepreneurship)).thenReturn(credentialEntrepreneurship);

        syncDidiProcessService.emmitCredentialEntrepreneurship(credentialEntrepreneurship);

        verify(didiService, times(1)).createAndEmmitCertificateDidi(credentialEntrepreneurship);

    }

    private CredentialCredit getCredentialCreditMock(){
        CredentialCredit credentialCredit = new CredentialCredit();
        credentialCredit.setCreditHolderDni(36637842L);
        credentialCredit.setIdDidiReceptor(null);
        credentialCredit.setId(1L);
        credentialCredit.setCreditHolder(null);
        credentialCredit.setCreditType("CONAMI 31");
        credentialCredit.setCurrentCycle("1");
        credentialCredit.setCreditState("Vigente");
        credentialCredit.setExpiredAmount(new BigDecimal(9L));
        credentialCredit.setIdBondareaCredit("987654");
        credentialCredit.setIdGroup("idGroup");
        credentialCredit.setTotalCycles(12);
        credentialCredit.setBeneficiaryDni(36637842L);
        credentialCredit.setBeneficiaryFirstName("Flor");
        credentialCredit.setBeneficiaryLastName("Tior");
        credentialCredit.setCreditHolderFirstName("Flor");
        credentialCredit.setCreditHolderLastName("Tiore");

        return credentialCredit;
    }

    private CredentialBenefits getCredentialBenefitsMock(){
        CredentialBenefits credentialBenefits = new CredentialBenefits();
        credentialBenefits.setCredentialState(this.getPendingDidiCredentialStateMock());
        credentialBenefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
        credentialBenefits.setBeneficiaryFirstName("Flor");
        credentialBenefits.setBeneficiaryLastName("Tior");
        credentialBenefits.setCreditHolderFirstName("Flor");
        credentialBenefits.setCreditHolderLastName("Tiore");
        credentialBenefits.setCreditHolderDni(36637842L);
        credentialBenefits.setBeneficiaryDni(36637842L);
        credentialBenefits.setIdDidiReceptor(null);
        credentialBenefits.setId(1L);
        credentialBenefits.setCreditHolder(null);
        credentialBenefits.setCredentialCategory(CredentialCategoriesCodes.BENEFIT.getCode());

        return credentialBenefits;
    }

    private CredentialIdentity getCredentialIdentityMock(){
        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setCredentialState(this.getPendingDidiCredentialStateMock());
        credentialIdentity.setBeneficiaryFirstName("Flor");
        credentialIdentity.setBeneficiaryLastName("Tior");
        credentialIdentity.setCreditHolderFirstName("Flor");
        credentialIdentity.setCreditHolderLastName("Tiore");
        credentialIdentity.setCreditHolderDni(36637842L);
        credentialIdentity.setBeneficiaryDni(36637842L);
        credentialIdentity.setIdDidiReceptor(null);
        credentialIdentity.setId(1L);
        credentialIdentity.setCreditHolder(null);
        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());
        credentialIdentity.setRelationWithCreditHolder("familiar");
        credentialIdentity.setBeneficiaryGender("Masculino");
        credentialIdentity.setBeneficiaryBirthDate(LocalDate.of(1990,12,12));

        return credentialIdentity;
    }

    private CredentialDwelling getCredentialDwellingMock(){
        CredentialDwelling credentialDwelling = new CredentialDwelling();
        credentialDwelling.setCredentialState(this.getPendingDidiCredentialStateMock());
        credentialDwelling.setBeneficiaryFirstName("Flor");
        credentialDwelling.setBeneficiaryLastName("Tior");
        credentialDwelling.setCreditHolderFirstName("Flor");
        credentialDwelling.setCreditHolderLastName("Tiore");
        credentialDwelling.setCreditHolderDni(36637842L);
        credentialDwelling.setBeneficiaryDni(36637842L);
        credentialDwelling.setIdDidiReceptor(null);
        credentialDwelling.setId(1L);
        credentialDwelling.setCreditHolder(null);
        credentialDwelling.setCredentialCategory(CredentialCategoriesCodes.DWELLING.getCode());
        credentialDwelling.setDwellingType("casa");
        credentialDwelling.setDwellingAddress("Direccion 123");
        credentialDwelling.setPossessionType("Dueño");

        return credentialDwelling;
    }

    private CredentialEntrepreneurship getCredentialEntrepreneurshipMock(){
        CredentialEntrepreneurship credentialEntrepreneurship = new CredentialEntrepreneurship();
        credentialEntrepreneurship.setCredentialState(this.getPendingDidiCredentialStateMock());
        credentialEntrepreneurship.setBeneficiaryFirstName("Flor");
        credentialEntrepreneurship.setBeneficiaryLastName("Tior");
        credentialEntrepreneurship.setCreditHolderFirstName("Flor");
        credentialEntrepreneurship.setCreditHolderLastName("Tiore");
        credentialEntrepreneurship.setCreditHolderDni(36637842L);
        credentialEntrepreneurship.setBeneficiaryDni(36637842L);
        credentialEntrepreneurship.setIdDidiReceptor(null);
        credentialEntrepreneurship.setId(1L);
        credentialEntrepreneurship.setCreditHolder(null);
        credentialEntrepreneurship.setCredentialCategory(CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode());
        credentialEntrepreneurship.setEntrepreneurshipAddress("Direccion 123");
        credentialEntrepreneurship.setEntrepreneurshipType("Comercio");
        credentialEntrepreneurship.setStartActivity(2018);
        credentialEntrepreneurship.setMainActivity("Comercio");
        credentialEntrepreneurship.setEntrepreneurshipName("el comercio");
        credentialEntrepreneurship.setEndActivity(null);

        return credentialEntrepreneurship;
    }

    private CredentialState getPendingDidiCredentialStateMock(){
        return new CredentialState(CredentialStatesCodes.PENDING_DIDI.getCode());
    }

    private DidiAppUser getDidiAppUserMock(){
        DidiAppUser didiAppUser = new DidiAppUser();
        didiAppUser.setDni(36637842L);
        didiAppUser.setDid("did:ethr:0x45b5bf83cc010c18739110d8d3397f1fa8a4d20a");
        didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_OK.getCode());

        return didiAppUser;
    }

}
