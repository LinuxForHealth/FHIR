/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.fail;

import java.io.Reader;
import java.util.Collections;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Account;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * Tests for validation against Code/Coding/CodeableConcept required (or maxValueSet) binding to syntax-based value sets.
 */
public class SyntaxBasedValueSetBindingTest {

	// Currently no resource types have a Code with a required binding to a syntax-based value set. If that changes, add tests here.
	
    @Test
    public void testMaxValueSetCodeValid() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/Account-1.json")) {
            Account account = FHIRParser.parser(Format.JSON).parse(reader);
            
        	account.toBuilder().language(Code.of("en-AU")).build();
        }
    }
	
	@Test
    public void testMaxValueSetCodeNotValid() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/Account-1.json")) {
            Account account = FHIRParser.parser(Format.JSON).parse(reader);

            try {
            	account.toBuilder().language(Code.of("invalidLanguageCode")).build();
            	fail();
            } catch (IllegalStateException e) {
            }
        }
    }
	
	// Currently no resource types have a Coding with a required binding to a syntax-based value set. If that changes, add tests here.

	// Currently no resource types have a Coding with a maxValueSet binding to a syntax-based value set. If that changes, add tests here.

	@Test
    public void testMaxValueSetCodeableConceptValid() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/Practitioner-1.json")) {
        	Practitioner practitioner = FHIRParser.parser(Format.JSON).parse(reader);
            
        	practitioner.toBuilder().communication(Collections.singletonList(CodeableConcept.builder().coding(
        			Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build())).build();
        }
    }
	
	@Test
    public void testMaxValueSetCodeableConceptNotValid() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/ibm/minimal/Practitioner-1.json")) {
        	Practitioner practitioner = FHIRParser.parser(Format.JSON).parse(reader);

            try {
            	practitioner.toBuilder().communication(Collections.singletonList(CodeableConcept.builder().coding(
                		Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                		Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build()).build())).build();
            	fail();
            } catch (IllegalStateException e) {
            }
        }
    }

}
