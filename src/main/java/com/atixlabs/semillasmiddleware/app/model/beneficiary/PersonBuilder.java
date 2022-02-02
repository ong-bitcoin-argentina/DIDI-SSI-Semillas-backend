package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.excel.Child;
import com.atixlabs.semillasmiddleware.app.model.excel.FamilyMember;
import com.atixlabs.semillasmiddleware.app.model.excel.Form;

public class PersonBuilder {

    private Person person;

    public PersonBuilder fromForm(Form form){
        person = new Person();
        person.setDocumentNumber(form.getNumeroDniBeneficiario());
        person.setFirstName(form.getNombreBeneficiario());
        person.setLastName(form.getApellidoBeneficiario());
        person.setBirthDate(form.getFechaNacimientoBeneficiario());
        person.setGender(form.getGeneroBeneficiario());

        return this;
    }

    public PersonBuilder fromChild(Child child){
        person = new Person();
        person.setDocumentNumber(child.getNumeroDocumentoHijo());
        person.setFirstName(child.getNombreHijo());
        person.setLastName(child.getApellidoHijo());
        person.setBirthDate(child.getFechaNacimientoHijo());
        person.setGender(child.getGeneroHijo());

        return this;
    }
    public PersonBuilder fromFamilyMember(FamilyMember family){
        person = new Person();
        person.setDocumentNumber(family.getNumeroDniFamiliar());
        person.setFirstName(family.getNombreFamiliar());
        person.setLastName(family.getApellidoFamiliar());
        person.setBirthDate(family.getFechaNacimientoFamiliar());
        person.setGender(family.getGeneroFamiliar());

        return this;
    }

    public Person build(){
        return person;
    }
}
