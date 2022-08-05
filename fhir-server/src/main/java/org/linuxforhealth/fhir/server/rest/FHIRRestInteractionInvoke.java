/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST custom operation interaction
 */
public class FHIRRestInteractionInvoke extends FHIRRestInteractionResource {

    private final FHIROperationContext operationContext;
    private final String method;
    private final String resourceTypeName;
    private String logicalId;
    private String versionId;
    private MultivaluedMap<String, String> queryParameters;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param operationContext
     * @param method
     * @param resourceTypeName
     * @param logicalId
     * @param versionId
     * @param resource
     * @param queryParameters
     */
    public FHIRRestInteractionInvoke(int entryIndex, Entry validationResponseEntry, String requestDescription,
            FHIRUrlParser requestURL, FHIROperationContext operationContext, String method,
            String resourceTypeName, String logicalId, String versionId, Resource resource,
            MultivaluedMap<String, String> queryParameters) {
        super(entryIndex, null, resource, validationResponseEntry, requestDescription, requestURL);
        this.operationContext = operationContext;
        this.method = method;
        this.resourceTypeName = resourceTypeName;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.queryParameters = queryParameters;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {

        // Make sure the context is configured correctly before we call invoke
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
        operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
        operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
        operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, method);

        visitor.doInvoke(this.method, getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getAccumulatedTime(), operationContext, resourceTypeName, logicalId, versionId, getNewResource(), queryParameters);
    }
}