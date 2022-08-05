/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import static org.linuxforhealth.fhir.model.type.String.string;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.HTTPReturnPreference;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.server.exception.FHIRRestBundledRequestException;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.server.spi.operation.FHIRRestOperationResponse;

/**
 * Abstract base class of the {@link FHIRRestInteractionVisitor}. Manages access to the
 * map managing local references (localRefMap) and the array of response Entry objects
 * (responseBundleEntries).
 */
public abstract class FHIRRestInteractionVisitorBase implements FHIRRestInteractionVisitor {
    private static final Logger log = Logger.getLogger(FHIRRestInteractionVisitorBase.class.getName());

    protected static final org.linuxforhealth.fhir.model.type.String SC_BAD_REQUEST_STRING = string(Integer.toString(SC_BAD_REQUEST));
    protected static final org.linuxforhealth.fhir.model.type.String SC_GONE_STRING = string(Integer.toString(SC_GONE));
    protected static final org.linuxforhealth.fhir.model.type.String SC_NOT_FOUND_STRING = string(Integer.toString(SC_NOT_FOUND));
    protected static final org.linuxforhealth.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));
    protected static final org.linuxforhealth.fhir.model.type.String SC_OK_STRING = string(Integer.toString(SC_OK));
    protected static final org.linuxforhealth.fhir.model.type.String SC_PRECONDITION_FAILED_STRING = string(Integer.toString(SC_PRECONDITION_FAILED));

    // the helper we use to do most of the heavy lifting
    protected final FHIRResourceHelpers helpers;

    // Used to resolve local references
    protected final Map<String, String> localRefMap;

    // Held as an array, so we can be sure of O(1) operations because entries are not processed in order
    private final Entry[] responseBundleEntries;

    /**
     * Protected constructor
     * @param helpers
     * @param localRefMap
     * @param responseBundleEntries
     */
    protected FHIRRestInteractionVisitorBase(FHIRResourceHelpers helpers,
            Map<String, String> localRefMap, Entry[] responseBundleEntries) {
        this.helpers = helpers;
        this.localRefMap = localRefMap;
        this.responseBundleEntries = responseBundleEntries;
    }

    /**
     * Set the given entry e in the response bundle and log a bundle entry completion
     * message.
     * @param entryIndex
     * @param e
     * @param requestDescription
     * @param accumulatedTime
     */
    protected void setEntryComplete(int entryIndex, Entry e, String requestDescription, long accumulatedTime) {
        responseBundleEntries[entryIndex] = e;
        logBundledRequestCompletedMsg(requestDescription, accumulatedTime, e.getResponse().getStatus().getValue());
    }

    protected Entry getResponseEntry(int entryIndex) {
        return responseBundleEntries[entryIndex];
    }

    private void logBundledRequestCompletedMsg(String requestDescription, long accumulatedTime, String httpStatus) {
        double elapsedSecs = accumulatedTime / 1000000000.0;

        final String msg = String.format("Completed bundle request took:[%7.3f secs]: %s status:[%s]", 
            elapsedSecs, requestDescription, httpStatus);
        log.info(msg);
    }

    /**
     * Build an entry for the bundle response
     * @param operationResponse
     * @param validationOutcome
     * @param requestDescription
     * @param accumulatedTime
     * @return
     * @throws FHIROperationException
     */
    protected Entry buildResponseBundleEntry(FHIRRestOperationResponse operationResponse,
            OperationOutcome validationOutcome, String requestDescription, long accumulatedTime)
            throws FHIROperationException {

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

        return bundleEntryBuilder.response(entryResponseBuilder.build()).build();
    }

    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }

    /**
     * This method will add a mapping to the local-to-external identifier map if the specified localIdentifier is
     * non-null.
     *
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @param resource
     *            the resource for which an external identifier will be built
     */
    protected void addLocalRefMapping(String localIdentifier, Resource resource) {
        if (localIdentifier != null) {
            final String externalIdentifier = ModelSupport.getTypeName(resource.getClass()) + "/" + resource.getId();
            localRefMap.put(localIdentifier, externalIdentifier);
            if (log.isLoggable(Level.FINER)) {
                log.finer("Added local/ext identifier mapping: " + localIdentifier + " --> " + externalIdentifier);
            }
        }
    }
    
    /**
     * Wrap the cause with a FHIRRestbundledRequestException and update each issue with
     * the entryIndex before throwing.
     * 
     * @param entryIndex
     * @param cause
     * @throws FHIROperationException
     */
    protected void updateIssuesWithEntryIndexAndThrow(Integer entryIndex, FHIROperationException cause) throws FHIROperationException {
        String msg = "Error while processing request bundle on entry " + entryIndex;
        List<Issue> updatedIssues = cause.getIssues().stream()
                .map(i -> i.toBuilder().expression(string("Bundle.entry[" + entryIndex + "]")).build())
                .collect(Collectors.toList());
        // no need to keep the issues in the cause any more since we've "promoted" them to the wrapped exception
        cause.withIssue(Collections.emptyList());
        throw new FHIRRestBundledRequestException(msg, cause).withIssue(updatedIssues);
    }
}