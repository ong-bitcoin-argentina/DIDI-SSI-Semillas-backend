package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "credential")
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
@ToString
public abstract class Credential extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiIssuer;

    private Long idDidiReceptor;

    @Column(unique = true)
    private Long idDidiCredential;

    private Long idHistorical;

    private LocalDateTime dateOfIssue;

    private LocalDateTime dateOfRevocation;


    @ManyToOne
    private Person creditHolder;
    private Long creditHolderDni;
    private String creditHolderName;

    @ManyToOne
    private Person beneficiary;
    private Long beneficiaryDni;
    private String beneficiaryName;

    @ManyToOne
    private CredentialState credentialState;

    private String credentialDescription;

    private String credentialCategory;
}
