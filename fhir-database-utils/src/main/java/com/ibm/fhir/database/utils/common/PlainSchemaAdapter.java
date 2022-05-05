/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.common;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.ibm.fhir.database.utils.api.DistributionType;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.With;


/**
 * Adapter to build the plain version of the FHIR schema. Uses
 * the IDatabaseAdapter to hide the specifics of a particular
 * database flavor (like Db2, PostgreSQL, Derby etc).
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
    public void detachPartition(String schemaName, String tableName, String partitionName, String newTableName) {
        databaseAdapter.detachPartition(schemaName, tableName, partitionName, newTableName);
    }

    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey, IdentityDef identity,
        String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints, DistributionType distributionRules) {
        databaseAdapter.createTable(schemaName, name, tenantColumnName, columns, primaryKey, identity, tablespaceName, withs, checkConstraints, null);
    }

    @Override
    public void applyDistributionRules(String schemaName, String tableName, DistributionType distributionRules) {
        databaseAdapter.applyDistributionRules(schemaName, tableName, null);
    }

    @Override
    public void alterTableAddColumn(String schemaName, String tableName, ColumnBase column) {
        databaseAdapter.alterTableAddColumn(schemaName, tableName, column);
    }

    @Override
    public void reorgTable(String schemaName, String tableName) {
        databaseAdapter.reorgTable(schemaName, tableName);
    }

    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        databaseAdapter.createRowType(schemaName, typeName, columns);
    }

    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        databaseAdapter.createArrType(schemaName, typeName, valueType, arraySize);
    }

    @Override
    public void dropType(String schemaName, String typeName) {
        databaseAdapter.dropType(schemaName, typeName);
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
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns,
        List<String> includeColumns, DistributionType distributionRules) {
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns, includeColumns, null);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns,
        DistributionType distributionRules) {
        databaseAdapter.createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns, null);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns, DistributionType distributionType) {
        databaseAdapter.createIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns);
    }

    @Override
    public void createIntVariable(String schemaName, String variableName) {
        databaseAdapter.createIntVariable(schemaName, variableName);
    }

    @Override
    public void createOrReplacePermission(String schemaName, String permissionName, String tableName, String predicate) {
        databaseAdapter.createOrReplacePermission(schemaName, permissionName, tableName, predicate);
    }

    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        databaseAdapter.activateRowAccessControl(schemaName, tableName);
    }

    @Override
    public void deactivateRowAccessControl(String schemaName, String tableName) {
        databaseAdapter.deactivateRowAccessControl(schemaName, tableName);
    }

    @Override
    public void setIntVariable(String schemaName, String variableName, int value) {
        databaseAdapter.setIntVariable(schemaName, variableName, value);
    }

    @Override
    public void dropTable(String schemaName, String name) {
        databaseAdapter.dropTable(schemaName, name);
    }

    @Override
    public void dropPermission(String schemaName, String permissionName) {
        databaseAdapter.dropPermission(schemaName, permissionName);
    }

    @Override
    public void dropVariable(String schemaName, String variableName) {
        databaseAdapter.dropVariable(schemaName, variableName);
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema, String targetTable,
        String targetColumnName, String tenantColumnName, List<String> columns, boolean enforced, DistributionType distributionType, boolean targetIsReference) {
        databaseAdapter.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, targetColumnName, tenantColumnName, columns, enforced);
    }

    @Override
    public int allocateTenant(String adminSchemaName, String schemaName, String tenantName, String tenantKey, String tenantSalt, String idSequenceName) {
        return databaseAdapter.allocateTenant(adminSchemaName, schemaName, tenantName, tenantKey, tenantSalt, idSequenceName);
    }

    @Override
    public void deleteTenantMeta(String adminSchemaName, int tenantId) {
        databaseAdapter.deleteTenantMeta(adminSchemaName, tenantId);
    }

    @Override
    public int findTenantId(String adminSchemaName, String tenantName) {
        return databaseAdapter.findTenantId(adminSchemaName, tenantName);
    }

    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        databaseAdapter.createTenantPartitions(tables, schemaName, newTenantId, extentSizeKB);
    }

    @Override
    public void addNewTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId) {
        databaseAdapter.addNewTenantPartitions(tables, schemaName, newTenantId);
    }

    @Override
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId) {
        databaseAdapter.removeTenantPartitions(tables, schemaName, tenantId);
    }

    @Override
    public void dropDetachedPartitions(Collection<Table> tables, String schemaName, int tenantId) {
        databaseAdapter.dropDetachedPartitions(tables, schemaName, tenantId);
    }

    @Override
    public void updateTenantStatus(String adminSchemaName, int tenantId, TenantStatus status) {
        databaseAdapter.updateTenantStatus(adminSchemaName, tenantId, status);
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
    public void dropTenantTablespace(int tenantId) {
        databaseAdapter.dropTenantTablespace(tenantId);
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
