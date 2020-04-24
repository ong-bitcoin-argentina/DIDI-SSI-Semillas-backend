package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialCredit extends Credential {

    private String idBondareaCredit;

    private LocalDate creationDate;

    private String creditType; //TODO  Credito semilla / escolar / estacional / individual / de motos/de min. desarrolo social

    private String idGroup;

    private String currentCycle;

    private int totalCycles;

    private int amountExpiredCycles;

    private String creditState;

    private Float expiredAmount; //TODO ? can have money in favour

    private Long dniBeneficiary; // is in main activity -> beneficiary.dni



    /*public CredentialCredit(PersonCategory personCategory){
        /*credentialCredit.setDateOfIssue(LocalDateTime.now());
        credentialCredit.setDateOfExpiry(LocalDateTime.now().plusDays(14L));
        credentialCredit.setCurrentCycle("imported-from-excel");
        credentialCredit.setCreditState("pre-credential");
        credentialCredit.setBeneficiaryDocumentType(personCategory.getIdType());
        credentialCredit.setBeneficiaryDocumentNumber(personCategory.getIdNumber());
        this.setDateOfIssue(LocalDateTime.now());
        this.setDateOfExpiry(LocalDateTime.now().plusDays(14L));
        this.setCurrentCycle("imported-from-excel");
        this.setCreditState("pre-credential");
        this.setDniBeneficiary(personCategory.getIdNumber());

    }*/


}
