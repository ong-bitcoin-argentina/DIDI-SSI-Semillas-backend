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

    private PersonRepository personRepository;


    @Autowired
    public LoanService(LoanRepository loanRepository, PersonRepository personRepository){
        this.loanRepository = loanRepository;
        this.personRepository = personRepository;
    }


    public List<Loan> findLoansWithoutCredential(){
        List<Loan> newLoans = loanRepository.findAllByHasCredentialFalse();

        return newLoans;
    }

    public void validateLoanBeneficiary(Loan loan){
        //TODO beneficiarieSSSS
        Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());
        if(opBeneficiary.isPresent()){
            if(opBeneficiary.get().getIdDidi() != null){
                create credit

            }
            else{
                create "pre credential" on state pending didi
            }
            loan.setHasCredential(true);

        }else{
            //throw error -> person should have been created before...
        }
    }
}
