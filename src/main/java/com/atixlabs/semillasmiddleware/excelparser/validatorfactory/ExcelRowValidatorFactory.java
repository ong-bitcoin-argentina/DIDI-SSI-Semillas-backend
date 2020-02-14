package com.atixlabs.semillasmiddleware.excelparser.validatorfactory;

import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import com.atixlabs.semillasmiddleware.excelparser.validator.RowValidator;
import com.atixlabs.semillasmiddleware.excelparser.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class ExcelRowValidatorFactory {

    public RowValidator<ExcelRow> getPuntajeExcelRowValidator() {

        List<Validator<ExcelRow>> validators = new ArrayList<>();
        return new RowValidator<>(validators);
    }
}
