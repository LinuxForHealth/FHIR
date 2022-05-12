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
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;

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

    private PreparedStatement systemProfiles;
    private int systemProfileCount;

    private PreparedStatement systemTags;
    private int systemTagCount;

    private PreparedStatement systemSecurity;
    private int systemSecurityCount;

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
        if (systemTagCount > 0) {
            systemTags.executeBatch();
            systemTagCount = 0;
        }
        if (systemProfileCount > 0) {
            systemProfiles.executeBatch();
            systemProfileCount = 0;
        }
        if (systemSecurityCount > 0) {
            systemSecurity.executeBatch();
            systemSecurityCount = 0;
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
                systemStringCount = 0;
            }
        }

        if (systemDates != null) {
            try {
                systemDates.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemDates = null;
                systemDateCount = 0;
            }
        }
        if (systemTags != null) {
            try {
                systemTags.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemTags = null;
                systemTagCount = 0;
            }
        }
        if (systemProfiles != null) {
            try {
                systemProfiles.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemProfiles = null;
                systemProfileCount = 0;
            }
        }
        if (systemSecurity != null) {
            try {
                systemSecurity.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemSecurity = null;
                systemProfileCount = 0;
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

    public void addTag(long logicalResourceId, long commonTokenValueId, short shardKey) throws SQLException {
        if (systemTags == null) {
            final String INS = "INSERT INTO logical_resource_tags(common_token_value_id, logical_resource_id, shard_key) VALUES (?,?,?)";
            systemTags = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemTags);
        psh.setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .setShort(shardKey)
            .addBatch();
        systemTagCount++;
    }

    public void addProfile(long logicalResourceId, long canonicalId, String version, String fragment, short shardKey) throws SQLException {
        if (systemProfiles == null) {
            final String INS = "INSERT INTO logical_resource_profiles(canonical_id, logical_resource_id, shard_key, version, fragment) VALUES (?,?,?,?,?)";
            systemProfiles = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemProfiles);
        psh.setLong(canonicalId)
            .setLong(logicalResourceId)
            .setShort(shardKey)
            .setString(version)
            .setString(fragment)
            .addBatch();
        systemProfileCount++;
    }

    public void addSecurity(long logicalResourceId, long commonTokenValueId, short shardKey) throws SQLException {
        if (systemTags == null) {
            final String INS = "INSERT INTO logical_resource_security(common_token_value_id, logical_resource_id, shard_key) VALUES (?,?,?)";
            systemSecurity = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemSecurity);
        psh.setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .setShort(shardKey)
            .addBatch();
        systemSecurityCount++;
    }
}