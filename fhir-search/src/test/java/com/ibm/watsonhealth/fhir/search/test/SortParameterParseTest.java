/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.SortParameter;
import com.ibm.watsonhealth.fhir.search.SortParameter.SortDirection;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This JUNIT test class contains methods that test the parsing of sort parameters in the SearchUtil class. 
 * @author markd
 *
 */
public class SortParameterParseTest {
    
    /**
     *  Tests an invalid direction modifier on the _sort query parameter.
     * @throws Exception
     */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidDirection() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_sort:xxx=birthdate";
        
        queryParameters.put("_sort:xxx", Collections.singletonList("birthdate"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
    }
    
    /**
     *  Tests an invalid sort parameter value.
     * @throws Exception
     */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidSortParm() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_sort=bogusSortParm";
        
        queryParameters.put("_sort", Collections.singletonList("bogusSortParm"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
    }
    
    /**
     *  Tests a valid sort with: asc modifier, and a valid parameter value.
     * @throws Exception
     */
    @Test 
    public void testValidSortParm() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        SortDirection direction = SortDirection.ASCENDING; 
        String sortParmName = "birthdate"; 
        String queryString = "&_sort:" + direction.value() + "=" + sortParmName;
        
        queryParameters.put("_sort:" + direction.value(), Collections.singletonList(sortParmName));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
        
        // Do sort parameter validation
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1,searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(1, sortParm.getQueryStringIndex());
        assertEquals(Parameter.Type.DATE, sortParm.getType());
        
        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());
    }
    
    /**
     *  Tests a valid sort with: desc modifier, and a valid parameter value.
     * @throws Exception
     */
    @Test 
    public void testValidSortParm1() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        SortDirection direction = SortDirection.DESCENDING; 
        String sortParmName = "birthdate"; 
        String queryString = "&_sort:" + direction.value() + "=" + sortParmName;
        
        queryParameters.put("_sort:" + direction.value(), Collections.singletonList(sortParmName));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        
        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1,searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(1, sortParm.getQueryStringIndex());
        assertEquals(Parameter.Type.DATE, sortParm.getType());
        
        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());
    }
    
    /**
     *  Tests a valid sort with no modifier, and a valid parameter value.
     * @throws Exception
     */
    @Test
    public void testValidSortParm2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String sortParmName = "birthdate"; 
        String queryString = "&_sort" + "=" + sortParmName;
        
        queryParameters.put("_sort", Collections.singletonList(sortParmName));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        
        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1,searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(SortDirection.ASCENDING, sortParm.getDirection());
        assertEquals(1, sortParm.getQueryStringIndex());
        assertEquals(Parameter.Type.DATE, sortParm.getType());
        
        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());
    }
    
    /**
     * Tests a valid sort with: asc modifier, a valid sort parameter value, and valid search parameters.
     * @throws Exception
     */
    @Test
    public void testValidSortParmWithSearchParms() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
         FHIRSearchContext searchContext;
         Class<Observation> resourceType = Observation.class;
         SortDirection direction = SortDirection.ASCENDING; 
         String sortParmName = "patient"; 
         String searchParmName = "performer";
         String searchParmValue = "Practioner/1";
         StringBuilder queryString = new StringBuilder().append("&").append(searchParmName).append("=").append(searchParmValue)
                                     .append("&_sort:").append(direction.value()).append("=").append(sortParmName);
         
         
         queryParameters.put("_sort:" + direction.value(), Collections.singletonList(sortParmName));
         queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString.toString());
         
        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1,searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertTrue(sortParm.getQueryStringIndex() > 0);
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Parameter.Type.REFERENCE, sortParm.getType());
        
        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        Parameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getName());
        assertNotNull(searchParm.getValues());
        assertEquals(1, searchParm.getValues().size());
        ParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(searchParmValue, parmValue.getValueString());
    }
    
    /**
     * Tests a valid sort with multiple valid sort parameters and valid search parameters.
     * @throws Exception
     */
    @Test
    public void testMultipleValidSortParmsWithSearchParms() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
         FHIRSearchContext searchContext;
         Class<Observation> resourceType = Observation.class;
         SortDirection directionAsc = SortDirection.ASCENDING;
         SortDirection directionDesc = SortDirection.DESCENDING;
         String sortParmName1 = "patient";
         String sortParmName2 = "status";
         String sortParmName3 = "value-string";
         String sortParmName4 = "value-date";
         String sortParmName5 = "value-quantity";
         String searchParmName = "performer";
         String searchParmValue = "Practioner/1";
         StringBuilder queryString = new StringBuilder().append("&").append(searchParmName).append("=").append(searchParmValue)
                    .append("&_sort:").append(directionAsc.value()).append("=").append(sortParmName1) 
                    .append("&_sort:").append(directionAsc.value()).append("=").append(sortParmName2)
                    .append("&_sort:").append(directionDesc.value()).append("=").append(sortParmName3)
                    .append("&_sort:").append(directionDesc.value()).append("=").append(sortParmName4)
                    .append("&_sort").append("=").append(sortParmName5);
         
         queryParameters.put("_sort:" + directionAsc.value(), Arrays.asList(new String[] {sortParmName2, sortParmName1}));
         queryParameters.put("_sort:" + directionDesc.value(), Arrays.asList(new String[] {sortParmName4, sortParmName3}));
         queryParameters.put("_sort", Arrays.asList(new String[] {sortParmName5}));
         queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString.toString());
         
        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(5,searchContext.getSortParameters().size());
        
        SortParameter sortParm1 = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName1, sortParm1.getName());
        assertEquals(directionAsc, sortParm1.getDirection());
        assertTrue(sortParm1.getQueryStringIndex() > 0);
        assertEquals(Parameter.Type.REFERENCE, sortParm1.getType());
        
        SortParameter sortParm2 = searchContext.getSortParameters().get(1);
        assertEquals(sortParmName2, sortParm2.getName());
        assertEquals(directionAsc, sortParm2.getDirection());
        assertTrue(sortParm2.getQueryStringIndex() > 0);
        assertEquals(Parameter.Type.TOKEN, sortParm2.getType());
        
        SortParameter sortParm3 = searchContext.getSortParameters().get(2);
        assertEquals(sortParmName3, sortParm3.getName());
        assertEquals(directionDesc, sortParm3.getDirection());
        assertTrue(sortParm3.getQueryStringIndex() > 0);
        assertEquals(Parameter.Type.STRING, sortParm3.getType());
        
        SortParameter sortParm4 = searchContext.getSortParameters().get(3);
        assertEquals(sortParmName4, sortParm4.getName());
        assertEquals(directionDesc, sortParm4.getDirection());
        assertTrue(sortParm4.getQueryStringIndex() > 0);
        assertEquals(Parameter.Type.DATE, sortParm4.getType());
        
        SortParameter sortParm5 = searchContext.getSortParameters().get(4);
        assertEquals(sortParmName5, sortParm5.getName());
        assertEquals(directionAsc, sortParm5.getDirection());
        assertTrue(sortParm5.getQueryStringIndex() > 0);
        assertEquals(Parameter.Type.QUANTITY, sortParm5.getType());
        
        
        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        Parameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getName());
        assertNotNull(searchParm.getValues());
        assertEquals(1, searchParm.getValues().size());
        ParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(searchParmValue, parmValue.getValueString());
    }
    
}
