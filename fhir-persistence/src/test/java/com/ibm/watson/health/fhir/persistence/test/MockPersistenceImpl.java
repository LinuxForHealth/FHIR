/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.test;

import java.util.List;

import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 *
 */
public class MockPersistenceImpl implements FHIRPersistence {

    @Override
    public Resource create(FHIRPersistenceContext context, Resource resource) throws FHIRPersistenceException {
    	return null;
    }

    @Override
    public Resource read(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public Resource vread(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId, String versionId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public Resource update(FHIRPersistenceContext context, String logicalId, Resource resource) throws FHIRPersistenceException {
    	return null;
    }

    @Override
    public List<Resource> history(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        return null;
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {
        return null;
    }
}
