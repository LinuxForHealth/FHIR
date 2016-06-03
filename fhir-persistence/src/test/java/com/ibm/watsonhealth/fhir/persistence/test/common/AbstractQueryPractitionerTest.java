/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
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
    @Test(groups = { "persistence", "create", "practitioner" })
    public void testCreatePractitioner1() throws Exception {
    	Practitioner practitioner = readResource(Practitioner.class, "practitioner-example.canonical.json");

        persistence.create(practitioner);
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
    @Test(groups = { "persistence", "create", "practitioner" })
    public void testCreatePractitioner2() throws Exception {
    	Practitioner practitioner = readResource(Practitioner.class, "practitioner-example-f001-evdb.canonical.json");

        persistence.create(practitioner);
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
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_001() throws Exception {
		
		String parmName = "given";
		String parmValue = "Adam";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
	}
	
	/**
	 * Tests a query for a Practitioner with name = 'Dr Adam Careful' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_002() throws Exception {
		
		String parmName = "name";
		String parmValue = "Dr Adam Careful";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
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
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_003() throws Exception {
		
		String parmName = "name";
		String parmValue = "Mr Adam Careful";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Practitioner with phonetic = 'Dr Adam Careful' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_004() throws Exception {
		
		String parmName = "phonetic";
		String parmValue = "Dr Adam Careful";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
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
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner2" })
	public void testPractitionerQuery_005() throws Exception {
		
		String parmName = "address-city";
		String parmValue = "Den Burg";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getAddress().get(0).getCity().getValue(),"Den Burg");
	}	
	
	/**
	 * Tests a query for a Practitioner with address = 'Den Burg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner2" })
	public void testPractitionerQuery_006() throws Exception {
		
		String parmName = "address";
		String parmValue = "Galapagosweg 91, Den Burg, 9105 PZ, NLD";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
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
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_007() throws Exception {
		
		String parmName = "name";
		String parmValue = "Careful";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
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
	@Test(groups = { "persistence", "search", "practitioner", "stringParam" }, dependsOnMethods = { "testCreatePractitioner1" })
	public void testPractitionerQuery_008() throws Exception {
		
		String parmName = "name";
		String parmValue = "Adam";
		Class<? extends Resource> resourceType = Practitioner.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Practitioner.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Practitioner)resources.get(0)).getName().getGiven().get(0).getValue(),"Adam");
		assertEquals(((Practitioner)resources.get(0)).getName().getFamily().get(0).getValue(),"Careful");
		assertEquals(((Practitioner)resources.get(0)).getName().getPrefix().get(0).getValue(),"Dr");
		assertEquals(((Practitioner)resources.get(0)).getName().getText().getValue(),"Dr Adam Careful");
	}
}
