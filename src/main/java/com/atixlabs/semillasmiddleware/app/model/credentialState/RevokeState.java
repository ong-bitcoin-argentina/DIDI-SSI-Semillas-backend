package com.atixlabs.semillasmiddleware.app.model.credentialState;

import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class RevokeState extends AuditableEntity { // how to use with credentialState

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCredential;

    //private String stateName = CredentialStatesCodes.CREDENTIAL_REVOKE.getCode();

    private String reason; //todo enum reasons

    private LocalDateTime dateOfRevocation;

}
