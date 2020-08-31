/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.app;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.bucket.client.ClientPropertyAdapter;
import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.cos.CosClient;
import com.ibm.fhir.bucket.persistence.FhirBucketSchema;
import com.ibm.fhir.bucket.persistence.MergeResourceTypes;
import com.ibm.fhir.bucket.persistence.MergeResourceTypesPostgres;
import com.ibm.fhir.bucket.scanner.CosReader;
import com.ibm.fhir.bucket.scanner.CosScanner;
import com.ibm.fhir.bucket.scanner.DataAccess;
import com.ibm.fhir.bucket.scanner.ResourceHandler;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.LogFormatter;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlAdapter;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlPropertyAdapter;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlTranslator;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.model.type.code.FHIRResourceType;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;
import com.ibm.fhir.task.core.service.TaskService;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int DEFAULT_CONNECTION_POOL_SIZE = 10;
    private static final int DEFAULT_MAX_FHIR_CONCURRENT_REQUESTS = 40;
    private static final int DEFAULT_COS_SCAN_INTERVAL_MS = 300000; // 5 mins
    private static final String DEFAULT_SCHEMA_NAME = "FHIRBUCKET";
    private final Properties cosProperties = new Properties();
    private final Properties dbProperties = new Properties();
    private final Properties fhirClientProperties = new Properties();

    // The type of database we're talking to
    private DbType dbType;
    
    // Connection pool used to work alongside the transaction provider
    private PoolConnectionProvider connectionPool;
    
    // Simple transaction service for use outside of JEE
    private ITransactionProvider transactionProvider;

    // The database schema for all tables used by this application
    private String schemaName = null;

    // Database connection pool size
    private int connectionPoolSize = DEFAULT_CONNECTION_POOL_SIZE;

    // How many JSON files can we process at the same time
    private int maxConcurrentJsonFiles = 10;
    
    // How many NDJSON files can we process at the same time (typically 1)
    private int maxConcurrentNdJsonFiles = 1;

    // How many FHIR requests should be allowed concurrently
    private int maxConcurrentFhirRequests = DEFAULT_MAX_FHIR_CONCURRENT_REQUESTS;
    
    // Just slightly over 2 minutes, which is just slightly longer than the FHIR default tx timeout
    private int poolShutdownTimeoutSeconds = 130;
    
    // The thread-pool shared by the services for async processing
    private ExecutorService commonPool;
    
    // Configured connection to IBM Cloud Object Storage (S3)
    private CosClient cosClient;
    
    // FHIR server requests go through this client
    private FhirClient fhirClient;

    // The list of buckets to scan for resources to load
    private final List<String> cosBucketList = new ArrayList<>();
    
    // The adapter configured for the type of database we're using
    private IDatabaseAdapter adapter;
    
    // The number of threads to use for the schema creation step
    private int createSchemaThreads = 1;
    
    private int cosScanIntervalMs = DEFAULT_COS_SCAN_INTERVAL_MS;
    
    // The COS scanner active object
    private CosScanner scanner;
    
    // The COS reader handling JSON files
    private CosReader jsonReader;
    
    // The COS reader handling NDJSON files (which are processed one at a time
    private CosReader ndJsonReader;
    
    // The active object processing resources read from COS
    private ResourceHandler resourceHandler;
        
    private String logDir = ".";

    // The tenant name
    private String tenantName;

    // Set of file types we are interested in
    private Set<FileType> fileTypes = new HashSet<>();
    
    // optional prefix for scanning a subset of the COS bucket
    private String pathPrefix;
    
    // Just create the schema and exit
    private boolean createSchema = false;
    
    // Skip NDJSON rows already processed. Assumes each row is an individual resource or transaction bundle
    private boolean incremental = false;
    
    // Skip NDJSON rows for which we already have processed and recorded logical ids. Requires a lookup per line
    private boolean incrementalExact = false;
    
    // optionally reload the same data after this seconds. -1 == do not recycle
    private int recycleSeconds = -1;
    
    /**
     * Parse command line arguments
     * @param args
     */
    public void parseArgs(String[] args) {
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--db-type":
                if (i < args.length + 1) {
                    this.dbType = DbType.from(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --db-type");
                }
                break;
            case "--create-schema":
                this.createSchema = true;
                break;
            case "--cos-properties":
                if (i < args.length + 1) {
                    loadCosProperties(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --cos-properties");
                }
                break;
            case "--db-properties":
                if (i < args.length + 1) {
                    loadDbProperties(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --db-properties");
                }
                break;
            case "--db-prop":
                if (i < args.length + 1) {
                    addDbProperty(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --db-properties");
                }
                break;
            case "--fhir-properties":
                if (i < args.length + 1) {
                    loadFhirClientProperties(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --fhir-properties");
                }
                break;
            case "--bucket":
                if (i < args.length + 1) {
                    this.cosBucketList.add(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --bucket");
                }
                break;
            case "--file-type":
                if (i < args.length + 1) {
                    this.fileTypes.add(FileType.valueOf(args[++i]));
                } else {
                    throw new IllegalArgumentException("missing value for --file-type");
                }
                break;
            case "--cos-scan-interval-ms":
                if (i < args.length + 1) {
                    this.cosScanIntervalMs = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --cos-scan-interval-ms");
                }
                break;
            case "--recycle-seconds":
                if (i < args.length + 1) {
                    this.recycleSeconds = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --recycle-seconds");
                }
                break;
            case "--max-concurrent-fhir-requests":
                if (i < args.length + 1) {
                    this.maxConcurrentFhirRequests = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --max-concurrent-fhir-requests");
                }
                break;
            case "--max-concurrent-json-files":
                if (i < args.length + 1) {
                    this.maxConcurrentJsonFiles = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --max-concurrent-json-files");
                }
                break;
            case "--max-concurrent-ndjson-files":
                if (i < args.length + 1) {
                    this.maxConcurrentNdJsonFiles = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --max-concurrent-ndjson-files");
                }
                break;
            case "--connection-pool-size":
                if (i < args.length + 1) {
                    this.connectionPoolSize = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --connection-pool-size");
                }
                break;
            case "--pool-shutdown-timeout-seconds":
                if (i < args.length + 1) {
                    this.poolShutdownTimeoutSeconds = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --resource-pool-shutdown-timeout-seconds");
                }
                break;
            case "--tenant-name":
                if (i < args.length + 1) {
                    this.tenantName = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --tenant-name");
                }
                break;
            case "--path-prefix":
                if (i < args.length + 1) {
                    this.pathPrefix = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --path-prefix");
                }
                break;
            case "--incremental":
                this.incremental = true;
                break;
            case "--incremental-exact":
                this.incrementalExact = true;
                break;
            default:
                throw new IllegalArgumentException("Bad arg: " + arg);
            }
        }
    }

    /**
     * Load COS properties from the given properties file
     * @param filename
     */
    protected void loadCosProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            cosProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Load the FHIR client properties from the given properties file
     * @param filename
     */
    protected void loadFhirClientProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            fhirClientProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Load DB properties from the given properties file
     * @param filename
     */
    protected void loadDbProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            dbProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Add the property from the arg given in the form of:
     *   param=value
     * @param arg
     */
    protected void addDbProperty(String arg) {
        String props[] = arg.split("=");
        if (props.length == 2) {
            dbProperties.put(props[0], props[1]);
        } else {
            logger.warning("Invalid property value: " + arg);
        }
    }
    
    /**
     * Rudimentary check of the configuration to make sure the 
     * basics have been provided
     */
    public void checkConfig() {
        if (dbType == null) {
            throw new IllegalArgumentException("No --db-type given");
        }
        
        if (dbProperties.isEmpty()) {
            throw new IllegalArgumentException("No database properties");
        }

        
        if (!this.createSchema && cosProperties.isEmpty()) {
            throw new IllegalArgumentException("No COS properties");
        }
        
        if (!this.createSchema && fhirClientProperties.isEmpty()) {
            throw new IllegalArgumentException("No FHIR properties");
        }
    }
    
    /**
     * Set up the database configuration we are going to use to coordinate
     * loading activities. Only one instance should be performing the schema
     * update, so we need a special table which can be used to serialize
     * the schema update process and avoid race conditions.
     */
    public void configure() {
        
        if (fileTypes.isEmpty()) {
            // use NDJSON if the user didn't provide their own choice
            this.fileTypes.add(FileType.NDJSON);
        }
        
        File f = new File(logDir, "fhirbucket.log");
        LogFormatter.init(f.getPath());
        

        switch (this.dbType) {
        case DB2:
            setupDb2Repository();
            break;
        case DERBY:
            setupDerbyRepository();
            break;
        case POSTGRESQL:
            setupPostgresRepository();
            break;
        }        
        
        // We constrain the number of concurrent tasks which are inflight, so a breathable
        // pool works nicely because it cannot grow unbounded
        this.commonPool = Executors.newCachedThreadPool();
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a Derby
     * database
     */
    public void setupDerbyRepository() {
        if (schemaName == null) {
            // use the default schema for Derby
            schemaName = "APP";
        }
        
        DerbyPropertyAdapter propertyAdapter = new DerbyPropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(new DerbyTranslator(), propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.adapter = new DerbyAdapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    public void setupDb2Repository() {
        if (schemaName == null) {
            schemaName = DEFAULT_SCHEMA_NAME;
        }
        
        IDatabaseTranslator translator = new Db2Translator();
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        
        Db2PropertyAdapter propertyAdapter = new Db2PropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(translator, propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.adapter = new Db2Adapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    public void setupPostgresRepository() {
        if (schemaName == null) {
            schemaName = DEFAULT_SCHEMA_NAME;
        }
        
        IDatabaseTranslator translator = new PostgreSqlTranslator();
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        PostgreSqlPropertyAdapter propertyAdapter = new PostgreSqlPropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(translator, propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.adapter = new PostgreSqlAdapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }
    
    /**
     * Create the version history table and a simple service which is used to
     * access information from it.
     * 
     * @throws SQLException
     */
    protected VersionHistoryService createVersionHistoryService() {
        if (this.adapter == null) {
            throw new IllegalStateException("Database adapter not configured");
        }

        // Create the version history table if it doesn't yet exist
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                adapter.createSchema(schemaName);
                CreateVersionHistory.createTableIfNeeded(schemaName, this.adapter);
            } catch (Exception x) {
                logger.log(Level.SEVERE, "failed to create version history table", x);
                tx.setRollbackOnly();
                throw x;
            }
        }
        
        // Current version history for the data schema.
        VersionHistoryService vhs = new VersionHistoryService(schemaName, schemaName);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(this.adapter);
        vhs.init();
        return vhs;
    }

    /**
     * Create or update the database schema to the latest definition
     */
    public void bootstrapDb() {

        // The version history service is used to track schema changes
        // so we know which to apply and which to skip
        VersionHistoryService vhs = createVersionHistoryService();
        
        // Create the schema in a managed transaction
        FhirBucketSchema schema = new FhirBucketSchema(schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        schema.constructModel(pdm);
        
        // Use the dependency information in the physical data model to 
        // build a task tree which can be executed in parallel, if desired
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.createSchemaThreads);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        pdm.collect(collector, adapter, this.transactionProvider, vhs);

        // FHIR in the hole!
        logger.info("Starting schema updates");
        collector.startAndWait();
        
        pool.shutdown();

        Collection<ITaskGroup> failedTaskGroups = collector.getFailedTaskGroups();
        if (failedTaskGroups.size() > 0) {
            final String failedStr =
                    failedTaskGroups.stream().map((tg) -> tg.getTaskId()).collect(Collectors.joining(","));
            logger.severe("Schema update [FAILED]: " + failedStr);
            throw new IllegalStateException("Schema update failed");
        } else {
            logger.info("Schema update [SUCCEEDED]");
        }
        
        // populate the RESOURCE_TYPES table
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                Set<String> resourceTypes = Arrays.stream(FHIRResourceType.ValueSet.values())
                        .map(FHIRResourceType.ValueSet::value)
                        .collect(Collectors.toSet());
                
                if (adapter.getTranslator().getType() == DbType.POSTGRESQL) {
                    // Postgres doesn't support batched merges, so we go with a simpler UPSERT
                    MergeResourceTypesPostgres mrt = new MergeResourceTypesPostgres(resourceTypes);
                    adapter.runStatement(mrt);
                } else {
                    MergeResourceTypes mrt = new MergeResourceTypes(resourceTypes);
                    adapter.runStatement(mrt);
                }
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Called by the shutdown hook to stop everything in an orderly fashion
     */
    protected void shutdown() {
        // The goal here is to stop the generation of new work and let
        // existing work drain before we completely pull the plug. This
        // hopefully means we don't end up with gaps in the resources
        // processed from an NDJSON file, allowing the --incremental option
        // to be used safely without skipping rows that weren't actually loaded
        logger.info("Stopping all services");

        // First up, signal everything to stop. This is just a notification,
        // we don't block on any of these
        this.scanner.signalStop();
        
        if (this.jsonReader != null) {
            this.jsonReader.signalStop();
        }
        
        if (this.ndJsonReader != null) {
            this.ndJsonReader.signalStop();
        }

        this.resourceHandler.signalStop();
        
        
        this.scanner.waitForStop();
        if (this.jsonReader != null) {
            this.jsonReader.waitForStop();
        }
        
        if (this.ndJsonReader != null) {
            this.ndJsonReader.waitForStop();
        }
        this.resourceHandler.waitForStop();
        this.fhirClient.shutdown();
        
        // Finally we can ask the common thread-pool to close up shop. Typically we
        // should wait for at least as long as the FHIR server transaction timeout
        // so that we don't lose any responses (and therefore fail to record the
        // resource ids in our database.
        this.commonPool.shutdown();
        try {
            this.commonPool.awaitTermination(this.poolShutdownTimeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException x) {
            logger.warning("Common thread-pool failed to terminate within " + poolShutdownTimeoutSeconds + "s");
        }
        logger.info("All services stopped");
    }

    /**
     * Choose which mode of the program we want to run:
     * - create the schema
     * - scan and load
     */
    public void process() {
        if (this.createSchema) {
            bootstrapDb();
        } else {
            scanAndLoad();
        }
    }
    
    /**
     * Start the processing threads and wait until we get told to stop
     */
    protected void scanAndLoad() {

        // Set up the shutdown hook to keep things orderly when asked to terminate
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
        
        cosClient = new CosClient(cosProperties);
        
        // Set up the client we use to send requests to the FHIR server
        fhirClient = new FhirClient(new ClientPropertyAdapter(fhirClientProperties));
        fhirClient.init(this.tenantName);
        
        // DataAccess hides the details of our interactions with the FHIRBUCKET tracking tables
        DataAccess dataAccess = new DataAccess(this.adapter, this.transactionProvider, this.schemaName);
        dataAccess.init();
        
        // Set up the scanner to look for new COS objects and register them in our database
        this.scanner = new CosScanner(cosClient, cosBucketList, dataAccess, this.fileTypes, pathPrefix, cosScanIntervalMs);
        scanner.init();
        
        // Set up the handler to process resources as they are read from COS
        // Uses an internal pool to parallelize NDJSON work
        this.resourceHandler = new ResourceHandler(this.commonPool, this.maxConcurrentFhirRequests, fhirClient, dataAccess);
        resourceHandler.init();
        
        // Set up the COS reader and wire it to the resourceHandler
        if (fileTypes.contains(FileType.JSON)) {
            this.jsonReader = new CosReader(commonPool, FileType.JSON, cosClient, resource -> resourceHandler.process(resource), this.maxConcurrentJsonFiles, dataAccess, incremental, recycleSeconds, incrementalExact);
            this.jsonReader.init();
        }

        if (fileTypes.contains(FileType.NDJSON)) {
            this.jsonReader = new CosReader(commonPool, FileType.NDJSON, cosClient, resource -> resourceHandler.process(resource), this.maxConcurrentNdJsonFiles, dataAccess, incremental, recycleSeconds, incrementalExact);
            this.jsonReader.init();
        }

        // JVM won't exit until the threads are stopped via the
        // shutdown hook
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        Main m = new Main();
        m.parseArgs(args);
        m.checkConfig();
        m.configure();
        m.process();
    }
}
