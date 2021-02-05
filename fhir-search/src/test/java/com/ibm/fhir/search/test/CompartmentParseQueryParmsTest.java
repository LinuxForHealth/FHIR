/*
 * (C) Copyright IBM Corp. 2016,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CommunicationRequest;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This TestNG test class contains methods that test the parsing of compartment related search data in the SearchUtil
 * class.
 */
public class CompartmentParseQueryParmsTest extends BaseSearchTest {

    /**
     * This method tests parsing compartment related query parms, passing an invalid compartment.
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalidComparmentName() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        SearchUtil.parseQueryParameters("bogusCompartmentName", "1", Observation.class, queryParameters, null);
    }

    /**
     * This method tests parsing compartment related query parms, passing an invalid resource type for the compartment.
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalidResource() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        SearchUtil.parseQueryParameters("Patient", "1", Device.class, queryParameters, null);
    }

    /**
     * This method tests parsing compartment related query parms. Based on the compartment and resource type, a single
     * inclusion criterion is expectedExceptions to be returned by SearchUtil.parseQueryParameters().
     */
    @Test
    public void testSingleInclusionCriteria() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "11";
        Class<? extends Resource> resourceType = Condition.class;
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, null);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());
        QueryParameter parm1 = context.getSearchParameters().get(0);
        assertEquals("patient", parm1.getCode());

        /*
         * The compartment > Resource is { "code" : "Condition", "param" : ["patient", "asserter"] },
         */
        QueryParameter p = parm1;
        String out = "";
        while (p != null) {
            out += (" -> " + p.getCode());
            p = p.getNextParameter();
        }
        if (DEBUG) {
            System.out.println(out);
        }
        assertNotNull(parm1.getNextParameter());
        assertEquals(" -> patient -> asserter", out);

        assertEquals(Type.REFERENCE, parm1.getType());
        assertEquals(1, parm1.getValues().size());
        assertEquals(compartmentName + "/" + compartmentLogicalId, parm1.getValues().get(0).getValueString());
    }

    /**
     * This method tests parsing compartment related query parms. Based on the compartment and resource type, multiple
     * inclusion criteria is expectedExceptions to be returned by SearchUtil.parseQueryParameters().
     */
    @Test
    public void testMultiInclusionCriteria() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "RelatedPerson";
        String compartmentLogicalId = "22";
        Class<? extends Resource> resourceType = CommunicationRequest.class;
        FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, null);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());

        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getCode().equals("recipient") || searchParm.getCode().equals("requester") || searchParm.getCode().equals("sender")));
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
     * Based on the compartment and resource type, multiple inclusion criteria is expectedExceptions to be returned by
     * SearchUtil.parseQueryParameters().
     */
    @Test
    public void testCompartmentWithQueryParms() throws Exception {
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
        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getCode().equals("performer") || searchParm.getCode().equals("subject")));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(2, parmCount);

        // Validate non-compartment related search parms.
        for (int i = 1; i < 3; i++) {
            searchParm = context.getSearchParameters().get(i);
            assertTrue((searchParm.getCode().equals("category") || searchParm.getCode().equals("value-quantity")));
            assertNotNull(searchParm.getValues());
            assertEquals(1, searchParm.getValues().size());
        }

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/"
                + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains(queryStringPart1), selfUri + " does not contain expectedExceptions " + queryStringPart1);
        assertTrue(selfUri.contains(queryStringPart2), selfUri + " does not contain expectedExceptions " + queryStringPart2);
    }

    /**
     * This method tests parsing compartment related query parms which are not valid. In lenient mode, this is
     * expectedExceptions to ignore the query parameter. In strict mode (lenient=false) this should throw an exception.
     */
    @Test
    public void testCompartmentWithFakeQueryParm() throws Exception {
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
        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue((searchParm.getCode().equals("performer") || searchParm.getCode().equals("subject")));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(2, parmCount);

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/"
                + resourceType.getSimpleName(), context);
        assertFalse(selfUri.contains(queryString), selfUri + " contain unexpectedExceptions query parameter 'fakeParameter'");

        try {
            SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParameters, false);
            fail("expectedExceptions parseQueryParameters to throw due to strict mode but it didn't.");
        } catch (Exception e) {
            assertTrue(e instanceof FHIRSearchException);
        }
    }

    /**
     * This method tests parsing null compartment related query parms together with non-compartment related query parms.
     * SearchUtil.parseQueryParameters() should ignore the null compartment related parms and successfully process the
     * non-compartment parms.
     */
    @Test
    public void testNoComparmentWithQueryParms() throws Exception {
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
        for (QueryParameter searchParm : context.getSearchParameters()) {
            assertTrue((searchParm.getCode().equals("category") || searchParm.getCode().equals("value-quantity")));
            assertNotNull(searchParm.getValues());
            assertEquals(1, searchParm.getValues().size());
            assertNull(searchParm.getNextParameter());
        }

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
        assertTrue(selfUri.contains(queryStringPart1), selfUri + " does not contain expectedExceptions " + queryStringPart1);
        assertTrue(selfUri.contains(queryStringPart2), selfUri + " does not contain expectedExceptions " + queryStringPart2);
    }

    private String encodeQueryString(String queryString) {
        try {
            URI uri = new URI("http", "dummy", "/path", queryString, null);
            return uri.getRawQuery();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("We should never get here", e);
        }
    }

}
