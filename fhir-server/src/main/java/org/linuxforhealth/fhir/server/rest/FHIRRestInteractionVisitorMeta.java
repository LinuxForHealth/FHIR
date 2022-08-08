/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.owasp.encoder.Encode;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.patch.FHIRPatch;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.CollectingVisitor;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.server.exception.FHIRResourceDeletedException;
import org.linuxforhealth.fhir.server.exception.FHIRResourceNotFoundException;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers.Interaction;
import org.linuxforhealth.fhir.server.spi.operation.FHIRRestOperationResponse;
import org.linuxforhealth.fhir.server.util.FHIRUrlParser;
import org.linuxforhealth.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Used to prepare bundle entries before they hit the persistence layer. For write operations
 * (CREATE, UPDATE, PATCH), the meta phase is responsible for establishing the identity of any
 * incoming resource, and updating the meta element accordingly. For other operations it is
 * currently a NOP, but in the future may be used for optimistic async reads when the payload
 * has been offloaded to another system.
 */
public class FHIRRestInteractionVisitorMeta extends FHIRRestInteractionVisitorBase {
    private static final Logger log = Logger.getLogger(FHIRRestInteractionVisitorPersist.class.getName());

    private static final boolean DO_VALIDATION = true;

    // True if the bundle is a transaction bundle, false if it's a batch bundle
    final boolean transaction;

    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestInteractionVisitorMeta(boolean transaction, FHIRResourceHelpers helpers, Map<String, String> localRefMap,
            Entry[] responseBundleEntries) {
        super(helpers, localRefMap, responseBundleEntries);
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters,
            String requestUri, boolean checkInteractionAllowed) throws Exception {
        // NOP. Nothing to do
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String versionId,
            MultivaluedMap<String, String> queryParameters) throws Exception {
        // NOP for now. TODO: when offloading payload, start an async read of the id/version payload
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, boolean throwExcOnNull,
            MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // NOP for now. TODO: when offloading payload, try an optimistic async read of the latest payload
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
            throws Exception {
        // NOP for now. TODO: optimistic async reads, if we can scope them properly
        logStart(entryIndex, requestDescription, requestURL);
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, FHIRPersistenceEvent event, List<Issue> warnings,
            Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
            String type, Resource resource, String ifNoneExist, String localIdentifier, PayloadPersistenceResponse offloadResponse)
            throws Exception {
        logStart(entryIndex, requestDescription, requestURL);

        // Skip CREATE if validation failed
        // TODO the logic in the old buildLocalRefMap uses SC_OK_STRING
        if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
            return null;
        }

        // Use doInteraction so we can implement common exception handling in one place
        return doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            // Validate that interaction is allowed for given resource type
            helpers.validateInteraction(Interaction.CREATE, type);

            // Inject the meta to the resource after optionally checking for ifNoneExist
            FHIRRestOperationResponse prepResponse = helpers.doCreateMeta(event, warnings, type, resource, ifNoneExist);
            if (prepResponse != null) {
                // ifNoneExist returned a result then add it to the result bundle which also
                // means this entry is complete.
                Entry entry = buildResponseBundleEntry(prepResponse, null, requestDescription, accumulatedTime);
                setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime);

                if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                    addLocalRefMapping(localIdentifier, prepResponse.getResource());
                }

                return null;
            }

            // Get the updated resource with the meta info
            Resource resourceWithMeta = event.getFhirResource();

            if (this.transaction) {
                resolveConditionalReferences(resourceWithMeta);
            }

            // Add the mapping between localIdentifier and the new logicalId from the resource
            if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                addLocalRefMapping(localIdentifier, resourceWithMeta);
            }

            // Pass back the updated resource so it can be used in the next phase if required
            final String logicalId = resourceWithMeta.getId();
            return new FHIRRestOperationResponse(resourceWithMeta, logicalId, null);
        });
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id,
            Resource resource, Resource prevResource, String ifMatchValue, String searchQueryString, boolean skippableUpdate,
            String localIdentifier, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch, PayloadPersistenceResponse offloadResponse)
            throws Exception {
        logStart(entryIndex, requestDescription, requestURL);

        // Skip UPDATE if validation failed
        if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
            return null;
        }

        // Process the first (meta) phase of the update interaction
        return doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            helpers.validateInteraction(Interaction.UPDATE, type);

            FHIRRestOperationResponse metaResponse = helpers.doUpdateMeta(event, type, id, null, resource, ifMatchValue, searchQueryString, skippableUpdate, ifNoneMatch, !DO_VALIDATION, warnings);

            // If the update was skippable we might be able to skip the future persistence step
            if (metaResponse.isCompleted()) {
                Entry entry = buildResponseBundleEntry(metaResponse, null, requestDescription, accumulatedTime);
                setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime);
            }

            // Get the updated resource with the meta info
            Resource resourceWithMeta = metaResponse.getResource();

            if (this.transaction) {
                resolveConditionalReferences(resourceWithMeta);
            }

            // Add the mapping between localIdentifier and the new logicalId from the resource
            if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                addLocalRefMapping(localIdentifier, resourceWithMeta);
            }

            // Pass back the updated resource so it can be used in the next phase if required
            return metaResponse;
        });
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id, Resource newResource,
            Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, List<Issue> warnings, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // Skip PATCH if validation failed
        // TODO the logic in the old buildLocalRefMap uses SC_OK_STRING
        if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
            return null;
        }

        // Process the first (meta) phase of the update interaction
        return doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            // Validate that interaction is allowed for given resource type
            helpers.validateInteraction(Interaction.PATCH, type);
            FHIRRestOperationResponse metaResponse = helpers.doUpdateMeta(event, type, id, patch, null, ifMatchValue, searchQueryString, skippableUpdate, null, !DO_VALIDATION, warnings);

            // If the update was skippable we might be able to skip the future persistence step
            if (metaResponse.isCompleted()) {
                Entry entry = buildResponseBundleEntry(metaResponse, null, requestDescription, accumulatedTime);
                setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime);
            }

            // Get the updated resource with the meta info
            Resource resourceWithMeta = metaResponse.getResource();

            if (this.transaction) {
                resolveConditionalReferences(resourceWithMeta);
            }

            // Add the mapping between localIdentifier and the new logicalId from the resource
            if (localIdentifier != null && !localRefMap.containsKey(localIdentifier)) {
                addLocalRefMapping(localIdentifier, resourceWithMeta);
            }

            // Pass back the updated resource so it can be used in the next phase if required
            return metaResponse;
        });
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription,
            FHIRUrlParser requestURL, long accumulatedTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
            String versionId, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String searchQueryString) throws Exception {
        logStart(entryIndex, requestDescription, requestURL);
        // NOP
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry, String requestDescription,
            long accumulatedTime) throws Exception {
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
    public FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long accumulatedTime, Status status, Entry responseEntry)
            throws Exception {
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

            queryParameters.add("_summary", "true");
            queryParameters.add("_count", "1");

            // Do a search, but no need to check if the interaction is allowed
            Bundle bundle = helpers.doSearch(type, null, null, queryParameters, null, false, true);

            int total = bundle.getTotal().getValue();

            if (total == 0) {
                throw buildRestException("Error resolving conditional reference: search '"
                        + Encode.forHtml(conditionalReference) + "' returned no results", IssueType.NOT_FOUND);
            }

            if (total > 1) {
                throw buildRestException("Error resolving conditional reference: search '"
                        + Encode.forHtml(conditionalReference) + "' returned multiple results", IssueType.MULTIPLE_MATCHES);
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
     * Unified exception handling for each of the interaction calls
     * @param entryIndex
     * @param v
     * @param failFast
     * @param requestDescription
     * @param accumulatedTime
     * @throws Exception
     */
    private FHIRRestOperationResponse doInteraction(int entryIndex, String requestDescription, long accumulatedTime,
            Callable<FHIRRestOperationResponse> v) throws Exception {
        // If we're a transaction bundle, we want to fail as soon as we hit a problem
        final boolean failFast = transaction;
        final long start = System.nanoTime();
        try {
            return v.call();
        } catch (FHIRResourceNotFoundException e) {
            if (failFast) {
                updateIssuesWithEntryIndexAndThrow(entryIndex, e);
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
                updateIssuesWithEntryIndexAndThrow(entryIndex, e);
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
                updateIssuesWithEntryIndexAndThrow(entryIndex, e);
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