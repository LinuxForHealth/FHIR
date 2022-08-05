/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.client;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Utilities for working with the FHIR client
 */
public class FHIRBucketClientUtil {
    
    
    /**
     * Render the resource as a string
     * @param resource
     * @return
     */
    public static String resourceToString(Resource resource) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            FHIRGenerator.generator(Format.JSON, false).generate(resource, os);
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (FHIRGeneratorException e) {
            throw new IllegalStateException(e);
        }
    }

}
