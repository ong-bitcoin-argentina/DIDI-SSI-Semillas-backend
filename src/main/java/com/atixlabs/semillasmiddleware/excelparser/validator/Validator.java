package com.atixlabs.semillasmiddleware.excelparser.validator;

import java.util.List;

public abstract class Validator<T> {

    protected short countErrors = 0;
    protected short warnings = 0;

    public abstract List<String> validate(T info);

}
