package me.shawlaf.banmanager.punish;

import java.util.concurrent.TimeUnit;

/**
 * Created by Florian on 15.04.2017.
 */
public class PunishmentLengthBuilder {
    
    private int years = 0, months = 0, weeks = 0, days = 0, hours = 0;
    private boolean permanent = false;
    
    public long getSum() {
        return isPermanent() ? -1L
                : TimeUnit.HOURS.toMillis(getHours()) + TimeUnit.DAYS.toMillis(getDays()) + TimeUnit.DAYS.toMillis(getWeeks() * 7) + TimeUnit.DAYS.toMillis(getMonths() * 30)
                + TimeUnit.DAYS.toMillis(getYears() * 365);
    }
    
    public void setPermanent(boolean perm) {
        this.permanent = perm;
    }
    
    public boolean isPermanent() {
        return permanent;
    }
    
    public int getYears() {
        return years;
    }
    
    public long addYear() {
        long oldsum = getSum();
        years += 1;
        return oldsum;
    }
    
    public long removeYear() {
        long oldsum = getSum();
        years -= 1;
        if (years < 0)
            years = 0;
        return oldsum;
    }
    
    public int getMonths() {
        return months;
    }
    
    public long addMonth() {
        long oldsum = getSum();
        months += 1;
        if (months == 12) {
            months = 0;
            addYear();
        }
        
        return oldsum;
    }
    
    public long removeMonth() {
        long oldsum = getSum();
        months -= 1;
        if (months < 0) {
            months = 0;
            if (years > 0) {
                removeYear();
                months = 11;
            }
        }
        return oldsum;
    }
    
    public int getWeeks() {
        return weeks;
    }
    
    public long addWeek() {
        long oldsum = getSum();
        weeks += 1;
        if (weeks == 4) {
            weeks = 0;
            addMonth();
        }
        return oldsum;
    }
    
    public long removeWeek() {
        long oldsum = getSum();
        weeks -= 1;
        if (weeks < 0) {
            weeks = 0;
            if (months > 0) {
                removeMonth();
                weeks = 3;
            }
        }
        return oldsum;
    }
    
    public int getDays() {
        return days;
    }
    
    public long addDay() {
        long oldsum = getSum();
        days += 1;
        if (days == 7) {
            days = 0;
            addWeek();
        }
        return oldsum;
    }
    
    public long removeDay() {
        long oldsum = getSum();
        days -= 1;
        if (days < 0) {
            days = 0;
            if (weeks > 0) {
                removeWeek();
                days = 6;
            }
        }
        return oldsum;
    }
    
    public int getHours() {
        return hours;
    }
    
    public long addHour() {
        long oldsum = getSum();
        hours += 1;
        if (hours == 24) {
            hours = 0;
            addDay();
        }
        return oldsum;
    }
    
    public long removeHour() {
        long oldsum = getSum();
        hours -= 1;
        if (hours < 0) {
            hours = 0;
            if (days > 0) {
                removeDay();
                hours = 23;
            }
        }
        return oldsum;
    }
    
    public void setYears(int years) {
        this.years = years;
    }
    
    public void setMonths(int months) {
        this.months = months;
    }
    
    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }
    
    public void setDays(int days) {
        this.days = days;
    }
    
    public void setHours(int hours) {
        this.hours = hours;
    }
}
