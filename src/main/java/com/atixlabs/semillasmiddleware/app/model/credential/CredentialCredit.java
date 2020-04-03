package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;


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


    public CredentialCredit(CredentialCredit credential) {
        this.idCredit = credential.idCredit;
        this.creditName = credential.creditName;
        this.idGroup = credential.idGroup;
        this.groupName = credential.groupName;
        this.rol = credential.rol;
        this.currentCycle = credential.currentCycle;
        this.creditState = credential.creditState;
        this.amount = credential.amount;
        this.dniBeneficiary = credential.dniBeneficiary;
    }
}
