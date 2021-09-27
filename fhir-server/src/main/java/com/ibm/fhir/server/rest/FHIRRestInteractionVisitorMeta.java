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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.CollectingVisitor;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;
import com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper;
import com.ibm.fhir.server.util.FHIRRestHelper.Interaction;

/**
 * Perform any conditional create searches. In order to correctly resolve all
 * the local references within a bundle, we need to process all conditional
 * create searches first so that we have the ids we need in the localRefMap
 * before we start to update any references.
 */
public class FHIRRestInteractionVisitorMeta extends FHIRRestInteractionVisitorBase {
    private static final Logger log = Logger.getLogger(FHIRRestInteractionVisitorPersist.class.getName());
    
    private static final boolean DO_VALIDATION = true;
    
    // Set if there's a bundle-level transaction, null otherwise
    final FHIRTransactionHelper txn;
    
    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestInteractionVisitorMeta(FHIRTransactionHelper txn, FHIRResourceHelpers helpers, Map<String, String> localRefMap, Entry[] responseBundleEntries) {
        super(helpers, localRefMap, responseBundleEntries);
        this.txn = txn;
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

        // Skip CREATE if validation failed
        // TODO the logic in the old buildLocalRefMap uses SC_OK_STRING
        if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
            return null;
        }
        
        // Use doInteraction so we can implement common exception handling in one place
        return doInteraction(entryIndex, txn != null, requestDescription, initialTime, () -> {
            
            // Validate that interaction is allowed for given resource type
            helpers.validateInteraction(Interaction.CREATE, type);
    
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(resource, helpers.buildPersistenceEventProperties(type, null, null, null));
            
            // Inject the meta to the resource after optionally checking for ifNoneExist
            FHIRRestOperationResponse prepResponse = helpers.doCreateMeta(txn, event, warnings, type, resource, ifNoneExist, !DO_VALIDATION);
            if (prepResponse != null) {
                // ifNoneExist returned a result record it in the response bundle then no more
                // processing required
                Entry entry = buildResponseBundleEntry(prepResponse, null, requestDescription, initialTime);
                setResponseEntry(entryIndex, entry);
                
                if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                    addLocalRefMapping(localIdentifier, prepResponse.getResource());
                }
                
                return null;
            }
            
            // Get the updated resource with the meta info
            Resource resourceWithMeta = event.getFhirResource();
            
            if (txn != null) {
                resolveConditionalReferences(resourceWithMeta);
            }
                
            final int newVersionNumber = Integer.parseInt(resourceWithMeta.getMeta().getVersionId().getValue());
            final Instant lastUpdated = resourceWithMeta.getMeta().getLastUpdated();
            final String logicalId = resourceWithMeta.getId();
            
            // Add the mapping between localIdentifier and the new logicalId from the resource
            if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                addLocalRefMapping(localIdentifier, resourceWithMeta);
            }
            
            // Pass back the updated resource so it can be used in the next phase if required
            return new FHIRRestOperationResponse(resourceWithMeta, logicalId, newVersionNumber, lastUpdated, null);
        });
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, 
        String type, String id, Resource resource, Resource prevResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);

        // Skip UPDATE if validation failed
        // TODO the logic in the old buildLocalRefMap uses SC_OK_STRING
        if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
            return null;
        }

        // Process the first (meta) phase of the update interaction
        return doInteraction(entryIndex, txn != null, requestDescription, initialTime, () -> {
            FHIRRestOperationResponse metaResponse = helpers.doUpdateMeta(type, id, null, resource, ifMatchValue, searchQueryString, skippableUpdate, !DO_VALIDATION, warnings);

            // Get the updated resource with the meta info
            Resource resourceWithMeta = metaResponse.getResource();
            
            if (txn != null) {
                resolveConditionalReferences(resourceWithMeta);
            }
                
//            final int newVersionNumber = Integer.parseInt(resourceWithMeta.getMeta().getVersionId().getValue());
//            final Instant lastUpdated = resourceWithMeta.getMeta().getLastUpdated();
//            final String logicalId = resourceWithMeta.getId();
            
            // Add the mapping between localIdentifier and the new logicalId from the resource
            if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                addLocalRefMapping(localIdentifier, resourceWithMeta);
            }
            
            // Pass back the updated resource so it can be used in the next phase if required
            return metaResponse;
            // return new FHIRRestOperationResponse(resourceWithMeta, logicalId, newVersionNumber, lastUpdated, null);
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
     * Scan the resource and collect any conditional references
     * @param resource
     * @return
     */
    private Set<String> getConditionalReferences(Resource resource) {
        Set<String> conditionalReferences = new HashSet<>();
        CollectingVisitor<Reference> visitor = new CollectingVisitor<>(Reference.class);
        resource.accept(visitor);
        for (Reference reference : visitor.getResult()) {
            if (reference.getReference() != null && reference.getReference().getValue() != null) {
                String value = reference.getReference().getValue();
                if (!value.startsWith("#") &&
                        !value.startsWith("urn:") &&
                        !value.startsWith("http:") &&
                        !value.startsWith("https:") &&
                        value.contains("?")) {
                    conditionalReferences.add(value);
                }
            }
        }
        return conditionalReferences;
    }
    
    /**
     * Scan the resource for any conditional references. For each one we find, perform the search
     * and add the result to the localRefMap.
     * @param resource
     * @throws Exception
     */
    private void resolveConditionalReferences(Resource resource) throws Exception {
        for (String conditionalReference : getConditionalReferences(resource)) {
            if (localRefMap.containsKey(conditionalReference)) {
                continue;
            }
            
            FHIRUrlParser parser = new FHIRUrlParser(conditionalReference);
            String type = parser.getPathTokens()[0];

            MultivaluedMap<String, String> queryParameters = parser.getQueryParameters();
            if (queryParameters.isEmpty()) {
                throw buildRestException("Invalid conditional reference: no query parameters found", IssueType.INVALID);
            }

            if (queryParameters.keySet().stream().anyMatch(key -> SearchConstants.SEARCH_RESULT_PARAMETER_NAMES.contains(key))) {
                throw buildRestException("Invalid conditional reference: only filtering parameters are allowed", IssueType.INVALID);
            }

            // We need to be in a transaction before we can do a search
            beginTransactionIfRequired();
            
            queryParameters.add("_summary", "true");
            queryParameters.add("_count", "1");

            // Do a search, but no need to check if the interaction is allowed
            Bundle bundle = helpers.doSearch(type, null, null, queryParameters, null, resource, false);

            int total = bundle.getTotal().getValue();

            if (total == 0) {
                throw buildRestException("Error resolving conditional reference: search returned no results", IssueType.NOT_FOUND);
            }

            if (total > 1) {
                throw buildRestException("Error resolving conditional reference: search returned multiple results", IssueType.MULTIPLE_MATCHES);
            }

            localRefMap.put(conditionalReference, type + "/" + bundle.getEntry().get(0).getResource().getId());
        }
    }
    
    private FHIROperationException buildRestException(String msg, IssueType issueType) {
        return buildRestException(msg, issueType, IssueSeverity.FATAL);
    }

    private FHIROperationException buildRestException(String msg, IssueType issueType, IssueSeverity severity) {
        return new FHIROperationException(msg).withIssue(buildOperationOutcomeIssue(severity, issueType, msg));
    }

    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    private OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType type, String msg) {
        return OperationOutcome.Issue.builder()
                .severity(severity)
                .code(type)
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
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
     * If we are transactional, start the transaction if it hasn't been started already
     * @throws FHIRPersistenceException
     */
    private void beginTransactionIfRequired() throws FHIRPersistenceException {
        if (txn != null && !txn.hasBegun()) {
            txn.begin();
        }
    }

    /**
     * Unified exception handling for each of the interaction calls
     * @param entryIndex
     * @param v
     * @param failFast
     * @param requestDescription
     * @param initialTime
     * @throws Exception
     */
    private FHIRRestOperationResponse doInteraction(int entryIndex, boolean failFast, String requestDescription, long initialTime, Callable<FHIRRestOperationResponse> v) throws Exception {
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