/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import static com.ibm.fhir.model.type.String.string;

import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource.Builder;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.CollectingVisitor;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

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
public class FHIRRestOperationVisitorPrepare implements FHIRRestOperationVisitor {
    private static final Logger log = Logger.getLogger(FHIRRestOperationVisitorImpl.class.getName());
    
    // the helper we use to do most of the heavy lifting
    private final FHIRResourceHelpers helpers;
        
    private final String bundleRequestCorrelationId;

    // Used to resolve local references
    final Map<String, String> localRefMap;
    
    // Is the bundle being processed as a single transaction?
    final boolean transaction;
    
    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestOperationVisitorPrepare(FHIRResourceHelpers helpers, String bundleRequestCorrelationId, Map<String, String> localRefMap, boolean transaction) {
        this.helpers = helpers;
        this.bundleRequestCorrelationId = bundleRequestCorrelationId;
        this.localRefMap = localRefMap;
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, long initialTime, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        // NOP. Nothing to do
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, long initialTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
        throws Exception {
        // NOP for now. TODO: when offloading payload, start an async read of the id/version payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
        MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // NOP for now. TODO: when offloading payload, try an optimistic async read of the latest payload
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
        throws Exception {
        // NOP for now. TODO: optimistic async reads, if we can scope them properly
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier, String logicalId) throws Exception {
        if (transaction) {
            resolveConditionalReferences(resource, localRefMap);
        }
        
        // Determine if we have a pre-generated resource ID
        String resourceId = retrieveGeneratedIdentifier(localRefMap, localIdentifier);
        if (resourceId == null) {
            // A resource id hasn't yet been assigned, so create one now
            resourceId = helpers.generateResourceId();
            
            if (localIdentifier != null) {
                addLocalRefMapping(localRefMap, localIdentifier, resourceId, null);
            }
        }
        
        // Convert any local references found within the resource to their corresponding external reference.
        ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
        resource.accept(visitor);
        resource = visitor.getResult();

        // TODO: perhaps make this part of the above visitor so that we only rebuild the resource once.
        final Instant lastUpdated = getCurrentInstant();
        final int newVersionNumber = 1;
        resource = copyAndSetResourceMetaFields(resource, logicalId, newVersionNumber, lastUpdated);
        
        // TODO: If the persistence layer supports offloading, we can store now. The offloadResponse
        // will be null if offloading is not supported
        Future<FHIRRestOperationResponse> offloadResponse = storePayload(resource, logicalId, newVersionNumber, lastUpdated);
        
        // Pass back the updated resource so it can be used in the next phase
        return new FHIRRestOperationResponse(resource, resourceId, newVersionNumber, lastUpdated, offloadResponse);
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, boolean doValidation, String localIdentifier) throws Exception {
        
        if (transaction) {
            resolveConditionalReferences(newResource, localRefMap);
        }
        
        // Convert any local references found within the resource to their corresponding external reference.
        ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
        newResource.accept(visitor);
        newResource = visitor.getResult();
        
        if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
            // TODO. The original version used the resource returned by the persistence update operation. But what's the difference?
            addLocalRefMapping(localRefMap, localIdentifier, null, newResource);
        }
        
        // Pass back the updated resource so it can be used in the next phase
        return new FHIRRestOperationResponse(null, null, newResource);
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, String requestDescription, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
        String versionId, String operationName, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, long initialTime, String type, String id, String searchQueryString) throws Exception {
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry) throws Exception {
        // NOP
        return null;
    }
    
    private void logStart(int entryIndex, String requestMethod, String url, FHIRUrlParser requestURL) {
        // Log our initial info message for this request.
        final StringBuilder requestDescription = new StringBuilder();
        requestDescription.append("entryIndex:[");
        requestDescription.append(entryIndex);
        requestDescription.append("] correlationId:[");
        requestDescription.append(bundleRequestCorrelationId);
        requestDescription.append("] method:[");
        requestDescription.append(requestMethod);
        requestDescription.append("] uri:[");
        requestDescription.append(url);
        requestDescription.append("]");
        if (log.isLoggable(Level.FINE)) {
            log.fine("Processing bundled request: " + requestDescription.toString());
            if (log.isLoggable(Level.FINER)) {
                log.finer("--> path: '" + requestURL.getPath() + "'");
                log.finer("--> query: '" + requestURL.getQuery() + "'");
            }
        }

    }

    @Override
    public FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long initialTime, Status status, Entry responseEntry) throws Exception {
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
     * @param localRefMap
     * @throws Exception
     */
    private void resolveConditionalReferences(Resource resource, Map<String, String> localRefMap) throws Exception {
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
     * This method will add a mapping to the local-to-external identifier map if the specified localIdentifier is
     * non-null.
     *
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @param externalIdentifier
     *            the externalIdentifier previously obtained for the resource (may be null
     *            if resource is not null)
     * @param resource
     *            the resource for which an external identifier will be built (may be null
     *            if externalIdentifier is not null)
     */
    private void addLocalRefMapping(Map<String, String> localRefMap, String localIdentifier, String externalIdentifier, Resource resource) {
        if (localIdentifier != null) {
            if (externalIdentifier == null) {
                externalIdentifier = ModelSupport.getTypeName(resource.getClass()) + "/" + resource.getId();
            }
            localRefMap.put(localIdentifier, externalIdentifier);
            if (log.isLoggable(Level.FINER)) {
                log.finer("Added local/ext identifier mapping: " + localIdentifier + " --> " + externalIdentifier);
            }
        }
    }
    
    /**
     * This method will retrieve the generated identifier associated with the specified local identifier from the
     * local ref map, or return null if there is no mapping for the local identifier.
     *
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @return the generated identifier
     */
    private String retrieveGeneratedIdentifier(Map<String, String> localRefMap, String localIdentifier) {
        String generatedIdentifier = null;
        String externalIdentifier = localRefMap.get(localIdentifier);
        if (externalIdentifier != null) {
            int index = externalIdentifier.indexOf("/");
            if (index > -1) {
                generatedIdentifier = externalIdentifier.substring(index+1);
            }
        }
        return generatedIdentifier;
    }
    
    /**
     * Creates and returns a copy of the passed resource with the {@code Resource.id}
     * {@code Resource.meta.versionId}, and {@code Resource.meta.lastUpdated} elements replaced.
     *
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param lastUpdated
     * @return the updated resource
     */
    @SuppressWarnings("unchecked")
    private <T extends Resource> T copyAndSetResourceMetaFields(T resource, String logicalId, int newVersionNumber, Instant lastUpdated) {
        Meta meta = resource.getMeta();
        Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
        metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
        metaBuilder.lastUpdated(lastUpdated);

        Builder resourceBuilder = resource.toBuilder();
        resourceBuilder.setValidating(false);
        return (T) resourceBuilder
                .id(logicalId)
                .meta(metaBuilder.build())
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
}