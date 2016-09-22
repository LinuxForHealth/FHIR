/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
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

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_patient() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "questionnaireresponse-example-gcs.canonical2.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_encounter() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse_with_encounter.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_encounter_1() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse_with_encounter_1.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_relatedPerson() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse-with-RelatedPerson.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_relatedPerson2() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse-with-RelatedPerson2.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
    public void testCreateQuestionnaireResponse_with_device() throws Exception {
    	QuestionnaireResponse questionnaireResp = readResource(QuestionnaireResponse.class, "QuestionnaireResponse_with_device.json");

        persistence.create(getDefaultPersistenceContext(), questionnaireResp);
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
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), QuestionnaireResponse.class);
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
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), QuestionnaireResponse.class);
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
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), QuestionnaireResponse.class);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
//		int lastPgNum = context.getLastPageNumber();
		assertTrue((count == 0)/* && (lastPgNum == Integer.MAX_VALUE)*/);
	}
	
	/*
	 * 
	 * Compartment search testcases
	 * 
	 */
	
	/**
	 * Tests for Single Inclusion criteria
	 */
	
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_noParams_EncCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Encounter", "f002", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with author = 'Practitioner/f201' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_author_EncCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Encounter", "f002", QuestionnaireResponse.class, persistence, "author", "Practitioner/f201");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getAuthor().getReference().getValue(),"Practitioner/f201");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2025-11-25T18:30:50+01:00' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_authored_noResults_EncCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Encounter", "f002", QuestionnaireResponse.class, persistence, "authored", "2025-11-25T18:30:50+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponseQuery_noParams_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with subject = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponseQuery_author_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", QuestionnaireResponse.class, persistence, "subject", "Patient/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSubject().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2025-11-25T18:30:50+01:00' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2" })
	public void testQuestionnaireResponseQuery_authored_noResults_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", QuestionnaireResponse.class, persistence, "authored", "2025-11-25T18:30:50+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_relatedPerson" })
	public void testQuestionnaireResponseQuery_noParams_RelatedPerson_compartment() throws Exception {
		List<Resource> resources = runQueryTest("RelatedPerson", "Benedicte", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'RelatedPerson/Benedicte' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_relatedPerson" })
	public void testQuestionnaireResponseQuery_source_RelatedPerson_compartment() throws Exception {
		List<Resource> resources = runQueryTest("RelatedPerson", "Benedicte", QuestionnaireResponse.class, persistence, "source", "RelatedPerson/Benedicte");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSource().getReference().getValue(),"RelatedPerson/Benedicte");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'RelatedPerson/None' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_relatedPerson" })
	public void testQuestionnaireResponseQuery_sourceNoResults_RelatedPerson_compartment() throws Exception {
		List<Resource> resources = runQueryTest("RelatedPerson", "Benedicte", QuestionnaireResponse.class, persistence, "source", "RelatedPerson/None");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_noParams_PractCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f201", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f201' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_source_PractCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f201", QuestionnaireResponse.class, persistence, "source", "Practitioner/f201");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSource().getReference().getValue(),"Practitioner/f201");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f2001' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_sourceNoResults_PractCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f201", QuestionnaireResponse.class, persistence, "source", "Practitioner/f2001");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f201', but an incorrect compartment id = f2001 which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter" })
	public void testQuestionnaireResponseQuery_badCompmtIdNoResults_PractCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f2001", QuestionnaireResponse.class, persistence, "source", "Practitioner/f2001");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_device" })
	public void testQuestionnaireResponseQuery_noParams_DevCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Device", "devID", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f201' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_device" })
	public void testQuestionnaireResponseQuery_source_DevCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Device", "devID", QuestionnaireResponse.class, persistence, "source", "Practitioner/f201");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((QuestionnaireResponse)resources.get(0)).getSource().getReference().getValue(),"Practitioner/f201");
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f2001' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_device" })
	public void testQuestionnaireResponseQuery_sourceNoResults_DevCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Device", "devID", QuestionnaireResponse.class, persistence, "source", "Practitioner/f2001");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with source = 'Practitioner/f201', but an incorrect compartment id = f2001 which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_device" })
	public void testQuestionnaireResponseQuery_badCompmtIdNoResults_DevCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Device", "devId", QuestionnaireResponse.class, persistence, "source", "Practitioner/f2001");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests for Multiple Inclusion criteria
	 */
	
	/**
	 * Tests a query for QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_relatedPerson", "testCreateQuestionnaireResponse_with_relatedPerson2" })
	public void testMutiInc_QRQuery_noParams_RelatedPersonCompmt() throws Exception {
		List<Resource> resources = runQueryTest("RelatedPerson", "Benedicte", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("gcs");
		expectedIdList.add("gcs_1");
				
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2014-12-11T04:44:16Z' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_relatedPerson", "testCreateQuestionnaireResponse_with_relatedPerson2" })
	public void testMutiInc_QRQuery_source_RelatedPersonCompmt() throws Exception {
		List<Resource> resources = runQueryTest("RelatedPerson", "Benedicte", QuestionnaireResponse.class, persistence, "authored", "2014-12-11T04:44:16Z");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("gcs");
		expectedIdList.add("gcs_1");
		
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
	
	/**
	 * Tests a query for QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter", "testCreateQuestionnaireResponse_with_encounter_1" })
	public void testMutiInc_QRQuery_noParams_PractitionerCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f201", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("f201");
		expectedIdList.add("f202");
				
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse with authored = '2013-06-18T00:00:00+01:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse_with_encounter", "testCreateQuestionnaireResponse_with_encounter_1" })
	public void testMutiInc_QRQuery_source_PractitionerCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f201", QuestionnaireResponse.class, persistence, "authored", "2013-06-18T00:00:00+01:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("f201");
		expectedIdList.add("f202");
		
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2", "testCreateQuestionnaireResponse_with_patient" })
	public void testMutiInc_QRQuery_noParams_PatientCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", QuestionnaireResponse.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("gcs_2");
		expectedIdList.add("gcs");
		
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
	
	/**
	 * Tests a query for a QuestionnaireResponse resource type with authored = '2014-12-11T04:44:16Z' which should yield correct results.
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateQuestionnaireResponse2", "testCreateQuestionnaireResponse_with_patient" })
	public void testMutiInc_QRQuery_authored_PatientCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", QuestionnaireResponse.class, persistence, "authored", "2014-12-11T04:44:16Z");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		//Get all the ids returned from search results
		List<String> resultSetIds = new ArrayList<String>();
		for(Resource temp : resources) {
			String id = ((QuestionnaireResponse)temp).getId().getValue();
			resultSetIds.add(id);
		}
		//Create a list of expected ids
		List<String> expectedIdList = new ArrayList<String>();
		expectedIdList.add("gcs_2");
		expectedIdList.add("gcs");
		
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}
}
