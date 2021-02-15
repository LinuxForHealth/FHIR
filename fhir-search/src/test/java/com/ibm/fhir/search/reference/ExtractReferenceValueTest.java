/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.reference;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Unit tests exercising the ReferenceParameterHandler utility functions
 */
public class ExtractReferenceValueTest {
    private static final String BASE = "https://example.com/";

    @BeforeClass
    public void setup() {
        // Make sure we reset the cached value of the service base
        // so that the new value will be computed using our BASE string
        // This is required to make this test deterministic
        ReferenceUtil.clearServiceBase();
        FHIRRequestContext context = new FHIRRequestContext();
        context.setOriginalRequestUri(BASE);
        FHIRRequestContext.set(context);
    }

    @AfterClass
    public void tidy() {
        // clear out the request context so we don't confuse other tests which
        // have forgotten to set this
        FHIRRequestContext.set(null);
    }

    @Test
    public void testPatientWithSystemUrlPrefix() throws Exception {
        final String patientRef = "Patient/17456ca7efe-dfe09119-26e3-4f7f-939e-ece768ca35ad"; // fake
        final String valueString = BASE + patientRef;
        final String refValue = SearchUtil.extractReferenceValue(valueString);
        assertEquals(refValue, patientRef);
    }

    @Test
    public void testPlainPatientRef() throws Exception {
        final String patientRef = "Patient/17456ca7efe-dfe09119-26e3-4f7f-939e-ece768ca35ad"; // fake

        // test reference without the prefix
        final String valueString = patientRef;
        final String refValue = SearchUtil.extractReferenceValue(valueString);
        assertEquals(refValue, patientRef);
    }

    @Test
    public void testPatientLogicalId() throws Exception {
        final String patientRef = "17456ca7efe-dfe09119-26e3-4f7f-939e-ece768ca35ad"; // fake

        // test reference without the prefix
        final String valueString = patientRef;
        final String refValue = SearchUtil.extractReferenceValue(valueString);
        assertEquals(refValue, patientRef);
    }

    @Test
    public void testTokenWithSystem() throws Exception {
        final String codeVal = BASE + "system|a-code-value";
        final String refValue = SearchUtil.extractReferenceValue(codeVal);
        assertEquals(refValue, codeVal);
    }

    @Test
    public void testCompositeTokenWithSystem() throws Exception {
        final String codeVal = BASE + "system|a-code-value" + "$" + BASE + "system|another-code-value";
        final String refValue = SearchUtil.extractReferenceValue(codeVal);
        assertEquals(refValue, codeVal);
    }
}