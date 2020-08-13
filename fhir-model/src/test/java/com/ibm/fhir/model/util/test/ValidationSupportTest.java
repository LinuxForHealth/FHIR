/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ValidationSupport;

/**
 * ValidationSupport Test
 */
public class ValidationSupportTest {

    @Test
    public void testCheckLanguageCodeValid() {
        ValidationSupport.checkLanguageCode(null, "elementName");
        ValidationSupport.checkLanguageCode(Code.of("en-AU"), "elementName");
        ValidationSupport.checkLanguageCode(Code.of("i-klingon"), "elementName");
        ValidationSupport.checkLanguageCode(Code.of("he-IL-u-ca-hebrew-tz-jeruslm"), "elementName");
    }

    @Test
    public void testCheckLanguageCodeNotValid() {
        try {
            ValidationSupport.checkLanguageCode(Code.of("1"), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkLanguageCode(Code.of("y--z"), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkLanguageCode(Code.of("invalidLanguageCode"), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckLanguageCodesValid() {
        ValidationSupport.checkLanguageCodes(Collections.singletonList(Code.of("ar")), "elementName");
    }
    
    @Test
    public void testCheckLanguageCodesNotValid() {
        try {
            ValidationSupport.checkLanguageCodes(Arrays.asList(Code.of("ar"), Code.of("1")), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckLanguageCodingValid() {
        ValidationSupport.checkLanguageCoding(Coding.builder().code(Code.of("ar")).build(), "elementName");
        ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build(), "elementName");
        ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("en-AU")).build(), "elementName");
        ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("i-klingon")).build(), "elementName");
        ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("he-IL-u-ca-hebrew-tz-jeruslm")).build(), "elementName");
    }

    @Test
    public void testCheckLanguageCodingNotValid() {
        try {
            ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try { 
            ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("y--z")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkLanguageCoding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckLanguageCodingsValid() {
        ValidationSupport.checkLanguageCodings(Collections.singletonList(Coding.builder().code(Code.of("ar")).build()), "elementName");
    }

    @Test
    public void testCheckLanguageCodingsNotValid() {
        try {
            ValidationSupport.checkLanguageCodings(Arrays.asList(Coding.builder().code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build()), "elementName");
            fail();    
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckLanguageCodeableConceptValid() {
        ValidationSupport.checkLanguageCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().code(Code.of("ar")).build()).build(), "elementName");
        ValidationSupport.checkLanguageCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build(), "elementName");
        ValidationSupport.checkLanguageCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build(), "elementName");
    }

    @Test
    public void testCheckLanguageCodeableConceptNotValid() {
        try {
            ValidationSupport.checkLanguageCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build()).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckLanguageCodeableConceptsValid() {
        ValidationSupport.checkLanguageCodeableConcepts(Collections.singletonList(CodeableConcept.builder().coding(
                Coding.builder().code(Code.of("ar")).build()).build()), "elementName");
    }

    @Test
    public void testCheckLanguageCodeableConceptsNotValid() {
        try {
            ValidationSupport.checkLanguageCodeableConcepts(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().code(Code.of("ar")).build()).build(),
                CodeableConcept.builder().coding(
                        Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                        Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build()).build()), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
}
