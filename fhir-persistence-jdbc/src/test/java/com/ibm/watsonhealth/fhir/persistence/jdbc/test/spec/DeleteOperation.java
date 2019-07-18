/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class DeleteOperation extends BaseOperation {

	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();

        final String logicalId = resource.getId().getValue();
        
        Resource newResource = tc.getPersistence().delete(context, resource.getClass(), logicalId);
        
        // Update the context with the modified resource. This is the deletion marker
        // and so should be substantially different when compared with the actual resource
        tc.setResource(newResource);
	}

}
