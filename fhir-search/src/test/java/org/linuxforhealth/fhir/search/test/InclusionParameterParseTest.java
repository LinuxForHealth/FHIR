/*
 * (C) Copyright IBM Corp. 2018, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

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

import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Measure;
import org.linuxforhealth.fhir.model.resource.Medication;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.TotalValueSet;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.InclusionParameter;
import org.linuxforhealth.fhir.search.parameters.SortParameter;
import org.linuxforhealth.fhir.search.sort.Sort;
import org.linuxforhealth.fhir.search.util.SearchHelper;

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
        searchHelper.parseQueryParameters(resourceType, queryParameters);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test
    public void testIncludeWithSort() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:general-practitioner:Practitioner&_sort=birthdate";
        String sortParmCode = "birthdate";
        InclusionParameter incParm = new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, true, false);

        queryParameters.put("_sort", Collections.singletonList("birthdate"));
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        assertNotNull(searchContext.getSortParameters());
        assertEquals(searchContext.getSortParameters().size(), 1);
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParm.getCode(), sortParmCode);
        assertEquals(sortParm.getDirection(), Sort.Direction.INCREASING);
        assertEquals(sortParm.getType(), Type.DATE);
        assertTrue(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());
        assertEquals(searchContext.getIncludeParameters().size(), 1);
        assertEquals(searchContext.getIncludeParameters().get(0), incParm);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIncludeWithTotal() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:general-practitioner:Practitioner&_total=none";
        InclusionParameter incParm = new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, true, false);

        queryParameters.put("_total", Collections.singletonList("none"));
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertEquals(searchContext.getTotalParameter(), TotalValueSet.NONE);
        assertTrue(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());
        assertEquals(searchContext.getIncludeParameters().size(), 1);
        assertEquals(searchContext.getIncludeParameters().get(0), incParm);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidWithResourceSearchType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Resource:xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidJoinResourceTypeNonLenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("MedicationOrder:patient"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeUnknownParameterName_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:bogus"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidParameterType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:active"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIncludeValidSingleTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:organization";

        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIncludeCanonical() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Measure> resourceType = Measure.class;
        String queryString = "&_include=Measure:depends-on:Library";

        queryParameters.put("_include", Collections.singletonList("Measure:depends-on:Library"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Measure", incParm.getJoinResourceType());
        assertEquals("depends-on", incParm.getSearchParameter());
        assertEquals("Library", incParm.getSearchParameterTargetType());
        assertTrue(incParm.isCanonical());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Measure", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIncludeMissingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        String queryString = "&_include=Patient:general-practitioner";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", null, false, false));

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());

        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeInvalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider:Contract"));
        System.out.println(searchHelper.parseQueryParameters(resourceType, queryParameters, false, true));
    }

    @Test
    public void testIncludeValidTargetType() throws Exception {
        // Changed to general-practitioner

        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:general-practitioner:Practitioner";

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("general-practitioner", incParm.getSearchParameter());
        assertEquals("Practitioner", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIncludeRevIncludeSummaryText() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:link:Patient"));
        queryParameters.put("_summary", Collections.singletonList("text"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIncludeRevIncludeSummaryText_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_summary=text";

        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:link:Patient"));
        queryParameters.put("_summary", Collections.singletonList("text"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);

        assertFalse(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testRevIncludeWithSort() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:general-practitioner:Organization&_sort=name";
        String sortParmCode = "name";
        InclusionParameter revIncParm = new InclusionParameter("Patient", "general-practitioner", "Organization", null, true, false);

        queryParameters.put("_sort", Collections.singletonList("name"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        assertNotNull(searchContext.getSortParameters());
        assertEquals(searchContext.getSortParameters().size(), 1);
        SortParameter sortParm = searchContext.getSortParameters().get(0);
        assertEquals(sortParm.getCode(), sortParmCode);
        assertEquals(sortParm.getDirection(), Sort.Direction.INCREASING);
        assertEquals(sortParm.getType(), Type.STRING);
        assertFalse(searchContext.hasIncludeParameters());
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(searchContext.getRevIncludeParameters().size(), 1);
        assertEquals(searchContext.getRevIncludeParameters().get(0), revIncParm);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testRevIncludeWithTotal() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        String queryString = "&_revinclude=Patient:general-practitioner:Organization&_total=estimate";
        InclusionParameter revIncParm = new InclusionParameter("Patient", "general-practitioner", "Organization", null, true, false);

        queryParameters.put("_total", Collections.singletonList("estimate"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);

        assertEquals(searchContext.getTotalParameter(), TotalValueSet.ESTIMATE);
        assertFalse(searchContext.hasIncludeParameters());
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(searchContext.getRevIncludeParameters().size(), 1);
        assertEquals(searchContext.getRevIncludeParameters().get(0), revIncParm);

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidJoinResourceType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Invalid:general-practitioner"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeUnknownParameterName_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:bogus"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidParameterType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:active"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    @Test
    public void testRevIncludeValidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;

        String queryString = "&_revinclude=Patient:general-practitioner:Organization";

        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner:Organization"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("general-practitioner", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testRevIncludeCanonical() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Library> resourceType = Library.class;

        String queryString = "&_revinclude=Measure:depends-on";

        queryParameters.put("_revinclude", Collections.singletonList("Measure:depends-on"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Measure", revIncParm.getJoinResourceType());
        assertEquals("depends-on", revIncParm.getSearchParameter());
        assertEquals("Library", revIncParm.getSearchParameterTargetType());
        assertTrue(revIncParm.isCanonical());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Library", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testRevIncludeUnspecifiedTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:general-practitioner";

        queryParameters.put("_revinclude", Collections.singletonList("Patient:general-practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("general-practitioner", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Organization", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testRevIncludeInvalidRevIncludeSpecification() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude", Collections.singletonList("Patient:link"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
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
        expectedIncludeParms.add(new InclusionParameter("Medication", "manufacturer", "Organization", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Substance", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Medication", null, false, false));

        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("MedicationDispense", "medication", "Medication", null, false, false));
        expectedRevIncludeParms.add(new InclusionParameter("MedicationAdministration", "medication", "Medication", null, false, false));

        queryParameters.put("_include", Arrays.asList(new String[] { "Medication:manufacturer", "Medication:ingredient" }));
        queryParameters.put("_revinclude", Arrays.asList(new String[] { "MedicationDispense:medication", "MedicationAdministration:medication" }));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedRevIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter revIncludeParm : searchContext.getRevIncludeParameters()) {
            assertTrue(expectedRevIncludeParms.contains(revIncludeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Medication", searchContext);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertFalse(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.equals("http://example.com/Patient?_count=10&_page=1"));
    }

    @Test
    public void testWildcardIncludeSingleMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_include", Collections.singletonList("Patient:*:RelatedPerson"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("link", incParm.getSearchParameter());
        assertEquals("RelatedPerson", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson", null, true, false));

        queryParameters.put("_include", Collections.singletonList("Patient:*"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson", null, true, false));

        queryParameters.put("_include", Arrays.asList("Patient:*:Organization", "Patient:*:Practitioner", "Patient:*:RelatedPerson"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertFalse(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Condition", searchContext);
        assertTrue(selfUri.equals("http://example.com/Condition?_count=10&_page=1"));
    }

    @Test
    public void testWildcardRevIncludeSingleMatchingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Encounter> resourceType = Encounter.class;

        queryParameters.put("_revinclude", Collections.singletonList("MedicationAdministration:*:Encounter"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("MedicationAdministration", incParm.getJoinResourceType());
        assertEquals("context", incParm.getSearchParameter());
        assertEquals("Encounter", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Encounter", searchContext);
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
        expectedIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient", null, true, false));

        queryParameters.put("_revinclude", Collections.singletonList("Procedure:*"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        expectedIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Condition", "patient", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Condition", "asserter", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Condition", "evidence-detail", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Condition", "subject", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Encounter", "patient", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Encounter", "subject", "Patient", null, true, false));

        queryParameters.put("_revinclude", Arrays.asList("Procedure:*", "Condition:*", "Encounter:*"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "Patient", null, true, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson", null, true, false));

        String revinclude1 = "&_revinclude=Procedure:patient:Patient";
        String revinclude2 = "&_revinclude=Procedure:performer:Patient";
        String revinclude3 = "&_revinclude=Procedure:subject:Patient";

        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "patient", "Patient", null, true, false));
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "performer", "Patient", null, true, false));
        expectedRevIncludeParms.add(new InclusionParameter("Procedure", "subject", "Patient", null, true, false));

        queryParameters.put("_include", Collections.singletonList("Patient:*"));
        queryParameters.put("_revinclude", Collections.singletonList("Procedure:*"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

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

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
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

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIterateIncludeInvalidWithSort() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_sort", Collections.singletonList("birthDate"));
        queryParameters.put("_include:iterate", Collections.singletonList("Patient:link"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIterateIncludeInvalidModifier_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include:invalid=Patient:link";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include:invalid", Collections.singletonList("Patient:link"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIterateIncludeInvalidModifier_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include:invalid", Collections.singletonList("Patient:link"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIterateIncludeInvalidJoinResourceType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String validQueryString = "&_include=Patient:organization";
        String invalidQueryString = "&_include:iterate=MedicationOrder:patient";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_include:iterate", Collections.singletonList("MedicationOrder:patient"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIterateIncludeInvalidJoinResourceType_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_include:iterate", Collections.singletonList("MedicationOrder:patient"));
        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }

    @Test
    public void testIterateRevIncludeInvalidTargetType_lenient() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString = "&_revinclude=Patient:organization:Organization";
        String invalidQueryString = "&_revinclude:iterate=Patient:general-practitioner:Practitioner";

        // In lenient mode, the invalid parameter should be ignored
        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("Patient:organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter incParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testIterateRevIncludeInvalidTargetType_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;

        // In strict mode, the query should throw a FHIRSearchException
        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testIterateRevIncludeInvalidTargetTypeNotUserSpecified_strict() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String validQueryString1 = "&_revinclude=PractitionerRole:organization:Organization";
        String validQueryString2 = "&_revinclude:iterate=Patient:general-practitioner:Organization";
        String validQueryString3 = "&_revinclude:iterate=Patient:general-practitioner:PractitionerRole";
        String invalidQueryString = "&_revinclude:iterate=Patient:general-practitioner:Practitioner&";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("PractitionerRole", "organization", "Organization", null, false, false));

        // In strict mode, the invalid parameter should be ignored if not explicitly specified by the user
        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("PractitionerRole:organization"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter incParm : searchContext.getRevIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(incParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
        assertTrue(selfUri.contains(validQueryString3));
        assertFalse(selfUri.contains(invalidQueryString));
    }

    @Test
    public void testIterateIncludeValidSingleBaseTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include:iterate=Patient:organization";

        queryParameters.put("_include:iterate", Collections.singletonList("Patient:organization"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIterateIncludeValidSingleIncludeTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String validQueryString1 = "&_include:iterate=Organization:endpoint";
        String validQueryString2 = "&_include=Patient:organization";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Organization", "endpoint", "Endpoint", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization", null, false, false));

        queryParameters.put("_include:iterate", Collections.singletonList("Organization:endpoint"));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter incParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(incParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
    }

    @Test
    public void testIterateIncludeValidBaseAndIncludeTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String validQueryString1 = "&_include:iterate=Organization:endpoint:Endpoint";
        String validQueryString2 = "&_include=Patient:organization:Organization";
        String validQueryString3 = "&_include:iterate=Patient:link:Patient";
        String validQueryString4 = "&_include:iterate=Patient:link:RelatedPerson";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Organization", "endpoint", "Endpoint", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "organization", "Organization", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "Patient", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "link", "RelatedPerson", Modifier.ITERATE, false, false));

        queryParameters.put("_include:iterate", Arrays.asList(new String[] { "Organization:endpoint", "Patient:link" }));
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertFalse(searchContext.hasRevIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter incParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(incParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
        assertTrue(selfUri.contains(validQueryString3));
        assertTrue(selfUri.contains(validQueryString4));
    }

    @Test
    public void testIterateIncludeMissingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        String validQueryString1 = "&_include:iterate=Patient:general-practitioner:Organization&";
        String validQueryString2 = "&_include:iterate=Patient:general-practitioner:Practitioner&";
        String validQueryString3 = "&_include:iterate=Patient:general-practitioner:PractitionerRole&";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Organization", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", Modifier.ITERATE, false, false));

        queryParameters.put("_include:iterate", Collections.singletonList("Patient:general-practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
        assertTrue(selfUri.contains(validQueryString3));
    }

    @Test
    public void testIterateRevIncludeUnpsecifiedBaseTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude:iterate=Patient:general-practitioner:Organization";

        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("general-practitioner", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertTrue(revIncParm.isIterate());
        assertFalse(revIncParm.isUserSpecifiedTargetType());
        assertFalse(searchContext.hasIncludeParameters());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Organization", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testIterateRevIncludeUnpsecifiedBaseAndIncludeTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Practitioner> resourceType = Practitioner.class;
        String validQueryString1 = "&_revinclude=PractitionerRole:practitioner:Practitioner&";
        String validQueryString2 = "&_revinclude:iterate=Patient:general-practitioner:Practitioner&";
        String validQueryString3 = "&_revinclude:iterate=Patient:general-practitioner:PractitionerRole&";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("PractitionerRole", "practitioner", "Practitioner", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "PractitionerRole", Modifier.ITERATE, false, false));

        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("PractitionerRole:practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertFalse(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getRevIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Practitioner", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
        assertTrue(selfUri.contains(validQueryString3));
    }

    @Test
    public void testIterateRevIncludeValidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Practitioner> resourceType = Practitioner.class;
        String validQueryString1 = "&_revinclude=PractitionerRole:practitioner:Practitioner&";
        String validQueryString2 = "&_revinclude:iterate=Patient:general-practitioner:Practitioner&";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("PractitionerRole", "practitioner", "Practitioner", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Patient", "general-practitioner", "Practitioner", Modifier.ITERATE, true, false));

        queryParameters.put("_revinclude:iterate", Collections.singletonList("Patient:general-practitioner:Practitioner"));
        queryParameters.put("_revinclude", Collections.singletonList("PractitionerRole:practitioner"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertFalse(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getRevIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Practitioner", searchContext);
        assertTrue(selfUri.contains(validQueryString1));
        assertTrue(selfUri.contains(validQueryString2));
    }

    @Test
    public void testMultiIterateIncludeRevinclude() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Medication> resourceType = Medication.class;
        String include1 = "&_include=Medication:manufacturer:Organization";
        String include2 = "&_include=Medication:ingredient:Substance";
        String include3 = "&_include=Medication:ingredient:Medication";
        String include4 = "&_include:iterate=Organization:endpoint:Endpoint";
        String include5 = "&_include:iterate=MedicationDispense:destination:Location";

        String revinclude1 = "&_revinclude=MedicationDispense:medication:Medication";
        String revinclude2 = "&_revinclude=MedicationAdministration:medication:Medication";
        String revinclude3 = "&_revinclude:iterate=MedicationStatement:medication:Medication";
        String revinclude4 = "&_revinclude:iterate=AdverseEvent:substance:Medication";
        String revinclude5 = "&_revinclude:iterate=AdverseEvent:substance:Substance";
        String revinclude6 = "&_revinclude:iterate=AdverseEvent:substance:MedicationAdministration";

        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Medication", "manufacturer", "Organization", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Substance", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Medication", null, false, false));
        expectedIncludeParms.add(new InclusionParameter("Organization", "endpoint", "Endpoint", Modifier.ITERATE, false, false));
        expectedIncludeParms.add(new InclusionParameter("MedicationDispense", "destination", "Location", Modifier.ITERATE, false, false));

        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("MedicationDispense", "medication", "Medication", null, false, false));
        expectedRevIncludeParms.add(new InclusionParameter("MedicationAdministration", "medication", "Medication", null, false, false));
        expectedRevIncludeParms.add(new InclusionParameter("MedicationStatement", "medication", "Medication", Modifier.ITERATE, true, false));
        expectedRevIncludeParms.add(new InclusionParameter("AdverseEvent", "substance", "Medication", Modifier.ITERATE, false, false));
        expectedRevIncludeParms.add(new InclusionParameter("AdverseEvent", "substance", "Substance", Modifier.ITERATE, false, false));
        expectedRevIncludeParms.add(new InclusionParameter("AdverseEvent", "substance", "MedicationAdministration", Modifier.ITERATE, false, false));

        queryParameters.put("_include", Arrays.asList(new String[] { "Medication:manufacturer", "Medication:ingredient" }));
        queryParameters.put("_revinclude", Arrays.asList(new String[] { "MedicationDispense:medication", "MedicationAdministration:medication" }));
        queryParameters.put("_include:iterate", Arrays.asList(new String[] { "Organization:endpoint", "MedicationDispense:destination" }));
        queryParameters.put("_revinclude:iterate", Arrays.asList(new String[] { "MedicationStatement:medication", "AdverseEvent:substance" }));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);

        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }

        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedRevIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter revIncludeParm : searchContext.getRevIncludeParameters()) {
            assertTrue(expectedRevIncludeParms.contains(revIncludeParm));
        }

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Medication", searchContext);
        assertTrue(selfUri.contains(include1));
        assertTrue(selfUri.contains(include2));
        assertTrue(selfUri.contains(include3));
        assertTrue(selfUri.contains(include4));
        assertTrue(selfUri.contains(include5));

        assertTrue(selfUri.contains(revinclude1));
        assertTrue(selfUri.contains(revinclude2));
        assertTrue(selfUri.contains(revinclude3));
        assertTrue(selfUri.contains(revinclude4));
        assertTrue(selfUri.contains(revinclude5));
        assertTrue(selfUri.contains(revinclude6));
    }

}
