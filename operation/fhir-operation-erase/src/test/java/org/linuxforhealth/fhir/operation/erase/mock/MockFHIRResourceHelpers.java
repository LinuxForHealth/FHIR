/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.erase.mock;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.patch.FHIRPatch;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Builder;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.ResourceEraseRecord;
import org.linuxforhealth.fhir.persistence.ResourceEraseRecord.Status;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.persistence.context.FHIRSystemHistoryContext;
import org.linuxforhealth.fhir.persistence.erase.EraseDTO;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.server.spi.operation.FHIRRestOperationResponse;

/**
 * Helper for Mocking failure tests with the FHIR Resource Helpers
 */
public class MockFHIRResourceHelpers implements FHIRResourceHelpers {
    private boolean throwEx;
    private boolean partial;
    private boolean notFound;
    private boolean greaterThanGreatest = false;
    private boolean latest = false;

    public MockFHIRResourceHelpers(boolean throwEx, boolean partial) {
        this.throwEx = throwEx;
        this.partial = partial;
        this.notFound = false;
    }

    public MockFHIRResourceHelpers(boolean throwEx, boolean partial, boolean notFound) {
        this.throwEx = throwEx;
        this.partial = partial;
        this.notFound = notFound;
    }

    public MockFHIRResourceHelpers(boolean throwEx, boolean partial, boolean notFound, boolean greaterThanGreatest, boolean latest) {
        this.throwEx = throwEx;
        this.partial = partial;
        this.notFound = notFound;
        this.greaterThanGreatest = greaterThanGreatest;
        this.latest = latest;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() throws Exception {

        return null;
    }

    @Override
    public int doReindex(FHIROperationContext operationContext, Builder operationOutcomeResult, Instant tstamp, List<Long> indexIds,
        String resourceLogicalId, boolean force) throws Exception {
        return 0;
    }

    @Override
    public ResourceEraseRecord doErase(FHIROperationContext operationContext, EraseDTO eraseDto) throws FHIROperationException {
        if (throwEx) {
            throw FHIROperationUtil.buildExceptionWithIssue("Bad Deal", IssueType.EXCEPTION, new Exception("Test"));
        }

        ResourceEraseRecord record = new ResourceEraseRecord();
        if (partial) {
            record.setStatus(Status.PARTIAL);
        }

        if (notFound) {
            record.setStatus(Status.NOT_FOUND);
        } else if (greaterThanGreatest) {
            record.setStatus(Status.NOT_SUPPORTED_GREATER);
        } else if (latest) {
            record.setStatus(Status.NOT_SUPPORTED_LATEST);
        }
        return record;
    }

    @Override
    public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, boolean doValidation) throws Exception {

        return null;
    }

    @Override
    public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate, boolean doValidation, Integer ifNoneMatch) throws Exception {

        return null;
    }

    @Override
    public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate) throws Exception {

        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception {

        return null;
    }

    @Override
    public SingleResourceResult<? extends Resource> doRead(String type, String id, boolean throwExcOnNull,
            MultivaluedMap<String, String> queryParameters) throws Exception {

        return null;
    }

    @Override
    public SingleResourceResult<? extends Resource> doVRead(String type, String id, String versionId,
            MultivaluedMap<String, String> queryParameters) throws Exception {

        return null;
    }

    @Override
    public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {

        return null;
    }

    @Override
    public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri, String resourceType) throws Exception {

        return null;
    }

    @Override
    public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters,
            String requestUri) throws Exception {

        return null;
    }

    @Override
    public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {

        return null;
    }

    @Override
    public Bundle doBundle(Bundle bundle, boolean skippableUpdates) throws Exception {

        return null;
    }

    @Override
    public List<Long> doRetrieveIndex(FHIROperationContext operationContext, String resourceTypeName, int count,
            java.time.Instant notModifiedAfter, Long afterIndexId) throws Exception {

        return null;
    }

    @Override
    public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters,
            String requestUri, boolean checkIfInteractionAllowed, boolean alwaysIncludeResource) throws Exception {

        return null;
    }

    @Override
    public String generateResourceId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey) {
        return null;
    }

    @Override
    public void validateInteraction(Interaction interaction, String resourceType) throws FHIROperationException {
    }

    @Override
    public FHIRRestOperationResponse doCreateMeta(FHIRPersistenceEvent event, List<Issue> warnings, String type, Resource resource,
            String ifNoneExist) throws Exception {
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreatePersist(FHIRPersistenceEvent event, List<Issue> warnings, Resource resource,
            PayloadPersistenceResponse offloadResponse) throws Exception {
        return null;
    }

    @Override
    public FHIRRestOperationResponse doUpdateMeta(FHIRPersistenceEvent event, String type, String id, FHIRPatch patch,
            Resource newResource, String ifMatchValue, String searchQueryString, boolean skippableUpdate, Integer ifNoneMatch,
            boolean doValidation, List<Issue> warnings) throws Exception {
        return null;
    }

    @Override
    public FHIRRestOperationResponse doPatchOrUpdatePersist(FHIRPersistenceEvent event, String type, String id, boolean isPatch,
            Resource newResource, Resource prevResource, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch,
            PayloadPersistenceResponse offloadResponse) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> buildPersistenceEventProperties(String type, String id, String version, FHIRSearchContext searchContext,
            FHIRSystemHistoryContext systemHistoryContext) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<Issue> validateResource(Resource resource) throws FHIROperationException {
        return Collections.emptyList();
    }
}