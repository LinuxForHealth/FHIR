/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.version.SchemaConstants;

/**
 * Set the current value for xxx_LOGICAL_RESOURCES.IS_DELETED. Called as
 * part of the schema migration step for schema version V0010 and V0011.
 * Note that for this to work for the multi-tenant (Db2) schema,
 * the SV_TENANT_ID needs to be set first.
 */
public class InitializeLogicalResourceDenorms implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(InitializeLogicalResourceDenorms.class.getName());
    private final String schemaName;
    private final String resourceTypeName;
    
    // Do not make static. Calendar is not thread-safe
    private final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public InitializeLogicalResourceDenorms(String schemaName, String resourceTypeName) {
        this.schemaName = schemaName;
        this.resourceTypeName = resourceTypeName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        if (translator.getType() == DbType.DERBY) {
            runForDerby(translator, c);
        } else {
            runCorrelatedUpdate(translator, c);
        }
    }

    /**
     * Perform the update using a correlated update statement, which works for Db2
     * and PostgresSQL
     * @param translator
     * @param c
     */
    private void runCorrelatedUpdate(IDatabaseTranslator translator, Connection c) {
        final String lrTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_LOGICAL_RESOURCES");
        final String rTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_RESOURCES");

        // Correlated update to grab the IS_DELETED and LAST_UPDATED values from xxx_RESOURCES and use them to
        // set xxx_LOGICAL_RESOURCES.IS_DELETED and LAST_UPDATED for the current_resource_id.
        final String DML = "UPDATE " + lrTable + " lr "
                + " SET (is_deleted, last_updated, version_id) = (SELECT r.is_deleted, r.last_updated, r.version_id FROM " + rTable + " r WHERE r.resource_id = lr.current_resource_id)"
                ;

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.executeUpdate();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Derby doesn't support correlated update statements, so we have to
     * do this manually
     * @param translator
     * @param c
     */
    private void runForDerby(IDatabaseTranslator translator, Connection c) {
        final String lrTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_LOGICAL_RESOURCES");
        final String rTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_RESOURCES");
        // Fetch the is_deleted and last_updated statement from the current version of each resource...
        final String select = ""
                + "SELECT lr.logical_resource_id, r.is_deleted, r.last_updated, r.version_id "
                + "  FROM " + rTable  + "  r, "
                + "       " + lrTable + " lr  "
                + " WHERE lr.current_resource_id = r.resource_id";

        // ...and use it to set the new column values in the corresponding XXX_LOGICAL_RESOURCES table
        final String update = "UPDATE " + lrTable + " SET is_deleted = ?, last_updated = ?, version_id = ? WHERE logical_resource_id = ?";

        try (Statement selectStatement = c.createStatement();
             PreparedStatement updateStatement = c.prepareStatement(update)) {
            ResultSet rs = selectStatement.executeQuery(select);

            int batchCount = 0;
            while (rs.next()) {
                long logicalResourceId = rs.getLong(1);
                String isDeleted = rs.getString(2);
                Timestamp lastUpdated = rs.getTimestamp(3, UTC);
                int versionId = rs.getInt(4);

                if (logger.isLoggable(Level.FINEST)) {
                    // log the update in a form which is useful for debugging
                    logger.finest("UPDATE " + lrTable
                        + " SET   is_deleted = '" + isDeleted + "'"
                        + ",    last_updated = '" + lastUpdated.toString() + "'"
                        + ",      version_id = " + versionId
                        + " WHERE logical_resource_id = " + logicalResourceId);
                }
                updateStatement.setString(1, isDeleted);
                updateStatement.setTimestamp(2, lastUpdated, UTC);
                updateStatement.setInt(3, versionId);
                updateStatement.setLong(4, logicalResourceId);
                updateStatement.addBatch();

                if (++batchCount == 500) {
                    updateStatement.executeBatch();
                    batchCount = 0;
                }
            }

            if (batchCount > 0) {
                updateStatement.executeBatch();
            }

        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}