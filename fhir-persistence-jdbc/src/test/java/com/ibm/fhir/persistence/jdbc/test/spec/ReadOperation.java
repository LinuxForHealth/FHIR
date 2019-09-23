/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Create the resource using the persistence
 * @author rarnold
 *
 */
public class ReadOperation extends BaseOperation {
    private static final Logger logger = Logger.getLogger(ReadOperation.class.getName());


	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
		final Resource resource = tc.getResource();
		final FHIRPersistenceContext context = tc.createPersistenceContext();
		
		final String logicalId = resource.getId().getValue();
		
		logger.fine("Reading: " + logicalId);
		
		Resource newResource = tc.getPersistence().read(context, resource.getClass(), logicalId);
		
        check(tc, resource, newResource, this.getClass().getSimpleName());
        
		// Update the context with the modified resource
		tc.setResource(newResource);
		
	}
}
