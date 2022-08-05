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
 * Collect size metrics for a PostgreSQL database and add them
 * to the model
 */
public class PostgresSizeCollector implements ISizeCollector {
    private static final Logger logger = Logger.getLogger(PostgresSizeCollector.class.getName());

    // the model used to collect the size info
    private final FHIRDbSizeModel model;

    /**
     * Public constructor
     * @param model
     */
    public PostgresSizeCollector(FHIRDbSizeModel model) {
        this.model = model;
    }

    @Override
    public void run(String schemaName, Connection connection, IDatabaseTranslator translator) {
        collectTableInfo(schemaName, connection, translator);
        collectIndexInfo(schemaName, connection, translator);
    }

    private void collectTableInfo(String schemaName, Connection connection, IDatabaseTranslator translator) {
        final String SQL = ""
                + "SELECT  table_name, row_estimate, total_bytes, index_bytes, toast_bytes,"
                + "        total_bytes-index_bytes-coalesce(toast_bytes,0) AS table_bytes"
                + "  FROM (SELECT c.oid,"
                + "               nspname AS table_schema,"
                + "               relname AS table_name,"
                + "               c.reltuples AS row_estimate,"
                + "               pg_total_relation_size(c.oid) AS total_bytes,"
                + "               pg_indexes_size(c.oid) AS index_bytes,"
                + "               pg_total_relation_size(reltoastrelid) AS toast_bytes"
                + "          FROM pg_class c"
                + "     LEFT JOIN pg_namespace n "
                + "            ON n.oid = c.relnamespace"
                + "         WHERE relkind = 'r'"
                + "           AND nspname = ?"
                + "  ) a";

        logger.info("Collecting PostgreSQL table size info for schema: '" + schemaName.toLowerCase() + "'");
        SchemaSupport util = new SchemaSupport();
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
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
        final String SQL = ""
                + "    SELECT t.tablename,"
                + "           psai.indexrelname                              AS index_name, "
                + "           pg_relation_size(i.indexrelid)                 AS index_size, "
                + "           CASE WHEN i.indisunique THEN 'Y' ELSE 'N' END  AS is_unique, "
                + "           psai.idx_scan                                  AS number_of_scans, "
                + "           psai.idx_tup_read                              AS tuples_read,"
                + "           psai.idx_tup_fetch                             AS tuples_fetched "
                + "      FROM pg_tables t "
                + " LEFT JOIN pg_class c ON t.tablename = c.relname "
                + " LEFT JOIN pg_index i ON c.oid = i.indrelid "
                + " LEFT JOIN pg_stat_all_indexes psai ON i.indexrelid = psai.indexrelid "
                + "     WHERE t.schemaname = ? "
                + "       AND psai.indexrelname IS NOT NULL "
                ;

        logger.info("Collecting PostgreSQL index size info for schema: '" + schemaName.toLowerCase() + "'");
        SchemaSupport util = new SchemaSupport();
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, schemaName.toLowerCase());
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
}