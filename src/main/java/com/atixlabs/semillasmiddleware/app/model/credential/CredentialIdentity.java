package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatusCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.sun.xml.bind.v2.TODO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialIdentity extends Credential {

    private Long dniBeneficiary;

    private String nameBeneficiary;

    private Long dniCreditHolder;

    private String nameCreditHolder;

    //GENDER ? BirthDate ?

    public CredentialIdentity(PersonCategory personCategory){

        this.setDniCreditHolder(personCategory.getIdNumber());
        this.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());
        this.setCredentialDescription("Identidad - Titular");
        this.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_BONDAREA.getCode());

        //todo: revisar como instanciar el objeto CredentialState para guardar en base.
        //this.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));

        this.dniBeneficiary = personCategory.getIdNumber();
        this.nameBeneficiary = personCategory.getName();
        this.dniCreditHolder = personCategory.getIdNumber();
        this.nameCreditHolder = personCategory.getName();
    }

}
