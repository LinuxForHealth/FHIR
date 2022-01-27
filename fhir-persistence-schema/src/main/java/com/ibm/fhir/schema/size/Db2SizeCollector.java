/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.schema.app.util.SchemaSupport;

/**
 * Collect size metrics for a Db2 database and add them
 * to the model
 */
public class Db2SizeCollector implements ISizeCollector {
    private static final Logger logger = Logger.getLogger(Db2SizeCollector.class.getName());

    // The model to which we add the info pulled from the DB
    private final FHIRDbSizeModel model;

    // The tenant name identifying the tenant from FHIR_ADMIN.TENANTS
    private final String tenantName;

    /**
     * Public constructor
     * @param model
     */
    public Db2SizeCollector(FHIRDbSizeModel model, String tenantName) {
        this.model = model;
        this.tenantName = tenantName;
    }

    @Override
    public void run(String schemaName, Connection connection, IDatabaseTranslator translator) {
        final String usn = schemaName.toUpperCase();
        int dataPartitionId = getDataPartitionId(schemaName, connection, translator);
        collectTableInfo(usn, connection, translator, dataPartitionId);
        collectIndexInfo(usn, connection, translator, dataPartitionId);
    }

    private void collectTableInfo(String schemaName, Connection connection, IDatabaseTranslator translator, int dataPartitionId) {
        // Note our schema does not use long or XML data types, so we don't need to include
        // their size. But it is important to target only the data partition associated with
        // the tenant.
        final String SQL = ""
                + "SELECT ati.tabname, tabs.card, "
                + "       (data_object_p_size + lob_object_p_size) * 1024 AS table_bytes "
                + "  FROM sysibmadm.admintabinfo AS ati, "
                + "       syscat.tables tabs "
                + " WHERE ati.tabschema = ? "
                + "   AND tabs.tabschema = ati.tabschema "
                + "   AND tabs.tabname = ati.tabname "
                + "   AND ati.data_partition_id = ? "
                + "   AND ati.tabname NOT LIKE 'DRP_%'" // for Db2 don't include the partition drop tables
                ;

        logger.info("Collecting Db2 table size info for schema: '" + schemaName + "'");
        SchemaSupport util = new SchemaSupport();
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, schemaName);
            ps.setInt(2, dataPartitionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String tableName = rs.getString(1);
                final long rowEstimate = rs.getLong(2);
                final long totalBytes = rs.getLong(3);
                
                // Note resourceType will be null for tables we don't care about
                final String resourceType = util.getResourceTypeFromTableName(tableName);
                if (resourceType != null) {
                    final boolean isParamTable = util.isParamTable(tableName);
                    
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine(String.format("%56s %34s %8d %10d", tableName, resourceType, rowEstimate, totalBytes));
                    }
                    model.accumulateTableSize(resourceType, tableName, isParamTable, totalBytes, rowEstimate);
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Get index info
     * @param schemaName
     * @param connection
     * @param translator
     */
    private void collectIndexInfo(String schemaName, Connection connection, IDatabaseTranslator translator, int dataPartitionId) {
        final String SQL = ""
                + "SELECT tabname AS table_name,"
                + "       indname AS index_name,"
                + "       index_object_p_size * 1024 as index_size "
                + "  FROM TABLE(sysproc.admin_get_index_info('I', ?,'')) "
                + " WHERE tabname NOT LIKE 'DRP_%' " // for Db2 don't include the partition drop tables
                + "   AND datapartitionid = ? " // make sure we only get data for the tenant's partition
                ;

        logger.info("Collecting Db2 index size info for schema: '" + schemaName + "'");
        SchemaSupport util = new SchemaSupport();
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, schemaName);
            ps.setInt(2, dataPartitionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String tableName = rs.getString(1);
                final String indexName = rs.getString(2);
                final long indexBytes = rs.getLong(3);
                
                // Note resourceType will be null for tables we don't care about
                final String resourceType = util.getResourceTypeFromTableName(tableName);
                if (resourceType != null) {
                    final boolean isParamTable = util.isParamTable(tableName);
                    
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine(String.format("%56s %56s %34s %10d", tableName, indexName, resourceType, indexBytes));
                    }
                    model.accumulateIndexSize(resourceType, tableName, isParamTable, indexName, indexBytes);
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
    
    /**
     * Get the DB partition id for the tenant.
     * 
     * @param schemaName
     * @param connection
     * @param translator
     * @return
     */
    private int getDataPartitionId(String schemaName, Connection connection, IDatabaseTranslator translator) {
        int dataPartitionId;
        // query expected to return a single row
        final String SQL = ""
                + "SELECT dp.datapartitionid "
                + "  FROM syscat.datapartitions dp,"
                + "       fhir_admin.tenants t "
                + " WHERE t.tenant_name = ? "
                + "   AND dp.tabschema = ? "
                + "   AND dp.tabname = 'PARAMETER_NAMES' " // any of our partitioned tables
                + "   AND dp.datapartitionname = 'TENANT' || t.mt_id"; // connect our tenant to db2 data partition
        
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, this.tenantName);
            ps.setString(2, schemaName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dataPartitionId = rs.getInt(1);
            } else {
                throw new DataAccessException("Invalid tenant name/schema combination: tenantName='" 
                        + this.tenantName + "', schemaName='" + schemaName + "'");
            }

            if (rs.next()) {
                // Just a safety check to make sure we haven't butchered the join
                throw new DataAccessException("Query returned multiple matches: " + SQL);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return dataPartitionId;
    }
}
