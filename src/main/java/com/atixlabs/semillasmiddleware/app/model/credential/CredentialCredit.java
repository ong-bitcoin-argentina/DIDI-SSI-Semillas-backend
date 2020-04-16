package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Table(name = "credential_credit")
@Entity
@PrimaryKeyJoinColumn(name = "id")
//@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialCredit extends Credential {



    //    private Long idCredit;
    private String creditName;

    private Long idGroup;

    private String groupName;

    private String rol;

    private String currentCycle;

    private String creditState;

    private Double amount;

    private String beneficiaryDocumentType;

    private Long beneficiaryDocumentNumber;


    @Override
    public String toString() {
        return "CredentialCredit{" +
  //              "idCredit=" + idCredit +
                ", creditName='" + creditName + '\'' +
                ", idGroup=" + idGroup +
                ", groupName='" + groupName + '\'' +
                ", rol='" + rol + '\'' +
                ", currentCycle='" + currentCycle + '\'' +
                ", creditState='" + creditState + '\'' +
                ", amount=" + amount +
                ", dniBeneficiary=" + beneficiaryDocumentNumber +
                "} " + super.toString();
    }
}
