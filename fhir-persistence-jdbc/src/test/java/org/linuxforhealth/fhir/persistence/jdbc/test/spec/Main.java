/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.spec;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.DefaultFHIRConfigProvider;
import org.linuxforhealth.fhir.config.FHIRConfigProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.common.JdbcConnectionProvider;
import org.linuxforhealth.fhir.database.utils.common.JdbcPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.derby.DerbyNetworkTranslator;
import org.linuxforhealth.fhir.database.utils.derby.DerbyPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.derby.DerbyTranslator;
import org.linuxforhealth.fhir.database.utils.pool.PoolConnectionProvider;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresTranslator;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.examples.Index;
import org.linuxforhealth.fhir.model.spec.test.DriverMetrics;
import org.linuxforhealth.fhir.model.spec.test.DriverStats;
import org.linuxforhealth.fhir.model.spec.test.Expectation;
import org.linuxforhealth.fhir.model.spec.test.R4ExamplesDriver;
import org.linuxforhealth.fhir.model.spec.test.SerializationProcessor;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.cache.CommonValuesCacheImpl;
import org.linuxforhealth.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import org.linuxforhealth.fhir.persistence.jdbc.cache.IdNameCache;
import org.linuxforhealth.fhir.persistence.jdbc.cache.LogicalResourceIdentCacheImpl;
import org.linuxforhealth.fhir.persistence.jdbc.cache.NameIdCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ICommonValuesCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import org.linuxforhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import org.linuxforhealth.fhir.schema.derby.DerbyFhirDatabase;
import org.linuxforhealth.fhir.validation.test.ValidationProcessor;

/**
 * Integration test using a multi-tenant schema in DB2 as the target for the
 * FHIR R4 Examples.
 *
 * TODO refactor to reduce duplication with the fhir-server-test Main app.
 *
 * Usage
 * <code>
 *   java org.linuxforhealth.fhir.persistence.jdbc.test.spec.Main
 *   --parse
 *   --validate
 *   --derby
 *   --expectation OK
 *   --file-name file:/path/to/foo.json
 *   --file-name file:/path/to/bar.json
 *   --expectation PARSE
 *   --file-name file:/path/to/bad.json
 *   --expectation VALIDATION
 *   --file-name json/minimal/Patient-1.json
 *
 *   java org.linuxforhealth.fhir.persistence.jdbc.test.spec.Main
 *   --parse
 *   --validate
 *   --derby
 *   --index MINIMAL_JSON
 * </code>
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int EXIT_RUNTIME_ERROR = 1;

    // The persistence API
    protected FHIRPersistence persistence = null;

    // FHIR datasource configuration properties
    protected Properties configProps = new Properties();

    private String schemaName = "FHIRDATA"; // default

    // The name of the index file to use
    private Index index;

    private boolean validate;

    // Number of threads to use if running concurrency checks
    private int threads = 8;

    // The number of times we perform the read each time. Useful for performance analysis
    private int readIterations = 1;

    // The number of requests which can be submitted to the pool before blocking
    private int maxInflight;

    // The thread pool used for concurrent request processing
    private ExecutorService pool;

    private List<FileExpectation> fileExpectations = new ArrayList<>();

    // mode of operation
    private static enum Operation {
        DERBY, DERBYNETWORK, POSTGRESQL, CITUS, PARSE
    }

    private Operation mode = Operation.POSTGRESQL;


    /**
     * Parse the command-line arguments
     * @param args
     */
    protected void parseArgs(String[] args) {

        // Make the CLI a little easier by allowing the expectation to
        // set once for a group of --file-name entries
        Expectation currentExpectation = Expectation.OK;

        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--threads":
                if (++i < args.length) {
                    this.threads = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --threads argument at posn: " + i);
                }
                break;
            case "--read-iterations":
                if (++i < args.length) {
                    this.readIterations = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --read-iterations argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    this.schemaName = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--index":
                if (++i < args.length) {
                    this.index = Index.valueOf(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--expectation":
                if (++i < args.length) {
                    currentExpectation = Expectation.valueOf(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--file-name":
                if (++i < args.length) {
                    this.fileExpectations.add(new FileExpectation(args[i], currentExpectation));
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--derby":
                this.mode = Operation.DERBY;
                break;
            case "--derbynetwork":
                this.mode = Operation.DERBYNETWORK;
                break;
            case "--postgresql":
                this.mode = Operation.POSTGRESQL;
                break;
            case "--citus":
                this.mode = Operation.CITUS;
                break;
            case "--parse":
                this.mode = Operation.PARSE;
                break;
            case "--validate":
                this.validate = true;
                break;
            case "--max-inflight":
                if (++i < args.length) {
                    this.maxInflight = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --max-inflight argument at posn: " + i);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }

        // Inject the data schema name into the configuration properties
        configProps.setProperty("db.default.schema", schemaName);
    }

    /**
     * Read the properties from the given file
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            configProps.load(is);
        }
        catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Configure the class using the values collected from parseArgs
     */
    protected void configure() {
        if (threads > 0) {
            pool = Executors.newFixedThreadPool(threads);

            if (maxInflight == 0) {
                maxInflight = threads * 2;
            }
        }
    }

    /**
     * Render a quick report on the time taken for various steps we've instrumented.
     * TODO: add as part of a regression test
     * @param dm
     * @param elapsed
     */
    protected void renderReport(DriverMetrics dm, long elapsed) {
        // how many seconds did we run for?
        double secs = elapsed / 1000.0;
        dm.render(new DriverStats(System.out, secs));
    }

    /**
     * Run the test with Derby or DB2 etc, depending on the chosen option
     * @throws Exception
     */
    protected void process() throws Exception {
        switch (this.mode) {
        case DERBY:
            processDerby();
            break;
        case DERBYNETWORK:
            processDerbyNetwork();
            break;
        case POSTGRESQL:
        case CITUS:
            processPostgreSql();
            break;
        case PARSE:
            processParse();
            break;
        }
    }

    /**
     * Run the driver based on the arguments we have
     * @param driver
     */
    protected void runDriver(R4ExamplesDriver driver) throws Exception {
        if (this.fileExpectations.size() > 0) {
            for (FileExpectation fx: this.fileExpectations) {
                driver.processExample(fx.getFilename(), fx.getExpectation());
            }
        }
        else if (this.index != null) {
            driver.processIndex(this.index);
        }
        else {
            driver.processIndex(Index.MINIMAL_JSON);
        }
    }

    /**
     * Populate the list of operations we want to use for the configured test
     * @param operations
     */
    protected void populateOperationsList(List<ITestResourceOperation> operations, DriverMetrics dm) {
        operations.add(new CreateOperation(dm));
        operations.add(new ReadOperation(dm, this.readIterations));
    }

    /**
     * Use an embedded derby target to process the examples
     * @throws Exception
     */
    protected void processDerby() throws Exception {
        // For Derby, the standard config provider is sufficient
        FHIRConfigProvider configProvider = new DefaultFHIRConfigProvider();

        // The DerbyFhirDatabase encapsulates Derby and provides an
        // IConnectionProvider implementation used by the persistence
        // layer to obtain connections.
        try (DerbyFhirDatabase database = new DerbyFhirDatabase()) {
            ICommonValuesCache rrc = new CommonValuesCacheImpl(100, 100, 100);
            ILogicalResourceIdentCache lric = new LogicalResourceIdentCacheImpl(100);
            FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), 
                new IdNameCache<Integer>(), new NameIdCache<Integer>(), rrc, lric);
            persistence = new FHIRPersistenceJDBCImpl(this.configProps, database, cache);

            // create a custom list of operations to apply in order to each resource
            DriverMetrics dm = new DriverMetrics();
            List<ITestResourceOperation> operations = new ArrayList<>();
            populateOperationsList(operations, dm);

            R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(persistence,
                    () -> createPersistenceContext(),
                    () -> createHistoryPersistenceContext(),
                    operations, configProvider, cache);

            // The driver will iterate over all the JSON examples in the R4 specification, parse
            // the resource and call the processor.
            long start = System.nanoTime();
            R4ExamplesDriver driver = new R4ExamplesDriver();
            driver.setMetrics(dm);
            driver.setProcessor(processor);

            // enable optional validation
            if (this.validate) {
                driver.setValidator(new ValidationProcessor());
            }

            if (pool != null) {
                driver.setPool(pool, maxInflight);
            }

            runDriver(driver);

            // print out some simple stats
            long elapsed = (System.nanoTime() - start) / DriverMetrics.NANOS_MS;
            renderReport(dm, elapsed);
        }
    }


    /**
     * Use a network derby target to process the examples
     * @throws Exception
     */
    protected void processDerbyNetwork() throws Exception {
        // Set up a connection provider pointing to the DB2 instance described
        // by the configProps
        JdbcPropertyAdapter adapter = new DerbyPropertyAdapter(configProps);
        DerbyTranslator translator = new DerbyNetworkTranslator();
        JdbcConnectionProvider cp = new JdbcConnectionProvider(translator, adapter);
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, this.threads);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
        FHIRConfigProvider configProvider = new DefaultFHIRConfigProvider();
        ICommonValuesCache rrc = new CommonValuesCacheImpl(100, 100, 100);
        ILogicalResourceIdentCache lric = new LogicalResourceIdentCacheImpl(100);
        FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new IdNameCache<Integer>(), new NameIdCache<Integer>(), rrc, lric);

        // create a custom list of operations to apply in order to each resource
        DriverMetrics dm = new DriverMetrics();
        List<ITestResourceOperation> operations = new ArrayList<>();
        populateOperationsList(operations, dm);

        R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(
                operations,
                this.configProps,
                connectionPool,
                null,
                null,
                transactionProvider,
                configProvider,
                cache);

        // The driver will iterate over all the JSON examples in the R4 specification, parse
        // the resource and call the processor.
        long start = System.nanoTime();
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setMetrics(dm);
        driver.setProcessor(processor);

        // enable optional validation
        if (this.validate) {
            driver.setValidator(new ValidationProcessor());
        }

        if (pool != null) {
            driver.setPool(pool, maxInflight);
        }

        runDriver(driver);

        // print out some simple stats
        long elapsed = (System.nanoTime() - start) / DriverMetrics.NANOS_MS;
        renderReport(dm, elapsed);
    }


    /**
     * Use a postgresql target to process the examples
     * @throws Exception
     */
    protected void processPostgreSql() throws Exception {
        // Set up a connection provider pointing to the PostgreSql instance described
        // by the configProps
        JdbcPropertyAdapter adapter = new PostgresPropertyAdapter(configProps);
        PostgresTranslator translator = new PostgresTranslator();
        JdbcConnectionProvider cp = new JdbcConnectionProvider(translator, adapter);
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, this.threads);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
        FHIRConfigProvider configProvider = new DefaultFHIRConfigProvider();
        ICommonValuesCache rrc = new CommonValuesCacheImpl(100, 100, 100);
        ILogicalResourceIdentCache lric = new LogicalResourceIdentCacheImpl(100);
        FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new IdNameCache<Integer>(), new NameIdCache<Integer>(), rrc, lric);


        // create a custom list of operations to apply in order to each resource
        DriverMetrics dm = new DriverMetrics();
        List<ITestResourceOperation> operations = new ArrayList<>();
        populateOperationsList(operations, dm);

        R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(
                operations,
                this.configProps,
                connectionPool,
                null,
                null,
                transactionProvider,
                configProvider,
                cache);

        // The driver will iterate over all the JSON examples in the R4 specification, parse
        // the resource and call the processor.
        long start = System.nanoTime();
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setMetrics(dm);
        driver.setProcessor(processor);

        // enable optional validation
        if (this.validate) {
            driver.setValidator(new ValidationProcessor());
        }

        if (pool != null) {
            driver.setPool(pool, maxInflight);
        }

        runDriver(driver);

        // print out some simple stats
        long elapsed = (System.nanoTime() - start) / DriverMetrics.NANOS_MS;
        renderReport(dm, elapsed);
    }

    /**
     * Do a parse-only run through the examples, useful for profiling
     */
    protected void processParse() throws Exception {
        R4ExamplesDriver driver = new R4ExamplesDriver();
        SerializationProcessor processor = new SerializationProcessor();
        driver.setProcessor(processor);

        // Should we also do validation?
        if (this.validate) {
            driver.setValidator(new ValidationProcessor());
        }

        // If we're testing concurrency, pass in a thread pool
        if (this.pool != null) {
            driver.setPool(this.pool, this.maxInflight);
        }

        runDriver(driver);
    }


    /**
     * Create a new {@link FHIRPersistenceContext} for the test
     * @return
     */
    protected FHIRPersistenceContext createPersistenceContext() {
        try {
            return FHIRPersistenceContextFactory.createPersistenceContext(null);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }

    /**
     * Createa anew FHIRPersistenceContext configure with a FHIRHistoryContext
     * @return
     */
    protected FHIRPersistenceContext createHistoryPersistenceContext() {
        try {
            FHIRHistoryContext fhc = FHIRPersistenceContextFactory.createHistoryContext();
            return FHIRPersistenceContextFactory.createPersistenceContext(null, fhc);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.parseArgs(args);
            m.configure();
            m.process();
        }
        catch (Exception x) {
            logger.log(Level.SEVERE, x.getMessage(), x);
            System.exit(EXIT_RUNTIME_ERROR);
        }
        System.exit(0);
    }

}
