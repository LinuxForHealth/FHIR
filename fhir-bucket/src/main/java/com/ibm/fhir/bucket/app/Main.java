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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.bucket.client.ClientPropertyAdapter;
import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.cos.CosClient;
import com.ibm.fhir.bucket.persistence.FhirBucketSchema;
import com.ibm.fhir.bucket.scanner.CosReader;
import com.ibm.fhir.bucket.scanner.CosScanner;
import com.ibm.fhir.bucket.scanner.DataAccess;
import com.ibm.fhir.bucket.scanner.ResourceHandler;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
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
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;
import com.ibm.fhir.task.core.service.TaskService;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int DEFAULT_POOL_SIZE = 10;
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
    private int maxPoolSize = DEFAULT_POOL_SIZE;
    
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
    
    // The COS scanner active object
    private CosScanner scanner;
    
    // The COS loader active object
    private CosReader reader;
    
    // The number of resources we can process in parallel
    private int resourcePoolSize = 10;
    
    // The active object processing resources read from COS
    private ResourceHandler resourceHandler;
        
    private String logDir = ".";

    // The tenant name
    private String tenantName;

    // Set of file types we are interested in
    private Set<FileType> fileTypes = new HashSet<>();
    
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
            case "--resource-pool-size":
                if (i < args.length + 1) {
                    this.resourcePoolSize = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --resource-pool-size");
                }
                break;
            case "--tenant-name":
                if (i < args.length + 1) {
                    this.tenantName = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --tenant-name");
                }
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
     * Rudimentary check of the configuration to make sure the 
     * basics have been provided
     */
    public void checkConfig() {
        if (dbType == null) {
            throw new IllegalArgumentException("No --db-type given");
        }
        
        if (cosProperties.isEmpty()) {
            throw new IllegalArgumentException("No --cos-properties");
        }
        
        if (dbProperties.isEmpty()) {
            throw new IllegalArgumentException("No --db-properties");
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
        
        bootstrapDb();
        cosClient = new CosClient(cosProperties);
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
        this.connectionPool = new PoolConnectionProvider(cp, maxPoolSize);
        this.adapter = new DerbyAdapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    public void setupDb2Repository() {
        if (schemaName == null) {
            schemaName = "fhirbucket";
        }
        
        Db2PropertyAdapter propertyAdapter = new Db2PropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(new Db2Translator(), propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, maxPoolSize);
        this.adapter = new Db2Adapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    public void setupPostgresRepository() {
        if (schemaName == null) {
            schemaName = "fhirbucket";
        }

        PostgreSqlPropertyAdapter propertyAdapter = new PostgreSqlPropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(new PostgreSqlTranslator(), propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, maxPoolSize);
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
    }

    /**
     * Called by the shutdown hook
     */
    protected void shutdown() {
        // TODO synchronization/null checking
        logger.info("Stopping all services");
        this.scanner.stop();
        this.reader.stop();
        this.resourceHandler.stop();
        this.fhirClient.shutdown();
        logger.info("All services stopped");
    }
    
    
    /**
     * Start the processing threads and wait until we get told to stop
     */
    protected void runAndWait() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
        
        // Set up the client we use to send requests to the FHIR server
        fhirClient = new FhirClient(new ClientPropertyAdapter(fhirClientProperties));
        fhirClient.init(this.tenantName);
        
        // Set up our data access layer
        DataAccess dataAccess = new DataAccess(this.adapter, this.transactionProvider, this.schemaName);
        dataAccess.init();
        
        // Set up the scanner to look for new COS objects and register them in our database
        this.scanner = new CosScanner(cosClient, cosBucketList, dataAccess, this.fileTypes);
        scanner.init();
        
        // Set up the handler to process resources as they are read from COS
        this.resourceHandler = new ResourceHandler(this.resourcePoolSize, fhirClient);
        resourceHandler.init();
        
        // Set up the COS reader and wire it to the resourceHandler
        this.reader = new CosReader(cosClient, resource -> resourceHandler.process(resource), maxPoolSize, dataAccess);
        reader.init();

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
        m.runAndWait();
    }
}
