package com.atixlabs.semillasmiddleware.util;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateUtil {

    public Date getDateNow(){
        return new Date();
    }
}
