/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;

public class DeleteOperation extends BaseOperation {

    @Override
    public void process(TestContext tc) throws FHIRPersistenceException {
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();

        Resource deletedResource = FHIRPersistenceTestSupport.delete(tc.getPersistence(), context, resource);

        // Update the context with the modified resource
        tc.setResource(deletedResource);
    }
}