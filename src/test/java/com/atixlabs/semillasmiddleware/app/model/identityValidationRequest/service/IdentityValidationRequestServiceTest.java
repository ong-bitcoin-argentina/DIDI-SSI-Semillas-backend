package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.controller.IdentityValidationRequestController;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.exceptions.InexistentIdentityValidationRequestException;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository.IdentityValidationRequestRepository;
import com.atixlabs.semillasmiddleware.app.service.MailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class IdentityValidationRequestServiceTest {

    private IdentityValidationRequestService identityValidationRequestService;

    @Autowired
    private IdentityValidationRequestRepository identityValidationRequestRepository;

    @Mock
    ShareStateChangeService shareStateChangeService;

    //todo: try to change this to use before so it is called once in this test class
    @BeforeEach
    public void setUp() {
        identityValidationRequestService = new IdentityValidationRequestService(identityValidationRequestRepository, shareStateChangeService);
    }

    @Test
    public void whenChangingStateOfInexistentExpectToThrowInexistentRequestException() {
        identityValidationRequestRepository.save(getNewRequest());
        identityValidationRequestService.findById(1l).orElseGet(() -> fail());
        assertThrows(InexistentIdentityValidationRequestException.class, () -> identityValidationRequestService.changeRequestState(Long.MAX_VALUE, RequestState.FAILURE, Optional.of("reason")));
    }

    @Test
    public void whenChangingStateOfValidRequestExpectToBeSaved() {
        identityValidationRequestRepository.save(getNewRequest());
        identityValidationRequestService.findById(1l).orElseGet(() -> fail());
        try {
            identityValidationRequestService.changeRequestState(1l, RequestState.SUCCESS, Optional.empty());
            Assert.assertEquals(RequestState.SUCCESS,identityValidationRequestRepository.findById(1l).get().getRequestState());
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
                LocalDate.now(),
                "");
    }
}