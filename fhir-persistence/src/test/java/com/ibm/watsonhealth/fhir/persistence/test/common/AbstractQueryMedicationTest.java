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

import com.ibm.watsonhealth.fhir.model.Medication;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMedicationTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Medication.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateMedication() throws Exception {
    	 
    	Medication medication = readResource(Medication.class, "medicationexample4.canonical.json");

    	persistence.create(medication);
        assertNotNull(medication);
        assertNotNull(medication.getId());
        assertNotNull(medication.getId().getValue());
        assertNotNull(medication.getMeta());
        assertNotNull(medication.getMeta().getVersionId().getValue());
        assertEquals("1", medication.getMeta().getVersionId().getValue());
    	 
    }    
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationQuery_noParams() throws Exception {
		List<Resource> resources = runQueryTest(Medication.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Medication with manufacturer = 'http://www.a-smeds.com/fhirresource/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationQuery_manufacturer() throws Exception {
		List<Resource> resources = runQueryTest(Medication.class, persistence, "manufacturer", "http://www.a-smeds.com/fhirresource/1");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Medication)resources.get(0)).getManufacturer().getReference().getValue(),"http://www.a-smeds.com/fhirresource/1");
	}
	
	/**
	 * Tests a query for a Medication with manufacturer = 'http://www.a-smeds.com/fhirresource1' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationQuery_manufacturer_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Medication.class, persistence, "manufacturer", "http://www.a-smeds.com/fhirresource1");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Medication with content = 'MedicationExample14' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationQuery_content() throws Exception {
		List<Resource> resources = runQueryTest(Medication.class, persistence, "content", "MedicationExample14");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Medication)resources.get(0)).getPackage().getContent().get(0).getItem().getReference().getValue(),"MedicationExample14");
	}	
		
	/**
	 * Tests a query for a Medication with ingredient = 'Amoxicillin' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationQuery_ingredient() throws Exception {
		List<Resource> resources = runQueryTest(Medication.class, persistence, "ingredient", "Amoxicillin");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Medication)resources.get(0)).getProduct().getIngredient().get(0).getItem().getReference().getValue(),"Amoxicillin");
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Medication.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Medication.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Medication with ingredient = 'Amoxicillin' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationPagination_002() throws Exception {
		
		String parmName = "ingredient";
		String parmValue = "Amoxicillin";
		Class<? extends Resource> resourceType = Medication.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Medication.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Medication)resources.get(0)).getProduct().getIngredient().get(0).getItem().getReference().getValue(),"Amoxicillin");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Medication with ingredient = 'XXX' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateMedication" })
	public void testMedicationPagination_003() throws Exception {
		
		String parmName = "ingredient";
		String parmValue = "XXX";
		Class<? extends Resource> resourceType = Medication.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Medication.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
		int lastPgNum = context.getLastPageNumber();
		assertTrue((count == 0) && (lastPgNum == Integer.MAX_VALUE));
	}
}
