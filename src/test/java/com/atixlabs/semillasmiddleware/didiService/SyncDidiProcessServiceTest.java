package com.atixlabs.semillasmiddleware.didiService;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.service.CredentialCreditService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class SyncDidiProcessServiceTest {

    @Mock
    private CredentialCreditService credentialCreditService;

    @Mock
    private DidiAppUserService didiAppUserService;

    @InjectMocks
    private SyncDidiProcessService syncDidiProcessService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenEmmitCredentialCreditsAndCredentialCreditsToEmmitIsEmpty_thenDoNothing() throws CredentialException {

        when(credentialCreditService.getCredentialCreditsOnPendindDidiState()).thenReturn(new ArrayList<CredentialCredit>());

        syncDidiProcessService.emmitCredentialCredits();


    }

    @Test
    public void whenHolderNotHaveDIDRegister_thenCredentialCreditNotEmmit(){

        when(didiAppUserService.getDidiAppUserByDni(anyLong())).thenReturn(null);

        CredentialCredit credentialCredit = this.getCredentialCreditMock();
        credentialCredit.setIdDidiReceptor(null);

        syncDidiProcessService.emmitCredentialCredit(credentialCredit);

        Assert.assertNull(credentialCredit.getIdDidiReceptor());

    }


    private CredentialCredit getCredentialCreditMock(){
        CredentialCredit credentialCredit = new CredentialCredit();
        credentialCredit.setCreditHolderDni(22333444L);
        credentialCredit.setIdDidiReceptor(null);
        return credentialCredit;
    }

}
