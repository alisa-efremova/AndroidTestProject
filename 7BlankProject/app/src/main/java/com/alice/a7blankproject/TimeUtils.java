package com.alice.a7blankproject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtils {
    public static final String DATE_PATTERN_ISO_1806 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN_SHORT    = "dd.MM.yyyy";

    public static String getSoapDateString(Date dt) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(DATE_PATTERN_ISO_1806);
        df.setTimeZone(tz);
        String nowAsISO = df.format(dt);
        return nowAsISO;
    }

    public static Date parseSoapDate(String dateStr)
    {
        DateFormat format = new SimpleDateFormat(DATE_PATTERN_ISO_1806);
        try {
            return format.parse(dateStr);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String pattern)
    {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
