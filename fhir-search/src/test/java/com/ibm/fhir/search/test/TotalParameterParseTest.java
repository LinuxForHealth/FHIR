/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.TotalValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This testng test class contains methods that test the parsing of the _total parameter in the
 * SearchUtil class.
 *
 * @author tbieste
 *
 */
public class TotalParameterParseTest extends BaseSearchTest {

    @Test
    public void testTotalNotSpecified() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(context);
        assertNull(context.getTotalParameter());
    }

    @Test
    public void testTotal() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_total", Arrays.asList("estimate"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(context);
        assertEquals(context.getTotalParameter(), TotalValueSet.ESTIMATE);
    }

    @Test
    public void testTotalInvalid_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_total", Arrays.asList("invalid"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters, true);
        assertNotNull(context);
        assertNull(context.getTotalParameter());
    }

    @Test
    public void testTotalInvalid_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_total", Arrays.asList("invalid"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "An error occurred while parsing parameter '_total'.");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testTotalMultipleParams_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_total", Arrays.asList("none", "accurate"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters, true);
        assertNotNull(context);
        assertEquals(context.getTotalParameter(), TotalValueSet.NONE);
    }

    @Test
    public void testTotalMultipleParams_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_total", Arrays.asList("none", "accurate"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "Search parameter '_total' is specified multiple times");

        }
        assertTrue(isExceptionThrown);
    }
}
