/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * Utility to do some extra testing related to the processing of search parameters
 */
public class SearchParamTest {
    protected static SearchHelper searchHelper = new SearchHelper();

    @Test
    public void testIdParamParsing() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        List<String> searchCriteria = new ArrayList<>();
        searchCriteria.add(Prefix.GE.value() + "2021-01-01");

        if (!searchCriteria.isEmpty()) {
            queryParameters.put(SearchConstants.LAST_UPDATED, searchCriteria);
        }
        queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

        // Results in two QueryParameter instances, each with a single QueryParameterValue
        queryParameters.put(SearchConstants.ID, Arrays.asList("patient1", "patient2"));


        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(searchContext);
        assertEquals(searchContext.getSearchParameters().size(), 3);
    }

    @Test
    public void testIdParamParsingSingle() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();

        List<String> searchCriteria = new ArrayList<>();
        searchCriteria.add(Prefix.GE.value() + "2021-01-01");

        if (!searchCriteria.isEmpty()) {
            queryParameters.put(SearchConstants.LAST_UPDATED, searchCriteria);
        }
        queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

        // Results in one QueryParameter instance with two QueryParameterValue instances
        queryParameters.put(SearchConstants.ID, Arrays.asList("patient1,patient2"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Patient.class, queryParameters);
        assertNotNull(searchContext);
        assertEquals(searchContext.getSearchParameters().size(), 2);
        QueryParameter idParam = searchContext.getSearchParameters().get(1);
        assertEquals(idParam.getCode(), SearchConstants.ID);
        assertEquals(idParam.getValues().size(), 2);
    }
}
