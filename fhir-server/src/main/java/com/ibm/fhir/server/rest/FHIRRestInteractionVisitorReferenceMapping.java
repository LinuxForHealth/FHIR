/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import static com.ibm.fhir.model.type.String.string;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.payload.PayloadKey;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;
import com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Visitor used to update references in an incoming resource prior to persistence
 */
public class FHIRRestInteractionVisitorReferenceMapping extends FHIRRestInteractionVisitorBase {

    // True if there's a bundle-level transaction, null otherwise
    final boolean transaction;

    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestInteractionVisitorReferenceMapping(boolean transaction, FHIRResourceHelpers helpers, Map<String, String> localRefMap, Entry[] responseBundleEntries) {
        super(helpers, localRefMap, responseBundleEntries);
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        // NOP. Nothing to do
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
        throws Exception {
        // NOP for now. TODO: when offloading payload, start an async optimistic read of the id/version payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
        MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // NOP for now. TODO: when offloading payload, try an optimistic async read of the latest payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
        throws Exception {
        // NOP for now. TODO: optimistic async reads, if we can scope them properly
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, FHIRPersistenceEvent event, List<Issue> warnings, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, Resource resource, String ifNoneExist, String localIdentifier) throws Exception {

        // Use doOperation so we can implement common exception handling in one place
        return doOperation(entryIndex, requestDescription, initialTime, () -> {

            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            final Resource finalResource = visitor.getResult(); // finalResource immutable

            // Try offloading storage of the payload. The offloadResponse will be null if not supported
            int newVersionNumber = Integer.parseInt(finalResource.getMeta().getVersionId().getValue());
            Future<PayloadKey> offloadResponse = storePayload(finalResource, finalResource.getId(), newVersionNumber);

            // Pass back the updated resource so it can be used in the next phase if required
            return new FHIRRestOperationResponse(finalResource, finalResource.getId(), offloadResponse);
        });
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL,
        long initialTime, String type, String id, Resource resource, Resource prevResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch) throws Exception {

        // Use doOperation for common exception handling
        return doOperation(entryIndex, requestDescription, initialTime, () -> {

            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            Resource newResource = visitor.getResult();

            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                addLocalRefMapping(localIdentifier, newResource);
            }

            // TODO support payload offload here

            // Pass back the updated resource so it can be used in the next phase
            FHIRRestOperationResponse result = new FHIRRestOperationResponse(null, null, newResource);
            result.setDeleted(isDeleted);
            
            return result;
        });
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime,
        String type, String id, Resource resource, Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, List<Issue> warnings, String localIdentifier) throws Exception {
        // Use doOperation for common exception handling
        return doOperation(entryIndex, requestDescription, initialTime, () -> {

            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            Resource newResource = visitor.getResult();

            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                addLocalRefMapping(localIdentifier, newResource);
            }

            // TODO support payload offload here

            // Pass back the updated resource so it can be used in the next phase
            return new FHIRRestOperationResponse(null, null, newResource);
        });
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
            String versionId, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, String searchQueryString) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long initialTime, Status status, Entry responseEntry) throws Exception {
        // NOP
        return null;
    }

    /**
     * If payload offloading is supported by the persistence layer, store the given resource. This
     * can be an async operation which we resolve at the end just prior to the transaction being
     * committed. If offloading isn't supported, the persistence layer returns null and the operation
     * is a NOP.
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @return
     */
    protected Future<PayloadKey> storePayload(Resource resource, String logicalId, int newVersionNumber) throws Exception {
       return helpers.storePayload(resource, logicalId, newVersionNumber);
    }

    /**
     * Unified exception handling for each of the operation calls
     * @param entryIndex
     * @param v
     * @param failFast
     * @param requestDescription
     * @param initialTime
     * @throws Exception
     */
    private FHIRRestOperationResponse doOperation(int entryIndex, String requestDescription, long initialTime, Callable<FHIRRestOperationResponse> v) throws Exception {
        final boolean failFast = transaction;
        try {
            return v.call();
        } catch (FHIRPersistenceResourceNotFoundException e) {
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
            setEntryComplete(entryIndex, entry, requestDescription, initialTime);
        } catch (FHIRPersistenceResourceDeletedException e) {
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
            setEntryComplete(entryIndex, entry, requestDescription, initialTime);
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
            setEntryComplete(entryIndex, entry, requestDescription, initialTime);
        }

        return null;
    }
}