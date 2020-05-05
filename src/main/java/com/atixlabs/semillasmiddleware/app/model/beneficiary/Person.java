package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.application.Application;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.*;

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

    private String firstName;
    private String lastName;

    private LocalDate birthDate;

    @OneToMany
    private List<DIDHisotoric> DIDIsHisotoric;


    //TODO user this
    public boolean equalsIgnoreId(Person person1, Person person2) {
        return person1.getDocumentNumber().equals(person2.getDocumentNumber()) &&
                person1.getFirstName().equals(person2.getFirstName()) &&
                person1.getLastName().equals(person2.getLastName()) &&
                person1.getBirthDate().isEqual(person2.getBirthDate());
    }

    //TODO review (delete static and use this)
    public static Person getPersonFromPersonCategory(PersonCategory personCategory) {
        Person person = new Person();
        person.setDocumentNumber(personCategory.getIdNumber());
        person.setFirstName(personCategory.getName());
        person.setLastName(personCategory.getSurname());
        person.setBirthDate(personCategory.getBirthDate());
        return person;
    }


    /*
    @JoinColumn(name = "ID_CREDENTIAL")
    @OneToMany
    private List<Credential> credentials;

*/

/*

    @OneToMany
    private List<Application> applications;
*/


    /*
    kinsman (pariente), p1,p2, tiporelacion (kind of kinship)

    tipo de relacion
            hijo
    conyugue
            familiar*/


}
