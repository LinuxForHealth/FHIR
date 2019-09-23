/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test.testng;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRParameters.Modifier;
import com.ibm.fhir.client.FHIRParameters.ValuePrefix;

/**
 * Tests related to the FHIRParameters class.
 */
public class FHIRParametersTest {
    
    @Test
    public void testParameter1() {
        FHIRParameters p = new FHIRParameters();
        p.count(5).format("application/xml").page(3).since("2016-01-01");
        String queryString = p.queryString();
        assertNotNull(queryString);
        assertTrue(queryString.contains("_count=5"));
        assertTrue(queryString.contains("_format=application/xml"));
        assertTrue(queryString.contains("_page=3"));
        assertTrue(queryString.contains("_since=2016-01-01"));
    }
    
    @Test
    public void testParameter2() {
        FHIRParameters p = new FHIRParameters();
        p.searchParam("name", Modifier.CONTAINS, "Ortiz")
            .searchParam("favorite-color", Modifier.IN, "red", "green", "blue");
        String queryString = p.queryString();
        assertNotNull(queryString);
        assertTrue(queryString.contains("name:contains=Ortiz"));
        assertTrue(queryString.contains("favorite-color:in=red,green,blue"));
    }
    
    @Test
    public void testParameter3() {
        FHIRParameters p = new FHIRParameters();
        p.searchParam("name", ValuePrefix.EQ, "Ortiz")
        .searchParam("favorite-color", ValuePrefix.NE, "red");
        String queryString = p.queryString();
        assertNotNull(queryString);
        assertTrue(queryString.contains("name=eqOrtiz"));
        assertTrue(queryString.contains("favorite-color=nered"));
    }
}
