/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

public class DeleteOperation extends BaseOperation {

    @Override
    public void process(TestContext tc) throws FHIRPersistenceException {
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();

        final String logicalId = resource.getId();
        
        Resource newResource = tc.getPersistence().delete(context, resource.getClass(), logicalId).getResource();
        
        // Update the context with the modified resource. This is the deletion marker
        // and so should be substantially different when compared with the actual resource
        tc.setResource(newResource);
    }

}
