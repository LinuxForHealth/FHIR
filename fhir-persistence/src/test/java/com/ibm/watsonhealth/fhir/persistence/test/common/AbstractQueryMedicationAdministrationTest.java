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

import com.ibm.watsonhealth.fhir.model.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.MedicationOrder;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMedicationAdministrationTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a MedicationAdministration.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "medicationAdministration" })
    public void testCreateMedicationAdministration() throws Exception {
    	MedicationAdministration medAdmin = readResource(MedicationAdministration.class, "medicationadministrationexample1.canonical.json");

    	persistence.create(medAdmin);
        assertNotNull(medAdmin);
        assertNotNull(medAdmin.getId());
        assertNotNull(medAdmin.getId().getValue());
        assertNotNull(medAdmin.getMeta());
        assertNotNull(medAdmin.getMeta().getVersionId().getValue());
        assertEquals("1", medAdmin.getMeta().getVersionId().getValue());
    } 
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationAdministration" }, dependsOnMethods = { "testCreateMedicationAdministration" })
	public void testMedicationAdministrationQuery_001() throws Exception {
		
		List<Parameter> searchParms = new ArrayList<>();
				
		List<Resource> resources = persistence.search(MedicationAdministration.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a MedicationAdministration with patient = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationAdministration", "referenceParam" }, dependsOnMethods = { "testCreateMedicationAdministration" })
	public void testMedicationAdministrationQuery_002() throws Exception {
		
		String parmName = "patient";
		String parmValue = "Patient/example";
		Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationAdministration.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationAdministration)resources.get(0)).getPatient().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for a MedicationAdministration with prescription = 'MedicationOrder/medrx005' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationAdministration", "referenceParam" }, dependsOnMethods = { "testCreateMedicationAdministration" })
	public void testMedicationAdministrationQuery_003() throws Exception {
		
		String parmName = "prescription";
		String parmValue = "MedicationOrder/medrx005";
		Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationAdministration.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationAdministration)resources.get(0)).getPrescription().getReference().getValue(),"MedicationOrder/medrx005");
	}
	
	/**
	 * Tests a query for a MedicationAdministration with practitioner = 'Practitioner/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationAdministration", "referenceParam" }, dependsOnMethods = { "testCreateMedicationAdministration" })
	public void testMedicationAdministrationQuery_004() throws Exception {
		
		String parmName = "practitioner";
		String parmValue = "Practitioner/example";
		Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationAdministration.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationAdministration)resources.get(0)).getPractitioner().getReference().getValue(),"Practitioner/example");
	}
	
	/**
	 * Tests a query for a MedicationAdministration with medication = 'Medication/medicationexample6' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationAdministration", "referenceParam" }, dependsOnMethods = { "testCreateMedicationAdministration" })
	public void testMedicationAdministrationQuery_005() throws Exception {
		
		String parmName = "medication";
		String parmValue = "Medication/medicationexample6";
		Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationAdministration.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationAdministration)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/medicationexample6");
	}
}
