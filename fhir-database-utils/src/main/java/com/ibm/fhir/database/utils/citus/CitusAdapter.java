/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.citus;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.DistributionRules;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.With;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;


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
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
        IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
        DistributionRules distributionRules) {

        // We don't use partitioning for multi-tenancy in our Citus implementation, so ignore the mt_id column
        if (tenantColumnName != null) {
            warnOnce(MessageKey.MULTITENANCY, "Citus does not support multi-tenancy: " + name);
        }

        // Build a Citus-specific create table statement
        String ddl = buildCitusCreateTableStatement(schemaName, name, columns, primaryKey, identity, withs, checkConstraints, distributionRules);
        runStatement(ddl);
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
     * @param distributionRules
     * @return
     */
    private String buildCitusCreateTableStatement(String schema, String name, List<ColumnBase> columns, 
            PrimaryKeyDef pkDef, IdentityDef identity, List<With> withs,
            List<CheckConstraint> checkConstraints, DistributionRules distributionRules) {

        if (identity != null && distributionRules != null && distributionRules.getDistributionColumn() != null) {
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
            final String ldc = distributionRules == null || distributionRules.getDistributionColumn() == null ? null : distributionRules.getDistributionColumn().toLowerCase();
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
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
        List<OrderedColumnDef> indexColumns, DistributionRules distributionRules) {
        // For Citus, we are prevented from creating a unique index unless the index contains
        // the distribution column
        List<String> columnNames = indexColumns.stream().map(ocd -> ocd.getColumnName()).collect(Collectors.toList());
        if (distributionRules != null && distributionRules.isDistributedTable() && !distributionRules.includesDistributionColumn(columnNames)) {
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
    public void applyDistributionRules(String schemaName, String tableName, DistributionRules distributionRules) {
        // Apply the distribution rules. Tables without distribution rules are created
        // only on Citus controller nodes and never distributed to the worker nodes. All
        // the distribution changes are implemented in one transaction, which makes it much
        // more efficient.
        final String fullName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        if (distributionRules.isReferenceTable()) {
            // A table that is fully replicated for each worker node
            logger.info("Citus: distributing reference table '" + fullName + "'");
            CreateReferenceTableDAO dao = new CreateReferenceTableDAO(schemaName, tableName);
            runStatement(dao);
        } else if (distributionRules.getDistributionColumn() != null && distributionRules.getDistributionColumn().length() > 0) {
            // A table that is sharded using a hash on the distributionColumn value
            logger.info("Citus: Sharding table '" + fullName + "' using '" + distributionRules.getDistributionColumn() + "'");
            CreateDistributedTableDAO dao = new CreateDistributedTableDAO(schemaName, tableName, distributionRules.getDistributionColumn());
            runStatement(dao);
        }
    }
}