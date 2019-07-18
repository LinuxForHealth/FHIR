/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class VReadOperation extends BaseOperation {


	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
	    	    
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();
        
        final String logicalId = resource.getId().getValue();
        final String versionId = resource.getMeta().getVersionId().getValue();
        
        Resource newResource = tc.getPersistence().vread(context, resource.getClass(), logicalId, versionId);
        check(tc, resource, newResource, this.getClass().getSimpleName());

        // This operation doesn't modify the resource, so we don't
        // bother updating the TestContext
	}

}
