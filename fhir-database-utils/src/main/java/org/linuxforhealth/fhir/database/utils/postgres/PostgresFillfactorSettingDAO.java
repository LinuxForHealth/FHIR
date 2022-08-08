/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.model.With;

/**
 * This DAO changes the fillfactor settings on the given PostgreSQL table. The fillfactor can be used
 * to leave free space in the database block structures for more efficient updates at the cost of
 * additional space usage.
 * 
 * In case you are wondering, we use "PostgresFillfactor" not "PostgresFillFactor" because the "fillfactor"
 * property name is a single word.
 */
public class PostgresFillfactorSettingDAO implements IDatabaseStatement {

    private static final Logger LOG = Logger.getLogger(PostgresFillfactorSettingDAO.class.getName());

    // The schema name
    private final String schema;
    
    // The table name
    private final String tableName;

    // The fillfactor value we will be using
    private final int fillfactor;

    /**
     * Alters the fillfactor setting for PostgreSQL tables
     * @param schema
     * @param tableName
     * @param fillfactor
     */
    public PostgresFillfactorSettingDAO(String schema, String tableName, int fillfactor) {
        this.schema = schema;
        this.tableName = tableName;
        this.fillfactor = fillfactor;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        if (translator.isFamilyPostgreSQL()) {
            // assume we need to set it unless it's been configured already
            boolean isFillfactor = true;
            LOG.fine(() -> "Checking the table fillfactor settings");
            final String statement0 = "select (reloptions)::VARCHAR "
                    + "from pg_class "
                    + "join pg_namespace on pg_namespace.oid = pg_class.relnamespace "
                    + "where relname = LOWER('" + tableName + "') "
                    + "and pg_namespace.nspname = LOWER('" + schema + "')";
            try (PreparedStatement ps = c.prepareStatement(statement0)) {
                ps.execute();
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    String settings = rs.getString(1);
                    if (!rs.wasNull()) {
                        String[] sar = settings
                                    .replace("{", "")
                                    .replace("}", "")
                                    .split(",");
                        for (String setting : sar) {
                            String[] kv = setting.split("=");
                            if ("fillfactor".equals(kv[0]) && fillfactor == Integer.parseInt(kv[1])) {
                                isFillfactor = false;
                            }
                        }
                    }
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }

            With withFillfactor = new With("fillfactor", Integer.toString(fillfactor));
            if (isFillfactor) {
                LOG.info("Starting update for the fillfactor on table '" + schema + "." + tableName + "'");
                // Build the SQL
                StringBuilder builder = new StringBuilder();
                builder.append("alter table ")
                    .append(schema).append(".").append(tableName)
                    .append(" SET ( ")
                    .append(withFillfactor.buildWithComponent())
                    .append(" )");
                final String statement1 = builder.toString();

                LOG.fine(() -> "Updating the table fillfactor settings " + statement1);
                int retry = 0;
                while (++retry <= 10) {
                    try (PreparedStatement ps = c.prepareStatement(statement1)) {
                        // IFF we hit a Lock (wait_event = relation) we'll just wait forever on the lock while a vacuum completes.
                        // we set a high number intentionally, and ensure it does eventually quit. This change should be about 100ms max, 
                        // it's only changing metadata.
                        ps.setQueryTimeout(1000);
                        ps.execute();
                    } catch (SQLException x) {
                        if (retry == 10) {
                            throw translator.translate(x);
                        } else {
                            LOG.warning("Retrying the fillfactor settings on [" + retry + "] '" + schema + "." + tableName + "'");
                        }
                    }
                }

                LOG.info("Completed update for the fillfactor on table '" + schema + "." + tableName + "'");
            } else {
                LOG.info("Skip update for the fillfactor on table (settings already configured) '" + schema + "." + tableName + "'");
            }
        }
    }
}