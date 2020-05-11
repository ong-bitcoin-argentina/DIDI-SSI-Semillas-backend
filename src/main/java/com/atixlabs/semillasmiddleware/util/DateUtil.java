package com.atixlabs.semillasmiddleware.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateUtil {

    public static Date getDateNow(){
        return new Date();
    }

    public static LocalDateTime getLocalDateTimeNow(){ return LocalDateTime.now();}

    public static LocalDateTime getLocalDateTimeNowWithFormat(String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateNow = LocalDateTime.now().format(formatter);
        return (LocalDateTime.parse(dateNow, formatter));
    }

    public static LocalDate getLocalDateWithFormat(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateNow = LocalDateTime.now().format(formatter);
        return LocalDate.parse(dateNow, formatter);
    }



}
