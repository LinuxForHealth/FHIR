/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.client;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Create the resource by POSTing to the FHIR server
 */
public class PostResource implements FhirServerRequest<FhirServerResponse> {
    // The resource we want to POST to the FHIR server
    private final Resource resource;

    public PostResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public FhirServerResponse run(FHIRBucketClient client) {
        // Serialize the resource as a JSON string
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            FHIRGenerator.generator(Format.JSON, false).generate(this.resource, os);
        } catch (FHIRGeneratorException e) {
            throw new IllegalStateException(e);
        }

        final String resourceType = getResourceType();
        final String payload = new String(os.toByteArray(), StandardCharsets.UTF_8);
        if ("Bundle".equals(resourceType)) {
            return client.post("", payload);

        } else {
            return client.post(resourceType, payload);
        }
    }

    private String getResourceType() {
        return resource.getClass().getSimpleName();
    }
}
