/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * An immutable definition of a table
 */
public class Table extends BaseObject {
    // The list of columns in this table
    private final List<ColumnBase> columns = new ArrayList<>();

    // The primary key definition for this table (optional)
    private final PrimaryKeyDef primaryKey;

    // The identity definition for this table (optional)
    private final IdentityDef identity;

    // All the indexes defined for this table
    private final List<IndexDef> indexes = new ArrayList<>();

    // All the FK constraints used by this table
    private final List<ForeignKeyConstraint> fkConstraints = new ArrayList<>();

    // enable access control
    private final SessionVariableDef accessControlVar;

    private final Tablespace tablespace;

    // The column to use when making this table multi-tenant (if supported by the the target)
    private final String tenantColumnName;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param name
     * @param version
     * @param tenantColumnName
     * @param columns
     * @param pk
     * @param identity
     * @param indexes
     * @param fkConstraints
     * @param accessControlVar
     * @param tablespace
     * @param dependencies
     * @param tags
     * @param privileges
     */
    public Table(String schemaName, String name, int version, String tenantColumnName, Collection<ColumnBase> columns, PrimaryKeyDef pk,
            IdentityDef identity, Collection<IndexDef> indexes, Collection<ForeignKeyConstraint> fkConstraints,
            SessionVariableDef accessControlVar, Tablespace tablespace, List<IDatabaseObject> dependencies, Map<String,String> tags,
            Collection<GroupPrivilege> privileges, List<Migration> migrations) {
        super(schemaName, name, DatabaseObjectType.TABLE, version, migrations);
        this.tenantColumnName = tenantColumnName;
        this.columns.addAll(columns);
        this.primaryKey = pk;
        this.identity = identity;
        this.indexes.addAll(indexes);
        this.fkConstraints.addAll(fkConstraints);
        this.accessControlVar = accessControlVar;
        this.tablespace = tablespace;

        // Adds all dependencies which aren't null.
        // The only circumstances where it is null is when it is self referencial (an FK on itself).
        addDependencies(dependencies.stream().filter(x -> x != null).collect(Collectors.toList()));

        addTags(tags);
        privileges.forEach(p -> p.addToObject(this));
    }

    /**
     * Getter for the primary key definition, or null if there isn't one
     * @return
     */
    public PrimaryKeyDef getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Getter for the identity definition, or null if there isn't one
     * @return
     */
    public IdentityDef getIdentity() {
        return identity;
    }

    /**
     * Getter for the optional tenant id column name
     * @return
     */
    public String getTenantColumnName() {
        return this.tenantColumnName;
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        final String tsName = this.tablespace == null ? null : this.tablespace.getName();
        target.createTable(getSchemaName(), getObjectName(), this.tenantColumnName, this.columns, this.primaryKey, this.identity, tsName);

        // Now add any indexes associated with this table
        for (IndexDef idx: this.indexes) {
            idx.apply(getSchemaName(), getObjectName(), this.tenantColumnName, target);
        }

        // Foreign key constraints
        for (ForeignKeyConstraint fkc: this.fkConstraints) {
            fkc.apply(getSchemaName(), getObjectName(), this.tenantColumnName, target);
        }

        // Apply tenant access control if required
        if (this.accessControlVar != null) {
            // The accessControlVar represents a DB2 session variable. Programs must set this value
            // for the current tenant when executing any SQL (both reads and writes) on
            // tables with this access control enabled
            final String variableName = accessControlVar.getQualifiedName();
            final String tenantPermission = getObjectName() + "_TENANT";
            final String predicate = getQualifiedName() + ".MT_ID = " + variableName;
            target.createOrReplacePermission(getSchemaName(), tenantPermission, getObjectName(), predicate);
            target.activateRowAccessControl(getSchemaName(), getObjectName());
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        if (priorVersion == null || priorVersion == 0) {
            apply(target);
        } else if (this.getVersion() > priorVersion) {
            for (Migration step : migrations) {
                step.migrateFrom(priorVersion).stream().forEachOrdered(target::runStatement);
            }
            // Re-apply tenant access control if required
            if (this.accessControlVar != null) {
                // The accessControlVar represents a DB2 session variable. Programs must set this value
                // for the current tenant when executing any SQL (both reads and writes) on
                // tables with this access control enabled
                final String variableName = accessControlVar.getQualifiedName();
                final String tenantPermission = getObjectName() + "_TENANT";
                final String predicate = getQualifiedName() + ".MT_ID = " + variableName;
                target.createOrReplacePermission(getSchemaName(), tenantPermission, getObjectName(), predicate);
                target.activateRowAccessControl(getSchemaName(), getObjectName());
            }
        }
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        if (this.accessControlVar != null) {
            target.deactivateRowAccessControl(getSchemaName(), getObjectName());

            final String tenantPermission = getObjectName() + "_TENANT";
            target.dropPermission(getSchemaName(), tenantPermission);
        }
        target.dropTable(getSchemaName(), getObjectName());
    }

    /**
     * Create a builder for {@link Table}.
     *
     * @param schemaName
     * @param tableName
     * @return
     */
    public static Builder builder(String schemaName, String tableName) {
        return new Builder(schemaName, tableName);
    }

    /**
     * Builder for table
     */
    public static class Builder extends VersionedSchemaObject {

        // LinkedHashSet so we can remember order
        private LinkedHashSet<ColumnDef> columns = new LinkedHashSet<>();

        // The definition of the primary key, or null if there isn't one
        private PrimaryKeyDef primaryKey;

        // The definition of the identity column for the table, or null if there isn't one
        private IdentityDef identity;

        // All the indexes defined for the table we are building
        private Map<String,IndexDef> indexes = new HashMap<>();

        // All the foreign key constraints defined for this table
        private Map<String, ForeignKeyConstraint> fkConstraints = new HashMap<>();

        // All the unique constraints defined for this table
        private Map<String, UniqueConstraint> uniqueConstraints = new HashMap<>();

        // other dependencies of this table
        private Set<IDatabaseObject> dependencies = new HashSet<>();

        // Is this table multi-tenant when supported?
        private String tenantColumnName;

        // A map of tags
        private Map<String,String> tags = new HashMap<>();

        // The tablespace to use for this table [optional]
        private Tablespace tablespace;

        // The variable to use for access control (when set)
        private SessionVariableDef accessControlVar;

        // Privileges to be granted on this table
        private List<GroupPrivilege> privileges = new ArrayList<>();

        /**
         * Private constructor to force creation through factory method
         * @param schemaName
         * @param tableName
         */
        private Builder(String schemaName, String tableName) {
            super(schemaName, tableName);
        }

        /**
         * Set the version
         * @param v
         * @return
         */
        public Builder setVersion(int v) {
            setVersionValue(v);
            return this;
        }

        /**
         * Setter for the tablespace
         * @param ts
         * @return
         */
        public Builder setTablespace(Tablespace ts) {
            this.tablespace = ts;
            return this;
        }

        public Builder addIntColumn(String columnName, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.INT);
            columns.add(cd);
            return this;
        }

        public Builder addSmallIntColumn(String columnName, Integer defaultValue, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);

            if (defaultValue != null) {
                cd.setDefaultVal(Integer.toString(defaultValue));
            }

            cd.setColumnType(ColumnType.SMALLINT);
            columns.add(cd);
            return this;
        }

        public Builder addBigIntColumn(String columnName, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.BIGINT);
            columns.add(cd);
            return this;
        }

        public Builder addDoubleColumn(String columnName, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.DOUBLE);
            columns.add(cd);
            return this;
        }

        public Builder addTimestampColumn(String columnName, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.TIMESTAMP);
            columns.add(cd);
            return this;
        }

        public Builder addTimestampColumn(String columnName, int numberOfFractionalSecondDigits, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.TIMESTAMP);
            cd.setPrecision(numberOfFractionalSecondDigits);
            columns.add(cd);
            return this;
        }

        public Builder addVarcharColumn(String columnName, int size, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.VARCHAR);
            cd.setSize(size);
            columns.add(cd);
            return this;
        }

        /**
         * Add a VARBINARY(nn) column
         * @param columnName
         * @param size
         * @param nullable
         * @return
         */
        public Builder addVarbinaryColumn(String columnName, int size, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.VARBINARY);
            cd.setSize(size);
            columns.add(cd);
            return this;
        }

        /**
         * Add char (fixed-width) column
         * @param columnName
         * @param size
         * @param nullable
         * @return
         */
        public Builder addCharColumn(String columnName, int size, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.CHAR);
            cd.setSize(size);
            columns.add(cd);
            return this;
        }

        public Builder addBlobColumn(String columnName, long size, int inlineSize, boolean nullable) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.BLOB);
            cd.setSize(size);
            cd.setInlineSize(inlineSize);
            columns.add(cd);
            return this;
        }

        /**
         * @param columnName
         * @param nullable
         * @param defaultVal this value is auto-quoted; do not pass the single-quote (') within the string value
         * @return
         */
        public Builder addClobColumn(String columnName, boolean nullable, String defaultVal) {
            ColumnDef cd = new ColumnDef(columnName);
            if (columns.contains(cd)) {
                throw new IllegalArgumentException("Duplicate column: " + columnName);
            }

            cd.setNullable(nullable);
            cd.setColumnType(ColumnType.CLOB);
            cd.setDefaultVal("'" + defaultVal + "'");
            columns.add(cd);
            return this;
        }

        /**
         * Set one of the columns to be the identity column for the table
         * @param constraintName
         * @param columns
         * @return
         */
        public Builder setIdentityColumn(String columnName, Generated generated) {
            this.identity = new IdentityDef(columnName, generated);
            return this;
        }

        /**
         * Add a primary key constraint to the table
         * @param constraintName
         * @param columns
         * @return
         */
        public Builder addPrimaryKey(String constraintName, String... columns) {
            if (this.primaryKey != null) {
                throw new IllegalStateException("Duplicate primary key constraint: " + constraintName);
            }
            checkColumns(columns);
            this.primaryKey = new PrimaryKeyDef(constraintName, Arrays.asList(columns));
            return this;
        }

        /**
         * Add an index to the table using the list of column names
         * @param indexName
         * @param columns
         * @return
         */
        public Builder addIndex(String indexName, String...columns) {
            if (this.indexes.containsKey(indexName)) {
                throw new IllegalStateException("Duplicate index name: " + indexName);
            }

            // Make sure all the given column names are valid for this table
            checkColumns(columns);
            indexes.put(indexName, new IndexDef(indexName, Arrays.asList(columns), false));
            return this;
        }

        /**
         * Add a unique index to the table using the list of column names
         * @param indexName
         * @param columns
         * @return
         */
        public Builder addUniqueIndex(String indexName, String...columns) {
            if (this.indexes.containsKey(indexName)) {
                throw new IllegalStateException("Duplicate index name: " + indexName);
            }
            checkColumns(columns);
            indexes.put(indexName, new IndexDef(indexName, Arrays.asList(columns), true));
            return this;
        }

        /**
         * Add a unique index with include columns
         * @param indexName
         * @param indexColumns
         * @param includeColumns
         * @return
         */
        public Builder addUniqueIndex(String indexName, Collection<String> indexColumns, Collection<String> includeColumns) {
            if (this.indexes.containsKey(indexName)) {
                throw new IllegalStateException("Duplicate index name: " + indexName);
            }
            checkColumns(indexColumns);
            checkColumns(includeColumns);
            indexes.put(indexName, new IndexDef(indexName, indexColumns, includeColumns));
            return this;
        }

        /**
         * Add a unique constraint to the table/column
         * @param constraintName
         * @param columnName - at least one column
         * @return
         */
        public Builder addUniqueConstraint(String constraintName, String... columnName) {
            this.uniqueConstraints.put(constraintName, new UniqueConstraint(constraintName, columnName));
            return this;
        }

        /**
         * Add a foreign key constraint pointing to the target table (with enforcement).
         * The list of columns is expected to match the primary key definition on the target.
         *
         * @param constraintName
         * @param targetSchema
         * @param targetTable
         * @param columns
         * @return
         */
        public Builder addForeignKeyConstraint(String constraintName, String targetSchema, String targetTable, String... columns) {
            return addForeignKeyConstraint(constraintName, true, targetSchema, targetTable, columns);
        }

        /**
         * Add a foreign key constraint pointing to the target table. The list of columns
         * is expected to match the primary key definition on the target
         *
         * @param constraintName
         * @param enforced
         * @param targetSchema
         * @param targetTable
         * @param columns
         * @return
         */
        public Builder addForeignKeyConstraint(String constraintName, boolean enforced, String targetSchema, String targetTable, String... columns) {
            this.fkConstraints.put(constraintName, new ForeignKeyConstraint(constraintName, enforced, false, targetSchema, targetTable, null, columns));
            return this;
        }

        /**
         * Adds a foreign key constraint relationship on itself. This is intentionally created as a separate method, so there are no mistakes.
         *
         * @param constraintName
         * @param targetSchema
         * @param targetTable
         * @param targetColumnName
         * @param columns
         * @return
         */
        public Builder addForeignKeyConstraintSelf(String constraintName, String targetSchema, String targetTable, String targetColumnName, String... columns) {
            this.fkConstraints.put(constraintName, new ForeignKeyConstraint(constraintName, true, true, targetSchema, targetTable, targetColumnName, columns));
            return this;
        }

        /**
         * Adds a foreign key constraint relationship on itself. This is intentionally created as a separate method, so there are no mistakes.
         *
         * @param constraintName
         * @param targetSchema
         * @param targetTable
         * @param targetColumnName
         * @param columns
         * @return
         */
        public Builder addForeignKeyConstraintAltTarget(String constraintName, String targetSchema, String targetTable, String targetColumnName, String... columns) {
            this.fkConstraints.put(constraintName, new ForeignKeyConstraint(constraintName, true, false, targetSchema, targetTable, targetColumnName, columns));
            return this;
        }

        /**
         * Check each of the columns in the given array are valid column names
         * @param columns
         */
        protected void checkColumns(String[] columns) {
            checkColumns(Arrays.asList(columns));
        }

        /**
         * Check each of the columns in the given array are valid column names
         * @param columns
         */
        protected void checkColumns(Collection<String> columns) {
            for (String columnName: columns) {
                ColumnDef cd = new ColumnDef(columnName);
                if (!this.columns.contains(cd)) {
                    throw new IllegalArgumentException("Invalid column name: " + columnName);
                }
            }
        }

        /**
         * Build the immutable table object based on the current configuration
         * @param dataModel
         * @return
         */
        public Table build(IDataModel dataModel) {

            // Check the FK references are valid
            List<IDatabaseObject> allDependencies = new ArrayList<>();
            allDependencies.addAll(this.dependencies);
            for (ForeignKeyConstraint c: this.fkConstraints.values()) {

                Table target = dataModel.findTable(c.getTargetSchema(), c.getTargetTable());
                if (target == null && !c.isSelf()) {
                    String targetName = DataDefinitionUtil.getQualifiedName(c.getTargetSchema(), c.getTargetTable());
                    throw new IllegalArgumentException("Invalid foreign key constraint " + c.getConstraintName() + ": target table does not exist: " + targetName);
                }
                allDependencies.add(target);
            }

            if (this.tablespace != null) {
                allDependencies.add(tablespace);
            }

            // Our schema objects are immutable by design, so all initialization takes place
            // through the constructor
            return new Table(getSchemaName(), getObjectName(), this.version, this.tenantColumnName, buildColumns(), this.primaryKey, this.identity, this.indexes.values(),
                    this.fkConstraints.values(), this.accessControlVar, this.tablespace, allDependencies, tags, privileges, migrations);

        }

        /**
         * Create the columns for the table based on the definitions that have been added
         * @return
         */
        protected List<ColumnBase> buildColumns() {
            if (this.columns.isEmpty()) {
                throw new IllegalStateException("no columns for table: " + getQualifiedName());
            }
            List<ColumnBase> result = new ArrayList<>();

            for (ColumnDef cd: this.columns) {
                ColumnBase column;
                switch (cd.getColumnType()) {
                case BIGINT:
                    column = new BigIntColumn(cd.getName(), cd.isNullable());
                    break;
                case INT:
                    column = new IntColumn(cd.getName(), cd.isNullable());
                    break;
                case SMALLINT:
                    column = new SmallIntColumn(cd.getName(), cd.isNullable(), cd.getDefaultVal());
                    break;
                case DOUBLE:
                    column = new DoubleColumn(cd.getName(), cd.isNullable());
                    break;
                case TIMESTAMP:
                    column = new TimestampColumn(cd.getName(), cd.isNullable(), cd.getPrecision());
                    break;
                case VARCHAR:
                    if (cd.getSize() > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Invalid size for column: " + cd.getName());
                    }
                    column = new VarcharColumn(cd.getName(), (int)cd.getSize(), cd.isNullable());
                    break;
                case VARBINARY:
                    if (cd.getSize() > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Invalid size for column: " + cd.getName());
                    }
                    column = new VarbinaryColumn(cd.getName(), (int)cd.getSize(), cd.isNullable());
                    break;
                case CHAR:
                    if (cd.getSize() > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Invalid size for column: " + cd.getName());
                    }
                    column = new CharColumn(cd.getName(), (int)cd.getSize(), cd.isNullable());
                    break;
                case BLOB:
                    column = new BlobColumn(cd.getName(), cd.getSize(), cd.getInlineSize(), cd.isNullable());
                    break;
                case CLOB:
                    column = new ClobColumn(cd.getName(), cd.isNullable(), cd.getDefaultVal());
                    break;
                default:
                    throw new IllegalStateException("Unsupported column type: " + cd.getColumnType().name());
                }
                result.add(column);
            }

            return result;
        }

        /**
         * Switch on access control for this table
         */
        public Builder enableAccessControl(SessionVariableDef var) {
            this.accessControlVar = var;

            // Add the session variable as a dependency for this table
            this.dependencies.add(var);

            return this;
        }

        /**
         * @param tagName
         * @param tagValue
         * @return
         */
        public Builder addTag(String tagName, String tagValue) {
            this.tags.put(tagName, tagValue);
            return this;
        }

        public Builder addPrivilege(String groupName, Privilege p) {
            this.privileges.add(new GroupPrivilege(groupName, p));
            return this;
        }

        /**
         * Add the collection of group privileges to this table
         * @param gps
         * @return
         */
        public Builder addPrivileges(Collection<GroupPrivilege> gps) {
            this.privileges.addAll(gps);
            return this;
        }

        /**
         * Setter to configure this table for multitenancy. Multitenancy support depends on the target
         * ...which in this case means DB2 supports it (using partitioning) but Derby does not...so for
         * Derby, we don't create the extra column or FK relationships back to the TENANTS table.
         * @return
         */
        public Builder setTenantColumnName(String name) {
            this.tenantColumnName = name;
            return this;
        }

        @Override
        public Builder addMigration(Migration... migration) {
            super.addMigration(migration);
            return this;
        }
    }

    /**
     * return true if the table already exists in the target
     * @param target
     * @return
     */
    public boolean exists(IDatabaseAdapter target) {
        return target.doesTableExist(getSchemaName(), getObjectName());
    }

}
