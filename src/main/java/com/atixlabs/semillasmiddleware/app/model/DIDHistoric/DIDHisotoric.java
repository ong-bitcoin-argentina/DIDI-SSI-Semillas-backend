package com.atixlabs.semillasmiddleware.app.model.DIDHistoric;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table
public class DIDHisotoric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiReceptor;

    private Long idPerson;

    private boolean isActive; //Only one did is active at the same time
}
