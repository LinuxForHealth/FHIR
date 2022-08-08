/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.linuxforhealth.fhir.database.utils.common.CalendarHelper;
import org.linuxforhealth.fhir.database.utils.common.PreparedStatementHelper;

/**
 * Batch insert statements for system-level parameters
 * @implNote targets the plain variant of the schema
 * without an explicit sharding column
 */
public class PlainPostgresSystemParameterBatch {
    private final Connection connection;

    private PreparedStatement systemStrings;
    private int systemStringCount;

    private PreparedStatement systemDates;
    private int systemDateCount;

    private PreparedStatement systemResourceTokenRefs;
    private int systemResourceTokenRefCount;

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
    public PlainPostgresSystemParameterBatch(Connection c) {
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
        if (systemResourceTokenRefCount > 0) {
            systemResourceTokenRefs.executeBatch();
            systemResourceTokenRefCount = 0;
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
        if (systemResourceTokenRefs != null) {
            try {
                systemResourceTokenRefs.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                systemResourceTokenRefs = null;
                systemResourceTokenRefCount = 0;
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
     * Add a string parameter value to the whole-system batch statement
     * 
     * @param logicalResourceId
     * @param parameterNameId
     * @param strValue
     * @param strValueLower
     * @throws SQLException
     */
    public void addString(long logicalResourceId, int parameterNameId, String strValue, String strValueLower) throws SQLException {
        // System level string attributes
        if (systemStrings == null) {
            final String insertSystemString = "INSERT INTO str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id) VALUES (?,?,?,?)";
            systemStrings = connection.prepareStatement(insertSystemString);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemStrings);
        psh.setInt(parameterNameId)
            .setString(strValue)
            .setString(strValueLower)
            .setLong(logicalResourceId)
            .addBatch();
        systemStringCount++;
    }

    /**
     * Add a date parameter value to the whole-system batch statement
     * @param logicalResourceId
     * @param parameterNameId
     * @param dateStart
     * @param dateEnd
     * @param compositeId
     * @throws SQLException
     */
    public void addDate(long logicalResourceId, int parameterNameId, Timestamp dateStart, Timestamp dateEnd, Integer compositeId) throws SQLException {
        if (systemDates == null) {
            final String insertSystemDate = "INSERT INTO date_values (parameter_name_id, date_start, date_end, logical_resource_id) VALUES (?,?,?,?)";
            systemDates = connection.prepareStatement(insertSystemDate);
        }
        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        systemDates.setInt(1, parameterNameId);
        systemDates.setTimestamp(2, dateStart, UTC);
        systemDates.setTimestamp(3, dateEnd, UTC);
        systemDates.setLong(4, logicalResourceId);
        systemDates.addBatch();
        systemDateCount++;
    }

    /**
     * Add a token parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param parameterNameId
     * @param commonTokenValueId
     * @throws SQLException
     */
    public void addResourceTokenRef(long logicalResourceId, int parameterNameId, long commonTokenValueId) throws SQLException {
        if (systemResourceTokenRefs == null) {
            final String tokenString = "INSERT INTO resource_token_refs (parameter_name_id, common_token_value_id, logical_resource_id) VALUES (?,?,?)";
            systemResourceTokenRefs = connection.prepareStatement(tokenString);
        }
        systemResourceTokenRefs.setInt(1, parameterNameId);
        systemResourceTokenRefs.setLong(2, commonTokenValueId);
        systemResourceTokenRefs.setLong(3, logicalResourceId);
        systemResourceTokenRefs.addBatch();
        systemResourceTokenRefCount++;
    }

    /**
     * Add a tag parameter value to the whole-system batch statement
     * 
     * @param logicalResourceId
     * @param commonTokenValueId
     * @throws SQLException
     */
    public void addTag(long logicalResourceId, long commonTokenValueId) throws SQLException {
        if (systemTags == null) {
            final String INS = "INSERT INTO logical_resource_tags(common_token_value_id, logical_resource_id) VALUES (?,?)";
            systemTags = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemTags);
        psh.setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .addBatch();
        systemTagCount++;
    }

    /**
     * Add a profile parameter value to the whole-system batch statement
     * @param logicalResourceId
     * @param canonicalId
     * @param version
     * @param fragment
     * @throws SQLException
     */
    public void addProfile(long logicalResourceId, long canonicalId, String version, String fragment) throws SQLException {
        if (systemProfiles == null) {
            final String INS = "INSERT INTO logical_resource_profiles(canonical_id, logical_resource_id, version, fragment) VALUES (?,?,?,?)";
            systemProfiles = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemProfiles);
        psh.setLong(canonicalId)
            .setLong(logicalResourceId)
            .setString(version)
            .setString(fragment)
            .addBatch();
        systemProfileCount++;
    }

    /**
     * Add a security parameter value to the whole-system batch statement
     * @param logicalResourceId
     * @param commonTokenValueId
     * @throws SQLException
     */
    public void addSecurity(long logicalResourceId, long commonTokenValueId) throws SQLException {
        if (systemSecurity == null) {
            final String INS = "INSERT INTO logical_resource_security(common_token_value_id, logical_resource_id) VALUES (?,?)";
            systemSecurity = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(systemSecurity);
        psh.setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .addBatch();
        systemSecurityCount++;
    }
}