/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import static com.ibm.fhir.model.type.String.string;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;
import com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Used to prepare bundle entries before they hit the persistence layer. The prepare phase
 * consists of 3 actions:
 *   1. Generate a system-assigned identifier for the resource if it doesn't have one
 *   2. Look up any conditional references
 *   3. Update References in the resource which are conditional or local
 * If the resource is modified, it is returned inside the FHIRRestOperationResponse.
 * 
 * If the bundle type is a transaction, the persistence layer transaction must be started
 * before this visitor is used
 */
public class FHIRRestInteractionVisitorReferenceMapping extends FHIRRestInteractionVisitorBase {
    private static final Logger log = Logger.getLogger(FHIRRestInteractionVisitorPersist.class.getName());
    
    private final String bundleRequestCorrelationId;

    // Set if there's a bundle-level transaction, null otherwise
    final FHIRTransactionHelper txn;
    
    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestInteractionVisitorReferenceMapping(FHIRTransactionHelper txn, FHIRResourceHelpers helpers, String bundleRequestCorrelationId, Map<String, String> localRefMap, Entry[] responseBundleEntries) {
        super(helpers, localRefMap, responseBundleEntries);
        this.txn = txn;
        this.bundleRequestCorrelationId = bundleRequestCorrelationId;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        // NOP. Nothing to do
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
        throws Exception {
        // NOP for now. TODO: when offloading payload, start an async read of the id/version payload
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
        MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // NOP for now. TODO: when offloading payload, try an optimistic async read of the latest payload
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
        throws Exception {
        // NOP for now. TODO: optimistic async reads, if we can scope them properly
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, List<Issue> warnings, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, Resource resource, String ifNoneExist, String localIdentifier) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        
        // Use doOperation so we can implement common exception handling in one place
        return doOperation(entryIndex, txn != null, requestDescription, initialTime, () -> {
                
            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            final Resource finalResource = visitor.getResult();
            
            // Resource processing is now complete and from this point on, we can treat the resource
            // as immutable.
            
            // TODO: If the persistence layer supports offloading, we can store now. The offloadResponse
            // will be null if offloading is not supported
            int newVersionNumber = Integer.parseInt(finalResource.getMeta().getVersionId().getValue());
            Instant lastUpdated = finalResource.getMeta().getLastUpdated();
            Future<FHIRRestOperationResponse> offloadResponse = storePayload(finalResource, finalResource.getId(), 
                newVersionNumber, lastUpdated);
            
            // Pass back the updated resource so it can be used in the next phase if required
            return new FHIRRestOperationResponse(finalResource, finalResource.getId(), newVersionNumber, lastUpdated, offloadResponse);
        });
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, 
        long initialTime, String type, String id, Resource resource, Resource prevResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);

        // Use doOperation for common exception handling
        return doOperation(entryIndex, txn != null, requestDescription, initialTime, () -> {
            
            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            Resource newResource = visitor.getResult();
            
            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                addLocalRefMapping(localIdentifier, newResource);
            }
            
            // Pass back the updated resource so it can be used in the next phase
            return new FHIRRestOperationResponse(null, null, newResource);
        });
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, 
        String type, String id, Resource newResource, Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, List<Issue> warnings) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
        String versionId, String operationName, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, String searchQueryString) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry) throws Exception {
        // NOP
        return null;
    }
    
    private void logStart(int entryIndex, String requestDescription, FHIRUrlParser requestURL) {
        // Log our initial info message for this request.
        if (log.isLoggable(Level.FINE)) {
            log.fine("Processing bundled request: " + requestDescription.toString());
            if (log.isLoggable(Level.FINER)) {
                log.finer("--> path: '" + requestURL.getPath() + "'");
                log.finer("--> query: '" + requestURL.getQuery() + "'");
            }
        }

    }

    @Override
    public FHIRRestOperationResponse issue(int entryIndex, long initialTime, Status status, Entry responseEntry) throws Exception {
        // NOP
        return null;
    }
    
    /**
     * Get the current time which can be used for the lastUpdated field
     * @return current time in UTC
     */
    protected Instant getCurrentInstant() {
        return Instant.now(ZoneOffset.UTC);

    }

    /**
     * If payload offloading is supported by the persistence layer, store the given resource. This
     * can be an async operation which we resolve at the end just prior to the transaction being
     * committed.
     * TODO: use a dedicated class instead of FHIRRestOperationResponse
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param lastUpdated
     * @return
     */
    protected Future<FHIRRestOperationResponse> storePayload(Resource resource, String logicalId, int newVersionNumber, Instant lastUpdated) {
       return helpers.storePayload(resource, logicalId, newVersionNumber, lastUpdated); 
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
    private FHIRRestOperationResponse doOperation(int entryIndex, boolean failFast, String requestDescription, long initialTime, Callable<FHIRRestOperationResponse> v) throws Exception {
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
            setResponseEntry(entryIndex, entry);
            logBundledRequestCompletedMsg(requestDescription.toString(), initialTime, SC_NOT_FOUND);
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
            setResponseEntry(entryIndex, entry);
            logBundledRequestCompletedMsg(requestDescription.toString(), initialTime, SC_GONE);
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
            setResponseEntry(entryIndex, entry);
            logBundledRequestCompletedMsg(requestDescription.toString(), initialTime, status.getStatusCode());
        }
        
        return null;
    }
}