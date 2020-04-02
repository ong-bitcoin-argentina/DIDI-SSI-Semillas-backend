package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialServiceTest {

    @Mock
    CredentialService credentialService;


    private List<CredentialDto> credentialsMock(){
        List<CredentialDto> credentials = new ArrayList<>();


        CredentialDto credential1 = new CredentialDto(1L,2L, LocalDateTime.now(),LocalDateTime.now().plusDays(1),"Jorge Rodrigues",29302594L,"Estado");
        credentials.add(credential1);

        return credentials;
    }

    @Test
    public void getCredentials() {
        when(credentialService.findAllCredentialsMock()).thenReturn(credentialsMock());

        List<CredentialDto> credentialsDto = credentialService.findAllCredentialsMock();

        verify(credentialService).findAllCredentialsMock();


        Assertions.assertTrue(credentialsDto.size() > 0);
        Assertions.assertEquals(credentialsMock().get(0).getId() ,credentialsDto.get(0).getId());
        Assertions.assertEquals(credentialsMock().get(0).getDniBeneficiary() ,credentialsDto.get(0).getDniBeneficiary());
        Assertions.assertEquals(credentialsMock().get(0).getIdDidiCredential() ,credentialsDto.get(0).getIdDidiCredential());
        Assertions.assertTrue(credentialsDto.get(0).getDateOfExpiry() != null);
        Assertions.assertTrue(credentialsDto.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsMock().get(0).getName() ,credentialsDto.get(0).getName());
    }

}
