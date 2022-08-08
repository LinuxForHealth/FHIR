/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.common;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

/**
 * Collection of utility functions to simply setting values on a PreparedStatement
 */
public class PreparedStatementHelper {
    // The PreparedStatement we delegate everything to
    private final PreparedStatement ps;

    // The calendar to make sure all times are treated as UTC
    private final Calendar UTC = CalendarHelper.getCalendarForUTC();

    // The current parameter index in the statement
    private int index = 1;

    /**
     * Public constructor
     * @param ps
     */
    public PreparedStatementHelper(PreparedStatement ps) {
        this.ps = ps;
    }

    /**
     * Set the (possibly null) int value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setInt(Integer value) throws SQLException {
        if (value != null) {
            ps.setInt(index, value);
        } else {
            ps.setNull(index, Types.INTEGER);
        }
        index++;
        return this;
    }

    /**
     * Set the (possibly null) long value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setLong(Long value) throws SQLException {
        if (value != null) {
            ps.setLong(index, value);
        } else {
            ps.setNull(index, Types.BIGINT);
        }
        index++;
        return this;
    }

    /**
     * Set the (possibly null) long value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setShort(Short value) throws SQLException {
        if (value != null) {
            ps.setShort(index, value);
        } else {
            ps.setNull(index, Types.SMALLINT);
        }
        index++;
        return this;
    }

    /**
     * Set the (possibly null) String value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setString(String value) throws SQLException {
        if (value != null) {
            ps.setString(index, value);
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
        index++;
        return this;
    }

    /**
     * Set the (possibly null) InputStream value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setBinaryStream(InputStream value) throws SQLException {
        if (value != null) {
            ps.setBinaryStream(index, value);
        } else {
            ps.setNull(index, Types.BINARY);
        }
        index++;
        return this;
    }

    /**
     * Set the (possibly null) int value at the current position
     * and increment the position by 1
     * @param value
     * @return this instance
     * @throws SQLException
     */
    public PreparedStatementHelper setTimestamp(Timestamp value) throws SQLException {
        if (value != null) {
            ps.setTimestamp(index, value, UTC);
        } else {
            ps.setNull(index, Types.TIMESTAMP);
        }
        index++;
        return this;
    }

    /**
     * Register an OUT parameter, assuming the delegate is a CallableStatement
     * @param parameterType from {@link java.sql.Types}
     * @return the parameter index of the OUT parameter
     * @throws SQLException
     */
    public int registerOutParameter(int parameterType) throws SQLException {
        int idx = index++;
        if (ps instanceof CallableStatement) {
            CallableStatement cs = (CallableStatement)ps;
            cs.registerOutParameter(idx, parameterType);
        } else {
            throw new IllegalStateException("Delegate is not a CallableStatement");
        }
        return idx;
    }

    /**
     * Add a new batch entry based on the current state of the {@link PreparedStatement}.
     * Note that we don't return this on purpose...because addBatch should be last in
     * any sequence of setXX(...) calls.
     * @throws SQLException
     */
    public void addBatch() throws SQLException {
        ps.addBatch();
        index = 1;
    }
}
