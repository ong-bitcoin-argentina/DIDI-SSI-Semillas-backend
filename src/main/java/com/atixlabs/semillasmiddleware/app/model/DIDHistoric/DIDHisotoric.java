package com.atixlabs.semillasmiddleware.app.model.DIDHistoric;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
public class DIDHisotoric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idDidiReceptor;

    private Long idPerson;

    private boolean isActive; //Only one did is active at the same time
}
