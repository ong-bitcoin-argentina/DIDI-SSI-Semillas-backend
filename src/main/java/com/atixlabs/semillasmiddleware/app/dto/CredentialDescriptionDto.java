package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Slf4j
@ToString
public class CredentialDescriptionDto {

    private Long id;

    private String idDidiCredential;

    private String name;

    private Long creditHolderDni;
    private Long beneficiaryDni;

    private String credentialState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdate;

    private String credentialType;

    private LocalDateTime dateOfRevocation;
    private String revocationReason;

    //beneficiary
    private String beneficiaryType;

    //credit
    private String idBondareaCredit;
    private LocalDate creationDate;
    private String creditType;
    private String idGroup;
    private String currentCycle;
    private int totalCycles;
    private int amountExpiredCycles;
    private String creditState;
    private LocalDate finishDate;
    private Float expiredAmount;

    //dwelling
    private String dwellingType;
    private String dwellingAddress;
    private String possessionType;

    //entrepreneurship
    private String entrepreneurshipType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startActivity;
    private String mainActivity;
    private String entrepreneurshipName;
    private String entrepreneurshipAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endActivity;


    //identity
    private String relationWithCreditHolder;
    private String beneficiaryGender;
    private LocalDate beneficiaryBirthDate;

    public CredentialDescriptionDto() {

    }

    public CredentialDescriptionDto constructBasedOnCredentialType(Credential credential){
        if (credential.getCredentialCategory() != null) {
            if (CredentialCategoriesCodes.BENEFIT.getCode().equals(credential.getCredentialCategory())) {
                    return new CredentialDescriptionDto((CredentialBenefits) credential);
            } else {
                if (CredentialCategoriesCodes.CREDIT.getCode().equals(credential.getCredentialCategory())) {
                        return new CredentialDescriptionDto((CredentialCredit) credential);
                } else {
                    if (CredentialCategoriesCodes.IDENTITY.getCode().equals(credential.getCredentialCategory())) {
                            return new CredentialDescriptionDto((CredentialIdentity) credential);
                    } else {
                        if (CredentialCategoriesCodes.DWELLING.getCode().equals(credential.getCredentialCategory())) {
                                return new CredentialDescriptionDto((CredentialDwelling) credential);
                        } else {
                            if (CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode().equals(credential.getCredentialCategory())) {
                                    return new CredentialDescriptionDto((CredentialEntrepreneurship) credential);
                            } else
                                return new CredentialDescriptionDto();
                        }
                    }
                }
            }
        }
        return new CredentialDescriptionDto();
    }


    private void baseConstructor(Credential credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiCredential();
        this.name = credential.getBeneficiaryFirstName() + " " + credential.getBeneficiaryLastName();
        this.creditHolderDni = credential.getCreditHolderDni();
        this.beneficiaryDni = credential.getBeneficiaryDni();
        this.credentialState = credential.getCredentialState().getStateName();
        this.lastUpdate = credential.getUpdated();
        this.credentialType = credential.getCredentialDescription();
        this.dateOfRevocation = credential.getDateOfRevocation();
        if(credential.getRevocationReason() !=null)
            this.revocationReason = credential.getRevocationReason().getReason();
    }

    public CredentialDescriptionDto(CredentialBenefits benefits){
        this.baseConstructor(benefits);
        this.beneficiaryType = benefits.getBeneficiaryType();
    }

    public CredentialDescriptionDto(CredentialCredit credit){
        this.baseConstructor(credit);
        this.idBondareaCredit = credit.getIdBondareaCredit();
        this.creationDate = credit.getCreationDate();
        this.creditType = credit.getCreditType();
        this.idGroup = credit.getIdGroup();
        this.currentCycle = credit.getCurrentCycle();
        this.totalCycles = credit.getTotalCycles();
        this.amountExpiredCycles = credit.getAmountExpiredCycles();
        this.creditState = credit.getCreditState();
        this.finishDate = credit.getFinishDate();
        this.expiredAmount = credit.getExpiredAmount();
    }

    public CredentialDescriptionDto(CredentialDwelling dwelling){
        this.baseConstructor(dwelling);
        this.dwellingType = dwelling.getDwellingType();
        this.dwellingAddress = dwelling.getDwellingAddress();
        this.possessionType = dwelling.getPossessionType();
    }

    public CredentialDescriptionDto(CredentialEntrepreneurship entrepreneurship){
        this.baseConstructor(entrepreneurship);
        this.entrepreneurshipType = entrepreneurship.getEntrepreneurshipType();
        this.startActivity = entrepreneurship.getStartActivity();
        this.mainActivity = entrepreneurship.getMainActivity();
        this.entrepreneurshipName = entrepreneurship.getEntrepreneurshipName();
        this.entrepreneurshipAddress = entrepreneurship.getEntrepreneurshipAddress();
        this.endActivity = entrepreneurship.getEndActivity();
    }

    public CredentialDescriptionDto(CredentialIdentity identity) {
        this.baseConstructor(identity);
        this.relationWithCreditHolder = identity.getRelationWithCreditHolder();
        this.beneficiaryGender = identity.getBeneficiaryGender();
        this.beneficiaryBirthDate = identity.getBeneficiaryBirthDate();
    }

}
