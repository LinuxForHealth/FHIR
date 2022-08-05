/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

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

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.model.DbType;

/**
 * Run a correlated update statement to update the new V0014 columns in LOGICAL_RESOURCES from the
 * corresponding values in the xx_LOGICAL_RESOURCES table (for a specific resource type)
 * Note that for this to work for the multi-tenant (Db2) schema,
 * the SV_TENANT_ID needs to be set first.
 */
public class MigrateV0014LogicalResourceIsDeletedLastUpdated implements IDatabaseStatement {
    private static final Logger LOG = Logger.getLogger(MigrateV0014LogicalResourceIsDeletedLastUpdated.class.getName());

    private final int MAX_CORRELATED_UPDATE_ROWS = 250000;

    // The FHIR data schema
    private final String schemaName;

    // The ressource type name for the subset of resources we want to process
    private final String resourceTypeName;

    // The database id for the resource type
    private final int resourceTypeId;

    // Do not make static. Calendar is not thread-safe
    private final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /**
     * Public constructor
     * @param schemaName
     * @param resourceTypeName
     * @param resourceTypeId
     */
    public MigrateV0014LogicalResourceIsDeletedLastUpdated(String schemaName, String resourceTypeName, int resourceTypeId) {
        this.schemaName = schemaName;
        this.resourceTypeName = resourceTypeName;
        this.resourceTypeId = resourceTypeId;
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
        // Correlated update to grab the IS_DELETED and LAST_UPDATED values from xxx_RESOURCES and use them to
        // set XX_LOGICAL_RESOURCES.IS_DELETED and LAST_UPDATED for the current_resource_id.
        final String srcTable = DataDefinitionUtil.getQualifiedName(schemaName, resourceTypeName + "_LOGICAL_RESOURCES");
        final String tgtTable = DataDefinitionUtil.getQualifiedName(schemaName, "LOGICAL_RESOURCES");

        final String DML = "UPDATE " + tgtTable + " tgt "
                + " SET (is_deleted, last_updated) = (SELECT src.is_deleted, src.last_updated FROM " + srcTable + " src WHERE tgt.logical_resource_id = src.logical_resource_id)"
                        + " WHERE tgt.logical_resource_id IN "
                        + "(SELECT tgt2.logical_resource_id FROM " + tgtTable + " tgt2"
                        + "  WHERE tgt2.resource_type_id = " + this.resourceTypeId + " AND tgt2.is_deleted = 'X' " + translator.limit(MAX_CORRELATED_UPDATE_ROWS + "") + ")";

        try (PreparedStatement ps = c.prepareStatement(DML)) {
            int count = 1;
            while (count > 0) {
                count = ps.executeUpdate();
                LOG.info("MigrateV0014 from '" + srcTable + "' blockSize = '" + count + "'  at  '" + java.time.Instant.now() + "'");
            }
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
        // Fetch the is_deleted and last_updated statement from the current version of each resource...
        final String select = ""
                + "SELECT lr.logical_resource_id, lr.is_deleted, lr.last_updated "
                + "  FROM " + lrTable + " lr  ";

        // ...and use it to set the new column values in the corresponding XXX_LOGICAL_RESOURCES table
        final String update = "UPDATE logical_resources SET is_deleted = ?, last_updated = ? WHERE logical_resource_id = ?";

        try (Statement selectStatement = c.createStatement();
            PreparedStatement updateStatement = c.prepareStatement(update)) {
            ResultSet rs = selectStatement.executeQuery(select);

            int batchCount = 0;
            while (rs.next()) {
                long logicalResourceId = rs.getLong(1);
                String isDeleted = rs.getString(2);
                Timestamp lastUpdated = rs.getTimestamp(3, UTC);

                if (LOG.isLoggable(Level.FINEST)) {
                    // log the update in a form which is useful for debugging
                    LOG.finest("UPDATE logical_resources "
                        + "   SET          is_deleted = '" + isDeleted + "'"
                        + ",             last_updated = '" + lastUpdated.toString() + "'"
                        + " WHERE logical_resource_id = " + logicalResourceId);
                }
                updateStatement.setString(1, isDeleted);
                updateStatement.setTimestamp(2, lastUpdated, UTC);
                updateStatement.setLong(3, logicalResourceId);
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