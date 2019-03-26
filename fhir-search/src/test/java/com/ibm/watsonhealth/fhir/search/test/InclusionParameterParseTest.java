/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.watsonhealth.fhir.model.Medication;
import com.ibm.watsonhealth.fhir.model.Organization;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.search.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This JUNIT test class contains methods that test the parsing of search result inclusion parameters (_include and _revinclude)
 * in the SearchUtil class. 
 * @author markd
 *
 */
public class InclusionParameterParseTest {
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_invalidSyntax() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=xxx";
        
        queryParameters.put("_include", Collections.singletonList("xxx"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_invalidWithSort() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=xxx&_sort=yyy";
        
        queryParameters.put("_include", Collections.singletonList("xxx"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_invalidJoinResourceType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=MedicationOrder:patient";
        
        queryParameters.put("_include", Collections.singletonList("MedicationOrder:patient"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_unknownParameterName() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:bogus";
        
        queryParameters.put("_include", Collections.singletonList("Patient:bogus"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_invalidParameterType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:active";
        
        queryParameters.put("_include", Collections.singletonList("Patient:active"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test
    public void testInclude_validSingleTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:organization";
        
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("organization", incParm.getSearchParameter());
        assertEquals("Organization", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());
        
    }
    
    @Test
    public void testInclude_missingTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        String queryString = "&_include=Patient:careprovider";
        
         List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
         expectedIncludeParms.add(new InclusionParameter("Patient", "careprovider", "Organization"));
         expectedIncludeParms.add(new InclusionParameter("Patient", "careprovider", "Practitioner"));
        
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testInclude_invalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:careprovider:Contract";
        
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider:Contract"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test
    public void testInclude_validTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&_include=Patient:careprovider:Practitioner";
        
        queryParameters.put("_include", Collections.singletonList("Patient:careprovider:Practitioner"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(1, searchContext.getIncludeParameters().size());
        InclusionParameter incParm = searchContext.getIncludeParameters().get(0);
        assertEquals("Patient", incParm.getJoinResourceType());
        assertEquals("careprovider", incParm.getSearchParameter());
        assertEquals("Practitioner", incParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasRevIncludeParameters());
    }
    
    
    @Test(expected = FHIRSearchException.class)
    public void testRevInclude_invalidTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:careprovider:Contract";
        
        queryParameters.put("_revinclude", Collections.singletonList("Patient:careprovider:Contract"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
    @Test
    public void testRevInclude_validTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:careprovider:Organization";
        
        queryParameters.put("_revinclude", Collections.singletonList("Patient:careprovider:Organization"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("careprovider", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());
    }
    
    @Test
    public void testRevInclude_unpsecifiedTargetType() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:careprovider";
        
        queryParameters.put("_revinclude", Collections.singletonList("Patient:careprovider"));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        assertNotNull(searchContext);
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(1, searchContext.getRevIncludeParameters().size());
        InclusionParameter revIncParm = searchContext.getRevIncludeParameters().get(0);
        assertEquals("Patient", revIncParm.getJoinResourceType());
        assertEquals("careprovider", revIncParm.getSearchParameter());
        assertEquals("Organization", revIncParm.getSearchParameterTargetType());
        assertFalse(searchContext.hasIncludeParameters());
    }
    
    @Test(expected = FHIRSearchException.class)
    public void testRevInclude_invalidRevIncludeSpecification() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Organization> resourceType = Organization.class;
        String queryString = "&_revinclude=Patient:link";
        
        queryParameters.put("_revinclude", Collections.singletonList("Patient:link"));
        SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
    }
    
        
    @Test
    public void testMulti_include_revinclude()     throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Medication> resourceType = Medication.class;
        String queryString = "&_include=Medication:manufacturer&include=Medication:ingredient" +
                             "_revinclude=MedicationOrder:medication&_revinclude=MedicationAdministration:medication";
        
        List<InclusionParameter> expectedIncludeParms = new ArrayList<>();
        expectedIncludeParms.add(new InclusionParameter("Medication", "manufacturer", "Organization"));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Substance"));
        expectedIncludeParms.add(new InclusionParameter("Medication", "ingredient", "Medication"));
        
        List<InclusionParameter> expectedRevIncludeParms = new ArrayList<>();
        expectedRevIncludeParms.add(new InclusionParameter("MedicationOrder", "medication", "Medication"));
        expectedRevIncludeParms.add(new InclusionParameter("MedicationAdministration", "medication", "Medication"));
        
        queryParameters.put("_include", Arrays.asList(new String[] {"Medication:manufacturer", "Medication:ingredient" }));
        queryParameters.put("_revinclude", Arrays.asList(new String[] {"MedicationOrder:medication", "MedicationAdministration:medication"}));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        
        assertNotNull(searchContext);
        assertTrue(searchContext.hasIncludeParameters());
        assertEquals(expectedIncludeParms.size(), searchContext.getIncludeParameters().size());
        for (InclusionParameter includeParm : expectedIncludeParms) {
            assertTrue(expectedIncludeParms.contains(includeParm));
        }
        
        assertTrue(searchContext.hasRevIncludeParameters());
        assertEquals(expectedRevIncludeParms.size(), searchContext.getRevIncludeParameters().size());
        for (InclusionParameter revIncludeParm : expectedRevIncludeParms) {
            assertTrue(expectedRevIncludeParms.contains(revIncludeParm));
        }
    }
    
}
