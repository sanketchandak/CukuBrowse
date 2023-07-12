package utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The DateUtility class provides utility methods for working with dates.
 */
public class DateUtils {
    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    private static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    /** Formats the given date using the default date format.
     * @param date the date to format
     * @return the formatted date as a string
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    /** Formats the given date using the default date and time format.
     * @param date the date to format
     * @return the formatted date and time as a string
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_FORMAT);
    }

    /** Formats the given date using the specified format.
     * @param date the date to format
     * @param format the format string
     * @return the formatted date as a string
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /** Parses the given date string using the default date format.
     * @param dateStr the date string to parse
     * @return the parsed date
     * @throws ParseException if the date string cannot be parsed
     */
    public static Date parseDate(String dateStr) throws ParseException {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /** Parses the given date string using the default date time format.
     * @param dateTimeStr the date string to parse
     * @return the parsed date
     * @throws ParseException if the date string cannot be parsed
     */
    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        return parseDate(dateTimeStr, DEFAULT_DATETIME_FORMAT);
    }

    /** Parses the given date string using the specified format.
     * @param dateStr the date string to parse
     * @param format the format string
     * @return the parsed date
     * @throws ParseException if the date string cannot be parsed
     */
    public static Date parseDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateStr);
    }

    /** Adds the specified number of days to the given date.
     * @param date the date to add days to
     * @param days the number of days to add
     * @return the resulting date
     */
    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /** Adds the specified number of months to the given date.
     * @param date the date to add days to
     * @param months the number of months to add
     * @return the resulting date
     */
    public static Date addMonthsToDate(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /** Adds the specified number of years to the given date.
     * @param date the date to add days to
     * @param years the number of months to add
     * @return the resulting date
     */
    public static Date addYearsToDate(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /** Checks if the given year is a leap year.
     * @param year the year to check
     * @return true if the year is a leap year, false otherwise
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /** Checks if the year of the given date is a leap year.
     * @param date the date to check
     * @return true if the year of the date is a leap year, false otherwise
     */
    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /** Checks if the given date falls on a weekend (Saturday or Sunday).
     * @param date the date to check
     * @return true if the date falls on a weekend, false otherwise
     */
    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /** Checks if two dates represent the same day (ignoring the time portion).
     * @param date1 the first date
     * @param date2 the second date
     * @return true if the dates represent the same day, false otherwise
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    /** Calculates the number of days between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of days between the two dates
     */
    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24));
    }

    /** Calculates the number of weeks between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of weeks between the two dates
     */
    public static int getWeeksBetweenDates(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24 * 7));
    }

    /** Calculates the number of months between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of months between the two dates
     */
    public static int getMonthsBetweenDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startYear = startCal.get(Calendar.YEAR);
        int startMonth = startCal.get(Calendar.MONTH);
        int endYear = endCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH);
        int months = (endYear - startYear) * 12 + (endMonth - startMonth);
        // Adjust the months if the end date is on or before the start date in the same month
        if (startCal.get(Calendar.DAY_OF_MONTH) > endCal.get(Calendar.DAY_OF_MONTH)) {
            months--;
        }
        return months;
    }

    /** Calculates the number of years between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of years between the two dates
     */
    public static int getYearsBetweenDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);
        int years = endYear - startYear;
        // Adjust the years if the end date is on or before the start date in the same year
        if (startCal.get(Calendar.MONTH) > endCal.get(Calendar.MONTH)
                || (startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH)
                && startCal.get(Calendar.DAY_OF_MONTH) > endCal.get(Calendar.DAY_OF_MONTH))) {
            years--;
        }
        return years;
    }

}

