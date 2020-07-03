package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    private CredentialIdentityRepository credentialIdentityRepository;

    public PersonService(CredentialIdentityRepository credentialIdentityRepository){
        this.credentialIdentityRepository = credentialIdentityRepository;
    }

    public Optional<List<Person>> findFamilyForHolder(Person holder) {

        List<Person> familiy = credentialIdentityRepository.findDistinctBeneficiaryFamilyByHolder(holder);
        return familiy != null ? Optional.of(familiy) : Optional.empty();

    }
}
