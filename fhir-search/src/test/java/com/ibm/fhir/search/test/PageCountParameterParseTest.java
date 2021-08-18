/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This testng test class contains methods that test the parsing of the _page and _count parameter in the
 * SearchUtil class.
 *
 * @author tbieste
 *
 */
public class PageCountParameterParseTest extends BaseSearchTest {

    @Test
    public void testPageAndCount() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_page", Arrays.asList("2"));
        queryParameters.put("_count", Arrays.asList("20"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(context);
        assertEquals(context.getPageNumber(), 2);
        assertEquals(context.getPageSize(), 20);
    }

    @Test
    public void testPageAndCountAboveMax() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_page", Arrays.asList("2"));
        queryParameters.put("_count", Arrays.asList(String.valueOf(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX + 1)));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(context);
        assertEquals(context.getPageNumber(), 2);
        assertEquals(context.getPageSize(), FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX);
    }

    @Test
    public void testPageAndCountInvalid_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_page", Arrays.asList("invalid"));
        queryParameters.put("_count", Arrays.asList("-1"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters, true);
        assertNotNull(context);
        assertEquals(context.getPageNumber(), 1);
        assertEquals(context.getPageSize(), 10);
    }

    @Test
    public void testPageAndCountInvalidPage_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_page", Arrays.asList("invalid"));
        queryParameters.put("_count", Arrays.asList("20"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "An error occurred while parsing parameter '_page'.");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testPageAndCountInvalidCount_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_page", Arrays.asList("2"));
        queryParameters.put("_count", Arrays.asList("-1"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "An error occurred while parsing parameter '_count'.");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testPageAndCountMultipleParams_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_page", Arrays.asList("3", "4"));
        queryParameters.put("_count", Arrays.asList("30", "40"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(Patient.class, queryParameters, true);
        assertNotNull(context);
        assertEquals(context.getPageNumber(), 3);
        assertEquals(context.getPageSize(), 30);
    }

    @Test
    public void testPageAndCountMultiplePageParams_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_page", Arrays.asList("3", "4"));
        queryParameters.put("_count", Arrays.asList("30"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "Search parameter '_page' is specified multiple times");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testPageAndCountMultipleCountParams_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_page", Arrays.asList("3"));
        queryParameters.put("_count", Arrays.asList("30", "40"));
        try {
            SearchUtil.parseQueryParameters(Patient.class, queryParameters, false);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "Search parameter '_count' is specified multiple times");

        }
        assertTrue(isExceptionThrown);
    }
}
