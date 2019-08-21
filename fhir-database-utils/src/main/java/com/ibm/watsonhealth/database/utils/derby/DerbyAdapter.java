/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.derby;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.database.utils.api.IDatabaseTarget;
import com.ibm.watsonhealth.database.utils.common.CommonDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;
import com.ibm.watsonhealth.database.utils.model.ColumnBase;
import com.ibm.watsonhealth.database.utils.model.PrimaryKeyDef;
import com.ibm.watsonhealth.database.utils.model.Table;

/**
 * A Derby database target
 * @author rarnold
 *
 */
public class DerbyAdapter extends CommonDatabaseAdapter {
    private static final Logger logger = Logger.getLogger(DerbyAdapter.class.getName());

    // Different warning messages we track so that we only have to report them once
    private enum MessageKey {
        MULTITENANCY, CREATE_VAR, CREATE_PERM, ENABLE_ROW_ACCESS, DISABLE_ROW_ACCESS, PARTITIONING, 
        ROW_TYPE, ROW_ARR_TYPE, DROP_TYPE, CREATE_PROC, DROP_PROC, TABLESPACE
    }
    
    // Just warn once for each unique message key. This cleans up build logs a lot
    private static final Set<MessageKey> warned = ConcurrentHashMap.newKeySet();
    

    /**
     * Public constructor
     * @param tgt the target database we want to manage
     */
    public DerbyAdapter(IDatabaseTarget tgt) {
        super(tgt, new DerbyTranslator());

    }

    /**
     * Once write each warning message once
     * @param msg
     */
    public void warnOnce(MessageKey messageKey, String msg) {
        if (logger.isLoggable(Level.WARNING) && !warned.contains(messageKey)) {
            warned.add(messageKey);
            logger.warning("[ONCE] " + msg);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createTable(java.lang.String, java.util.List, com.ibm.watsonhealth.fhir.schema.model.PrimaryKeyDef, com.ibm.watsonhealth.fhir.schema.model.PartitionDef)
     */
    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
            String tablespaceName) {
        
        // Derby doesn't support partitioning, so we ignore tenantColumnName
        if (tenantColumnName != null) {
            warnOnce(MessageKey.MULTITENANCY, "Derby does support not multi-tenancy: " + name);
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
        warnOnce(MessageKey.CREATE_VAR, "Derby does not support CREATE VARIABLE for: " + variableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#createPermission(java.lang.String, java.lang.String)
     */
    @Override
    public void createPermission(String schemaName, String permissionName, String tableName, String predicate) {
        warnOnce(MessageKey.CREATE_PERM, "Derby does not support CREATE PERMISSION for: " + permissionName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.IDatabase#activateRowAccessControl(java.lang.String)
     */
    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        warnOnce(MessageKey.ENABLE_ROW_ACCESS, "Derby does not support ROW ACCESS CONTROL for table: " + tableName);
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
        warnOnce(MessageKey.DISABLE_ROW_ACCESS, "Derby does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createTenantPartitions(java.util.Collection, java.lang.String, int)
     */
    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        warnOnce(MessageKey.PARTITIONING, "Derby does not support tenant partitioning");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createRowType(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        warnOnce(MessageKey.ROW_TYPE, "Create row type not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createArrType(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        warnOnce(MessageKey.ROW_ARR_TYPE, "Create array row type not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#dropType(java.lang.String, java.lang.String)
     */
    @Override
    public void dropType(String schemaName, String typeName) {
        warnOnce(MessageKey.DROP_TYPE, "Drop type not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createProcedure(java.lang.String, java.lang.String, java.util.function.Supplier)
     */
    @Override
    public void createProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        warnOnce(MessageKey.CREATE_PROC, "Create procedure not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#dropProcedure(java.lang.String, java.lang.String)
     */
    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        warnOnce(MessageKey.DROP_PROC, "Drop procedure not supported in Derby");
    }

    @Override
    public void createTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not supported in Derby");
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Drop tablespace not supported in Derby");
    }

    @Override
    public void detachPartition(String schemaName, String tableName, String partitionName, String newTableName) {
        warnOnce(MessageKey.PARTITIONING, "Detach partition not supported in Derby");
    }

    @Override
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId,
            String tenantStagingTable) {
        warnOnce(MessageKey.PARTITIONING, "Remove tenant partitions not supported in Derby");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter#createTablespace(java.lang.String, int)
     */
    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not supported in Derby");
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
