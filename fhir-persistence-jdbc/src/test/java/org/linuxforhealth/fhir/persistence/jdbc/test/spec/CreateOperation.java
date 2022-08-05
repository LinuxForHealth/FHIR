/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.spec;

import java.time.ZoneOffset;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.spec.test.DriverMetrics;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.util.FHIRPersistenceUtil;

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
        // create will fail with a version id mismatch error.
        final String logicalId = tc.getPersistence().generateResourceId();
        final Instant lastUpdated = Instant.now(ZoneOffset.UTC);
        Resource newResource = FHIRPersistenceUtil.copyAndSetResourceMetaFields(resource, logicalId, 1, lastUpdated);
        newResource = tc.getPersistence().create(context, newResource).getResource();
        check(tc, resource, newResource, this.getClass().getSimpleName());
        
        // Update the context with the modified resource
        tc.setResource(newResource);
        
        if (metrics != null) {
            // record some instrumentation
            metrics.addPostTime((System.nanoTime() - start) / DriverMetrics.NANOS_MS);
        }
    }
}
