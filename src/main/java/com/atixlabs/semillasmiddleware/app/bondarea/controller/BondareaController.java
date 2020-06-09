package com.atixlabs.semillasmiddleware.app.bondarea.controller;

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
    private ProcessControlService processControlService;

    @Autowired
    public BondareaController(BondareaService bondareaService, ProcessControlService processControlService) {
        this.bondareaService = bondareaService;
        this.processControlService = processControlService;
    }


    @PostMapping("/synchronize")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Loan>> synchronizeBondareaLoans() throws InvalidProcessException {
        try {
            bondareaService.synchronizeLoans();
        }
        catch (BondareaSyncroException ex){
            log.error("Could not synchronized data from Bondarea ! "+ ex.getMessage());
            processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA.getCode(), ProcessControlStatusCodes.FAIL.getCode());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (InvalidProcessException ex){
            log.error("Could not get the process ! "+ ex.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (InvalidExpiredConfigurationException ex) {
            log.error(ex.getMessage());
            processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA.getCode(), ProcessControlStatusCodes.FAIL.getCode());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * MOCK PURPOSE:
     * Synchronize new loans mock. It can be used to create loans for the 1st time.
     * Then to test the creation of credit credential and benefits credentials
     *
     */
    @PostMapping("/synchronizeMock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Loan>> synchronizeBondareaLoansMock1(@RequestBody List<LoanDto> loansJson)  {
        log.info("BONDAREA - GET LOANS MOCK");
        List<Loan> loans;
        try {
          //  LocalDate todayPlusOne = DateUtil.getLocalDateWithFormat("dd/MM/yyyy").plusDays(1); //get the loans with the actual day +1
            //loansDto = bondareaService.getLoansMock("","", todayPlusOne.toString());
            loans = loansJson.stream().map(loanDto -> new Loan(loanDto)).collect(Collectors.toList());
            bondareaService.createAndUpdateLoans(loans);
            bondareaService.setPendingLoansFinalStatusMock();
        }
        catch (Exception ex){
            log.error("Could not synchronized data from Bondarea !"+ ex.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            // check credits for defaults
            bondareaService.checkCreditsForDefault();
        }
        catch (InvalidExpiredConfigurationException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(loans, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>(loans, HttpStatus.OK);
    }


    }
