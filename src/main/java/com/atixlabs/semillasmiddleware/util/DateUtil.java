package com.atixlabs.semillasmiddleware.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DateUtil {

    public Date getDateNow(){
        return new Date();
    }

    public LocalDateTime getLocalDateTimeNow(){ return LocalDateTime.now();}
}
