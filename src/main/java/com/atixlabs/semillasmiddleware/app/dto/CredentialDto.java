package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class CredentialDto {

    private Long id;

    private Long idDidiCredential;

    private Date dateOfIssue;

    private Date dateOfExpiry;

    private String name;

    private Long dniBeneficiary;

    private String creditState;

    //tipo de credencial ?

    public CredentialDto(Long id, Long idDidiCredential, Date dateOfIssue, Date dateOfExpiry, String name, Long dniBeneficiary, String creditState) {
        this.id = id;
        this.idDidiCredential = idDidiCredential;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiry = dateOfExpiry;
        this.name = name;
        this.dniBeneficiary = dniBeneficiary;
        this.creditState = creditState;
    }

    public CredentialDto(CredentialCredit credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiCredential();
        this.dateOfIssue = credential.getDateOfIssue();
        this.dateOfExpiry = credential.getDateOfExpiry();
        this.name = credential.getBeneficiary().getName();
        this.dniBeneficiary = credential.getBeneficiary().getDocumentNumber();
        this.creditState = credential.getCreditState();
    }


//  tipo credencial -> enum tmb -> string
}
