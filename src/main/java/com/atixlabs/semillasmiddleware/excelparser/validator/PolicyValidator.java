package com.atixlabs.semillasmiddleware.excelparser.validator;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;

import java.util.ArrayList;
import java.util.List;

public class PolicyValidator extends EmptyFieldValidator<SancorPolicyRow> {

    //- Policy : is not empty and is a number
    @Override
    public List<String> validate(SancorPolicyRow info) {

        return this.validateEmptyField(info.getPolicy(), "Póliza");
    }
}
