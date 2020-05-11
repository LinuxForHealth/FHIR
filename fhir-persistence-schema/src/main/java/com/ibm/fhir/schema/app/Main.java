/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.LogFormatter;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2GetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlAdapter;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlPropertyAdapter;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlTranslator;
import com.ibm.fhir.database.utils.tenant.AddTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.GetTenantDAO;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.util.TenantKeyFileUtil;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.GetResourceTypeList;
import com.ibm.fhir.schema.control.OAuthSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;
import com.ibm.fhir.schema.model.ResourceType;
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
    private static final double NANOS = 1e9;

    // Indicates if the feature is enabled for the DbType
    public List<DbType> MULTITENANT_FEATURE_ENABLED = Arrays.asList(DbType.DB2);
    public List<DbType> STORED_PROCEDURE_ENABLED = Arrays.asList(DbType.DB2, DbType.POSTGRESQL);

    // Properties accumulated as we parse args and read configuration files
    private final Properties properties = new Properties();

    // The schema we will use for all the FHIR data tables
    private String schemaName;

    // The schema used for administration of tenants
    private String adminSchemaName = "FHIR_ADMIN";

    // The schema used for administration of OAuth 2.0 clients
    private String oauthSchemaName = "FHIR_OAUTH";

    // Arguments requesting we drop the objects from the schema
    private boolean dropSchema = false;
    private boolean dropAdmin = false;
    private boolean confirmDrop = false;
    private boolean updateProc = false;
    private boolean checkCompatibility = false;
    private boolean createFhirSchema = false;
    private boolean updateFhirSchema = false;
    private boolean createOauthSchema = false;
    private boolean updateOauthSchema = false;

    // By default, the dryRun option is OFF, and FALSE
    // When overridden, it simulates the actions.
    private Boolean dryRun = false;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // The database type being populated (default: Db2)
    private DbType dbType = DbType.DB2;
    private IDatabaseTranslator translator = new Db2Translator();

    // Tenant management
    private boolean allocateTenant;
    private boolean dropTenant;
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;

    // Tenant Key Output or Input File
    private String tenantKeyFileName;
    private TenantKeyFileUtil tenantKeyFileUtil = new TenantKeyFileUtil();

    // The tenant name for when we want to add a new tenant key
    private String addKeyForTenant;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    // The connection pool and transaction provider to support concurrent operations
    private int maxConnectionPoolSize = FhirSchemaConstants.DEFAULT_POOL_SIZE;
    private PoolConnectionProvider connectionPool;
    private ITransactionProvider transactionProvider;

    // Random generator for new tenant keys and salts
    private static final SecureRandom random = new SecureRandom();

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
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case to avoid tricky-to-catch errors related to quoting names
                    //this.schemaName = args[i].toUpperCase();
                    this.schemaName = args[i];

                    if (!schemaName.equals(args[i])) {
                        logger.info("Schema name forced to upper case: " + schemaName);
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--grant-to":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    this.grantTo = args[i].toUpperCase();
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--add-tenant-key":
                if (++i < args.length) {
                    this.addKeyForTenant = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-proc":
                this.updateProc = true;
                break;
            case "--check-compatibility":
                this.checkCompatibility = true;
                break;
            case "--drop-admin":
                this.dropAdmin = true;
                break;
            case "--test-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.testTenant = true;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key":
                if (++i < args.length) {
                    this.tenantKey = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key-file":
                if (++i < args.length) {
                    tenantKeyFileName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-schema":
                this.updateFhirSchema = true;
                this.dropSchema = false;
                this.updateOauthSchema = true;
                break;
            case "--create-schemas":
                this.createFhirSchema = true;
                this.createOauthSchema = true;
                break;
            case "--drop-schema":
                this.updateFhirSchema = false;
                this.dropSchema = true;
                break;
            case "--pool-size":
                if (++i < args.length) {
                    this.maxConnectionPoolSize = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--prop":
                if (++i < args.length) {
                    // properties are given as name=value
                    addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--confirm-drop":
                this.confirmDrop = true;
                break;
            case "--allocate-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.allocateTenant = true;
                    this.dropTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--drop-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.dropTenant = true;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--dry-run":
                this.dryRun = Boolean.TRUE;
                break;
            case "--db-type":
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                switch (dbType) {
                case DERBY:
                    translator = new DerbyTranslator();
                    // For some reason, embedded derby deadlocks if we use multiple threads
                    maxConnectionPoolSize = 1;
                    break;
                case POSTGRESQL:
                    translator = new PostgreSqlTranslator();
                    break;
                case DB2:
                default:
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
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
            properties.put(kv[0], kv[1]);
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

    /***
     * prints a brief menu to the standard out showing the usage.
     */
    public static void printUsage() {
        PrintStream ps = System.err;
        ps.println("Usage: ");

        // Properties File
        ps.println("--prop-file path-to-property-file");
        ps.println(" * loads the properties from a file");

        // Schema Name
        ps.println("--schema-name schema-name");
        ps.println(" * uses the schema as specified, must be valid.");

        // Grant Permissions to a valid username
        ps.println("--grant-to username");
        ps.println(" * uses the user as specified, must be valid.");
        ps.println(" * and grants permission to the username");

        // Add Tenant Key
        ps.println("--add-tenant-key tenant-key");
        ps.println(" * adds a tenant-key");

        // Updates the Sotred Procedure for a Tenant
        ps.println("--update-proc");
        ps.println(" * updates the stored procedure for a specific tenant");

        // Checks feature compatiblility
        ps.println("--check-compatibility");
        ps.println(" * checks feature compatibility ");

        // Drop the Admin Schema
        ps.println("--drop-admin");
        ps.println(" * drops the admin schema ");

        // Test Tenant
        ps.println("--test-tenant tenantName");
        ps.println(" * used to test with tenantName");

        // Tenant Key
        ps.println("--tenant-key tenantKey");
        ps.println(" * uses the tenant-key in the queries");

        // Tenant Key File
        ps.println("--tenant-key-file tenant-key-file-location");
        ps.println(" * sets the tenant key file location");

        // Update Schema action
        ps.println("--update-schema");
        ps.println(" * action to update the schema ");

        // Create Schema action
        ps.println("--create-schemas");
        ps.println(" * action to create the schema ");

        // Drop Schema action
        ps.println("--drop-schema");
        ps.println(" * action to drop the schema ");

        // Uses a specified poolsize
        ps.println("--pool-size poolSize");
        ps.println(" * poolsize used with the database actions ");

        // Property used to connect
        ps.println("--prop name=value");
        ps.println(" * name=value that is passed in on the commandline  ");

        // Confirms dropping of the schema
        ps.println("--confirm-drop");
        ps.println(" * confirms the dropping of a schema");

        // Allocates Tenant
        ps.println("--allocate-tenant");
        ps.println(" * allocates a tenant");

        // Drops a Tenant
        ps.println("--drop-tenant tenantName");
        ps.println(" * drops the tenant given the tenantName");

        // Dry Run functionality
        ps.println("--dry-run ");
        ps.println(" * simulates the actions of the actions that change the datastore");
    }

    /**
     * Set up the logger using the log.dir system property
     */
    protected void configureLogger() {
        final String logDirectory = System.getProperty("log.dir");
        if (logDirectory == null || logDirectory.isEmpty()) {
            configureLogger(".");
        } else {
            configureLogger(logDirectory);
        }
    }

    /**
     * Configure the logger to use the given directory.
     *
     * @param logDir
     */
    protected void configureLogger(final String logDir) {
        File f = new File(logDir, "fhirschema.log");
        LogFormatter.init(f.getPath());
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
        case EXIT_VALIDATION_FAILED:
            logger.warning("SCHEMA CHANGE: FAILED");
            break;
        default:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        }
    }

    /**
     * Drop all the objects in the admin and data schemas. Typically used
     * during development.
     */
    protected void dropSchema() {

        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(target);

                    if (this.dropSchema) {
                        // Just drop the objects associated with the FHIRDATA schema group
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
                    }

                    if (dropAdmin) {
                        // Just drop the objects associated with the ADMIN schema group
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
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

    private JdbcPropertyAdapter getPropertyAdapter(Properties props) {
        switch (dbType) {
        case DB2:
            return new Db2PropertyAdapter(props);
        case DERBY:
            return new DerbyPropertyAdapter(props);
        case POSTGRESQL:
            return new PostgreSqlPropertyAdapter(props);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    private IDatabaseAdapter getDbAdapter(JdbcTarget target) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(target);
        case DERBY:
            return new DerbyAdapter(target);
        case POSTGRESQL:
            return new PostgreSqlAdapter(target);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    private IDatabaseAdapter getDbAdapter(IConnectionProvider connectionProvider) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(connectionProvider);
        case DERBY:
            return new DerbyAdapter(connectionProvider);
        case POSTGRESQL:
            return new PostgreSqlAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    /**
     * Update the schema
     */
    protected void updateSchema() {
        // Build/update the FHIR-related tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Build/update the Liberty OAuth-related tables
        if (updateOauthSchema) {
            OAuthSchemaGenerator oauthSchemaGenerator = new OAuthSchemaGenerator(oauthSchemaName);
            oauthSchemaGenerator.buildOAuthSchema(pdm);
        }

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.maxConnectionPoolSize);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        IDatabaseAdapter adapter = getDbAdapter(connectionPool);

        // Before we start anything, we need to make sure our schema history
        // tables are in place. There's only a single history table, which
        // resides in the admin schema and handles the history of all objects
        // in any schema being managed.
        CreateVersionHistory.createTableIfNeeded(adminSchemaName, adapter);

        // Current version history for the data schema
        VersionHistoryService vhs = new VersionHistoryService(adminSchemaName, schemaName, oauthSchemaName);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();

        // Use the version history service to determine if this table existed before we run `applyWithHistory`
        boolean newDb = vhs.getVersion(schemaName, DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == null || 
                vhs.getVersion(schemaName, DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == 0;

        applyModel(pdm, adapter, collector, vhs);
        // There is a working data model at this point.

        // If the db is multi-tenant, we populate the resource types and parameter names in allocate-tenant.
        // Otherwise, if its a new schema, populate the resource types and parameters names (codes) now
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType) && newDb ) {
            populateResourceTypeAndParameterNameTableEntries(null);
        }
    }

    /**
     * Create fhir data and admin schema
     */
    protected void createFhirSchemas() {
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(target);
                    adapter.createSchema(schemaName);
                    adapter.createSchema(adminSchemaName);
                    if (createOauthSchema) {
                        adapter.createSchema(oauthSchemaName);
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
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     */
    protected void updateProcedures() {
        if (!STORED_PROCEDURE_ENABLED.contains(dbType)) {
            return;
        }
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(target);
                    pdm.applyProcedures(adapter);
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
     * Start the schema object creation tasks and wait for everything to complete
     *
     * @param pdm
     * @param adapter
     * @param collector
     * @param vhs
     */
    protected void applyModel(PhysicalDataModel pdm, IDatabaseAdapter adapter, ITaskCollector collector,
            VersionHistoryService vhs) {
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
     * Load the DB2 driver class
     */
    protected void loadDriver() {
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * Connect to the target database
     *
     * @return
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(properties);
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
        JdbcPropertyAdapter adapter = getPropertyAdapter(properties);

        JdbcConnectionProvider cp = new JdbcConnectionProvider(this.translator, adapter);
        this.connectionPool = new PoolConnectionProvider(cp, this.maxConnectionPoolSize);
        this.transactionProvider = new SimpleTransactionProvider(this.connectionPool);
    }

    /**
     * Process the requested operation
     */
    protected void process() {
        long start = System.nanoTime();
        configureConnectionPool();

        if (this.checkCompatibility) {
            checkCompatibility();
        }

        if (addKeyForTenant != null) {
            addTenantKey();
        } else if (this.dropSchema) {
            // only proceed with the drop if the user has provided additional confirmation
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (this.dropAdmin) {
            // only try to drop the admin schema
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (updateFhirSchema) {
            updateSchema();
        } else if (createFhirSchema) {
            createFhirSchemas();
        } else if (updateProc) {
            updateProcedures();
        } else if (this.allocateTenant) {
            allocateTenant();
        } else if (this.testTenant) {
            testTenant();
        } else if (this.dropTenant) {
            dropTenant();
        }

        if (this.grantTo != null) {
            grantPrivileges(FhirSchemaConstants.FHIR_USER_GRANT_GROUP);
        }

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     *
     * @param groupName
     */
    protected void grantPrivileges(String groupName) {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        final Db2Adapter adapter = new Db2Adapter(this.connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                pdm.applyGrants(adapter, groupName, grantTo);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     */
    protected void addTenantKey() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Only if the Tenant Key file is provided as a parameter is it not null.
        // in  this case we want special behavior.
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
        checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantDAO tid = new GetTenantDAO(adminSchemaName, addKeyForTenant);
                Tenant tenant = adapter.runStatement(tid);

                if (tenant != null) {
                    // Attach the new tenant key to the tenant:
                    AddTenantKeyDAO adder =
                            new AddTenantKeyDAO(adminSchemaName, tenant.getTenantId(), tenantKey, tenantSalt,
                                    FhirSchemaConstants.TENANT_SEQUENCE);
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
            logger.info(
                    "New tenant key from file: " + addKeyForTenant + " [tenantKeyFileName=" + tenantKeyFileName + "]");
            if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
            }
        }

    }

    /**
     * checks if tenant name and tenant key exists.
     *
     * @param adapter    the db2 adapter as this is a db2 feature only now
     * @param tenantName the tenant's name
     * @param tenantKey  tenant key
     */
    protected void checkIfTenantNameAndTenantKeyExists(Db2Adapter adapter, String tenantName, String tenantKey) {
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
                                throw new IllegalArgumentException("tenantName and tenantKey already exists");
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Problem checking the results");
                    }
                } catch (SQLException e) {
                    throw new IllegalArgumentException(
                            "Exception when querying backend to verify tenant key and tenant name", e);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     */
    protected void allocateTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // The key we'll use for this tenant. This key should be used in subsequent
        // activities related to this tenant, such as setting the tenant context.
        if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
            // Only if the Tenant Key file is provided as a parameter is it not null.
            // in  this case we want special behavior.
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
        checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey);

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
                        adapter.allocateTenant(adminSchemaName, schemaName, tenantName, tenantKey, tenantSalt,
                                FhirSchemaConstants.TENANT_SEQUENCE);

                // The tenant-id is important because this is also used to identify the partition number
                logger.info("Tenant Id[" + tenantName + "] = [" + tenantId + "]");
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Get the data model to create the table partitions. This is threaded, so transactions are
        // handled within each thread by the adapter. This means we should probably pull some of
        // that logic out of the adapter and handle it at a higher level. Note...the extent size used
        // for the partitions needs to match the extent size of the original table tablespace (FHIR_TS)
        // so this must be constant.
        pdm.addTenantPartitions(adapter, schemaName, tenantId, FhirSchemaConstants.FHIR_TS_EXTENT_KB);

        // Fill any static data tables (which are also partitioned by tenant)
        // Prepopulate the Resource Type Tables and Parameters Name/Code Table
        populateResourceTypeAndParameterNameTableEntries(tenantId);

        // Now all the table partitions have been allocated, we can mark the tenant as ready
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                adapter.updateTenantStatus(adminSchemaName, tenantId, TenantStatus.ALLOCATED);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        if (tenantKeyFileName == null) {
            logger.info("Allocated tenant: " + tenantName + " [key=" + tenantKey + "] with Id = " + tenantId);
        } else {
            logger.info("Allocated tenant: " + tenantName + " [tenantKeyFileName=" + tenantKeyFileName + "] with Id = "
                    + tenantId);
            if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
            }
        }
    }

    /**
     * populates for the given tenantId the RESOURCE_TYPE table.
     *
     * @implNote if you update this method, be sure to update
     *           DerbyBootstrapper.populateResourceTypeAndParameterNameTableEntries
     *           and DerbyFhirDatabase.populateResourceTypeAndParameterNameTableEntries
     *           The reason is there are three different ways of managing the transaction.
     * @param tenantId the mt_id that is used to setup the partition.
     *                 passing in null signals not multi-tenant.
     */
    protected void populateResourceTypeAndParameterNameTableEntries(Integer tenantId) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                logger.info("tenantId [" + tenantId + "] is being pre-populated with lookup table data.");
                PopulateResourceTypes populateResourceTypes =
                        new PopulateResourceTypes(adminSchemaName, schemaName, tenantId);
                populateResourceTypes.run(translator, c);

                PopulateParameterNames populateParameterNames =
                        new PopulateParameterNames(adminSchemaName, schemaName, tenantId);
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
     * Check that we can call the set_tenant procedure successfully (which means
     * that the
     * tenant record exists in the tenants table)
     */
    protected void testTenant() {
        if (this.tenantName == null || this.tenantName.isEmpty()) {
            throw new IllegalStateException("Missing tenant name");
        }

        // Part of Bring your own Tenant Key
        if (tenantKeyFileName != null) {
            // Only if the Tenant Key file is provided as a parameter is it not null.
            // in  this case we want special behavior.
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        } else {
            tenantKey = getRandomKey();
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
                Db2SetTenantVariable cmd = new Db2SetTenantVariable(adminSchemaName, tenantName, tenantKey);
                adapter.runStatement(cmd);

                Db2GetTenantVariable getter = new Db2GetTenantVariable(adminSchemaName);
                Integer tid = adapter.runStatement(getter);
                if (tid == null) {
                    throw new IllegalStateException("SV_TENANT_ID not set!");
                }

                // Print the id from the session variable (used for access control)
                logger.info("tenantName='" + tenantName + "', tenantId=" + tid);

                // Now let's check we can run a select against one our tenant-based
                // tables
                GetResourceTypeList rtListGetter = new GetResourceTypeList(schemaName);
                List<ResourceType> rtList = adapter.runStatement(rtListGetter);
                rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Deallocate this tenant, dropping all the related partitions
     */
    protected void dropTenant() {
        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        logger.info("Marking tenant for drop: " + tenantName);
        int tenantId;
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                tenantId = adapter.findTenantId(schemaName, tenantName);
                if (tenantId < 1) {
                    throw new IllegalArgumentException("Tenant '" + tenantName + "' not found in schema " + schemaName);
                }

                // Mark the tenant as frozen before we proceed with dropping anything
                adapter.updateTenantStatus(schemaName, tenantId, TenantStatus.FROZEN);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Remove all the tenant-based data
        pdm.removeTenantPartitions(adapter, schemaName, tenantId);
        pdm.dropOldTenantTables();
        pdm.dropTenantTablespace();

        // Now all the table partitions have been allocated, we can mark the tenant as dropped
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                adapter.updateTenantStatus(schemaName, tenantId, TenantStatus.DROPPED);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    protected boolean checkCompatibility() {
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            return adapter.checkCompatibility(this.adminSchemaName);
        }
    }

    /**
     * Generate a random 32 byte value encoded as a Base64 string (44 characters).
     *
     * @return
     */
    private String getRandomKey() {
        byte[] buffer = new byte[32];
        random.nextBytes(buffer);

        Encoder enc = Base64.getEncoder();
        return enc.encodeToString(buffer);
    }

    /**
     * Print the classpath so we can see what on earth is going on with connecting
     * to DB2 using an api key.
     */
    private static void logClasspath() {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("CLASSPATH: ");
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL[] classpath = ((URLClassLoader) cl).getURLs();
            for (URL u : classpath) {
                logger.fine("  " + u.getFile());
            }
        }
    }

    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        logClasspath();

        int exitStatus;
        Main m = new Main();
        try {
            m.configureLogger();
            m.parseArgs(args);
            m.loadDriver();
            m.process();
            exitStatus = m.getExitStatus();
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            printUsage();
            exitStatus = EXIT_BAD_ARGS;
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
