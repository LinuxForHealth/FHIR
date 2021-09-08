/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Visitor implementation to access the persistence operations. Each operation
 * result is wrapped in a {@link FHIRRestOperationResponse} object, which is
 * what allows us to use the visitor pattern here. Note that this differs
 * slightly from the original FHIRRestHelper implementation in which operations
 * returned different types. That said, all the heavy lifting is the same,
 * performed by the implementation behind {@link FHIRResourceHelpers}.
 */
public class FHIRRestOperationVisitorImpl implements FHIRRestOperationVisitor<FHIRRestOperationResponse> {
    private static final Logger log = Logger.getLogger(FHIRRestOperationVisitorImpl.class.getName());
    
    // the helper we use to do most of the heavy lifting
    private final FHIRResourceHelpers helpers;
    
    private final String bundleRequestCorrelationId;

    // Used to resolve local references
    final Map<String, String> localRefMap;
    
    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestOperationVisitorImpl(FHIRResourceHelpers helpers, String bundleRequestCorrelationId, Map<String, String> localRefMap) {
        this.helpers = helpers;
        this.bundleRequestCorrelationId = bundleRequestCorrelationId;
        this.localRefMap = localRefMap;
    }

    @Override
    public FHIRRestOperationResponse doSearch(int entryIndex, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doVRead(int entryIndex, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
        throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doRead(int entryIndex, String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
        MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doHistory(int entryIndex, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
        throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doCreate(int entryIndex, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doUpdate(int entryIndex, String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate, boolean doValidation, String localIdentifier) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doPatch(int entryIndex, String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        boolean skippableUpdate) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doInvoke(int entryIndex, FHIROperationContext operationContext, String resourceTypeName, String logicalId,
        String versionId, String operationName, Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse doDelete(int entryIndex, String type, String id, String searchQueryString) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry) throws Exception {
        // TODO Auto-generated method stub
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
    public FHIRRestOperationResponse issue(int entryIndex, Status status, Entry responseEntry) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}