/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.client;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Create the resource by POSTing to the FHIR server
 */
public class PostResource implements FhirServerRequest<String> {
    // The resource we want to POST to the FHIR server
    private final Resource resource;
    
    public PostResource(Resource resource) {
        this.resource = resource;
    }
    
    @Override
    public String run(FhirClient client) {
        // Serialize the resource as a JSON string
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            FHIRGenerator.generator(Format.JSON, false).generate(this.resource, os);
        } catch (FHIRGeneratorException e) {
            throw new IllegalStateException(e);
        }

        FhirServerResponse response = client.post(getResourceType(), os.toString(StandardCharsets.UTF_8));
        return response.getLocationHeader();
    }

    /**
     * Parse the response into a Resource object
     * @param rdr
     * @return
     */
    private static Resource parseResponse(Reader rdr) {
        try {
            return FHIRParser.parser(Format.JSON).parse(rdr);
        } catch (FHIRParserException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private String getResourceType() {
        return resource.getClass().getSimpleName();
    }
}
