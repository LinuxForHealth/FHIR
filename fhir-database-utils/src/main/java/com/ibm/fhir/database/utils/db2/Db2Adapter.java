/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DistributionContext;
import com.ibm.fhir.database.utils.api.DuplicateNameException;
import com.ibm.fhir.database.utils.api.DuplicateSchemaException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.PartitionInfo;
import com.ibm.fhir.database.utils.api.UndefinedNameException;
import com.ibm.fhir.database.utils.common.CommonDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.DropColumn;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.IntColumn;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.With;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;

/**
 * Implementation of our database adapter which provides implementation of control
 * functions specific to DB2 for things like schema and partition management
 */
public class Db2Adapter extends CommonDatabaseAdapter {
    private static final Logger logger = Logger.getLogger(Db2Adapter.class.getName());

    private static final String DROP_SPECIFIC = "SELECT SPECIFICNAME FROM SYSCAT.ROUTINES WHERE ROUTINESCHEMA = ? AND ROUTINENAME = ?";

    /**
     * Public constructor
     * @param tgt the target database we want to manage
     */
    public Db2Adapter(IDatabaseTarget tgt) {
        super(tgt, new Db2Translator());
    }

    public Db2Adapter(IConnectionProvider cp) {
        super(cp, new Db2Translator());
    }

    public Db2Adapter() {
        super();
    }

    @Override
    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
            IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
            DistributionContext distributionContext) {

        // With DB2 we can implement support for multi-tenancy, which we do by injecting a MT_ID column
        // to the definition and partitioning on that column
        List<ColumnBase> cols = new ArrayList<>(columns.size()+1);
        if (tenantColumnName != null) {
            IntColumn tenantIdCol = new IntColumn(tenantColumnName, false);
            cols.add(tenantIdCol);

            // We also need to tweak the primary key definition by making the given tenant column name
            // the first member
            if (primaryKey != null) {
                List<String> pkCols = new ArrayList<>();
                pkCols.add(tenantColumnName);
                pkCols.addAll(primaryKey.getColumns());

                // substitute in the new PK definition
                primaryKey = new PrimaryKeyDef(primaryKey.getConstraintName(), pkCols);
            }
        }

        // Now append all the actual columns we want in the table
        cols.addAll(columns);

        String ddl = buildCreateTableStatement(schemaName, name, cols, primaryKey, identity, tablespaceName, With.EMPTY, checkConstraints);

        // Our multi-tenant tables are range-partitioned as part of our data isolation strategy
        // We reserve partition 0. Real tenant partitions start at 1...
        // PARTITION BY RANGE (mt_id) (STARTING 0 INCLUSIVE ENDING 0 INCLUSIVE)
        if (tenantColumnName != null) {
            ddl = ddl + " PARTITION BY RANGE (" + tenantColumnName + ") "
                    + "(STARTING 0 INCLUSIVE "
                    + "   ENDING 0 INCLUSIVE )";

        }

        runStatement(ddl);
    }

    @Override
    public void createIntVariable(String schemaName, String variableName) {
        final String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, variableName);
        String ddl = "CREATE VARIABLE " + qualifiedName + " INT DEFAULT NULL";
        runStatement(ddl);
    }

    @Override
    public void createOrReplacePermission(String schemaName, String permissionName, String tableName, String predicate) {
        final String qualifiedPermissionName = DataDefinitionUtil.getQualifiedName(schemaName, permissionName);
        final String qualifiedTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        DataDefinitionUtil.assertSecure(predicate);

        final String ddl = ""
                + "CREATE OR REPLACE PERMISSION " + qualifiedPermissionName
                + " ON " + qualifiedTableName
                + " FOR ROWS WHERE " + predicate
                + " ENFORCED FOR ALL ACCESS ENABLE ";
        runStatement(ddl);
    }

    @Override
    public void activateRowAccessControl(String schemaName, String tableName) {
        final String qualifiedTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "ALTER TABLE " + qualifiedTableName + " ACTIVATE ROW ACCESS CONTROL";
        runStatement(ddl);
    }

    @Override
    public void setIntVariable(String schemaName, String variableName, int value) {
        final String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, variableName);
        final String sql = "SET " + qualifiedName + " = ?";
        target.runStatementWithInt(getTranslator(), sql, value);
    }

    @Override
    public void createTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId, int extentSizeKB) {
        DataDefinitionUtil.assertValidName(schemaName);

        // Make sure each of our partitioned tables includes partitions up to and including
        // the maxTenantId value. Get the current values, and then fill in the gaps.
        Map<String, PartitionInfo> partitionInfoMap = new HashMap<>();

        // Make sure there's a tablespace available for this tenant before we
        // try to create the actual partitions
        final String tablespaceName = "TS_TENANT" + newTenantId;
        try (ITransaction tx = TransactionFactory.openTransaction(connectionProvider)) {
            try {
                // Get the current partition info
                loadPartitionInfoMap(partitionInfoMap, schemaName);

                logger.info("Creating tablespace: " + tablespaceName);
                Db2CreateTablespace createTablespace = new Db2CreateTablespace(tablespaceName, extentSizeKB);
                runStatement(createTablespace);
            }
            catch (RuntimeException x) {
                logger.severe("Create tablespace failed for " + tablespaceName + ": " + x.getMessage());
                tx.setRollbackOnly();
                throw x;
            }
        }

        addNewTenantPartitions(tables, partitionInfoMap, newTenantId, tablespaceName);
    }

    @Override
    public void addNewTenantPartitions(Collection<Table> tables, String schemaName, int newTenantId) {
        DataDefinitionUtil.assertValidName(schemaName);

        // Make sure each of our partitioned tables includes partitions up to and including
        // the maxTenantId value. Get the current values, and then fill in the gaps.
        Map<String, PartitionInfo> partitionInfoMap = new HashMap<>();
        try (ITransaction tx = TransactionFactory.openTransaction(connectionProvider)) {
            try {
                // Get the current partition info
                loadPartitionInfoMap(partitionInfoMap, schemaName);
            }
            catch (RuntimeException x) {
                logger.severe("Get partition info failed for schema " + schemaName + ": " + x.getMessage());
                tx.setRollbackOnly();
                throw x;
            }
        }

        final String tablespaceName = "TS_TENANT" + newTenantId;
        addNewTenantPartitions(tables, partitionInfoMap, newTenantId, tablespaceName);
    }

    /**
     * Add a new tenant partition to each of the tables in the collection. Idempotent, so can
     * be run to add partitions for existing tenants to new tables
     * @param tables
     * @param partitionInfoMap
     * @param newTenantId
     * @param tablespaceName
     */
    public void addNewTenantPartitions(Collection<Table> tables, Map<String, PartitionInfo> partitionInfoMap, int newTenantId, String tablespaceName) {
        // Thread pool for parallelizing requests
        int poolSize = connectionProvider.getPoolSize();
        if (poolSize == -1) {
            // Default Value - 40
            poolSize = 40;
        }
        final ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        final AtomicInteger taskCount = new AtomicInteger();
        for (Table t: tables) {
            String qualifiedName = t.getQualifiedName();
            PartitionInfo pi = partitionInfoMap.get(t.getObjectName());
            if (pi == null) {
                // We should only be dealing with partitioned tables at this stage, so this
                // is a fatal error
                throw new DataAccessException("No partition information found for table: " + qualifiedName);
            } else {
                // Submit to the pool for processing
                taskCount.incrementAndGet();
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            createTenantPartitionsThr(t, pi, newTenantId, tablespaceName);
                        } catch (Throwable x) {
                            logger.log(Level.SEVERE, "tenant creation failed: " + t.getName(), x);
                        } finally {
                            taskCount.decrementAndGet();
                        }
                    }
                });
            }
        }

        // Wait for all the tasks to complete
        pool.shutdown();
        try {
            while (!pool.isTerminated()) {
                logger.info("Waiting for partitioning tasks to complete: " + taskCount.get());
                pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException x) {
            // Not cool. This means that only some of the tables will have the partition assigned
            throw new DataAccessException("Tenant partition creation did not complete");
        }

    }

    /**
     * Ensure that the given table has all the partitions necessary up to and
     * including the max tenant id
     *
     * @param t
     * @param pi
     * @param newTenantId
     * @param tablespaceName
     */
    public void createTenantPartitionsThr(Table t, PartitionInfo pi, int newTenantId, String tablespaceName) {
        // Each thread needs to manage its own transaction
        try (ITransaction tx = TransactionFactory.openTransaction(connectionProvider)) {
            try {
                if (pi.getHighValue() == null || pi.getHighValue().isEmpty()) {
                    throw new IllegalArgumentException("Missing upper partition information");
                }

                if (newTenantId > Integer.parseInt(pi.getHighValue())) {
                    logger.info("Adding tenant partition: TENANT" + newTenantId + " to " + t.getName());
                    Db2AddTablePartition cmd = new Db2AddTablePartition(t.getSchemaName(), t.getObjectName(), newTenantId, tablespaceName);
                    runStatement(cmd);
                    logger.info("Added tenant partition: TENANT" + newTenantId + " to " + t.getName());
                } else {
                    // Not an error, because we want to make this idempotent
                    logger.info("Partition already exists: TENANT" + newTenantId + " for " + t.getName());
                }
            } catch (RuntimeException x) {
                logger.severe("Rolling back transaction after tenant creation failed for table " + t.getName());
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Read partition info from the database catalog.
     *
     * @param partitionInfoMap
     * @param tableSchema
     */
    protected void loadPartitionInfoMap(Map<String, PartitionInfo> partitionInfoMap, String tableSchema) {
        // To improve testability, we want to avoid any JDBC gunk at this level,
        // but luckily a bit of functional programming comes to the rescue
        Db2GetPartitionInfo statement = new Db2GetPartitionInfo("SYSCAT", tableSchema, (PartitionInfo c) -> partitionInfoMap.put(c.getTableName(), c));
        runStatement(statement);
    }

    @Override
    public void deactivateRowAccessControl(String schemaName, String tableName) {
        final String qualifiedTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "ALTER TABLE " + qualifiedTableName + " DEACTIVATE ROW ACCESS CONTROL";

        try {
            runStatement(ddl);
        }
        catch (UndefinedNameException x) {
            logger.warning(ddl + "; Table not found");
        }
    }

    @Override
    public void createRowType(String schemaName, String typeName, List<ColumnBase> columns) {
        //  CREATE OR REPLACE TYPE <schema>.t_str_values AS ROW (parameter_name_id INTEGER, str_value VARCHAR(511 OCTETS), str_value_lcase   VARCHAR(511 OCTETS))
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE OR REPLACE TYPE ");
        ddl.append(DataDefinitionUtil.getQualifiedName(schemaName, typeName));
        ddl.append(" AS ROW (");
        ddl.append(DataDefinitionUtil.columnSpecList(this, columns));
        ddl.append(")");

        runStatement(ddl.toString());
    }

    @Override
    public void createArrType(String schemaName, String typeName, String valueType, int arraySize) {
        //  CREATE OR REPLACE TYPE <schema>.t_str_values_arr AS <schema>.t_str_values ARRAY[256]
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE OR REPLACE TYPE ");
        ddl.append(DataDefinitionUtil.getQualifiedName(schemaName, typeName));
        ddl.append(" AS ");
        ddl.append(DataDefinitionUtil.getQualifiedName(schemaName, valueType));
        ddl.append(" ARRAY[");
        ddl.append(arraySize);
        ddl.append("]");

        runStatement(ddl.toString());

    }

    @Override
    public void dropType(String schemaName, String typeName) {
        final String ddl = "DROP TYPE " + DataDefinitionUtil.getQualifiedName(schemaName, typeName);

        try {
            runStatement(ddl);
        }
        catch (UndefinedNameException x) {
            logger.warning(ddl + "; type not found");
        }
    }

    @Override
    public void createOrReplaceProcedure(String schemaName, String procedureName, Supplier<String> supplier) {
        final String objectName = DataDefinitionUtil.getQualifiedName(schemaName, procedureName);
        logger.info("Create or replace procedure " + objectName);

        // Build the create procedure DDL and apply it
        final StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE OR REPLACE PROCEDURE ");
        ddl.append(objectName);
        ddl.append(System.lineSeparator());
        ddl.append(supplier.get());

        final String ddlString = ddl.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(ddlString);
        }

        runStatement(ddlString);
    }

    @Override
    public void dropProcedure(String schemaName, String procedureName) {
        List<String> existingStoredProcedures = new ArrayList<>();
        if (connectionProvider != null) {
            try (Connection c = connectionProvider.getConnection()) {
                try (PreparedStatement p = c.prepareStatement(DROP_SPECIFIC)) {
                    p.setString(1, schemaName);
                    p.setString(2, procedureName);
                    if (p.execute()) {
                        // Closes with PreparedStatement
                        ResultSet rs = p.getResultSet();
                        while (rs.next()) {
                            existingStoredProcedures.add(rs.getString(1));
                        }
                    }
                }
            } catch (SQLException x) {
                throw getTranslator().translate(x);
            }
        }

        // As the procedure signatures are mutated, we don't want to be in the situation where the signature change, and
        // we can't drop.
        for (String existingStoredProcedure : existingStoredProcedures) {
            final String pname = DataDefinitionUtil.getQualifiedName(schemaName, existingStoredProcedure);
            final String ddl = "DROP SPECIFIC PROCEDURE " + pname;
            try {
                runStatement(ddl);
            } catch (UndefinedNameException x) {
                logger.warning(ddl + "; PROCEDURE not found");
            }
        }
    }

    @Override
    public void createTablespace(String tablespaceName) {
        DataDefinitionUtil.assertValidName(tablespaceName);
        final String ddl = "CREATE TABLESPACE " + tablespaceName + " MANAGED BY AUTOMATIC STORAGE";
        runStatement(ddl);
    }

    @Override
    public void createTablespace(String tablespaceName, int extentSizeKB) {
        Db2CreateTablespace createTablespace = new Db2CreateTablespace(tablespaceName, extentSizeKB);
        runStatement(createTablespace);
    }

    @Override
    public void dropTablespace(String tablespaceName) {
        DataDefinitionUtil.assertValidName(tablespaceName);

        // Should be idempotent, so won't error out if the tablespace has already been dropped
        Db2DropTablespace dts = new Db2DropTablespace(tablespaceName);
        runStatement(dts);
    }

    @Override
    public void detachPartition(String schemaName, String tableName, String partitionName, String intoTableName) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String detachedName = DataDefinitionUtil.getQualifiedName(schemaName, intoTableName);
        final String ddl = "ALTER TABLE " + qname + " DETACH PARTITION " + partitionName + " INTO " + detachedName;

        try {
            runStatement(ddl);
        } catch (DataAccessException x) {
            // Suppress the error, in case this is an older version and we have a new table
            logger.warning("Detach partition skipped for '" + qname + "/" + partitionName + "'. Reason: " + x.getMessage());
        }
    }

    @Override
    public void removeTenantPartitions(Collection<Table> tables, String schemaName, int tenantId) {

        // Identify all the partitioned tables contained within schemaName
        Map<String, PartitionInfo> partitionInfoMap = new HashMap<>();
        loadPartitionInfoMap(partitionInfoMap, schemaName);

        for (Table t: tables) {
            PartitionInfo pi = partitionInfoMap.get(t.getObjectName());
            if (pi == null) {
                // We should only be dealing with partitioned tables at this stage, so this
                // is a fatal error
                String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, t.getObjectName());
                throw new DataAccessException("No partition information found for table: " + qualifiedName);
            } else {
                final String partitionName = "TENANT" + tenantId;
                final String targetTableName = getDetachedPartitionTableName(t, tenantId);
                detachPartition(schemaName, t.getObjectName(), partitionName, targetTableName);
            }
        }
    }

    @Override
    public void dropDetachedPartitions(Collection<Table> tables, String schemaName, int tenantId) {

        // Only process tables which are partitioned
        Map<String, PartitionInfo> partitionInfoMap = new HashMap<>();
        loadPartitionInfoMap(partitionInfoMap, schemaName);

        for (Table t : tables) {
            PartitionInfo pi = partitionInfoMap.get(t.getObjectName());
            if (pi == null) {
                // We should only be dealing with partitioned tables at this stage, so this
                // is a fatal error
                String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, t.getObjectName());
                throw new DataAccessException("No partition information found for table: " + qualifiedName);
            } else {
                // drop the table which now represents the detached partition
                final String detachedPartitionTableName = getDetachedPartitionTableName(t, tenantId);
                try {
                    logger.info("Dropping detached partition (table): '" + detachedPartitionTableName + "'");
                    dropTable(schemaName, detachedPartitionTableName);
                } catch (Exception x) {
                    // we want this to be idempotent, so we suppress propagation of any error
                    logger.warning("Drop failed for `" + detachedPartitionTableName + "` - " + x.getMessage());

                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, detachedPartitionTableName, x);
                    }
                }
            }
        }
    }

    /**
     * Get the name of the table created when the given tenant's partition is
     * dropped (to deprovision a tenant). This is just the table name prefixed with
     * DRP_n_ where n is the tenantId.
     *
     * @param tenantId
     * @return
     */
    private String getDetachedPartitionTableName(Table t, int tenantId) {
        return "DRP_" + tenantId + "_" + t.getObjectName();
    }

    @Override
    public boolean doesTableExist(String schemaName, String tableName) {
        // Check the DB2 catalog to see if the table exists
        Db2GetTableInfo dao = new Db2GetTableInfo(schemaName, tableName);
        Db2TableInfo info = runStatement(dao);
        return info != null;
    }

    @Override
    public String varbinaryClause(int size) {
        return "VARBINARY(" + size + ")";
    }

    @Override
    public String blobClause(long size, int inlineSize) {
        return "BLOB(" + size + ") INLINE LENGTH " + inlineSize;
    }

    @Override
    public String varcharClause(int size) {
        return "VARCHAR(" + size + " OCTETS)";
    }

    @Override
    public boolean checkCompatibility(String adminSchema) {
        // As long as we don't get an exception, we should be considered compatible
        Db2CheckCompatibility checker = new Db2CheckCompatibility(adminSchema);
        runStatement(checker);
        return true;
    }

    @Override
    public void runStatement(IDatabaseStatement stmt) {
        super.runStatement(stmt);
        if (stmt instanceof DropColumn) {
            // A table reorg is typically needed after dropping a column in Db2; the runstats before and after it are "best practice"
            // per https://dba.stackexchange.com/questions/30231/do-i-need-to-runstats-after-a-reorg-in-db2/30233#30233

            String qname = ((DropColumn) stmt).getSchemaName() + "." + ((DropColumn) stmt).getTableName();

            String reorgCommand = "REORG TABLE " + qname;
            super.runStatement(new Db2AdminCommand(reorgCommand));

            Db2AdminCommand runstats = new Db2AdminCommand("RUNSTATS ON TABLE " + qname + " WITH DISTRIBUTION AND DETAILED INDEXES ALL");
            super.runStatement(runstats);
        }
    }

    @Override
    public void createSchema(String schemaName) {
        try {
            String ddl = "CREATE SCHEMA " + schemaName;
            runStatement(ddl);
            logger.log(Level.INFO, "The schema '" + schemaName + "' is created");
        } catch (DuplicateNameException | DuplicateSchemaException e) {
            logger.log(Level.WARNING, "The schema '" + schemaName + "' already exists; proceed with caution.");
        }
    }

    @Override
    public boolean useSessionVariable() {
        return true;
    }

    @Override
    public void dropTenantTablespace(int tenantId) {
        final String tablespaceName = "TS_TENANT" + tenantId;
        dropTablespace(tablespaceName);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseAdapter#disableForeignKey(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void disableForeignKey(String schemaName, String tableName, String constraintName) {
        // ALTER TABLE foo ALTER FOREIGN KEY fk NOT ENFORCED
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "ALTER TABLE " + qname + " ALTER FOREIGN KEY " + constraintName + " NOT ENFORCED";
        runStatement(ddl);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseAdapter#enableForeignKey(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void enableForeignKey(String schemaName, String tableName, String constraintName) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "ALTER TABLE " + qname + " ALTER FOREIGN KEY " + constraintName + " ENFORCED";
        runStatement(ddl);
    }

    @Override
    public boolean doesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName) {
        Db2DoesForeignKeyConstraintExist test = new Db2DoesForeignKeyConstraintExist(schemaName, tableName, constraintName);
        // runStatement may return null in some unit-tests, so we need to protect against that
        Boolean val = runStatement(test);
        return val != null && val.booleanValue();
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseAdapter#setIntegrityOff(java.lang.String, java.lang.String)
     */
    @Override
    public void setIntegrityOff(String schemaName, String tableName) {
        // SET INTEGRITY FOR child OFF;
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "SET INTEGRITY FOR " + qname + " OFF";

        // so important, we log it
        logger.info(ddl);

        runStatement(ddl);

    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseAdapter#setIntegrityUnchecked(java.lang.String, java.lang.String)
     */
    @Override
    public void setIntegrityUnchecked(String schemaName, String tableName) {
        // SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "SET INTEGRITY FOR " + qname + " ALL IMMEDIATE UNCHECKED";

        // so important, we log it
        logger.info(ddl);

        runStatement(ddl);
    }

    @Override
    public void reorgTable(String schemaName, String tableName) {
        Db2Reorg cmd = new Db2Reorg(schemaName, tableName);
        runStatement(cmd);
    }

    @Override
    public List<SchemaInfoObject> listSchemaObjects(String schemaName) {
        List<SchemaInfoObject> result = new ArrayList<>();
        Db2ListTablesForSchema listTables = new Db2ListTablesForSchema(schemaName);
        result.addAll(runStatement(listTables));
        
        Db2ListViewsForSchema listViews = new Db2ListViewsForSchema(schemaName);
        result.addAll(runStatement(listViews));
        
        Db2ListSequencesForSchema listSequences = new Db2ListSequencesForSchema(schemaName);
        result.addAll(runStatement(listSequences));
        
        return result;
    }
}