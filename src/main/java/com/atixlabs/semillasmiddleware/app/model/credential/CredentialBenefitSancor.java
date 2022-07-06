package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialBenefitSancor extends Credential{

    private Long certificateNumber;//certificate number
    private Long ref; //Pollicynumber
    private Long policyNumber;

    public CredentialBenefitSancor(CredentialBenefitSancor credentialBenefitSancor) {
        super(credentialBenefitSancor);
        this.certificateNumber = credentialBenefitSancor.certificateNumber;
        this.certificateNumber = credentialBenefitSancor.certificateNumber;
        this.policyNumber = credentialBenefitSancor.policyNumber;
    }

    @Override
    public boolean isManuallyRevocable(){return false;}

    public void addPolicyData(SancorPolicy sancorPolicy){
        this.certificateNumber = sancorPolicy.getCertificateNumber();
        this.ref = sancorPolicy.getPolicyNumber();
        this.policyNumber = sancorPolicy.getPolicyNumber();
    }
}
