/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.spec;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.util.FHIRPersistenceTestSupport;

public class DeleteOperation extends BaseOperation {

    @Override
    public void process(TestContext tc) throws FHIRPersistenceException {
        final Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();

        // Resource is no longer stored with the deletion marker, so delete
        // doesn't return anything
        FHIRPersistenceTestSupport.delete(tc.getPersistence(), context, resource);
    }
}