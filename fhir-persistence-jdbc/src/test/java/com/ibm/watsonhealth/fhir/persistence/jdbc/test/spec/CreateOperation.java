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
public class CreateOperation extends BaseOperation {
    @Override
    public void process(TestContext tc) throws FHIRPersistenceException {
        
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();
        
        Resource newResource = tc.getPersistence().create(context, resource);
        check(tc, resource, newResource, this.getClass().getSimpleName());

        
        // Update the context with the modified resource
        tc.setResource(newResource);
    }
}
