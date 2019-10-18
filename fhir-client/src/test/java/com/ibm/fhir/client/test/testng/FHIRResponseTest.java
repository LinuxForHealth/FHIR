/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test.testng;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.client.impl.FHIRResponseImpl;

/**
 * Basic tests related to the FHIRResponse interface.
 */
public class FHIRResponseTest {

    @Test
    public void testParseLocation1() throws Exception {
        FHIRResponse response = new FHIRResponseImpl(null);
        String[] tokens = response.parseLocation("http://localhost:9080/fhir-server/api/v4/Patient/12345/_history/3");
        assertNotNull(tokens);
        assertEquals(3, tokens.length);
        assertEquals("Patient", tokens[0]);
        assertEquals("12345", tokens[1]);
        assertEquals("3", tokens[2]);
    }

    @Test
    public void testParseLocation2() throws Exception {
        FHIRResponse response = new FHIRResponseImpl(null);
        String[] tokens = response.parseLocation("http://localhost:9080/fhir-server/api/v4/Patient/12345");
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("Patient", tokens[0]);
        assertEquals("12345", tokens[1]);
    }

    @Test(expectedExceptions = { NullPointerException.class })
    public void testParseLocation3() throws Exception {
        FHIRResponse response = new FHIRResponseImpl(null);
        response.parseLocation(null);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testParseLocation4() throws Exception {
        FHIRResponse response = new FHIRResponseImpl(null);
        response.parseLocation("badlocation");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testParseLocation5() throws Exception {
        FHIRResponse response = new FHIRResponseImpl(null);
        response.parseLocation("a/_history./b");
    }
}
