package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanService {


    private LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, PersonRepository personRepository){
        this.loanRepository = loanRepository;
    }


    public List<Loan> findLoansWithoutCredential(){
        List<Loan> newLoans = loanRepository.findAllByHasCredentialFalse();

        return newLoans;
    }


}
