package com.example.imgtest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String getCurrentTime(String pattern) {
        return getCurrentTime(pattern, "GMT+8:00");
    }

    public static String getCurrentTime(String pattern, String timeZone) {
        if (pattern == null || pattern.isEmpty() || timeZone == null || timeZone.isEmpty()) {
            return null;
        }

        SimpleDateFormat dataFormat = new SimpleDateFormat(pattern);
        dataFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dataFormat.format(new Date());
    }


    public static long getFitTimeSpan(final long millis0, final long millis1) {
        double duration = (double) (millis0-millis1)/(1000*60);
        if (duration>0){
            return (long) Math.ceil(duration);
        }else
        {
            return (long)Math.ceil(-duration);
        }
    }


}