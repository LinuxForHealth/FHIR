/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.DriverMetrics;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Create the resource using the persistence

 *
 */
public class CreateOperation extends BaseOperation {
    
    private final DriverMetrics metrics;
    public CreateOperation() {
        this.metrics = null;
    }

    /**
     * Public constructor initializing this with a DriverMetrics
     * @param dm
     */
    public CreateOperation(DriverMetrics dm) {
        this.metrics = dm;
    }

    @Override
    public void process(TestContext tc) throws FHIRPersistenceException {
        
        long start = 0;
        
        if (metrics != null) {
            start = System.nanoTime();
        }
        
        final FHIRPersistenceContext context = tc.createPersistenceContext();
        final Resource resource = tc.getResource();

        // This needs to be a new resource. If it's not, then the
        // create will fail with a version id mismatch error
        Resource newResource = tc.getPersistence().create(context, resource).getResource();
        check(tc, resource, newResource, this.getClass().getSimpleName());
        
        // Update the context with the modified resource
        tc.setResource(newResource);
        
        if (metrics != null) {
            // record some instrumentation
            metrics.addPostTime((System.nanoTime() - start) / DriverMetrics.NANOS_MS);
        }
    }
}
