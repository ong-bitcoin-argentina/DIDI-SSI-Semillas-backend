package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.sancor.service.SancorPolicyService;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.validatorfactory.ExcelRowValidatorFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

class SancorSaludExcelParseServiceTest {

    @Mock
    private SancorPolicyService sancorPolicyService;

    @Mock
    private ExcelRowValidatorFactory excelRowValidatorFactory;

    @InjectMocks
    private SancorSaludExcelParseService sancorSaludExcelParseService;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @CsvSource({"D00001, 1L","D0000100,100L","D10000100,10000100L","9100,9100L"})
    void whenParseDniReceiveX_thenReturnY(String certificateClient, String longValue){
        Long result = sancorSaludExcelParseService.parseDni(certificateClient);
        Assert.assertEquals(Long.valueOf(Long.parseLong(longValue)), result);
    }

    @Test
    public void processRowTest(){
        Workbook mockWorkbook = Mockito.mock(Workbook.class);
        Sheet mockSheet = Mockito.mock(Sheet.class);
        Row mockRow = Mockito.mock(Row.class);

        Mockito.doReturn(mockSheet).when(mockWorkbook).createSheet();

        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        ProcessExcelFileResult result = sancorSaludExcelParseService.processRow(mockRow, true,
                processExcelFileResult,false, false, false);

        Assertions.assertNotEquals(processExcelFileResult, result);

    }

}
