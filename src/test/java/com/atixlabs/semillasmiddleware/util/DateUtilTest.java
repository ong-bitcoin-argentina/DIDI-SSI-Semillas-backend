package com.atixlabs.semillasmiddleware.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtilTest {

    @Test
    public void toStringOk(){
        LocalDate localDate =  LocalDate.of(2020,1,2);
        String result = DateUtil.toString(localDate);
        Assert.assertEquals(result,"02/01/2020");
    }

}
