/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.build;

import java.util.List;

import com.ibm.fhir.database.utils.api.DistributionContext;
import com.ibm.fhir.database.utils.api.DistributionType;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.PlainSchemaAdapter;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.With;

/**
 * Represents an adapter used to build the FHIR schema when
 * used with a distributed databse like Citus
 */
public class DistributedSchemaAdapter extends PlainSchemaAdapter {
    // The distribution column to use by default for DISTRIBUTED tables
    private final String defaultDistributionColumnName;

    /**
     * Public constructor
     * 
     * @param databaseAdapter
     */
    public DistributedSchemaAdapter(IDatabaseAdapter databaseAdapter, String defaultDistributionColumnName) {
        super(databaseAdapter);
        this.defaultDistributionColumnName = defaultDistributionColumnName;
    }

    /**
     * Create a DistributionContext using the given type and column. If the distribution column
     * is null, then the default distribution column is used.
     * @param distributionType
     * @param distributionColumnName
     * @return
     */
    private DistributionContext createContext(DistributionType distributionType, String distributionColumnName) {
        if (distributionType == DistributionType.DISTRIBUTED && distributionColumnName == null) {
            distributionColumnName = defaultDistributionColumnName;
        }
        return new DistributionContext(distributionType, distributionColumnName);
    }

    @Override
    public void createTable(String schemaName, String name, List<ColumnBase> columns, PrimaryKeyDef primaryKey, IdentityDef identity,
        String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints, DistributionType distributionType, String distributionColumnName) {

        DistributionContext dc = createContext(distributionType, distributionColumnName);
        databaseAdapter.createTable(schemaName, name, columns, primaryKey, identity, tablespaceName, withs, checkConstraints, dc);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns,
        List<String> includeColumns, DistributionType distributionType, String distributionColumnName) {
        DistributionContext dc = createContext(distributionType, distributionColumnName);
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, indexColumns, includeColumns, dc);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns,
        DistributionType distributionType, String distributionColumnName) {

        DistributionContext dc = createContext(distributionType, distributionColumnName);
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, indexColumns, dc);
    }

    @Override
    public void applyDistributionRules(String schemaName, String tableName, DistributionType distributionType, String distributionColumnName) {
        DistributionContext dc = createContext(distributionType, distributionColumnName);
        databaseAdapter.applyDistributionRules(schemaName, tableName, dc);
    }
}
