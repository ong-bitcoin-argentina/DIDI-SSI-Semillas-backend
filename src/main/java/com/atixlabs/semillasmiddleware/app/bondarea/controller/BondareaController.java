package com.atixlabs.semillasmiddleware.app.bondarea.controller;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.LoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.BondareaLoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import com.atixlabs.semillasmiddleware.app.exceptions.NoExpiredConfigurationExists;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<Loan>> synchronizeBondareaLoans()  {
        log.info("BONDAREA - GET LOANS");
        List<BondareaLoanDto> loansDto;
        List<Loan> loans;
        try {
            LocalDate todayPlusOne = DateUtil.getLocalDateWithFormat("dd/MM/yyyy").plusDays(1); //get the loans with the actual day +1
            loansDto = bondareaService.getLoans(BondareaLoanStatusCodes.ACTIVE.getCode(), "", todayPlusOne.toString());
            loans = loansDto.stream().map(loanDto -> new Loan(loanDto)).collect(Collectors.toList());
            bondareaService.updateExistingLoans(loans);
            bondareaService.setPendingLoansFinalStatus();
        }
        catch (Exception ex){
            log.error("Could not synchronized data from Bondarea !"+ ex.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(loans, HttpStatus.OK);
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
        log.info("BONDAREA - GET LOANS");
        List<Loan> loans;
        try {
          //  LocalDate todayPlusOne = DateUtil.getLocalDateWithFormat("dd/MM/yyyy").plusDays(1); //get the loans with the actual day +1
            //loansDto = bondareaService.getLoansMock("","", todayPlusOne.toString());
            loans = loansJson.stream().map(loanDto -> new Loan(loanDto)).collect(Collectors.toList());
            bondareaService.updateExistingLoans(loans);
            bondareaService.setPendingLoansFinalStatusMock();
        }
        catch (Exception ex){
            log.error("Could not synchronized data from Bondarea !"+ ex.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        try {
            // check credits
            bondareaService.checkCreditsForDefault();
        }
        catch (NoExpiredConfigurationExists ex) {
            log.error(ex.getMessage());
        }


        return new ResponseEntity<>(loans, HttpStatus.OK);
    }




    }
