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
import com.ibm.watsonhealth.fhir.search.Parameter;
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
    @Test(groups = { "persistence", "create", "questionnaireResponse" })
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
    @Test(groups = { "persistence", "create", "questionnaireResponse" })
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
	@Test(groups = { "persistence", "search", "questionnaireResponse", "referenceParam" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_001() throws Exception {
		
		String parmName = "author";
		String parmValue = "Patient/1";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getAuthor().getReference().getValue(),"Patient/1");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with questionnaire = 'Questionnaire/1661690' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaireResponse", "referenceParam" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_002() throws Exception {
		
		String parmName = "questionnaire";
		String parmValue = "Questionnaire/1661690";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getQuestionnaire().getReference().getValue(),"Questionnaire/1661690");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with subject = 'Patient/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaireResponse", "referenceParam" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_003() throws Exception {
		
		String parmName = "subject";
		String parmValue = "Patient/1";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSubject().getReference().getValue(),"Patient/1");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f007' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaireResponse", "referenceParam" }, dependsOnMethods = { "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponseQuery_004() throws Exception {
		
		String parmName = "source";
		String parmValue = "Practitioner/f007";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSource().getReference().getValue(),"Practitioner/f007");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with patient = 'Patient/1' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaireResponse", "referenceParam" }, dependsOnMethods = { "testCreateQuestionnaireResponse1" })
	public void testQuestionnaireResponseQuery_005() throws Exception {
		
		String parmName = "patient";
		String parmValue = "Patient/1";
		Class<? extends Resource> resourceType = QuestionnaireResponse.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(QuestionnaireResponse.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSubject().getReference().getValue(),"Patient/1");
	}
}
