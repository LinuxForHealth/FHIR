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

import com.ibm.watsonhealth.fhir.model.Group;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryGroupTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Group.
     * 
     * @throws Exception
     */
    @Test(groups = { "persistence", "create", "group" })
    public void testCreateGroup() throws Exception {
    	Group group = readResource(Group.class, "group-example-member.canonical.json");

    	persistence.create(group);
        assertNotNull(group);
        assertNotNull(group.getId());
        assertNotNull(group.getId().getValue());
        assertNotNull(group.getMeta());
        assertNotNull(group.getMeta().getVersionId().getValue());
        assertEquals("1", group.getMeta().getVersionId().getValue());
    } 
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "group" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_001() throws Exception {
        Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "group", "referenceParam" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_002() throws Exception {
		
		String parmName = "member";
		String parmValue = "Patient/pat1";
		Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(0).getEntity().getReference().getValue(),"Patient/pat1");
	}
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat4' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "group", "referenceParam" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_003() throws Exception {
		
		String parmName = "member";
		String parmValue = "Patient/pat4";
		Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(3).getEntity().getReference().getValue(),"Patient/pat4");
	}
}
