package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.application.Application;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table
public class Person {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long documentNumber;

    private String name;

    private LocalDate birthDate;
    
    @JoinColumn(name = "ID_CREDENTIAL")
    @OneToMany
    private List<Credential> credentials;

    @OneToMany
    private List<DIDHisotoric> DIDIsHisotoric;


    //Si es titular, no sera un pariente. Si es un pariente tendra en Kinship su titual asociado.
    @OneToOne
     private Kinship Kinship;

    @OneToMany
    private List<Application> applications;

    public Person(PersonCategory personCategory){
        this.setName(personCategory.getName()+" "+ personCategory.getSurname());
        this.setDocumentNumber(personCategory.getIdNumber());
        this.setBirthDate(personCategory.getBirthdate());
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", documentNumber=" + documentNumber +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", credentials=" + credentials +
                '}';
    }


    /*
    kinsman (pariente), p1,p2, tiporelacion (kind of kinship)

    tipo de relacion
            hijo
    conyugue
            familiar*/


}
