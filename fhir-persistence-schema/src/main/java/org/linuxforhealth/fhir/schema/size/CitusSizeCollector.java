/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.schema.app.util.SchemaSupport;

/**
 * Collect size metrics for a Citus database and add them
 * to the model
 * @implNote see https://github.com/citusdata/citus_docs/issues/231
 */
public class CitusSizeCollector implements ISizeCollector {
    private static final Logger logger = Logger.getLogger(PostgresSizeCollector.class.getName());

    // the model used to collect the size info
    private final FHIRDbSizeModel model;

    /**
     * Public constructor
     * @param model
     */
    public CitusSizeCollector(FHIRDbSizeModel model) {
        this.model = model;
    }

    @Override
    public void run(String schemaName, Connection connection, IDatabaseTranslator translator) {
        collectTableInfo(schemaName, connection, translator);
        collectIndexInfo(schemaName, connection, translator);
    }

    private void collectTableInfo(String schemaName, Connection connection, IDatabaseTranslator translator) {
        final String select = ""
                + "SELECT  table_name, "
                + "        0 AS row_estimate, "
                + "        total_bytes, "
                + "        total_bytes - table_bytes AS index_bytes, "
                + "        0 AS toast_bytes, "
                + "        table_bytes "
                + "   FROM (SELECT c.oid, "
                + "                nspname AS table_schema, "
                + "                relname AS table_name, "
                + "                citus_total_relation_size(p.logicalrelid) AS total_bytes, "
                + "                citus_table_size(p.logicalrelid) AS table_bytes "
                + "           FROM pg_class c "
                + "      LEFT JOIN pg_namespace n "
                + "             ON n.oid = c.relnamespace "
                + "      LEFT JOIN pg_dist_partition p "
                + "             ON p.logicalrelid = (nspname || '.' || relname)::regclass "
                + "          WHERE relkind = 'r' "
                + "            AND nspname = ?"
                + "   ) a";
        
        
        logger.info("Collecting Citus table size info for schema: '" + schemaName.toLowerCase() + "'");
        SchemaSupport util = new SchemaSupport();
        try (PreparedStatement ps = connection.prepareStatement(select)) { 
            ps.setString(1, schemaName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String tableName = rs.getString(1);
                final long rowEstimate = rs.getLong(2);
                final long totalBytes = rs.getLong(3);
                final long indexBytes = rs.getLong(4);
                final long toastBytes = rs.getLong(5);
                
                // Note resourceType will be null for tables we don't care about
                final String resourceType = util.getResourceTypeFromTableName(tableName);
                if (resourceType != null) {
                    final boolean isParamTable = util.isParamTable(tableName);
                    
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine(String.format("%56s %34s %8d %10d %10d %10d", tableName, resourceType, rowEstimate, totalBytes, indexBytes, toastBytes));
                    }
                    model.accumulateTableSize(resourceType, tableName, isParamTable, totalBytes - indexBytes, rowEstimate);
                }
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    private void collectIndexInfo(String schemaName, Connection connection, IDatabaseTranslator translator) {
        logger.warning("Citus does not support index-level size reports");
    }
}