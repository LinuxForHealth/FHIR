/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * ValidationSupport tests for the checkReferenceType methods
 */
public class ValidationSupportReferenceTest {
    private final String NAME = "test";

    /**
     * Test the checkReferenceType overload for choice types
     */
    @Test
    public void testCheckReference_choice() {
        Element choiceElement = Reference.builder().reference("Patient/1").build();

        ValidationSupport.checkReferenceType(choiceElement, NAME, "Patient");
        ValidationSupport.checkReferenceType(choiceElement, NAME, "Patient", "Group");
        ValidationSupport.checkReferenceType(choiceElement, NAME, "Group", "Patient");

        try {
            ValidationSupport.checkReferenceType(choiceElement, NAME, "Observation");
            fail("Expected validation error but didn't get one");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                "Resource type found in reference value: 'Patient/1' for element: 'test' must be one of: [Observation]");
        }

        choiceElement = Reference.builder().reference("Bogus/1").build();
        try {
            ValidationSupport.checkReferenceType(choiceElement, NAME, "Observation");
            fail("Expected validation error but didn't get one");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                "Invalid reference value or resource type not found in reference value: 'Bogus/1' for element: 'test'");
        }
    }

    /**
     * Test the checkReferenceType overload for repeating elements
     */
    @Test
    public void testCheckReference_list() {
        List<Reference> refs = new ArrayList<>();
        refs.add(Reference.builder().reference("Patient/1").build());
        checkRefList(refs);

        refs.add(Reference.builder().reference("Patient/2").build());
        checkRefList(refs);

        refs.add(Reference.builder().type(Uri.of("Patient")).build());
        checkRefList(refs);

        // not a very likely scenario, but added just to make sure we don't blow up in an unusual way
        refs.add(null);
        checkRefList(refs);

        refs.add(Reference.builder().reference("Observation/fail").build());
        try {
            checkRefList(refs);
            fail("Expected validation error but didn't get one");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in reference value: 'Observation/fail' for element: 'test' must be one of: [Patient]");
        }
    }

    private void checkRefList(List<Reference> refs) {
        ValidationSupport.checkReferenceType(refs, NAME, "Patient");
        ValidationSupport.checkReferenceType(refs, NAME, "Patient", "Group");
        ValidationSupport.checkReferenceType(refs, NAME, "Group", "Patient");

        try {
            ValidationSupport.checkReferenceType(refs, NAME, "Observation");
            fail("Expected validation error but didn't get one");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in reference value: 'Patient/1' for element: 'test' must be one of: [Observation]");
        }
    }

    /**
     * Test the checkReferenceType method with a null Reference / null Reference.reference
     */
    @Test
    public void testCheckReference_null() {
        Reference ref = null;
        ValidationSupport.checkReferenceType(ref, NAME, "Patient");

        ref = Reference.builder()
                .extension(FHIRUtil.DATA_ABSENT_REASON_UNKNOWN)
                .build();
        ValidationSupport.checkReferenceType(ref, NAME, "Patient");

        ref = Reference.builder()
                .type(Uri.of("Patient"))
                .build();
        ValidationSupport.checkReferenceType(ref, NAME, "Patient");

        ref = Reference.builder()
                .type(Uri.of("Observation"))
                .build();
        try {
            ValidationSupport.checkReferenceType(ref, NAME, "Patient");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in Reference.type: 'Observation' for element: 'test' must be one of: [Patient]");
        }

        ref = Reference.builder()
                .type(Uri.of("Bogus"))
                .build();
        try {
            ValidationSupport.checkReferenceType(ref, NAME, "Patient");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in Reference.type: 'Bogus' for element: 'test' must be a valid resource type name");
        }
    }

    /**
     * Test the checkReferenceType overload for a standalone reference element
     */
    @Test
    public void testCheckReference() {
        Reference ref = Reference.builder().reference("Patient/1").build();

        ValidationSupport.checkReferenceType(ref, NAME, "Patient");
        ValidationSupport.checkReferenceType(ref, NAME, "Patient", "Group");
        ValidationSupport.checkReferenceType(ref, NAME, "Group", "Patient");

        try {
            ValidationSupport.checkReferenceType(ref, NAME, "Observation");
            fail("Expected validation error but didn't get one");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in reference value: 'Patient/1' for element: 'test' must be one of: [Observation]");
        }

        // type matches the implied reference type of the ref value (Patient/1)
        ref = ref.toBuilder()
                .type(Uri.of("Patient"))
                .build();
        ValidationSupport.checkReferenceType(ref, NAME, "Patient");

        // type does not match the implied reference type of the ref value
        ref = ref.toBuilder()
                .type(Uri.of("Observation"))
                .build();
        try {
            ValidationSupport.checkReferenceType(ref, NAME, "Patient");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in Reference.type: 'Observation' for element: 'test' must be one of: [Patient]");
        }

        // type isn't even valid
        ref = ref.toBuilder()
                .type(Uri.of("Bogus"))
                .build();
        try {
            ValidationSupport.checkReferenceType(ref, NAME, "Patient");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "Resource type found in Reference.type: 'Bogus' for element: 'test' must be a valid resource type name");
        }
    }
}
