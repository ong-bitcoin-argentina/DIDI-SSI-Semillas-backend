package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import io.restassured.internal.assertion.Assertion;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest
@Slf4j
public class CredentialServiceTest {

    @Mock
    CredentialService credentialService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private List<CredentialDto> credentialsMock(){
        List<CredentialDto> credentials = new ArrayList<>();


        CredentialDto credential1 = new CredentialDto(1L,2L,null, LocalDateTime.now(),LocalDateTime.now().plusDays(1),"Jorge Rodrigues",29302594L,"Estado");
        credentials.add(credential1);

        return credentials;
    }

    @Test
    public void getAllCredentials()
    {
        when(credentialService.findAllCredentialsMock()).thenReturn(credentialsMock());

        List<CredentialDto> credentialsDto = credentialService.findAllCredentialsMock();

        verify(credentialService).findAllCredentialsMock();

        Assertions.assertTrue(credentialsDto.size() > 0);
        Assertions.assertEquals(credentialsMock().get(0).getId() ,credentialsDto.get(0).getId());
        Assertions.assertEquals(credentialsMock().get(0).getDniBeneficiary() ,credentialsDto.get(0).getDniBeneficiary());
        Assertions.assertEquals(credentialsMock().get(0).getIdDidiCredential() ,credentialsDto.get(0).getIdDidiCredential());
        //Assertions.assertEquals(credentialsMock().get(0).getDateOfExpiry() ,credentialsDto.get(0).getDateOfExpiry());
        //Assertions.assertEquals(credentialsMock().get(0).getDateOfIssue() ,credentialsDto.get(0).getDateOfIssue());
        Assertions.assertEquals(credentialsMock().get(0).getName() ,credentialsDto.get(0).getName());
    }

}
