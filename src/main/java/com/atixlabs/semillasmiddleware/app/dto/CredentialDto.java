package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@ToString
public class CredentialDto {

    //If we change a field name here, we must change it also in front !!!

    private Long id;

    private String idDidiCredential; //this is the didi of the person

    private String name;
    private String holderName;

    private Long creditHolderDni;
    private Long dniBeneficiary;

    private String credentialState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfIssue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdate;

    private String credentialType;

    private Boolean isRevocable;

    private LocalDateTime dateOfRevocation;
    private String revocationReason;
    private  BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;


    //beneficiary
    private String beneficiaryType;

    //credit
    private String idBondareaCredit;
    private LocalDate creationDate;
    private String creditType;
    private String idGroup;
    private String currentCycle;
    private Integer totalCycles;
    private Integer amountExpiredCycles;
    private String creditState;
    private LocalDate finishDate;
    private String expiredAmount;
    private Integer currentCycleNumber;
    private LocalDate expirationDate;

    //dwelling
    private String dwellingType;
    private String dwellingAddress;
    private String possessionType;
    private String district;
    private String generalConditions;
    private String lightInstallation;
    private String neighborhoodType;
    private Boolean gas;
    private Boolean carafe;
    private Boolean water;
    private Boolean waterWell;
    private String address;
    private String location;
    private String neighborhood;

    //entrepreneurship
    private String entrepreneurshipType;

    private Integer startActivity;
    private String mainActivity;
    private String entrepreneurshipName;
    private String entrepreneurshipAddress;

    //Sancor
    private Long certificateNumber;
    private Long ref;
    private Long policyNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endActivity;


    //identity
    private String relationWithCreditHolder;
    private String beneficiaryGender;
    private LocalDate beneficiaryBirthDate;

    public CredentialDto() {

    }

    public CredentialDto(Credential credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiReceptor();
        this.name = credential.getBeneficiaryFirstName() +" "+ credential.getBeneficiaryLastName();
        this.dniBeneficiary = credential.getBeneficiaryDni();
        this.creditHolderDni = credential.getCreditHolderDni();
        this.credentialState = credential.getCredentialState().getStateName();
        this.lastUpdate = credential.getUpdated();
        this.credentialType = credential.getCredentialDescription();
        this.isRevocable = credential.isManuallyRevocable();
        this.dateOfIssue = credential.getDateOfIssue();
    }



    public void baseCredentialDto(Credential credential) {
        this.id = credential.getId();
        this.idDidiCredential = credential.getIdDidiReceptor();
        this.name = credential.getBeneficiaryFirstName() +" "+ credential.getBeneficiaryLastName();
        this.dniBeneficiary = credential.getBeneficiaryDni();
        this.creditHolderDni = credential.getCreditHolderDni();
        this.credentialState = credential.getCredentialState().getStateName();
        this.lastUpdate = credential.getUpdated();
        this.credentialType = credential.getCredentialDescription();
        this.dateOfRevocation = credential.getDateOfRevocation();
        this.isRevocable = credential.isManuallyRevocable();

        if(credential.getRevocationReason() !=null)
            this.revocationReason = credential.getRevocationReason().getReason();
    }

    public static CredentialDto constructBasedOnCredentialType(Credential credential){
        if (credential.getCredentialCategory() != null) {
            switch (credential.getCredentialCategory()){
                case "Beneficio Semillas":
                    return new CredentialDto((CredentialBenefits) credential);

                case "Crediticia":
                    return new CredentialDto((CredentialCredit) credential);

                case "Identidad":
                    return new CredentialDto((CredentialIdentity) credential);

                case "Vivienda":
                    return new CredentialDto((CredentialDwelling) credential);

                case "Emprendimiento":
                    return new CredentialDto((CredentialEntrepreneurship) credential);

                case "Sancor Salud":
                    return new CredentialDto((CredentialBenefitSancor) credential);

                default:
                    return new CredentialDto();
            }
        }
        return new CredentialDto();
    }


    public CredentialDto(CredentialBenefits benefits){
        this.baseCredentialDto(benefits);
        this.beneficiaryType = benefits.getBeneficiaryType();
    }

    public CredentialDto(CredentialCredit credit){
        this.baseCredentialDto(credit);
        this.idBondareaCredit = credit.getIdBondareaCredit();
        this.creationDate = credit.getCreationDate();
        this.creditType = credit.getCreditType();
        this.idGroup = credit.getIdGroup();
        this.currentCycle = credit.getCurrentCycle();
        this.totalCycles = credit.getTotalCycles();
        this.amountExpiredCycles = credit.getAmountExpiredCycles();
        this.creditState = credit.getCreditStatus();
        this.finishDate = credit.getFinishDate();
        this.expiredAmount = credit.getExpiredAmount().toString();
        this.currentCycleNumber = credit.getCurrentCycleNumber();
        this.expirationDate = credit.getExpirationDate();
        this.startDate = credit.getStartDate();
        this.amount = credit.getAmount();
    }

    public CredentialDto(CredentialDwelling dwelling){
        this.baseCredentialDto(dwelling);
        this.dwellingType = dwelling.getDwellingType();
        this.dwellingAddress = dwelling.getDwellingAddress();
        this.possessionType = dwelling.getPossessionType();
        this.district = dwelling.getDistrict();
        //nuevos campos
        this.generalConditions = dwelling.getGeneralConditions();
        this.lightInstallation = dwelling.getLightInstallation();
        this.neighborhoodType = dwelling.getNeighborhoodType();
        this.gas = dwelling.getGas();
        this.carafe = dwelling.getCarafe();
        this.water = dwelling.getWater();
        this.address = dwelling.getAddress();
        this.location = dwelling.getLocation();
        this.neighborhood = dwelling.getNeighborhood();
        this.waterWell = dwelling.getWatterWell();
    }

    public CredentialDto(CredentialEntrepreneurship entrepreneurship){
        this.baseCredentialDto(entrepreneurship);
        this.entrepreneurshipType = entrepreneurship.getEntrepreneurshipType();
        this.startActivity = entrepreneurship.getStartActivity();
        this.mainActivity = entrepreneurship.getMainActivity();
        this.entrepreneurshipName = entrepreneurship.getEntrepreneurshipName();
        this.entrepreneurshipAddress = entrepreneurship.getEntrepreneurshipAddress();
        this.endActivity = entrepreneurship.getEndActivity();
    }

    public CredentialDto(CredentialIdentity identity) {
        this.baseCredentialDto(identity);
        this.holderName = identity.getCreditHolderFirstName() + " " + identity.getCreditHolderLastName();
        this.relationWithCreditHolder = identity.getRelationWithCreditHolder();
        this.beneficiaryGender = identity.getBeneficiaryGender();
        this.beneficiaryBirthDate = identity.getBeneficiaryBirthDate();
    }

    public CredentialDto(CredentialBenefitSancor sancor) {
        this.baseCredentialDto(sancor);
        this.certificateNumber = sancor.getCertificateNumber();
        this.ref = sancor.getRef();
        this.policyNumber = sancor.getPolicyNumber();

    }

}
