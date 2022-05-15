/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Simplifies reading values from a {@link ResultSet}
 */
public class ResultSetReader {
    private final Calendar UTC = CalendarHelper.getCalendarForUTC();

    // The ResultSet we're reading from
    private final ResultSet rs;

    private int index = 0;

    /**
     * Canonical constructor
     * @param rs
     */
    public ResultSetReader(ResultSet rs) {
        this.rs = rs;
    }

    /**
     * Invoke {@link ResultSet#next()}
     * @return true if the ResultSet has a row
     * @throws SQLException
     */
    public boolean next() throws SQLException {
        index = 1;
        return rs.next();
    }

    /**
     * Get a string column value and increment the column index
     * @return
     * @throws SQLException
     */
    public String getString() throws SQLException {
        return rs.getString(index++);
    }

    /**
     * Get a Short column value and increment the column index
     * @return
     * @throws SQLException
     */
    public Short getShort() throws SQLException {
        Short result = rs.getShort(index++);
        if (rs.wasNull()) {
            result = null;
        }
        return result;
    }

    /**
     * Get an Integer column value and increment the column index
     * @return
     * @throws SQLException
     */
    public Integer getInt() throws SQLException {
        Integer result = rs.getInt(index++);
        if (rs.wasNull()) {
            result = null;
        }
        return result;
    }

    /**
     * Get a Long column value and increment the column index
     * @return
     * @throws SQLException
     */
    public Long getLong() throws SQLException {
        Long result = rs.getLong(index++);
        if (rs.wasNull()) {
            result = null;
        }
        return result;
    }

    /**
     * Get a Timestamp column value and increment the column index
     * @return
     * @throws SQLException
     */
    public Timestamp getTimestamp() throws SQLException {
        Timestamp result = rs.getTimestamp(index++, UTC);
        if (rs.wasNull()) {
            result = null;
        }
        return result;
    }
}
