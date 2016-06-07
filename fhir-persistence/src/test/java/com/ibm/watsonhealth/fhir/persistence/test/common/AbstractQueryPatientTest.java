/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.humanName;

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
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
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
    @Test(groups = { "cloudant", "jpa" })
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
    @Test(groups = { "cloudant", "jpa" })
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
    @Test(groups = { "cloudant", "jpa" })
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
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreatePatient4() throws Exception {
        Patient patient = readResource(Patient.class, "Patient1.json");
        persistence.create(patient);
        assertNotNull(patient);
    } 
    
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_noparams() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, null, null);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Patient with family name = 'Doe' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_family() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "family", "Doe");
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
	}

    /**
	 * Tests a query for a Patient with family name = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_family_noResults() throws Exception {
		
		String parmName = "family";
		String parmValue = "Non-existent";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_004() throws Exception {
		String parmName = "address-city";
		String parmValue = "Amsterdam";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
	}
	
	/**
	 * Tests a query for a Patient with address-city = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_005() throws Exception {
		String parmName = "address-city";
		String parmValue = "Non-existent";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with link = 'Patient/pat2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_link() throws Exception {
		String parmName = "link";
		String parmValue = "Patient/pat2";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<PatientLink> linkList = ((Patient)resources.get(0)).getLink();
		assertEquals(linkList.get(0).getOther().getReference().getValue(),"Patient/pat2");
	}
	
	/**
	 * Tests a query for a Patient with organization = 'Organization/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient2" })
	public void testPatient_organization() throws Exception {
		String parmName = "organization";
		String parmValue = "Organization/1";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/1");
	}
	
	/**
	 * Tests a query for a Patient with careprovider = 'Organization/2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient3" })
	public void testPatient_careProvider() throws Exception {
		String parmName = "careprovider";
		String parmValue = "Organization/2";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getCareProvider().get(0).getReference().getValue(),"Organization/2");
	}
	
	/**
	 * Tests a query for a Patient with family name = 'Doe' using :exact modifier which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_exactModifier() throws Exception {
		String parmName = "family:exact";
		String parmValue = "Doe";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
	}
	
	/**
	 * Tests a query for Patients with family name != 'Doe' using :not modifier which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_notModifier() throws Exception {
		
		String parmName = "family:not";
		String parmValue = "Doe";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertTrue(hnList.contains(humanName("John Doe-Smith-Jones")) == false);
	}
	
    @Test(enabled = false, groups = { "cloudant", "jpa"}, dependsOnMethods = {"testCreatePatient4"})
    public void testPatient_birthdate() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "birthdate", "eq1944-08-11");
        assertTrue(resources.size() > 0);
    }
    
	/**
	 * Tests a query for Patients with address field missing using :missing modifier which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" })
	public void testPatientQuery_0011() throws Exception {
		
		String parmName = "address:missing";
		String parmValue = "false";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		System.out.println("Size = " + resources.size());
		assertTrue(resources.size() != 0);
	}*/
	
	/**
	 * Tests a query for Patients with address field containing partial matches using :contains modifier which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" })
	public void testPatient_containsModifier() throws Exception {
		
		String parmName = "address:contains";
		String parmValue = "Amsterdam";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertTrue(addrList.get(0).getCity().getValue().equalsIgnoreCase("Amsterdam"));
	}
}
