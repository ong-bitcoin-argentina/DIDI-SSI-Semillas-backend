package com.atixlabs.semillasmiddleware.app.bondarea.model.constants;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoanStatusCodesTest {

    @Test
    void whenFinalizedString_getByCode_expectFinalizedEnum() {
        String finalizedCode = LoanStatusCodes.FINALIZED.getCode();
        Optional<LoanStatusCodes> code = LoanStatusCodes.getByCode(finalizedCode);
        assertTrue(code.isPresent());
        assertEquals(code.get(), LoanStatusCodes.FINALIZED);
    }
}