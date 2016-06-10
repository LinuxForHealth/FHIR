/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Group;
import com.ibm.watsonhealth.fhir.model.Resource;

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
	public void testGroupQuery_001() throws Exception {
		List<Resource> resources = runQueryTest(Group.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Group with member = 'Patient/pat1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateGroup" })
	public void testGroupQuery_002() throws Exception {
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
	public void testGroupQuery_003() throws Exception {
		List<Resource> resources = runQueryTest(Group.class, persistence, "member", "Patient/pat4");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Group)resources.get(0)).getMember().get(3).getEntity().getReference().getValue(),"Patient/pat4");
	}
}
