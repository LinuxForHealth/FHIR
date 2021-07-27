/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.With;

/**
 * Per the Performance Guide, this DAO implements VACUUM setting changes.
 * https://ibm.github.io/FHIR/guides/FHIRPerformanceGuide/#412-tuning-auto-vacuum
 *
 * Lower the trigger threshold for starting work
 * alter table fhirdata.logical_resources SET (autovacuum_vacuum_scale_factor = 0.01, autovacuum_vacuum_threshold=1000);
 *
 * Increase the amount of work vacuuming completes before taking a breather (default is typically 200)
 * alter table fhirdata.logical_resources SET (autovacuum_vacuum_cost_limit=2000);
 */
public class PostgresVacuumSettingDAO implements IDatabaseStatement {

    private static final Logger LOG = Logger.getLogger(PostgresVacuumSettingDAO.class.getName());

    private String schema = null;
    private String tableName = null;

    private int vacuumCostLimit = 0;
    private Double vacuumScaleFactor = null;
    private int vacuumThreshold = 0;

    /**
     * sets up the vacuum setting for Postgres
     *
     * @param schema
     * @param tableName
     * @param vacuumCostLimit
     * @param vacuumScaleFactor
     * @param vacuumThreshold
     */
    public PostgresVacuumSettingDAO(String schema, String tableName, int vacuumCostLimit, Double vacuumScaleFactor, int vacuumThreshold) {
        this.schema = schema;
        this.tableName = tableName;
        this.vacuumCostLimit = vacuumCostLimit;
        this.vacuumScaleFactor = vacuumScaleFactor;
        this.vacuumThreshold = vacuumThreshold;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        if (translator.getType() == DbType.POSTGRESQL) {
            boolean isScaleFactor = true;
            boolean isVacuumThreshold = true;
            boolean isVacuumCostLimit = true;
            LOG.fine(() -> "Checking the table vacuum settings");
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
                            if ("autovacuum_vacuum_scale_factor".equals(kv[0]) && (vacuumScaleFactor == null || vacuumScaleFactor == Double.parseDouble(kv[1]))) {
                                // Setting already in place or we are skipping the autovacuum_vacuum_scale_factor
                                isScaleFactor = false;
                            } else if ("autovacuum_vacuum_threshold".equals(kv[0]) && vacuumThreshold == Integer.parseInt(kv[1])) {
                                isVacuumThreshold = false;
                            } else if ("autovacuum_vacuum_cost_limit".equals(kv[0]) && vacuumCostLimit == Integer.parseInt(kv[1])) {
                                isVacuumCostLimit = false;
                            }
                        }
                    }
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }

            List<With> withs = generateWiths(isScaleFactor, isVacuumThreshold, isVacuumCostLimit);
            if (!withs.isEmpty()) {
                LOG.info("Starting update for the autovacuum on table '" + schema + "." + tableName + "'");
                // Build the SQL
                StringBuilder builder = new StringBuilder();
                builder.append("alter table ")
                    .append(schema).append(".").append(tableName)
                    .append(" SET ( ")
                    .append(withs.stream()
                                    .map(with -> with.buildWithComponent())
                                    .collect(Collectors.joining(",")))
                    .append(" )");
                final String statement1 = builder.toString();

                LOG.fine(() -> "Updating the table vacuum settings " + statement1);
                int retry = 0;
                while (++retry <= 10) {
                    try (PreparedStatement ps = c.prepareStatement(statement1)) {
                        // IFF we hit a Lock (wait_event = relation) we'll just wait forever on the lock while the vacuum completes.
                        // we set a high number intentionally, and ensure it does eventually quit. This change should be about 100ms max, it's only changing
                        // metadata.
                        ps.setQueryTimeout(1000);
                        ps.execute();
                    } catch (SQLException x) {
                        if (retry == 10) {
                            throw translator.translate(x);
                        } else {
                            LOG.warning("Retrying the vacuum settings on [" + retry + "] '" + schema + "." + tableName + "'");
                        }
                    }
                }

                LOG.info("Completed update for the autovacuum on table '" + schema + "." + tableName + "'");
            } else {
                LOG.info("Skip update for the autovacuum on table (settings already configured) '" + schema + "." + tableName + "'");
            }
        }
    }

    /**
     * Selective generation of the settings
     * @param isScaleFactor
     * @param isVacuumThreshold
     * @param isVacuumCostLimit
     * @return
     */
    private List<With> generateWiths(boolean isScaleFactor, boolean isVacuumThreshold, boolean isVacuumCostLimit){
        List<With> withs = new ArrayList<>();

        if (isScaleFactor && vacuumScaleFactor != null) {
            withs.add(With.with("autovacuum_vacuum_scale_factor", Double.toString(vacuumScaleFactor)));
        }
        if (isVacuumThreshold) {
            withs.add(With.with("autovacuum_vacuum_threshold", Integer.toString(vacuumThreshold)));
        }
        if (isVacuumCostLimit) {
            withs.add(With.with("autovacuum_vacuum_cost_limit", Integer.toString(vacuumCostLimit)));
        }
        return withs;
    }
}