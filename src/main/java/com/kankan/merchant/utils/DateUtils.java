package com.kankan.merchant.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
@Slf4j
public class DateUtils {

    public static String getCurDate() {
        Calendar today = Calendar.getInstance();
        return getDate(today.getTime(), "yyyy-MM-dd");
    }

    public static String getCurDateTime() {
        Calendar today = Calendar.getInstance();
        return getDate(today.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDate(Date date, String mask) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (date == null) {
            log.error("date is null!");
        } else {
            df = new SimpleDateFormat(mask);
            returnValue = df.format(date);
        }

        return returnValue;
    }
}
