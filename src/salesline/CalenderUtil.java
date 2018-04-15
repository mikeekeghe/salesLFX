/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Eche Michael
 */
public class CalenderUtil {
        /**
     * Default date format in the form 2013-03-18.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    /**
     * Returns the given date as a well formatted string. The above defined date
     * format is used.
     * 
     * @param calendar date to be returned as a string
     * @return formatted string
     */


    /**
     * Converts a String in the format "yyyy-MM-dd" to a Calendar object.
     * 
     * Returns null if the String could not be converted.
     * 
     * @param dateString the date as String
     * @return the calendar object or null if it could not be converted
     */
    public static Calendar parse(String dateString) {
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(DATE_FORMAT.parse(dateString));
            return result;
        } catch (ParseException e) {
            return null;
        }
}

    public static String getTime() {
         Date theTime = new Date();
        return DATE_FORMAT.format(theTime);
        
    }
}
