package com.atixlabs.semillasmiddleware.app.repository;


import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class CredentialServiceCustomImpl implements CredentialServiceCustom {

    @Autowired
    CredentialRepository credentialRepository;

    @PersistenceContext
    protected EntityManager em;



    @Override
    public List<Credential> findCredentialsWithFilter(String credentialType, String name, String dniBeneficiary, String idDidiCredential, String dateOfExpiry, String dateOfIssue, String credentialState) {
        //DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        //LocalDateTime dateTime = LocalDateTime.parse(dateOfIssue, formatter);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Credential> cq = cb.createQuery(Credential.class);

        Root<Credential> credential = cq.from(Credential.class);
        List<Predicate> predicates = new ArrayList<>();

        if (credentialType != null) {
            predicates.add(cb.equal(credential.get("credentialType"), credentialType));
        }

       /* if (name != null) {
            predicates.add(cb.equal(credential.get("name"), name));
        }*/
        //TODO JOIN con person dniBeneficiary y name


        if (idDidiCredential != null) {
            predicates.add(cb.equal(credential.get("idDidiCredential"), idDidiCredential));
        }

        if (dateOfExpiry != null) {
            predicates.add(cb.equal(credential.get("dateOfExpiry"), dateOfExpiry));
        }

        if (dateOfIssue != null) { //TODO: busqueda por fecha sin horario dentro del timestamp (formato iso)
            predicates.add(cb.equal(credential.get("dateOfIssue"), dateOfIssue));
        }

        if (credentialState != null) {
            predicates.add(cb.equal(credential.get("credentialState"), credentialState));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
