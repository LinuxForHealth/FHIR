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

import com.ibm.watsonhealth.fhir.model.Questionnaire;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
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
    @Test(groups = { "persistence", "create", "device" })
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
	@Test(groups = { "persistence", "search", "questionnaire", "stringParam" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_001() throws Exception {
		
		String parmName = "publisher";
		String parmValue = "Team Voltron";
		Class<? extends Resource> resourceType = Questionnaire.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Questionnaire.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Questionnaire)resources.get(0)).getPublisher().getValue(),"Team Voltron");
	}
	
	/**
	 * Tests a query for a Questionnaire with version = '10.0' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaire", "stringParam" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_002() throws Exception {
		
		String parmName = "version";
		String parmValue = "10.0";
		Class<? extends Resource> resourceType = Questionnaire.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Questionnaire.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Questionnaire with title = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "questionnaire", "stringParam" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_003() throws Exception {
		
		String parmName = "title";
		String parmValue = "Non-existent";
		Class<? extends Resource> resourceType = Questionnaire.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		List<Parameter> searchParms = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Questionnaire.class, searchParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
}
