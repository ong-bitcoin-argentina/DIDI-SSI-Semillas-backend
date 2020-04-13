package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Kinship {

    @JoinColumn(name = "ID_PERSON")
    @OneToOne
    private Person mainBeneficiary;

    //private Person kinsman; //todo: eliminar esto, ya que se agrega en persona

    private String kindOfKinship ;

}
