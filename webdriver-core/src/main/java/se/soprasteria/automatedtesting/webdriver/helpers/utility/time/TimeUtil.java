package se.soprasteria.automatedtesting.webdriver.helpers.utility.time;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class TimeUtil {
    private static final String DEFAULT_TIMEZONE = "Europe/Stockholm";

    public static long getMinutesDiff(String time1, String time2) {
        long minutesDiff = ChronoUnit.MINUTES.between(
                LocalTime.parse(time1), LocalTime.parse(time2)
        );
        if (minutesDiff < 0) {
            return 24 * 60 + minutesDiff;
        }
        return minutesDiff;
    }

    public static long between(String time1, String time2) {
        return ChronoUnit.MINUTES.between(
                LocalTime.parse(time1), LocalTime.parse(time2)
        );
    }

    public static String getCurrentTime() {
        return LocalTime.now(ZoneId.of(DEFAULT_TIMEZONE)).toString();
    }

    public static boolean isValidTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidDate(String date){
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String getCurrentTimeShort() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getCurrentDateShort() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

}
