package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Credential extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiIssueer;

    private Long idDidiReceptor;

    @Column(unique = true)
    private Long idDidiCredential;

    private Long idHistorical;

    private LocalDateTime dateOfIssue;

    private LocalDateTime dateOfExpiry;

    private Long idRelatedCredential;

    @ManyToOne
    private Person beneficiary;

    private String credentialState;


    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", idDidiIssueer=" + idDidiIssueer +
                ", idDidiReceptor=" + idDidiReceptor +
                ", idDidiCredential=" + idDidiCredential +
                ", idHistorical=" + idHistorical +
                ", dateOfIssue=" + dateOfIssue +
                ", dateOfExpiry=" + dateOfExpiry +
                ", idRelatedCredential=" + idRelatedCredential +
                ", beneficiary=" + beneficiary +
                ", credentialState='" + credentialState + '\'' +
                '}';
    }
}
