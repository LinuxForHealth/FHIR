/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

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
		
		// TODO check the resource
		
		
		// Update the context with the modified resource
		tc.setResource(newResource);
		
	}
}
