/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.SecurityContext;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * Selects the single instance of Erase for a given request.
 *
 * @implNote Facilitates versioning of the Erase functionality, or should there be a need to switch between implementations
 * for Persistence Layers or specific behaviors, the EraseFactory enables the single location to make that decision.
 */
public class EraseFactory {
    private EraseFactory() {
        // No Operation
    }

    /**
     * Creates a single instance of the Erase factory.
     *
     * @param operationContext
     * @param resourceHelper
     * @param parameters
     * @param resourceType
     * @param logicalId
     * @param versionId
     * @return Single instance of $erase
     */
    public static Erase getInstance(FHIROperationContext operationContext, FHIRResourceHelpers resourceHelper, Parameters parameters, Class<? extends Resource> resourceType, String logicalId, String versionId) {
        // @implNote the following are guaranteed to be in the OperationContext as the JAXRS layer is injecting them, and we're
        // adding them to the Context as we go.

        // Pick off the HttpMethod
        HttpMethod method = (HttpMethod) operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE);

        // Pick off the security context from the FHIROperationContext.
        SecurityContext securityContext = (SecurityContext) operationContext.getProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT);

        return new EraseImpl(method, securityContext, resourceHelper, parameters, resourceType, logicalId, versionId);
    }
}