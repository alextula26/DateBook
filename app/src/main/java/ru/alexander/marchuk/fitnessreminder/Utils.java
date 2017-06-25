package ru.alexander.marchuk.fitnessreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(date);
    }

    public static Date parseDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String getFullDate(Date date){
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE\nd MMM yyyy");
        return fullDateFormat.format(date);
    }
}
