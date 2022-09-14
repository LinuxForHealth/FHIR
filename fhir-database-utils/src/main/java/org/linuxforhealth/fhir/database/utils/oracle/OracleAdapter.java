/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.DistributionContext;
import org.linuxforhealth.fhir.database.utils.api.DuplicateNameException;
import org.linuxforhealth.fhir.database.utils.api.DuplicateSchemaException;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTarget;
import org.linuxforhealth.fhir.database.utils.api.UndefinedNameException;
import org.linuxforhealth.fhir.database.utils.common.AddForeignKeyConstraint;
import org.linuxforhealth.fhir.database.utils.common.CommonDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.common.SchemaInfoObject;
import org.linuxforhealth.fhir.database.utils.model.CheckConstraint;
import org.linuxforhealth.fhir.database.utils.model.ColumnBase;
import org.linuxforhealth.fhir.database.utils.model.ForeignKeyConstraint;
import org.linuxforhealth.fhir.database.utils.model.IdentityDef;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;
import org.linuxforhealth.fhir.database.utils.model.PrimaryKeyDef;
import org.linuxforhealth.fhir.database.utils.model.Privilege;
import org.linuxforhealth.fhir.database.utils.model.With;

/**
 * A Oracle database target
 */
public class OracleAdapter extends CommonDatabaseAdapter {
    private static final Logger logger = Logger.getLogger(OracleAdapter.class.getName());

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
    public OracleAdapter(IDatabaseTarget tgt) {
        super(tgt, new OracleTranslator());
    }

    public OracleAdapter(IConnectionProvider cp) {
        super(cp, new OracleTranslator());
    }

    public OracleAdapter() {
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
    public void createTable(String schemaName, String name, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
            IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
            DistributionContext distributionContext) {

        // We also ignore tablespace for Oracle
        String ddl = buildCreateTableStatement(schemaName, name, columns, primaryKey, identity, null, withs, checkConstraints);
        runStatement(ddl);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName, List<OrderedColumnDef> indexColumns,
            List<String> includeColumns, DistributionContext distributionContext) {
        // Oracle doesn't support include columns, so we just have to create a normal index
        createUniqueIndex(schemaName, tableName, indexName, indexColumns, distributionContext);
    }

    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        warnOnce(MessageKey.ROW_TYPE, "Create row type not utilized in Oracle");
    }

    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        warnOnce(MessageKey.ROW_ARR_TYPE, "Create array row type not utilized in Oracle");
    }

    @Override
    public void dropType(String schemaName, String typeName) {
        warnOnce(MessageKey.DROP_TYPE, "Drop type not utilized in Oracle");
    }

    @Override
    public void createTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not utilized in Oracle");
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        warnOnce(MessageKey.TABLESPACE, "Drop tablespace not utilized in Oracle");
    }

    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        warnOnce(MessageKey.TABLESPACE, "Create tablespace not utilized in Oracle");
    }

    @Override
    public boolean doesTableExist(String schemaName, String tableName) {
        OracleDoesTableExist cmd = new OracleDoesTableExist(schemaName, tableName);
        return runStatement(cmd);
    }

    /**
     * Check if the named view currently exists
     * @param schemaName
     * @param viewName
     * @return
     */
    private boolean doesViewExist(String schemaName, String viewName) {
        OracleDoesViewExist cmd = new OracleDoesViewExist(schemaName, viewName);
        return runStatement(cmd);
    }

    /**
     * Check if the named index currently exists
     * @param schemaName the schema name of the index (not the table)
     * @param indexName the name of the index object
     * @return
     */
    private boolean doesIndexExist(String schemaName, String indexName) {
        OracleDoesIndexExist cmd = new OracleDoesIndexExist(schemaName, indexName);
        return runStatement(cmd);
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
        return "VARCHAR2(" + size + ")";
    }

    @Override
    public String timestampClause(Integer precision) {
        // Oracle doesn't support the timestamp precision argument
        return "TIMESTAMP";
    }


    @Override
    public String doubleClause() {
        return "DOUBLE";
    }

    @Override
    public String clobClause() {
        return "CLOB";
    }

    @Override
    public void createForeignKeyConstraint(String constraintName, String schemaName, String name, String targetSchema,
        String targetTable, String targetColumnName, List<String> columns, boolean enforced) {

        String tableName = DataDefinitionUtil.getQualifiedName(schemaName, name);
        String targetName = DataDefinitionUtil.getQualifiedName(targetSchema, targetTable);

        StringBuilder ddl = new StringBuilder();
        ddl.append("ALTER TABLE ");
        ddl.append(tableName);
        ddl.append(" ADD CONSTRAINT ");
        ddl.append(constraintName);
        ddl.append(" FOREIGN KEY(");
        ddl.append(DataDefinitionUtil.join(columns));
        ddl.append(") REFERENCES ");
        ddl.append(targetName);
        if (!Objects.isNull(targetColumnName) && !targetColumnName.isEmpty()) {
            ddl.append(' ')
                .append('(')
                .append(targetColumnName)
                .append(')');
        }
        if (!enforced) {
            ddl.append(" DISABLED");
        }

        try {
            // Be aware: applying FK constraints in parallel may hit deadlocks in some database implementations
            runStatement(ddl.toString());
        } catch (Exception x) {
            logger.warning("Statement failed (" + x.getMessage() + ") " + ddl.toString());
            throw x;
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
                    constraint.getColumns(), constraint.isEnforced());
            }
        } else {
            super.runStatement(stmt);
        }
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns, DistributionContext distributionContext) {
        // Oracle doesn't support index name prefixed with the schema name.
        String ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
        runStatement(ddl);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns) {
        // Oracle doesn't support index name prefixed with the schema name.
        String ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, indexColumns, !USE_SCHEMA_PREFIX);
        runStatement(ddl);
    }

    @Override
    public boolean checkCompatibility(String adminSchema) {
        final String statement = "SELECT 1 FROM DUAL";
        boolean result = false;
        try (Connection c = connectionProvider.getConnection(); PreparedStatement stmt = c.prepareStatement(statement)) {
            result = stmt.execute();
        } catch (SQLException x) {
            throw this.getTranslator().translate(x);
        }
        return result;
    }

    @Override
    public void createSchema(String schemaName) {
        try {
            String ddl = "CREATE SCHEMA " + schemaName;
            runStatement(ddl);
            logger.log(Level.INFO, "The schema '" + schemaName + "' is created or already exists");
        } catch (DuplicateNameException | DuplicateSchemaException e) {
            logger.log(Level.WARNING, "The schema '" + schemaName + "' already exists; proceed with caution.");
        }
    }

    /*
     * @implNote the following are NOT used in the Oracle implementation of the schema
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
        throw new IllegalStateException("unsupported");
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
        // Not supported by Oracle

        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        DataDefinitionUtil.assertValidName(columnName);

        // modify the CACHE property of the identity column
        final String ddl = "ALTER TABLE " + qname + " ALTER COLUMN " + columnName + " SET CACHE " + cache;

        warnOnce(MessageKey.ALTER_TABLE_SEQ_CACHE, "Not supported in Oracle: " + ddl);
    }

    @Override
    public void dropPermission(String schemaName, String permissionName) {
        final String nm = getQualifiedName(schemaName, permissionName);
        final String ddl = "DROP PERMISSION " + nm;

        warnOnce(MessageKey.DROP_PERMISSION, "Not supported in Oracle: " + ddl);
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
        warnOnce(MessageKey.DROP_VARIABLE, "Not supported in Oracle: " + ddl);
    }

    @Override
    public void createOrReplaceFunction(String schemaName, String functionName, Supplier<String> supplier) {
        // For Oracle, we need to drop the function first to avoid ending up
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
        return null;
    }
    
    @Override
    public void dropForeignKey(String schemaName, String tableName, String constraintName) {
    }
    
    @Override
    public void dropTable(String schemaName, String tableName) {

        // Check the table exists first, otherwise the exception will
        // break the current transaction (Oracle behavior)
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
        // Annoyingly for Oracle, when you grant INSERT to a table, you don't
        // automatically get USAGE on the sequence used to implement a generated
        // as identity column.
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(grantToUser);
        final String grant = "GRANT USAGE ON ALL SEQUENCES IN SCHEMA " + schemaName + " TO " + grantToUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged

        runStatement(grant);
    }
}