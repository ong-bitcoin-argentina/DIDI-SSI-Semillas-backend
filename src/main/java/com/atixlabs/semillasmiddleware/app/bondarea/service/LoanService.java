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
    private ProcessControlService processControlService;

    @Autowired
    public LoanService(LoanRepository loanRepository, PersonRepository personRepository, ProcessControlService processControlService){
        this.loanRepository = loanRepository;
        this.processControlService = processControlService;
    }


    public List<Loan> findLoansWithoutCredential(){
        List<Loan> newLoans = loanRepository.findAllByHasCredentialFalse();

        return newLoans;
    }

    public List<Loan> findLoansWithCredential(){
        List<Loan> newLoans = loanRepository.findAllByHasCredentialTrue();

        return newLoans;
    }

    public List<Loan> findLastLoansModified(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        List<Loan> modifiedCredits = loanRepository.findAllByUpdateTimeGreaterThanAndStatus(credentialProcessLastTime, LoanStatusCodes.ACTIVE.getCode());
        return modifiedCredits;
    }


}
