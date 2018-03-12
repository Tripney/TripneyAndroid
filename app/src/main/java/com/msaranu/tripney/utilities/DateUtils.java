package com.msaranu.tripney.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Saranu on 4/6/17.
 */

public class DateUtils {


    public static Date shiftTimeZone(String strDate) {


        SimpleDateFormat utcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        utcFormat.setTimeZone(TimeZone.getDefault()); //local

        try {

            Date localDate = utcFormat.parse(strDate);

            DateFormat pstFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            pstFormat.setTimeZone(TimeZone.getTimeZone("UTC")); //UTC  time

            return pstFormat.parse(pstFormat.format(localDate));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }



    public static Date convertStringtoDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm");

        Date date = null;
        try {
            date = sdf.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar convertStringtoCalendar(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = sdf.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar convertUTCtoLocalTime(String mDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(mDate));
        Date date = calendar.getTime();


        SimpleDateFormat utcFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {

            Date localDate = utcFormat.parse(utcFormat.format(date));


            DateFormat pstFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            pstFormat.setTimeZone(TimeZone.getDefault()); //Device local time


            Calendar retCalendar = Calendar.getInstance();
            retCalendar.setTime(pstFormat.parse(pstFormat.format(localDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return calendar;
    }

    public static String convertCalendarToDisplayDate(Calendar cal) {

        SimpleDateFormat dateDisplay = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat timeDisplay = new SimpleDateFormat("hh:mm a z");

        Date date = cal.getTime();

        return dateDisplay.format(date) +"\n" + timeDisplay.format(date) + " " ;
    }


    public static String convertCalendarToDisplayDateEvent(Calendar cal) {

        SimpleDateFormat dateDisplay = new SimpleDateFormat("EEE, MMM dd hh:mm a z");

        Date date = cal.getTime();

        return dateDisplay.format(date) ;
    }

    public static String convertCalendarToDisplayDateEdit(Calendar cal) {

        SimpleDateFormat dateDisplay = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = cal.getTime();

        return dateDisplay.format(date) ;
    }
}
