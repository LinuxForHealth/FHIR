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

import com.ibm.watsonhealth.fhir.model.Questionnaire;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryQuestionnaireTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Questionnaire.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateQuestionnaire() throws Exception {
    	Questionnaire questionnaire = readResource(Questionnaire.class, "Questionnaire.json");

        persistence.create(questionnaire);
        assertNotNull(questionnaire);
        assertNotNull(questionnaire.getId());
        assertNotNull(questionnaire.getId().getValue());
        assertNotNull(questionnaire.getMeta());
        assertNotNull(questionnaire.getMeta().getVersionId().getValue());
        assertEquals("1", questionnaire.getMeta().getVersionId().getValue());
    }   
	
	/**
	 * Tests a query for a Questionnaire with publisher = 'Team Voltron' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_publisher() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "publisher", "Team Voltron");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Questionnaire)resources.get(0)).getPublisher().getValue(),"Team Voltron");
	}
	
	/**
	 * Tests a query for a Questionnaire with version = '10.0' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_version() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "version", "10.0");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Questionnaire with title = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_title() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "title", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Questionnaire with date = '1969-12-31T19:00:02+00:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_date() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "date", "1969-12-31T19:00:02+00:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Questionnaire)resources.get(0)).getDate().getValue(),"1969-12-31T19:00:02+00:00");
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(enabled=true,groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnairePagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Questionnaire.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Questionnaire.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(resources.size(), count);
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Questionnaire with date = '1969-12-31T19:00:02+00:00' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(enabled=true, groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnairePagination_002() throws Exception {
		
		String parmName = "date";
		String parmValue = "1969-12-31T19:00:02+00:00";
		Class<? extends Resource> resourceType = Questionnaire.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Questionnaire.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Questionnaire)resources.get(0)).getDate().getValue(),"1969-12-31T19:00:02+00:00");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(resources.size(), context.getTotalCount());
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
}
