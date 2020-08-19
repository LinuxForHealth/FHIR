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
    
    @Test
    public void testCheckUcumCodeValid() {
        ValidationSupport.checkUcumCode(null, "elementName");
        ValidationSupport.checkUcumCode(Code.of("10.uN.s/(cm.m2)"), "elementName");
        ValidationSupport.checkUcumCode(Code.of("%{Activity}"), "elementName");
        ValidationSupport.checkUcumCode(Code.of("[mi_us]"), "elementName");
    }

    @Test
    public void testCheckUcumCodeNotValid() {
        try {
            ValidationSupport.checkUcumCode(Code.of(null), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCode(Code.of("{invalid{annotation}}"), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCode(Code.of("invalid space"), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCode(Code.of("]invalidBracket["), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckUcumCodesValid() {
        ValidationSupport.checkUcumCodes(Collections.singletonList(Code.of("mg/dL")), "elementName");
    }
    
    @Test
    public void testCheckUcumCodesNotValid() {
        try {
            ValidationSupport.checkUcumCodes(Arrays.asList(Code.of("mg/dL"), Code.of("[[]]")), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckUcumCodingValid() {
        ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build(), "elementName");
        ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("cm[H2O]")).build(), "elementName");
        ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("g.m/({hb}.m2)")).build(), "elementName");
        ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/mmol")).build(), "elementName");
    }

    @Test
    public void testCheckUcumCodingNotValid() {
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().code(Code.of("mg/mmol")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(null)).code(Code.of("mg/mmol")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of(null)).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/mmol")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try { 
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid space")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
        try {
            ValidationSupport.checkUcumCoding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("{embedded{}brace}")).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckUcumCodingsValid() {
        ValidationSupport.checkUcumCodings(Collections.singletonList(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/dL")).build()), "elementName");
    }

    @Test
    public void testCheckUcumCodingsNotValid() {
        try {
            ValidationSupport.checkUcumCodings(Arrays.asList(Coding.builder().code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/dL")).build()), "elementName");
            fail();    
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckUcumCodeableConceptValid() {
        ValidationSupport.checkUcumCodeableConcept(CodeableConcept.builder().coding(
            Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build(), "elementName");
        ValidationSupport.checkUcumCodeableConcept(CodeableConcept.builder().coding(
            Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("cm[H2O]")).build()).build(), "elementName");
        ValidationSupport.checkUcumCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("g.m/({hb}.m2)")).build()).build(), "elementName");
    }

    @Test
    public void testCheckUcumCodeableConceptNotValid() {
        try {
            ValidationSupport.checkUcumCodeableConcept(CodeableConcept.builder().coding(
                Coding.builder().build(),
                Coding.builder().code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid ucum code")).build()).build(), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
    @Test
    public void testCheckUcumCodeableConceptsValid() {
        ValidationSupport.checkUcumCodeableConcepts(Collections.singletonList(CodeableConcept.builder().coding(
            Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build()), "elementName");
    }

    @Test
    public void testCheckUcumCodeableConceptsNotValid() {
        try {
            ValidationSupport.checkUcumCodeableConcepts(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build(),
                CodeableConcept.builder().coding(
                    Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                    Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid code")).build()).build()), "elementName");
            fail();
        }
        catch (IllegalStateException e) {}
    }
    
}
