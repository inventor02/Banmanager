package me.shawlaf.banmanager.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    
    private static final char[] numbers = "0123456789".toCharArray();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, ''yy h:mm a");
    
    private TimeUtils() {
    }
    
    public static String getDurationBreakdown(long millis, TimeUnit... exluce) {
        return getDurationBreakdown(millis, 2, exluce);
    }
    
    public static String getDurationBreakdown(long millis, int maxParts, TimeUnit... exluce) {
        List<TimeUnit> exluceList = Arrays.asList(exluce);
        
        if (millis < 0) {
            return "Permanent";
        }
        
        long weeks = TimeUnit.MILLISECONDS.toDays(millis) / 7;
        millis -= TimeUnit.DAYS.toMillis(weeks * 7);
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        
        StringBuilder sb = new StringBuilder();
        
        int countParts = 0;
        
        if (weeks != 0 && countParts < maxParts) {
            sb.append(weeks + (weeks == 1 ? " Week " : " Weeks "));
            countParts++;
        }
        
        if (days != 0 && ! exluceList.contains(TimeUnit.DAYS) && countParts < maxParts) {
            sb.append(days + (days == 1 ? " Day " : " Days "));
            countParts++;
        }
        
        if (hours != 0 && ! exluceList.contains(TimeUnit.HOURS) && countParts < maxParts) {
            sb.append(hours + (hours == 1 ? " Hour " : " Hours "));
            countParts++;
        }
        
        if (minutes != 0 && ! exluceList.contains(TimeUnit.MINUTES) && countParts < maxParts) {
            sb.append(minutes + (minutes == 1 ? " Minute " : " Minutes "));
            countParts++;
        }
        
        if (seconds != 0 && ! exluceList.contains(TimeUnit.SECONDS) && countParts < maxParts) {
            sb.append(seconds + (seconds == 1 ? " Second " : " Seconds "));
        }
        
        return sb.toString().trim();
    }
    
    public static String format(long date) {
        return DATE_FORMAT.format(new Date(date));
    }
    
    public static String formatCurrentTime() {
        return format(System.currentTimeMillis());
    }
    
}
