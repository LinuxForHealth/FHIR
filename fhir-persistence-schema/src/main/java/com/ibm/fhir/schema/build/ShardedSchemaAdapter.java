/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.build;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.api.DistributionContext;
import com.ibm.fhir.database.utils.api.DistributionType;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.SmallIntColumn;
import com.ibm.fhir.database.utils.model.With;

/**
 * Adapter implementation used to build the distributed variant of
 * the IBM FHIR Server RDBMS schema.
 * 
 * This schema adds a distribution key column to every table identified as
 * distributed. This column is also added to every index and FK relationship
 * as needed. We use a smallint (2 bytes) which represents a signed integer
 * holding values in the range [-32768, 32767]. This provides sufficient spread,
 * assuming we won't be using a database with thousands of nodes.
 */
public class ShardedSchemaAdapter extends FhirSchemaAdapter {

    // The distribution column to add to each table marked as distributed
    final String shardColumnName;

    /**
     * @param databaseAdapter
     */
    public ShardedSchemaAdapter(IDatabaseAdapter databaseAdapter, String shardColumnName) {
        super(databaseAdapter);
        this.shardColumnName = shardColumnName;
    }

    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey, IdentityDef identity,
        String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints, DistributionType distributionType, String distributionColumnName) {
        // If the table is distributed, we need to inject the distribution column into the columns list. This same
        // column will need to be injected into each of the index definitions
        List<ColumnBase> actualColumns = new ArrayList<>();
        if (distributionType == DistributionType.DISTRIBUTED) {
            ColumnBase distributionColumn = new SmallIntColumn(shardColumnName, false, null);
            actualColumns.add(distributionColumn);
            if (primaryKey != null) {
                // we need to alter the primary so it includes the distribution column
                // as the last member
                List<String> newCols = new ArrayList<>(primaryKey.getColumns());
                newCols.add(distributionColumnName);
                primaryKey = new PrimaryKeyDef(primaryKey.getConstraintName(), newCols);
            }
        }

        actualColumns.addAll(columns);
        DistributionContext dc = new DistributionContext(distributionType, shardColumnName);
        databaseAdapter.createTable(schemaName, name, tenantColumnName, actualColumns, primaryKey, identity, tablespaceName, withs, checkConstraints, dc);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns,
        List<String> includeColumns, DistributionType distributionType, String distributionColumnName) {
        
        List<OrderedColumnDef> actualColumns = new ArrayList<>(indexColumns);
        if (distributionType == DistributionType.DISTRIBUTED) {
            // inject the distribution column into the index definition
            actualColumns.add(new OrderedColumnDef(this.shardColumnName, null, null));
        }

        // Create the index using the modified set of index columns
        DistributionContext dc = new DistributionContext(distributionType, shardColumnName);
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, actualColumns, includeColumns, dc);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns,
        DistributionType distributionType, String distributionColumnName) {

        List<OrderedColumnDef> actualColumns = new ArrayList<>(indexColumns);
        if (distributionType == DistributionType.DISTRIBUTED) {
            // inject the distribution column into the index definition
            actualColumns.add(new OrderedColumnDef(this.shardColumnName, null, null));
        }

        // Create the index using the modified set of index columns
        DistributionContext dc = new DistributionContext(distributionType, shardColumnName);
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, actualColumns, dc);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns, DistributionType distributionType) {
        // Create the index using the modified set of index columns
        databaseAdapter.createIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns);
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema, String targetTable,
        String targetColumnName, String tenantColumnName, List<String> columns, boolean enforced, DistributionType distributionType, boolean targetIsReference) {
        // If both this and the target table are distributed, we need to add the distributionColumnName
        // to the FK relationship definition. If the target is a reference, it won't have the shard_key
        // column because the table is fully replicated across all nodes and therefore any FK relationship
        // can be based on the original PK definition without the extra sharding column.
        List<String> newCols = new ArrayList<>(columns);
        if (distributionType == DistributionType.DISTRIBUTED && !targetIsReference) {
            newCols.add(shardColumnName);
        }
        databaseAdapter.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, targetColumnName, tenantColumnName, newCols, enforced);
    }
}