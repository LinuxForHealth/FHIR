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
    @Test(groups = { "cloudant", "jpa" })
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
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_noParams() throws Exception {
		List<Resource> resources = runQueryTest(Group.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_member1() throws Exception {
		List<Resource> resources = runQueryTest(Group.class, persistence, "member", "Patient/pat1");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(0).getEntity().getReference().getValue(),"Patient/pat1");
	}
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat4' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_member2() throws Exception {
		List<Resource> resources = runQueryTest(Group.class, persistence, "member", "Patient/pat4");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(3).getEntity().getReference().getValue(),"Patient/pat4");
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(enabled=true,groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat4' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(enabled=true, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupPagination_002() throws Exception {
		
		String parmName = "member";
		String parmValue = "Patient/pat4";
		Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(3).getEntity().getReference().getValue(),"Patient/pat4");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Group with member = 'Patint/pat4' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(enabled=true, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupPagination_003() throws Exception {
		
		String parmName = "member";
		String parmValue = "Patint/pat4";
		Class<? extends Resource> resourceType = Group.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Group.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count == 0) && (lastPgNum == 0));
	}
}
