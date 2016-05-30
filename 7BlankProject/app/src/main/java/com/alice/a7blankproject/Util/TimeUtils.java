package com.alice.a7blankproject.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtils {
    public static final String DATE_PATTERN_ISO_1806 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN_SHORT_DATE = "dd.MM.yyyy";
    public static final String DATE_PATTERN_SHORT_DATETIME = "dd.MM.yyyy HH:mm";

    public static final String DEFAULT_TIMEZONE = "UTC";

    public static String getSoapDateString(Date dt) {
        TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIMEZONE);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN_ISO_1806);
        df.setTimeZone(tz);
        String nowAsISO = df.format(dt);
        return nowAsISO;
    }

    public static Date parseSoapDate(String dateStr) {
        DateFormat format = new SimpleDateFormat(DATE_PATTERN_ISO_1806);
        try {
            return format.parse(dateStr);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date addToDate(Date date, int period) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, period);
        return calendar.getTime();
    }

    public static Date resetTimePartOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIMEZONE);
        cal.setTimeZone(tz);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
