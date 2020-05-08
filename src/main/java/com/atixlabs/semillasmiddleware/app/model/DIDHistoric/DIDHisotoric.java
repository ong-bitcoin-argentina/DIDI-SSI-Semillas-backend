package com.atixlabs.semillasmiddleware.app.model.DIDHistoric;

import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
public class DIDHisotoric extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiReceptor;

    private Long idPerson;

    private boolean isActive; //Only one did is active at the same time
}
