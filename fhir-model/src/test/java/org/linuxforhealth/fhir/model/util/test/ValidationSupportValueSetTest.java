/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test;

import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.config.FHIRModelConfig;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * ValidationSupport tests for the checkValueSetBinding methods
 */
public class ValidationSupportValueSetTest {

    @Test
    public void testCheckValueSetBindingCodeValid1() {
        ValidationSupport.checkValueSetBinding((Code)null, "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingCodeValid2() {
        ValidationSupport.checkValueSetBinding(Code.of("code1"), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingCodeValid3() {
        ValidationSupport.checkValueSetBinding(Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingCodeNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("code1"), "elementName", "valueSetUrl", null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingCodeNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("code1"), "elementName", "valueSetUrl", null, "code2");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingCodesValid() {
        ValidationSupport.checkValueSetBinding(Arrays.asList(Code.of("code1"), Code.of("code2")), "elementName", "valueSetUrl", null, "code1", "code2");
    }

    @Test
    public void testCheckValueSetBindingCodesNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Code.of("code1"), Code.of("code2")), "elementName", "valueSetUrl", null, "code1", "code3");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingStringValid1() {
        ValidationSupport.checkValueSetBinding((org.linuxforhealth.fhir.model.type.String)null, "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingStringValid2() {
        ValidationSupport.checkValueSetBinding(org.linuxforhealth.fhir.model.type.String.of("code1"), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingStringValid3() {
        ValidationSupport.checkValueSetBinding(org.linuxforhealth.fhir.model.type.String.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingStringNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(org.linuxforhealth.fhir.model.type.String.of("code1"), "elementName", "valueSetUrl", null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingStringNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(org.linuxforhealth.fhir.model.type.String.of("code1"), "elementName", "valueSetUrl", null, "code2");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingStringsValid() {
        ValidationSupport.checkValueSetBinding(Arrays.asList(org.linuxforhealth.fhir.model.type.String.of("code1"), org.linuxforhealth.fhir.model.type.String.of("code2")), "elementName", "valueSetUrl", null, "code1", "code2");
    }

    @Test
    public void testCheckValueSetBindingStringsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(org.linuxforhealth.fhir.model.type.String.of("code1"), org.linuxforhealth.fhir.model.type.String.of("code2")), "elementName", "valueSetUrl", null, "code1", "code3");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingUriValid1() {
        ValidationSupport.checkValueSetBinding((Uri)null, "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingUriValid2() {
        ValidationSupport.checkValueSetBinding(Uri.of("code1"), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingUriValid3() {
        ValidationSupport.checkValueSetBinding(Uri.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSetUrl", null, "code1");
    }

    @Test
    public void testCheckValueSetBindingUriNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Uri.of("code1"), "elementName", "valueSetUrl", null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingUriNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Uri.of("code1"), "elementName", "valueSetUrl", null, "code2");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingUrisValid() {
        ValidationSupport.checkValueSetBinding(Arrays.asList(Uri.of("code1"), Uri.of("code2")), "elementName", "valueSetUrl", null, "code1", "code2");
    }

    @Test
    public void testCheckValueSetBindingUrisNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Uri.of("code1"), Uri.of("code2")), "elementName", "valueSetUrl", null, "code1", "code3");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingCodingValid1() {
        ValidationSupport.checkValueSetBinding((Coding)null, "elementName", "valueSetUrl", null);
    }

    @Test
    public void testCheckValueSetBindingCodingValid2() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build(), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodingValid3() {
        ValidationSupport.checkValueSetBinding(Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSet", null);
    }

    @Test
    public void testCheckValueSetBindingCodingNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().build(), "elementName", "valueSet", null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodingNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().code(Code.of("code")).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodingNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("system")).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodingNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build(), "elementName", "valueSet", "system", "code1");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodingNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("system")).extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodingsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodingsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build(), Coding.builder().build()), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantityValid1() {
        ValidationSupport.checkValueSetBinding((Quantity)null, "elementName", "valueSetUrl", null);
    }

    @Test
    public void testCheckValueSetBindingQuantityValid2() {
        ValidationSupport.checkValueSetBinding(Quantity.builder().system(Uri.of("system")).code(Code.of("code")).build(), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingQuantityValid3() {
        ValidationSupport.checkValueSetBinding(Quantity.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSet", null);
    }

    @Test
    public void testCheckValueSetBindingQuantityNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Quantity.builder().build(), "elementName", "valueSet", null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantityNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Quantity.builder().code(Code.of("code")).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantityNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Quantity.builder().system(Uri.of("system")).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantityNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Quantity.builder().system(Uri.of("system")).code(Code.of("code")).build(), "elementName", "valueSet", "system", "code1");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantityNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Quantity.builder().system(Uri.of("system")).extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingQuantitysValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Quantity.builder().system(Uri.of("system")).code(Code.of("code")).build()), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingQuantitysNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Quantity.builder().system(Uri.of("system")).code(Code.of("code")).build(), Quantity.builder().build()), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptValid1() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()).build(), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptValid2() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", "valueSet", null);
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptValid3() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("invalidCode")).build(),
                Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()).build(), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptValid4() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("invalidCode")).build(),
                Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().build()).build(), "elementName", "valueSet", "system");
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
                .coding(Coding.builder().code(Code.of("code")).build(),
                    Coding.builder().system(Uri.of("system")).build(),
                    Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("code")).build(),
                    Coding.builder().system(Uri.of("system")).code(Code.of("invalidCode")).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("invalidSystem"))
                    .code(Code.of("code"))
                    .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(
            CodeableConcept.builder().coding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()).build()), "elementName", "valueSet", "system", "code");
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()).build(),
                CodeableConcept.builder().coding(
                    Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("code")).build(),
                    Coding.builder().system(Uri.of("system")).code(Code.of("invalidCode")).build()).build()), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientValid1() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("code")).build()).build(), "elementName", "valueSet", "system");
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientValid2() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().text(org.linuxforhealth.fhir.model.type.String.of("text")).build(), "elementName", "valueSet", "system", "code");
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientValid3() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("invalidSystem")).build()).build(), "elementName", "valueSet", "system", "code");
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientValid4() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("system")).code(Code.of("code")).build()).build(), "elementName", "valueSet", "system", "code");
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientNotValid1() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("code")).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
            FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
        }
    }

    @Test
    public void testCheckValueSetBindingCodeableConceptLenientNotValid2() {
        FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, false);
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("system")).code(Code.of("invalidCode")).build()).build(), "elementName", "valueSet", "system", "code");
            fail();
        } catch (IllegalStateException e) {
            FHIRModelConfig.setProperty(FHIRModelConfig.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, true);
        }
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeValid1() {
        ValidationSupport.checkValueSetBinding((Code)null, "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeValid2() {
        ValidationSupport.checkValueSetBinding(Code.of("en-AU"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeValid5() {
        ValidationSupport.checkValueSetBinding(Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("1"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("y--z"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("invalidLanguageCode"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Code.builder().value("").extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("i-klingon"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeNotValid6() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("he-IL-u-ca-hebrew-tz-jeruslm"), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodesValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Code.of("ar")), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodesNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Code.of("ar"), Code.of("1")), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingValid1() {
        ValidationSupport.checkValueSetBinding(Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingValid2() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingValid3() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("en-AU")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("y--z")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().code(Code.of("ar")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid6() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid7() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().code(Code.of("ar")).extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid8() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("i-klingon")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingNotValid9() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("he-IL-u-ca-hebrew-tz-jeruslm")).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

   @Test
    public void testCheckValueSetBindingLanguageCodingsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodingsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Coding.builder().code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build()), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptValid1() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptValid2() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptValid3() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptValid4() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build(),
                Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().code(Code.of("ar")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(
                Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build()).build(), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(CodeableConcept.builder().coding(
            Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("ar")).build()).build()), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingLanguageCodeableConceptsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().code(Code.of("ar")).build()).build(),
                CodeableConcept.builder().coding(
                        Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("ar")).build(),
                        Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguageCode")).build()).build()), "elementName", ValidationSupport.ALL_LANG_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingUcumCodeValid1() {
        ValidationSupport.checkValueSetBinding((Code)null, "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeValid2() {
        ValidationSupport.checkValueSetBinding(Code.of("10.uN.s/(cm.m2)"), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeValid3() {
        ValidationSupport.checkValueSetBinding(Code.of("%{Activity}"), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeValid4() {
        ValidationSupport.checkValueSetBinding(Code.of("[mi_us]"), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeValid5() {
        ValidationSupport.checkValueSetBinding(Code.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Code.builder().build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("{invalid{annotation}}"), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("invalid space"), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Code.of("]invalidBracket["), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Code.builder().value("").extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodesValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Code.of("mg/dL")), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodesNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Code.of("mg/dL"), Code.of("[[]]")), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid1() {
        ValidationSupport.checkValueSetBinding((Coding)null, "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid2() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid3() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("cm[H2O]")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid4() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("g.m/({hb}.m2)")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid5() {
        ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/mmol")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingValid6() {
        ValidationSupport.checkValueSetBinding(Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().code(Code.of("mg/mmol")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid4() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.builder().build()).code(Code.of("mg/mmol")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid5() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.builder().build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid6() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/mmol")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid7() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid space")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingNotValid8() {
        try {
            ValidationSupport.checkValueSetBinding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("{embedded{}brace}")).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodingsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/dL")).build()), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodingsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(Coding.builder().code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("mg/dL")).build()), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptValid1() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptValid2() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptValid3() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("g.m/({hb}.m2)")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptValid4() {
        ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
            .coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                Coding.builder().extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptNotValid1() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder().coding(Coding.builder().build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptNotValid2() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
                .coding(Coding.builder().code(Code.of("mg/dL")).build(),
                    Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).build(),
                    Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                    Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid ucum code")).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptNotValid3() {
        try {
            ValidationSupport.checkValueSetBinding(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("invalidSystem"))
                    .code(Code.of("mg/dL"))
                    .extension(Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build()).build()).build(), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptsValid() {
        ValidationSupport.checkValueSetBinding(Collections.singletonList(
            CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build()), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
    }

    @Test
    public void testCheckValueSetBindingUcumCodeableConceptsNotValid() {
        try {
            ValidationSupport.checkValueSetBinding(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("10*6/L")).build()).build(),
                CodeableConcept.builder().coding(
                    Coding.builder().system(Uri.of("invalidSystem")).code(Code.of("mg/dL")).build(),
                    Coding.builder().system(Uri.of(ValidationSupport.UCUM_CODE_SYSTEM_URL)).code(Code.of("invalid code")).build()).build()), "elementName", ValidationSupport.UCUM_UNITS_VALUE_SET_URL, null);
            fail();
        } catch (IllegalStateException e) {
        }
    }

}
