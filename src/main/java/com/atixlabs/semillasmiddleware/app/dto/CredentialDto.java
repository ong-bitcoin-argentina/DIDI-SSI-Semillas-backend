package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CredentialDto {

    private Long id;

    private Long idDidiCredential;

    private Long idHistorical;

    private LocalDateTime dateOfIssue;

    private LocalDateTime dateOfExpiry;

    private String name;

    private Long dniBeneficiary;

    private String creditState;

    public CredentialDto(Long id, Long idDidiCredential, Long idHistorical, LocalDateTime dateOfIssue, LocalDateTime dateOfExpiry, String name, Long dniBeneficiary, String creditState) {
        this.id = id;
        this.idDidiCredential = idDidiCredential;
        this.idHistorical = idHistorical;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiry = dateOfExpiry;
        this.name = name;
        this.dniBeneficiary = dniBeneficiary;
        this.creditState = creditState;
    }


//  tipo credencial -> enum tmb -> string
}
