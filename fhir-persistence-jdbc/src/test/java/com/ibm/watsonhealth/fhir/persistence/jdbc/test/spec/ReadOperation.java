/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.util.ResourceFingerprintVisitor;
import com.ibm.watsonhealth.fhir.persistence.util.SaltHash;

/**
 * Create the resource using the persistence
 * @author rarnold
 *
 */
public class ReadOperation implements ITestResourceOperation {


	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
		final Resource resource = tc.getResource();
		final FHIRPersistenceContext context = tc.createPersistenceContext();
		
		final String logicalId = resource.getId().getValue();
		
		Resource newResource = tc.getPersistence().read(context, resource.getClass(), logicalId);
		
		// The persistence layer changes the resource by updating things like the id and last-modified
		// time, so we can't use Resource#equals() to compare. Instead, we generate a resource "fingerprint"
		// which is a SHA-256 digest of the entire resource, skipping stuff that might be changed
		// by the FHIR server.
        SaltHash originalFingerprint = tc.getOriginalFingerprint();
        ResourceFingerprintVisitor v = new ResourceFingerprintVisitor(originalFingerprint.getSalt());
        newResource.accept(newResource.getClass().getSimpleName(), v);
        
        if (!v.getSaltAndHash().equals(originalFingerprint)) {
            throw new AssertionError("Fingerprint mismatch after READ for " + resource.getClass().getSimpleName());
        }
        
		// Update the context with the modified resource
		tc.setResource(newResource);
		
	}
}
