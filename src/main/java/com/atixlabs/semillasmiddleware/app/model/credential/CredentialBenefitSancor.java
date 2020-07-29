package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialBenefitSancor extends Credential{

    private String certificateNumber;
    private String ref;
    private String policyNumber;

    public CredentialBenefitSancor(CredentialBenefitSancor credentialBenefitSancor) {
        super(credentialBenefitSancor);
        this.certificateNumber = credentialBenefitSancor.certificateNumber;
        this.certificateNumber = credentialBenefitSancor.certificateNumber;
        this.policyNumber = credentialBenefitSancor.policyNumber;
    }

    @Override
    public boolean isManuallyRevocable(){return false;}
}
