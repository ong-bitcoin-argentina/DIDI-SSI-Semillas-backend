package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProviderServiceTest {

    @InjectMocks
    ProviderService providerService;

    @Mock
    ProviderRepository providerRepository;

    @Mock
    ProviderCategoryRepository providerCategoryRepository;

    @Mock
    ProviderCategoryService providerCategoryService;


    @Test
    void whenCreatingUserWitInvalidCategoryExpectToThrowInexistentCategoryException() {
        ProviderCreateRequest providerCreateRequest = this.getNewProviderRequest();
        try{
            providerService.create(providerCreateRequest);
        }catch (InexistentCategoryException ice){
            fail();
        }

    }


    @Test
    void findAll() {

    }

    private ProviderCreateRequest getNewProviderRequest(){
        return ProviderCreateRequest
                .builder()
                .email("provider@test.com")
                .benefit(59)
                .categoryId(1l)
                .phone("+5411111111")
                .name("Provider")
                .speciality("Speciality")
                .build();
    }
}