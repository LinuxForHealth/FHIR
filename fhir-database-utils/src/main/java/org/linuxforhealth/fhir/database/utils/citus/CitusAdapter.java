/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.api.DistributionContext;
import org.linuxforhealth.fhir.database.utils.api.DistributionType;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTarget;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.model.CheckConstraint;
import org.linuxforhealth.fhir.database.utils.model.ColumnBase;
import org.linuxforhealth.fhir.database.utils.model.IdentityDef;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;
import org.linuxforhealth.fhir.database.utils.model.PrimaryKeyDef;
import org.linuxforhealth.fhir.database.utils.model.With;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresAdapter;


/**
 * A database adapter implementation for Citus (distributed PostgreSQL)
 */
public class CitusAdapter extends PostgresAdapter {
    private static final Logger logger = Logger.getLogger(CitusAdapter.class.getName());

    /**
     * Public constructor
     * @param target
     */
    public CitusAdapter(IDatabaseTarget target) {
        super(target);
    }

    /**
     * Public constructor
     * @param cp
     */
    public CitusAdapter(IConnectionProvider cp) {
        super(cp);
    }

    @Override
    public void createTable(String schemaName, String name, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
        IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
        DistributionContext distributionContext) {

        if (distributionContext != null) {
            // Build a Citus-specific create table statement
            String ddl = buildCitusCreateTableStatement(schemaName, name, columns, primaryKey, identity, withs, checkConstraints, distributionContext);
            runStatement(ddl);
        } else {
            // building a plain schema, so we can use the standard PostgreSQL method
            super.createTable(schemaName, name, columns, primaryKey, identity, tablespaceName, withs, checkConstraints, distributionContext);
        }
    }

    /**
     * Construct a CREATE TABLE statement using some Citus-specific business
     * logic.
     * @param schema
     * @param name
     * @param columns
     * @param pkDef
     * @param identity
     * @param withs
     * @param checkConstraints
     * @param distributionType
     * @return
     */
    private String buildCitusCreateTableStatement(String schema, String name, List<ColumnBase> columns, 
            PrimaryKeyDef pkDef, IdentityDef identity, List<With> withs,
            List<CheckConstraint> checkConstraints, DistributionContext distributionContext) {

        final DistributionType distributionType = distributionContext.getDistributionType();
        final String distributionColumnName = distributionContext.getDistributionColumnName();
        if (identity != null && (distributionType == DistributionType.DISTRIBUTED || distributionType == DistributionType.REFERENCE)) {
            logger.warning("Citus: Ignoring IDENTITY columns on distributed table: '" + name + "." + identity.getColumnName());
            identity = null;
        }

        StringBuilder result = new StringBuilder();
        result.append("CREATE TABLE ");
        result.append(getQualifiedName(schema, name));
        result.append('(');
        result.append(buildColumns(columns, identity));

        if (pkDef != null) {
            // Add the primary key definition after the columns. For Citus, if the table is 
            // distributed (sharded) then the distribution key MUST be one of the columns
            // of the primary key. Make sure we lower-case things first so we guarantee a
            // match where expected
            String pkColString = String.join(",", pkDef.getColumns());
            Set<String> pkSet = pkDef.getColumns().stream().map(c -> c.toLowerCase()).collect(Collectors.toSet());
            final String ldc = distributionType == DistributionType.DISTRIBUTED ? distributionColumnName.toLowerCase() : null;
            if (ldc == null || pkSet.contains(ldc)) {
                result.append(", CONSTRAINT ");
                result.append(pkDef.getConstraintName());
                result.append(" PRIMARY KEY (");
                result.append(pkColString);
                result.append(')');
            } else {
                // Hopefully this is an intended data model design decision. Because it's so
                // fundamental, we always want to warn about it.
                logger.warning("Skipping primary key for table '" + name 
                    + "' because it does not include required distribution column: '" + ldc 
                    + "', only (" + pkColString + ")");
            }
        }
        
        // Add any CHECK constraints
        for (CheckConstraint cc: checkConstraints) {
            result.append(", CONSTRAINT ");
            result.append(cc.constraintName);
            result.append(" CHECK (");
            result.append(cc.getConstraintExpression());
            result.append(")");
        }
        result.append(')');

        // Creates WITH (fillfactor=70, key2=val2);
        if (withs != null && !withs.isEmpty()) {
            StringBuilder builder = new StringBuilder(" WITH (");
            builder.append(
                withs.stream()
                    .map(with -> with.buildWithComponent())
                    .collect(Collectors.joining(",")));
            builder.append(")");
            result.append(builder.toString());
        }

        return result.toString();
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns, DistributionContext distributionContext) {
        // For Citus, we are prevented from creating a unique index unless the index contains
        // the distribution column
        final DistributionType distributionType = distributionContext.getDistributionType();
        final String distributionColumnName = distributionContext.getDistributionColumnName();
        List<String> columnNames = indexColumns.stream().map(ocd -> ocd.getColumnName()).collect(Collectors.toList());
        if (distributionType == DistributionType.DISTRIBUTED && !includesDistributionColumn(distributionColumnName, columnNames)) {
            // Can only a normal index because it isn't partitioned by the distributionColumn
            String ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
            runStatement(ddl);
        } else {
            // Index is partitioned by the distributionColumn, so it can be unique
            String ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
            runStatement(ddl);
        }
    }

    @Override
    public void applyDistributionRules(String schemaName, String tableName, DistributionContext distributionContext) {
        // Apply the distribution rules. Tables without distribution rules are created
        // only on Citus controller nodes and never distributed to the worker nodes.
        final String fullName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        CitusDistributionCheckDAO distributionCheck = new CitusDistributionCheckDAO(schemaName, tableName);
        if (runStatement(distributionCheck)) {
            logger.info("Table '" + fullName + "' is already distributed");
            return;
        }

        final DistributionType distributionType = distributionContext.getDistributionType();
        final String distributionColumnName = distributionContext.getDistributionColumnName();
        if (distributionType == DistributionType.REFERENCE) {
            // A table that is fully replicated for each worker node
            logger.info("Citus: distributing reference table '" + fullName + "'");
            CreateReferenceTableDAO dao = new CreateReferenceTableDAO(schemaName, tableName);
            runStatement(dao);
        } else if (distributionType == DistributionType.DISTRIBUTED) {
            // A table that is sharded using a hash on the distributionColumn value
            logger.info("Citus: Sharding table '" + fullName + "' using '" + distributionColumnName + "'");
            CreateDistributedTableDAO dao = new CreateDistributedTableDAO(schemaName, tableName, distributionColumnName);
            runStatement(dao);
        }
    }

    @Override
    public void distributeFunction(String schemaName, String functionName, int distributeByParamNumber) {
        if (distributeByParamNumber < 1) {
            throw new IllegalArgumentException("invalid distributeByParamNumber value: " + distributeByParamNumber);
        }
        // Need to get the signature text first in order to build the create_distribution_function
        // statement. Note the cast to ::regprocedure will return a string like this:
        // "fhirdata.add_logical_resource_ident(integer,character varying)"
        // which can be passed in to the Citus create_distributed_function procedure
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, functionName);
        final String SELECT =
                "SELECT p.oid::regprocedure " +
                "  FROM pg_catalog.pg_proc p " +
                " WHERE p.oid::regproc::text = LOWER(?)";

        if (connectionProvider != null) {
            try (Connection c = connectionProvider.getConnection()) {
                String functionSig = null;
                try (PreparedStatement ps = c.prepareStatement(SELECT)) {
                    ps.setString(1, objectName);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        functionSig = rs.getString(1);
                    }

                    if (rs.next()) {
                        final String fn = DataDefinitionUtil.getQualifiedName(schemaName, functionName);
                        logger.severe("Overloaded function signature: " + fn + " " + functionSig);
                        functionSig = rs.getString(1);
                        logger.severe("Overloaded function signature: " + fn + " " + functionSig);
                        throw new IllegalStateException("Overloading not supported for function '" + fn + "'");
                    }
                }

                if (functionSig != null) {
                    logger.info("Distributing function: " + functionSig);
                    final String DISTRIBUTE = "SELECT create_distributed_function(?::regprocedure, ?::text)";
                    try (PreparedStatement ps = c.prepareStatement(DISTRIBUTE)) {
                        ps.setString(1, functionSig);
                        ps.setString(2, "$" + distributeByParamNumber);
                        ps.execute();
                    }
                } else {
                    logger.warning("No matching function found for '" + objectName + "'");
                }
            } catch (SQLException x) {
                throw getTranslator().translate(x);
            }
        } else {
            throw new IllegalStateException("distributeFunction requires a connectionProvider");
        }
    }

    /**
     * Asks if the distributionColumnName is contained in the given collection of column names
     *
     * @implNote case-insensitive
     * @param distributionColumnName
     * @param columns
     * @return
     */
    public boolean includesDistributionColumn(String distributionColumnName, Collection<String> columns) {
        if (distributionColumnName != null) {
            Set<String> colSet = columns.stream().map(p -> p.toLowerCase()).collect(Collectors.toSet());
            return colSet.contains(distributionColumnName.toLowerCase());
        }
        return false;
    }
}