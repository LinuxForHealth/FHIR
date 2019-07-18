/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.derby;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.ibm.watsonhealth.database.utils.api.IDatabaseTarget;
import com.ibm.watsonhealth.database.utils.common.CommonDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;
import com.ibm.watsonhealth.database.utils.model.ColumnBase;
import com.ibm.watsonhealth.database.utils.model.PartitionDef;
import com.ibm.watsonhealth.database.utils.model.PrimaryKeyDef;
import com.ibm.watsonhealth.database.utils.model.Table;

/**
 * A Derby database target
 * @author rarnold
 *
 */
public class DerbyAdapter extends CommonDatabaseAdapter {
    private static final Logger logger = Logger.getLogger(DerbyAdapter.class.getName());

    /**
     * Public constructor
     * @param tgt the target database we want to manage
     */
    public DerbyAdapter(IDatabaseTarget tgt) {
        super(tgt, new DerbyTranslator());
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createTable(java.lang.String, java.util.List, com.ibm.watsonhealth.fhir.schema.model.PrimaryKeyDef, com.ibm.watsonhealth.fhir.schema.model.PartitionDef)
     */
    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
            String tablespaceName) {
        
        // Derby doesn't support partitioning, so we ignore tenantColumnName
        if (tenantColumnName != null) {
            logger.warning("Derby does not multi-tenancy: " + name);
        }

        // We also ignore tablespace for Derby
        String ddl = buildCreateTableStatement(schemaName, name, columns, primaryKey, null);
        
        
        runStatement(ddl);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createUniqueIndex(java.lang.String, java.lang.String, java.util.List, java.util.List)
     */
    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<String> indexColumns,
            List<String> includeColumns) {
        
        // Derby doesn't support include columns, so we just have to create a normal index
        createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns);
    }


    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createIntVariable(java.lang.String)
     */
    @Override
    public void createIntVariable(String schemaName, String variableName) {
        logger.warning("Derby does not support CREATE VARIABLE for: " + variableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createPermission(java.lang.String, java.lang.String)
     */
    @Override
    public void createPermission(String schemaName, String permissionName, String tableName, String predicate) {
        logger.warning("Derby does not support CREATE PERMISSION for: " + permissionName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#activateRowAccessControl(java.lang.String)
     */
    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        logger.warning("Derby does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#setIntVariable(java.lang.String)
     */
    @Override
    public void setIntVariable(String schemaName, String variableName, int value) {
        // As this is a runtime issue, we throw as an exception instead of
        // simply logging a warning. This shouldn't be called in the case
        // of a Derby database
        throw new IllegalStateException("setIntVariable not supported on Derby for: " + variableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#deactivateRowAccessControl(java.lang.String, java.lang.String)
     */
    @Override
    public void deactivateRowAccessControl(String schemaName, String tableName) {
        logger.warning("Derby does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createTenantPartitions(java.util.Collection, java.lang.String, int)
     */
    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        logger.warning("Derby does not support tenant partitioning");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createRowType(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        logger.warning("Feature not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createArrType(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        logger.warning("Feature not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#dropType(java.lang.String, java.lang.String)
     */
    @Override
    public void dropType(String schemaName, String typeName) {
        logger.warning("Feature not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createProcedure(java.lang.String, java.lang.String, java.util.function.Supplier)
     */
    @Override
    public void createProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        logger.warning("Feature not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#dropProcedure(java.lang.String, java.lang.String)
     */
    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        logger.warning("Feature not supported in Derby");
    }

    @Override
    public void createTablespace(String tablespaceName) {
        logger.warning("Feature not supported in Derby");
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        logger.warning("Feature not supported in Derby");
    }

    @Override
    public void detachPartition(String schemaName, String tableName, String partitionName, String newTableName) {
        logger.warning("Feature not supported in Derby");
    }

    @Override
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId,
            String tenantStagingTable) {
        logger.warning("Feature not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createTablespace(java.lang.String, int)
     */
    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        logger.warning("Feature not supported in Derby");
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#doesTableExist(java.lang.String, java.lang.String)
     */
    @Override
    public boolean doesTableExist(String schemaName, String tableName) {
        DerbyDoesTableExist dao = new DerbyDoesTableExist(schemaName, tableName);
        return runStatement(dao);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createSequence(java.lang.String, java.lang.String, int)
     */
    @Override
    public void createSequence(String schemaName, String sequenceName, int cache) {
        /*CREATE SEQUENCE fhir_sequence
             AS BIGINT
     START WITH 1
          CACHE 1000
       NO CYCLE;
    */
        // Derby doesn't support CACHE
        final String sname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        final String ddl = "CREATE SEQUENCE " + sname + " AS BIGINT START WITH 1 NO CYCLE";
        runStatement(ddl);
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTypeAdapter#varbinaryClause(int)
     */
    @Override
    public String varbinaryClause(int size) {
        // I have no idea why Derby doesn't use VARBINARY...but oh well
        return "VARCHAR(" + size + ") FOR BIT DATA";
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTypeAdapter#blobClause(long, int)
     */
    @Override
    public String blobClause(long size, int inlineSize) {
        // Derby doesn't support the INLINE feature (which greatly helps with
        // performance on DB2)
        return "BLOB(" + size + ")";            
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTypeAdapter#varcharClause(int)
     */
    @Override
    public String varcharClause(int size) {
        return "VARCHAR(" + size + ")";
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, 
            String targetSchema, String targetTable, String tenantColumnName,
            List<String> columns) {
        
        // Make the call, but without the tenantColumnName because Derby doesn't support
        // our multi-tenant implementation
        super.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, null, columns);
    }

    @Override
    protected List<String> prefixTenantColumn(String tenantColumnName, List<String> columns) {
        // No tenant support, so simply return the columns list unchanged, without prefixing
        // the tenanteColumnName
        return columns;
    }
}
