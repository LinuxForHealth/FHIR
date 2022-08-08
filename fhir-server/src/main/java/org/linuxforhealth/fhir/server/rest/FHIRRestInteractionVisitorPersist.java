/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.patch.FHIRPatch;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceIfNoneMatchException;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.server.exception.FHIRResourceDeletedException;
import org.linuxforhealth.fhir.server.exception.FHIRResourceNotFoundException;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.server.spi.operation.FHIRRestOperationResponse;
import org.linuxforhealth.fhir.server.util.FHIRUrlParser;
import org.linuxforhealth.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Visitor implementation to access the persistence operations. Each operation
 * result is wrapped in a {@link FHIRRestOperationResponse} object, which is
 * what allows us to use the visitor pattern here. Note that this differs
 * slightly from the original FHIRRestHelper implementation in which operations
 * returned different types. That said, all the heavy lifting is the same,
 * performed by the implementation behind {@link FHIRResourceHelpers}.
 */
public class FHIRRestInteractionVisitorPersist extends FHIRRestInteractionVisitorBase {

    // True if the bundle type is TRANSACTION
    final boolean transaction;

    /**
     * Public constructor
     *
     * @param helpers
     * @param localRefMap
     * @param responseBundleEntries
     * @param transaction
     */
    public FHIRRestInteractionVisitorPersist(FHIRResourceHelpers helpers, Map<String, String> localRefMap,
            Entry[] responseBundleEntries, boolean transaction) {
        super(helpers, localRefMap, responseBundleEntries);
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri,
            boolean checkInteractionAllowed) throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            Bundle searchResults = helpers.doSearch(type, compartment, compartmentId, queryParameters, requestUri,
                    checkInteractionAllowed, true);

            return Bundle.Entry.builder()
                    .resource(searchResults)
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .build();
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
            throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            // Perform the VREAD and return the result entry we want in the response bundle
            SingleResourceResult<? extends Resource> srr = helpers.doVRead(type, id, versionId, queryParameters);
            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .resource(srr.getResource())
                    .build();
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, boolean throwExcOnNull,
            MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed)
            throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            // Perform the VREAD and return the result entry we want in the response bundle
            SingleResourceResult<? extends Resource> readResult = helpers.doRead(type, id, throwExcOnNull, queryParameters);
            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .resource(readResult.getResource())
                    .build();
        });
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
            throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            Bundle bundle = helpers.doHistory(type, id, queryParameters, requestUri);
            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .resource(bundle)
                    .build();
        });
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, FHIRPersistenceEvent event, List<Issue> warnings,
            Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
            String type, Resource resource, String ifNoneExist, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            FHIRRestOperationResponse ior = helpers.doCreatePersist(event, warnings, resource, offloadResponse);

            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }

            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, accumulatedTime);
        });

        // No need to return anything. doOperation injects the entry to the responseBundle
        return null;
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id,
            Resource newResource, Resource prevResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch, PayloadPersistenceResponse offloadResponse) throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {

            FHIRRestOperationResponse ior = helpers.doPatchOrUpdatePersist(event, type, id, false, newResource, prevResource, warnings, isDeleted, ifNoneMatch, offloadResponse);
            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }
            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, accumulatedTime);
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id,
            Resource newResource, Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, List<Issue> warnings, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception {

        // For patch, if the original resource was deleted, we'd have already thrown an error.
        // Note that the patch will have already been applied to the resource...so this is
        // really just an update as far as the persistence layer is concerned
        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            FHIRRestOperationResponse ior = helpers.doPatchOrUpdatePersist(event, type, id, true, newResource, prevResource,
                warnings, false, null, offloadResponse);
            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }
            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, accumulatedTime);
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
            FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            Resource response = helpers.doInvoke(operationContext, resourceTypeName, logicalId, versionId, resource, queryParameters);
            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .resource(response)
                    .build();
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String searchQueryString) throws Exception {

        doInteraction(entryIndex, requestDescription, accumulatedTime, () -> {
            FHIRRestOperationResponse ior = helpers.doDelete(type, id, searchQueryString);

            int httpStatus = ior.getStatus().getStatusCode();
            OperationOutcome oo = ior.getOperationOutcome();

            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(string(Integer.toString(httpStatus)))
                        .outcome(oo)
                        .build())
                    .build();
        });

        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry,
            String requestDescription, long accumulatedTime) throws Exception {
        setEntryComplete(entryIndex, validationResponseEntry, requestDescription, accumulatedTime);
        return null;
    }

    @Override
    public FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long accumulatedTime, Status status,
            Entry responseEntry) throws Exception {
        setEntryComplete(entryIndex, responseEntry, requestDescription, accumulatedTime);
        return null;
    }

    /**
     * Unified exception handling for each of the operation calls
     * @param entryIndex
     * @param v
     * @param failFast
     * @param requestDescription
     * @param accumulatedTime
     * @throws Exception
     */
    private void doInteraction(int entryIndex, String requestDescription, long accumulatedTime, Callable<Entry> v)
            throws Exception {
        // To make it clear, we fail immediately on exception if we're processing bundleType == TRANSACTION
        final boolean failFast = transaction;
        final long start = System.nanoTime();
        try {
            // If we already have a response entry for the given index then the entry has already
            // completed or failed in a previous phase so we skip it here
            if (getResponseEntry(entryIndex) == null) {
                Entry entry = v.call();
                final long elapsed = System.nanoTime() - start;
                setEntryComplete(entryIndex, entry, requestDescription, accumulatedTime + elapsed);
            }
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
        } catch (FHIRPersistenceIfNoneMatchException e) {
            if (failFast) {
                updateIssuesWithEntryIndexAndThrow(entryIndex, e);
            }

            // Because this exception was thrown, we know that this is to be treated as an error
            // for this particular entry
            Entry entry = Entry.builder()
                    .resource(FHIRUtil.buildOperationOutcome(e, false))
                    .response(Entry.Response.builder()
                        .status(SC_PRECONDITION_FAILED_STRING)
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
    }
}