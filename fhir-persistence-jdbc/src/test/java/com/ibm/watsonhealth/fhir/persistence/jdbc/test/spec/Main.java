/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;

/**
 * Integration test using a multi-tenant schema in DB2 as the target for the
 * FHIR R4 Examples.
 * @author rarnold
 *
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final int EXIT_OK = 0; // validation was successful
    private static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    private static final int EXIT_RUNTIME_ERROR = 2; // programming error
    private static final int EXIT_VALIDATION_FAILED = 3; // validation test failed
    private static final double NANOS = 1e9;

    // The persistence API
    protected FHIRPersistence persistence = null;

    // FHIR datasource configuration properties
    protected Properties configProps = new Properties();
    
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;

    
    /**
     * Parse the command-line arguments
     * @param args
     */
    protected void parseArgs(String[] args) {
        
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--tenant-key":
                if (++i < args.length) {
                    this.tenantKey = args[i];
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

            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
        
    }
    
    /**
     * Process the examples
     * @throws Exception
     */
    protected void process() throws Exception {
        persistence = new FHIRPersistenceJDBCNormalizedImpl(this.configProps);
        R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(persistence, 
            () -> createPersistenceContext(),
            () -> createHistoryPersistenceContext());

        // Provide the credentials we need for accessing a multi-tenant schema (if enabled)
        if (this.tenantName != null) {
            processor.setTenantName(this.tenantName);
            processor.setTenantKey(this.tenantKey);
        }
        
        // The driver will iterate over all the JSON examples in the R4 specification, parse
        // the resource and call the processor.
        R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setProcessor(processor);
        driver.processAllExamples();        
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
        }
    }

}
