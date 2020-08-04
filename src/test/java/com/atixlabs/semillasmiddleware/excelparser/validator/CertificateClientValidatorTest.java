package com.atixlabs.semillasmiddleware.excelparser.validator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CertificateClientValidatorTest {

    @Test
    public void whenSendOnlyNumbersTovalidateFormat_thenReturnOk(){
        CertificateClientValidator certificateClientValidator = new CertificateClientValidator();

        List<String> errors = certificateClientValidator.validateFormat("12345", "test");
        Assert.assertTrue(errors.isEmpty());

    }

    @Test
    public void whenSendDAndNumbersTovalidateFormat_thenReturnOk(){
        CertificateClientValidator certificateClientValidator = new CertificateClientValidator();

        List<String> errors = certificateClientValidator.validateFormat("D12345", "test");
        Assert.assertTrue(errors.isEmpty());

    }

    @Test
    public void whenSendTwoDAndNumbersTovalidateFormat_thenReturnNotOk(){
        CertificateClientValidator certificateClientValidator = new CertificateClientValidator();

        List<String> errors = certificateClientValidator.validateFormat("DD12345", "test");
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void whenSendOnlyDTovalidateFormat_thenReturnNotOk(){
        CertificateClientValidator certificateClientValidator = new CertificateClientValidator();

        List<String> errors = certificateClientValidator.validateFormat("D", "test");
        Assert.assertFalse(errors.isEmpty());

    }
}
