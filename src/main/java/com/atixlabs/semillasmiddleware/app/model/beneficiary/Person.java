package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.application.Application;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@ToString
public class Person {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentType; //TODO enum or class

    @Column(unique = true)
    private Long documentNumber;

    private String name;

    private LocalDate birthDate;
    
    @JoinColumn(name = "ID_CREDENTIAL")
    @OneToMany
    private List<Credential> credentials;

    //Si es titular, no sera un pariente. Si es un pariente tendra en Kinship su titual asociado.
    @OneToOne
     private Kinship Kinship;

    @OneToMany
    private List<Application> applications;

    /*
    kinsman (pariente), p1,p2, tiporelacion (kind of kinship)

    tipo de relacion
            hijo
    conyugue
            familiar*/


}
