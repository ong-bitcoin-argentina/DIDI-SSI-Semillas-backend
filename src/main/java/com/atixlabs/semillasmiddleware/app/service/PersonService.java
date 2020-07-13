package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    private PersonRepository personRepository;
    private CredentialIdentityRepository credentialIdentityRepository;

    public PersonService(CredentialIdentityRepository credentialIdentityRepository, PersonRepository personRepository) {
        this.credentialIdentityRepository = credentialIdentityRepository;
        this.personRepository = personRepository;
    }

    public Optional<List<Person>> findFamilyForHolder(Person holder) {

        List<Person> familiy = credentialIdentityRepository.findDistinctBeneficiaryFamilyByHolder(holder);
        return familiy != null ? Optional.of(familiy) : Optional.empty();

    }

    public Optional<Person>  findByDocumentNumber(Long dniNumber) {

            return personRepository.findByDocumentNumber(dniNumber);
    }
}
