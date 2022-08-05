/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.fail;

import java.io.Reader;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.AllergyIntolerance;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Uri;

public class CodeableConceptRequiredBindingTest {
    @Test
    public void testCodeableConceptRequiredBinding() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/AllergyIntolerance-1.json")) {
            AllergyIntolerance allergyIntolerance = FHIRParser.parser(Format.JSON).parse(reader);
            
            // valid
            try {
                allergyIntolerance.toBuilder().clinicalStatus(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"))
                        .code(Code.of("active"))
                            .build())
                        .build())
                    .build();
            } catch (IllegalStateException e) {
                fail();
            }
            
            // invalid
            try {
                allergyIntolerance.toBuilder().clinicalStatus(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"))
                        .code(Code.of("xxx"))
                            .build())
                        .build())
                    .build();
                fail();
            } catch (IllegalStateException e) {
                
            }
            
            // valid
            try {
                allergyIntolerance.toBuilder().verificationStatus(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification"))
                        .code(Code.of("confirmed"))
                            .build())
                        .build())
                    .build();
            } catch (IllegalStateException e) {
                fail();
            }
            
            // invalid
            try {
                allergyIntolerance.toBuilder().verificationStatus(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification"))
                        .code(Code.of("xxx"))
                            .build())
                        .build())
                    .build();
                fail();
            } catch (IllegalStateException e) {
                
            }
        }
    }
}
