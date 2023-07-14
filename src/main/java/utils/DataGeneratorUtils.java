package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DataGeneratorUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataGeneratorUtils.class);
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHA_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_STRING = "0123456789";
    private static final Random random = new SecureRandom();

    public static String generateRandomAlphaNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHA_NUMERIC_STRING.length());
            sb.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomAlphaString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHA_STRING.length());
            sb.append(ALPHA_STRING.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomNumericString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(index));
        }
        return builder.toString();
    }

    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static String generateRandomEmail(String server) {
        return generateRandomAlphaNumericString(12) +
                "@" +
                server +
                ".com";
    }

    public static Date generateRandomDate(Date startDate, Date endDate) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        if (startMillis > endMillis) {
            throw new IllegalArgumentException("Start date cannot be higher than end date");
        }
        long randomMillisSinceEpoch = getRandomMillisBetween(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    public static Date generateRandomDateExcludingWeekend(Date startDate, Date endDate) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        if (startMillis > endMillis) {
            throw new IllegalArgumentException("Start date cannot be higher than end date");
        }
        long randomMillis = getRandomMillisBetween(startMillis, endMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(randomMillis);
        // Adjust the date if it falls on a weekend
        adjustDateForWeekend(calendar, startMillis, endMillis);
        return calendar.getTime();
    }

    private static long getRandomMillisBetween(long startMillis, long endMillis) {
        long range = endMillis - startMillis;
        long randomOffset = (long) (random.nextDouble() * range);
        return startMillis + randomOffset;
    }

    private static void adjustDateForWeekend(Calendar calendar, long startMillis, long endMillis) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            if (dayOfWeek == Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 2); // Move to Monday
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to Monday
            }
        }

        // Check if the adjusted date is beyond the specified boundaries
        long adjustedMillis = calendar.getTimeInMillis();
        if (adjustedMillis < startMillis) {
            calendar.setTimeInMillis(startMillis);
        } else if (adjustedMillis > endMillis) {
            calendar.setTimeInMillis(endMillis);
        }
    }

    public static String generateRandomCreditCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String generateRandomCreditCardExpirationDate() {
        int month = random.nextInt(12) + 1;
        int year = random.nextInt(10) + 2023;
        return String.format("%02d/%04d", month, year);
    }

    public static String generateRandomCreditCardCVV() {
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
