/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Table;

/**
 * Abstraction of the SQL to use for a given database. This allows us to hide as
 * much as possible the differences in syntax and support between DB2 and Derby
 * (which is used for unit-testing). Derby is pretty close to DB2 in most cases,
 * but notably does not support partitioning, variables or SPL stored
 * procedures.
 */
public interface IDatabaseAdapter {
    /**
     * Get the {@link IDatabaseTranslator} associated with this adapter
     *
     * @return
     */
    public IDatabaseTranslator getTranslator();

    /**
     * Create a new tablespace with the given name
     *
     * @param tablespaceName
     */
    public void createTablespace(String tablespaceName);

    /**
     * Create a new tablespace using the given extent size
     *
     * @param tablespaceName
     * @param extentSizeKB
     */
    public void createTablespace(String tablespaceName, int extentSizeKB);

    /**
     * Drop an existing tablespace, including all of its contents
     *
     * @param tablespaceName
     */
    public void dropTablespace(String tablespaceName);

    /**
     * Detach the partition
     *
     * @param schemaName
     * @param tableName
     * @param partitionName
     * @param newTableName
     */
    public void detachPartition(String schemaName, String tableName, String partitionName, String newTableName);

    /**
     * Build the create table DDL
     *
     * @param schemaName
     * @param name
     * @param tenantColumnName optional column name to enable multi-tenancy
     * @param columns
     * @param primaryKey
     * @param identity
     * @param tablespaceName
     */
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns,
            PrimaryKeyDef primaryKey, IdentityDef identity, String tablespaceName);

    /**
     * Create ROW type used for passing values to stored procedures e.g.:
     *
     * <pre>
     * CREATE OR REPLACE TYPE <schema>.t_str_values AS ROW (parameter_name_id INTEGER,
     * str_value VARCHAR(511 OCTETS), str_value_lcase VARCHAR(511 OCTETS))
     * </pre>
     *
     * @param schemaName
     * @param typeName
     * @param columns
     */
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns);

    /**
     * Create ARRAY type used for passing values to stored procedures e.g.: CREATE
     * OR REPLACE TYPE <schema>.t_str_values_arr AS <schema>.t_str_values ARRAY[256]
     *
     * @param schemaName
     * @param typeName
     * @param valueType
     * @param arraySize
     */
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize);

    /**
     * Drop the type object from the schema
     *
     * @param schemaName
     * @param typeName
     */
    public void dropType(String schemaName, String typeName);

    /**
     * Create the stored procedure using the DDL text provided by the supplier
     *
     * @param schemaName
     * @param procedureName
     * @param supplier
     */
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier);

    /**
     * Drop the given procedure
     *
     * @param schemaName
     * @param procedureName
     */
    public void dropProcedure(String schemaName, String procedureName);

    /**
     *
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param tenantColumnName
     * @param indexColumns
     * @param includeColumns
     */
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
            List<String> indexColumns, List<String> includeColumns);

    /**
     *
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param tenantColumnName
     * @param indexColumns
     */
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
            List<String> indexColumns);

    /**
     *
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param tenantColumnName
     * @param indexColumns
     */
    public void createIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
            List<String> indexColumns);

    /**
     *
     * <pre>
     * CREATE VARIABLE ptng.session_tenant INT DEFAULT NULL;
     * </pre>
     *
     * @param schemaName
     * @param variableName
     */
    public void createIntVariable(String schemaName, String variableName);

    /**
     *
     * <pre>
     * CREATE OR REPLACE PERMISSION ROW_ACCESS ON ptng.patients FOR ROWS WHERE patients.mt_id =
     * ptng.session_tenant ENFORCED FOR ALL ACCESS ENABLE;
     * </pre>
     *
     * @param schemaName
     * @param permissionName
     * @param tableName
     * @param predicate
     */
    public void createOrReplacePermission(String schemaName, String permissionName, String tableName, String predicate);

    /**
     *
     *
     * <pre> ALTER TABLE <tbl> ACTIVATE ROW ACCESS CONTROL
     * </pre>
     *
     * @param schemaName
     * @param tableName
     */
    public void activateRowAccessControl(String schemaName, String tableName);

    /**
     * Deactivate row access control on a table ALTER TABLE <tbl> DEACTIVATE ROW
     * ACCESS CONTROL
     *
     * @param schemaName
     * @param tableName
     */
    public void deactivateRowAccessControl(String schemaName, String tableName);

    /**
     * Build the DML statement for setting a session variable
     *
     * @param schemaName
     * @param variableName
     * @param value
     */
    public void setIntVariable(String schemaName, String variableName, int value);

    /**
     * Drop table from the schema
     *
     * @param schemaName
     * @param name
     */
    public void dropTable(String schemaName, String name);

    /**
     * Drop permission object from the schema
     *
     * @param schemaName
     * @param permissionName
     */
    public void dropPermission(String schemaName, String permissionName);

    /**
     * @param schemaName
     * @param variableName
     */
    public void dropVariable(String schemaName, String variableName);

    /**
     *
     * @param constraintName
     * @param schemaName
     * @param name
     * @param targetSchema
     * @param targetTable
     * @param targetColumnName
     * @param tenantColumnName
     * @param columns
     * @param enforced
     */
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema,
            String targetTable, String targetColumnName, String tenantColumnName, List<String> columns, boolean enforced);

    /**
     * Allocate a new tenant
     *
     * @param adminSchemaName
     * @param schemaName
     * @param tenantName
     * @param tenantKey
     * @param tenantSalt
     * @param idSequenceName
     * @return
     */
    public int allocateTenant(String adminSchemaName, String schemaName, String tenantName, String tenantKey,
            String tenantSalt, String idSequenceName);

    /**
     * Get the tenant id for the given schema and tenant name
     *
     * @param adminSchemaName
     * @param tenantName
     * @return
     */
    public int findTenantId(String adminSchemaName, String tenantName);

    /**
     * Create the partitions on each of these tables
     *
     * @param tables
     * @param schemaName
     * @param newTenantId
     * @param extentSizeKB
     */
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB);

    /**
     * Detach the partitions from each of the given tables. The tenantStaingTable is
     * the name of the table used to record the "into" table name that the detached
     * partition becomes.
     *
     * @param tables
     * @param schemaName
     * @param tenantId
     * @param tenantStagingTable
     */
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId,
            String tenantStagingTable);

    /**
     * Update the tenant status
     *
     * @param adminSchemaName
     * @param tenantId
     * @param status
     */
    public void updateTenantStatus(String adminSchemaName, int tenantId, TenantStatus status);

    /**
     *
     * @param schemaName
     * @param sequenceName
     * @param cache
     */
    public void createSequence(String schemaName, String sequenceName, int cache);

    /**
     *
     * @param schemaName
     * @param sequenceName
     */
    public void dropSequence(String schemaName, String sequenceName);

    /**
     * Grant the list of privileges on the named object to the user. This is a
     * general purpose method which can be used to specify privileges for any object
     * type which doesn't need the object type to be specified in the grant DDL.
     *
     * @param schemaName
     * @param tableName
     * @param privileges
     * @param toUser
     */
    public void grantObjectPrivileges(String schemaName, String tableName, Collection<Privilege> privileges, String toUser);

    /**
     * Grant the collection of privileges on the named procedure to the user
     *
     * @param schemaName
     * @param procedureName
     * @param privileges
     * @param toUser
     */
    public void grantProcedurePrivileges(String schemaName, String procedureName, Collection<Privilege> privileges,
            String toUser);

    /**
     * Grant the collection of privileges on the named variable to the user
     *
     * @param schemaName
     * @param variableName
     * @param privileges
     * @param toUser
     */
    public void grantVariablePrivileges(String schemaName, String variableName, Collection<Privilege> privileges,
            String toUser);

    /**
     * Grant the collection of privileges on the named variable to the user
     *
     * @param schemaName
     * @param objectName
     * @param group
     * @param toUser
     */
    public void grantSequencePrivileges(String schemaName, String objectName, Collection<Privilege> group,
            String toUser);

    /**
     * Run the given supplier statement against the database represented by this
     * adapter
     *
     * @param <T>
     * @param supplier
     * @return
     */
    public <T> T runStatement(IDatabaseSupplier<T> supplier);

    /**
     * Run the given statement against the database represented by this adapter
     *
     * @param statement
     */
    public void runStatement(IDatabaseStatement statement);

    /**
     * Check if the table currently exists
     *
     * @param schemaName
     * @param objectName
     * @return
     */
    public boolean doesTableExist(String schemaName, String objectName);


    /**
     * Create a database schema
     *
     * @param schemaName
     */
    public void createSchema(String schemaName);

    /**
     * create a unique constraint on a table.
     * 
     * @param constraintName
     * @param columns
     * @param schemaName
     * @param name
     */
    public void createUniqueConstraint(String constraintName, List<String> columns, String schemaName, String name);

    /**
     * checks connectivity to the database and that it is compatible
     * @param adminSchema
     * @return
     */
    public boolean checkCompatibility(String adminSchema);

    /**
     * 
     * @return a false, if not used, or true if used with the persistence layer.
     */
    public default boolean useSessionVariable() {
        return false;
    }

    /**
     * creates or replaces the SQL function
     * @param schemaName
     * @param objectName
     * @param supplier
     */
    public void createOrReplaceFunction(String schemaName, String objectName, Supplier<String> supplier);

    /**
     * drops a given function 
     * @param schemaName
     * @param functionName
     */
    public void dropFunction(String schemaName, String functionName);

    /** 
     * grants permissions on a given function
     * @param schemaName
     * @param functionName
     * @param privileges
     * @param toUser
     */
    public void grantFunctionPrivileges(String schemaName, String functionName, Collection<Privilege> privileges, String toUser);
}