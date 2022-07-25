/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.With;

/**
 * Adapter to create a particular flavor of the FHIR schema
 */
public interface ISchemaAdapter {

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
     * Build the create table DDL
     *
     * @param schemaName
     * @param name
     * @param columns
     * @param primaryKey
     * @param identity
     * @param tablespaceName
     * @param withs
     * @param checkConstraints
     * @param distributionType
     * @param distributionColumnName
     */
    public void createTable(String schemaName, String name, List<ColumnBase> columns,
            PrimaryKeyDef primaryKey, IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
            DistributionType distributionType, String distributionColumnName);

    /**
     * Apply any distribution rules configured for the named table
     * @param schemaName
     * @param tableName
     * @param distributionType
     * @param distributionColumnName
     */
    public void applyDistributionRules(String schemaName, String tableName, DistributionType distributionType, String distributionColumnName);

    /**
     * Add a new column to an existing table
     * @param schemaName
     * @param tableName
     * @param column
     */
    public void alterTableAddColumn(String schemaName, String tableName, ColumnBase column);

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
     * Create a unique index
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param indexColumns
     * @param includeColumns
     * @param distributionType
     * @param distributionColumnName
     */
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
            List<OrderedColumnDef> indexColumns, List<String> includeColumns, 
            DistributionType distributionType, String distributionColumnName);

    /**
     * Create a unique index
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param indexColumns
     * @param distributionRules
     * @param distributionColumnName
     */
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
            List<OrderedColumnDef> indexColumns, DistributionType distributionType, String distributionColumnName);

    /**
     * Create an index on the named schema.table object
     * @param schemaName
     * @param tableName
     * @param indexName
     * @param indexColumns
     * @param distributionType
     */
    public void createIndex(String schemaName, String tableName, String indexName,
            List<OrderedColumnDef> indexColumns, DistributionType distributionType);

    /**
     * Drop table from the schema
     *
     * @param schemaName
     * @param name
     */
    public void dropTable(String schemaName, String name);

    /**
     *
     * @param constraintName
     * @param schemaName
     * @param name
     * @param targetSchema
     * @param targetTable
     * @param targetColumnName
     * @param columns
     * @param enforced
     * @param distributionType distribution type of the source table
     * @param targetIsReference
     */
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema,
            String targetTable, String targetColumnName, List<String> columns, 
            boolean enforced, DistributionType distributionType, boolean targetIsReference);

    /**
     *
     * @param schemaName
     * @param sequenceName
     * @param startWith the START WITH value for the sequence
     * @param cache the sequence CACHE value
     */
    public void createSequence(String schemaName, String sequenceName, long startWith, int cache, int incrementBy);

    /**
     *
     * @param schemaName
     * @param sequenceName
     */
    public void dropSequence(String schemaName, String sequenceName);

    /**
     * Sets/resets the sequence to start with the given value.
     * @param schemaName
     * @param sequenceName
     * @param restartWith
     * @param cache
     */
    public void alterSequenceRestartWith(String schemaName, String sequenceName, long restartWith, int cache, int incrementBy);

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
     * Grants USAGE on the given schemaName to the given user
     * @param schemaName
     */
    public void grantSchemaUsage(String schemaName, String grantToUser);

    /**
     * Grant access to all sequences in the named schema
     * @param schemaName
     * @param grantToUser
     */
    public void grantAllSequenceUsage(String schemaName, String grantToUser);

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
     * creates or replaces the SQL function
     * @param schemaName
     * @param objectName
     * @param supplier
     */
    public void createOrReplaceFunction(String schemaName, String objectName, Supplier<String> supplier);

    /**
     * For Citus, functions can be distributed by one of their parameters (typically the first)
     * @param schemaName
     * @param functionName
     * @param distributeByParamNumber
     */
    public void distributeFunction(String schemaName, String functionName, int distributeByParamNumber);

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

    /**
     * Disable the FK with the given constraint name
     * @param tableName
     * @param constraintName
     */
    public void disableForeignKey(String schemaName, String tableName, String constraintName);

    /**
     * Drop the FK on the table with the given constraint name
     * @param schemaName
     * @param tableName
     * @param constraintName
     */
    public void dropForeignKey(String schemaName, String tableName, String constraintName);

    /**
     * Enable the FK with the given constraint name
     * @param schemaName
     * @param tableName
     * @param constraintName
     */
    public void enableForeignKey(String schemaName, String tableName, String constraintName);

    /**
     * Check to see if the named foreign key constraint already exists
     * @param schemaName
     * @param tableName
     * @param constraintName
     * @return
     */
    public boolean doesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName);

    /**
     *
     * @param schemaName
     * @param tableName
     */
    public void setIntegrityOff(String schemaName, String tableName);

    /**
     *
     * @param schemaName
     * @param tableName
     */
    public void setIntegrityUnchecked(String schemaName, String tableName);

    /**
     * Change the CACHE value of the named identity generated always column
     * @param schemaName
     * @param objectName
     * @param columnName
     * @param cache
     */
    public void alterTableColumnIdentityCache(String schemaName, String objectName, String columnName, int cache);

    /**
     * Drop the named index
     * @param schemaName
     * @param indexName
     */
    public void dropIndex(String schemaName, String indexName);

    /**
     * Create the view as defined by the selectClause
     * @param schemaName
     * @param objectName
     * @param selectClause
     */
    public void createView(String schemaName, String objectName, String selectClause);

    /**
     * Drop the view from the database
     * @param schemaName
     * @param objectName
     */
    public void dropView(String schemaName, String objectName);

    /**
     * Create or replace the view
     * @param schemaName
     * @param objectName
     * @param selectClause
     */
    public void createOrReplaceView(String schemaName, String objectName, String selectClause);
    
    /**
     * List the objects present in the given schema
     * @param schemaName
     * @return
     */
    List<SchemaInfoObject> listSchemaObjects(String schemaName);

    /**
     * Run the given statement against the database represented by this adapter
     *
     * @param statement
     */
    public void runStatement(IDatabaseStatement statement);
}