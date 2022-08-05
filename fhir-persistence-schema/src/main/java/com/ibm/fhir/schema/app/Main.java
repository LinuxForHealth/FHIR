/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import static com.ibm.fhir.schema.app.menu.Menu.CHECK_COMPATIBILITY;
import static com.ibm.fhir.schema.app.menu.Menu.CONFIRM_DROP;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMAS;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_BATCH;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_FHIR;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_OAUTH;
import static com.ibm.fhir.schema.app.menu.Menu.DB_TYPE;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_ADMIN;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_BATCH;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_FHIR;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_OAUTH;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SPLIT_TRANSACTION;
import static com.ibm.fhir.schema.app.menu.Menu.FORCE;
import static com.ibm.fhir.schema.app.menu.Menu.FORCE_UNUSED_TABLE_REMOVAL;
import static com.ibm.fhir.schema.app.menu.Menu.GRANT_READ_TO;
import static com.ibm.fhir.schema.app.menu.Menu.GRANT_TO;
import static com.ibm.fhir.schema.app.menu.Menu.HELP;
import static com.ibm.fhir.schema.app.menu.Menu.POOL_SIZE;
import static com.ibm.fhir.schema.app.menu.Menu.PROP;
import static com.ibm.fhir.schema.app.menu.Menu.PROP_FILE;
import static com.ibm.fhir.schema.app.menu.Menu.SCHEMA_NAME;
import static com.ibm.fhir.schema.app.menu.Menu.SCHEMA_TYPE;
import static com.ibm.fhir.schema.app.menu.Menu.SHOW_DB_SIZE;
import static com.ibm.fhir.schema.app.menu.Menu.SHOW_DB_SIZE_DETAIL;
import static com.ibm.fhir.schema.app.menu.Menu.TARGET;
import static com.ibm.fhir.schema.app.menu.Menu.THREAD_POOL_SIZE;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_PROC;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_SCHEMA;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_SCHEMA_BATCH;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_SCHEMA_FHIR;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_SCHEMA_OAUTH;
import static com.ibm.fhir.schema.app.menu.Menu.UPDATE_VACUUM;
import static com.ibm.fhir.schema.app.menu.Menu.VACUUM_COST_LIMIT;
import static com.ibm.fhir.schema.app.menu.Menu.VACUUM_SCALE_FACTOR;
import static com.ibm.fhir.schema.app.menu.Menu.VACUUM_TABLE_NAME;
import static com.ibm.fhir.schema.app.menu.Menu.VACUUM_TRESHOLD;
import static com.ibm.fhir.schema.app.util.CommonUtil.configureLogger;
import static com.ibm.fhir.schema.app.util.CommonUtil.getDbAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.getPropertyAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.getSchemaAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.loadDriver;
import static com.ibm.fhir.schema.app.util.CommonUtil.logClasspath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.core.util.handler.HostnameHandler;
import com.ibm.fhir.database.utils.api.ConcurrentUpdateException;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DatabaseNotReadyException;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ILeaseManagerConfig;
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.api.TableSpaceRemovalException;
import com.ibm.fhir.database.utils.api.UniqueConstraintViolationException;
import com.ibm.fhir.database.utils.citus.CitusTranslator;
import com.ibm.fhir.database.utils.citus.ConfigureConnectionDAO;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.postgres.GatherTablesDataModelVisitor;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.database.utils.postgres.PostgresVacuumSettingDAO;
import com.ibm.fhir.database.utils.schema.LeaseManager;
import com.ibm.fhir.database.utils.schema.LeaseManagerConfig;
import com.ibm.fhir.database.utils.schema.SchemaVersionsManager;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.database.utils.version.CreateControl;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.CreateWholeSchemaVersion;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.schema.app.menu.Menu;
import com.ibm.fhir.schema.control.AddForeignKey;
import com.ibm.fhir.schema.control.BackfillResourceChangeLog;
import com.ibm.fhir.schema.control.DropForeignKey;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.FhirSchemaVersion;
import com.ibm.fhir.schema.control.GetLogicalResourceNeedsV0014Migration;
import com.ibm.fhir.schema.control.GetLogicalResourceNeedsV0027Migration;
import com.ibm.fhir.schema.control.GetResourceChangeLogEmpty;
import com.ibm.fhir.schema.control.GetResourceTypeList;
import com.ibm.fhir.schema.control.GetXXLogicalResourceNeedsMigration;
import com.ibm.fhir.schema.control.InitializeLogicalResourceDenorms;
import com.ibm.fhir.schema.control.JavaBatchSchemaGenerator;
import com.ibm.fhir.schema.control.MigrateV0014LogicalResourceIsDeletedLastUpdated;
import com.ibm.fhir.schema.control.MigrateV0021AbstractTypeRemoval;
import com.ibm.fhir.schema.control.MigrateV0027LogicalResourceIdent;
import com.ibm.fhir.schema.control.OAuthSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;
import com.ibm.fhir.schema.control.TableHasData;
import com.ibm.fhir.schema.control.UnusedTableRemovalNeedsV0021Migration;
import com.ibm.fhir.schema.model.ResourceType;
import com.ibm.fhir.schema.model.Schema;
import com.ibm.fhir.schema.size.CitusSizeCollector;
import com.ibm.fhir.schema.size.FHIRDbSizeModel;
import com.ibm.fhir.schema.size.ISizeCollector;
import com.ibm.fhir.schema.size.ISizeReport;
import com.ibm.fhir.schema.size.PostgresSizeCollector;
import com.ibm.fhir.schema.size.ReadableSizeReport;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;
import com.ibm.fhir.task.core.service.TaskService;

/**
 * Utility app to connect to a database and create/update the IBM FHIR Server schema.
 * The DDL processing is idempotent, with only the necessary changes applied.
 * <br>
 * This utility also includes an option to exercise the tenant partitioning code.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int EXIT_OK = 0; // validation was successful
    private static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    private static final int EXIT_RUNTIME_ERROR = 2; // programming error
    private static final int EXIT_VALIDATION_FAILED = 3; // validation test failed
    private static final int EXIT_NOT_READY = 4; // DATABASE NOT READY
    private static final int EXIT_TABLESPACE_REMOVAL_NOT_COMPLETE = 5; // Tablespace Removal not complete
    private static final int EXIT_CONCURRENT_UPDATE = 6; // Another schema update is running and the wait time expired

    private static final Menu menu = new Menu();
    private static boolean help = Boolean.FALSE;
    private static final double NANOS = 1e9;

    // Indicates if the feature is enabled for the DbType
    public List<DbType> STORED_PROCEDURE_ENABLED = Arrays.asList(DbType.POSTGRESQL, DbType.CITUS);
    public List<DbType> PRIVILEGES_FEATURE_ENABLED = Arrays.asList(DbType.POSTGRESQL, DbType.CITUS);

    // Properties accumulated as we parse args and read configuration files
    private final Properties properties = new Properties();

    // Default Values for schema names
    public static final String ADMIN_SCHEMANAME = "FHIR_ADMIN";
    public static final String OAUTH_SCHEMANAME = "FHIR_OAUTH";
    public static final String BATCH_SCHEMANAME = "FHIR_JBATCH";
    public static final String DATA_SCHEMANAME = "FHIRDATA";

    // Force upper-case to avoid tricky-to-catch errors related to quoting names
    private Schema schema = new Schema();

    // Arguments requesting we drop the objects from the schema
    private boolean dropAdmin = false;
    private boolean confirmDrop = false;
    private boolean updateProc = false;
    private boolean checkCompatibility = false;

    // Action flags related to FHIR Schema
    private boolean createFhirSchema = false;
    private boolean updateFhirSchema = false;
    private boolean dropFhirSchema = false;
    private boolean grantFhirSchema = false;

    // Action flags related to OAuth Schema
    private boolean createOauthSchema = false;
    private boolean updateOauthSchema = false;
    private boolean dropOauthSchema = false;
    private boolean grantOauthSchema = false;

    // Action flags related to Java Batch Schema
    private boolean createJavaBatchSchema = false;
    private boolean updateJavaBatchSchema = false;
    private boolean dropJavaBatchSchema = false;
    private boolean grantJavaBatchSchema = false;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // The database user we will grant read-only data access privileges to (usually null)
    private String grantReadTo;

    // Action flag related to Vacuuming:
    private boolean updateVacuum = false;
    private String vacuumTableName = null;
    private int vacuumCostLimit = 2000;
    private int vacuumThreshold = 1000;
    private Double vacuumScaleFactor = null;

    // How many seconds to wait to obtain the update lease
    private int waitForUpdateLeaseSeconds = 10;

    // The database type being populated. Now a required parameter.
    private DbType dbType;
    private IDatabaseTranslator translator;

    // Optional subset of resource types (for faster schema builds when testing)
    private Set<String> resourceTypeSubset;

    // Forces the removal of tables if data exists
    private boolean forceUnusedTableRemoval = false;

    // Force schema update even if whole-schema-version is current
    private boolean force = false;

    // Report on database size metrics
    private boolean showDbSize;

    // Include detail output in the report (default is no)
    private boolean showDbSizeDetail = false;

    // Split drops into multiple transactions?
    private boolean dropSplitTransaction = false;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    // The pool size for concurrent operations (typically 1 because concurrent DDL can cause internal DB deadlocks)
    private int threadPoolSize = FhirSchemaConstants.DEFAULT_THREAD_POOL_SIZE;

    // The connection pool and transaction provider to support concurrent operations
    private int maxConnectionPoolSize = threadPoolSize + FhirSchemaConstants.CONNECTION_POOL_HEADROOM;
    private PoolConnectionProvider connectionPool;
    private ITransactionProvider transactionProvider;

    // Configuration to control how the LeaseManager operates
    private ILeaseManagerConfig leaseManagerConfig;

    // Which flavor of the FHIR data schema should we build?
    private SchemaType dataSchemaType = SchemaType.PLAIN;

    // -----------------------------------------------------------------------------------------------------------------
    // The following method is related to the common methods and functions
    /**
     * @return a created connection to the selected database
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(properties);
        logger.info("Opening connection to: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return connection;
    }

    /**
     * Create a simple connection pool associated with our data source so that we
     * can perform the DDL deployment in parallel
     */
    protected void configureConnectionPool() {
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        JdbcConnectionProvider cp = new JdbcConnectionProvider(this.translator, adapter);
        this.connectionPool = new PoolConnectionProvider(cp, this.maxConnectionPoolSize);
        this.transactionProvider = new SimpleTransactionProvider(this.connectionPool);

        if (this.dbType == DbType.CITUS) {
            connectionPool.setNewConnectionHandler(Main::configureCitusConnection);
        }
    }

    /**
     * Add the admin schema objects to the {@link PhysicalDataModel}
     *
     * @param pdm the data model to build
     */
    protected void buildAdminSchemaModel(PhysicalDataModel pdm) {
        // Add the tenant and tenant_keys tables and any other admin schema stuff
        final SchemaType adminSchemaType = SchemaType.PLAIN;
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), adminSchemaType);
        gen.buildAdminSchema(pdm);
    }

    /**
     * Add the OAuth schema objects to the given {@link PhysicalDataModel}
     *
     * @param pdm the model to build
     */
    protected void buildOAuthSchemaModel(PhysicalDataModel pdm) {
        // Build/update the Liberty OAuth-related tables
        OAuthSchemaGenerator oauthSchemaGenerator = new OAuthSchemaGenerator(schema.getOauthSchemaName());
        oauthSchemaGenerator.buildOAuthSchema(pdm);
    }

    /**
     * Add the JavaBatch schema objects to the given {@link PhysicalDataModel}
     *
     * @param pdm
     */
    protected void buildJavaBatchSchemaModel(PhysicalDataModel pdm) {
        // Build/update the Liberty JBatch related tables
        JavaBatchSchemaGenerator javaBatchSchemaGenerator = new JavaBatchSchemaGenerator(schema.getJavaBatchSchemaName());
        javaBatchSchemaGenerator.buildJavaBatchSchema(pdm);
    }

    /**
     * Start the schema object creation tasks and wait for everything to complete
     *
     * @param pdm
     * @param adapter
     * @param collector
     * @param vhs
     */
    protected void applyModel(PhysicalDataModel pdm, ISchemaAdapter adapter, ITaskCollector collector, VersionHistoryService vhs, SchemaType schemaType) {
        logger.info("Collecting model update tasks");
        // If using a distributed RDBMS (like Citus) then skip the initial FK creation
        final boolean includeForeignKeys = schemaType != SchemaType.DISTRIBUTED;
        SchemaApplyContext context = SchemaApplyContext.builder().setIncludeForeignKeys(includeForeignKeys).build();
        pdm.collect(collector, adapter, context, this.transactionProvider, vhs);

        // FHIR in the hole!
        logger.info("Starting model updates");
        collector.startAndWait();

        Collection<ITaskGroup> failedTaskGroups = collector.getFailedTaskGroups();
        if (failedTaskGroups.size() > 0) {
            this.exitStatus = EXIT_RUNTIME_ERROR;

            final String failedStr =
                    failedTaskGroups.stream().map((tg) -> tg.getTaskId()).collect(Collectors.joining(","));
            logger.severe("List of failed task groups: " + failedStr);
        }
    }

    /**
     * specific feature to check if it is compatible.
     *
     * @return
     */
    protected boolean checkCompatibility() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            return adapter.checkCompatibility(schema.getAdminSchemaName());
        }
    }

    /**
     * Create the schemas
     */
    protected void createSchemas() {
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                    // We always create the 'admin' schema to track to the changes to any of the other schemas.
                    adapter.createSchema(schema.getAdminSchemaName());

                    // FHIR Data Schema
                    if (createFhirSchema) {
                        adapter.createSchema(schema.getSchemaName());
                        if (this.grantTo != null) {
                            adapter.grantSchemaUsage(schema.getSchemaName(), grantTo);
                        }
                        if (this.grantReadTo != null) {
                            adapter.grantSchemaUsage(schema.getSchemaName(), grantReadTo);
                        }
                        c.commit();
                    }

                    // OAuth Schema
                    if (createOauthSchema) {
                        adapter.createSchema(schema.getOauthSchemaName());
                        if (this.grantTo != null) {
                            adapter.grantSchemaUsage(schema.getOauthSchemaName(), grantTo);
                        }
                    }

                    // Java Batch Schema
                    if (createJavaBatchSchema) {
                        adapter.createSchema(schema.getJavaBatchSchemaName());
                        if (this.grantTo != null) {
                            adapter.grantSchemaUsage(schema.getJavaBatchSchemaName(), grantTo);
                        }
                    }
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Process the schemas configured to be updated
     */
    protected void updateSchemas() {

        // Make sure that we have the CONTROL table created before we try any
        // schema update work
        ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            CreateControl.createTableIfNeeded(schema.getAdminSchemaName(), schemaAdapter);
        } catch (UniqueConstraintViolationException x) {
            // Race condition - two or more instances trying to create the CONTROL table
            throw new ConcurrentUpdateException("Concurrent update - create control table");
        }

        if (updateFhirSchema) {
            updateFhirSchema();
        }

        if (updateOauthSchema) {
            updateOauthSchema();
        }

        if (updateJavaBatchSchema) {
            updateJavaBatchSchema();
        }
    }

    /**
     * Add FHIR data schema objects to the given {@link PhysicalDataModel}
     * @param pdm the data model being built
     */
    protected void buildFhirDataSchemaModel(PhysicalDataModel pdm) {
        FhirSchemaGenerator gen;
        if (resourceTypeSubset == null || resourceTypeSubset.isEmpty()) {
            gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), getDataSchemaType());
        } else {
            gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), getDataSchemaType(), resourceTypeSubset);
        }

        gen.buildSchema(pdm);
        switch (dbType) {
        case DERBY:
            logger.info("No database specific artifacts");
            break;
        case POSTGRESQL:
            gen.buildDatabaseSpecificArtifactsPostgres(pdm);
            break;
        case CITUS:
            gen.buildDatabaseSpecificArtifactsCitus(pdm);
            break;
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    /**
     * Update the FHIR data schema
     */
    protected void updateFhirSchema() {
        LeaseManager leaseManager = new LeaseManager(this.translator, connectionPool, transactionProvider, schema.getAdminSchemaName(), schema.getSchemaName(),
            leaseManagerConfig);

        try {
            if (!leaseManager.waitForLease(waitForUpdateLeaseSeconds)) {
                throw new ConcurrentUpdateException("Concurrent update for FHIR data schema: '" + schema.getSchemaName() + "'");
            }

            final String targetSchemaName = schema.getSchemaName();
            ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, schemaAdapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                int currentSchemaVersion = svm.getVersionForSchema();
                if (this.dbType == DbType.CITUS) {
                    // First version with Citus support is V0027 and we can't upgrade
                    // from before that
                    if (currentSchemaVersion >= 0 && currentSchemaVersion < FhirSchemaVersion.V0027.vid()) {
                        throw new IllegalStateException("Cannot upgrade Citus databases with schema version < V0027");
                    }
                }
                // fail the schema-update if there are existing Evidence or EvidenceVariable resource instances
                if (currentSchemaVersion < FhirSchemaVersion.V0030.vid()) {
                    if (checkIfDataExistsForV0030()) {
                        throw new IllegalStateException("Cannot update schema due to existing Evidence or EvidenceVariable resource instances");
                    }
                }

                // Build/update the FHIR-related tables as well as the stored procedures
                PhysicalDataModel pdm = new PhysicalDataModel(isDistributed());
                buildFhirDataSchemaModel(pdm);
                boolean isNewDb = updateSchema(pdm, getDataSchemaType());

                if (this.exitStatus == EXIT_OK) {
                    populateResourceTypeAndParameterNameTableEntries();

                    // backfill the resource_change_log table if needed
                    backfillResourceChangeLog();

                    // perform any updates we need related to the V0010 schema change (IS_DELETED flag)
                    applyDataMigrationForV0010();

                    // V0014 IS_DELETED and LAST_UPDATED added to whole-system LOGICAL_RESOURCES
                    applyDataMigrationForV0014();

                    // V0021 removes Abstract Type tables which are unused.
                    applyTableRemovalForV0021();

                    // V0027 populate the new LOGICAL_RESOURCE_IDENT table
                    applyDataMigrationForV0027();

                    // Apply privileges if asked
                    if (grantTo != null) {
                        grantPrivilegesForFhirData();
                    }

                    if (grantReadTo != null) {
                        grantReadPrivilegesForFhirData();
                    }
                    
                    

                    // Finally, update the whole schema version
                    svm.updateSchemaVersion();

                    // Log warning messages that unused tables will be removed in a future release.
                    // TODO: This will no longer be needed after the tables are removed (https://github.com/LinuxForHealth/FHIR/issues/713).
                    logWarningMessagesForDeprecatedTables();
                }
            } else if (this.force) {
                logger.info("Cannot force when schema is ahead of this version; skipping update for: '" + targetSchemaName + "'");
                this.exitStatus = EXIT_BAD_ARGS;
            } else {
                // useful to see what version the database is actually at
                int databaseSchemaVersion = svm.getVersionForSchema();
                logger.info("Schema is up-to-date [version=" + databaseSchemaVersion + "]; skipping update for: '" + targetSchemaName + "'");
            }
        } finally {
            leaseManager.cancelLease();
        }
    }

    /**
     * Build and apply the OAuth schema changes
     */
    protected void updateOauthSchema() {
        LeaseManager leaseManager = new LeaseManager(this.translator, connectionPool, transactionProvider, schema.getAdminSchemaName(), schema.getOauthSchemaName(),
            leaseManagerConfig);

        try {
            if (!leaseManager.waitForLease(waitForUpdateLeaseSeconds)) {
                throw new ConcurrentUpdateException("Concurrent update for Liberty OAuth schema: '" + schema.getOauthSchemaName() + "'");
            }

            final String targetSchemaName = schema.getOauthSchemaName();
            ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, schemaAdapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                PhysicalDataModel pdm = new PhysicalDataModel(false);
                buildOAuthSchemaModel(pdm);
                updateSchema(pdm, SchemaType.PLAIN);

                if (this.exitStatus == EXIT_OK) {
                    // Apply privileges if asked
                    if (grantTo != null) {
                        grantPrivilegesForOAuth();
                    }

                    // Mark the schema as up-to-date
                    svm.updateSchemaVersion();
                }
            } else if (this.force) {
                logger.info("Cannot force when schema is ahead of this version; skipping update for: '" + targetSchemaName + "'");
                this.exitStatus = EXIT_BAD_ARGS;
            } else {
                logger.info("Schema is current; skipping update for: '" + targetSchemaName + "'");
            }
        } finally {
            leaseManager.cancelLease();
        }
    }

    /**
     * Build and apply the JavaBatch schema changes
     */
    protected void updateJavaBatchSchema() {
        LeaseManager leaseManager = new LeaseManager(this.translator, connectionPool, transactionProvider, schema.getAdminSchemaName(), schema.getJavaBatchSchemaName(),
            leaseManagerConfig);

        try {
            if (!leaseManager.waitForLease(waitForUpdateLeaseSeconds)) {
                throw new ConcurrentUpdateException("Concurrent update for Liberty JavaBatch schema: '" + schema.getJavaBatchSchemaName() + "'");
            }

            final String targetSchemaName = schema.getJavaBatchSchemaName();
            ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, schemaAdapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                PhysicalDataModel pdm = new PhysicalDataModel(false);
                buildJavaBatchSchemaModel(pdm);
                updateSchema(pdm, SchemaType.PLAIN);

                if (this.exitStatus == EXIT_OK) {
                    // Apply privileges if asked
                    if (grantTo != null) {
                        grantPrivilegesForBatch();
                    }

                    // Mark the schema as up-to-date
                    svm.updateSchemaVersion();
                }
            } else if (this.force) {
                logger.info("Cannot force when schema is ahead of this version; skipping update for: '" + targetSchemaName + "'");
                this.exitStatus = EXIT_BAD_ARGS;
            } else {
                logger.info("Schema is current; skipping update for: '" + targetSchemaName + "'");
            }
        } finally {
            leaseManager.cancelLease();
        }
    }

    /**
     * Update the schema associated with the given {@link PhysicalDataModel}
     * @return true if the database is new
     */
    protected boolean updateSchema(PhysicalDataModel pdm, SchemaType schemaType) {

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        logger.info("Connection pool size: " + this.maxConnectionPoolSize);
        logger.info("    Thread pool size: " + this.threadPoolSize);
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.threadPoolSize);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        ISchemaAdapter schemaAdapter = getSchemaAdapter(schemaType, dbType, connectionPool);

        // Before we start anything, we need to make sure our schema history
        // and control tables are in place. These tables are used to manage
        // all FHIR data, oauth and JavaBatch schemas we build
        try (ITransaction tx = transactionProvider.getTransaction()) {
            CreateVersionHistory.createTableIfNeeded(schema.getAdminSchemaName(), schemaAdapter);
        }

        // Current version history for the data schema
        VersionHistoryService vhs =
                new VersionHistoryService(schema.getAdminSchemaName(), schema.getSchemaName(), schema.getOauthSchemaName(), schema.getJavaBatchSchemaName());
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();

        // Use the version history service to determine if this table existed before we run `applyWithHistory`
        boolean isNewDb = vhs.getVersion(schema.getSchemaName(), DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == null ||
                vhs.getVersion(schema.getSchemaName(), DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == 0;

        applyModel(pdm, schemaAdapter, collector, vhs, schemaType);
        applyDistributionRules(pdm, schemaType);

        // The physical database objects should now match what was defined in the PhysicalDataModel

        return isNewDb;
    }

    /**
     * Apply any table distribution rules then add all the
     * FK constraints that are needed. Applying all the distribution rules
     * in one transaction causes issues with Citus/PostgreSQL (out of shared memory
     * errors) so instead we provide a function to allow the visitor to break
     * things up into smaller transactions.
     * @param pdm
     */
    private void applyDistributionRules(PhysicalDataModel pdm, SchemaType schemaType) {
        if (dbType == DbType.CITUS) {
            ISchemaAdapter schemaAdapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
            pdm.applyDistributionRules(schemaAdapter, () -> TransactionFactory.openTransaction(connectionPool));
        }

        final boolean includedForeignKeys = schemaType != SchemaType.DISTRIBUTED;
        // Skip all foreign key creation for Citus if we're in distributed mode
        if (!includedForeignKeys && dbType != DbType.CITUS) {
            // Now that all the tables have been distributed, it should be safe
            // to apply the FK constraints
            ISchemaAdapter adapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
            AddForeignKey adder = new AddForeignKey(adapter);
            pdm.visit(adder, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP, () -> TransactionFactory.openTransaction(connectionPool));
        }
    }

    /**
     * Special case to initialize new connections created by the connection pool
     * for Citus databases
     * @param c
     */
    private static void configureCitusConnection(Connection c) {
        logger.info("Citus: Configuring new database connection");
        ConfigureConnectionDAO dao = new ConfigureConnectionDAO();
        dao.run(new CitusTranslator(), c);
     }

    /**
     * populates the RESOURCE_TYPE table.
     *
     * @implNote if you update this method, be sure to update
     *           DerbyBootstrapper.populateResourceTypeAndParameterNameTableEntries
     *           and DerbyFhirDatabase.populateResourceTypeAndParameterNameTableEntries
     *           The reason is there are three different ways of managing the transaction.
     */
    protected void populateResourceTypeAndParameterNameTableEntries() {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                logger.info("Populating schema with lookup table data.");
                PopulateResourceTypes populateResourceTypes =
                        new PopulateResourceTypes(schema.getSchemaName());
                populateResourceTypes.run(translator, c);

                PopulateParameterNames populateParameterNames =
                        new PopulateParameterNames(schema.getSchemaName());
                populateParameterNames.run(translator, c);
                logger.info("Finished prepopulating the resource type and search parameter code/name tables tables");
            } catch (SQLException ex) {
                tx.setRollbackOnly();
                throw new DataAccessException(ex);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Drop all the objects in the admin and data schemas.
     * Typically used during development.
     */
    protected void dropSchema() {
        PhysicalDataModel pdm = new PhysicalDataModel(isDistributed());
        buildCommonModel(pdm, dropFhirSchema, dropOauthSchema, dropJavaBatchSchema);

        // Dropping the schema in PostgreSQL can fail with an out of shared memory error
        // which is apparently related to max_locks_per_transaction. It may not be possible
        // to increase this value (e.g. in cloud databases) and so to work around this, before
        // dropping the schema objects, we knock out all the FOREIGN KEY constraints first.
        if (dropFhirSchema) {
            logger.info("Dropping FK constraints in the data schema: " + this.schema.getSchemaName());
            dropForeignKeyConstraints(pdm, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
        }

        if (dropOauthSchema) {
            logger.info("Dropping FK constraints in the OAuth schema: " + this.schema.getOauthSchemaName());
            dropForeignKeyConstraints(pdm, FhirSchemaGenerator.SCHEMA_GROUP_TAG, OAuthSchemaGenerator.OAUTH_GROUP);
        }

        if (dropJavaBatchSchema) {
            logger.info("Dropping FK constraints in the Batch schema: " + this.schema.getJavaBatchSchemaName());
            dropForeignKeyConstraints(pdm, FhirSchemaGenerator.SCHEMA_GROUP_TAG, JavaBatchSchemaGenerator.BATCH_GROUP);
        }

        if (dropAdmin) {
            // Also drop the FK constraints within the administration schema
            logger.info("Dropping FK constraints in the admin schema: " + this.schema.getAdminSchemaName());
            dropForeignKeyConstraints(pdm, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
        }

        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);
                    ISchemaAdapter schemaAdapter = getSchemaAdapter(getDataSchemaType(), adapter);
                    ISchemaAdapter plainSchemaAdapter = getSchemaAdapter(SchemaType.PLAIN, adapter);
                    VersionHistoryService vhs =
                            new VersionHistoryService(schema.getAdminSchemaName(), schema.getSchemaName(), schema.getOauthSchemaName(), schema.getJavaBatchSchemaName());
                    vhs.setTransactionProvider(transactionProvider);
                    vhs.setTarget(adapter);

                    if (dropFhirSchema) {
                        // Just drop the objects associated with the FHIRDATA schema group
                        final String schemaName = schema.getSchemaName();
                        if (this.dropSplitTransaction) {
                            // important that we use an adapter connected with the connection pool
                            // (which is connected to the transaction provider)
                            ISchemaAdapter poolSchemaAdapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
                            pdm.dropSplitTransaction(poolSchemaAdapter, this.transactionProvider, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
                        } else {
                            // old fashioned drop where we do everything in one (big) transaction
                            pdm.drop(schemaAdapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
                        }

                        // Drop the whole-schema-version table
                        CreateWholeSchemaVersion.dropTable(schemaName, plainSchemaAdapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropOauthSchema) {
                        // Just drop the objects associated with the OAUTH schema group
                        final String schemaName = schema.getOauthSchemaName();
                        pdm.drop(schemaAdapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, OAuthSchemaGenerator.OAUTH_GROUP);
                        CreateWholeSchemaVersion.dropTable(schemaName, plainSchemaAdapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropJavaBatchSchema) {
                        // Just drop the objects associated with the BATCH schema group
                        final String schemaName = schema.getJavaBatchSchemaName();
                        pdm.drop(plainSchemaAdapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, JavaBatchSchemaGenerator.BATCH_GROUP);
                        CreateWholeSchemaVersion.dropTable(schemaName, plainSchemaAdapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropAdmin) {
                        // Just drop the objects associated with the ADMIN schema group
                        CreateVersionHistory.generateTable(pdm, ADMIN_SCHEMANAME, true);
                        CreateControl.buildTableDef(pdm, ADMIN_SCHEMANAME, true);
                        pdm.drop(plainSchemaAdapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
                        if (!checkSchemaIsEmpty(adapter, schema.getAdminSchemaName())) {
                            throw new DataAccessException("Schema '" + schema.getAdminSchemaName() + "' not empty after drop");
                        }
                    }
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Add part of the schema drop process, we first kill all
     * the foreign key constraints. The rest of the drop is
     * performed in a separate transaction.
     *
     * @param pdm
     */
    private void dropForeignKeyConstraints(PhysicalDataModel pdm, String tagGroup, String tag) {
        if (dbType == DbType.CITUS) {
            // For Citus we need to break up the drop into smaller transactions to avoid
            // out of shared memory errors. The drop of each FK idempotent, so we can
            // restart if we need to
            IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
            Set<Table> referencedTables = new HashSet<>();
            DropForeignKey dropper = new DropForeignKey(adapter, referencedTables);
            pdm.visit(dropper, tagGroup, tag, () -> TransactionFactory.openTransaction(connectionPool));
        } else {
            // For all other databases, we can drop all the FK constraints in a single transaction
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                    Set<Table> referencedTables = new HashSet<>();
                    DropForeignKey dropper = new DropForeignKey(adapter, referencedTables);
                    pdm.visit(dropper, tagGroup, tag, null);
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // The following method is related to the Stored Procedures and Functions feature
    /**
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     */
    protected void updateProcedures() {
        if (!STORED_PROCEDURE_ENABLED.contains(dbType)) {
            return;
        }

        // Build/update the tables as well as the stored procedures
        PhysicalDataModel pdm = new PhysicalDataModel(isDistributed());
        // Since this is a stored procedure, we need the model.
        // We must pass in true to flag to the underlying layer that the
        // Procedures need to be generated.
        buildCommonModel(pdm, true, updateOauthSchema, updateJavaBatchSchema);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                try {
                    ISchemaAdapter schemaAdapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
                    SchemaApplyContext context = SchemaApplyContext.getDefault();
                    pdm.applyProcedures(schemaAdapter, context);
                    pdm.applyFunctions(schemaAdapter, context);

                    // Because we're replacing the procedures, we should also check if
                    // we need to apply the associated privileges
                    if (this.grantTo != null) {
                        pdm.applyProcedureAndFunctionGrants(schemaAdapter, FhirSchemaConstants.FHIR_USER_GRANT_GROUP, grantTo);
                    }
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
                // Reminder: Don't fall into the trap, let the connectionPool and Transaction Management handle the
                // transaction commit.
            } catch (SQLException x) {
                tx.setRollbackOnly();
                throw translator.translate(x);
            }
        }
    }

    /**
     * Build a common PhysicalDataModel containing all the requested schemas
     *
     * @param pdm the model to construct
     * @param addFhirDataSchema include objects for the FHIR data schema
     * @param addOAuthSchema include objects for the Liberty OAuth schema
     * @param addJavaBatchSchema include objects for the Liberty JavaBatch schema
     */
    protected void buildCommonModel(PhysicalDataModel pdm, boolean addFhirDataSchema, boolean addOAuthSchema, boolean addJavaBatchSchema) {
        if (addFhirDataSchema) {
            buildFhirDataSchemaModel(pdm);
        }

        if (addOAuthSchema) {
            buildOAuthSchemaModel(pdm);
        }

        if (addJavaBatchSchema) {
            buildJavaBatchSchemaModel(pdm);
        }
    }

    protected void grantPrivilegesForFhirData() {
        grantPrivilegesForFhirData(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, this.grantTo);
    }

    /**
     * Grant only SELECT privileges to the given user to provide read-only direct schema access
     */
    protected void grantReadPrivilegesForFhirData() {
        Objects.requireNonNull(this.grantReadTo, "grantReadTo not set");

        final ISchemaAdapter adapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // In order to SELECT from the tables, we need at least USAGE on the schema
                adapter.grantSchemaUsage(schema.getSchemaName(), grantReadTo);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        grantPrivilegesForFhirData(FhirSchemaConstants.FHIR_READ_USER_GRANT_GROUP, this.grantReadTo);
    }

    /**
     * Apply grants to the FHIR data schema objects
     * @param privilegeGroupName identifies the group of privileges to apply
     */
    protected void grantPrivilegesForFhirData(String privilegeGroupName, String targetUser) {

        final ISchemaAdapter schemaAdapter = getSchemaAdapter(getDataSchemaType(), dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // This may have been granted as part of creating the schema, but it won't hurt to make sure
                schemaAdapter.grantSchemaUsage(schema.getSchemaName(), grantTo);

                PhysicalDataModel pdm = new PhysicalDataModel(isDistributed());
                buildFhirDataSchemaModel(pdm);
                pdm.applyGrants(schemaAdapter, privilegeGroupName, targetUser);

                // Grant SELECT on WHOLE_SCHEMA_VERSION to the FHIR server user
                // Note the constant comes from SchemaConstants on purpose
                CreateWholeSchemaVersion.grantPrivilegesTo(schemaAdapter, schema.getSchemaName(), privilegeGroupName, targetUser);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Apply grants to the OAuth schema objects
     */
    protected void grantPrivilegesForOAuth() {
        final ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // This may have been granted as part of creating the schema, but it won't hurt to make sure
                schemaAdapter.grantSchemaUsage(schema.getOauthSchemaName(), grantTo);

                PhysicalDataModel pdm = new PhysicalDataModel(false);
                buildOAuthSchemaModel(pdm);
                pdm.applyGrants(schemaAdapter, FhirSchemaConstants.FHIR_OAUTH_GRANT_GROUP, grantTo);

            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    /**
     * Apply grants to the JavaBatch schema objects
     */
    protected void grantPrivilegesForBatch() {
        final ISchemaAdapter schemaAdapter = getSchemaAdapter(SchemaType.PLAIN, dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // This may have been granted as part of creating the schema, but it won't hurt to make sure
                schemaAdapter.grantSchemaUsage(schema.getJavaBatchSchemaName(), grantTo);

                PhysicalDataModel pdm = new PhysicalDataModel(false);
                buildJavaBatchSchemaModel(pdm);
                pdm.applyGrants(schemaAdapter, FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, grantTo);

                // special case for the JavaBatch schema in PostgreSQL
                schemaAdapter.grantAllSequenceUsage(schema.getJavaBatchSchemaName(), grantTo);

            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     */
    protected void grantPrivileges() {
        if (!PRIVILEGES_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // The case where all are to be granted on the default schemas.
        if (!(updateFhirSchema || grantFhirSchema || updateOauthSchema
                || grantOauthSchema || updateJavaBatchSchema || grantJavaBatchSchema)) {
            grantOauthSchema = true;
            grantFhirSchema = true;
            grantJavaBatchSchema = true;
        }

        if (grantFhirSchema) {
            if (grantTo != null) {
                grantPrivilegesForFhirData();
            }
            if (grantReadTo != null) {
                grantReadPrivilegesForFhirData();                
            }
        }

        if (grantOauthSchema && grantTo != null) {
            grantPrivilegesForOAuth();
        }

        if (grantJavaBatchSchema && grantTo != null) {
            grantPrivilegesForBatch();
        }
    }

    /**
     * What type of FHIR data schema do we want to build?
     * @return
     */
    protected SchemaType getDataSchemaType() {
        return this.dataSchemaType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // The following methods are related to parsing arguments and action selection
    /**
     * Parse the command-line arguments, building up the environment and
     * establishing
     * the run-list
     *
     * @param args
     */
    protected void parseArgs(String[] args) {
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        LeaseManagerConfig.Builder lmConfig = LeaseManagerConfig.builder();
        lmConfig.withHost(new HostnameHandler().getHostname());
        lmConfig.withLeaseTimeSeconds(100); // default
        lmConfig.withStayAlive(true);       // default

        for (int i = 0; i < args.length; i++) {
            int nextIdx = (i + 1);
            String arg = args[i];
            switch (arg) {
            case PROP_FILE:
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case SCHEMA_NAME:
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);
                    schema.setSchemaName(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case GRANT_TO:
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    this.grantTo = args[i].toUpperCase();
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case GRANT_READ_TO:
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    this.grantReadTo = args[i].toUpperCase();
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case TARGET:
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);
                    List<String> targets = Arrays.asList(args[i].split(","));
                    for (String target : targets) {
                        String tmp = target.toUpperCase();
                        nextIdx++;
                        if (tmp.startsWith("BATCH")) {
                            this.grantJavaBatchSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setJavaBatchSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else if (tmp.startsWith("OAUTH")) {
                            this.grantOauthSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setOauthSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else if (tmp.startsWith("DATA")) {
                            this.grantFhirSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else {
                            throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case UPDATE_PROC:
                this.updateProc = true;
                break;
            case CHECK_COMPATIBILITY:
                this.checkCompatibility = true;
                break;
            case DROP_ADMIN:
                this.dropAdmin = true;
                break;
            case UPDATE_SCHEMA:
                this.updateFhirSchema = true;
                this.updateOauthSchema = true;
                this.updateJavaBatchSchema = true;
                break;
            case UPDATE_SCHEMA_FHIR:
                this.updateFhirSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setSchemaName(args[nextIdx]);
                    i++;
                } else {
                    schema.setSchemaName(DATA_SCHEMANAME);
                }
                break;
            case UPDATE_SCHEMA_BATCH:
                this.updateJavaBatchSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case UPDATE_SCHEMA_OAUTH:
                this.updateOauthSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case CREATE_SCHEMAS:
                this.createFhirSchema = true;
                this.createOauthSchema = true;
                this.createJavaBatchSchema = true;
                break;
            case CREATE_SCHEMA_FHIR:
                this.createFhirSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case CREATE_SCHEMA_BATCH:
                this.createJavaBatchSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case CREATE_SCHEMA_OAUTH:
                this.createOauthSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case DROP_SCHEMA:
                System.err.print("Option '--drop-schema' has been retired.  Please use '--drop-schema-fhir', "
                        + "'--drop-schema-batch', and/or '--drop-schema-oauth'.");
                break;
            case DROP_SCHEMA_FHIR:
                this.dropFhirSchema = Boolean.TRUE;
                break;
            case DROP_SCHEMA_BATCH:
                this.dropJavaBatchSchema = Boolean.TRUE;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case DROP_SCHEMA_OAUTH:
                this.dropOauthSchema = Boolean.TRUE;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case DROP_SPLIT_TRANSACTION:
                this.dropSplitTransaction = true;
                break;
            case POOL_SIZE:
                if (++i < args.length) {
                    this.maxConnectionPoolSize = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case THREAD_POOL_SIZE:
                if (++i < args.length) {
                    this.threadPoolSize = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case PROP:
                if (++i < args.length) {
                    // properties are given as name=value
                    addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case UPDATE_VACUUM:
                updateVacuum = true;
                break;
            case VACUUM_COST_LIMIT:
                if (++i < args.length) {
                    this.vacuumCostLimit = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case VACUUM_TRESHOLD:
                if (++i < args.length) {
                    this.vacuumThreshold = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case VACUUM_SCALE_FACTOR:
                if (++i < args.length) {
                    this.vacuumScaleFactor = Double.parseDouble(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case VACUUM_TABLE_NAME:
                if (++i < args.length) {
                    this.vacuumTableName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case CONFIRM_DROP:
                this.confirmDrop = true;
                break;
            case SCHEMA_TYPE:
                if (++i < args.length) {
                    this.dataSchemaType = SchemaType.valueOf(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case DB_TYPE:
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                switch (dbType) {
                case DERBY:
                    translator = new DerbyTranslator();
                    break;
                case POSTGRESQL:
                    translator = new PostgresTranslator();
                    break;
                case CITUS:
                    translator = new CitusTranslator();
                    dataSchemaType = SchemaType.DISTRIBUTED; // by default
                    break;
                default:
                    break;
                }
                break;
            case FORCE_UNUSED_TABLE_REMOVAL:
                forceUnusedTableRemoval = true;
                break;
            case FORCE:
                force = true;
                break;
            case SHOW_DB_SIZE:
                showDbSize = true;
                break;
            case SHOW_DB_SIZE_DETAIL:
                showDbSizeDetail = true;
                break;
            case HELP:
                help = Boolean.TRUE;
                throw new IllegalArgumentException("Help Menu Triggered");
            default:
                throw new IllegalArgumentException("Invalid argument: '" + arg + "'");
            }
        }

        this.leaseManagerConfig = lmConfig.build();

        // Make sure we configure the connection pool to have some extra
        // headroom above the thread pool size to support other threads
        // (the lease manager, for example)
        if (this.maxConnectionPoolSize < threadPoolSize + FhirSchemaConstants.CONNECTION_POOL_HEADROOM) {
            this.maxConnectionPoolSize = threadPoolSize + FhirSchemaConstants.CONNECTION_POOL_HEADROOM;
            logger.warning("Connection pool size below minimum headroom. Setting it to " + this.maxConnectionPoolSize);
        }

        if (this.dbType == null) {
            throw new IllegalArgumentException(DB_TYPE + " <type> is required");
        }
    }

    /**
     * Read the properties from the given file
     *
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
            // Trim leading and trailing whitespace from property values (except password)
            for (Entry<Object, Object> entry : properties.entrySet()) {
                if (!"password".equals(entry.getKey())) {
                    String trimmedValue = entry.getValue().toString().trim();
                    if (!trimmedValue.equals(entry.getValue().toString())) {
                        logger.warning("Whitespace trimmed from value of property '" + entry.getKey() + "'");
                        entry.setValue(trimmedValue);
                    }
                }
            }
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Parse the given key=value string and add to the properties being collected
     *
     * @param pair
     */
    public void addProperty(String pair) {
        String[] kv = pair.split("=");
        if (kv.length == 2) {
            // Trim leading and trailing whitespace from property value (except password)
            if (!"password".equals(kv[0])) {
                String trimmedValue = kv[1].trim();
                if (!trimmedValue.equals(kv[1])) {
                    logger.warning("Whitespace trimmed from value of property '" + kv[0] + "'");
                }
                properties.put(kv[0], trimmedValue);
            } else {
                properties.put(kv[0], kv[1]);
            }
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

    /**
     * Perform the special data migration steps required for the V0010 version of the schema
     */
    protected void applyDataMigrationForV0010() {
        doMigrationForV0010();
    }

    protected void applyDataMigrationForV0014() {
        dataMigrationForV0014();
    }

    protected void applyDataMigrationForV0027() {
        dataMigrationForV0027();
    }

    /**
     * Get the list of resource types to drive resource-by-resource operations
     *
     * @return the full list of FHIR R4 resource types, or a subset of names if so configured
     */
    private Set<String> getResourceTypes() {
        Set<String> result;
        if (this.resourceTypeSubset == null || this.resourceTypeSubset.isEmpty()) {
            // Should simplify FhirSchemaGenerator and always pass in this list. When switching
            // over to false, migration is required to drop the tables no longer required.
            final boolean includeAbstractResourceTypes = false;
            result = ModelSupport.getResourceTypes(includeAbstractResourceTypes).stream().map(Class::getSimpleName).collect(Collectors.toSet());
        } else {
            result = this.resourceTypeSubset;
        }

        return result;
    }

    /**
     * Get the list of resource types from the database.
     *
     * @param adapter
     * @param schemaName
     * @return
     */
    private List<ResourceType> getResourceTypesList(IDatabaseAdapter adapter, String schemaName) {

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetResourceTypeList cmd = new GetResourceTypeList(schemaName);
                return adapter.runStatement(cmd);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Perform the data migration for V0010 (non-multi-tenant schema)
     */
    private void doMigrationForV0010() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        Set<String> resourceTypes = getResourceTypes();

        // Process each resource type in its own transaction to avoid pressure on the tx log
        for (String resourceTypeName : resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    // only process tables which have been converted to the V0010 schema but
                    // which have not yet had their data migrated. The migration can't be
                    // done as part of the schema change because some tables need a REORG which
                    // has to be done after the transaction in which the alter table was performed.
                    GetXXLogicalResourceNeedsMigration needsMigrating = new GetXXLogicalResourceNeedsMigration(schema.getSchemaName(), resourceTypeName);
                    if (adapter.runStatement(needsMigrating)) {
                        logger.info("V0010-V0012 Migration: Updating " + resourceTypeName + "_LOGICAL_RESOURCES denormalized columns in schema "
                                + schema.getSchemaName());
                        InitializeLogicalResourceDenorms cmd = new InitializeLogicalResourceDenorms(schema.getSchemaName(), resourceTypeName);
                        adapter.runStatement(cmd);
                    }
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * Migrate the LOGICAL_RESOURCE IS_DELETED and LAST_UPDATED data
     */
    private void dataMigrationForV0014() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        List<ResourceType> resourceTypes = getResourceTypesList(adapter, schema.getSchemaName());

        // Process each resource type in its own transaction to avoid pressure on the tx log
        for (ResourceType resourceType : resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    dataMigrationForV0014(adapter, schema.getSchemaName(), resourceType);
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * V0027 migration. Populate LOGICAL_RESOURCE_IDENT from LOGICAL_RESOURCES
     */
    private void dataMigrationForV0027() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                dataMigrationForV0027(adapter, schema.getSchemaName());
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * only process tables which have not yet had their data migrated. The migration can't be
     * done as part of the schema change because some tables need a REORG which
     * has to be done after the transaction in which the alter table was performed.
     *
     * @param adapter
     * @param schemaName
     * @param resourceType
     */
    private void dataMigrationForV0014(IDatabaseAdapter adapter, String schemaName, ResourceType resourceType) {
        GetLogicalResourceNeedsV0014Migration needsMigrating = new GetLogicalResourceNeedsV0014Migration(schemaName, resourceType.getId());
        if (adapter.runStatement(needsMigrating)) {
            logger.info("V0014 Migration: Updating LOGICAL_RESOURCES.IS_DELETED and LAST_UPDATED for schema '"
                    + schemaName + "' and resource type '" + resourceType.toString() + "'");
            MigrateV0014LogicalResourceIsDeletedLastUpdated cmd =
                    new MigrateV0014LogicalResourceIsDeletedLastUpdated(schemaName, resourceType.getName(), resourceType.getId());
            adapter.runStatement(cmd);
        }
    }

    /**
     * If the LOGICAL_RESOURCE_IDENT table is empty, fill it using values from
     * LOGICAL_RESOURCES
     * @param adapter
     * @param schemaName
     */
    private void dataMigrationForV0027(IDatabaseAdapter adapter, String schemaName) {
        GetLogicalResourceNeedsV0027Migration needsMigrating = new GetLogicalResourceNeedsV0027Migration(schemaName);
        if (adapter.runStatement(needsMigrating)) {
            MigrateV0027LogicalResourceIdent cmd = new MigrateV0027LogicalResourceIdent(schemaName);
            adapter.runStatement(cmd);
        }
    }

    /**
     * Backfill the RESOURCE_CHANGE_LOG table if it is empty
     */
    protected void backfillResourceChangeLog() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetResourceChangeLogEmpty isEmpty = new GetResourceChangeLogEmpty(schema.getSchemaName());
                if (adapter.runStatement(isEmpty)) {
                    // change log is empty, so we need to backfill it with data
                    doBackfill(adapter);
                } else {
                    logger.info("RESOURCE_CHANGE_LOG has data so skipping backfill");
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Backfill the RESOURCE_CHANGE_LOG table for all the resource types. Non-multi-tenant
     * implementation
     *
     * @param adapter
     */
    private void doBackfill(IDatabaseAdapter adapter) {
        Set<String> resourceTypes = getResourceTypes();

        for (String resourceTypeName : resourceTypes) {
            logger.info("Backfilling RESOURCE_CHANGE_LOG with " + resourceTypeName
                    + " resources for schema '" + schema.getSchemaName() + "'");
            BackfillResourceChangeLog backfill = new BackfillResourceChangeLog(schema.getSchemaName(), resourceTypeName);
            adapter.runStatement(backfill);
        }
    }

    /**
     * updates the vacuum settings for postgres/citus.
     */
    public void updateVacuumSettings() {
        if (dbType != DbType.POSTGRESQL && dbType != DbType.CITUS) {
            logger.severe("Updating the vacuum settings is only supported on postgres and the setting is for '" + dbType + "'");
            return;
        }

        // Create the Physical Data Model
        PhysicalDataModel pdm = new PhysicalDataModel(isDistributed());
        buildCommonModel(pdm, true, false, false);

        // Setup the Connection Pool
        this.maxConnectionPoolSize = 10;
        configureConnectionPool();
        PostgresAdapter adapter = new PostgresAdapter(connectionPool);

        if (vacuumTableName != null) {
            runSingleTable(adapter, pdm, schema.getSchemaName(), vacuumTableName);
        } else {
            // Process all tables in the schema ... except for XX_RESOURCES and COMMON_TOKEN_VALUES and COMMON_CANONICAL_VALUES
            GatherTablesDataModelVisitor visitor = new GatherTablesDataModelVisitor();
            pdm.visit(visitor);

            for (Table tbl : visitor.getTables()) {
                runSingleTable(adapter, pdm, schema.getSchemaName(), tbl.getObjectName());
            }
        }
    }

    /**
     * Should we build the distributed variant of the FHIR data schema. This
     * changes how we need to handle certain unique indexes and foreign key
     * constraints.
     * @return
     */
    private boolean isDistributed() {
        return dataSchemaType == SchemaType.DISTRIBUTED;
    }

    /**
     * runs the vacuum update inside a single connection and single transaction.
     *
     * @param adapter
     * @param pdm
     * @param schemaName
     * @param vacuumTableName
     */
    private void runSingleTable(PostgresAdapter adapter, PhysicalDataModel pdm, String schemaName, String vacuumTableName) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // If the vacuumTableName exists, then we try finding the table definition.
                Table table = pdm.findTable(schemaName, vacuumTableName);
                if (table == null) {
                    logger.severe("Table [" + vacuumTableName + "] is not found, and no vacuum settings are able to be updated.");
                    throw new IllegalArgumentException("Table [" + vacuumTableName + "] is not found, and no vacuum settings are able to be updated.");
                }
                PostgresVacuumSettingDAO alterVacuumSettings =
                        new PostgresVacuumSettingDAO(schemaName, table.getObjectName(), vacuumCostLimit, vacuumScaleFactor, vacuumThreshold);
                adapter.runStatement(alterVacuumSettings);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Check to see if we have anything left over in a schema we expect to be empty
     * @param adapter
     * @param schemaName
     * @return
     */
    private boolean checkSchemaIsEmpty(IDatabaseAdapter adapter, String schemaName) {
        List<SchemaInfoObject> schemaObjects = adapter.listSchemaObjects(schemaName);
        boolean result = schemaObjects.isEmpty();
        if (!result) {
            // When called, we expect the schema to be empty, so let's dump what we have
            final String remaining = schemaObjects.stream().map(Object::toString).collect(Collectors.joining(","));
            logger.warning("Remaining objects in schema '" + schemaName + "': [" + remaining + "]");
        }
        return result;
    }

    /**
     * Query the database to collect information on space usage by tables and indexes
     * attributed to resources and their search parameters then render to a useful
     * report
     */
    private void generateDbSizeReport() {
        FHIRDbSizeModel model = new FHIRDbSizeModel(this.schema.getSchemaName());
        final ISizeCollector collector;
        switch (dbType) {
        case POSTGRESQL:
            collector = new PostgresSizeCollector(model);
            break;
        case CITUS:
            logger.warning("**** Citus size report is incomplete ****");
            collector = new CitusSizeCollector(model);
            break;
        case DERBY:
            collector = null;
            logger.severe("Size report not supported for Derby databases");
            exitStatus = EXIT_BAD_ARGS;
        default:
            throw new IllegalArgumentException("Unsupported DbType: " + dbType);
        }

        if (collector != null) {
            collectDbSizeInfo(collector);
            // render the report using UTF8 regardless of system config
            OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
            ISizeReport report = new ReadableSizeReport(writer, this.showDbSizeDetail);
            report.render(model);
            try {
                writer.flush();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }
    }

    /**
     * Run the collector to populate the size model
     * @param collector
     */
    private void collectDbSizeInfo(ISizeCollector collector) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection()) {
                collector.run(this.schema.getSchemaName(), c, translator);
            } catch (SQLException x) {
                tx.setRollbackOnly();
                throw this.translator.translate(x);
            }
        }
    }

    /**
     * Process the requested operation
     */
    protected void process() {
        long start = System.nanoTime();
        loadDriver(translator);
        configureConnectionPool();

        if (dbType == DbType.DERBY && threadPoolSize > 1) {
            logger.warning("Embedded Derby does not support concurrent schema updates;" +
                    " ignoring '--thread-pool-size' and using a single thread.");
            this.threadPoolSize = 1;
        }

        if (this.checkCompatibility) {
            checkCompatibility();
        }

        if (translator.isDerby() && !"APP".equals(schema.getSchemaName())) {
            if (schema.isOverrideDataSchema()) {
                logger.warning("Only the APP schema is supported for Apache Derby; ignoring the passed"
                        + " schema name '" + schema.getSchemaName() + "' and using APP.");
            }
            schema.setSchemaName("APP");
        }

        // [optional] use a subset of resource types to make testing quicker
        String resourceTypesString = properties.getProperty("resourceTypes");
        if (resourceTypesString != null && resourceTypesString.length() > 0) {
            resourceTypeSubset = new HashSet<>(Arrays.asList(resourceTypesString.split(",")));

            // Double check for Abstract Types
            if (resourceTypeSubset.contains("DomainResource") || resourceTypeSubset.contains("Resource")) {
                throw new IllegalArgumentException("--prop resourceTypes=<resourceTypes> should not include Abstract types");
            }
        }

        if (showDbSize) {
            generateDbSizeReport();
        } else if (updateVacuum) {
            updateVacuumSettings();
        } else if (this.dropAdmin || this.dropFhirSchema || this.dropJavaBatchSchema || this.dropOauthSchema) {
            // only proceed with the drop if the user has provided additional confirmation
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (updateFhirSchema || updateOauthSchema || updateJavaBatchSchema) {
            updateSchemas();
        } else if (createFhirSchema || createOauthSchema || createJavaBatchSchema) {
            createSchemas();
        } else if (updateProc) {
            updateProcedures();
        } else if (grantTo != null || grantReadTo != null) {
            // Finally, if --grant-to or --grant-read-to has been specified on its own, 
            // we simply rerun all the grants which allows granting server to more than 
            // one user if that's required
            grantPrivileges();
        }

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    /**
     * Get the program exit status from the environment
     *
     * @return
     */
    protected int getExitStatus() {
        return this.exitStatus;
    }

    /**
     * Write a final status message - useful for QA to review when checking the
     * output
     */
    protected void logStatusMessage(int status) {
        switch (status) {
        case EXIT_OK:
            logger.info("SCHEMA CHANGE: OK");
            break;
        case EXIT_BAD_ARGS:
            logger.severe("SCHEMA CHANGE: BAD ARGS");
            break;
        case EXIT_RUNTIME_ERROR:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        case EXIT_CONCURRENT_UPDATE:
            logger.warning("SCHEMA CHANGE: CONCURRENT UPDATE");
            break;
        case EXIT_VALIDATION_FAILED:
            logger.warning("SCHEMA CHANGE: FAILED");
            break;
        default:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        }
    }

    /**
     * Removes RESOURCE and DOMAIN_RESOURCE tables from the fhir data schemas.
     */
    private void applyTableRemovalForV0021() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                String adminSchemaName = this.schema.getAdminSchemaName();
                String schemaName = this.schema.getSchemaName();
                UnusedTableRemovalNeedsV0021Migration needsMigrating = new UnusedTableRemovalNeedsV0021Migration(schemaName);
                if (adapter.runStatement(needsMigrating)) {
                    logger.info("V0021 Migration: Removing Abstract Tables from schema '" + schemaName + "'");
                    MigrateV0021AbstractTypeRemoval cmd = new MigrateV0021AbstractTypeRemoval(adapter, adminSchemaName, schemaName, forceUnusedTableRemoval);
                    adapter.runStatement(cmd);
                    logger.info("V0021 Migration: Completed the Removal of the Abstract Tables from schema '" + schemaName + "'");
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Log warning messages for deprecated tables.
     */
    private void logWarningMessagesForDeprecatedTables() {
        List<String> deprecatedResourceTypes = Arrays.asList(
            "EffectEvidenceSynthesis",
            "MedicinalProduct",
            "MedicinalProductAuthorization",
            "MedicinalProductContraindication",
            "MedicinalProductIndication",
            "MedicinalProductIngredient",
            "MedicinalProductInteraction",
            "MedicinalProductManufactured",
            "MedicinalProductPackaged",
            "MedicinalProductPharmaceutical",
            "MedicinalProductUndesirableEffect",
            "RiskEvidenceSynthesis",
            "SubstanceNucleicAcid",
            "SubstancePolymer",
            "SubstanceProtein",
            "SubstanceReferenceInformation",
            "SubstanceSourceMaterial",
            "SubstanceSpecification"
        );
        List<String> deprecatedTables = Arrays.asList(
            "_DATE_VALUES", "_LATLNG_VALUES", "_LOGICAL_RESOURCES", "_NUMBER_VALUES",
            "_QUANTITY_VALUES", "_RESOURCE_TOKEN_REFS", "_RESOURCES","_STR_VALUES");
        for (String deprecatedType : deprecatedResourceTypes) {
            logger.warning(deprecatedType + " tables [" +
                    deprecatedType + String.join(", " + deprecatedType, deprecatedTables) +
                    "] will be dropped in a future release. " +
                    "No data should be written to these tables. " +
                    "If any data exists in these tables, that data should be exported (if desired) and deleted from these tables.");
        }
    }

    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        logClasspath(logger);

        int exitStatus;
        Main m = new Main();
        try {
            configureLogger();
            m.parseArgs(args);
            m.process();
            exitStatus = m.getExitStatus();
        } catch (TableSpaceRemovalException x) {
            logger.warning("Tablespace removal is not complete, as an async dependency has not finished dettaching. Please re-try.");
            exitStatus = EXIT_TABLESPACE_REMOVAL_NOT_COMPLETE;
        } catch (ConcurrentUpdateException x) {
            // We handle this, so no need to log the exception
            logger.log(Level.WARNING, "Please try again later: update is already running - " + x.getMessage());
            exitStatus = EXIT_CONCURRENT_UPDATE;
        } catch (DatabaseNotReadyException x) {
            logger.log(Level.SEVERE, "The database is not yet available. Please re-try.", x);
            exitStatus = EXIT_NOT_READY;
        } catch (IllegalArgumentException x) {
            if (!help) {
                logger.log(Level.SEVERE, "bad argument", x);
                exitStatus = EXIT_BAD_ARGS;
            } else {
                exitStatus = EXIT_OK;
            }
            menu.generateHelpMenu();
        } catch (Exception x) {
            logger.log(Level.SEVERE, "schema tool failed", x);
            exitStatus = EXIT_RUNTIME_ERROR;
        }

        // Write out a final status message to make it easy to see validation success/failure
        m.logStatusMessage(exitStatus);

        // almost certainly will get flagged during code-scan, but this is intentional,
        // as we genuinely want to exit with the correct status here. The code-scan tool
        // really ought to be able to see that this is a main function in a J2SE environment
        System.exit(exitStatus);
    }
    
    /**
     * Check if data exists for V0030(Evidence or EvidenceVariable resource instances).
     *
     * @return true, if successful
     */
    private boolean checkIfDataExistsForV0030() {

        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                return checkIfDataExistsForV0030(adapter, schema.getSchemaName());
            } catch (DataAccessException dae) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw dae;
            }
        }
        
    }

    
    /**
     * Check if data exists for V0030(Evidence or EvidenceVariable resource instances).
     *
     * @param adapter the database adapter
     * @param schemaName the schema containing the FHIR data tables
     */
    private boolean checkIfDataExistsForV0030(IDatabaseAdapter adapter, String schemaName) {
        TableHasData cmd = new TableHasData(schemaName, "evidence_logical_resources", adapter);
        if (adapter.runStatement(cmd)) {
            logger.severe("At least one Evidence resource exists. Cannot upgrade " + schemaName);
            return true;
        }
        cmd = new TableHasData(schemaName, "evidencevariable_logical_resources", adapter);
        if (adapter.runStatement(cmd)) {
            logger.severe("At least one EvidenceVariable resource exists. Cannot upgrade " + schemaName);
            return true;
        }
        return false;
    }
}