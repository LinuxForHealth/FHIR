/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.ResourceChangeLogRecord;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.payload.PayloadKey;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 *
 */
public class MockPersistenceImpl implements FHIRPersistence {

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) 
            throws FHIRPersistenceException {
    	return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> createWithMeta(FHIRPersistenceContext context, T resource) 
            throws FHIRPersistenceException {
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

    @Override
    public int reindex(FHIRPersistenceContext context, OperationOutcome.Builder oob, Instant tstamp, List<Long> indexIds,
            String resourceLogicalId) throws FHIRPersistenceException {
        return 0;
    }

    @Override
    public String generateResourceId() {
        return null;
    }

    @Override
    public ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType, Instant fromLastModified, Instant toLastModified,
            Function<ResourcePayload, Boolean> process) throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ResourceChangeLogRecord> changes(int resourceCount, Instant fromLastModified, Long afterResourceId, String resourceTypeName)
            throws FHIRPersistenceException {
        return Collections.emptyList();
    }

    @Override
    public List<Long> retrieveIndex(int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException {
        return Collections.emptyList();
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> updateWithMeta(FHIRPersistenceContext context, T resource)
            throws FHIRPersistenceException {
        return null;
    }

    @Override
    public Future<PayloadKey> storePayload(Resource resource, String logicalId, int newVersionNumber) {
        return null;
    }
}