package me.captivelabs.goliveit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Soko Android on 10/26/2015.
 */
public class DataFormatter {

    public static Date DateFormatter(String date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date dateObj = new Date();
        try {
            dateObj = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateObj; //might be null coz of try-catch
    }

    public static String getMonth(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sept";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Jan";
        }
    }

    public static String getDayOfWeek(int day) {
        switch (day) {
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thu";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";
            default:
                return "Mon";
        }
    }

    public static String formatTime(String rawDate) {
        String time = "";

        Date date = DataFormatter.DateFormatter(rawDate);
        if (date != null) {
            Calendar cal = Calendar.getInstance(); //might want to do this once
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            String am_pm = "PM";

            time = String.format("%02d:%02d", hour, minute);
        }
        return time;
    }

    public static String formatDate(String rawDate) {
        String time = "";

        Date date = DataFormatter.DateFormatter(rawDate);
        if (date != null) {
            Calendar cal = Calendar.getInstance(); //might want to do this once
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            String am_pm = "PM";

            time = String.format("%s, %s %d", getDayOfWeek(day_of_week), getMonth(month), day);
        }
        return time;
    }
}
