package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.excel.Child;
import com.atixlabs.semillasmiddleware.app.model.excel.FamilyMember;
import com.atixlabs.semillasmiddleware.app.model.excel.Form;

import java.time.LocalDate;

public class PersonBuilder {

    private Person person;

    public PersonBuilder fromForm(Form form){
        person = new Person();
        person.setDocumentNumber(form.getNumeroDniBeneficiario());
        person.setFirstName(form.getNombreBeneficiario());
        person.setLastName(form.getApellidoBeneficiario());
        person.setBirthDate(LocalDate.parse(form.getFechaNacimientoBeneficiario()));
        if (form.getGeneroBeneficiario().isEmpty()) {
            person.setGender(form.getGeneroOtroBeneficiario());
        } else {
            person.setGender(form.getGeneroBeneficiario());
        }
        return this;
    }

    public PersonBuilder fromChild(Child child){
        person = new Person();
        person.setDocumentNumber(child.getNumeroDocumentoHijo());
        person.setFirstName(child.getNombreHijo());
        person.setLastName(child.getApellidoHijo());
        person.setBirthDate(LocalDate.parse(child.getFechaNacimientoHijo()));
        person.setGender(child.getGeneroHijo());
        if (child.getGeneroHijo().isEmpty())
            person.setGender(child.getGeneroOtroHijo());
        else
            person.setGender(child.getGeneroHijo());
        return this;
    }
    public PersonBuilder fromFamilyMember(FamilyMember family){
        person = new Person();
        person.setDocumentNumber(family.getNumeroDniFamiliar());
        person.setFirstName(family.getNombreFamiliar());
        person.setLastName(family.getApellidoFamiliar());
        person.setBirthDate(LocalDate.parse(family.getFechaNacimientoFamiliar()));
       if (family.getGeneroFamilia().isEmpty()){
           person.setGender(family.getGeneroOtroFamiliar());
       }else{
           person.setGender(family.getGeneroFamilia());
       }
        return this;
    }

    public Person build(){
        return person;
    }
}
