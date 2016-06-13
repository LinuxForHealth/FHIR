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

import com.ibm.watsonhealth.fhir.model.MedicationAdministration;
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
    @Test(groups = { "cloudant", "jpa" })
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
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_noParams() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a MedicationOrder with encounter = 'Encounter/f002' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_encounter() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "encounter", "Encounter/f002");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getEncounter().getReference().getValue(),"Encounter/f002");
	}
	
	/**
	 * Tests a query for a MedicationOrder with prescriber = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_prescriber() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "prescriber", "Practitioner/f007");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getPrescriber().getReference().getValue(),"Practitioner/f007");
	}
	
	/**
	 * Tests a query for a MedicationOrder with patient = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_patient() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "patient", "Patient/f001");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getPatient().getReference().getValue(),"Patient/f001");
	}
	
	/**
	 * Tests a query for a MedicationOrder with medication = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_medication() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "medication", "Medication/MedicationExample2");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/MedicationExample2");
	}
	
	/**
	 * Tests a query for a MedicationOrder with datewritten = '2015-01-15' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_datewritten() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "datewritten", "2015-01-15");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((MedicationOrder)resources.get(0)).getDateWritten().getValue(),"2015-01-15");
	}
	
	/**
	 * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedicationOrder" })
	public void testMedicationOrderQuery_datewritten_noResults() throws Exception {
		List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "datewritten", "2025-01-15");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
}
