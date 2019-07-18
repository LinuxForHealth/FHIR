/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples.test;

import com.ibm.watsonhealth.fhir.examples.CompleteMockDataCreator;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public class GenerateSingleResource {
    public static void main(String[] args) throws Exception {
        CompleteMockDataCreator creator = new CompleteMockDataCreator();
        
        Resource resource = creator.createResource("Goal", 1);
        FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, true);
        generator.generate(resource, System.out);
    }
}
