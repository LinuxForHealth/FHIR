/*
 * (C) Copyright IBM Corp. 2018, 2021
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
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This testng test class contains methods that test the parsing of the search result _summary parameter in the
 * SearchUtil class.
 *
 * @author Albert Wang
 *
 */
public class SummaryParameterParseTest extends BaseSearchTest {

    @Test
    public void testSummary() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_summary", Arrays.asList("true"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(context);
        assertNotNull(context.getSummaryParameter());
        assertEquals(context.getSummaryParameter(), SummaryValueSet.TRUE);
    }

    @Test
    public void testSummaryMultiple_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_summary", Arrays.asList("data","true"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(context);
        assertNotNull(context.getSummaryParameter());
        assertEquals(context.getSummaryParameter(), SummaryValueSet.DATA);
    }

    @Test
    public void testSummaryMultiple_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        boolean isExceptionThrown = false;

        queryParameters.put("_summary", Arrays.asList("data","true"));
        try {
            SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "Search parameter '_summary' is specified multiple times");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testSummaryInvalid_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_summary", Arrays.asList("invalid"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(context);
        assertNull(context.getSummaryParameter());
    }

    @Test
    public void testSummaryInvalid_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        boolean isExceptionThrown = false;

        queryParameters.put("_summary", Arrays.asList("invalid"));
        try {
            SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "An error occurred while parsing parameter '_summary'.");
        }
        assertTrue(isExceptionThrown);
    }

}
