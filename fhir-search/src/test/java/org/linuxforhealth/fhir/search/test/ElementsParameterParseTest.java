/*
 * (C) Copyright IBM Corp. 2018, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * This testng test class contains methods that test the parsing of the search result _elements parameter in the
 * SearchUtil class.
 *
 * @author markd
 * @author pbastide
 *
 */
public class ElementsParameterParseTest extends BaseSearchTest {

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalid_singleElement() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Collections.singletonList("_id"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testFake_singleElement_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=_id";

        queryParameters.put("_elements", Collections.singletonList("bogus"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(context);
        assertTrue(context.getElementsParameters() == null || context.getElementsParameters().size() == 0);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertFalse(selfUri.contains(queryString), selfUri + " contains unexpected " + queryString);
        assertEquals(2, context.getOutcomeIssues().size());
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testFake_singleElement_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Collections.singletonList("bogus"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    @Test
    public void testFake_multiElements_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Collections.singletonList("id,contact,bogus,name"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(3, context.getElementsParameters().size());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains("id"), selfUri + " does not contain expected elements param 'id'");
        assertTrue(selfUri.contains("contact"), selfUri + " does not contain expected elements param 'contact'");
        assertTrue(selfUri.contains("name"), selfUri + " does not contain expected elements param 'name'");
        assertFalse(selfUri.contains("bogus"), selfUri + " contains unexpected elements param 'bogus'");
        assertEquals(2, context.getOutcomeIssues().size());
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testFake_multiElements_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Collections.singletonList("id,contact,bogus,name"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    @Test
    public void testFake_multiElementParams_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Arrays.asList("id","contact","bogus","name"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(1, context.getElementsParameters().size());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains("id"), selfUri + " does not contain expected elements param 'id'");
        assertEquals(1, context.getOutcomeIssues().size());
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testFake_multiElementParams_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Arrays.asList("id","contact","bogus","name"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    @Test
    public void testValid_singleElement() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=name";

        queryParameters.put("_elements", Collections.singletonList("name"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(1, context.getElementsParameters().size());
        assertEquals("name", context.getElementsParameters().get(0));

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains(queryString), selfUri + " does not contain expected " + queryString);
    }

    @Test
    public void testValidMultiElements() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_elements", Collections.singletonList("name,photo,identifier"));
        FHIRSearchContext context = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());

        assertEquals(context.getElementsParameters().size(), 3);
        assertTrue(context.getElementsParameters().contains("name"));
        assertTrue(context.getElementsParameters().contains("photo"));
        assertTrue(context.getElementsParameters().contains("identifier"));

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains("name"), selfUri + " does not contain expected elements param 'name'");
        assertTrue(selfUri.contains("photo"), selfUri + " does not contain expected elements param 'photo'");
        assertTrue(selfUri.contains("identifier"), selfUri + " does not contain expected elements param 'identifier'");
    }

}
