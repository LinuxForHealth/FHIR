/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.resource.Patient;

public class EmptyResourceTest {
    @Test
    public void testEmptyPatient() {
        try {
            Patient.builder().build();
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "global-1: All FHIR elements must have a @value or children");
        }
    }
}
