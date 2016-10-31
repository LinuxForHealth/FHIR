/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of search result sorting related tests that will be run against
 *  each of the various persistence layer implementations that implement a subclass of this class.
 *   
 */
public abstract class AbstractQuerySortTest extends AbstractPersistenceTest {
	
		
    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
    	
        super.setUp();
        Patient patient;
        String patientFile;
        
        // Load 5 patients
        for (int i = 1; i <=5; i++) 
        {
        	patientFile = "sortTest.patient" + i + ".json";
        	patient = readResource(Patient.class, patientFile);
         	persistence.create(getDefaultPersistenceContext(), patient);
         	assertNotNull(patient);
         	assertNotNull(patient.getId());
            assertNotNull(patient.getId().getValue());
        }
        
    }
	     
    /**
     * Tests a single ascending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa" })
    public void testSingleSortParmAsc() throws Exception {
			
		Class<Patient> resourceType = Patient.class;
		FHIRSearchContext searchContext;
		FHIRPersistenceContext persistenceContext;
		Map<String, List<String>> queryParameters = new HashMap<>();
		String queryString;
		Patient patient;
		String currentFamilyName;
		String previousFamilyName;
			
		queryString = "&deceased=false&_sort=family";
		queryParameters.put("deceased", Collections.singletonList("false"));
		queryParameters.put("_sort", Arrays.asList(new String[] {"family"}));
				
		searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
		searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousFamilyName = null;
        for (Resource resource : resources) {
        	patient = (Patient) resource;
        	assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
        	currentFamilyName = patient.getName().get(0).getFamily().get(0).getValue();
        	assertNotNull(currentFamilyName);
        	if (previousFamilyName == null) {
        		previousFamilyName = currentFamilyName;
        	}
        	else {
        		assertTrue(currentFamilyName.compareTo(previousFamilyName) >= 0);
        	}
        }
	}
    
    /**
     * Tests a single descending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa" })
    public void testSingleSortParmDesc() throws Exception {
			
		Class<Patient> resourceType = Patient.class;
		FHIRSearchContext searchContext;
		FHIRPersistenceContext persistenceContext;
		Map<String, List<String>> queryParameters = new HashMap<>();
		String queryString;
		Patient patient;
		String currentFamilyName;
		String previousFamilyName;
			
		queryString = "&deceased=false&_sort:desc=family";
		queryParameters.put("deceased", Collections.singletonList("false"));
		queryParameters.put("_sort:desc", Arrays.asList(new String[] {"family"}));
				
		searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
		searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousFamilyName = null;
        for (Resource resource : resources) {
        	patient = (Patient) resource;
        	assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
        	currentFamilyName = patient.getName().get(0).getFamily().get(0).getValue();
        	assertNotNull(currentFamilyName);
        	if (previousFamilyName == null) {
        		previousFamilyName = currentFamilyName;
        	}
        	else {
        		assertTrue(currentFamilyName.compareTo(previousFamilyName) <= 0);
        	}
        }
	}
    
    /**
     * Tests an ascending sort parameter in combination with a descending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa" })
    public void testMultiSortParms() throws Exception {
			
		Class<Patient> resourceType = Patient.class;
		FHIRSearchContext searchContext;
		FHIRPersistenceContext persistenceContext;
		Map<String, List<String>> queryParameters = new HashMap<>();
		String queryString;
		Patient patient;
		String currentOrg;
		String previousOrg;
		String currentPhoneNumber;
		String previousPhoneNumber;
			
		queryString = "&deceased=false&_sort:asc=organization&_sort:desc=telecom";
		queryParameters.put("deceased", Collections.singletonList("false"));
		queryParameters.put("_sort:asc", Collections.singletonList("organization"));
		queryParameters.put("_sort:desc", Arrays.asList(new String[] {"telecom"}));
				
		searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
		searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousOrg = null;
        previousPhoneNumber = null;
        for (Resource resource : resources) {
        	patient = (Patient) resource;
        	assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
        	currentOrg = patient.getManagingOrganization().getReference().getValue();
        	assertNotNull(currentOrg);
        	if (previousOrg == null) {
        		previousOrg = currentOrg;
        	}
        	else {
        		assertTrue(currentOrg.compareTo(previousOrg) >= 0);
        	}
        	currentPhoneNumber = patient.getTelecom().get(0).getValue().getValue();
        	assertNotNull(currentPhoneNumber);
        	if (previousPhoneNumber == null) {
        		previousPhoneNumber = currentPhoneNumber;
        	}
        	else {
        		assertTrue(currentPhoneNumber.compareTo(previousPhoneNumber) <= 0);
        	}
        }
	}
}
