/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import static com.ibm.fhir.model.type.String.string;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;
import com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper;

/**
 * Visitor implementation to access the persistence operations. Each operation
 * result is wrapped in a {@link FHIRRestOperationResponse} object, which is
 * what allows us to use the visitor pattern here. Note that this differs
 * slightly from the original FHIRRestHelper implementation in which operations
 * returned different types. That said, all the heavy lifting is the same,
 * performed by the implementation behind {@link FHIRResourceHelpers}.
 */
public class FHIRRestOperationVisitorImpl implements FHIRRestOperationVisitor {
    private static final Logger log = Logger.getLogger(FHIRRestOperationVisitorImpl.class.getName());
    
    // the helper we use to do most of the heavy lifting
    private final FHIRResourceHelpers helpers;
    
    private final String bundleRequestCorrelationId;
    private static final com.ibm.fhir.model.type.String SC_BAD_REQUEST_STRING = string(Integer.toString(SC_BAD_REQUEST));
    private static final com.ibm.fhir.model.type.String SC_GONE_STRING = string(Integer.toString(SC_GONE));
    private static final com.ibm.fhir.model.type.String SC_NOT_FOUND_STRING = string(Integer.toString(SC_NOT_FOUND));
    private static final com.ibm.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));
    private static final com.ibm.fhir.model.type.String SC_OK_STRING = string(Integer.toString(SC_OK));

    // Used to resolve local references
    final Map<String, String> localRefMap;

    // Held as an ArrayList, so we can be sure of O(1) operations for random puts
    final ArrayList<Entry> responseBundleEntries;

    // True if the bundle type is TRANSACTION
    final boolean transaction;
    
    /**
     * Public constructor
     * @param helpers
     * @param bundleRequestCorrelationId
     * @param localRefMap
     * @param responseBundleEntries
     */
    public FHIRRestOperationVisitorImpl(FHIRResourceHelpers helpers, String bundleRequestCorrelationId, Map<String, String> localRefMap, ArrayList<Entry> responseBundleEntries, boolean transaction) {
        this.helpers = helpers;
        this.bundleRequestCorrelationId = bundleRequestCorrelationId;
        this.localRefMap = localRefMap;
        this.responseBundleEntries = responseBundleEntries;
        this.transaction = transaction;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, long initialTime, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        
        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            Bundle searchResults = helpers.doSearch(type, compartment, compartmentId, queryParameters, requestUri, contextResource, checkInteractionAllowed);
            
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
    public FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, long initialTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
        throws Exception {
        
        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            // Perform the VREAD and return the result entry we want in the response bundle
            Resource resource = helpers.doVRead(type, id, versionId, queryParameters);
            return Entry.builder()
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .resource(resource)
                    .build();
        });
        
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
        MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {

        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            // Perform the VREAD and return the result entry we want in the response bundle
            SingleResourceResult<? extends Resource> readResult = helpers.doRead(type, id, throwExcOnNull, includeDeleted, contextResource, queryParameters);
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
    public FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
        throws Exception {
        
        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
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
    public FHIRRestOperationResponse doCreate(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier, String logicalId) throws Exception {

        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            FHIRRestOperationResponse ior = helpers.doCreate(type, resource, ifNoneExist, doValidation);
        
            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }
    
            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, initialTime);
        });
        return null;
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, boolean doValidation, String localIdentifier) throws Exception {
        
        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            FHIRRestOperationResponse ior = helpers.doUpdate(type, localIdentifier, newResource, ifMatchValue, searchQueryString, skippableUpdate, doValidation);
            
            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }
            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, initialTime);        
        });
        
        return null;
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, String requestDescription, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate) throws Exception {

        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            FHIRRestOperationResponse response = helpers.doPatch(type, id, patch, ifMatchValue, searchQueryString, skippableUpdate);
            return buildResponseBundleEntry(response, null, requestDescription, initialTime);
        });
        
        return null;
    }

    @Override
    public FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
        String versionId, String operationName, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        
        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
            Resource response = helpers.doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, resource, queryParameters);
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
    public FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, long initialTime, String type, String id, String searchQueryString) throws Exception {

        doOperation(entryIndex, transaction, requestDescription, initialTime, () -> {
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
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry) throws Exception {
        responseBundleEntries.add(entryIndex, validationResponseEntry);
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
        responseBundleEntries.add(entryIndex, responseEntry);
        return null;
    }
    
    private Entry buildResponseBundleEntry(FHIRRestOperationResponse operationResponse, OperationOutcome validationOutcome,
        String requestDescription, long initialTime) throws FHIROperationException {

        Resource resource = operationResponse.getResource();
        URI locationURI = operationResponse.getLocationURI();
        int httpStatus = operationResponse.getStatus().getStatusCode();
    
        Entry.Response.Builder entryResponseBuilder = Entry.Response.builder()
                .status(string(Integer.toString(httpStatus)))
                .outcome(validationOutcome);
        if (resource != null) {
            entryResponseBuilder = entryResponseBuilder
                    .id(resource.getId())
                    .lastModified(resource.getMeta().getLastUpdated())
                    .etag(string(getEtagValue(resource)));
        }
        if (locationURI != null) {
            entryResponseBuilder = entryResponseBuilder.location(Uri.of(locationURI.toString()));
        }
    
        Entry.Builder bundleEntryBuilder = Entry.builder();
        if (HTTPReturnPreference.REPRESENTATION.equals(FHIRRequestContext.get().getReturnPreference())) {
            bundleEntryBuilder.resource(resource);
        } else if (HTTPReturnPreference.OPERATION_OUTCOME.equals(FHIRRequestContext.get().getReturnPreference())) {
            // Given that we execute the operation with validation turned off, the operationResponse outcome is unlikely
            // to contain useful information, but the validationOutcome already exists under the Entry.response
            bundleEntryBuilder.resource(operationResponse.getOperationOutcome());
        }
    
        // TODO logBundledRequestCompletedMsg(requestDescription, initialTime, httpStatus);
        return bundleEntryBuilder.response(entryResponseBuilder.build()).build();
    }
    
    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }
    
    private void logBundledRequestCompletedMsg(String requestDescription, long initialTime, int httpStatus) {
        StringBuffer statusMsg = new StringBuffer();
        statusMsg.append(" status:[" + httpStatus + "]");
        double elapsedSecs = (System.currentTimeMillis() - initialTime) / 1000.0;
        log.info("Completed bundled request[" + elapsedSecs + " secs]: "
                + requestDescription.toString() + statusMsg.toString());
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
    private void doOperation(int entryIndex, boolean failFast, String requestDescription, long initialTime, Callable<Entry> v) throws Exception {
        try {
            Entry entry = v.call();
            responseBundleEntries.add(entryIndex, entry);
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
            responseBundleEntries.add(entryIndex, entry);
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
            responseBundleEntries.add(entryIndex, entry);
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
            responseBundleEntries.add(entryIndex, entry);
            logBundledRequestCompletedMsg(requestDescription.toString(), initialTime, status.getStatusCode());
        }

    }
}