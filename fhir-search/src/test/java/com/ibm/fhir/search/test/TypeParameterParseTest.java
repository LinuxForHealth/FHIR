/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * Test the parsing of the search result _type parameter in the SearchUtil class.
 */
public class TypeParameterParseTest extends BaseSearchTest {

    @Test
    public void testType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Arrays.asList("Patient"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters);
        assertNotNull(context);
        assertNotNull(context.getSearchResourceTypes());
        assertEquals(context.getSearchResourceTypes().size(), 1);
        assertTrue(context.getSearchResourceTypes().contains("Patient"));
    }

    @Test
    public void testTypeMultiple() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Collections.singletonList("Patient,Practitioner"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters);
        assertNotNull(context);
        assertNotNull(context.getSearchResourceTypes());
        assertEquals(context.getSearchResourceTypes().size(), 2);
        assertTrue(context.getSearchResourceTypes().contains("Patient"));
        assertTrue(context.getSearchResourceTypes().contains("Practitioner"));
    }

    @Test
    public void testTypeNonSystemSearch_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Arrays.asList("Patient"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Patient.class, queryParameters, true, true);
        assertNotNull(context);
        assertNull(context.getSearchResourceTypes());
    }

    @Test
    public void testTypeNonSystemSearch_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_type", Arrays.asList("Patient"));
        try {
            searchHelper.parseQueryParameters(Patient.class, queryParameters, false, true);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "_type parameter is only supported for whole-system search");
        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testTypeMultipleParams() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Arrays.asList("Patient", "Practitioner"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters, true, true);
        assertNotNull(context);
        assertNotNull(context.getSearchResourceTypes());
        assertEquals(context.getSearchResourceTypes().size(), 2);
        assertTrue(context.getSearchResourceTypes().contains("Patient"));
        assertTrue(context.getSearchResourceTypes().contains("Practitioner"));
    }

    @Test
    public void testTypeMultipleParams_oneInvalid_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Arrays.asList("Patient", "Bogus", "Practitioner"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters, true, true);
        assertNotNull(context);
        assertNotNull(context.getSearchResourceTypes());
        assertEquals(context.getSearchResourceTypes().size(), 2);
        assertTrue(context.getSearchResourceTypes().contains("Patient"));
        assertTrue(context.getSearchResourceTypes().contains("Practitioner"));
    }

    @Test
    public void testTypeMultipleParams_oneInvalid_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_type", Arrays.asList("Patient", "Bogus", "Practitioner"));
        try {
            searchHelper.parseQueryParameters(Resource.class, queryParameters, false, true);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "_type parameter has invalid resource type: Bogus");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testTypeInvalid_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Collections.singletonList("invalid,Practitioner"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters, true, true);
        assertNotNull(context);
        assertNotNull(context.getSearchResourceTypes());
        assertEquals(context.getSearchResourceTypes().size(), 1);
        assertTrue(context.getSearchResourceTypes().contains("Practitioner"));
    }

    @Test
    public void testTypeInvalid_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_type", Collections.singletonList("invalid,Practitioner"));
        try {
            searchHelper.parseQueryParameters(Resource.class, queryParameters, false, true);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "_type parameter has invalid resource type: invalid");

        }
        assertTrue(isExceptionThrown);
    }

    @Test
    public void testTypeAbstract_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_type", Collections.singletonList("Resource"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(Resource.class, queryParameters, true, true);
        assertNotNull(context);
        assertNull(context.getSearchResourceTypes());
    }

    @Test
    public void testTypeAbstract_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        boolean isExceptionThrown = false;

        queryParameters.put("_type", Collections.singletonList("Resource"));
        try {
            searchHelper.parseQueryParameters(Resource.class, queryParameters, false, true);
        } catch(Exception ex) {
            isExceptionThrown = true;
            assertEquals(ex.getMessage(), "_type parameter has invalid resource type: Resource");

        }
        assertTrue(isExceptionThrown);
    }

}
