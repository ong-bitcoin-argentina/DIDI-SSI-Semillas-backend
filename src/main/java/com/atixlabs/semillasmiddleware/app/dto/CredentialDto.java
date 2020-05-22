package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;

import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@ToString
public class CredentialDto {

    private Long id;

    private String idDidiCredential;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfIssue;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    //private LocalDateTime dateOfExpiry;

    private String name;

    private Long dniBeneficiary;

    private String credentialState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime lastUpdate;

    private String credentialType;

    private String credentialStatus;

    private boolean isRevocable;


    public CredentialDto(Credential credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiCredential();
        this.dateOfIssue = credential.getDateOfIssue();
        //this.dateOfExpiry = credential.getDateOfRevocation();
        this.name = credential.getBeneficiaryFirstName() +" "+ credential.getBeneficiaryLastName();
        this.dniBeneficiary = credential.getBeneficiaryDni();
        this.credentialState = credential.getCredentialState().getStateName();
        this.lastUpdate = credential.getUpdated();
        this.credentialType = credential.getCredentialDescription();
        this.isRevocable = credential.isManuallyRevocable();
    }

}
