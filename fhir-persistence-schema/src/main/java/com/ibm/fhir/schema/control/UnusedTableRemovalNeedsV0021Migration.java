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
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Checks to see if any of the tables exist in the target database.
 */
public class UnusedTableRemovalNeedsV0021Migration implements IDatabaseSupplier<Boolean> {

    // List of Tables to check for removal
    public static final List<String> DEPRECATED_TABLES =
            Arrays.asList(
                // DOMAINRESOURCE
                "DOMAINRESOURCE_COMPOSITES",
                "DOMAINRESOURCE_DATE_VALUES",
                "DOMAINRESOURCE_LATLNG_VALUES",
                "DOMAINRESOURCE_LOGICAL_RESOURCES",
                "DOMAINRESOURCE_NUMBER_VALUES",
                "DOMAINRESOURCE_PROFILES",
                "DOMAINRESOURCE_QUANTITY_VALUES",
                "DOMAINRESOURCE_RESOURCE_TOKEN_REFS",
                "DOMAINRESOURCE_RESOURCES",
                "DOMAINRESOURCE_SECURITY",
                "DOMAINRESOURCE_STR_VALUES",
                "DOMAINRESOURCE_TAGS",
                "DOMAINRESOURCE_TOKEN_VALUES",
                // RESOURCE
                "RESOURCE_COMPOSITES",
                "RESOURCE_DATE_VALUES",
                "RESOURCE_LATLNG_VALUES",
                "RESOURCE_LOGICAL_RESOURCES",
                "RESOURCE_NUMBER_VALUES",
                "RESOURCE_PROFILES",
                "RESOURCE_QUANTITY_VALUES",
                "RESOURCE_RESOURCE_TOKEN_REFS",
                "RESOURCE_RESOURCES",
                "RESOURCE_SECURITY",
                "RESOURCE_STR_VALUES",
                "RESOURCE_TAGS",
                "RESOURCE_TOKEN_VALUES"
                );

    // Table Count
    public static final int TABLE_COUNT = DEPRECATED_TABLES.size();

    // The FHIR data schema
    private final String schemaName;

    /**
     * Public Constructor
     * @param schemaName
     */
    public UnusedTableRemovalNeedsV0021Migration(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        switch (translator.getType()) {
        case POSTGRESQL:
        case CITUS:
            return checkPostgres(translator, c);
        case DERBY:
            return checkDerby(translator, c);
        default:
            // if another database is supported, we won't have the tables at this point.
            // Therefore it's always going to be false.
            return false;
        }
    }

    /**
     * Checks derby's sys catalog
     *
     * @param translator
     * @param c
     * @return
     */
    public boolean checkDerby(IDatabaseTranslator translator, Connection c) {
        // Grab the list of tables for the configured schema from the Derby sys catalog
        final String sql = ""
                + "SELECT 1 FROM sys.systables AS tables "
                + "  JOIN sys.sysschemas AS schemas "
                + "    ON (tables.schemaid = schemas.schemaid) "
                + " WHERE schemas.schemaname = ?"
                + "     AND tables.tablename in (" + addParameterMarkers(TABLE_COUNT) + ")";
        return hasTables(translator, c, sql);
    }

    /**
     * Checks postgresql for the tables in the schema
     *
     * @param translator
     * @param c
     * @return
     */
    public boolean checkPostgres(IDatabaseTranslator translator, Connection c) {
        // Grab the list of tables for the configured schema from the PostgreSQL schema
        // catalog
        final String sql = ""
                + "SELECT 1 FROM information_schema.tables "
                + " WHERE table_schema = lower(?)"
                + "     AND table_name in (" + addParameterMarkers(TABLE_COUNT) + ")";
        return hasTables(translator, c, sql);
    }

    /**
     * builds the parameter markers
     * @param numOfParameters
     * @return
     */
    private String addParameterMarkers(int numOfParameters) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfParameters; i++) {
            builder.append(" ? ");
            if (numOfParameters-1 != i) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    /**
     *
     * @param translator
     * @param c
     * @param sql
     * @return
     */
    private boolean hasTables(IDatabaseTranslator translator, Connection c, final String sql) {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, schemaName);

            for (String deprecatedTable : DEPRECATED_TABLES) {
                if (translator.isFamilyPostgreSQL()) {
                    ps.setString(i++, deprecatedTable.toLowerCase());
                } else {
                    ps.setString(i++, deprecatedTable);
                }
            }
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}