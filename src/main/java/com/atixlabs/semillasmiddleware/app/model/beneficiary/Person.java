package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.excel.Child;
import com.atixlabs.semillasmiddleware.app.model.excel.FamilyMember;
import com.atixlabs.semillasmiddleware.app.model.excel.Form;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private String gender;

    @OneToMany
    private List<DIDHisotoric> DIDIsHisotoric;

    @ManyToMany(fetch = FetchType.EAGER)
    protected List<Loan> defaults; //TODO must be a HashSet

    public Person(Form form) {
        this.documentNumber = form.getNumeroDniBeneficiario();
        this.firstName = form.getNombreBeneficiario();
        this.lastName = form.getApellidoBeneficiario();
        this.birthDate = form.getFechaNacimientoBeneficiario();
        this.gender = form.getGeneroBeneficiario();

    }

    public void Spouse(Form form){
        this.documentNumber = form.getNumeroDniConyuge();
        this.firstName = form.getNombreConyuge();
        this.lastName = form.getApellidoConyuge();
        this.birthDate = form.getFechaNacimientoConyuge();
        if (form.getGeneroConyuge().isEmpty())
            this.gender= form.getGeneroOtroConyuge();
        else
            this.gender = form.getGeneroConyuge();
    }

    public Person(Child child) {
        this.documentNumber = child.getNumeroDocumentoHijo();
        this.firstName = child.getNombreHijo();
        this.lastName = child.getApellidoHijo();
        this.birthDate = child.getFechaNacimientoHijo();
        if (child.getGeneroHijo().isEmpty())
            this.gender = child.getGeneroOtroHijo();
        else
            this.gender = child.getGeneroHijo();
    }

    public Person(FamilyMember family) {
        this.documentNumber = family.getNumeroDniFamiliar();
        this.firstName = family.getNombreFamiliar();
        this.lastName = family.getApellidoFamiliar();
        this.birthDate = family.getFechaNacimientoFamiliar();
        if (family.getGeneroFamiliar().isEmpty())
            this.gender = family.getGeneroOtroFamiliar();
        else
            this.gender = family.getGeneroFamiliar();
    }

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
        person.setDocumentNumber(personCategory.getDocumentNumber());
        person.setFirstName(personCategory.getName());
        person.setLastName(personCategory.getSurname());
        person.setBirthDate(personCategory.getBirthDate());
        person.setGender(personCategory.getGender());
        return person;
    }

    public boolean isInDefault(){return (this.defaults!=null ?  defaults.size()>0 : false);}


    public boolean removeLoanInDefault(Loan loan){
        if(this.isInDefault()){
            if(this.getDefaults().contains(loan)){
                return this.getDefaults().remove(loan);
            }
        }

        return false;
    }

    public void addLoanInDefault(Loan loan){
        if(this.getDefaults()==null){
            this.setDefaults(new ArrayList<Loan>());
        }
        if(!this.getDefaults().contains(loan)){
            this.getDefaults().add(loan);
        }
    }

}
