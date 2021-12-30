package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialRelationHolderType;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.excel.Form;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

import static com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType.BENEFICIARY;
import static com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType.CHILD;
import static com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType.OTHER_KINSMAN;
import static com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType.SPOUSE;


@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialIdentity extends Credential {

    private String relationWithCreditHolder;
    private String beneficiaryGender;
    private LocalDate beneficiaryBirthDate;

    public CredentialIdentity(CredentialIdentity credentialIdentity){
        super(credentialIdentity);

        this.relationWithCreditHolder = credentialIdentity.relationWithCreditHolder;
        this.beneficiaryGender = credentialIdentity.beneficiaryGender;
        this.beneficiaryBirthDate = credentialIdentity.beneficiaryBirthDate;

    }

    public CredentialIdentity(CredentialIdentity credentialIdentity, String relationWithCreditHolder){
        super(credentialIdentity);

        this.relationWithCreditHolder = relationWithCreditHolder;
        this.beneficiaryGender = credentialIdentity.beneficiaryGender;
        this.beneficiaryBirthDate = credentialIdentity.beneficiaryBirthDate;

    }

    public CredentialIdentity(Person beneficiary, PersonType personType) {
        this.setBeneficiary(beneficiary);
        this.setBeneficiaryDni(beneficiary.getDocumentNumber());
        this.setBeneficiaryFirstName(beneficiary.getFirstName());
        this.setBeneficiaryLastName(beneficiary.getLastName());
        this.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());
        this.setBeneficiaryGender(beneficiary.getGender());
        this.setBeneficiaryBirthDate(beneficiary.getBirthDate());
        if (personType.equals(BENEFICIARY)) {
            this.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
            this.setRelationWithCreditHolder(CredentialRelationHolderType.HOLDER.getCode());
        } else if (personType.equals(SPOUSE) || personType.equals(CHILD) || personType.equals(OTHER_KINSMAN)) {
            this.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILY.getCode());
            this.setRelationWithCreditHolder(CredentialRelationHolderType.KINSMAN.getCode());
        }
    }
}
