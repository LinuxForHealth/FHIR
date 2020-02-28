/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.fail;

import java.io.Reader;

import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.config.FHIRModelConfig;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.type.Reference;

public class CheckReferenceTypeTest {
    @Test
    public void testCheckReferenceType() throws Exception {
        FHIRModelConfig.setCheckReferenceTypes(true);
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/Observation-1.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(reader);
            
            // valid
            try {
                observation.toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient/1234"))
                        .build())
                    .build();
            } catch (IllegalStateException e) {
                fail();
            }
            
            // invalid
            try {
                observation.toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Condition/1234"))
                        .build())
                    .build();
                fail();
            } catch (IllegalStateException e) {
            }
            
            // turn off reference type checking
            FHIRModelConfig.setCheckReferenceTypes(false);
            
            // valid
            try {
                observation.toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient/1234"))
                        .build())
                    .build();
            } catch (IllegalStateException e) {
                fail();
            }
            
            // invalid
            try {
                observation.toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Condition/1234"))
                        .build())
                    .build();
            } catch (IllegalStateException e) {
                fail();
            }
        }
    }
}
