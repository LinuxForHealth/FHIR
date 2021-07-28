/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 * Replaces the Base URL in the OriginalRequestURI
 */
public class OriginalRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(OriginalRequestFilter.class.getName());

    public static final String PROPERTY_EXTERNAL_BASE_URL = "fhirServer/core/externalBaseUrl";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UriInfo info = requestContext.getUriInfo();

        FHIRRequestContext ctx = FHIRRequestContext.get();
        if (ctx == null) {
            requestContext.abortWith(buildAbortWithResponseBadContext());
        } else {
            String baseUrl = FHIRConfigHelper.getStringProperty(PROPERTY_EXTERNAL_BASE_URL, null);
            if (baseUrl != null) {
                String originalRequestUri = baseUrl +
                        "/" + info.getPath() + "?" + info.getRequestUri().getRawQuery();
                ctx.setOriginalRequestUri(originalRequestUri);
                LOG.fine(() ->  "originalRequestUri override [" + originalRequestUri + "]");
            }  else {
                LOG.fine(() -> "No BaseURL specified, requestUri nont changed.s");
            }
        }
    }

    /**
     * builds the abort responsne context.
     * @return
     */
    public Response buildAbortWithResponseBadContext() {
        String diagnostics = "Context is not yet set.";
        OperationOutcome oo = FHIRUtil.buildOperationOutcome(new FHIROperationException(diagnostics), false);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(oo)
                .build();
    }
}