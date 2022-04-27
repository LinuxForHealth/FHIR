/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import static com.ibm.fhir.model.type.String.string;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.exception.FHIRResourceDeletedException;
import com.ibm.fhir.server.exception.FHIRResourceNotFoundException;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;
import com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Visitor used to initiate payload offloading when supported by the
 * persistence layer
 */
public class FHIRRestInteractionVisitorOffload extends FHIRRestInteractionVisitorBase {

    // True if there's a bundle-level transaction, null otherwise
    final boolean transaction;

    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestInteractionVisitorOffload(boolean transaction, FHIRResourceHelpers helpers, Map<String, String> localRefMap, Entry[] responseBundleEntries) {
        super(helpers, localRefMap, responseBundleEntries);
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri, boolean checkInteractionAllowed) throws Exception {
        // NOP. Nothing to do
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
            throws Exception {
        // NOP for now. TODO: when offloading payload, start an async optimistic read of the id/version payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, boolean throwExcOnNull,
            MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // NOP for now. TODO: when offloading payload, try an optimistic async read of the latest payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
            throws Exception {
        // NOP for now. TODO: optimistic async reads, if we can scope them properly
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, FHIRPersistenceEvent event, List<Issue> warnings, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, Resource resource, String ifNoneExist, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception {
        // Note the offloadResponse will be null when passed in to this method, because
        // we only initiate the offload in this particular visitor - the fact that we have
        // the parameter defined is just a side-effect of the visitor pattern we're using.

        // Use doOperation so we can implement common exception handling in one place
        return doOperation(entryIndex, requestDescription, accumulatedTime, () -> {
            // Try offloading storage of the payload. The offloadResponse will be null if not supported
            String resourcePayloadKey = UUID.randomUUID().toString();
            int newVersionNumber = Integer.parseInt(resource.getMeta().getVersionId().getValue());
            PayloadPersistenceResponse actualOffloadResponse = storePayload(resource, resource.getId(), newVersionNumber, resourcePayloadKey);

            // Resource doesn't change, so no need to return it
            return new FHIRRestOperationResponse(null, null, actualOffloadResponse);
        });
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, Resource resource, Resource prevResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch, PayloadPersistenceResponse offloadResponse) throws Exception {

        // Use doOperation for common exception handling
        return doOperation(entryIndex, requestDescription, accumulatedTime, () -> {
            // Try offloading storage of the payload. The offloadResponse will be null if not supported
            String resourcePayloadKey = UUID.randomUUID().toString();
            int newVersionNumber = Integer.parseInt(resource.getMeta().getVersionId().getValue());
            PayloadPersistenceResponse actualOffloadResponse = storePayload(resource, resource.getId(), newVersionNumber, resourcePayloadKey);

            // Resource doesn't change, so no need to return it
            FHIRRestOperationResponse result = new FHIRRestOperationResponse(null, null, actualOffloadResponse);
            result.setDeleted(isDeleted);

            return result;
        });
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
        String type, String id, Resource resource, Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, List<Issue> warnings, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception {
        // Use doOperation for common exception handling
        return doOperation(entryIndex, requestDescription, accumulatedTime, () -> {

            // Try offloading storage of the payload. The offloadResponse will be null if not supported
            String resourcePayloadKey = UUID.randomUUID().toString();
            int newVersionNumber = Integer.parseInt(resource.getMeta().getVersionId().getValue());
            PayloadPersistenceResponse actualOffloadResponse = storePayload(resource, resource.getId(), newVersionNumber, resourcePayloadKey);

            // Resource doesn't change, so no need to return it
            return new FHIRRestOperationResponse(null, null, actualOffloadResponse);
        });
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
            String versionId, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id, String searchQueryString) throws Exception {
        // We no longer store the payload for a deleted resource
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry, String requestDescription, long accumulatedTime) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long accumulatedTime, Status status, Entry responseEntry) throws Exception {
        // NOP
        return null;
    }

    /**
     * If payload offloading is supported by the persistence layer, store the given resource. This
     * can be an async operation which we resolve at the end just prior to the transaction being
     * committed. If offloading isn't enabled, the operation is a NOP and the persistence layer
     * returns null.
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param resourcePayloadKey
     * @return
     */
    protected PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey) throws Exception {
       return helpers.storePayload(resource, logicalId, newVersionNumber, resourcePayloadKey);
    }

    /**
     * Unified exception handling for each of the operation calls
     *
     * @param entryIndex
     * @param requestDescription
     * @param accumulatedTime
     * @param v
     * @return
     * @throws Exception
     */
    private FHIRRestOperationResponse doOperation(int entryIndex, String requestDescription, long accumulatedTime, Callable<FHIRRestOperationResponse> v) throws Exception {
        final boolean failFast = transaction;
        final long start = System.nanoTime();
        try {
            return v.call();
        } catch (FHIRResourceNotFoundException e) {
            if (failFast) {
                String msg = "Error while processing request bundle.";
                throw new FHIRRestBundledRequestException(msg, e).withIssue(e.getIssues());
            }

            // Record the error as an entry in the result bundle
            Entry entry = Entry.builder()
                    .resource(FHIRUtil.buildOperationOutcome(e, false))
                    .response(Entry.Response.builder()
                        .status(SC_NOT_FOUND_STRING)
                        .build())
                    .build();
            final long elapsed = System.nanoTime() - start;
            setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime + elapsed);
        } catch (FHIRResourceDeletedException e) {
            if (failFast) {
                String msg = "Error while processing request bundle.";
                throw new FHIRRestBundledRequestException(msg, e).withIssue(e.getIssues());
            }

            Entry entry = Entry.builder()
                    .resource(FHIRUtil.buildOperationOutcome(e, false))
                    .response(Entry.Response.builder()
                        .status(SC_GONE_STRING)
                        .build())
                    .build();
            final long elapsed = System.nanoTime() - start;
            setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime + elapsed);
        } catch (FHIROperationException e) {
            if (failFast) {
                String msg = "Error while processing request bundle.";
                throw new FHIRRestBundledRequestException(msg, e).withIssue(e.getIssues());
            }

            Status status;
            if (e instanceof FHIRSearchException) {
                status = Status.BAD_REQUEST;
            } else {
                status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            }

            Entry entry = Entry.builder()
                    .resource(FHIRUtil.buildOperationOutcome(e, false))
                    .response(Entry.Response.builder()
                        .status(string(Integer.toString(status.getStatusCode())))
                        .build())
                    .build();
            final long elapsed = System.nanoTime() - start;
            setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime + elapsed);
        }

        return null;
    }
}