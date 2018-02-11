package com.msaranu.tripney.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Saranu on 4/6/17.
 */

public class DateUtils {


    public static String getFormattedMonthForHeader(String strDate){
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

    public static String getFormattedDayForHeader(String strDate){
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


    public static long convertMovieDatetoMilliSeconds(String strDate){

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static long convertEventDatetoMilliSeconds(String strDate){

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static String convertEventDatetoDisplayFormat(String strDate){

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

    public static String convertMilitarytoStandard(String mTime){
        DateFormat militaryFormat = new SimpleDateFormat( "HHmm");
        DateFormat standardFormat = new SimpleDateFormat( "hh:mm aa");
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
}
