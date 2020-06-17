package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LoanService {


    private LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }


    public List<Loan> findLoansWithoutCredential(){
        List<Loan> newLoans = loanRepository.findAllByHasCredentialFalse();

        return newLoans;
    }


    public List<Loan> findLastLoansModified(LocalDateTime credentialProcessLastTime, List<String> statuses) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        List<Loan> modifiedCredits = loanRepository.findAllByUpdateTimeGreaterThanAndStatusIn(credentialProcessLastTime, statuses);
        return modifiedCredits;
    }


}
