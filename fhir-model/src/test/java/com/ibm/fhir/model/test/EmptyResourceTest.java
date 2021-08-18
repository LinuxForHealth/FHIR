/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.test;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Patient.Link;
import com.ibm.fhir.model.type.Narrative;

public class EmptyResourceTest {
    @Test
    public void testEmptyPatient() {
        Patient.builder().build();
    }

    @Test
    public void testPatientWithEmptyNarrative() {
        Patient.builder().text(Narrative.EMPTY).build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInvalidPatient() {
        Patient.builder().link((Link)null).build();
    }
}
