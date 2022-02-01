/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

 package com.ibm.fhir.server.resources.filters;

import static com.ibm.fhir.core.FHIRMediaType.VERSION_40;
import static com.ibm.fhir.core.FHIRMediaType.VERSION_43;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRMediaType;

public class FHIRVersionRequestFilter implements ContainerRequestFilter {
    public static final String FHIR_VERSION_PROP = "com.ibm.fhir.server.fhirVersion";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        /*
         * This method will look through the MediaTypes constructed by JAX-RS from the incoming "Accept" header
         * and add the most preferred value to the request context under the FHIR_VERSION_PROP name using the following
         * order of preference:
         *
         *   1. "4.3" from the acceptableMediaTypes
         *   2. "4.0" from the acceptableMediaTypes
         *   3. whatever is configured in the fhirServer/core/defaultFhirVersion config property
         *   4. "4.0"
         */
        String fhirVersion = null;
        for (MediaType mediaType : requestContext.getAcceptableMediaTypes()) {
            if (mediaType.getParameters() != null) {
                String fhirVersionParam = mediaType.getParameters().get(FHIRMediaType.FHIR_VERSION_PARAMETER);
                if (fhirVersionParam != null) {
                    // "startsWith" to cover the x.y.x cases which are technically invalid, but close enough
                    if (fhirVersionParam.startsWith(VERSION_43)) {
                        // one of the acceptable media types was our "actual" fhir version, so use that and stop looking
                        fhirVersion = VERSION_43;
                        break;
                    } else if (fhirVersionParam.startsWith(VERSION_40)) {
                        // set the fhirVersion parameter but keep looking in case our "actual" version is also acceptable
                        fhirVersion = fhirVersionParam;
                    }
                }
            }
        }
        if (fhirVersion == null) {
            fhirVersion = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_FHIR_VERSION, FHIRMediaType.VERSION_40);
        }
        requestContext.setProperty(FHIR_VERSION_PROP, fhirVersion);
    }
}