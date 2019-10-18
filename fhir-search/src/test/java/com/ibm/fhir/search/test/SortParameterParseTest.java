/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

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

import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.SearchConstants.SortDirection;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;
import com.ibm.fhir.search.parameters.SortParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * 
 * This UNIT test class contains methods that test the parsing of sort parameters in the SearchUtil class.
 * 
 * @author markd
 * @author pbastide
 *
 */
public class SortParameterParseTest extends BaseSearchTest {

    /**
     * Tests an invalid direction modifier on the _sort query parameter.
     * 
     * @throws Exception
     */
    @Test(expectedExceptions = FHIRSearchException.class)
    public void testInvalidDirection() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_sort:xxx", Collections.singletonList("birthdate"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    /**
     * Tests an invalid sort parameter value.
     * 
     * @throws Exception
     */
    @Test
    public void testUnknownSortParm() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_sort=bogusSortParm";

        // In lenient mode, invalid search parameters should be ignored
        queryParameters.put("_sort", Collections.singletonList("bogusSortParm"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.getSortParameters() == null || searchContext.getSortParameters().isEmpty());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertFalse(selfUri.contains(queryString), selfUri + " contain unexpected " + queryString);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testUnknownSortParm_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode (lenient=false), the search should throw a FHIRSearchException
        queryParameters.put("_sort", Collections.singletonList("bogusSortParm"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    /**
     * Tests a valid sort with: asc modifier, and a valid parameter value.
     * 
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
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        // Do sort parameter validation
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryString), selfUri + " does not contain expected " + queryString);
    }

    /**
     * Tests a valid sort with: desc modifier, and a valid parameter value.
     * 
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
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryString), selfUri + " does not contain expected " + queryString);
    }

    /**
     * Tests a valid sort with no modifier, and a valid parameter value.
     * 
     * @throws Exception
     */
    @Test
    public void testValidSortParm2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String sortParmName = "birthdate";

        queryParameters.put("_sort", Collections.singletonList(sortParmName));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(SortDirection.ASCENDING, sortParm.getDirection());
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        // The server adds the implicit sort direction and so we just look for the parameter instead of the full
        // queryString
        assertTrue(selfUri.contains(sortParmName), selfUri + " does not contain expected sort parameter 'birthdate'");
    }

    /**
     * Tests a valid sort with: asc modifier, a valid sort parameter value, and valid search parameters.
     * 
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
        String searchParmValue = "Practitioner/1";
        String queryStringPart1 = "&" + searchParmName + "=" + searchParmValue;
        String queryStringPart2 = "&_sort:" + direction.value() + "=" + sortParmName;

        queryParameters.put("_sort:" + direction.value(), Collections.singletonList(sortParmName));
        queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName, sortParm.getName());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Type.REFERENCE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        Parameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getName());
        assertNotNull(searchParm.getValues());
        assertEquals(1, searchParm.getValues().size());
        ParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(searchParmValue, parmValue.getValueString());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryStringPart1), selfUri + " does not contain expected " + queryStringPart1);
        assertTrue(selfUri.contains(queryStringPart2), selfUri + " does not contain expected " + queryStringPart2);
    }

    /**
     * Tests a valid sort with multiple valid sort parameters and valid search parameters.
     * 
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
        String queryStringPart1 = "&_sort:" + directionAsc.value() + "=" + sortParmName1;
        String queryStringPart2 = "&_sort:" + directionAsc.value() + "=" + sortParmName2;
        String queryStringPart3 = "&_sort:" + directionDesc.value() + "=" + sortParmName3;
        String queryStringPart4 = "&_sort:" + directionDesc.value() + "=" + sortParmName4;
        String queryStringPart5 = "&_sort" + "=" + sortParmName5;
        String queryStringPart6 = "&" + searchParmName + "=" + searchParmValue;

        queryParameters.put("_sort:" + directionAsc.value(), Arrays.asList(new String[] { sortParmName1, sortParmName2 }));
        queryParameters.put("_sort:" + directionDesc.value(), Arrays.asList(new String[] { sortParmName3, sortParmName4 }));
        queryParameters.put("_sort", Arrays.asList(new String[] { sortParmName5 }));
        queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(5, searchContext.getSortParameters().size());

        SortParameter sortParm1 = searchContext.getSortParameters().get(0);
        assertEquals(sortParmName1, sortParm1.getName());
        assertEquals(directionAsc, sortParm1.getDirection());
        assertEquals(Type.REFERENCE, sortParm1.getType());

        SortParameter sortParm2 = searchContext.getSortParameters().get(1);
        assertEquals(sortParmName2, sortParm2.getName());
        assertEquals(directionAsc, sortParm2.getDirection());
        assertEquals(Type.TOKEN, sortParm2.getType());

        SortParameter sortParm3 = searchContext.getSortParameters().get(2);
        assertEquals(sortParmName3, sortParm3.getName());
        assertEquals(directionDesc, sortParm3.getDirection());
        assertEquals(Type.STRING, sortParm3.getType());

        SortParameter sortParm4 = searchContext.getSortParameters().get(3);
        assertEquals(sortParmName4, sortParm4.getName());
        assertEquals(directionDesc, sortParm4.getDirection());
        assertEquals(Type.DATE, sortParm4.getType());

        SortParameter sortParm5 = searchContext.getSortParameters().get(4);
        assertEquals(sortParmName5, sortParm5.getName());
        assertEquals(directionAsc, sortParm5.getDirection());
        assertEquals(Type.QUANTITY, sortParm5.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        Parameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getName());
        assertNotNull(searchParm.getValues());
        assertEquals(1, searchParm.getValues().size());
        ParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(searchParmValue, parmValue.getValueString());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryStringPart1), selfUri + " does not contain expected " + queryStringPart1);
        assertTrue(selfUri.contains(queryStringPart2), selfUri + " does not contain expected " + queryStringPart2);
        assertTrue(selfUri.contains(queryStringPart3), selfUri + " does not contain expected " + queryStringPart3);
        assertTrue(selfUri.contains(queryStringPart4), selfUri + " does not contain expected " + queryStringPart4);
        // The server adds the implicit sort direction and so we just look for the parameter instead of the full
        // queryString
        assertTrue(selfUri.contains(sortParmName5), selfUri + " does not contain expected sort parameter '" + sortParmName5 + "'");
        assertTrue(selfUri.contains(queryStringPart6), selfUri + " does not contain expected " + queryStringPart6);
    }

}
