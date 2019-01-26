package com.dannysms.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getTimeInFormat(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        return date;
    }

    public static String getCurrentDateTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date());
    }

    public static long timeDiffInHr(String dateStart, String dateStop) {
        long dH = 0;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays > 0) {
                dH = 24;
            } else if (diffHours > 0) {
                dH = diffHours + diffMinutes/60;
            } else {
                dH = diffMinutes/60;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return dH;
    }

    public static String timeDiff(String dateStart, String dateStop) {
        String dH = "";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays > 0) {
                dH = String.valueOf(diffDays + " d," + (diffHours) + " hr," + (diffMinutes) + " min");
            } else if (diffHours > 0) {
                dH = String.valueOf((diffHours) + " hr," + (diffMinutes) + " min");
            } else {
                dH = String.valueOf((diffMinutes) + " min");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dH;
    }
}
