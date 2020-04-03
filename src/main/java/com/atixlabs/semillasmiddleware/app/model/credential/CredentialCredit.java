package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialCredit extends Credential {

    private Long idCredit;

    private String creditName;

    private Long idGroup;

    private String groupName;

    private String rol;

    private String currentCycle;

    private String creditState;

    private Double amount;

    private Long dniBeneficiary;


}
