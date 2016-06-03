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

import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryLocationTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Location.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "location" })
    public void testCreateLocation1() throws Exception {
    	Location location = readResource(Location.class, "location-example.canonical.json");

        persistence.create(location);
        assertNotNull(location);
        assertNotNull(location.getId());
        assertNotNull(location.getId().getValue());
        assertNotNull(location.getMeta());
        assertNotNull(location.getMeta().getVersionId().getValue());
        assertEquals("1", location.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Location.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "location" })
    public void testCreateLocation2() throws Exception {
    	Location location = readResource(Location.class, "location-example-room.canonical.json");

        persistence.create(location);
        assertNotNull(location);
        assertNotNull(location.getId());
        assertNotNull(location.getId().getValue());
        assertNotNull(location.getMeta());
        assertNotNull(location.getMeta().getVersionId().getValue());
        assertEquals("1", location.getMeta().getVersionId().getValue());
    }
	
	/**
	 * Tests a query for a Location with name = 'South Wing, second floor' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "persistence", "search", "location", "stringParam" }, dependsOnMethods = { "testCreateLocation1" })
	public void testLocationQuery_001() throws Exception {
		
		String parmName = "name";
		String parmValue = "South Wing, second floor";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertTrue((parmValue.equals(((Location)resources.get(0)).getName().getValue())) || (parmValue.equals(((Location)resources.get(1)).getName().getValue())));
	}*/	
	
	/**
	 * Tests a query for a Location with address-country = 'USA' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "location", "stringParam" }, dependsOnMethods = { "testCreateLocation1" })
	public void testLocationQuery_002() throws Exception {
		
		String parmName = "address-country";
		String parmValue = "USA";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Location with address-city = 'Den Burg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "location", "stringParam" }, dependsOnMethods = { "testCreateLocation1" })
	public void testLocationQuery_003() throws Exception {
		
		String parmName = "address-city";
		String parmValue = "Den Burg";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Location)resources.get(0)).getAddress().getCity().getValue(),"Den Burg");
	}
	
	/**
	 * Tests a query for a Location with address = 'Den Burg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "location", "stringParam" }, dependsOnMethods = { "testCreateLocation1" })
	public void testLocationQuery_004() throws Exception {
		
		String parmName = "address";
		String parmValue = "Den Burg";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Location)resources.get(0)).getAddress().getCity().getValue(),"Den Burg");
		assertEquals(((Location)resources.get(0)).getAddress().getLine().get(0).getValue(),"Galapagosweg 91, Building A");
		assertEquals(((Location)resources.get(0)).getAddress().getPostalCode().getValue(),"9105 PZ");
		assertEquals(((Location)resources.get(0)).getAddress().getCountry().getValue(),"NLD");
	}
	
	/**
	 * Tests a query for a Location with organization = 'Organization/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "location", "referenceParam" }, dependsOnMethods = { "testCreateLocation1" })
	public void testLocationQuery_005() throws Exception {
		
		String parmName = "organization";
		String parmValue = "Organization/f001";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Location)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/f001");
	}
	
	/**
	 * Tests a query for a Location with partof = 'Location/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "location", "referenceParam" }, dependsOnMethods = { "testCreateLocation2" })
	public void testLocationQuery_006() throws Exception {
		
		String parmName = "partof";
		String parmValue = "Location/1";
		Class<? extends Resource> resourceType = Location.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Location.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Location)resources.get(0)).getPartOf().getReference().getValue(),"Location/1");
	}
}
