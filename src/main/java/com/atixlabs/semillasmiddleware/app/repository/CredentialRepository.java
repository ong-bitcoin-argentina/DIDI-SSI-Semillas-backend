package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.CredentialState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> , CredentialRepositoryCustom{

    ArrayList<Credential> findByCredentialStateAndBeneficiaryDniIn(CredentialState credentialState, ArrayList<Long> dniList);

    ArrayList<Credential> findByCredentialCategoryAndCredentialState(String code, CredentialState credentialStatePending);

    List<Credential> findByBeneficiaryDniAndCredentialCategoryAndCreditHolderDniAndCredentialStateIn(Long beneficiaryDni, String credentialCategoryCode, Long creditHolderDni, List<CredentialState> credentialStateActivePending);

    List<Credential> findByBeneficiaryDniAndCredentialCategoryAndCredentialStateIn(Long beneficiaryDni, String credentialCategoryCode, List<CredentialState> credentialStateActivePending);

    ArrayList<Credential> findByCreditHolderDniIn(List<Long> dniList);

    ArrayList<Credential> findByBeneficiaryDniIn(List<Long> dniList);

    List<Credential> findAll(Specification<Credential> specification);

}
