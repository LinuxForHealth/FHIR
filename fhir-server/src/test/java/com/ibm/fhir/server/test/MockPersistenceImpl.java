/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Builder;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.ResourceChangeLogRecord;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.server.util.FHIRRestHelperTest;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 */
public class MockPersistenceImpl implements FHIRPersistence {
    int id = 0;

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException {
        throw new IllegalStateException("API no longer used; provided for backward compatibility only");
        // return create(context, resource, generateResourceId(), 1, Instant.now());
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource, String logicalId, int versionNumber,
        Instant lastUpdated) throws FHIRPersistenceException {
        
        // since 1869 the persistence layer no longer modifies the resource. But we can do
        // a quick check here to make sure that the logicalId and version match our meta
        if (!resource.getId().equals(logicalId)) {
            throw new IllegalStateException("Resource.id '" + resource.getId() + "' does not match given logicalId: '" + logicalId + "'");
        }
        
        String versionStr = Integer.toString(versionNumber);
        if (!versionStr.equals(resource.getMeta().getVersionId().getValue())) {
            throw new IllegalStateException("Resource.meta.versionId '" + resource.getMeta().getVersionId() + "' does not match given versionNumber: '" + versionStr + "'");
        }
        
        SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                .success(true)
                .resource(resource);
        return resultBuilder.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        // TODO. Why this logic? Definitely worthy of a comment
        if (logicalId.startsWith("generated")) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(null).build();
        } else {
            T updatedResource = (T) Patient.builder().id("test").meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build()).build();
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(updatedResource).build();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        if (logicalId.startsWith("generated")) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(null).build();
        } else {
            T updatedResource = (T) Patient.builder().id("test").meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build()).build();
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(updatedResource).build();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException {
        // For this mock, the versionId doesn't matter
        return update(context, logicalId, -1, resource);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, int newVersionId, T resource)
        throws FHIRPersistenceException {
//        final T updatedResource;
        OperationOutcome operationOutcome = null;
        if (resource.getLanguage() != null && resource.getLanguage().getValue().equals("en-US")) {
            operationOutcome = FHIRRestHelperTest.ID_SPECIFIED;
        }
//        if (resource.getId().startsWith("generated")) {
//            updatedResource = (T) resource.toBuilder().meta(Meta.builder().versionId(Id.of("1")).build()).build();
//        } else {
//            updatedResource = (T) resource.toBuilder().meta(Meta.builder().versionId(Id.of("2")).build()).build();
//        }
        SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                .success(true)
                .resource(resource) // persistence layer should no longer change the resource!
                .outcome(operationOutcome);
        return resultBuilder.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> MultiResourceResult<T> history(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException {
        T updatedResource = (T) Patient.builder().id("test").meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build()).build();
        return new MultiResourceResult.Builder<T>()
                .success(true)
                .resource(updatedResource).build();
    }

    @Override
    public MultiResourceResult<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        return new MultiResourceResult.Builder<>()
                .success(true)
                .resource(new ArrayList<>()).build();
    }

    @Override
    public boolean isTransactional() {
        return true;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        return new MockTransactionAdapter();
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {
        return null;
    }

    @Override
    public String generateResourceId() {
        return "generated-" + id++;
    }

    @Override
    public int reindex(FHIRPersistenceContext context, Builder operationOutcomeResult, java.time.Instant tstamp, List<Long> indexIds,
        String resourceLogicalId) throws FHIRPersistenceException {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> SingleResourceResult<T> delete(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException {
        T updatedResource = (T) Patient.builder().id("test").meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build()).build();
        SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                .success(true)
                .resource(updatedResource);
        return resultBuilder.build();
    }

    @Override
    public ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType, java.time.Instant fromLastModified,
        java.time.Instant toLastModified, Function<ResourcePayload, Boolean> process) throws FHIRPersistenceException {
        // NOP
        return null;
    }

    @Override
    public List<ResourceChangeLogRecord> changes(int resourceCount, java.time.Instant fromLastModified, Long afterResourceId, String resourceTypeName)
        throws FHIRPersistenceException {
        // NOP
        return null;
    }

    @Override
    public List<Long> retrieveIndex(int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException {
        // NOP
        return null;
    }
}