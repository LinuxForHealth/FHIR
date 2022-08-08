/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.examples.test;

import org.linuxforhealth.fhir.examples.CompleteMockDataCreator;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Resource;

public class GenerateSingleResource {
    public static void main(String[] args) throws Exception {
        CompleteMockDataCreator creator = new CompleteMockDataCreator();
        
        Resource resource = creator.createResource("Goal", 1);
        FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, true);
        generator.generate(resource, System.out);
    }
}
