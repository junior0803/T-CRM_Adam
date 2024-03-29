package com.bts.adamcrm.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public final static String UI_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public final static String DB_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String UI_DATE_FORMAT = "dd/MM/yyyy";

    public static String formatDate (String date, String initDateFormat, String endDateFormat) {
        Date initDate = null;
        String parsedDate = "";
        if (date != null && !date.equals("")) {
            try {
                initDate = new SimpleDateFormat(initDateFormat).parse(date);
                SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
                parsedDate = formatter.format(initDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parsedDate;
    }

    public boolean isSummerTime(String date){
        TimeZone timeZone = TimeZone.getDefault();
        if (timeZone.useDaylightTime()){
            try {
                Date dateTime = new SimpleDateFormat(UI_DATE_TIME_FORMAT).parse(date);
                if (timeZone.inDaylightTime(dateTime))
                    return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
