package com.atixlabs.semillasmiddleware.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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

    public static DateTimeFormatter getYearFormatDate(){
       return new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

    }

    public static String toString(LocalDate dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = dateTime.format(formatter);
        return date;
    }

    public static Instant getInstantNow(){
        return Instant.now();
    }


}
