/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Helper for time and calendar-related functions related to JDBC statements and results
 */
public class CalendarHelper {
    // Calendar is not thread-safe, but is non-trivial to construct, so we hold in thread-local
    private static final ThreadLocal<Calendar> UTC_CALENDAR = new ThreadLocal<Calendar>() {
        protected Calendar initialValue() { 
            return Calendar.getInstance(TimeZone.getTimeZone("UTC")); 
        };
    };
    
    /**
     * Get a thread-specific instance of a calendar configured for UTC.
     * @return
     */
    public static Calendar getCalendarForUTC() {
        return UTC_CALENDAR.get();
    }
    
    /**
     * Helper to obtain a UTC timestamp from a JDBC {@link ResultSet}
     * @param rs
     * @param col
     * @return
     * @throws SQLException
     */
    public static Timestamp getTimestampUTC(ResultSet rs, int col) throws SQLException {
        return rs.getTimestamp(col, getCalendarForUTC());
    }

    /**
     * Helper to set a UTC timestamp value in a JDBC {@link PreparedStatement}
     * Supports null handling for ts.
     * @param ps
     * @param col
     * @param ts
     * @throws SQLException
     */
    public static void setTimestampUTC(PreparedStatement ps, int col, Timestamp ts) throws SQLException {
        if (ts != null) {
            ps.setTimestamp(col, ts, getCalendarForUTC());
        } else {
            ps.setNull(col, Types.TIMESTAMP);
        }
    }
}