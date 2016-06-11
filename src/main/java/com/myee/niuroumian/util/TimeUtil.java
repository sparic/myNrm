package com.myee.niuroumian.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jelynn on 2016/6/6.
 */
public class TimeUtil {

    public static Timestamp getTodayStart(){
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date date = currentDate.getTime();
        return new Timestamp(date.getTime());
    }


    public static Timestamp getTodayEnd(){
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        Date date = currentDate.getTime();
        return new Timestamp(date.getTime());
    }

    public static Timestamp convertStr2Tim(String date) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(date);
            System.out.println(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

}
