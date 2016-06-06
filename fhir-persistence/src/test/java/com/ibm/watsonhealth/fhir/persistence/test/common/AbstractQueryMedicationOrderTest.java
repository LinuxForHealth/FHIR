/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.MedicationOrder;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMedicationOrderTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a MedicationOrder.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "medicationOrder" })
    public void testCreateMedicationOrder() throws Exception {
    	MedicationOrder medOrder = readResource(MedicationOrder.class, "medicationorderexample1.canonical.json");

    	persistence.create(medOrder);
        assertNotNull(medOrder);
        assertNotNull(medOrder.getId());
        assertNotNull(medOrder.getId().getValue());
        assertNotNull(medOrder.getMeta());
        assertNotNull(medOrder.getMeta().getVersionId().getValue());
        assertEquals("1", medOrder.getMeta().getVersionId().getValue());
    } 
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationOrder" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_001() throws Exception {
        Class<? extends Resource> resourceType = MedicationOrder.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);	
		List<Resource> resources = persistence.search(MedicationOrder.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a MedicationOrder with encounter = 'Encounter/f002' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationOrder", "referenceParam" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_002() throws Exception {
		
		String parmName = "encounter";
		String parmValue = "Encounter/f002";
		Class<? extends Resource> resourceType = MedicationOrder.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationOrder.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getEncounter().getReference().getValue(),"Encounter/f002");
	}
	
	/**
	 * Tests a query for a MedicationOrder with prescriber = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationOrder", "referenceParam" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_003() throws Exception {
		
		String parmName = "prescriber";
		String parmValue = "Practitioner/f007";
		Class<? extends Resource> resourceType = MedicationOrder.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationOrder.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getPrescriber().getReference().getValue(),"Practitioner/f007");
	}
	
	/**
	 * Tests a query for a MedicationOrder with patient = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationOrder", "referenceParam" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_004() throws Exception {
		
		String parmName = "patient";
		String parmValue = "Patient/f001";
		Class<? extends Resource> resourceType = MedicationOrder.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationOrder.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getPatient().getReference().getValue(),"Patient/f001");
	}
	
	/**
	 * Tests a query for a MedicationOrder with medication = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "medicationOrder", "referenceParam" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_005() throws Exception {
		
		String parmName = "medication";
		String parmValue = "Medication/MedicationExample2";
		Class<? extends Resource> resourceType = MedicationOrder.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(MedicationOrder.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/MedicationExample2");
	}
}
