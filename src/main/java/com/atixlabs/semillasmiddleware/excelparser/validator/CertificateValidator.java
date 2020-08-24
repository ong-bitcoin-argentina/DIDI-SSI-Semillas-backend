package com.atixlabs.semillasmiddleware.excelparser.validator;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;

import java.util.List;

public class CertificateValidator extends EmptyFieldValidator<SancorPolicyRow> {
    // Certificate :  is not empty
    @Override
    public List<String> validate(SancorPolicyRow info) {

        return this.validateEmptyField(info.getCertificateNumber(), "Certificado");
    }

}
