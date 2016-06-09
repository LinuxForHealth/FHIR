/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.humanName;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

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
    public void testCreatePatient5() throws Exception {
        Patient patient = readResource(Patient.class, "patient-example-c.canonical.json");
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
		List<Resource> resources = runQueryTest(Patient.class, persistence, "family", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with given name = 'Clanton' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_given() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "given", "Clanton");
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertEquals(hnList.get(0).getGiven().get(0).getValue(),"Clanton");
	}

    /**
	 * Tests a query for a Patient with given name = 'Clantons' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_given_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "given", "Clantons");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with name = 'Clanton Mussenden' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_name() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clanton Mussenden");
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertEquals(hnList.get(0).getText().getValue(),"Clanton Mussenden");
	}

    /**
	 * Tests a query for a Patient with name = 'Clantons' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_name_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clantons");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_city() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-city", "Amsterdam");
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
	public void testPatient_city_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-city", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-country = 'NLD' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_country() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-country", "NLD");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertEquals(addrList.get(0).getCountry().getValue(),"NLD");
	}
	
	/**
	 * Tests a query for a Patient with address-country = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_country_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-country", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-state = 'UT' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_state() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-state", "UT");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertEquals(addrList.get(0).getState().getValue(),"UT");
	}
	
	/**
	 * Tests a query for a Patient with address-state = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_state_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-state", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with address-postalcode = '841131103' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_postalCode() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-postalcode", "841131103");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertEquals(addrList.get(0).getPostalCode().getValue(),"841131103");
	}
	
	/**
	 * Tests a query for a Patient with address-postalcode = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_postalCode_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address-postalcode", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	
	/**
	 * Tests a query for a Patient with gender = 'Male' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "non-cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_gender() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "gender", "Male");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getGender().getValue(),"Male");
	}*/
	
	/**
	 * Tests a query for a Patient with gender = 'Female' which should yield no results
	 * @throws Exception
	 */
	/*@Test(groups = { "non-cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_gender_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "gender", "Female");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}*/
	
	/**
	 * Tests a query for a Patient with phone = '8016626839' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "non-cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_phone() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "phone", "8016626839");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getTelecom().get(1).getValue().getValue(),"8016626839");
	}*/
	
	/**
	 * Tests a query for a Patient with phone = '80166268396' which should yield no results
	 * @throws Exception
	 */
	/*@Test(groups = { "non-cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient4" })
	public void testPatient_phone_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "phone", "80166268396");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}*/
	
	/**
	 * Tests a query for a Patient with link = 'Patient/pat2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa"}, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_link() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "link", "Patient/pat2");
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
		List<Resource> resources = runQueryTest(Patient.class, persistence, "organization", "Organization/1");
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
		List<Resource> resources = runQueryTest(Patient.class, persistence, "careprovider", "Organization/2");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Patient)resources.get(0)).getCareProvider().get(0).getReference().getValue(),"Organization/2");
	}
	
	/**
	 * Tests a query for Patients with birthdate = '1944-08-11' which should yield correct results
	 * @throws Exception
	 */
	
    @Test(enabled = true, groups = { "cloudant", "jpa"}, dependsOnMethods = {"testCreatePatient4"})
    public void testPatient_birthdate() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "birthdate", "eq1944-08-11");
        assertTrue(resources.size() > 0);
		assertEquals(((Patient)resources.get(0)).getBirthDate().getValue(),"1944-08-11");
    }
    
    /**
	 * Tests a query for Patients with birthdate = '1944-08-10' which should yield no results
	 * @throws Exception
	 */
    
    @Test(enabled = false, groups = { "cloudant", "jpa"}, dependsOnMethods = {"testCreatePatient4"})
    public void testPatient_birthdate_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "birthdate", "eq1944-08-11");
        assertTrue(resources.size() > 0);
		assertEquals(((Patient)resources.get(0)).getBirthDate().getValue(),"1944-08-11");
    }
    
    /**
	 * Tests a query for Patients with deathdate = '2015-02-14T13:42:00+10:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDate() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
	}
	
	/**
	 * Tests a query for Patients with deathdate = '2020-02-14T13:42:00+10:00' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDate_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2020-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Patient with family name = 'Doe' using :exact modifier which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_exactModifier() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "family:exact", "Doe");
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
		List<Resource> resources = runQueryTest(Patient.class, persistence, "family:not", "Doe");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<HumanName> hnList = ((Patient)resources.get(0)).getName();
		assertTrue(hnList.contains(humanName("John Doe-Smith-Jones")) == false);
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
	@Test(enabled = false, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" })
	public void testPatient_containsModifier() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "address:contains", "Amsterdam");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<Address> addrList = ((Patient)resources.get(0)).getAddress();
		assertTrue(addrList.get(0).getCity().getValue().equalsIgnoreCase("Amsterdam"));
	}
	
	/**
	 * Tests a query for Patients with birthdate = '1950-08-15' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient1" })
	public void testPatient_birthDate() throws Exception {
		
		String parmName = "birthdate";
		String parmValue = "1950-08-15";
		Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Patient.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue(((Patient)resources.get(0)).getBirthDate().getValue().equals("1950-08-15"));
	}*/
	
	/**
	 * Tests a query for Patients with deathdate = 'eq2015-02-14T13:42:00+10:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateEQ() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
	}
	
	/**
	 * Tests a query for Patients with deathdate = 'ne2015-02-14T13:42:00+10:00' which should yield no results
	 * @throws Exception
	 */
	@Test(enabled=false, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateNE() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "ne2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for Patients with deathdate = 'gt2015-02-14T13:42:00+10:00' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateGT() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "gt2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for Patients with deathdate = 'lt2015-02-14T13:42:00+10:00' which should yield no results
	 * @throws Exception
	 */
	@Test(enabled=false, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateLT() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "lt2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for Patients with deathdate = 'ge2015-02-14T13:42:00+10:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateGE() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "ge2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
	}
	
	/**
	 * Tests a query for Patients with deathdate = 'le2015-02-14T13:42:00+10:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient5" })
	public void testPatient_deathDateLE() throws Exception {
		List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "le2015-02-14T13:42:00+10:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
	}
}
