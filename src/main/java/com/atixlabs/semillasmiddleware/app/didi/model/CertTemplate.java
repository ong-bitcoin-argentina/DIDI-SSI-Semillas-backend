package com.atixlabs.semillasmiddleware.app.didi.model;

import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "cert_template")
@Entity
@NoArgsConstructor
public class CertTemplate {

    @Id
    private Long id;

    private CredentialCategoriesCodes credentialCategoriesCodes;

    private String templateCode;

    @Column(length = 80)
    private String templateDescription;
}
