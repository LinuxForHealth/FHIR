/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 *
 */
public class MockPersistenceImpl implements FHIRPersistence {

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException {
    	return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException {
    	return null;
    }

    @Override
    public <T extends Resource> MultiResourceResult<T> history(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public MultiResourceResult<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException {
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
