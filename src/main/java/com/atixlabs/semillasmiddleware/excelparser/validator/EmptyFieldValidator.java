package com.atixlabs.semillasmiddleware.excelparser.validator;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;

import java.util.ArrayList;
import java.util.List;

public abstract class EmptyFieldValidator<T> extends Validator<T> {


    private String emptyErrorMessage = "El campo %s es obligaotrio, no puede estar vacio";

    public List<String> validateEmptyField(Long value, String fieldName) {

        List<String> errors = new ArrayList<>();

        if(value==null){
            errors.add(String.format(emptyErrorMessage,fieldName));
        }

        return errors;
    }

    public List<String> validateEmptyField(String value, String fieldName) {

        List<String> errors = new ArrayList<>();

        if(value==null || value.trim().isEmpty()){
            errors.add(String.format(emptyErrorMessage,fieldName));
        }

        return errors;
    }
}
