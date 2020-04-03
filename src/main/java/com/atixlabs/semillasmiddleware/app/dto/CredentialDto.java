package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CredentialDto {

    private Long id;

    private Long idDidiCredential;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfIssue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfExpiry;

    private String name;

    private Long dniBeneficiary;

    private String credentialState;

    private LocalDateTime lastUpdate;

    //tipo de credencial ?

    public CredentialDto(Long id, Long idDidiCredential, LocalDateTime dateOfIssue, LocalDateTime dateOfExpiry, String name, Long dniBeneficiary, String creditState) {
        this.id = id;
        this.idDidiCredential = idDidiCredential;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiry = dateOfExpiry;
        this.name = name;
        this.dniBeneficiary = dniBeneficiary;
        this.credentialState = creditState;
    }

    public CredentialDto(CredentialCredit credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiCredential();
        this.dateOfIssue = credential.getDateOfIssue();
        this.dateOfExpiry = credential.getDateOfExpiry();
        this.name = credential.getBeneficiary().getName();
        this.dniBeneficiary = credential.getBeneficiary().getDocumentNumber();
        this.credentialState = credential.getCredentialState();
        this.lastUpdate = credential.getUpdated();
    }


//  tipo credencial -> enum tmb -> string
}
