package com.atixlabs.semillasmiddleware.app.model.credential;


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
public class CredentialBenefits extends Credential{

    private String beneficiaryType; //TODo titular / familiar -> para fill se consigue por parentezco


}
