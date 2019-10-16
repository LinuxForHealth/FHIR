/*
 * (C) Copyright IBM Corp. 2018,2019
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
        String queryString = "&_summary=true";

        queryParameters.put("_summary", Arrays.asList("true"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNotNull(context.getSummaryParameter());
        assertEquals(context.getSummaryParameter(), SummaryValueSet.TRUE);
    }
    
    
    @Test
    public void testSummaryMultiple() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_summary=data,true";

        queryParameters.put("_summary", Arrays.asList("data","true"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNotNull(context.getSummaryParameter());
        assertEquals(context.getSummaryParameter(), SummaryValueSet.DATA);
    }
    
    @Test
    public void testSummaryInvalid() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_summary=invalid";

        queryParameters.put("_summary", Arrays.asList("invalid"));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(context);
        assertNull(context.getSummaryParameter());
    }
    
    
    @Test
    public void testSummaryInvalid_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_summary=invalid";
        boolean isSummaryValueCorrect = true;

        queryParameters.put("_summary", Arrays.asList("invalid"));
        try {
            SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString, false);
        } catch(Exception ex) {
            isSummaryValueCorrect = false;
        }
        assertTrue(!isSummaryValueCorrect);
    }

}
