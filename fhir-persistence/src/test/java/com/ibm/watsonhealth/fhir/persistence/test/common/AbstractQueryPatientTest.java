/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Address;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.PatientLink;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryPatientTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "patient" })
    public void testCreatePatient1() throws Exception {
   		Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

    	persistence.create(patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "patient" })
    public void testCreatePatient2() throws Exception {
   		Patient patient = readResource(Patient.class, "patient-example.canonical.json");

    	persistence.create(patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "patient" })
    public void testCreatePatient3() throws Exception {
   		Patient patient = readResource(Patient.class, "patient-glossy-example.canonical.json");

    	persistence.create(patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    } 
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_001() throws Exception {
		
		List<Parameter> searchParms = new ArrayList<>();
				
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Patient with family name = 'Doe' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "stringParam" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_002() throws Exception {
		
		String parmName = "family";
		String parmValue = "Doe";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
	}
	
	/**
	 * Tests a query for a Patient with family name = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "stringParam" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_003() throws Exception {
		
		String parmName = "family";
		String parmValue = "Non-existent";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "stringParam" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_004() throws Exception {
		
		String parmName = "address-city";
		String parmValue = "Amsterdam";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
	}
	
	/**
	 * Tests a query for a Patient with address-city = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "stringParam" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_005() throws Exception {
		
		String parmName = "address-city";
		String parmValue = "Non-existent";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with link = 'Patient/pat2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "referenceParam" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatientQuery_006() throws Exception {
		
		String parmName = "link";
		String parmValue = "Patient/pat2";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<PatientLink> linkList = ((Patient)resources.get(0)).getLink();
		assertEquals(linkList.get(0).getOther().getReference().getValue(),"Patient/pat2");
	}
	
	/**
	 * Tests a query for a Patient with organization = 'Organization/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "referenceParam" }, dependsOnMethods = { "testCreatePatient2" })
	public void testPatientQuery_007() throws Exception {
		
		String parmName = "organization";
		String parmValue = "Organization/1";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/1");
	}
	
	/**
	 * Tests a query for a Patient with careprovider = 'Organization/2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "patient", "referenceParam" }, dependsOnMethods = { "testCreatePatient3" })
	public void testPatientQuery_008() throws Exception {
		
		String parmName = "careprovider";
		String parmValue = "Organization/2";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getCareProvider().get(0).getReference().getValue(),"Organization/2");
	}	
}
