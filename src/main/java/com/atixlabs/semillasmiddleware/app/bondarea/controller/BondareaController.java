package com.atixlabs.semillasmiddleware.app.bondarea.controller;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.exceptions.BondareaSyncroException;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.LoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import com.atixlabs.semillasmiddleware.app.exceptions.InvalidExpiredConfigurationException;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(BondareaController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class BondareaController {

    public static final String URL_MAPPING_CREDENTIAL = "/bondarea";

    private BondareaService bondareaService;

    @Autowired
    public BondareaController(BondareaService bondareaService) {
        this.bondareaService = bondareaService;
    }


    @PostMapping("/synchronize")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> synchronizeBondareaLoans() throws InvalidProcessException {
        boolean result = true;
        try {
            result = bondareaService.synchronizeLoans(null);
        } catch (InvalidProcessException ex) {
            log.error("Could not get the process ! " + ex.getMessage());
            return new ResponseEntity<>("Could not get the process !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>("Error synchronizing and processing data from Bondarea !", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * MOCK PURPOSE:
     * Synchronize new loans mock. It can be used to create loans for the 1st time.
     * Then to test the creation of credit credential and benefits credentials
     *
     */
    @PostMapping("/synchronizeMock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> synchronizeBondareaLoansMock1(@RequestBody List<LoanDto> loansJson) throws InvalidProcessException {
        log.info("BONDAREA - GET LOANS MOCK");
        List<BondareaLoanDto> loans;
        boolean result = true;

        loans = loansJson.stream().map(loanDto -> new BondareaLoanDto(loanDto)).collect(Collectors.toList());
        try {
            result = bondareaService.synchronizeLoans(loans);
        } catch (InvalidProcessException ex) {
            log.error("Could not get the process ! " + ex.getMessage());
            return new ResponseEntity<>("Could not get the process !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>("Error synchronizing and processing data from Bondarea !", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    }
