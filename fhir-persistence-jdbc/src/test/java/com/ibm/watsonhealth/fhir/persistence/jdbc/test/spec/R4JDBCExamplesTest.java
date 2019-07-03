/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractPersistenceTest;

public class R4JDBCExamplesTest extends AbstractPersistenceTest {
	
    private Properties properties;

    public R4JDBCExamplesTest() throws Exception {
    	this.properties = readTestProperties("test.normalized.properties");
    }
	
    @Test
    public void perform() throws Exception {
    	
    	R4JDBCExamplesDriver driver = new R4JDBCExamplesDriver(persistence, () -> createPersistenceContext());
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

	@Override
	public FHIRPersistence getPersistenceImpl() throws Exception {
		return new FHIRPersistenceJDBCNormalizedImpl(this.properties);
	}

	@Override
    public void bootstrapDatabase() throws Exception {
		// Create the derby database
    }

}
