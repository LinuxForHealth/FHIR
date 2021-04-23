/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.SaltHash;
import com.ibm.fhir.model.util.test.ResourceComparatorVisitor;
import com.ibm.fhir.model.visitor.ResourceFingerprintVisitor;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Create the resource using the persistence
 *
 */
public abstract class BaseOperation implements ITestResourceOperation {
    
    /**
     * Perform a comparison between the original resource and new value returned by the persistence
     * layer
     * @param tc
     * @param resource
     * @param newResource
     * @param oper
     * @throws FHIRPersistenceException
     */
    protected void check(TestContext tc, Resource resource, Resource newResource, String oper) throws FHIRPersistenceException {
        // Fingerprint the new resource using the same salt and make sure it matches
        SaltHash originalFingerprint = tc.getOriginalFingerprint();
        ResourceFingerprintVisitor v = new ResourceFingerprintVisitor(originalFingerprint.getSalt());
        newResource.accept(newResource.getClass().getSimpleName(), v);
        
        if (!v.getSaltAndHash().equals(originalFingerprint)) {
            
            // Let's run the comparator so that we can report on any difference between the resources
            ResourceComparatorVisitor originals = new ResourceComparatorVisitor();
            tc.getResource().accept(tc.getResource().getClass().getSimpleName(), originals);
            
            ResourceComparatorVisitor others = new ResourceComparatorVisitor();
            newResource.accept(newResource.getClass().getSimpleName(), others);
            
            // Perform a bi-directional comparison of values in the maps
            ResourceComparatorVisitor.compare(originals.getValues(), others.getValues());
            
            throw new FHIRPersistenceException("Fingerprint mismatch after " + oper + " for " + resource.getClass().getSimpleName());
        }
        
    }
}
