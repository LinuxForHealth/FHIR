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
public class CreateOperation implements ITestResourceOperation {

	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
	    
		final Resource resource = tc.getResource();
		final FHIRPersistenceContext context = tc.createPersistenceContext();
		
		Resource newResource = tc.getPersistence().create(context, resource);

		// Fingerprint the new resource using the same salt and make sure it matches
		SaltHash originalFingerprint = tc.getOriginalFingerprint();
		ResourceFingerprintVisitor v = new ResourceFingerprintVisitor(originalFingerprint.getSalt());
		newResource.accept(newResource.getClass().getSimpleName(), v);
		
		if (!v.getSaltAndHash().equals(originalFingerprint)) {
		    throw new AssertionError("Fingerprint mismatch after CREATE for " + resource.getClass().getSimpleName());
		}
		
		// Update the context with the modified resource
		tc.setResource(newResource);
	}
}
