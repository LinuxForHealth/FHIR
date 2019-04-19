/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.watsonhealth.fhir.model.CommunicationRequest;
import com.ibm.watsonhealth.fhir.model.Condition;
import com.ibm.watsonhealth.fhir.model.Device;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This JUNIT test class contains methods that test the parsing of compartment related search data in the SearchUtil class.
 * @author markd
 *
 */
public class CompartmentParseQueryParmsTest {
    
    /**
     * This method tests parsing compartment related query parms, passing an invalid compartment.
     * @throws Exception
     */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidComparmentName() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        SearchUtil.parseQueryParameters("bogusCompartmentName", "1", Observation.class, queryParameters, null);
    }
    
    /**
     * This method tests parsing compartment related query parms, passing an invalid resource type for the compartment.
     * @throws Exception
     */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidResource() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        SearchUtil.parseQueryParameters("Patient", "1", Device.class, queryParameters, null);
    }
    
    /**
     * This method tests parsing compartment related query parms. 
     * Based on the compartment and resource type, a single inclusion criterion is expected to be 
     * returned by SearchUtil.parseQueryParameters(). 
     * @throws Exception
     */
    @Test 
    public void testSingleInclusionCriteria() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "11";
        Class<? extends Resource> resourceType = Condition.class;
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, null);
        
        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());
        Parameter parm1 = context.getSearchParameters().get(0);
        assertEquals("patient",parm1.getName());
        assertNull(parm1.getNextParameter());
        assertEquals(Type.REFERENCE, parm1.getType());
        assertEquals(1, parm1.getValues().size());
        assertEquals(compartmentName + "/" + compartmentLogicalId, parm1.getValues().get(0).getValueString());
    }
    
    /**
     * This method tests parsing compartment related query parms. 
     * Based on the compartment and resource type, multiple inclusion criteria is expected to be 
     * returned by SearchUtil.parseQueryParameters(). 
     * @throws Exception
     */
    @Test 
    public void testMultiInclusionCriteria() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "RelatedPerson";
        String compartmentLogicalId = "22";
        Class<? extends Resource> resourceType = CommunicationRequest.class;
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, null);
        
        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());
        
        Parameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getName().equals("recipient") ||
                       searchParm.getName().equals("requester") ||
                       searchParm.getName().equals("sender")));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(3, parmCount);
    }
    
    /**
     * This method tests parsing compartment related query parms together with non-compartment related query parms.. 
     * Based on the compartment and resource type, multiple inclusion criteria is expected to be 
     * returned by SearchUtil.parseQueryParameters(). 
     * @throws Exception
     */
    @Test 
    public void testCompartmentWithQueryParms() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "33";
        Class<? extends Resource> resourceType = Observation.class;
        
        String queryStringPart1 = "category=vital-signs";
        String queryStringPart2 = "value-quantity=" + encodeQueryString("eq185|http://unitsofmeasure.org|[lb_av]");
        String queryString = "?" + queryStringPart1 + "&" + queryStringPart2;
        
        queryParameters.put("category", Collections.singletonList("vital-signs"));
        queryParameters.put("value-quantity", Collections.singletonList("eq185|http://unitsofmeasure.org|[lb_av]"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, queryString);
        
        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(3, context.getSearchParameters().size());
        
        // Validate compartment related search parms.
        Parameter searchParm = context.getSearchParameters().get(2);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getName().equals("performer") ||
                       searchParm.getName().equals("subject")));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(2, parmCount);
        
        // Validate non-compartment related search parms.
        for (int i = 0; i < 2; i++) {
            searchParm = context.getSearchParameters().get(i);
            assertTrue((searchParm.getName().equals("category") ||    
                        searchParm.getName().equals("value-quantity")));
            assertNotNull(searchParm.getValues());
            assertEquals(1, searchParm.getValues().size());
        }
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri + " does not contain expected " + queryStringPart1, selfUri.contains(queryStringPart1));
        assertTrue(selfUri + " does not contain expected " + queryStringPart2, selfUri.contains(queryStringPart2));
    }

    /**
     * This method tests parsing compartment related query parms which are not valid. 
     * In lenient mode, this is expected to ignore the query parameter.
     * In strict mode (lenient=false) this should throw an exception.
     * @throws Exception
     */
    @Test 
    public void testCompartmentWithFakeQueryParm() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "33";
        Class<? extends Resource> resourceType = Observation.class;
        
        String queryString = "fakeParameter=fakeValue";
        queryParameters.put("fakeParameter", Collections.singletonList("fakeValue"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, queryString);
        
        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());
        
        // Validate compartment related search parms.
        Parameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getName().equals("performer") ||
                       searchParm.getName().equals("subject")));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(2, parmCount);
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/" + resourceType.getSimpleName(), context);
        assertFalse(selfUri + " contain unexpected query parameter 'fakeParameter'", selfUri.contains(queryString));
        
        try {
            SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, null, false);
            fail("Expected parseQueryParameters to throw due to strict mode but it didn't.");
        } catch (Exception e) {
            assertTrue(e instanceof FHIRSearchException);
        }
    }
    
    /**
    * This method tests parsing null compartment related query parms together with non-compartment related query parms. 
    * SearchUtil.parseQueryParameters() should ignore the null compartment related parms and successfully process the
    * non-compartment parms.
    * @throws Exception
    */
    @Test 
    public void testNoComparmentWithQueryParms() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<? extends Resource> resourceType = Observation.class;
        
        String queryStringPart1 = "category=vital-signs";
        String queryStringPart2 = "value-quantity=" + encodeQueryString("eq185|http://unitsofmeasure.org|[lb_av]");
        String queryString = "?" + queryStringPart1 + "&" + queryStringPart2;
        
        queryParameters.put("category", Collections.singletonList("vital-signs"));
        queryParameters.put("value-quantity", Collections.singletonList("eq185|http://unitsofmeasure.org|[lb_av]"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(null, null, resourceType, queryParameters, queryString);
        
        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(2, context.getSearchParameters().size());
        
        // Validate non-compartment related search parms.
        for (Parameter searchParm : context.getSearchParameters()) {
            assertTrue((searchParm.getName().equals("category") ||    
                        searchParm.getName().equals("value-quantity")));
            assertNotNull(searchParm.getValues());
            assertEquals(1, searchParm.getValues().size());
            assertNull(searchParm.getNextParameter());
        }
        
        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri + " does not contain expected " + queryStringPart1, selfUri.contains(queryStringPart1));
        assertTrue(selfUri + " does not contain expected " + queryStringPart2, selfUri.contains(queryStringPart2));
    }
    
    private String encodeQueryString(String queryString) {
        try {
            URI uri = new URI("http", "dummy", "/path", queryString, null);
            return uri.getRawQuery();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("We should never get here", e);
        }
    }
    
    /**
     * This method is not meant to be run as part of the normal execution of this test class.
     * It's special purpose is to print the contents of SearchUtil.compartmentMap. 
     * To execute this method, un-comment it and make method buildCompartmentMap() public.
     */
    /* @Test 
    public void testLoadCompartmentMap() {
        Map<String, Map<String, List<String>>> compartmentMap = SearchUtil.buildCompartmentMap();
        for (String compartment : compartmentMap.keySet()) {
            System.out.println("Compartment: " + compartment);
            Map<String, List<String>> map = compartmentMap.get(compartment);
            for (String key : map.keySet()) {
                List<String> inclusionCriteria = map.get(key);
                System.out.println("    key: " + key + ", inclusionCriteria: " + inclusionCriteria);
            }
            
        }
    } */

}
