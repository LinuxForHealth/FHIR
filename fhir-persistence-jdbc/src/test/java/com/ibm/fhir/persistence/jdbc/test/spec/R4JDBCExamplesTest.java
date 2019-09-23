/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.Properties;

import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

public class R4JDBCExamplesTest extends AbstractPersistenceTest {

    // The Derby database instance used for the persistence tests
    private DerbyFhirDatabase database;
	
    private Properties properties;

    public R4JDBCExamplesTest() throws Exception {
    	this.properties = readTestProperties("test.normalized.properties");
    }
    
    @BeforeSuite
    public void bootstrapAndLoad() {
        System.out.println("Bootstrapping database:");
        
        
        System.out.println("Processing examples:");
    }
	
    @Test(groups = { "jdbc-seed" })
    public void perform() throws Exception {
    	
    	R4JDBCExamplesProcessor processor = new R4JDBCExamplesProcessor(persistence, 
    	    () -> createPersistenceContext(),
    	    () -> createHistoryPersistenceContext());
    	
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
    		return getDefaultPersistenceContext();
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
            return getPersistenceContextForHistory(fhc);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }

	@Override
	public FHIRPersistence getPersistenceImpl() throws Exception {
		return new FHIRPersistenceJDBCNormalizedImpl(this.properties, database);
	}

	@Override
    public void bootstrapDatabase() throws Exception {
	    // Create the derby database
	    this.database = new DerbyFhirDatabase();
    }

	@AfterClass
	public void shutdown() throws Exception {
	    System.out.println("Shutting down database");
	    if (this.database != null) {
            this.database.close();
	    }
	}
}
