/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DuplicateNameException;
import com.ibm.fhir.database.utils.api.DuplicateSchemaException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.common.AddForeignKeyConstraint;
import com.ibm.fhir.database.utils.common.CommonDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.ForeignKeyConstraint;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Table;

/**
 * A Derby database target
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

    public DerbyAdapter(IConnectionProvider cp) {
        super(cp, new DerbyTranslator());
    }

    public DerbyAdapter() {
        super();
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

    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
            IdentityDef identity, String tablespaceName) {
        // Derby doesn't support partitioning, so we ignore tenantColumnName
        if (tenantColumnName != null) {
            warnOnce(MessageKey.MULTITENANCY, "Derby does not support multi-tenancy on: [" + name + "]");
        }

        // We also ignore tablespace for Derby
        String ddl = buildCreateTableStatement(schemaName, name, columns, primaryKey, identity, null);
        runStatement(ddl);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<String> indexColumns,
            List<String> includeColumns) {

        // Derby doesn't support include columns, so we just have to create a normal index
        createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns);
    }

    @Override
    public void createIntVariable(String schemaName, String variableName) {
        warnOnce(MessageKey.CREATE_VAR, "Derby does not support CREATE VARIABLE for: " + variableName);
    }

    @Override
    public void createOrReplacePermission(String schemaName, String permissionName, String tableName, String predicate) {
        warnOnce(MessageKey.CREATE_PERM, "Derby does not support CREATE PERMISSION for: " + permissionName);
    }

    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        warnOnce(MessageKey.ENABLE_ROW_ACCESS, "Derby does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    @Override
    public void setIntVariable(String schemaName, String variableName, int value) {
        // As this is a runtime issue, we throw as an exception instead of
        // simply logging a warning. This shouldn't be called in the case
        // of a Derby database
        throw new IllegalStateException("setIntVariable not supported on Derby for: " + variableName);
    }

    @Override
    public void deactivateRowAccessControl(String schemaName, String tableName) {
        warnOnce(MessageKey.DISABLE_ROW_ACCESS, "Derby does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        warnOnce(MessageKey.PARTITIONING, "Derby does not support tenant partitioning");
    }

    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        warnOnce(MessageKey.ROW_TYPE, "Create row type not supported in Derby");
    }

    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        warnOnce(MessageKey.ROW_ARR_TYPE, "Create array row type not supported in Derby");
    }

    @Override
    public void dropType(String schemaName, String typeName) {
        warnOnce(MessageKey.DROP_TYPE, "Drop type not supported in Derby");
    }

    @Override
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        warnOnce(MessageKey.CREATE_PROC, "Create procedure not supported in Derby");
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        warnOnce(MessageKey.DROP_PROC, "Drop procedure not supported in Derby");
    }

    @Override
    public void createTablespace(String tablespaceName) {
        logger.fine("Create tablespace not supported in Derby");
    }

    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        logger.fine("Create tablespace not supported in Derby");
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        logger.fine("Drop tablespace not supported in Derby");
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

    @Override
    public boolean doesTableExist(String schemaName, String tableName) {
        DerbyDoesTableExist dao = new DerbyDoesTableExist(schemaName, tableName);
        return runStatement(dao);
    }

    @Override
    public void createSequence(String schemaName, String sequenceName, int cache) {
        /* CREATE SEQUENCE fhir_sequence
         *     AS BIGINT
         *     START WITH 1
         *     CACHE 1000
         *     NO CYCLE;
        */
        // Derby doesn't support CACHE
        final String sname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        final String ddl = "CREATE SEQUENCE " + sname + " AS BIGINT START WITH 1 NO CYCLE";
        runStatement(ddl);

    }

    @Override
    public String varbinaryClause(int size) {
        // I have no idea why Derby doesn't use VARBINARY...but oh well
        return "VARCHAR(" + size + ") FOR BIT DATA";
    }

    @Override
    public String blobClause(long size, int inlineSize) {
        // Derby doesn't support the INLINE feature (which greatly helps with
        // performance on DB2)
        return "BLOB(" + size + ")";
    }

    @Override
    public String varcharClause(int size) {
        return "VARCHAR(" + size + ")";
    }

    @Override
    public String timestampClause(Integer precision) {
        // Derby doesn't support the timestamp precision argument
        return "TIMESTAMP";
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema,
        String targetTable, String targetColumnName, String tenantColumnName, List<String> columns, boolean enforced) {
        // If enforced=false, skip the constraint because Derby doesn't support unenforced constraints
        if (enforced) {
            // Make the call, but without the tenantColumnName because Derby doesn't support our multi-tenant implementation
            super.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, targetColumnName, null, columns, true);
        }
    }

    @Override
    protected List<String> prefixTenantColumn(String tenantColumnName, List<String> columns) {
        // No tenant support, so simply return the columns list unchanged, without prefixing
        // the tenanteColumnName
        return columns;
    }

    @Override
    public void runStatement(IDatabaseStatement stmt) {
        if (stmt instanceof AddForeignKeyConstraint) {
            AddForeignKeyConstraint afk = (AddForeignKeyConstraint) stmt;
            for (ForeignKeyConstraint constraint : afk.getConstraints()) {
                createForeignKeyConstraint(constraint.getConstraintName(), afk.getSchemaName(), afk.getTableName(),
                    constraint.getTargetSchema(), constraint.getTargetTable(), constraint.getTargetColumnName(),
                    afk.getTenantColumnName(), constraint.getColumns(), constraint.isEnforced());
            }
        } else {
            super.runStatement(stmt);
        }
    }

    @Override
    public boolean checkCompatibility(String adminSchema) {
        String stmt = "VALUES 1";
        super.runStatement(stmt);
        return true;
    }

    @Override
    public void createSchema(String schemaName){
        try {
            String ddl = "CREATE SCHEMA " + schemaName;
            runStatement(ddl);
            logger.log(Level.INFO, "The schema '" + schemaName + "' is created");
        } catch (DuplicateNameException | DuplicateSchemaException e) {
            logger.log(Level.WARNING, "The schema '" + schemaName + "' already exists; proceed with caution.");
        }
    }
}