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

import com.ibm.watsonhealth.fhir.model.Questionnaire;
import com.ibm.watsonhealth.fhir.model.Resource;

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
	public void testQuestionnaireQuery_001() throws Exception {
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
	public void testQuestionnaireQuery_002() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "version", "10.0");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Questionnaire with title = 'Non-existent' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_003() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "title", "Non-existent");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Questionnaire with date = '1969-12-31T19:00:02+00:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateQuestionnaire" })
	public void testQuestionnaireQuery_004() throws Exception {
		List<Resource> resources = runQueryTest(Questionnaire.class, persistence, "date", "1969-12-31T19:00:02+00:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Questionnaire)resources.get(0)).getDate().getValue(),"1969-12-31T19:00:02+00:00");
	}
}
