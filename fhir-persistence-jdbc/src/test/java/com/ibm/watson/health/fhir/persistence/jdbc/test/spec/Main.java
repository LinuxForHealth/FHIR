/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.test.spec;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watson.health.database.utils.common.JdbcConnectionProvider;
import com.ibm.watson.health.database.utils.common.JdbcPropertyAdapter;
import com.ibm.watson.health.database.utils.db2.Db2PropertyAdapter;
import com.ibm.watson.health.database.utils.db2.Db2Translator;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.spec.test.Expectation;
import com.ibm.watson.health.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.watson.health.fhir.model.spec.test.SerializationProcessor;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watson.health.fhir.schema.derby.DerbyFhirDatabase;
import com.ibm.watson.health.fhir.validation.test.ValidationProcessor;

/**
 * Integration test using a multi-tenant schema in DB2 as the target for the
 * FHIR R4 Examples.
 * @author rarnold
 * 
 * Usage 
 *   java com.ibm.watson.health.fhir.persistence.jdbc.test.spec.Main
 *   --parse
 *   --validate
 *   --expectation OK
 *   --file-name file:/path/to/foo.json
 *   --file-name file:/path/to/bar.json
 *   --expectation PARSE
 *   --file-name file:/path/to/bad.json
 *   --expectation VALIDATION
 *   --file-name json/ibm/minimal/Patient-1.json
 *   
 *   java com.ibm.watson.health.fhir.persistence.jdbc.test.spec.Main
 *   --parse
 *   --validate
 *   --index-file file:/path/to/my/index.txt
 * 
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final int EXIT_RUNTIME_ERROR = 1;

    // The persistence API
    protected FHIRPersistence persistence = null;
    
    // FHIR datasource configuration properties
    protected Properties configProps = new Properties();
    
    private String schemaName = "FHIRDATA"; // default
    private String tenantName;
    private String tenantKey;
    
    // The name of the index file to use
    private String indexFileName;
    
    private boolean validate;
    
    private List<FileExpectation> fileExpectations = new ArrayList<>();

    // mode of operation
    private static enum Operation {
        DB2, DERBY, PARSE
    }
    
    private Operation mode = Operation.DB2;

    
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
            case "--tenant-key":
                if (++i < args.length) {
                    this.tenantKey = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
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
            case "--tenant-name":
                if (++i < args.length) {
                    this.tenantName = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--index-file":
                if (++i < args.length) {
                    this.indexFileName = args[i];
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
            case "--parse":
                this.mode = Operation.PARSE;
                break;
            case "--validate":
                this.validate = true;
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
     * Run the test with Derby or DB2, depending on the chosen option
     * @throws Exception
     */
    protected void process() throws Exception {
        switch (this.mode) {
        case DB2:
            processDB2();
            break;
        case DERBY:
            processDerby();
            break;
        case PARSE:
            processParse();
            break;
        }
    }
    
    /**
     * Process the examples
     * @throws Exception
     */
    protected void processDB2() throws Exception {
        
        // Set up a connection provider pointing to the DB2 instance described
        // by the configProps
        JdbcPropertyAdapter adapter = new Db2PropertyAdapter(configProps);
        Db2Translator translator = new Db2Translator();
        JdbcConnectionProvider cp = new JdbcConnectionProvider(translator, adapter);
        
        // Provide the credentials we need for accessing a multi-tenant schema (if enabled)
        // Must set this BEFORE we create our persistence object
        if (this.tenantName != null) {
            if (tenantKey == null) {
                throw new IllegalArgumentException("No tenant-key provided");
            }
            
            // Set up the FHIRRequestContext on this thread so that the persistence layer
            // can configure itself for this tenant
            FHIRRequestContext rc = FHIRRequestContext.get();
            rc.setTenantId(this.tenantName);
            rc.setTenantKey(this.tenantKey);
        }

        
        persistence = new FHIRPersistenceJDBCNormalizedImpl(this.configProps, cp);
        R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(persistence, 
            () -> createPersistenceContext(),
            () -> createHistoryPersistenceContext());
                
        // The driver will iterate over all the JSON examples in the R4 specification, parse
        // the resource and call the processor.
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setProcessor(processor);
        runDriver(driver);
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
        else if (this.indexFileName != null) {
            driver.processIndex(this.indexFileName);
        }
        else {
            driver.processAllExamples();        
        }
    }

    /**
     * Use a derby target to process the examples
     * @throws Exception
     */
    protected void processDerby() throws Exception {
        
        // Set up a connection provider pointing to the DB2 instance described
        // by the configProps
        try (DerbyFhirDatabase database = new DerbyFhirDatabase()) {
        
            persistence = new FHIRPersistenceJDBCNormalizedImpl(this.configProps, database);
            R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(persistence, 
                () -> createPersistenceContext(),
                () -> createHistoryPersistenceContext());
                    
            // The driver will iterate over all the JSON examples in the R4 specification, parse
            // the resource and call the processor.
            R4ExamplesDriver driver = new R4ExamplesDriver();
            driver.setProcessor(processor);
            runDriver(driver);
        }
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
            m.process();
        }
        catch (Exception x) {
            logger.log(Level.SEVERE, x.getMessage(), x);
            System.exit(EXIT_RUNTIME_ERROR);
        }
    }

}
