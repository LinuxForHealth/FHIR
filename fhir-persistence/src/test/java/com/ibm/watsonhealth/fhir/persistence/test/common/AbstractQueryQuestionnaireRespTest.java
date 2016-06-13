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

import com.ibm.watsonhealth.fhir.model.QuestionnaireResponse;
import com.ibm.watsonhealth.fhir.model.Resource;

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
	public void testQuestionnaireResponseQuery_001() throws Exception {
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
	public void testQuestionnaireResponseQuery_002() throws Exception {
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
	public void testQuestionnaireResponseQuery_003() throws Exception {
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
	public void testQuestionnaireResponseQuery_004() throws Exception {
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
	public void testQuestionnaireResponseQuery_005() throws Exception {
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
	public void testQuestionnaireResponseQuery_006() throws Exception {
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
	public void testQuestionnaireResponseQuery_007() throws Exception {
		List<Resource> resources = runQueryTest(QuestionnaireResponse.class, persistence, "authored", "2025-11-25T18:30:50+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
}
