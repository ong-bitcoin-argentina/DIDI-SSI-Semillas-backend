package com.atixlabs.semillasmiddleware.app.bondarea.controller;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoan;
import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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


    @GetMapping("/getLoans")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BondareaLoan>> getLoans()  {
        log.info("BONDAREA - GET LOANS");
        List<BondareaLoan> loans;
        // idAccount not null!!
        try {
            loans = bondareaService.getLoans("12345", "55");
        }
        catch (Exception ex){
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }


    }
