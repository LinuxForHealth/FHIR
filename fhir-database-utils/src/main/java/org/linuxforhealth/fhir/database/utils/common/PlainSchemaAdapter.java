/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.common;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.linuxforhealth.fhir.database.utils.api.DistributionType;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.model.CheckConstraint;
import org.linuxforhealth.fhir.database.utils.model.ColumnBase;
import org.linuxforhealth.fhir.database.utils.model.IdentityDef;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;
import org.linuxforhealth.fhir.database.utils.model.PrimaryKeyDef;
import org.linuxforhealth.fhir.database.utils.model.Privilege;
import org.linuxforhealth.fhir.database.utils.model.With;


/**
 * Adapter to build the plain version of the FHIR schema. Uses
 * the IDatabaseAdapter to hide the specifics of a particular
 * database flavor (like PostgreSQL, Derby etc).
 */
public class PlainSchemaAdapter implements ISchemaAdapter {

    // The adapter we use to execute database-specific DDL
    protected final IDatabaseAdapter databaseAdapter;

    /**
     * Public constructor
     * 
     * @param databaseAdapter
     */
    public PlainSchemaAdapter(IDatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    @Override
    public void createTablespace(String tablespaceName) {
        databaseAdapter.createTablespace(tablespaceName);
    }

    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        databaseAdapter.createTablespace(tablespaceName, extentSizeKB);
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        databaseAdapter.dropTablespace(tablespaceName);
    }

    @Override
    public void createTable(String schemaName, String name, List<ColumnBase> columns, PrimaryKeyDef primaryKey, IdentityDef identity,
        String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints, DistributionType distributionType, String distributionColumnName) {
        databaseAdapter.createTable(schemaName, name, columns, primaryKey, identity, tablespaceName, withs, checkConstraints, null);
    }

    @Override
    public void applyDistributionRules(String schemaName, String tableName, DistributionType distributionType, String distributionColumnName) {
        databaseAdapter.applyDistributionRules(schemaName, tableName, null);
    }

    @Override
    public void alterTableAddColumn(String schemaName, String tableName, ColumnBase column) {
        databaseAdapter.alterTableAddColumn(schemaName, tableName, column);
    }

    @Override
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        databaseAdapter.createOrReplaceProcedure(schemaName, procedureName, supplier);
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        databaseAdapter.dropProcedure(schemaName, procedureName);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns,
        List<String> includeColumns, DistributionType distributionType, String distributionColumnName) {
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, indexColumns, includeColumns, null);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns,
        DistributionType distributionType, String distributionColumnName) {
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, indexColumns, null);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns, DistributionType distributionType) {
        databaseAdapter.createIndex(schemaName, tableName, indexName, indexColumns);
    }

    @Override
    public void dropTable(String schemaName, String name) {
        databaseAdapter.dropTable(schemaName, name);
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema, String targetTable,
        String targetColumnName, List<String> columns, boolean enforced, DistributionType distributionType, boolean targetIsReference) {
        databaseAdapter.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, targetColumnName, columns, enforced);
    }

    @Override
    public void createSequence(String schemaName, String sequenceName, long startWith, int cache, int incrementBy) {
        databaseAdapter.createSequence(schemaName, sequenceName, startWith, cache, incrementBy);
    }

    @Override
    public void dropSequence(String schemaName, String sequenceName) {
        databaseAdapter.dropSequence(schemaName, sequenceName);
    }

    @Override
    public void alterSequenceRestartWith(String schemaName, String sequenceName, long restartWith, int cache, int incrementBy) {
        databaseAdapter.alterSequenceRestartWith(schemaName, sequenceName, restartWith, cache, incrementBy);
    }

    @Override
    public void grantObjectPrivileges(String schemaName, String tableName, Collection<Privilege> privileges, String toUser) {
        databaseAdapter.grantObjectPrivileges(schemaName, tableName, privileges, toUser);
    }

    @Override
    public void grantProcedurePrivileges(String schemaName, String procedureName, Collection<Privilege> privileges, String toUser) {
        databaseAdapter.grantProcedurePrivileges(schemaName, procedureName, privileges, toUser);
    }

    @Override
    public void grantVariablePrivileges(String schemaName, String variableName, Collection<Privilege> privileges, String toUser) {
        databaseAdapter.grantVariablePrivileges(schemaName, variableName, privileges, toUser);
    }

    @Override
    public void grantSequencePrivileges(String schemaName, String objectName, Collection<Privilege> group, String toUser) {
        databaseAdapter.grantSequencePrivileges(schemaName, objectName, group, toUser);
    }

    @Override
    public void grantSchemaUsage(String schemaName, String grantToUser) {
        databaseAdapter.grantSchemaUsage(schemaName, grantToUser);

    }

    @Override
    public void grantAllSequenceUsage(String schemaName, String grantToUser) {
        databaseAdapter.grantAllSequenceUsage(schemaName, grantToUser);
    }

    @Override
    public boolean doesTableExist(String schemaName, String objectName) {
        return databaseAdapter.doesTableExist(schemaName, objectName);
    }

    @Override
    public void createSchema(String schemaName) {
        databaseAdapter.createSchema(schemaName);
    }

    @Override
    public void createUniqueConstraint(String constraintName, List<String> columns, String schemaName, String name) {
        databaseAdapter.createUniqueConstraint(constraintName, columns, schemaName, name);
    }

    @Override
    public boolean checkCompatibility(String adminSchema) {
        return databaseAdapter.checkCompatibility(adminSchema);
    }

    @Override
    public void createOrReplaceFunction(String schemaName, String objectName, Supplier<String> supplier) {
        databaseAdapter.createOrReplaceFunction(schemaName, objectName, supplier);
    }

    @Override
    public void distributeFunction(String schemaName, String functionName, int distributeByParamNumber) {
        databaseAdapter.distributeFunction(schemaName, functionName, distributeByParamNumber);
    }

    @Override
    public void dropFunction(String schemaName, String functionName) {
        databaseAdapter.dropFunction(schemaName, functionName);
    }

    @Override
    public void grantFunctionPrivileges(String schemaName, String functionName, Collection<Privilege> privileges, String toUser) {
        databaseAdapter.grantFunctionPrivileges(schemaName, functionName, privileges, toUser);
    }

    @Override
    public void disableForeignKey(String schemaName, String tableName, String constraintName) {
        databaseAdapter.disableForeignKey(schemaName, tableName, constraintName);
    }

    @Override
    public void dropForeignKey(String schemaName, String tableName, String constraintName) {
        databaseAdapter.dropForeignKey(schemaName, tableName, constraintName);
    }

    @Override
    public void enableForeignKey(String schemaName, String tableName, String constraintName) {
        databaseAdapter.enableForeignKey(schemaName, tableName, constraintName);
    }

    @Override
    public boolean doesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName) {
        return databaseAdapter.doesForeignKeyConstraintExist(schemaName, tableName, constraintName);
    }

    @Override
    public void setIntegrityOff(String schemaName, String tableName) {
        databaseAdapter.setIntegrityOff(schemaName, tableName);
    }

    @Override
    public void setIntegrityUnchecked(String schemaName, String tableName) {
        databaseAdapter.setIntegrityUnchecked(schemaName, tableName);
    }

    @Override
    public void alterTableColumnIdentityCache(String schemaName, String objectName, String columnName, int cache) {
        databaseAdapter.alterTableColumnIdentityCache(schemaName, objectName, columnName, cache);
    }

    @Override
    public void dropIndex(String schemaName, String indexName) {
        databaseAdapter.dropIndex(schemaName, indexName);
    }

    @Override
    public void createView(String schemaName, String objectName, String selectClause) {
        databaseAdapter.createView(schemaName, objectName, selectClause);
    }

    @Override
    public void dropView(String schemaName, String objectName) {
        databaseAdapter.dropView(schemaName, objectName);
    }

    @Override
    public void createOrReplaceView(String schemaName, String objectName, String selectClause) {
        databaseAdapter.createOrReplaceView(schemaName, objectName, selectClause);
    }

    @Override
    public List<SchemaInfoObject> listSchemaObjects(String schemaName) {
        return databaseAdapter.listSchemaObjects(schemaName);
    }

    @Override
    public void runStatement(IDatabaseStatement statement) {
        databaseAdapter.runStatement(statement);
    }
}
