/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This JUNIT test class contains methods that test the parsing of the search result _elements parameter in the SearchUtil class. 
 * @author markd
 *
 */
public class ElementsParameterParseTest {
    
    @Test(expected = FHIRSearchException.class)
    public void testInvalid_singleElement() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=_id";
        
        queryParameters.put("_elements", Collections.singletonList("_id"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test
    public void testFake_singleElement() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=_id";
        
        queryParameters.put("_elements", Collections.singletonList("bogus"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertTrue(context.getElementsParameters() == null || context.getElementsParameters().size() == 0);
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertFalse(selfUri + " contains unexpected " + queryString, selfUri.contains(queryString));
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testFake_singleElement_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=_id";
        
        queryParameters.put("_elements", Collections.singletonList("bogus"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString, false);
    }
    
    @Test
    public void testFake_multiElements() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=id,contact,bogus,name";
        
        queryParameters.put("_elements", Arrays.asList("id","contact","bogus","name"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(3, context.getElementsParameters().size());
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri + " does not contain expected elements param 'id'", selfUri.contains("id"));
        assertTrue(selfUri + " does not contain expected elements param 'contact'", selfUri.contains("contact"));
        assertTrue(selfUri + " does not contain expected elements param 'name'", selfUri.contains("name"));
        assertFalse(selfUri + " contains unexpected elements param 'bogus'", selfUri.contains("bogus"));
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testFake_multiElements_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=id,contact,bogus,name";
        
        queryParameters.put("_elements", Arrays.asList("id","contact","bogus","name"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString, false);
    }
    
    @Test 
    public void testValid_singleElement() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=name";
        
        queryParameters.put("_elements", Arrays.asList("name"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(1, context.getElementsParameters().size());
        assertEquals("name", context.getElementsParameters().get(0));
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri + " does not contain expected " + queryString, selfUri.contains(queryString));
    }
    
    @Test 
    public void testValid_multiElements() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_elements=name,photo,animal,identifier";
        
        queryParameters.put("_elements", Arrays.asList("name","photo","animal","identifier"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNotNull(context.getElementsParameters());
        assertEquals(4, context.getElementsParameters().size());
        for (String element : context.getElementsParameters()) {
            assertTrue(queryParameters.get("_elements").contains(element));
        }
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri + " does not contain expected elements param 'name'", selfUri.contains("name"));
        assertTrue(selfUri + " does not contain expected elements param 'photo'", selfUri.contains("photo"));
        assertTrue(selfUri + " does not contain expected elements param 'animal'", selfUri.contains("animal"));
        assertTrue(selfUri + " does not contain expected elements param 'identifier'", selfUri.contains("identifier"));
    }
    
}
