package com.apptpro.apptpro.Models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateTimeFormatting {
    /**
     * A static method for converting UTC String to a time format subtible for the
     * local time of the users system.
     * @param utcString The string representing a UTC date & time
     * @param parseForT If true, will add a T in between date and time.
     * @param formatted If true, the given UTC string will be converted to local system time using
     *                  the FormatStyle.SHORT constant
     * @return The UTC string converted to local system time
     */
    public static String UTCStringToDefaultTimeFormat(String utcString,boolean parseForT, boolean formatted) {
        if(parseForT) {
            utcString = utcString.replaceAll("\\s+","T");
        }
        LocalDateTime apptStart  = LocalDateTime.parse(utcString);
        ZonedDateTime apptStartFinal = ZonedDateTime.of(apptStart, ZoneId.of("UTC"));
        apptStartFinal = ZonedDateTime.ofInstant(apptStartFinal.toInstant(),ZoneId.systemDefault());
        if(formatted) {
            DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
            return apptStartFinal.format(dtf);
        }
        return apptStartFinal.toLocalDateTime().toString();


    }

}
