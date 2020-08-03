package com.atixlabs.semillasmiddleware.excelparser.app.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class SancorSaludExcelParseSerciceTest {

    @InjectMocks
    private SancorSaludExcelParseService sancorSaludExcelParseService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenParseDniReciveD00001_thenReturn1(){
        String certificateClient = "D00001";
        Long result = sancorSaludExcelParseService.parseDni(certificateClient);
        Assert.assertEquals(Long.valueOf(1L), result);
    }

    @Test
    public void whenParseDniReciveD0000100_thenReturn100(){
        String certificateClient = "D0000100";
        Long result = sancorSaludExcelParseService.parseDni(certificateClient);
        Assert.assertEquals(Long.valueOf(100L), result);
    }

    @Test
    public void whenParseDniReciveD10000100_thenReturn10000100(){
        String certificateClient = "D10000100";
        Long result = sancorSaludExcelParseService.parseDni(certificateClient);
        Assert.assertEquals(Long.valueOf(10000100L), result);
    }

    @Test
    public void whenParseDniRecive9100_thenReturn9100(){
        String certificateClient = "9100";
        Long result = sancorSaludExcelParseService.parseDni(certificateClient);
        Assert.assertEquals(Long.valueOf(9100L), result);
    }

}
