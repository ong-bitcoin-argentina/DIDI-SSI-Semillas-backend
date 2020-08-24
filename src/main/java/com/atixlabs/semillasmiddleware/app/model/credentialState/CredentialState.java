package com.atixlabs.semillasmiddleware.app.model.credentialState;

import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
@ToString
//From enum CredentialStatesCodes
public class CredentialState extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String stateName;

    public CredentialState(String state){
        this.stateName = state;
    }

    public  CredentialState(){};


}
