package com.ibm.fhir.cql.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

public class DateHelper {

    private static int MIN_DAY = 1;
    private static int MAX_DAY = 31;
    
    public static LocalDateTime dateFloor(TemporalAccessor ta) {
        LocalDate date = getDateWithDefaults(ta, 1, MIN_DAY);
        return LocalDateTime.of( date, LocalTime.MIN );
    }

    public static LocalDateTime dateCeiling(TemporalAccessor ta) {
        LocalDate date = getDateWithDefaults(ta, 12, MAX_DAY);
        return LocalDateTime.of( date, LocalTime.MAX );
    }

    protected static LocalDate getDateWithDefaults(TemporalAccessor ta, int defaultMonth, int defaultDay) {
        ChronoUnit precision = (ChronoUnit) ta.query(TemporalQueries.precision());
        
        int year;
        int month;
        int day;
        
        switch (precision) {
        case DAYS:
            day = ta.get(ChronoField.DAY_OF_MONTH);
            month = ta.get(ChronoField.MONTH_OF_YEAR);
            year = ta.get(ChronoField.YEAR);
            break;
        case MONTHS:
            year = ta.get(ChronoField.YEAR);
            month = ta.get(ChronoField.MONTH_OF_YEAR);;
            day = (defaultDay == MAX_DAY) ? lastDayOfMonth(year, month) : defaultDay;
            break;            
        case YEARS:
            year = ta.get(ChronoField.YEAR);
            month = defaultMonth;
            day = (defaultDay == MAX_DAY) ? lastDayOfMonth(year, month) : defaultDay;
            break;
        default:
            throw new IllegalArgumentException("Unexpected date precision " + precision.name());
        }
        
        return LocalDate.of(year, month, day);
    }
    
    private static int lastDayOfMonth(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        return ym.lengthOfMonth();
    }
}
