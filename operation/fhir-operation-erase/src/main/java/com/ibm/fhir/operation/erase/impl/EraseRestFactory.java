/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import javax.ws.rs.core.SecurityContext;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * Selects the single instance of Erase for a given request.
 *
 * @implNote Facilitates different versions of the Erase functionality, or should there be a need to switch between implementations
 * for Persistence Layers or specific behaviors, the EraseFactory enables the single location to make that decision.
 */
public class EraseRestFactory {
    private static final CompartmentUtil compartmentHelper = new CompartmentUtil();

    private EraseRestFactory() {
        // No Operation
    }

    /**
     * Creates a single instance of the Erase factory.
     *
     * @param operationContext
     * @param parameters
     * @param resourceType
     * @param logicalId
     * @return Single instance of $erase
     */
    public static EraseRest getInstance(FHIROperationContext operationContext, Parameters parameters, Class<? extends Resource> resourceType, String logicalId) {
        // @implNote the following are guaranteed to be in the OperationContext as the JAXRS layer is injecting them, and we're
        // adding them to the Context as we go.

        // Pick off the HttpMethod
        String method = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE);

        // Pick off the security context from the FHIROperationContext.
        SecurityContext securityContext = (SecurityContext) operationContext.getProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT);

        return new EraseRestImpl(method, securityContext, parameters, resourceType, logicalId, compartmentHelper);
    }
}