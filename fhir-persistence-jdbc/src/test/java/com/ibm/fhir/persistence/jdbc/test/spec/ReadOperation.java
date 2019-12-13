/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.DriverMetrics;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Create the resource using the persistence
 *
 */
public class ReadOperation extends BaseOperation {
    private static final Logger logger = Logger.getLogger(ReadOperation.class.getName());

    private final DriverMetrics metrics;

    // The number of times we repeat. Useful for quick performance analysis
    private final int readIterations;

    /**
     * Public constructor without instrumentation
     */
    public ReadOperation() {
        this.metrics = null;
        this.readIterations = 1;
    }

    /**
     * Public constructor initialized with instrumentation - used to help baseline performance
     * @param dm
     */
    public ReadOperation(DriverMetrics dm, int readIterations) {
        this.metrics = dm;
        this.readIterations = readIterations;
    }

	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
	    for (int i=0; i<this.readIterations; i++) {
    	    long start = 0;
    	    if (metrics != null) {
    	        start = System.nanoTime();
    	    }
    		final Resource resource = tc.getResource();
    		final FHIRPersistenceContext context = tc.createPersistenceContext();
    		
    		final String logicalId = resource.getId();

    		if (logger.isLoggable(Level.FINE)) {
    		    logger.fine("Reading: " + logicalId);
    		}
    		
    		SingleResourceResult<? extends Resource> newResource = tc.getPersistence().read(context, resource.getClass(), logicalId);
    		
            check(tc, resource, newResource.getResource(), this.getClass().getSimpleName());
            
    		// Update the context with the modified resource
    		tc.setResource(newResource.getResource());
    
    		if (metrics != null) {
    		    // some simple instrumentation
    		    metrics.addGetTime((System.nanoTime() - start) / DriverMetrics.NANOS_MS);
    		}
	    }
	}
}
