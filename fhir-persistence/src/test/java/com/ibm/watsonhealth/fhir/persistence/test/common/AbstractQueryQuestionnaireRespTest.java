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

import com.ibm.watsonhealth.fhir.model.QuestionnaireResponse;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryQuestionnaireRespTest extends AbstractPersistenceTest {
        
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a QuestionnaireResponse.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateQuestionnaireResponse1() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse.json");

        persistence.create(questionnaireResp);
        assertNotNull(questionnaireResp);
        assertNotNull(questionnaireResp.getId());
        assertNotNull(questionnaireResp.getId().getValue());
        assertNotNull(questionnaireResp.getMeta());
        assertNotNull(questionnaireResp.getMeta().getVersionId().getValue());
        assertEquals("1", questionnaireResp.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a QuestionnaireResponse.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateQuestionnaireResponse2() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "questionnaireresponse-example-gcs.canonical.json");

        persistence.create(questionnaireResp);
        assertNotNull(questionnaireResp);
        assertNotNull(questionnaireResp.getId());
        assertNotNull(questionnaireResp.getId().getValue());
        assertNotNull(questionnaireResp.getMeta());
        assertNotNull(questionnaireResp.getMeta().getVersionId().getValue());
        assertEquals("1", questionnaireResp.getMeta().getVersionId().getValue());
    }
    
    /**
	 * Tests a query for a QuestionnaireResponse with author = 'Patient/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_author() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "author", "Patient/1");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getAuthor().getReference().getValue(),"Patient/1");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with questionnaire = 'Questionnaire/1661690' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_questionnaire() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "questionnaire", "Questionnaire/1661690");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getQuestionnaire().getReference().getValue(),"Questionnaire/1661690");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with subject = 'Patient/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_subject() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "subject", "Patient/1");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSubject().getReference().getValue(),"Patient/1");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponseQuery_source() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "source", "Practitioner/f007");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSource().getReference().getValue(),"Practitioner/f007");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with patient = 'Patient/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_patient() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "patient", "Patient/1");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSubject().getReference().getValue(),"Patient/1");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2015-11-25T18:30:50+01:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_authored() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "authored", "2015-11-25T18:30:50+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getAuthored().getValue(),"2015-11-25T18:30:50+01:00");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2025-11-25T18:30:50+01:00' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_authored_noResults() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "authored", "2025-11-25T18:30:50+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1", "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponsePagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2015-11-25T18:30:50+01:00' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponsePagination_002() throws Exception {
		
		String parmName = "authored";
		String parmValue = "2015-11-25T18:30:50+01:00";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getAuthored().getValue(),"2015-11-25T18:30:50+01:00");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2025-11-25T18:30:50+01:00' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponsePagination_003() throws Exception {
		
		String parmName = "authored";
		String parmValue = "2025-11-25T18:30:50+01:00";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count == 0) && (lastPgNum == 0));
	}
}
