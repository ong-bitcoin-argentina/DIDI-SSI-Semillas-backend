package com.atixlabs.semillasmiddleware.excelparser.validator;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;

import java.util.ArrayList;
import java.util.List;

public class CertificateClientValidator extends EmptyFieldValidator<SancorPolicyRow> {

    @Override
    public List<String> validate(SancorPolicyRow info) {

        String fieldName = "Cliente Certificado";

        List<String> errors = new ArrayList<String>();
        errors.addAll(this.validateEmptyField(info.getCertificateNumber(), fieldName));
        errors.addAll(this.validateFormat(info.getCertificateClient(), fieldName));

        return errors;
    }

    public List<String> validateFormat(String value, String fieldName){
        String regexNumbers = "\\d+";
        String regexDAndNumbers = "D\\d+";
        List<String> errors = new ArrayList<String>();
        if(value == null || !(value.matches(regexDAndNumbers) || value.matches(regexNumbers))){
            errors.add(String.format("El campo Cliente %s debe tener el formato 'Dnnnnn...' o ser solo números",fieldName));
        }

        return errors;
    }

}
