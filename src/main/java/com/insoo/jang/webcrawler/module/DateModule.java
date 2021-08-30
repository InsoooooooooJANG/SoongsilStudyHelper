package com.insoo.jang.webcrawler.module;

import java.util.Calendar;
import java.util.Date;

public class DateModule {
    public static Date GetToday()
    {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date today = c.getTime();
        return today;
    }

    public static Boolean isToday(Date compareDate){
        Date today = GetToday();

        if(compareDate.equals(null)) {
            return false;
        }else return today.compareTo(compareDate) == 0;
    }

}
