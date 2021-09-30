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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Abstract base class of the {@link FHIRRestInteractionVisitor}. Manages access to the
 * map managing local references (localRefMap) and the array of response Entry objects 
 * (responseBundleEntries).
 */
public abstract class FHIRRestInteractionVisitorBase implements FHIRRestInteractionVisitor {
    private static final Logger log = Logger.getLogger(FHIRRestInteractionVisitorBase.class.getName());
    
    protected static final com.ibm.fhir.model.type.String SC_BAD_REQUEST_STRING = string(Integer.toString(SC_BAD_REQUEST));
    protected static final com.ibm.fhir.model.type.String SC_GONE_STRING = string(Integer.toString(SC_GONE));
    protected static final com.ibm.fhir.model.type.String SC_NOT_FOUND_STRING = string(Integer.toString(SC_NOT_FOUND));
    protected static final com.ibm.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));
    protected static final com.ibm.fhir.model.type.String SC_OK_STRING = string(Integer.toString(SC_OK));

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

    protected void setResponseEntry(int entryIndex, Entry e) {
        responseBundleEntries[entryIndex] = e;
    }
    
    protected Entry getResponseEntry(int entryIndex) {
        return responseBundleEntries[entryIndex];
    }
    
    protected void logBundledRequestCompletedMsg(String requestDescription, long initialTime, int httpStatus) {
        logBundledRequestCompletedMsg(requestDescription, initialTime, Integer.toString(httpStatus));
    }
    
    protected void logBundledRequestCompletedMsg(String requestDescription, long initialTime, String httpStatus) {
        StringBuffer statusMsg = new StringBuffer();
        statusMsg.append(" status:[" + httpStatus + "]");
        double elapsedSecs = (System.currentTimeMillis() - initialTime) / 1000.0;
        log.info("Completed bundled request[" + elapsedSecs + " secs]: "
                + requestDescription.toString() + statusMsg.toString());
    }
    
    /**
     * Build an entry for the bundle response
     * @param operationResponse
     * @param validationOutcome
     * @param requestDescription
     * @param initialTime
     * @return
     * @throws FHIROperationException
     */
    protected Entry buildResponseBundleEntry(FHIRRestOperationResponse operationResponse, OperationOutcome validationOutcome,
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
}