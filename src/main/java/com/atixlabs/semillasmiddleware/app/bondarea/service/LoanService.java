package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
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


    public List<Loan> findActiveAndOkLoansWithoutCredential(){
        return loanRepository.findAllByHasCredentialAndStatusAndState(false, LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.OK.getCode());
    }


    public List<Loan> findLastLoansModified(LocalDateTime credentialProcessLastTime, List<String> statuses) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusIn(credentialProcessLastTime, statuses);
    }

    public List<Loan> findLastLoansModifiedInDefault(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusAndState(credentialProcessLastTime, LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.DEFAULT.getCode());
    }

    public List<Loan> findLastLoansModifiedActive(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusAndState(credentialProcessLastTime, LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.OK.getCode());
    }

    public List<Loan> findLastLoansModifiedActiveWithCredential(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusAndStateAndHasCredential(credentialProcessLastTime, LoanStatusCodes.ACTIVE.getCode(), LoanStateCodes.OK.getCode(), true);
    }

    public List<Loan> findLastLoansModifiedFinalized(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusAndState(credentialProcessLastTime, LoanStatusCodes.FINALIZED.getCode(), LoanStateCodes.OK.getCode());
    }

    public List<Loan> findLastLoansModifiedCancelled(LocalDateTime credentialProcessLastTime) {
        //get the modified credits: the credits that modified in the last sync and (if exits) the credits that had been modified before that last sync.
        return loanRepository.findAllByUpdateTimeGreaterThanAndStatusAndState(credentialProcessLastTime, LoanStatusCodes.CANCELLED.getCode(), LoanStateCodes.OK.getCode());
    }

    public List<Loan> findOthersLoansActivesForHolder(Loan loan){
        return loanRepository.findAllByDniPersonAndStatusAndIdBondareaLoanNot(loan.getDniPerson(), LoanStatusCodes.ACTIVE.getCode(),loan.getIdBondareaLoan());
    }

}
