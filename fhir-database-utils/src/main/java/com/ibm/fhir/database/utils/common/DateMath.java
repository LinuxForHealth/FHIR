/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ibm.fhir.database.utils.api.DataAccessException;

/**
 * Simple utility class to handle the date arithmetic we need for rolling
 * date-based partitions
 * TODO use java.time instead
 */
public class DateMath {
    private static final String FORMAT = "yyyy-MM-dd";
    private static final String FORMAT2 = "yyyyMMdd";

    /**
     * Add howMany days to the given {@link Date}
     * @param d
     * @param howMany
     * @return
     */
    public static Date addDays(Date d, int howMany) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.DATE, howMany);
        return gc.getTime();
    }

    /**
     * Add howMany months to the given {@link Date}
     * @param d
     * @param howMany
     * @return
     */
    public static Date addMonths(Date d, int howMany) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.MONTH, howMany);
        return gc.getTime();
    }

    /**
     * Parse the date string which is expected to be yyyy-MM-dd
     * @param str
     * @return
     */
    public static Date parse(String str) {
        DateFormat df = new SimpleDateFormat(FORMAT);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Format the date value as yyyy-MM-dd
     * @param d
     * @return
     */
    public static String format(Date d) {
        DateFormat df = new SimpleDateFormat(FORMAT);
        return df.format(d);
    }

    /**
     * Alternate format of the date value: yyyyMMdd
     * @param d
     * @return
     */
    public static String format2(Date d) {
        DateFormat df = new SimpleDateFormat(FORMAT2);
        return df.format(d);
    }

    /**
     * Truncate the date to the midnight on the first day of the month
     * @param d
     * @return
     */
    public static Date truncateToMonth(Date d) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * Truncate the date to the beginning of the day
     * @param d
     * @return
     */
    public static Date truncateToDay(Date d) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * Add the requested number of seconds to the given date
     * @param d
     * @param howMany
     * @return
     */
    public static Date addSeconds(Date d, int howMany) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.SECOND, howMany);
        return gc.getTime();
    }

    /**
     * Return the max of 2 dates
     * @param d1
     * @param d2
     * @return max date
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return null;
        }
        if (d1 == null || d2 == null) {
                return (d1 == null ? d2 : d1);
        }
        return (d1.after(d2)) ? d1 : d2;
    }

}
