package com.atixlabs.semillasmiddleware.excelparser.validatorfactory;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;
import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import com.atixlabs.semillasmiddleware.excelparser.validator.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Create the chain of validators for each type of row
 */
@Component
public class ExcelRowValidatorFactory {

    public RowValidator<ExcelRow> getExcelRowValidator() {

        List<Validator<ExcelRow>> validators = new ArrayList<>();
        return new RowValidator<>(validators);
    }

    public RowValidator<SancorPolicyRow> getSancorPolicyRowValidator(){
        List<Validator<SancorPolicyRow>> validators = new ArrayList<>();

        validators.add(new PolicyValidator());
        validators.add(new CertificateValidator());
        validators.add(new CertificateClientValidator());

        return new RowValidator<>(validators);
    }
}
