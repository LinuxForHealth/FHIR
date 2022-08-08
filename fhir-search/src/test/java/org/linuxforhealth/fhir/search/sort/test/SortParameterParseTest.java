/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.sort.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;
import org.linuxforhealth.fhir.search.parameters.SortParameter;
import org.linuxforhealth.fhir.search.sort.Sort;
import org.linuxforhealth.fhir.search.test.BaseSearchTest;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * This unit test class contains methods that test the parsing of sort
 * parameters in the SearchUtil class.
 */
public class SortParameterParseTest extends BaseSearchTest {

    /**
     * Tests an direction modifier on the _sort query parameter value.
     */
    @Test(expectedExceptions = {})
    public void testValidDescendingDirection() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_sort", Collections.singletonList("-birthdate"));

        FHIRSearchContext _ctx = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(_ctx);
        assertEquals(_ctx.getSortParameters().get(0).getDirection().value(), '-');
    }

    /**
     * Tests an direction modifier on the _sort query parameter value.
     */
    @Test(expectedExceptions = {})
    public void testValidAscendingDirection() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_sort", Collections.singletonList("birthdate"));

        FHIRSearchContext _ctx = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(_ctx);
        assertEquals(_ctx.getSortParameters().get(0).getDirection().value(), '+');
    }

    /**
     * Tests an direction modifier on the _sort query parameter value which is
     * invalid.
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalidAscendingDirection() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_sort", Collections.singletonList("birthdateXX"));

        FHIRSearchContext _ctx = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(_ctx);
    }

    /**
     * Tests an invalid sort parameter value.
     */
    @Test
    public void testUnknownSortParm_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_sort=bogusSortParm";

        // In lenient mode, invalid search parameters should be ignored
        queryParameters.put("_sort", Collections.singletonList("bogusSortParm"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.getSortParameters() == null || searchContext.getSortParameters().isEmpty());

        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertFalse(selfUri.contains(queryString), selfUri + " contain unexpected " + queryString);
        assertEquals(2, searchContext.getOutcomeIssues().size());
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testUnknownSortParm_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode (lenient=false), the search should throw a FHIRSearchException
        queryParameters.put("_sort", Collections.singletonList("bogusSortParm"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    /**
     * Tests a valid sort with: asc modifier, and a valid parameter value.
     */
    @Test
    public void testValidSortParm() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String sortParmCode = "birthdate";
        String queryString = "&_sort=" + sortParmCode;

        queryParameters.put("_sort", Collections.singletonList(sortParmCode));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        // Do sort parameter validation
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmCode, sortParm.getCode());
        assertEquals(sortParm.getDirection().value(), '+');
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryString), selfUri + " does not contain expected " + queryString);
    }

    /**
     * Tests a valid sort with: desc modifier, and a valid parameter value.
     */
    @Test
    public void testValidSortParm1() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        Sort.Direction direction = Sort.Direction.DECREASING;
        String sortParmCode = "birthdate";
        String queryString = "&_sort=" + direction.value() + sortParmCode;

        queryParameters.put("_sort", Collections.singletonList(direction.value() + sortParmCode));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmCode, sortParm.getCode());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryString), selfUri + " does not contain expected " + queryString);
    }

    /**
     * Tests a valid sort with no modifier, and a valid parameter value.
     */
    @Test
    public void testValidSortParm2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String sortParmCode = "birthdate";

        queryParameters.put("_sort", Collections.singletonList(sortParmCode));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmCode, sortParm.getCode());
        assertEquals(Sort.Direction.INCREASING, sortParm.getDirection());
        assertEquals(Type.DATE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertTrue(searchContext.getSearchParameters().isEmpty());

        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        // The server adds the implicit sort direction and so we just look for the parameter instead of the full
        // queryString
        assertTrue(selfUri.contains(sortParmCode), selfUri + " does not contain expected sort parameter 'birthdate'");
    }

    /**
     * Tests a valid sort with: asc modifier, a valid sort parameter value, and
     * valid search parameters.
     */
    @Test
    public void testValidSortParmWithSearchParms() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        FHIRRequestContext.set(context);
        String originalRequestUri = "https://example.com/Patient/123";
        context.setOriginalRequestUri(originalRequestUri);

        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Observation> resourceType = Observation.class;
        Sort.Direction direction = Sort.Direction.INCREASING;
        String sortParmCode = "patient";
        String searchParmName = "performer";
        String searchParmValue = "Practitioner/1";
        String queryStringPart1 = "&" + searchParmName + "=" + searchParmValue;
        String queryStringPart2 = "&_sort=" + sortParmCode;

        queryParameters.put("_sort", Collections.singletonList(sortParmCode));
        queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        // Do sort parameter validation
        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(1, searchContext.getSortParameters().size());
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParmCode, sortParm.getCode());
        assertEquals(direction, sortParm.getDirection());
        assertEquals(Type.REFERENCE, sortParm.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        QueryParameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getCode());
        assertNotNull(searchParm.getValues());
        assertEquals(searchParm.getValues().size(), 1);
        QueryParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(parmValue.getValueString(), searchParmValue);

        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains(queryStringPart1), selfUri + " does not contain expected " + queryStringPart1);
        assertTrue(selfUri.contains(queryStringPart2), selfUri + " does not contain expected " + queryStringPart2);
    }

    /**
     * Tests a valid sort with multiple valid sort parameters and valid search
     * parameters.
     */
    @Test
    public void testMultipleValidSortParmsWithSearchParms() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        FHIRRequestContext.set(context);
        String originalRequestUri = "https://example.com/Patient/123";
        context.setOriginalRequestUri(originalRequestUri);

        Map<String, List<String>> queryParameters = new LinkedHashMap<>();
        FHIRSearchContext searchContext;
        Class<Observation> resourceType = Observation.class;

        Sort.Direction directionDesc = Sort.Direction.DECREASING;
        String sortParmCode1 = "patient";
        String sortParmCode2 = "status";
        String sortParmCode3 = "value-string";
        String sortParmCode4 = "value-date";
        String sortParmCode5 = "value-quantity";
        String searchParmName = "performer";
        String searchParmValue = "Practioner/1";

        // Build up the QueryParameters
        queryParameters.put("_sort", Collections.singletonList(sortParmCode1 + "," + sortParmCode2 + "," +
                directionDesc.value() + sortParmCode3 + "," + directionDesc.value() + sortParmCode4 + "," + sortParmCode5));
        queryParameters.put(searchParmName, Collections.singletonList(searchParmValue));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertNotNull(searchContext.getSortParameters());
        assertEquals(searchContext.getSortParameters().size(), 5);

        SortParameter sortParm1 = searchContext.getSortParameters().get(0);
        assertEquals(sortParmCode1, sortParm1.getCode());
        assertEquals('+', sortParm1.getDirection().value());
        assertEquals(Type.REFERENCE, sortParm1.getType());

        SortParameter sortParm2 = searchContext.getSortParameters().get(1);
        assertEquals(sortParmCode2, sortParm2.getCode());
        assertEquals('+', sortParm2.getDirection().value());
        assertEquals(Type.TOKEN, sortParm2.getType());

        SortParameter sortParm3 = searchContext.getSortParameters().get(2);
        assertEquals(sortParmCode3, sortParm3.getCode());
        assertEquals(directionDesc, sortParm3.getDirection());
        assertEquals(Type.STRING, sortParm3.getType());

        SortParameter sortParm4 = searchContext.getSortParameters().get(3);
        assertEquals(sortParmCode4, sortParm4.getCode());
        assertEquals(directionDesc, sortParm4.getDirection());
        assertEquals(Type.DATE, sortParm4.getType());

        SortParameter sortParm5 = searchContext.getSortParameters().get(4);
        assertEquals(sortParmCode5, sortParm5.getCode());
        assertEquals('+', sortParm5.getDirection().value());
        assertEquals(Type.QUANTITY, sortParm5.getType());

        // Do search parameter validation
        assertNotNull(searchContext.getSearchParameters());
        assertEquals(1, searchContext.getSearchParameters().size());
        QueryParameter searchParm = searchContext.getSearchParameters().get(0);
        assertEquals(searchParmName, searchParm.getCode());
        assertNotNull(searchParm.getValues());
        assertEquals(searchParm.getValues().size(), 1);

        // since #1929 we do not expand search params to include incoming uri
        QueryParameterValue parmValue = searchParm.getValues().get(0);
        assertEquals(parmValue.getValueString(), searchParmValue);

        // Check the component parts and build up the QueryString parts
        String selfUri =
                SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), searchContext);
        assertTrue(selfUri.contains("status"), selfUri + " does not contain expected status");
        assertTrue(selfUri.contains("-value-date"), selfUri + " does not contain expected -value-date ");
        assertTrue(selfUri.contains("-value-string"), selfUri + " does not contain expected -value-string");
        assertTrue(selfUri.contains("value-quantity"), selfUri + " does not contain expected value-quantity");

        // The server adds the implicit sort direction and so we just look for the parameter instead of the full
        // queryString
        assertTrue(selfUri.contains("patient"), selfUri + " does not contain expected sort parameter 'patient'");
    }
}