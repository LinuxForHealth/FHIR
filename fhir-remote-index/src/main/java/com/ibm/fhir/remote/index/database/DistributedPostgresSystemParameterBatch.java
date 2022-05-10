/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import com.ibm.fhir.database.utils.common.CalendarHelper;

/**
 * Batch insert statements for system-level parameters
 * @implNote targets the distributed variant of the schema
 * where each table includes a shard_key column
 */
public class DistributedPostgresSystemParameterBatch {
    private final Connection connection;

    private PreparedStatement systemStrings;
    private int systemStringCount;

    private PreparedStatement systemDates;
    private int systemDateCount;

    /**
     * Public constructor
     * @param c
     */
    public DistributedPostgresSystemParameterBatch(Connection c) {
        this.connection = c;
    }

    /**
     * Push the current batch
     */
    public void pushBatch() throws SQLException {
        if (systemStringCount > 0) {
            systemStrings.executeBatch();
            systemStringCount = 0;
        }
        if (systemDateCount > 0) {
            systemDates.executeBatch();
            systemDateCount = 0;
        }
    }

    /**
     * Clear the current batch
     */
    public void clearBatch() throws SQLException {
        if (systemStringCount > 0) {
            systemStrings.clearBatch();
            systemStringCount = 0;
        }
        if (systemDateCount > 0) {
            systemDates.clearBatch();
            systemDateCount = 0;
        }
    }
    /**
     * Closes all the statements currently open
     */
    public void close() {

        if (systemStrings != null) {
            try {
                systemStrings.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemStrings = null;
            }
        }

        if (systemDates != null) {
            try {
                systemDates.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemDates = null;
            }
        }
    }

    /**
     * Set the compositeId on the given PreparedStatement, handling a value if necessary
     * @param ps
     * @param index
     * @param compositeId
     * @throws SQLException
     */
    private void setComposite(PreparedStatement ps, int index, Integer compositeId) throws SQLException {
        if (compositeId != null) {
            ps.setInt(index, compositeId);
        } else {
            ps.setNull(index, Types.INTEGER);
        }
    }

    public void addString(long logicalResourceId, int parameterNameId, String strValue, String strValueLower, Integer compositeId, short shardKey) throws SQLException {
            // System level string attributes
            if (systemStrings == null) {
                final String insertSystemString = "INSERT INTO str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id, shard_key) VALUES (?,?,?,?,?)";
                systemStrings = connection.prepareStatement(insertSystemString);
            }
            systemStrings.setInt(1, parameterNameId);
            systemStrings.setString(2, strValue);
            systemStrings.setString(3, strValueLower);
            systemStrings.setLong(4, logicalResourceId);
            setComposite(systemStrings, 5, compositeId);
            systemStrings.setShort(6, shardKey);
            systemStrings.addBatch();
            systemStringCount++;
    }

    public void addDate(long logicalResourceId, int parameterNameId, Timestamp dateStart, Timestamp dateEnd, Integer compositeId, short shardKey) throws SQLException {
        if (systemDates == null) {
            final String insertSystemDate = "INSERT INTO date_values (parameter_name_id, date_start, date_end, logical_resource_id, shard_key) VALUES (?,?,?,?,?)";
            systemDates = connection.prepareStatement(insertSystemDate);
        }
        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        systemDates.setInt(1, parameterNameId);
        systemDates.setTimestamp(2, dateStart, UTC);
        systemDates.setTimestamp(3, dateEnd, UTC);
        systemDates.setLong(4, logicalResourceId);
        setComposite(systemDates, 5, compositeId);
        systemDates.setShort(6, shardKey);
        systemDates.addBatch();
        systemDateCount++;
    }
}