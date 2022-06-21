/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DistributionContext;
import com.ibm.fhir.database.utils.api.DuplicateNameException;
import com.ibm.fhir.database.utils.api.DuplicateSchemaException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.UndefinedNameException;
import com.ibm.fhir.database.utils.common.AddForeignKeyConstraint;
import com.ibm.fhir.database.utils.common.CommonDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.ForeignKeyConstraint;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.With;

/**
 * A PostgreSql database target
 */
public class PostgresAdapter extends CommonDatabaseAdapter {
    private static final Logger logger = Logger.getLogger(PostgresAdapter.class.getName());

    // Different warning messages we track so that we only have to report them once
    protected enum MessageKey {
        MULTITENANCY,
        CREATE_VAR,
        CREATE_PERM,
        ENABLE_ROW_ACCESS,
        DISABLE_ROW_ACCESS,
        PARTITIONING,
        ROW_TYPE,
        ROW_ARR_TYPE,
        DROP_TYPE,
        CREATE_PROC,
        DROP_PROC,
        TABLESPACE,
        ALTER_TABLE_SEQ_CACHE,
        DROP_PERMISSION,
        DROP_VARIABLE
    }

    // Constant for better readability in method calls
    protected static final boolean USE_SCHEMA_PREFIX = true;

    // Just warn once for each unique message key. This cleans up build logs a lot
    private static final Set<MessageKey> warned = ConcurrentHashMap.newKeySet();

    /**
     * Public constructor
     * @param tgt the target database we want to manage
     */
    public PostgresAdapter(IDatabaseTarget tgt) {
        super(tgt, new PostgresTranslator());
    }

    public PostgresAdapter(IConnectionProvider cp) {
        super(cp, new PostgresTranslator());
    }

    public PostgresAdapter() {
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
            IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
            DistributionContext distributionContext) {

        // PostgreSql doesn't support partitioning, so we ignore tenantColumnName
        if (tenantColumnName != null) {
            warnOnce(MessageKey.MULTITENANCY, "PostgreSql does not support multi-tenancy: " + name);
        }

        // We also ignore tablespace for PostgreSql
        String ddl = buildCreateTableStatement(schemaName, name, columns, primaryKey, identity, null, withs, checkConstraints);
        runStatement(ddl);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName, List<OrderedColumnDef> indexColumns,
            List<String> includeColumns, DistributionContext distributionContext) {
        // PostgreSql doesn't support include columns, so we just have to create a normal index
        createUniqueIndex(schemaName, tableName, indexName, tenantColumnName, indexColumns, distributionContext);
    }

    @Override
    public void createIntVariable(String schemaName, String variableName) {
        warnOnce(MessageKey.CREATE_VAR, "PostgreSql does not support CREATE VARIABLE for: " + variableName);
    }

    @Override
    public void createOrReplacePermission(String schemaName, String permissionName, String tableName, String predicate) {
        warnOnce(MessageKey.CREATE_PERM, "PostgreSql does not support CREATE PERMISSION for: " + permissionName);
    }

    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        warnOnce(MessageKey.ENABLE_ROW_ACCESS, "PostgreSql does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    @Override
    public void setIntVariable(String schemaName, String variableName, int value) {
        // As this is a runtime issue, we throw as an exception instead of
        // simply logging a warning. This shouldn't be called in the case
        // of a PostgreSql database
        throw new IllegalStateException("setIntVariable not supported on PostgreSql for: " + variableName);
    }

    @Override
    public void deactivateRowAccessControl(String schemaName, String tableName) {
        warnOnce(MessageKey.DISABLE_ROW_ACCESS, "PostgreSql does not support ROW ACCESS CONTROL for table: " + tableName);
    }

    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        warnOnce(MessageKey.PARTITIONING, "PostgreSql does not support tenant partitioning");
    }

    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        warnOnce(MessageKey.ROW_TYPE, "Create row type not supported in PostgreSql");
    }

    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        warnOnce(MessageKey.ROW_ARR_TYPE, "Create array row type not supported in PostgreSql");
    }

    @Override
    public void dropType(String schemaName, String typeName) {
        warnOnce(MessageKey.DROP_TYPE, "Drop type not supported in PostgreSql");
    }

    @Override
    public void createTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not supported in PostgreSql");
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Drop tablespace not supported in PostgreSql");
    }

    @Override
    public void detachPartition(String schemaName, String tableName, String partitionName, String newTableName) {
        warnOnce(MessageKey.PARTITIONING, "Detach partition not supported in PostgreSql");
    }

    @Override
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId) {
        warnOnce(MessageKey.PARTITIONING, "Remove tenant partitions not supported in PostgreSql");
    }

    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not supported in PostgreSql");
    }

    @Override
    public boolean doesTableExist(String schemaName, String tableName) {
        PostgresDoesTableExist dao = new PostgresDoesTableExist(schemaName, tableName);
        return runStatement(dao);
    }

    /**
     * Check if the named view currently exists
     * @param schemaName
     * @param viewName
     * @return
     */
    private boolean doesViewExist(String schemaName, String viewName) {
        PostgresDoesViewExist dao = new PostgresDoesViewExist(schemaName, viewName);
        return runStatement(dao);
    }

    /**
     * Check if the named index currently exists
     * @param schemaName
     * @param indexName
     * @return
     */
    private boolean doesIndexExist(String schemaName, String indexName) {
        PostgresDoesIndexExist dao = new PostgresDoesIndexExist(schemaName, indexName);
        return runStatement(dao);
    }

    @Override
    public void createSequence(String schemaName, String sequenceName, long startWith, int cache, int incrementBy) {
        /* CREATE SEQUENCE fhir_sequence
         *     AS BIGINT
         *     START WITH 1000
         *     CACHE 1000
         *     NO CYCLE;
        */
        final String sname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        final String ddl = "CREATE SEQUENCE " + sname + " AS BIGINT "
                + " INCREMENT BY " + incrementBy
                + " START WITH " + startWith
                + " CACHE " + cache
                + " NO CYCLE";
        runStatement(ddl);
    }

    @Override
    public String varbinaryClause(int size) {
        return "BYTEA";
    }

    @Override
    public String blobClause(long size, int inlineSize) {
        return "BYTEA";
    }

    @Override
    public String varcharClause(int size) {
        return "VARCHAR(" + size + ")";
    }

    @Override
    public String timestampClause(Integer precision) {
        // PostgreSql doesn't support the timestamp precision argument
        return "TIMESTAMP";
    }


    @Override
    public String doubleClause() {
        return "DOUBLE PRECISION";
    }

    @Override
    public String clobClause() {
        return "TEXT";
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema,
        String targetTable, String targetColumnName, String tenantColumnName, List<String> columns, boolean enforced) {
        // If enforced=false, skip the constraint because PostgreSQL doesn't support unenforced constraints
        if (enforced) {
            // Make the call, but without the tenantColumnName because PostgreSQL doesn't support our multi-tenant implementation
            super.createForeignKeyConstraint(constraintName, schemaName, name, targetSchema, targetTable, targetColumnName, null, columns, true);
        }
    }

    @Override
    protected List<OrderedColumnDef> prefixTenantColumn(String tenantColumnName, List<OrderedColumnDef> columns) {
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
    public void createUniqueIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
        List<OrderedColumnDef> indexColumns, DistributionContext distributionContext) {
        indexColumns = prefixTenantColumn(tenantColumnName, indexColumns);
        // Postgresql doesn't support index name prefixed with the schema name.
        String ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
        runStatement(ddl);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName, String tenantColumnName,
        List<OrderedColumnDef> indexColumns) {
        indexColumns = prefixTenantColumn(tenantColumnName, indexColumns);
        // Postgresql doesn't support index name prefixed with the schema name.
        String ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
        runStatement(ddl);
    }

    @Override
    public boolean checkCompatibility(String adminSchema) {
        final String statement = "SELECT 1";
        boolean result = false;
        try (Connection c = connectionProvider.getConnection(); PreparedStatement stmt = c.prepareStatement(statement)) {
            result = stmt.execute();
        } catch (SQLException x) {
            throw this.getTranslator().translate(x);
        }
        return result;
    }

    @Override
    public void createSchema(String schemaName){
        try {
            String ddl = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
            runStatement(ddl);
            logger.log(Level.INFO, "The schema '" + schemaName + "' is created or already exists");
        } catch (DuplicateNameException | DuplicateSchemaException e) {
            logger.log(Level.WARNING, "The schema '" + schemaName + "' already exists; proceed with caution.");
        }
    }

    /*
     * @implNote the following are NOT supported on postgres, and thus, we're logging out FINE only.
     */
    @Override
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        logger.fine("Create or replace procedure not run on [" + objectName + "].  This is as expected");
    }

    @Override
    public void grantProcedurePrivileges(String schemaName, String procedureName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        logger.fine("Grant procedure not run on [" + objectName + "]. This is as expected");
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        logger.fine("Drop procedure not run on [" + objectName + "]. This is as expected");
    }

    @Override
    public void dropDetachedPartitions(Collection<Table> tables, String schemaName, int tenantId) {
        warnOnce(MessageKey.PARTITIONING, "Partitioning not supported. This is as expected");
    }

    @Override
    public void dropTenantTablespace(int tenantId) {
        logger.fine("Drop tablespace not supported. This is as expected");
    }

    @Override
    public void disableForeignKey(String schemaName, String tableName, String constraintName) {
        // not expecting this to be called for this adapter
        throw new UnsupportedOperationException("Disable FK currently not supported for this adapter.");
    }

    @Override
    public void enableForeignKey(String schemaName, String tableName, String constraintName) {
        // not expecting this to be called for this adapter
        throw new UnsupportedOperationException("Disable FK currently not supported for this adapter.");
    }

    @Override
    public boolean doesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName) {
        // check the catalog to see if the named constraint exists
        PostgresDoesForeignKeyConstraintExist fkExists = new PostgresDoesForeignKeyConstraintExist(schemaName, constraintName);
        // runStatement may return null in some unit-tests, so we need to protect against that
        Boolean val = runStatement(fkExists);
        return val != null && val.booleanValue();
    }

    @Override
    public void setIntegrityOff(String schemaName, String tableName) {
        // not expecting this to be called for this adapter
        throw new UnsupportedOperationException("Set integrity off not supported for this adapter.");
    }

    @Override
    public void setIntegrityUnchecked(String schemaName, String tableName) {
        // not expecting this to be called for this adapter
        throw new UnsupportedOperationException("Set integrity unchecked not supported for this adapter.");
    }

    @Override
    public void alterTableColumnIdentityCache(String schemaName, String tableName, String columnName, int cache) {
        // Not supported by PostgreSQL

        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        DataDefinitionUtil.assertValidName(columnName);

        // modify the CACHE property of the identity column
        final String ddl = "ALTER TABLE " + qname + " ALTER COLUMN " + columnName + " SET CACHE " + cache;

        warnOnce(MessageKey.ALTER_TABLE_SEQ_CACHE, "Not supported in PostgreSQL: " + ddl);
    }

    @Override
    public void dropPermission(String schemaName, String permissionName) {
        final String nm = getQualifiedName(schemaName, permissionName);
        final String ddl = "DROP PERMISSION " + nm;

        warnOnce(MessageKey.DROP_PERMISSION, "Not supported in PostgreSQL: " + ddl);
    }

    @Override
    public void dropSequence(String schemaName, String sequenceName) {
        final String sname = DataDefinitionUtil.getQualifiedName(schemaName.toLowerCase(), sequenceName.toLowerCase());
        final String ddl = "DROP SEQUENCE IF EXISTS " + sname;

        try {
            runStatement(ddl);
        } catch (UndefinedNameException x) {
            logger.warning(ddl + "; Sequence not found");
        }
    }

    @Override
    public void dropVariable(String schemaName, String variableName) {
        final String nm = getQualifiedName(schemaName, variableName);
        final String ddl = "DROP VARIABLE " + nm;
        warnOnce(MessageKey.DROP_VARIABLE, "Not supported in PostgreSQL: " + ddl);
    }

    @Override
    public void createOrReplaceFunction(String schemaName, String functionName, Supplier<String> supplier) {
        // For PostgreSQL, we need to drop the function first to avoid ending up
        // with the same function name having different args (non-unique) which
        // causes problems later on
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, functionName);
        logger.info("Dropping current function " + objectName);

        final String DROP_SPECIFIC =
                "select p.oid::regproc || '(' || pg_get_function_identity_arguments(p.oid) || ')' " +
                "    FROM pg_catalog.pg_proc p" +
                "    WHERE p.oid::regproc::text = LOWER(?)";

        List<String> existingFunctions = new ArrayList<>();
        if (connectionProvider != null) {
            try (Connection c = connectionProvider.getConnection()) {
                try (PreparedStatement p = c.prepareStatement(DROP_SPECIFIC)) {
                    p.setString(1, objectName);
                    if (p.execute()) {
                        // Closes with PreparedStatement
                        ResultSet rs = p.getResultSet();
                        while (rs.next()) {
                            existingFunctions.add(rs.getString(1));
                        }
                    }
                }
            } catch (SQLException x) {
                throw getTranslator().translate(x);
            }
        }

        // As the signatures are mutated, we don't want to be in the situation where the signature change, and
        // we can't drop.
        for (String existingFunction : existingFunctions) {
            final StringBuilder ddl = new StringBuilder()
                    .append("DROP FUNCTION IF EXISTS ")
                    .append(existingFunction);
            try {
                runStatement(ddl.toString());
            } catch (UndefinedNameException x) {
                logger.warning(ddl + "; FUNCTION not found");
            }
        }
        super.createOrReplaceFunction(schemaName, functionName, supplier);
    }

    @Override
    public List<SchemaInfoObject> listSchemaObjects(String schemaName) {
        List<SchemaInfoObject> result = new ArrayList<>();
        PostgresListTablesForSchema listTables = new PostgresListTablesForSchema(schemaName);
        result.addAll(runStatement(listTables));
        
        PostgresListViewsForSchema listViews = new PostgresListViewsForSchema(schemaName);
        result.addAll(runStatement(listViews));
        
        PostgresListSequencesForSchema listSequences = new PostgresListSequencesForSchema(schemaName);
        result.addAll(runStatement(listSequences));

        return result;
    }
    
    @Override
    public void dropForeignKey(String schemaName, String tableName, String constraintName) {
        // For PostgreSQL, we need to make sure the constraint exists before
        // we drop it, because exceptions break the transaction
        PostgresDoesForeignKeyConstraintExist fkExists = new PostgresDoesForeignKeyConstraintExist(schemaName, constraintName);
        if (runStatement(fkExists)) {
            super.dropForeignKey(schemaName, tableName, constraintName);
        }
    }
    
    @Override
    public void dropTable(String schemaName, String tableName) {

        // Check the table exists first, otherwise the exception will
        // break the current transaction (PostgreSQL behavior)
        if (doesTableExist(schemaName, tableName)) {
            super.dropTable(schemaName, tableName);
        }
    }
    
    @Override
    public void dropView(String schemaName, String viewName) {
        // Check the view exists before we try to drop it, otherwise
        // the exception will break the current transaction
        if (doesViewExist(schemaName, viewName)) {
            super.dropView(schemaName, viewName);
        }
    }

    @Override
    public void dropIndex(String schemaName, String indexName) {
        // Check the index exists before we try to drop it, otherwise
        // the exception will break the current transaction
        if (doesIndexExist(schemaName, indexName)) {
            super.dropIndex(schemaName, indexName);
        }
    }

    @Override
    public void grantSchemaUsage(String schemaName, String grantToUser) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(grantToUser);
        final String grant = "GRANT USAGE ON SCHEMA " + schemaName + " TO " + grantToUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged

        runStatement(grant);
    }

    @Override
    public void grantAllSequenceUsage(String schemaName, String grantToUser) {
        // Annoyingly for PostgreSQL, when you grant INSERT to a table, you don't
        // automatically get USAGE on the sequence used to implement a generated
        // as identity column.
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(grantToUser);
        final String grant = "GRANT USAGE ON ALL SEQUENCES IN SCHEMA " + schemaName + " TO " + grantToUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged

        runStatement(grant);
    }
}