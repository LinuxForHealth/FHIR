/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This TestNG test class contains methods that test the parsing of search result inclusion parameters (_include and
 * _revinclude) in the SearchUtil class.
 *
 */
public class InclusionParameterParseTest extends BaseSearchTest {

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidSyntax() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("xxx"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIncludeInvalidSyntax_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include=xxx";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include", Collections.singletonList("xxx"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidWithSort() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_sort", Collections.singletonList("birthDate"));
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidWithTotal() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_total", Collections.singletonList("none"));
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidWithResourceSearchType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Resource:xxx"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIncludeInvalidJoinResourceTypeLenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include=MedicationOrder:patient";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include", Collections.singletonList("MedicationOrder:patient"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidJoinResourceTypeNonLenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("MedicationOrder:patient"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    @Test
    public void testIncludeUnknownParameterName_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include=Patient:bogus";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include", Collections.singletonList("Patient:bogus"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeUnknownParameterName_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:bogus"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    @Test
    public void testIncludeInvalidParameterType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include=Patient:active";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include", Collections.singletonList("Patient:active"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidParameterType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:active"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIncludeValidSingleTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:organization";

        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIncludeMissingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        String queryString = "&_include=Patient:general-practitioner";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole"));

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());

        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIncludeInvalidTargetType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include=Patient:careprovider:Contract";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider:Contract"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider:Contract"));
        System.out.println(SearchUtil.parseQueryParameters(resourceType, queryParameters, false));
    }

    @Test
    public void testIncludeValidTargetType() throws Exception {
        // Changed to general-practitioner

        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:general-practitioner:Practitioner";

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("general-practitioner", incParm.getSearchParameter());
        assertEquals("Practitioner", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevincludeInvalidWithTotal() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_total", Collections.singletonList("none"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testRevIncludeInvalidJoinResourceType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString = "&_revinclude=Patient:organization:Organization";
        String invalidQueryString = "&_revinclude=Invalid:general-practitioner";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_revinclude", Collections.singletonList("Invalid:general-practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidJoinResourceType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Invalid:general-practitioner"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    @Test
    public void testRevIncludeInvalidTargetType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString = "&_revinclude=Patient:organization:Organization";
        String invalidQueryString = "&_revinclude=Patient:general-practitioner:Practitioner";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testRevIncludeUnknownParameterName_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString = "&_revinclude=Patient:organization:Organization";
        String invalidQueryString = "&_revinclude=Patient:bogus";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_revinclude", Collections.singletonList("Patient:bogus"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeUnknownParameterName_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:bogus"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    @Test
    public void testRevIncludeInvalidParameterType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString = "&_revinclude=Patient:organization:Organization";
        String invalidQueryString = "&_revinclude=Patient:active";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_revinclude", Collections.singletonList("Patient:active"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidParameterType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:active"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
    }

    @Test
    public void testRevIncludeValidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;

        String queryString = "&_revinclude=Patient:general-practitioner:Organization";

        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Organization"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("general-practitioner", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testRevIncludeUnpsecifiedTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:general-practitioner";

        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("general-practitioner", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidRevIncludeSpecification() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:link"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testMultiIncludeRevinclude() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Medication> resourceType = Medication.class;
        String include1 = "&_include=Medication:manufacturer";
        String include2 = "&_include=Medication:ingredient";
        String include3 = "&_revinclude=MedicationDispense:medication";
        String include4 = "&_revinclude=MedicationAdministration:medication";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Medication", "manufacturer", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Substance"));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Medication"));

        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("MedicationDispense", "medication", "Medication"));
        expectedRevIncludeParms.add(new InclusionParameter("MedicationAdministration", "medication", "Medication"));

        queryParameters.put("_include", Arrays.asList(new String[] { "Medication:manufacturer", "Medication:ingredient" }));
        queryParameters.put("_revinclude", Arrays.asList(new String[] { "MedicationDispense:medication", "MedicationAdministration:medication" }));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertTrue(searchContext.hasRevIncludeParameters());

        for (InclusionParameter revIncludeParm : expectedRevIncludeParms) {
            assertTrue(expectedRevIncludeParms.contains(revIncludeParm));
        }

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));

        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
    }

    @Test
    public void testWildcardIncludeNoMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_include", Collections.singletonList("Patient:*:Medication"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertFalse(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.equals("http://example.com/Patient?_count=10&_page=1"));
    }

    @Test
    public void testWildcardIncludeSingleMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_include", Collections.singletonList("Patient:*:RelatedPerson"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("link", incParm.getSearchParameter());
        assertEquals("RelatedPerson", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.equals("http://example.com/Patient?_count=10&_include=" + incParm.getJoinResourceType() +
            ":" + incParm.getSearchParameter() + ":" + incParm.getSearchParameterTargetType() + "&_page=1"));
    }

    @Test
    public void testWildcardIncludeNoTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String include1 = "&_include=Patient:general-practitioner:Organization";
        String include2 = "&_include=Patient:general-practitioner:Practitioner";
        String include3 = "&_include=Patient:general-practitioner:PractitionerRole";
        String include4 = "&_include=Patient:organization:Organization";
        String include5 = "&_include=Patient:link:Patient";
        String include6 = "&_include=Patient:link:RelatedPerson";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson"));

        queryParameters.put("_include", Collections.singletonList("Patient:*"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
        assertTrue(selfUri.contains(include5));
        assertTrue(selfUri.contains(include6));
    }

    @Test
    public void testWildcardIncludeMultipleTargetTypes() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String include1 = "&_include=Patient:general-practitioner:Organization";
        String include2 = "&_include=Patient:general-practitioner:Practitioner";
        String include3 = "&_include=Patient:organization:Organization";
        String include4 = "&_include=Patient:link:RelatedPerson";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson"));

        queryParameters.put("_include", Arrays.asList("Patient:*:Organization", "Patient:*:Practitioner", "Patient:*:RelatedPerson"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
    }

    @Test
    public void testWildcardRevIncludeNoMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Condition> resourceType = Condition.class;

        queryParameters.put("_revinclude", Collections.singletonList("Patient:*:Condition"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertFalse(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Condition", searchContext);
        assertTrue(selfUri.equals("http://example.com/Condition?_count=10&_page=1"));
    }

    @Test
    public void testWildcardRevIncludeSingleMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Encounter> resourceType = Encounter.class;

        queryParameters.put("_revinclude", Collections.singletonList("MedicationAdministration:*:Encounter"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("MedicationAdministration", incParm.getJoinResourceType());
        assertEquals("context", incParm.getSearchParameter());
        assertEquals("Encounter", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Encounter", searchContext);
        assertTrue(selfUri.equals("http://example.com/Encounter?_count=10&_revinclude=" + incParm.getJoinResourceType() +
            ":" + incParm.getSearchParameter() + ":" + incParm.getSearchParameterTargetType() + "&_page=1"));
    }

    @Test
    public void testWildcardRevIncludeNoTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String include1 = "&_revinclude=Procedure:patient:Patient";
        String include2 = "&_revinclude=Procedure:performer:Patient";
        String include3 = "&_revinclude=Procedure:subject:Patient";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient"));

        queryParameters.put("_revinclude", Collections.singletonList("Procedure:*"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
    }

    @Test
    public void testWildcardMultipleRevIncludeNoTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String include1 = "&_revinclude=Procedure:patient:Patient";
        String include2 = "&_revinclude=Procedure:performer:Patient";
        String include3 = "&_revinclude=Procedure:subject:Patient";
        String include4 = "&_revinclude=Condition:patient:Patient";
        String include5 = "&_revinclude=Condition:asserter:Patient";
        String include6 = "&_revinclude=Condition:evidence-detail:Patient";
        String include7 = "&_revinclude=Condition:subject:Patient";
        String include8 = "&_revinclude=Encounter:patient:Patient";
        String include9 = "&_revinclude=Encounter:subject:Patient";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Condition", "patient", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Condition", "asserter", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Condition", "evidence-detail", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Condition", "subject", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Encounter", "patient", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Encounter", "subject", "Patient"));

        queryParameters.put("_revinclude", Arrays.asList("Procedure:*", "Condition:*", "Encounter:*"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
        assertTrue(selfUri.contains(include5));
        assertTrue(selfUri.contains(include6));
        assertTrue(selfUri.contains(include7));
        assertTrue(selfUri.contains(include8));
        assertTrue(selfUri.contains(include9));
    }

    @Test
    public void testWildcardIncludeAndRevIncludeNoTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String include1 = "&_include=Patient:general-practitioner:Organization";
        String include2 = "&_include=Patient:general-practitioner:Practitioner";
        String include3 = "&_include=Patient:general-practitioner:PractitionerRole";
        String include4 = "&_include=Patient:organization:Organization";
        String include5 = "&_include=Patient:link:Patient";
        String include6 = "&_include=Patient:link:RelatedPerson";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "Patient"));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson"));

        String revinclude1 = "&_revinclude=Procedure:patient:Patient";
        String revinclude2 = "&_revinclude=Procedure:performer:Patient";
        String revinclude3 = "&_revinclude=Procedure:subject:Patient";

        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient"));
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient"));
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient"));

        queryParameters.put("_include", Collections.singletonList("Patient:*"));
        queryParameters.put("_revinclude", Collections.singletonList("Procedure:*"));
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);

        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedRevIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : expectedRevIncludeParms) {
            assertTrue(expectedRevIncludeParms.contains(includeParm));
        }

        String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
        assertTrue(selfUri.contains(include5));
        assertTrue(selfUri.contains(include6));
        assertTrue(selfUri.contains(revinclude1));
        assertTrue(selfUri.contains(revinclude2));
        assertTrue(selfUri.contains(revinclude3));
    }

}
