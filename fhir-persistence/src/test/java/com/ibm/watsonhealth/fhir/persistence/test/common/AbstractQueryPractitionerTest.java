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

import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryPractitionerTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Practitioner.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreatePractitioner1() throws Exception {
    	Practitioner practitioner = readResource(Practitioner.class, "practitioner-example.canonical.json");

        persistence.create(getDefaultPersistenceContext(), practitioner);
        assertNotNull(practitioner);
        assertNotNull(practitioner.getId());
        assertNotNull(practitioner.getId().getValue());
        assertNotNull(practitioner.getMeta());
        assertNotNull(practitioner.getMeta().getVersionId().getValue());
        assertEquals("1", practitioner.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Practitioner.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreatePractitioner2() throws Exception {
    	Practitioner practitioner = readResource(Practitioner.class, "practitioner-example-f001-evdb.canonical.json");

        persistence.create(getDefaultPersistenceContext(), practitioner);
        assertNotNull(practitioner);
        assertNotNull(practitioner.getId());
        assertNotNull(practitioner.getId().getValue());
        assertNotNull(practitioner.getMeta());
        assertNotNull(practitioner.getMeta().getVersionId().getValue());
        assertEquals("1", practitioner.getMeta().getVersionId().getValue());
    }
	
	/**
	 * Tests a query for a Practitioner with given = 'Adam' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_given() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "given", "Adam");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Dr Adam Careful' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_name() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "name", "Dr Adam Careful");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
		assertEquals(((Practitioner)resources.get(0)).getName().getFamily().get(0).getValue(),"Careful");
		assertEquals(((Practitioner)resources.get(0)).getName().getPrefix().get(0).getValue(),"Dr");
		assertEquals(((Practitioner)resources.get(0)).getName().getText().getValue(),"Dr Adam Careful");
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Mr Adam Careful' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_name_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "name", "Mr Adam Careful");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Practitioner with phonetic = 'Dr Adam Careful' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_phonetic() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "phonetic", "Dr Adam Careful");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
		assertEquals(((Practitioner)resources.get(0)).getName().getFamily().get(0).getValue(),"Careful");
		assertEquals(((Practitioner)resources.get(0)).getName().getPrefix().get(0).getValue(),"Dr");
		assertEquals(((Practitioner)resources.get(0)).getName().getText().getValue(),"Dr Adam Careful");
	}
	
	/**
	 * Tests a query for a Practitioner with address-city = 'Den Burg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner2" })
	public void testPractitionerQuery_addressCity() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "address-city", "Den Burg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCity().getValue(),"Den Burg");
	}	
	
	/**
	 * Tests a query for a Practitioner with address = 'Den Burg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner2" })
	public void testPractitionerQuery_address() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "address-city", "Den Burg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCity().getValue(),"Den Burg");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getLine().get(0).getValue(),"Galapagosweg 91");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getPostalCode().getValue(),"9105 PZ");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCountry().getValue(),"NLD");
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Careful' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_name_last() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "name", "Dr Adam Careful");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
		assertEquals(((Practitioner)resources.get(0)).getName().getFamily().get(0).getValue(),"Careful");
		assertEquals(((Practitioner)resources.get(0)).getName().getPrefix().get(0).getValue(),"Dr");
		assertEquals(((Practitioner)resources.get(0)).getName().getText().getValue(),"Dr Adam Careful");
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Adam' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_name_first() throws Exception {
		List<Resource> resources = runQueryTest(Practitioner.class, persistence, "name", "Dr Adam");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
		assertEquals(((Practitioner)resources.get(0)).getName().getFamily().get(0).getValue(),"Careful");
		assertEquals(((Practitioner)resources.get(0)).getName().getPrefix().get(0).getValue(),"Dr");
		assertEquals(((Practitioner)resources.get(0)).getName().getText().getValue(),"Dr Adam Careful");
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1", "testCreatePractitioner2" })
	public void testPractitionerPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Practitioner.class);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Practitioner with address = 'Den Burg' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner2" })
	public void testPractitionerPagination_002() throws Exception {
		
		String parmName = "address-city";
		String parmValue = "Den Burg";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Practitioner.class);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCity().getValue(),"Den Burg");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getLine().get(0).getValue(),"Galapagosweg 91");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getPostalCode().getValue(),"9105 PZ");
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCountry().getValue(),"NLD");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Mr Adam Careful' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerPagination_003() throws Exception {
		
		String parmName = "name";
		String parmValue = "Mr Adam Careful";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Practitioner.class);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
//		int lastPgNum = context.getLastPageNumber();
		assertTrue((count == 0)/* && (lastPgNum == Integer.MAX_VALUE)*/);
	}
}
