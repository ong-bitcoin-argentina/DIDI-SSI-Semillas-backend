package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialCreditRepository extends JpaRepository<CredentialCredit, Long>, CredentialCreditRepositoryCustom {

    Optional<CredentialCredit> findByIdBondareaCredit(String idBondarea);

    List<CredentialCredit> findByIdBondareaCreditAndCreditStateOrderByUpdatedDesc(String idBondarea, String creditState);

    Optional<CredentialCredit> findTopByIdBondareaCreditOrderByIdDesc(String idBondarea);

    Optional<CredentialCredit> findFirstByIdBondareaCreditOrderByDateOfIssueDesc(String idBondarea);

    List<CredentialCredit> findByCreditHolderDniAndCredentialStateIn(Long holderDni, List<CredentialState> credentialStates);

    List<CredentialCredit> findByCreditHolderDniAndCredentialState(Long holderDni, CredentialState credentialStates);

    List<CredentialCredit> findByCreditHolderDniAndCredentialStateInAndFinishDateIsNull(Long holderDni, List<CredentialState> credentialStates);

    List<CredentialCredit> findByIdGroupAndCredentialStateIn(String idGroup, List<CredentialState> credentialStates);

    List<CredentialCredit> findByIdGroupInAndCreditHolderDniAndCredentialStateIn(List<String> idGroup, Long dni, List<CredentialState> credentialStates);

    List<CredentialCredit> findByCredentialState(CredentialState credentialState);
}
