package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.controller.IdentityValidationRequestController;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.StatusChangeDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.exceptions.InexistentIdentityValidationRequestException;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository.IdentityValidationRequestRepository;
import com.atixlabs.semillasmiddleware.app.service.DidiServerService;
import com.atixlabs.semillasmiddleware.app.service.MailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IdentityValidationRequestServiceTest {

    private IdentityValidationRequestService identityValidationRequestService;

    @Autowired
    private IdentityValidationRequestRepository identityValidationRequestRepository;

    @Mock
    private DidiServerService didiServerService;

    @Mock
    private ShareStateChangeService shareStateChangeService;

    @BeforeEach
    public void setUp() {
        identityValidationRequestService = new IdentityValidationRequestService(identityValidationRequestRepository, shareStateChangeService, didiServerService);
    }

    @Test
    public void whenChangingStateOfInexistentExpectToThrowInexistentRequestException() {
        identityValidationRequestRepository.save(getNewRequest());
        identityValidationRequestService.findById(1l).orElseGet(() -> fail());
        StatusChangeDto statusChangeDto = new StatusChangeDto(RequestState.FAILURE, Optional.empty(), Optional.of("reason"), Optional.of(38746263l));
        assertThrows(InexistentIdentityValidationRequestException.class, () -> identityValidationRequestService.changeRequestState(Long.MAX_VALUE, statusChangeDto));
    }

    @Test
    public void whenChangingStateOfValidRequestExpectToBeSaved() {
        Long dni = 38746263l;
        identityValidationRequestRepository.save(getNewRequest());
        identityValidationRequestService.findById(1l).orElseGet(() -> fail());
        try {
            StatusChangeDto statusChangeDto = new StatusChangeDto(RequestState.SUCCESS, Optional.empty(), Optional.empty(), Optional.of(dni));
            identityValidationRequestService.changeRequestState(1l, statusChangeDto);
            Optional<IdentityValidationRequest> identityValidationRequest = identityValidationRequestRepository.findById(1l);
            Assert.assertTrue(identityValidationRequest.isPresent());
            Assert.assertEquals(RequestState.SUCCESS,identityValidationRequest.get().getRequestState());
            Assert.assertEquals(dni, identityValidationRequest.get().getDni());
        }catch (InexistentIdentityValidationRequestException ex){
            fail();
        }
    }

    private IdentityValidationRequest getNewRequest(){
        return new IdentityValidationRequest(3999999l,
                "ethr:x9f0",
                "some@some.com",
                "+542222",
                "name",
                "last name",
                RequestState.IN_PROGRESS,
                LocalDate.now());
    }
}