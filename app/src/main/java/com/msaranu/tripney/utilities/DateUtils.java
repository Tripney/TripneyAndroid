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


    public static String getFormattedMonthForHeader(String strDate) {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        DateFormat pstFormat = new SimpleDateFormat("MMM");
        try {
            date = utcFormat.parse(strDate);
            return pstFormat.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

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


    public static String getFormattedDayForHeader(String strDate) {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat pstFormat = new SimpleDateFormat("dd");
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
            return pstFormat.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static long convertMovieDatetoMilliSeconds(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static long convertEventDatetoMilliSeconds(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static String convertEventDatetoDisplayFormat(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");


        Date date = null;
        try {
            date = sdf.parse(strDate);
            return sdfDisplay.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertMilitarytoStandard(String mTime) {
        DateFormat militaryFormat = new SimpleDateFormat("HHmm");
        DateFormat standardFormat = new SimpleDateFormat("hh:mm aa");
        Date date = null;
        try {
            date = militaryFormat.parse(mTime);
            return standardFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String convertEventDateListItemDisplayFormat(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEE, MMM dd, hh:mm a");

        Date date = null;
        try {
            date = sdf.parse(strDate);
            return sdfDisplay.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertEventDateFormatNoTime(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEE, MMM dd, yyyy");

        Date date = null;
        try {
            date = sdf.parse(strDate);
            return sdfDisplay.format(date);
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
}
