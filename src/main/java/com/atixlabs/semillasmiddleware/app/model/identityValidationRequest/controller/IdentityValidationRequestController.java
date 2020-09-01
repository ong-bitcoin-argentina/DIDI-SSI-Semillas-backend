package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.controller;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RejectReason;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationFilter;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.StatusChangeDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.exceptions.InexistentIdentityValidationRequestException;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service.IdentityValidationRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(IdentityValidationRequestController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class IdentityValidationRequestController {

    public static final String URL_MAPPING = "/identityValidationRequests";

    @Autowired
    public IdentityValidationRequestController(IdentityValidationRequestService identityValidationRequestService){
        this.identityValidationRequestService = identityValidationRequestService;
    }

    private IdentityValidationRequestService identityValidationRequestService;

    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody @Valid IdentityValidationRequestDto identityValidationRequestDto){
        identityValidationRequestService.create(identityValidationRequestDto);
        return ResponseEntity.accepted().body("created.");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<IdentityValidationRequest> findAllRequests(@RequestParam @Min(0) int page,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dateFrom,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dateTo,
                                                           @RequestParam Optional<String> criteriaQuery,  //name and surname  dni
                                                           @RequestParam Optional<RequestState> requestState){

        IdentityValidationFilter identityValidationFilter = new IdentityValidationFilter(dateFrom, dateTo, criteriaQuery, requestState);
        return identityValidationRequestService.findAll(page, identityValidationFilter);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String> updateRequest(@PathVariable @Min(1) Long id, @RequestBody @Valid StatusChangeDto statusChangeDto){

        if (!statusChangeDto.getRejectReason().isPresent() && RequestState.valueOf(statusChangeDto.getRequestState()).equals(RequestState.FAILURE))
            return ResponseEntity.badRequest().body("You must specify a rejection reason");

        try{
            identityValidationRequestService.changeRequestState(id, statusChangeDto);
        }catch (InexistentIdentityValidationRequestException iivr){
            return ResponseEntity.badRequest().body("An identity validation request corresponding with provided id does not exist");
        }

        return ResponseEntity.ok().body("ok.");

    }
}
