/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

 package com.ibm.fhir.server.resources.filters;

import static com.ibm.fhir.core.FHIRVersionParam.VERSION_40;
import static com.ibm.fhir.core.FHIRVersionParam.VERSION_43;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.FHIRVersionParam;

/**
 * A request filter that sets the {@value #FHIR_VERSION_PROP} request context property with the
 * preferred FHIRVersionParam for the current interaction according to the following algorithm:
 *
 * <p>For PUT and POST requests:
 * <ol>
 * <li>if the Content-Type header has a proper fhirVersion value (e.g. "4.0" or "4.3") use that
 * <li>otherwise fall back to the "other request" algorithm
 * </ol>
 * <p>For all other requests:
 * <ol>
 * <li>"4.3" from the acceptableMediaTypes
 * <li>"4.0" from the acceptableMediaTypes
 * <li>whatever is configured in the fhirServer/core/defaultFhirVersion config property
 * <li>"4.0"
 * </ol>
 */
public class FHIRVersionRequestFilter implements ContainerRequestFilter {
    public static final String FHIR_VERSION_PROP = "com.ibm.fhir.server.fhirVersion";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        FHIRVersionParam fhirVersion;

        switch (requestContext.getMethod()) {
        case HttpMethod.PUT:
            fhirVersion = getFhirVersionFromContentTypeHeader(requestContext);
            break;
        case HttpMethod.POST:
            if (requestContext.getUriInfo().getPath().endsWith("_search")) {
                fhirVersion = getFhirVersionFromAcceptHeader(requestContext);
            } else {
                fhirVersion = getFhirVersionFromContentTypeHeader(requestContext);
            }
            break;
        case HttpMethod.GET:
        default:
            fhirVersion = getFhirVersionFromAcceptHeader(requestContext);
            break;
        }

        requestContext.setProperty(FHIR_VERSION_PROP, fhirVersion);
    }

    /**
     * The FHIRVersionParam to use for the current interaction as determined by the Content-Type header
     *
     * @param requestContext
     * @return the FHIRVersionParam value from the Content-Type header if it is specified,
     *          otherwise falls back to {@link #getFhirVersionFromAcceptHeader(ContainerRequestContext)}
     */
    private FHIRVersionParam getFhirVersionFromContentTypeHeader(ContainerRequestContext requestContext) {
        MediaType mediaType = requestContext.getMediaType();
        if (mediaType != null) {
            String submittedVersion = mediaType.getParameters().get(FHIRMediaType.FHIR_VERSION_PARAMETER);
            if (submittedVersion != null) {
                // "startsWith" to cover the x.y.z cases which are technically invalid, but close enough
                if (submittedVersion.startsWith(VERSION_43.value())) {
                    return VERSION_43;
                } else if (submittedVersion.startsWith(VERSION_40.value())) {
                    return VERSION_40;
                }
            }
        }
        // fall back to getFhirVersionFromAcceptHeader logic
        return getFhirVersionFromAcceptHeader(requestContext);
    }

    /**
     * The FHIRVersionParam to use for the current interaction as determined by the Accept header
     *
     * @param requestContext
     * @return the FHIRVersionParam value from the Accept header if it is specified,
     *          otherwise from the defaultFhirVersion property of the fhir-server-config
     */
    private FHIRVersionParam getFhirVersionFromAcceptHeader(ContainerRequestContext requestContext) {
        FHIRVersionParam fhirVersion = null;
        for (MediaType mediaType : requestContext.getAcceptableMediaTypes()) {
            if (mediaType.getParameters() != null) {
                String fhirVersionParam = mediaType.getParameters().get(FHIRMediaType.FHIR_VERSION_PARAMETER);
                if (fhirVersionParam != null) {
                    // "startsWith" to cover the x.y.z cases which are technically invalid, but close enough
                    if (fhirVersionParam.startsWith(VERSION_43.value())) {
                        // one of the acceptable media types was our "actual" fhir version, so use that and stop looking
                        fhirVersion = VERSION_43;
                        break;
                    } else if (fhirVersionParam.startsWith(VERSION_40.value())) {
                        // set the fhirVersion parameter but keep looking in case our "actual" version is also acceptable
                        fhirVersion = VERSION_40;
                    }
                }
            }
        }
        if (fhirVersion == null) {
            String fhirVersionString = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_FHIR_VERSION, VERSION_40.value());
            fhirVersion = FHIRVersionParam.from(fhirVersionString);
        }
        return fhirVersion;
    }
}