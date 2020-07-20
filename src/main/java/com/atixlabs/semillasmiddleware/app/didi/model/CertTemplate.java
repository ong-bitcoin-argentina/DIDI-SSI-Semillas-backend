package com.atixlabs.semillasmiddleware.app.didi.model;

import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "cert_template")
@Entity
@NoArgsConstructor
public class CertTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private CredentialCategoriesCodes credentialCategoriesCodes;

    private String templateCode;

    @Column(length = 80)
    private String templateDescription;

    public CertTemplate(CredentialCategoriesCodes credentialCategoriesCodes, String templateCode, String templateDescription){
        this.credentialCategoriesCodes = credentialCategoriesCodes;
        this.templateCode = templateCode;
        this.templateDescription = templateDescription;
    }
}
