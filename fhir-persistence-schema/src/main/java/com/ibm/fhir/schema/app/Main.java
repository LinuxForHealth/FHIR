/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import static com.ibm.fhir.schema.app.menu.Menu.ADD_TENANT_KEY;
import static com.ibm.fhir.schema.app.menu.Menu.ALLOCATE_TENANT;
import static com.ibm.fhir.schema.app.menu.Menu.CHECK_COMPATIBILITY;
import static com.ibm.fhir.schema.app.menu.Menu.CONFIRM_DROP;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMAS;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_BATCH;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_FHIR;
import static com.ibm.fhir.schema.app.menu.Menu.CREATE_SCHEMA_OAUTH;
import static com.ibm.fhir.schema.app.menu.Menu.DB_TYPE;
import static com.ibm.fhir.schema.app.menu.Menu.DELETE_TENANT_META;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_ADMIN;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_DETACHED;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_BATCH;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_FHIR;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_SCHEMA_OAUTH;
import static com.ibm.fhir.schema.app.menu.Menu.DROP_TENANT;
import static com.ibm.fhir.schema.app.menu.Menu.FORCE;
import static com.ibm.fhir.schema.app.menu.Menu.FORCE_UNUSED_TABLE_REMOVAL;
import static com.ibm.fhir.schema.app.menu.Menu.FREEZE_TENANT;
import static com.ibm.fhir.schema.app.menu.Menu.GRANT_TO;
import static com.ibm.fhir.schema.app.menu.Menu.HELP;
import static com.ibm.fhir.schema.app.menu.Menu.LIST_TENANTS;
import static com.ibm.fhir.schema.app.menu.Menu.POOL_SIZE;
import static com.ibm.fhir.schema.app.menu.Menu.PROP;
import static com.ibm.fhir.schema.app.menu.Menu.PROP_FILE;
import static com.ibm.fhir.schema.app.menu.Menu.REFRESH_TENANTS;
import static com.ibm.fhir.schema.app.menu.Menu.REVOKE_ALL_TENANT_KEYS;
import static com.ibm.fhir.schema.app.menu.Menu.REVOKE_TENANT_KEY;
import static com.ibm.fhir.schema.app.menu.Menu.SCHEMA_NAME;
import static com.ibm.fhir.schema.app.menu.Menu.SHOW_DB_SIZE;
import static com.ibm.fhir.schema.app.menu.Menu.SHOW_DB_SIZE_DETAIL;
import static com.ibm.fhir.schema.app.menu.Menu.SKIP_ALLOCATE_IF_TENANT_EXISTS;
import static com.ibm.fhir.schema.app.menu.Menu.TARGET;
import static com.ibm.fhir.schema.app.menu.Menu.TENANT_KEY;
import static com.ibm.fhir.schema.app.menu.Menu.TENANT_KEY_FILE;
import static com.ibm.fhir.schema.app.menu.Menu.TENANT_NAME;
import static com.ibm.fhir.schema.app.menu.Menu.TEST_TENANT;
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
import static com.ibm.fhir.schema.app.util.CommonUtil.getRandomKey;
import static com.ibm.fhir.schema.app.util.CommonUtil.loadDriver;
import static com.ibm.fhir.schema.app.util.CommonUtil.logClasspath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
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
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.TableSpaceRemovalException;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.api.UndefinedNameException;
import com.ibm.fhir.database.utils.api.UniqueConstraintViolationException;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2GetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.postgres.GatherTablesDataModelVisitor;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.database.utils.postgres.PostgresVacuumSettingDAO;
import com.ibm.fhir.database.utils.schema.LeaseManager;
import com.ibm.fhir.database.utils.schema.LeaseManagerConfig;
import com.ibm.fhir.database.utils.schema.SchemaVersionsManager;
import com.ibm.fhir.database.utils.tenant.AddTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.DeleteTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.GetTenantDAO;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.database.utils.version.CreateControl;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.CreateWholeSchemaVersion;
import com.ibm.fhir.database.utils.version.SchemaConstants;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.schema.app.menu.Menu;
import com.ibm.fhir.schema.app.util.TenantKeyFileUtil;
import com.ibm.fhir.schema.control.BackfillResourceChangeLog;
import com.ibm.fhir.schema.control.BackfillResourceChangeLogDb2;
import com.ibm.fhir.schema.control.DisableForeignKey;
import com.ibm.fhir.schema.control.DropForeignKey;
import com.ibm.fhir.schema.control.EnableForeignKey;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.FhirSchemaVersion;
import com.ibm.fhir.schema.control.GetLogicalResourceNeedsV0014Migration;
import com.ibm.fhir.schema.control.GetResourceChangeLogEmpty;
import com.ibm.fhir.schema.control.GetResourceTypeList;
import com.ibm.fhir.schema.control.GetTenantInfo;
import com.ibm.fhir.schema.control.GetTenantList;
import com.ibm.fhir.schema.control.GetXXLogicalResourceNeedsMigration;
import com.ibm.fhir.schema.control.InitializeLogicalResourceDenorms;
import com.ibm.fhir.schema.control.JavaBatchSchemaGenerator;
import com.ibm.fhir.schema.control.MigrateV0014LogicalResourceIsDeletedLastUpdated;
import com.ibm.fhir.schema.control.MigrateV0021AbstractTypeRemoval;
import com.ibm.fhir.schema.control.OAuthSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;
import com.ibm.fhir.schema.control.SetTenantIdDb2;
import com.ibm.fhir.schema.control.TenantInfo;
import com.ibm.fhir.schema.control.UnusedTableRemovalNeedsV0021Migration;
import com.ibm.fhir.schema.model.ResourceType;
import com.ibm.fhir.schema.model.Schema;
import com.ibm.fhir.schema.size.Db2SizeCollector;
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
    public List<DbType> MULTITENANT_FEATURE_ENABLED = Arrays.asList(DbType.DB2);
    public List<DbType> STORED_PROCEDURE_ENABLED = Arrays.asList(DbType.DB2, DbType.POSTGRESQL);
    public List<DbType> PRIVILEGES_FEATURE_ENABLED = Arrays.asList(DbType.DB2, DbType.POSTGRESQL);

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

    // Tenant Key
    private boolean skipIfTenantExists = false;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // Action flag related to Vacuuming:
    private boolean updateVacuum = false;
    private String vacuumTableName = null;
    private int vacuumCostLimit = 2000;
    private int vacuumThreshold = 1000;
    private Double vacuumScaleFactor = null;

    // How many seconds to wait to obtain the update lease
    private int waitForUpdateLeaseSeconds = 10;

    // The database type being populated (default: Db2)
    private DbType dbType = DbType.DB2;
    private IDatabaseTranslator translator = new Db2Translator();

    // Optional subset of resource types (for faster schema builds when testing)
    private Set<String> resourceTypeSubset;

    // Tenant management
    private boolean allocateTenant;
    private boolean refreshTenants;
    private boolean dropTenant;
    private boolean freezeTenant;
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;
    private boolean listTenants;
    private boolean dropDetached;
    private boolean deleteTenantMeta;
    private boolean revokeTenantKey;
    private boolean revokeAllTenantKeys;

    // Forces the removal of tables if data exists
    private boolean forceUnusedTableRemoval = false;

    // Force schema update even if whole-schema-version is current
    private boolean force = false;
    
    // Report on database size metrics
    private boolean showDbSize;
    
    // Include detail output in the report (default is no)
    private boolean showDbSizeDetail = false;

    // Tenant Key Output or Input File
    private String tenantKeyFileName;
    private TenantKeyFileUtil tenantKeyFileUtil = new TenantKeyFileUtil();

    // The tenant name for when we want to add a new tenant key
    private String addKeyForTenant;

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
    }

    /**
     * Add the admin schema objects to the {@link PhysicalDataModel}
     *
     * @param pdm the data model to build
     */
    protected void buildAdminSchemaModel(PhysicalDataModel pdm) {
        // Add the tenant and tenant_keys tables and any other admin schema stuff
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant());
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
    protected void applyModel(PhysicalDataModel pdm, IDatabaseAdapter adapter, ITaskCollector collector, VersionHistoryService vhs) {
        logger.info("Collecting model update tasks");
        pdm.collect(collector, adapter, this.transactionProvider, vhs);

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
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            CreateControl.createTableIfNeeded(schema.getAdminSchemaName(), adapter);
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
            gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant());
        } else {
            gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant(), resourceTypeSubset);
        }

        gen.buildSchema(pdm);
        switch (dbType) {
        case DB2:
            gen.buildDatabaseSpecificArtifactsDb2(pdm);
            break;
        case DERBY:
            logger.info("No database specific artifacts");
            break;
        case POSTGRESQL:
            gen.buildDatabaseSpecificArtifactsPostgres(pdm);
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
            IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, adapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                // Build/update the FHIR-related tables as well as the stored procedures
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildFhirDataSchemaModel(pdm);
                boolean isNewDb = updateSchema(pdm);

                if (this.exitStatus == EXIT_OK) {
                    // If the db is multi-tenant, we populate the resource types and parameter names in allocate-tenant.
                    // Otherwise, if its a new schema, populate the resource types and parameters names (codes) now
                    if (!MULTITENANT_FEATURE_ENABLED.contains(dbType) && isNewDb) {
                        populateResourceTypeAndParameterNameTableEntries(null);
                    }

                    if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
                        logger.info("Refreshing tenant partitions");
                        refreshTenants();
                    }

                    // backfill the resource_change_log table if needed
                    backfillResourceChangeLog();

                    // perform any updates we need related to the V0010 schema change (IS_DELETED flag)
                    applyDataMigrationForV0010();

                    // V0014 IS_DELETED and LAST_UPDATED added to whole-system LOGICAL_RESOURCES
                    applyDataMigrationForV0014();

                    // V0021 removes Abstract Type tables which are unused.
                    applyTableRemovalForV0021();

                    // Apply privileges if asked
                    if (grantTo != null) {
                        grantPrivilegesForFhirData();
                    }

                    // Finally, update the whole schema version
                    svm.updateSchemaVersion();
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
            IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, adapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildOAuthSchemaModel(pdm);
                updateSchema(pdm);

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
            IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
            try (ITransaction tx = transactionProvider.getTransaction()) {
                CreateWholeSchemaVersion.createTableIfNeeded(targetSchemaName, adapter);
            }

            // If our schema is already at the latest version, we can skip a lot of processing
            SchemaVersionsManager svm = new SchemaVersionsManager(translator, connectionPool, transactionProvider, targetSchemaName,
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());
            if (svm.isSchemaOld() || this.force && svm.isSchemaVersionMatch()) {
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildJavaBatchSchemaModel(pdm);
                updateSchema(pdm);

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
    protected boolean updateSchema(PhysicalDataModel pdm) {

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        logger.info("Connection pool size: " + this.maxConnectionPoolSize);
        logger.info("    Thread pool size: " + this.threadPoolSize);
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.threadPoolSize);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);

        // Before we start anything, we need to make sure our schema history
        // and control tables are in place. These tables are used to manage
        // all FHIR data, oauth and JavaBatch schemas we build
        try (ITransaction tx = transactionProvider.getTransaction()) {
            CreateVersionHistory.createTableIfNeeded(schema.getAdminSchemaName(), adapter);
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

        applyModel(pdm, adapter, collector, vhs);
        // The physical database objects should now match what was defined in the PhysicalDataModel

        return isNewDb;
    }

    /**
     * populates for the given tenantId the RESOURCE_TYPE table.
     *
     * @implNote if you update this method, be sure to update
     *           DerbyBootstrapper.populateResourceTypeAndParameterNameTableEntries
     *           and DerbyFhirDatabase.populateResourceTypeAndParameterNameTableEntries
     *           The reason is there are three different ways of managing the transaction.
     * @param tenantId
     *            the mt_id that is used to setup the partition.
     *            passing in null signals not multi-tenant.
     */
    protected void populateResourceTypeAndParameterNameTableEntries(Integer tenantId) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                String logTenantId = tenantId != null ? Integer.toString(tenantId) : "default";
                logger.info("tenantId [" + logTenantId + "] is being pre-populated with lookup table data.");
                PopulateResourceTypes populateResourceTypes =
                        new PopulateResourceTypes(schema.getAdminSchemaName(), schema.getSchemaName(), tenantId);
                populateResourceTypes.run(translator, c);

                PopulateParameterNames populateParameterNames =
                        new PopulateParameterNames(schema.getAdminSchemaName(), schema.getSchemaName(), tenantId);
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
        PhysicalDataModel pdm = new PhysicalDataModel();
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
                    VersionHistoryService vhs =
                            new VersionHistoryService(schema.getAdminSchemaName(), schema.getSchemaName(), schema.getOauthSchemaName(), schema.getJavaBatchSchemaName());
                    vhs.setTransactionProvider(transactionProvider);
                    vhs.setTarget(adapter);

                    if (dropFhirSchema) {
                        // Just drop the objects associated with the FHIRDATA schema group
                        final String schemaName = schema.getSchemaName();
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
                        CreateWholeSchemaVersion.dropTable(schemaName, adapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropOauthSchema) {
                        // Just drop the objects associated with the OAUTH schema group
                        final String schemaName = schema.getOauthSchemaName();
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, OAuthSchemaGenerator.OAUTH_GROUP);
                        CreateWholeSchemaVersion.dropTable(schemaName, adapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropJavaBatchSchema) {
                        // Just drop the objects associated with the BATCH schema group
                        final String schemaName = schema.getJavaBatchSchemaName();
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, JavaBatchSchemaGenerator.BATCH_GROUP);
                        CreateWholeSchemaVersion.dropTable(schemaName, adapter);
                        if (!checkSchemaIsEmpty(adapter, schemaName)) {
                            throw new DataAccessException("Schema '" + schemaName + "' not empty after drop");
                        }
                        vhs.clearVersionHistory(schemaName);
                    }

                    if (dropAdmin) {
                        // Just drop the objects associated with the ADMIN schema group
                        CreateVersionHistory.generateTable(pdm, ADMIN_SCHEMANAME, true);
                        CreateControl.buildTableDef(pdm, ADMIN_SCHEMANAME, true);
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
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
     * performed in a second transaction.
     *
     * @param pdm
     */
    private void dropForeignKeyConstraints(PhysicalDataModel pdm, String tagGroup, String tag) {
        try (Connection c = createConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                Set<Table> referencedTables = new HashSet<>();
                DropForeignKey dropper = new DropForeignKey(adapter, referencedTables);
                pdm.visit(dropper, tagGroup, tag);
            } catch (Exception x) {
                c.rollback();
                throw x;
            }
            c.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
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
        PhysicalDataModel pdm = new PhysicalDataModel();
        // Since this is a stored procedure, we need the model.
        // We must pass in true to flag to the underlying layer that the
        // Procedures need to be generated.
        buildCommonModel(pdm, true, updateOauthSchema, updateJavaBatchSchema);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                try {
                    IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
                    pdm.applyProcedures(adapter);
                    pdm.applyFunctions(adapter);

                    // Because we're replacing the procedures, we should also check if
                    // we need to apply the associated privileges
                    if (this.grantTo != null) {
                        pdm.applyProcedureAndFunctionGrants(adapter, FhirSchemaConstants.FHIR_USER_GRANT_GROUP, grantTo);
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

    /**
     * Apply grants to the FHIR data schema objects
     */
    protected void grantPrivilegesForFhirData() {

        final IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildFhirDataSchemaModel(pdm);
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_USER_GRANT_GROUP, grantTo);

                // Grant SELECT on WHOLE_SCHEMA_VERSION to the FHIR server user
                // Note the constant comes from SchemaConstants on purpose
                CreateWholeSchemaVersion.grantPrivilegesTo(adapter, schema.getSchemaName(), SchemaConstants.FHIR_USER_GRANT_GROUP, grantTo);
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
        final IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildOAuthSchemaModel(pdm);
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_OAUTH_GRANT_GROUP, grantTo);

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
        final IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                PhysicalDataModel pdm = new PhysicalDataModel();
                buildJavaBatchSchemaModel(pdm);
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, grantTo);

                // special case for the JavaBatch schema in PostgreSQL
                adapter.grantAllSequenceUsage(schema.getJavaBatchSchemaName(), grantTo);

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
            grantPrivilegesForFhirData();
        }

        if (grantOauthSchema) {
            grantPrivilegesForOAuth();
        }

        if (grantJavaBatchSchema) {
            grantPrivilegesForBatch();
        }
    }

    /**
     * Do we want to build the multitenant variant of the schema (currently only supported
     * by DB2)
     *
     * @return
     */
    protected boolean isMultitenant() {
        return MULTITENANT_FEATURE_ENABLED.contains(this.dbType);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // The following methods are related to Multi-Tenant only.
    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     */
    protected void addTenantKey() {
        if (!isMultitenant()) {
            return;
        }

        // Only if the Tenant Key file is provided as a parameter is it not null.
        // in this case we want special behavior.
        if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        } else {
            tenantKey = getRandomKey();
        }

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = getRandomKey();

        Db2Adapter adapter = new Db2Adapter(connectionPool);
        checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey, false);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantDAO tid = new GetTenantDAO(schema.getAdminSchemaName(), addKeyForTenant);
                Tenant tenant = adapter.runStatement(tid);

                if (tenant != null) {
                    // Attach the new tenant key to the tenant:
                    AddTenantKeyDAO adder =
                            new AddTenantKeyDAO(schema.getAdminSchemaName(), tenant.getTenantId(), tenantKey, tenantSalt, FhirSchemaConstants.TENANT_SEQUENCE);
                    adapter.runStatement(adder);
                } else {
                    throw new IllegalArgumentException("Tenant does not exist: " + addKeyForTenant);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        if (tenantKeyFileName == null) {
            // Generated
            logger.info("New tenant key: " + addKeyForTenant + " [key=" + tenantKey + "]");
        } else {
            // Loaded from File
            logger.info("New tenant key from file: " + addKeyForTenant + " [tenantKeyFileName=" + tenantKeyFileName + "]");
            if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
            }
        }

    }

    /**
     * checks if tenant name and tenant key exists.
     *
     * @param adapter
     *            the db2 adapter as this is a db2 feature only now
     * @param tenantName
     *            the tenant's name
     * @param tenantKey
     *            tenant key
     * @param skip
     *            whether or not to skip over cases where this tenantName/tenantKey combination already exists
     *
     * @throws IllegalArgumentException if the tenantName/tenantKey combination already exists and the {@code skip} argument is false
     *
     * @return indicates if the tenantName/tenantKey exists
     */
    protected boolean checkIfTenantNameAndTenantKeyExists(Db2Adapter adapter, String tenantName, String tenantKey, boolean skip) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                final String sql =
                        "SELECT t.tenant_status FROM fhir_admin.tenants t WHERE t.tenant_name = ? "
                                + "AND EXISTS (SELECT 1 FROM fhir_admin.tenant_keys tk WHERE tk.mt_id = t.mt_id "
                                + "AND tk.tenant_hash = sysibm.hash(tk.tenant_salt || ?, 2))";
                try (PreparedStatement stmt = connectionPool.getConnection().prepareStatement(sql)) {
                    stmt.setString(1, tenantName);
                    stmt.setString(2, tenantKey);
                    if (stmt.execute()) {
                        try (ResultSet resultSet = stmt.getResultSet();) {
                            if (resultSet.next()) {
                                if (skip) {
                                    return true;
                                } else {
                                    throw new IllegalArgumentException("tenantName and tenantKey already exists");
                                }
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Problem checking the results");
                    }
                } catch (SQLException e) {
                    throw new IllegalArgumentException("Exception when querying backend to verify tenant key and tenant name", e);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
        return false;
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     */
    protected void allocateTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Make sure only a single instance of this is running on a given schema
        LeaseManager leaseManager = new LeaseManager(this.translator, connectionPool, transactionProvider, schema.getAdminSchemaName(), schema.getSchemaName(),
            leaseManagerConfig);

        try {
            if (!leaseManager.waitForLease(waitForUpdateLeaseSeconds)) {
                throw new ConcurrentUpdateException("Concurrent update (allocate-tenant) for FHIR data schema: '" + schema.getSchemaName() + "'");
            }

            // IMPORTANT! Check the schema name aligns with the actual schema for this tenant
            checkSchemaForTenant();

            // The key we'll use for this tenant. This key should be used in subsequent
            // activities related to this tenant, such as setting the tenant context.
            if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                // Only if the Tenant Key file is provided as a parameter is it not null.
                // in this case we want special behavior.
                tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
            } else {
                tenantKey = getRandomKey();
            }

            // The salt is used when we hash the tenantKey. We're just using SHA-256 for
            // the hash here, not multiple rounds of a password hashing algorithm. It's
            // sufficient in our case because we are using a 32-byte random value as the
            // key, giving 256 bits of entropy.
            final String tenantSalt = getRandomKey();

            Db2Adapter adapter = new Db2Adapter(connectionPool);

            // Conditionally skip if the tenant name and key exist (this enables idempotency)
            boolean skip = checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey, skipIfTenantExists);
            if (skip) {
                return;
            }

            if (tenantKeyFileName == null) {
                logger.info("Allocating new tenant: " + tenantName + " [key=" + tenantKey + "]");
            } else {
                logger.info("Allocating new tenant: " + tenantName + " [tenantKeyFileName=" + tenantKeyFileName + "]");
            }

            // Open a new transaction and associate it with our connection pool. Remember
            // that we don't support distributed transactions, so all connections within
            // this transaction must come from the same pool
            int tenantId;
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    tenantId =
                            adapter.allocateTenant(schema.getAdminSchemaName(), schema.getSchemaName(), tenantName, tenantKey, tenantSalt, FhirSchemaConstants.TENANT_SEQUENCE);

                    // The tenant-id is important because this is also used to identify the partition number
                    logger.info("Tenant Id[" + tenantName + "] = [" + tenantId + "]");
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }

            // Build/update the tables as well as the stored procedures
            FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant());
            PhysicalDataModel pdm = new PhysicalDataModel();
            gen.buildSchema(pdm);

            // Get the data model to create the table partitions. This is threaded, so transactions are
            // handled within each thread by the adapter. This means we should probably pull some of
            // that logic out of the adapter and handle it at a higher level. Note...the extent size used
            // for the partitions needs to match the extent size of the original table tablespace (FHIR_TS)
            // so this must be constant.
            pdm.addTenantPartitions(adapter, schema.getSchemaName(), tenantId, FhirSchemaConstants.FHIR_TS_EXTENT_KB);

            // Fill any static data tables (which are also partitioned by tenant)
            // Prepopulate the Resource Type Tables and Parameters Name/Code Table
            populateResourceTypeAndParameterNameTableEntries(tenantId);

            // Now all the table partitions have been allocated, we can mark the tenant as ready
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    adapter.updateTenantStatus(schema.getAdminSchemaName(), tenantId, TenantStatus.ALLOCATED);
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }

            if (grantTo != null) {
                // If the --grant-to has been given, we also need to apply that here, although
                // this really ought to be done when the schema is first built
                grantPrivileges();
            }

            if (tenantKeyFileName == null) {
                logger.info("Allocated tenant: " + tenantName + " [key=" + tenantKey + "] with Id = " + tenantId);
                logger.info("The tenantKey JSON follows: \t\n{\"tenantKey\": \"" + tenantKey + "\"}");
            } else {
                logger.info("Allocated tenant: " + tenantName + " [tenantKeyFileName=" + tenantKeyFileName + "] with Id = "
                        + tenantId);
                if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                    tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
                }
            }
        } finally {
            leaseManager.cancelLease();
        }
    }

    /**
     * Run a check to make sure that if the tenant already exists its schema
     * matches the specified schema. This prevents users from accidentally
     * creating a second instance of a tenant in a different schema
     */
    protected void checkSchemaForTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // only relevant for databases which support multiple tenants within one schema (like Db2)
            return;
        }
        List<TenantInfo> tenants = getTenantList();

        // Scan over the list to see if the tenant we are working with
        // already exists
        for (TenantInfo ti : tenants) {
            if (ti.getTenantName().equals(this.tenantName)) {
                if (ti.getTenantSchema() == null) {
                    // The schema is empty so no chance of adding tenants
                    throw new IllegalArgumentException("Schema '" + schema.getSchemaName() + "'"
                            + " for tenant '" + ti.getTenantName() + "'"
                            + " does not contain a valid IBM FHIR Server schema");
                } else if (!ti.getTenantSchema().equalsIgnoreCase(schema.getSchemaName())) {
                    // The given schema name doesn't match where we think this tenant
                    // should be located, so throw an error before any damage is done
                    throw new IllegalArgumentException("--schema-name argument '" + schema.getSchemaName()
                            + "' does not match schema '" + ti.getTenantSchema() + "' for tenant '"
                            + ti.getTenantName() + "'");
                }
            }
        }
    }

    /**
     * Get the list of tenants and the schemas each is currently defined in. Because
     * this tenant-schema mapping isn't stored directly, it has to be inferred by
     * looking up the schema from one of the tables we know should exist. The schema
     * name may therefore be null if the tenant record (in FHIR_ADMIN.TENANTS) exists,
     * but all the tables from that schema have been dropped.
     *
     * @return
     */
    protected List<TenantInfo> getTenantList() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return Collections.emptyList();
        }

        // Similar to the allocate tenant processing, except in this case we want to
        // make sure that each table has all the tenant partitions required. This is
        // to handle the case where a schema update has added a new table.
        List<TenantInfo> tenants;
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantList rtListGetter = new GetTenantList(schema.getAdminSchemaName());
                tenants = adapter.runStatement(rtListGetter);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        return tenants;
    }

    /**
     * Make sure all the tables has a partition created for the configured tenant
     */
    protected void refreshTenants() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Similar to the allocate tenant processing, except in this case we want to
        // make sure that each table has all the tenant partitions required. This is
        // to handle the case where a schema update has added a new table.
        List<TenantInfo> tenants = getTenantList();

        // make sure the list is sorted by tenantId. Lambdas really do clean up this sort of code
        tenants.sort((TenantInfo left, TenantInfo right) -> left.getTenantId() < right.getTenantId() ? -1 : left.getTenantId() > right.getTenantId() ? 1 : 0);
        for (TenantInfo ti : tenants) {
            // For issue 1847 we only want to process for the named data schema if one is provided
            // If no -schema-name arg is given, we process all tenants.
            if (ti.getTenantSchema() != null && (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema()))) {
                // It's crucial we use the correct schema for each particular tenant, which
                // is why we have to build the PhysicalDataModel separately for each tenant
                FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), ti.getTenantSchema(), isMultitenant());
                PhysicalDataModel pdm = new PhysicalDataModel();
                gen.buildSchema(pdm);

                Db2Adapter adapter = new Db2Adapter(connectionPool);
                pdm.addNewTenantPartitions(adapter, ti.getTenantSchema(), ti.getTenantId());
            }
        }
    }

    /**
     * List the tenants currently configured
     */
    protected void listTenants() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantList rtListGetter = new GetTenantList(schema.getAdminSchemaName());
                List<TenantInfo> tenants = adapter.runStatement(rtListGetter);

                System.out.println(TenantInfo.getHeader());
                tenants.forEach(System.out::println);
            } catch (UndefinedNameException x) {
                System.out.println("The FHIR_ADMIN schema appears not to be deployed with the TENANTS table");
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Check that we can call the set_tenant procedure successfully (which means
     * that the
     * tenant record exists in the tenants table)
     */
    protected void testTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        if (this.tenantName == null || this.tenantName.isEmpty()) {
            throw new IllegalStateException("Missing tenant name");
        }

        // Part of Bring your own Tenant Key
        if (tenantKeyFileName != null) {
            // Only if the Tenant Key file is provided as a parameter is it not null.
            // in this case we want special behavior.
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        }

        if (this.tenantKey == null || this.tenantKey.isEmpty()) {
            throw new IllegalArgumentException("No tenant-key value provided");
        }

        logger.info("Testing tenant: [" + tenantName + "]");

        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // The tenants table, variable and set_tenant procedure are all located in
                // the admin schema. The data access user only has execute privileges on the
                // set_tenant procedure and read access to the variable. The variable can
                // only be set by calling the stored procedure
                Db2SetTenantVariable cmd = new Db2SetTenantVariable(schema.getAdminSchemaName(), tenantName, tenantKey);
                adapter.runStatement(cmd);

                Db2GetTenantVariable getter = new Db2GetTenantVariable(schema.getAdminSchemaName());
                Integer tid = adapter.runStatement(getter);
                if (tid == null) {
                    throw new IllegalStateException("SV_TENANT_ID not set!");
                }

                // Print the id from the session variable (used for access control)
                logger.info("tenantName='" + tenantName + "', tenantId=" + tid);

                // Now let's check we can run a select against one our tenant-based
                // tables
                GetResourceTypeList rtListGetter = new GetResourceTypeList(schema.getSchemaName());
                List<ResourceType> rtList = adapter.runStatement(rtListGetter);
                rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    protected TenantInfo getTenantInfo() {
        TenantInfo result;
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            throw new IllegalStateException("Not a multi-tenant database");
        }

        Db2Adapter adapter = new Db2Adapter(connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                GetTenantInfo command = new GetTenantInfo(schema.getAdminSchemaName(), tenantName);
                result = adapter.runStatement(command);

                if (result == null) {
                    logger.info("Use --list-tenants to display the current tenants");
                    throw new IllegalArgumentException("Tenant '" + tenantName + "' not found in admin schema " + schema.getAdminSchemaName());
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // make sure we set the schema name correctly if it couldn't be found in the database
        // (which happens after all the partitions for a particular tenant are detached)
        String tenantSchema = result.getTenantSchema();
        if (tenantSchema == null || tenantSchema.isEmpty()) {
            // the schema can no longer be derived from the database, so we
            // need it to be provided on the command line.
            if (schema.getSchemaName() == null || schema.getSchemaName().isEmpty()) {
                throw new IllegalArgumentException("Must provide the tenant schema with --schema-name");
            }
            result.setTenantSchema(schema.getSchemaName());
        } else {
            // if a schema name was provided on the command line, let's double-check it matches
            // the schema used for this tenant in the database
            if (!tenantSchema.equalsIgnoreCase(schema.getSchemaName())) {
                throw new IllegalArgumentException("--schema-name '" + schema.getSchemaName() + "' argument does not match tenant schema: '"
                        + tenantSchema + "'");
            }
        }

        return result;
    }

    /**
     * Mark the tenant so that it can no longer be accessed (this prevents
     * the SET_TENANT method from authenticating the tenantName/tenantKey
     * pair, so the SV_TENANT_ID variable never gets set).
     *
     * @return the TenantInfo associated with the tenant
     */
    protected TenantInfo freezeTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            throw new IllegalStateException("Not a multi-tenant database");
        }

        TenantInfo result = getTenantInfo();
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        logger.info("Marking tenant for drop: " + tenantName);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                // Mark the tenant as frozen before we proceed with dropping anything
                if (result.getTenantStatus() == TenantStatus.ALLOCATED) {
                    adapter.updateTenantStatus(schema.getAdminSchemaName(), result.getTenantId(), TenantStatus.FROZEN);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
        return result;
    }

    /**
     * Deallocate this tenant, dropping all the related partitions. This needs to be
     * idempotent because there are steps which must be run in separate transactions
     * (due to how Db2 handles detaching partitions). This is further complicated by
     * referential integrity constraints in the schema. See:
     * - https://www.ibm.com/support/knowledgecenter/SSEPGG_11.5.0/com.ibm.db2.luw.admin.partition.doc/doc/t0021576.html
     *
     * The workaround described in the above link is:
     * // Change the RI constraint to informational:
     * 1. ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
     *
     * 2. ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
     *
     * 3. SET INTEGRITY FOR child OFF;
     *
     * // Change the RI constraint back to enforced:
     * 4. ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
     *
     * 5. SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
     * 6. Assuming that the CHILD table does not have any dependencies on partition P0,
     * 7. and that no updates on the CHILD table are permitted until this UOW is complete,
     * no RI violation is possible during this UOW.
     *
     * COMMIT WORK;
     *
     * Unfortunately, #7 above essentially requires that all writes cease until the
     * UOW is completed and the integrity is enabled again on all the child (leaf)
     * tables. Of course, this could be relaxed if the application is trusted not
     * to mess up the referential integrity...which we know to be true for the
     * FHIR server persistence layer.
     *
     * If the risk is deemed too high, tenant removal should be performed in a
     * maintenance window.
     */
    protected void dropTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way because the SET_TENANT stored procedure
        // will reject any request for the tenant being dropped.
        TenantInfo tenantInfo = freezeTenant();

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), tenantInfo.getTenantSchema(), isMultitenant());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Detach the tenant partition from each of the data tables
        detachTenantPartitions(pdm, tenantInfo);

        // this may not complete successfully because Db2 runs the detach as an async
        // process. Just need to run --drop-detached to clean up.
        dropDetachedPartitionTables(pdm, tenantInfo);
    }

    /**
     * Drop any tables which have previously been detached. The detach process is asynchronous,
     * so this is a sort of garbage collection, sweeping up cruft left in the database.
     */
    protected void dropDetachedPartitionTables() {

        TenantInfo tenantInfo = getTenantInfo();
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), tenantInfo.getTenantSchema(), isMultitenant());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        dropDetachedPartitionTables(pdm, tenantInfo);
    }

    /**
     * Drop any tables which have previously been detached. Once all tables have been
     * dropped, we go on to drop the tablespace. If the tablespace drop is successful,
     * we know the cleanup is complete so we can update the tenant status accordingly
     *
     * @param pdm
     * @param tenantInfo
     */
    protected void dropDetachedPartitionTables(PhysicalDataModel pdm, TenantInfo tenantInfo) {
        // In a new transaction, drop any of the tables that were created by
        // the partition detach operation.
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                pdm.dropDetachedPartitions(adapter, tenantInfo.getTenantSchema(), tenantInfo.getTenantId());
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // We can drop the tenant's tablespace only after all the table drops have been committed
        logger.info("Dropping tablespace for tenant " + tenantInfo.getTenantId() + "/" + tenantName);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            boolean retry = true;
            int wait = 0;
            while (retry) {
                try {
                    retry = false;
                    // With all the objects removed, it should be safe to remove the tablespace
                    pdm.dropTenantTablespace(adapter, tenantInfo.getTenantId());
                } catch (DataAccessException x) {
                    boolean error = x instanceof DataAccessException && x.getCause() instanceof SQLException
                            && (((SQLException) x.getCause()).getErrorCode() == -282);
                    if (error && wait++ <= 30) {
                        retry = true;
                        logger.warning("Waiting on async dettach dependency to finish - count '" + wait + "'");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new DataAccessException(e);
                        }
                    } else if (error) {
                        throw new TableSpaceRemovalException(x.getCause());
                    } else {
                        // Something went wrong, so mark the transaction as failed
                        tx.setRollbackOnly();
                        throw x;
                    }
                }
            }
        }

        // Now all the table partitions have been allocated, we can mark the tenant as dropped
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                adapter.updateTenantStatus(schema.getAdminSchemaName(), tenantInfo.getTenantId(), TenantStatus.DROPPED);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Temporarily suspend RI so that tables which are the subject of foreign
     * key relationships can have their partitions dropped. A bit frustrating
     * to have to go through this, because the child table partition will be
     * detached before the parent anyway, so theoretically this workaround
     * shouldn't be necessary.
     * ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
     * ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
     * SET INTEGRITY FOR child OFF;
     * ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
     * SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
     */
    protected void detachTenantPartitions(PhysicalDataModel pdm, TenantInfo tenantInfo) {
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // collect the set of all child tables with FK relationships to
                // partitioned tables
                Set<Table> childTables = new HashSet<>();

                // ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
                pdm.visit(new DisableForeignKey(adapter, childTables));

                // ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
                pdm.detachTenantPartitions(adapter, tenantInfo.getTenantSchema(), tenantInfo.getTenantId());

                // SET INTEGRITY FOR child OFF;
                childTables.forEach(t -> adapter.setIntegrityOff(t.getSchemaName(), t.getObjectName()));

                // ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
                pdm.visit(new EnableForeignKey(adapter));

                // SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
                childTables.forEach(t -> adapter.setIntegrityUnchecked(t.getSchemaName(), t.getObjectName()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    /**
     * Delete all the metadata associated with the named tenant.
     */
    protected void deleteTenantMeta() {
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        TenantInfo tenantInfo = getTenantInfo();
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                if (tenantInfo.getTenantStatus() == TenantStatus.DROPPED) {
                    adapter.deleteTenantMeta(schema.getAdminSchemaName(), tenantInfo.getTenantId());
                } else {
                    throw new IllegalStateException("Cannot delete tenant meta data until status is " + TenantStatus.DROPPED.name());
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * revokes a tenant key or if no tenant key is specified remove all of them for the
     * given tenant.
     */
    protected void revokeTenantKey() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        TenantInfo tenantInfo = getTenantInfo();
        int tenantId = tenantInfo.getTenantId();

        // Only if the Tenant Key file is provided as a parameter is it not null.
        // in this case we want special behavior.
        if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        }

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                DeleteTenantKeyDAO dtk = new DeleteTenantKeyDAO(schema.getAdminSchemaName(), tenantId, tenantKey);

                Db2Adapter adapter = new Db2Adapter(connectionPool);
                adapter.runStatement(dtk);
                int count = dtk.getCount();
                logger.info("Tenant Key revoked for '" + tenantInfo.getTenantName() + "' total removed=[" + count + "]");
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
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
            case ADD_TENANT_KEY:
                if (++i < args.length) {
                    this.addKeyForTenant = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case REVOKE_TENANT_KEY:
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                this.tenantName = args[i];
                this.revokeTenantKey = true;
                break;
            case REVOKE_ALL_TENANT_KEYS:
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                this.tenantName = args[i];
                this.revokeAllTenantKeys = true;
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
            case TEST_TENANT:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.testTenant = true;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case TENANT_NAME:
                if (++i < args.length) {
                    this.tenantName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case TENANT_KEY:
                if (++i < args.length) {
                    this.tenantKey = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case TENANT_KEY_FILE:
                if (++i < args.length) {
                    tenantKeyFileName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case LIST_TENANTS:
                this.listTenants = true;
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
            case ALLOCATE_TENANT:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.allocateTenant = true;
                    this.dropTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case REFRESH_TENANTS:
                this.refreshTenants = true;
                this.allocateTenant = false;
                this.dropTenant = false;
                break;
            case DROP_TENANT:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.dropTenant = true;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case FREEZE_TENANT:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.freezeTenant = true;
                    this.dropTenant = false;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case DROP_DETACHED:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.dropDetached = true;
                    this.dropTenant = false;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case DELETE_TENANT_META:
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.deleteTenantMeta = true;
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
                case DB2:
                default:
                    break;
                }
                break;
            // Skips the allocateTenant action if the tenant exists (e.g. a shortcircuit)
            case SKIP_ALLOCATE_IF_TENANT_EXISTS:
                skipIfTenantExists = true;
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
        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // Process each tenant one-by-one
            List<TenantInfo> tenants = getTenantList();
            for (TenantInfo ti : tenants) {

                // If no --schema-name override was specified, we process all tenants, otherwise we
                // process only tenants which belong to the override schema name
                if (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema())) {
                    dataMigrationForV0010(ti);
                }
            }
        } else {
            doMigrationForV0010();
        }
    }

    protected void applyDataMigrationForV0014() {
        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // Process each tenant one-by-one
            List<TenantInfo> tenants = getTenantList();
            for (TenantInfo ti : tenants) {

                // If no --schema-name override was specified, we process all tenants, otherwise we
                // process only tenants which belong to the override schema name
                if (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema())) {
                    dataMigrationForV0014(ti);
                }
            }
        } else {
            dataMigrationForV0014();
        }
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
     * Migrate the IS_DELETED data for the given tenant
     *
     * @param ti
     */
    private void dataMigrationForV0010(TenantInfo ti) {
        // Multi-tenant schema so we know this is Db2:
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        Set<String> resourceTypes = getResourceTypes();

        // Process each update in its own transaction so we don't stress the tx log space
        for (String resourceTypeName : resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    SetTenantIdDb2 setTenantId = new SetTenantIdDb2(schema.getAdminSchemaName(), ti.getTenantId());
                    adapter.runStatement(setTenantId);

                    GetXXLogicalResourceNeedsMigration needsMigrating = new GetXXLogicalResourceNeedsMigration(schema.getSchemaName(), resourceTypeName);
                    if (adapter.runStatement(needsMigrating)) {
                        logger.info("V0010 Migration: Updating " + resourceTypeName + "_LOGICAL_RESOURCES.IS_DELETED "
                                + "for tenant '" + ti.getTenantName() + "', schema '" + ti.getTenantSchema() + "'");
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
     * Migrate the LOGICAL_RESOURCE IS_DELETED and LAST_UPDATED data for the given tenant
     *
     * @param ti
     */
    private void dataMigrationForV0014(TenantInfo ti) {
        // Multi-tenant schema so we know this is Db2:
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        List<ResourceType> resourceTypes = getResourceTypesList(adapter, schema.getSchemaName());

        // Process each update in its own transaction so we don't over-stress the tx log space
        for (ResourceType resourceType : resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    SetTenantIdDb2 setTenantId = new SetTenantIdDb2(schema.getAdminSchemaName(), ti.getTenantId());
                    adapter.runStatement(setTenantId);

                    logger.info("V0014 Migration: Updating " + "LOGICAL_RESOURCES.IS_DELETED and LAST_UPDATED for " + resourceType.toString()
                            + " for tenant '" + ti.getTenantName() + "', schema '" + ti.getTenantSchema() + "'");

                    dataMigrationForV0014(adapter, ti.getTenantSchema(), resourceType);
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
     * Backfill the RESOURCE_CHANGE_LOG table if it is empty
     */
    protected void backfillResourceChangeLog() {
        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // Process each tenant one-by-one
            List<TenantInfo> tenants = getTenantList();
            for (TenantInfo ti : tenants) {

                // If no --schema-name override was specified, we process all tenants, otherwise we
                // process only tenants which belong to the override schema name
                if (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema())) {
                    backfillResourceChangeLogDb2(ti);
                }
            }
        } else {
            // Not a multi-tenant database, so we only have to do this once
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
    }

    /**
     * backfill the resource_change_log table if it is empty
     */
    protected void backfillResourceChangeLogDb2(TenantInfo ti) {

        Db2Adapter adapter = new Db2Adapter(connectionPool);

        Set<String> resourceTypes = getResourceTypes();

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                SetTenantIdDb2 setTenantId = new SetTenantIdDb2(schema.getAdminSchemaName(), ti.getTenantId());
                adapter.runStatement(setTenantId);

                GetResourceChangeLogEmpty isEmpty = new GetResourceChangeLogEmpty(schema.getSchemaName());
                if (adapter.runStatement(isEmpty)) {
                    // change log is empty, so we need to backfill it with data
                    for (String resourceTypeName : resourceTypes) {
                        logger.info("Backfilling RESOURCE_CHANGE_LOG with " + resourceTypeName
                                + " resources for tenant '" + ti.getTenantName() + "', schema '" + ti.getTenantSchema() + "'");
                        BackfillResourceChangeLogDb2 backfill = new BackfillResourceChangeLogDb2(schema.getSchemaName(), resourceTypeName);
                        adapter.runStatement(backfill);
                    }
                } else {
                    logger.info("RESOURCE_CHANGE_LOG has data for tenant '" + ti.getTenantName() + "' so skipping backfill");
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
     * updates the vacuum settings for postgres.
     */
    public void updateVacuumSettings() {
        if (dbType != DbType.POSTGRESQL) {
            logger.severe("Updating the vacuum settings is only supported on postgres and the setting is for '" + dbType + "'");
            return;
        }

        // Create the Physical Data Model
        PhysicalDataModel pdm = new PhysicalDataModel();
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
        case DB2:
            collector = new Db2SizeCollector(model, this.tenantName);
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
        } else if (addKeyForTenant != null) {
            addTenantKey();
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
        } else if (this.allocateTenant) {
            allocateTenant();
        } else if (this.listTenants) {
            listTenants();
        } else if (this.refreshTenants) {
            refreshTenants();
        } else if (this.testTenant) {
            testTenant();
        } else if (this.freezeTenant) {
            freezeTenant();
        } else if (this.dropDetached) {
            dropDetachedPartitionTables();
        } else if (this.deleteTenantMeta) {
            deleteTenantMeta();
        } else if (this.dropTenant) {
            dropTenant();
        } else if (this.revokeTenantKey) {
            revokeTenantKey();
        } else if (this.revokeAllTenantKeys) {
            if (this.tenantKey != null) {
                throw new IllegalArgumentException("[ERROR] --tenant-key <key-value> should not be specified together with --drop-all-tenant-keys");
            }
            revokeTenantKey();
        } else if (grantTo != null) {
            // Finally, if --grant-to has been specified on its own, we simply rerun all the grants
            // which allows granting server to more than one user if that's required
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
}