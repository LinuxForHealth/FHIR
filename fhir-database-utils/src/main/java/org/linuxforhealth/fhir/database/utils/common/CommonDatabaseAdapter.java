/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.api.DistributionContext;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTarget;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTypeAdapter;
import org.linuxforhealth.fhir.database.utils.api.UndefinedNameException;
import org.linuxforhealth.fhir.database.utils.model.CheckConstraint;
import org.linuxforhealth.fhir.database.utils.model.ColumnBase;
import org.linuxforhealth.fhir.database.utils.model.IdentityDef;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;
import org.linuxforhealth.fhir.database.utils.model.PrimaryKeyDef;
import org.linuxforhealth.fhir.database.utils.model.Privilege;
import org.linuxforhealth.fhir.database.utils.model.With;

/**
 * Provides schema control functions common to our supported databases:
 *   PostgreSQL
 *   Derby (for unit tests, not production)
 */
public abstract class CommonDatabaseAdapter implements IDatabaseAdapter, IDatabaseTypeAdapter {
    private static final Logger logger = Logger.getLogger(CommonDatabaseAdapter.class.getName());

    // The target to use for executing our DDL
    protected final IDatabaseTarget target;

    // The source of database connections
    protected final IConnectionProvider connectionProvider;

    // The translator used to to tweak the syntax for the database
    private final IDatabaseTranslator translator;
    /**
     * Protected constructor
     * @param tgt database targeted
     * @param dt the translator for this type of database
     */
    protected CommonDatabaseAdapter(IDatabaseTarget tgt, IDatabaseTranslator dt) {
        this.target = tgt;
        this.translator = dt;
        this.connectionProvider = null;
    }

    /**
     * Public constructor for when we're using a connection provider
     * @param cp
     * @param dt
     */
    protected CommonDatabaseAdapter(IConnectionProvider cp, IDatabaseTranslator dt) {
        this.target = null;
        this.translator = dt;
        this.connectionProvider = cp;
    }

    /**
     * Constructor used by AddColumn only for getting DB type specific column name.
     */
    protected CommonDatabaseAdapter() {
        this.target = null;
        this.translator = null;
        this.connectionProvider = null;
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return this.translator;
    }

    /**
     * Build the list of columns in the create table statement
     */
    protected String buildColumns(List<ColumnBase> columns, IdentityDef identity) {
        StringBuilder result = new StringBuilder();
        for (ColumnBase column: columns) {
            if (result.length() > 0) {
                result.append(", ");
            }

            result.append(column.getName());
            result.append(" ");
            result.append(column.getTypeInfo(this));

            if (identity != null && column.getName().equals(identity.getColumnName())) {
                result.append(" GENERATED " + identity.getGenerated() + " AS IDENTITY");
            } // AS IDENTITY implies NOT NULL so this can be and else if
            else if (!column.isNullable()) {
                result.append(" NOT NULL");
            }

            // Outputs the default value
            if (column.getDefaultVal() != null) {
                result.append(" DEFAULT ");
                result.append(column.getDefaultVal());
            }
        }
        return result.toString();
    }

    /**
     * Generate a create table statement suitable for Derby
     *
     * @param schema
     * @param name
     * @param columns
     * @param pkDef
     * @param tablespaceName
     * @param withs the list of table metadata parameters
     * @return
     */
    protected String buildCreateTableStatement(String schema, String name, List<ColumnBase> columns, 
            PrimaryKeyDef pkDef, IdentityDef identity, String tablespaceName, List<With> withs,
            List<CheckConstraint> checkConstraints) {
        StringBuilder result = new StringBuilder();
        result.append("CREATE TABLE ");
        result.append(getQualifiedName(schema, name));
        result.append('(');
        result.append(buildColumns(columns, identity));

        // Add the primary key definition after the columns
        if (pkDef != null) {
            result.append(", CONSTRAINT ");
            result.append(pkDef.getConstraintName());
            result.append(" PRIMARY KEY (");

            StringBuilder cols = new StringBuilder();
            for (String c: pkDef.getColumns()) {
                if (cols.length() > 0) {
                    cols.append(", ");
                }
                cols.append(c);
            }

            result.append(cols);
            result.append(')');
        }
        
        // Add any CHECK constraints
        for (CheckConstraint cc: checkConstraints) {
            result.append(", CONSTRAINT ");
            result.append(cc.constraintName);
            result.append(" CHECK (");
            result.append(cc.getConstraintExpression());
            result.append(")");
        }
        result.append(')');

        // Creates WITH (fillfactor=70, key2=val2);
        if (withs != null && !withs.isEmpty()) {
            StringBuilder builder = new StringBuilder(" WITH (");
            builder.append(
                withs.stream()
                    .map(with -> with.buildWithComponent())
                    .collect(Collectors.joining(",")));
            builder.append(")");
            result.append(builder.toString());
        }

        if (tablespaceName != null) {
            DataDefinitionUtil.assertValidName(tablespaceName);
            result.append(" IN ");
            result.append(tablespaceName);
        }
        return result.toString();
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns, List<String> includeColumns, DistributionContext distributionContext) {
        String ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, indexColumns, includeColumns, true);
        runStatement(ddl);
    }

    @Override
    public void createUniqueIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns, DistributionContext distributionContext) {
        String ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, indexColumns, true);
        runStatement(ddl);
    }

    @Override
    public void createIndex(String schemaName, String tableName, String indexName,
        List<OrderedColumnDef> indexColumns) {
        String ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, indexColumns, true);
        runStatement(ddl);
    }

    /**
     * Prefix the tenantColumnName to the list of columns, or do nothing
     * if tenantColumnName is null
     * @param tenantColumnName
     * @param columns
     * @return
     */
    protected List<OrderedColumnDef> prefixTenantColumn(String tenantColumnName, List<OrderedColumnDef> columns) {
        List<OrderedColumnDef> result;
        if (tenantColumnName == null) {
            result = columns; // no change
        }
        else {
            result = new ArrayList<>(columns.size() + 1);
            result.add(new OrderedColumnDef(tenantColumnName, null, null));
            result.addAll(columns);
        }

        return result;
    }

    /**
     * Execute the statement on a connection managed by our connection provider
     * @param ddl
     */
    protected void runStatement(final String ddl) {
        if (this.connectionProvider != null) {
            try (Connection c = connectionProvider.getConnection()) {
                runStatement(c, ddl);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        } else {
            target.runStatement(this.translator, ddl);
        }
    }

    /**
     * Run the given SQL statement on the connection
     * @param c
     * @param ddl
     * @throws SQLException
     */
    private void runStatement(Connection c, final String ddl) throws SQLException {
        if (logger.isLoggable(Level.FINE)) {
            System.out.println(ddl);
        }

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
    }


    /**
     * Return the fully qualified name in the form "SCHEMA.OBJECT"
     * Validates that both schema and object names are valid
     * @param schemaName
     * @param objectName
     * @return the fully qualified name
     * @throws IllegalArgumentException if either name is not a valid database object name
     */
    public String getQualifiedName(String schemaName, String objectName) {
        return DataDefinitionUtil.getQualifiedName(schemaName, objectName);
    }

    @Override
    public void dropTable(String schemaName, String tableName) {
        final String nm = getQualifiedName(schemaName, tableName);
        final String ddl = "DROP TABLE " + nm;

        try {
            runStatement(ddl);
        } catch (UndefinedNameException x) {
            logger.warning(ddl + "; TABLE not found");
        }
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        final String nm = getQualifiedName(schemaName, procedureName);
        final String ddl = "DROP PROCEDURE IF EXISTS " + nm;

        try {
            runStatement(ddl);
        } catch (UndefinedNameException x) {
            logger.warning(ddl + "; PROCEDURE not found");
        }
    }

    @Override
    public void dropFunction(String schemaName, String functionName) {
        final String nm = getQualifiedName(schemaName, functionName);
        final String ddl = "DROP FUNCTION IF EXISTS " + nm;

        try {
            runStatement(ddl);
        } catch (UndefinedNameException x) {
            logger.warning(ddl + "; FUNCTION not found");
        }
    }

    @Override
    public void dropPermission(String schemaName, String permissionName) {
        final String nm = getQualifiedName(schemaName, permissionName);
        final String ddl = "DROP PERMISSION " + nm;

        try {
            runStatement(ddl);
        }
        catch (UndefinedNameException x) {
            logger.warning(ddl + "; PERMISSION not found");
        }
    }

    @Override
    public void dropVariable(String schemaName, String variableName) {
        final String nm = getQualifiedName(schemaName, variableName);
        final String ddl = "DROP VARIABLE " + nm;

        try {
            runStatement(ddl);
        }
        catch (UndefinedNameException x) {
            logger.warning(ddl + "; VARIABLE not found");
        }
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
            ddl.append(" NOT ENFORCED");
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
    public void createUniqueConstraint(String constraintName, List<String> columns, String schemaName, String name) {
        String tableName = DataDefinitionUtil.getQualifiedName(schemaName, name);

        StringBuilder ddl = new StringBuilder();
        ddl.append("ALTER TABLE ");
        ddl.append(tableName);
        ddl.append(" ADD CONSTRAINT ");
        ddl.append(constraintName);
        ddl.append(" UNIQUE (");
        ddl.append(DataDefinitionUtil.join(columns));
        ddl.append(")");

        try {
            runStatement(ddl.toString());
        } catch (Exception x) {
            logger.warning("Statement failed [" + x.getMessage() + "] [" + ddl.toString() + "]");
            throw x;
        }
    }

    /**
     * Run the statement using the connectionProvider to obtain a new
     * connection. Also, there should be a transaction open on the current
     * thread at this time
     * @param stmt
     */
    @Override
    public void runStatement(IDatabaseStatement stmt) {
        if (this.connectionProvider != null) {
            try (Connection c = connectionProvider.getConnection()) {
                stmt.run(getTranslator(), c);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        } else {
            this.target.runStatement(getTranslator(), stmt);
        }
    }

    @Override
    public <T> T runStatement(IDatabaseSupplier<T> supplier) {
        if (this.connectionProvider != null) {
            // Expects some sort of outside transaction management and connection wrapping
            try (Connection c = connectionProvider.getConnection()) {
                return supplier.run(getTranslator(), c);
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        } else {
            return this.target.runStatement(getTranslator(), supplier);
        }
    }

    @Override
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        logger.info("Create or replace procedure " + objectName);

        final StringBuilder ddl = new StringBuilder()
                .append("CREATE OR REPLACE PROCEDURE ")
                .append(objectName)
                .append(System.lineSeparator())
                .append(supplier.get());

        final String ddlString = ddl.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(ddlString);
        }

        runStatement(ddlString);
    }

    @Override
    public void createOrReplaceFunction(String schemaName, String functionName, Supplier<String> supplier) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, functionName);
        logger.info("Create or replace function " + objectName);

        final StringBuilder ddl = new StringBuilder()
                .append("CREATE OR REPLACE FUNCTION ")
                .append(objectName)
                .append(System.lineSeparator())
                .append(supplier.get());

        final String ddlString = ddl.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(ddlString);
        }
        runStatement(ddlString);
    }

    @Override
    public void createSequence(String schemaName, String sequenceName, long startWith, int cache, int incrementBy) {
        /*
         * Example syntax:
         * <CODE>CREATE SEQUENCE <sequence-name>
         * AS BIGINT
         * START WITH <start-with>
         * CACHE <cache>
         * NO CYCLE;</CODE>
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
    public void dropSequence(String schemaName, String sequenceName) {
        final String sname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        final String ddl = "DROP SEQUENCE " + sname;

        try {
            runStatement(ddl);
        }
        catch (UndefinedNameException x) {
            logger.warning(ddl + "; Sequence not found");
        }
    }

    @Override
    public void alterSequenceRestartWith(String schemaName, String sequenceName, long restartWith, int cache, int incrementBy) {
        // make sure we never reduce the sequence value
        GetSequenceNextValueDAO dao = new GetSequenceNextValueDAO(schemaName, sequenceName);
        Long maxValue = runStatement(dao);
        if (maxValue != null && maxValue > restartWith) {
            restartWith = maxValue;
        }

        // Note the keyword RESTART instead of START.
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        final String ddl = "ALTER SEQUENCE " + qname
                + " RESTART WITH " + restartWith
                + " INCREMENT BY " + incrementBy
                + " CACHE " + cache;

        // so important, we log it
        logger.info(ddl);

        runStatement(ddl);
    }

    @Override
    public void alterTableColumnIdentityCache(String schemaName, String tableName, String columnName, int cache) {
        // Check input strings are clean
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        DataDefinitionUtil.assertValidName(columnName);

        // modify the CACHE property of the identity column
        final String ddl = "ALTER TABLE " + qname + " ALTER COLUMN " + columnName + " SET CACHE " + cache;

        // so important, we log it
        logger.info(ddl);

        runStatement(ddl);
    }

    @Override
    public void dropForeignKey(String schemaName, String tableName, String constraintName) {
        DropForeignKeyConstraint cmd = new DropForeignKeyConstraint(schemaName, tableName, constraintName);
        runStatement(cmd);
    }

    /**
     * get the privileges as a comma-separated string
     * @param privileges
     * @return
     */
    private String privilegeString(Collection<Privilege> privileges) {
        return privileges.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public void grantObjectPrivileges(String schemaName, String tableName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        DataDefinitionUtil.assertValidName(toUser);
        final String privs = privilegeString(privileges);
        final String grant = "GRANT " + privs + " ON " + objectName + " TO " + toUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged

        runStatement(grant);
    }

    @Override
    public void grantProcedurePrivileges(String schemaName, String procedureName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        DataDefinitionUtil.assertValidName(toUser);

        final String privs = privilegeString(privileges);
        final String grant = "GRANT " + privs + " ON PROCEDURE " + objectName + " TO " + toUser;
        logger.info("Applying: " + grant); // Grants are very useful to see logged
        runStatement(grant);
    }

    @Override
    public void grantFunctionPrivileges(String schemaName, String functionName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, functionName);
        DataDefinitionUtil.assertValidName(toUser);

        final String privs = privilegeString(privileges);
        final String grant = "GRANT " + privs + " ON FUNCTION " + objectName + " TO " + toUser;
        logger.info("Applying: " + grant); // Grants are very useful to see logged
        runStatement(grant);
    }

    @Override
    public void grantVariablePrivileges(String schemaName, String variableName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, variableName);
        DataDefinitionUtil.assertValidName(toUser);
        final String privs = privilegeString(privileges);
        final String grant = "GRANT " + privs + " ON VARIABLE " + objectName + " TO " + toUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged
        runStatement(grant);
    }

    @Override
    public void grantSequencePrivileges(String schemaName, String variableName, Collection<Privilege> privileges, String toUser) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, variableName);
        DataDefinitionUtil.assertValidName(toUser);
        final String privs = privilegeString(privileges);
        final String grant = "GRANT " + privs + " ON SEQUENCE " + objectName + " TO " + toUser;

        logger.info("Applying: " + grant); // Grants are very useful to see logged
        runStatement(grant);
    }

    @Override
    public void dropIndex(String schemaName, String indexName) {
        // Create the qualified name, making sure the input is also secure
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, indexName);
        final String ddl = "DROP INDEX " + qname;

        try {
            runStatement(ddl);
        } catch (UndefinedNameException x) {
            logger.warning(ddl + "; INDEX not found");
        }
    }

    @Override
    public void dropView(String schemaName, String viewName) {
        // don't propagate errors...we don't really care if the drop failed
        DropViewDAO dao = new DropViewDAO(schemaName, viewName, false);
        runStatement(dao);
    }

    @Override
    public void createOrReplaceView(String schemaName, String viewName, String selectClause) {
        CreateOrReplaceViewDAO dao = new CreateOrReplaceViewDAO(schemaName, viewName, selectClause);
        runStatement(dao);
    }

    @Override
    public void createView(String schemaName, String viewName, String selectClause) {
        // for databases (like Derby) without CREATE OR REPLACE support
        CreateOrReplaceViewDAO dao = new CreateOrReplaceViewDAO(schemaName, viewName, selectClause, false);
        runStatement(dao);
    }

    @Override
    public void alterTableAddColumn(String schemaName, String tableName, ColumnBase column) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String col = buildColumns(Collections.singletonList(column), null);

        final StringBuilder ddl = new StringBuilder();
        ddl.append("ALTER TABLE ");
        ddl.append(qname);
        ddl.append(" ADD COLUMN ");
        ddl.append(col);
        runStatement(ddl.toString());
    }

    @Override
    public void applyDistributionRules(String schemaName, String tableName, DistributionContext distributionContext) {
        // NOP. Only used for distributed databases like Citus
    }

    @Override
    public void distributeFunction(String schemaName, String functionName, int distributeByParamNumber) {
        // NOP. Only used for distributed databases like Citus
    }

    @Override
    public void grantSchemaUsage(String schemaName, String grantToUser) {
        logger.info("Grant usage on schema not required for '" + schemaName + "' on this database");
    }
    @Override
    public void grantAllSequenceUsage(String schemaName, String grantToUser) {
        logger.info("Grant usage on all sequences in schema not required for '" + schemaName + "' on this database");
    }
}