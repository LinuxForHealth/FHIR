/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

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

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.model.resource.CommunicationRequest;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Device;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * Test the parsing of compartment related search data from SearchHelper.
 */
public class CompartmentParseQueryParmsTest extends BaseSearchTest {
    private static final String INTERNAL_PATIENT_COMPARTMENT_PARAM = "internal-Patient-Compartment";
    private static final String INTERNAL_RELATEDPERSON_COMPARTMENT_PARAM = "internal-RelatedPerson-Compartment";

    /**
     * This method tests parsing compartment related query parms, passing an invalid compartment.
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalidComparmentName() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        searchHelper.parseCompartmentQueryParameters("bogusCompartmentName", "1", Observation.class, queryParameters);
    }

    /**
     * This method tests parsing compartment related query parms, passing an invalid resource type for the compartment.
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testInvalidResource() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        searchHelper.parseCompartmentQueryParameters("Patient", "1", Device.class, queryParameters);
    }

    /**
     * This method tests parsing compartment related query parms.
     * Based on the compartment and resource type, two inclusion criteria are expected.
     */
    @Test
    public void testTwoInclusionCriteria() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "11";
        Class<? extends Resource> resourceType = Condition.class;
        FHIRSearchContext context = searchHelper.parseCompartmentQueryParameters(compartmentName,
                compartmentLogicalId, resourceType, queryParameters);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());
        QueryParameter parm1 = context.getSearchParameters().get(0);
        assertEquals(INTERNAL_PATIENT_COMPARTMENT_PARAM, parm1.getCode());

        assertEquals(Type.REFERENCE, parm1.getType());
        assertEquals(1, parm1.getValues().size());
        assertEquals(compartmentName + "/" + compartmentLogicalId, parm1.getValues().get(0).getValueString());
    }

    /**
     * This method tests parsing compartment related query parms.
     * Based on the compartment and resource type, three inclusion criteria are expected.
     */
    @Test
    public void testThreeInclusionCriteria() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "RelatedPerson";
        String compartmentLogicalId = "22";
        Class<? extends Resource> resourceType = CommunicationRequest.class;
        FHIRSearchContext context = searchHelper.parseCompartmentQueryParameters(compartmentName,
                compartmentLogicalId, resourceType, queryParameters);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());

        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue(searchParm.getCode().equals(INTERNAL_RELATEDPERSON_COMPARTMENT_PARAM));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(parmCount, 1);
    }

    /**
     * This method tests parsing compartment related query parms together with non-compartment related query parms..
     * Based on the compartment and resource type, multiple inclusion criteria is expectedExceptions to be returned by
     * searchHelper.parseQueryParameters().
     */
    @Test
    public void testCompartmentWithQueryParms() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "33";
        Class<? extends Resource> resourceType = Observation.class;

        String queryStringPart1 = "category=vital-signs";
        String queryStringPart2 = "value-quantity=" + encodeQueryString("eq185|http://unitsofmeasure.org|[lb_av]");

        queryParameters.put("category", Collections.singletonList("vital-signs"));
        queryParameters.put("value-quantity", Collections.singletonList("eq185|http://unitsofmeasure.org|[lb_av]"));
        FHIRSearchContext context = searchHelper.parseCompartmentQueryParameters(compartmentName,
                compartmentLogicalId, resourceType, queryParameters);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(context.getSearchParameters().size(), 3);

        // Validate compartment related search parms.
        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue(searchParm.getCode().equals(INTERNAL_PATIENT_COMPARTMENT_PARAM));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(parmCount, 1);

        // Validate non-compartment related search parms.
        for (int i = 1; i < 3; i++) {
            searchParm = context.getSearchParameters().get(i);
            assertTrue((searchParm.getCode().equals("category") || searchParm.getCode().equals("value-quantity")));
            assertNotNull(searchParm.getValues());
            assertEquals(searchParm.getValues().size(), 1);
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/"
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
        FHIRRequestContext.get().setTenantId("default");
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "33";
        Class<? extends Resource> resourceType = Observation.class;

        String queryString = "fakeParameter=fakeValue";
        queryParameters.put("fakeParameter", Collections.singletonList("fakeValue"));
        FHIRSearchContext context = searchHelper.parseCompartmentQueryParameters(compartmentName,
                compartmentLogicalId, resourceType, queryParameters);

        assertNotNull(context);
        assertNotNull(context.getSearchParameters());
        assertEquals(1, context.getSearchParameters().size());

        // Validate compartment related search parms.
        QueryParameter searchParm = context.getSearchParameters().get(0);
        int parmCount = 0;
        while (searchParm != null) {
            parmCount++;
            assertTrue(searchParm.getCode().equals(INTERNAL_PATIENT_COMPARTMENT_PARAM));
            assertEquals(Type.REFERENCE, searchParm.getType());
            assertTrue(searchParm.isInclusionCriteria());
            assertFalse(searchParm.isChained());
            assertEquals(1, searchParm.getValues().size());
            assertEquals(compartmentName + "/" + compartmentLogicalId, searchParm.getValues().get(0).getValueString());
            searchParm = searchParm.getNextParameter();
        }
        assertEquals(1, parmCount);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + compartmentName + "/" + compartmentLogicalId + "/"
                + resourceType.getSimpleName(), context);
        assertFalse(selfUri.contains(queryString), selfUri + " contain unexpectedExceptions query parameter 'fakeParameter'");

        try {
            searchHelper.parseCompartmentQueryParameters(compartmentName, compartmentLogicalId,
                    resourceType, queryParameters, false, true, FHIRVersionParam.VERSION_43);
            fail("expectedExceptions parseQueryParameters to throw due to strict mode but it didn't.");
        } catch (Exception e) {
            assertTrue(e instanceof FHIRSearchException);
        }
    }

    /**
     * This method tests parsing null compartment related query parms together with non-compartment related query parms.
     * searchHelper.parseQueryParameters() should ignore the null compartment related parms and successfully process the
     * non-compartment parms.
     */
    @Test
    public void testNoComparmentWithQueryParms() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<? extends Resource> resourceType = Observation.class;

        String queryStringPart1 = "category=vital-signs";
        String queryStringPart2 = "value-quantity=" + encodeQueryString("eq185|http://unitsofmeasure.org|[lb_av]");

        queryParameters.put("category", Collections.singletonList("vital-signs"));
        queryParameters.put("value-quantity", Collections.singletonList("eq185|http://unitsofmeasure.org|[lb_av]"));
        FHIRSearchContext context = searchHelper.parseCompartmentQueryParameters(null, null, resourceType, queryParameters);

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

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/" + resourceType.getSimpleName(), context);
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
