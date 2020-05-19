package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialIdentity extends Credential {


public CredentialIdentity(CredentialIdentity credentialIdentity){
    super(credentialIdentity);
}

}
