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

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.RiskAssessment;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryRiskAssmtTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a RiskAssessment.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateRiskAssessment1() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "PrognosisRiskAssessment.json");

        persistence.create(getDefaultPersistenceContext(), riskAssmt);
        assertNotNull(riskAssmt);
        assertNotNull(riskAssmt.getId());
        assertNotNull(riskAssmt.getId().getValue());
        assertNotNull(riskAssmt.getMeta());
        assertNotNull(riskAssmt.getMeta().getVersionId().getValue());
        assertEquals("1", riskAssmt.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a RiskAssessment.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateRiskAssessment2() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "riskassessment-example-cardiac.canonical.json");

        persistence.create(getDefaultPersistenceContext(), riskAssmt);
        assertNotNull(riskAssmt);
        assertNotNull(riskAssmt.getId());
        assertNotNull(riskAssmt.getId().getValue());
        assertNotNull(riskAssmt.getMeta());
        assertNotNull(riskAssmt.getMeta().getVersionId().getValue());
        assertEquals("1", riskAssmt.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a RiskAssessment.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateRiskAssessment3() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "RiskAssessment_for_Patient.json");

        persistence.create(getDefaultPersistenceContext(), riskAssmt);
        assertNotNull(riskAssmt);
        assertNotNull(riskAssmt.getId());
        assertNotNull(riskAssmt.getId().getValue());
        assertNotNull(riskAssmt.getMeta());
        assertNotNull(riskAssmt.getMeta().getVersionId().getValue());
        assertEquals("1", riskAssmt.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a RiskAssessment.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateRiskAssessment_with_device() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "RiskAssessment_with_Device.json");

        persistence.create(getDefaultPersistenceContext(), riskAssmt);
        assertNotNull(riskAssmt);
        assertNotNull(riskAssmt.getId());
        assertNotNull(riskAssmt.getId().getValue());
        assertNotNull(riskAssmt.getMeta());
        assertNotNull(riskAssmt.getMeta().getVersionId().getValue());
        assertEquals("1", riskAssmt.getMeta().getVersionId().getValue());
    }
    
    /**
	 * Tests a query for a RiskAssessment with condition = 'Condition/stroke' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment1" })
	public void testRiskAssessmentQuery_001() throws Exception {
		List<Resource> resources = runQueryTest(RiskAssessment.class, persistence, "condition", "Condition/stroke");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getCondition().getReference().getValue(),"Condition/stroke");
	}
	
	/**
	 * Tests a query for a RiskAssessment with performer = 'Practitioner/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment2" })
	public void testRiskAssessmentQuery_002() throws Exception {
		List<Resource> resources = runQueryTest(RiskAssessment.class, persistence, "performer", "Practitioner/f001");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getPerformer().getReference().getValue(),"Practitioner/f001");
	}
	
	/**
	 * Tests a query for a RiskAssessment with date = "2010-11-22" which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment2" })
	public void testRiskAssessmentQuery_003() throws Exception {
		List<Resource> resources = runQueryTest(RiskAssessment.class, persistence, "date", "2010-11-22");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getDate().getValue(),"2010-11-22");
	}
	
	/**
	 * Tests a query for a RiskAssessment with date = "2010-00-22" which should result in a FHIRSearchException being thrown
	 * because the date is invalid.
	 * 
	 */
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment2" }, expectedExceptions = FHIRSearchException.class)
	public void testRiskAssessmentQuery_004() throws Exception {
		List<Resource> resources = runQueryTest(RiskAssessment.class, persistence, "date", "2010-00-22");
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
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment1", "testCreateRiskAssessment2" })
	public void testRiskAssessmentPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = RiskAssessment.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), RiskAssessment.class);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a RiskAssessment with condition = 'Condition/stroke' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateRiskAssessment1" })
	public void testRiskAssessmentPagination_002() throws Exception {
		
		String parmName = "condition";
		String parmValue = "Condition/stroke";
		Class<? extends Resource> resourceType = RiskAssessment.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), RiskAssessment.class);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getCondition().getReference().getValue(),"Condition/stroke");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/*
	 * 
	 * Compartment search testcases
	 * 
	 */
	
	/**
	 * Tests a query for a RiskAssessment with performer = 'Practitioner/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment3" })
	public void testRiskAssessmentQuery_PatCompmt_1() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", RiskAssessment.class, persistence, "performer", "Practitioner/f001");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getPerformer().getReference().getValue(),"Practitioner/f001");
	}
	
	/**
	 * Tests a query for a RiskAssessment with date = "0010-11-22" which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment3" })
	public void testRiskAssessmentQuery_PatCompmt_2() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", RiskAssessment.class, persistence, "date", "0010-11-22");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a RiskAssessment with performer = 'Practitioner/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment3" })
	public void testRiskAssessmentQuery_PractCompmt_1() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f001", RiskAssessment.class, persistence, "performer", "Practitioner/f001");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getPerformer().getReference().getValue(),"Practitioner/f001");
	}
	
	/**
	 * Tests a query for a RiskAssessment with date = "0010-11-22" which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment3" })
	public void testRiskAssessmentQuery_PractCompmt_2() throws Exception {
		List<Resource> resources = runQueryTest("Practitioner", "f001", RiskAssessment.class, persistence, "date", "0010-11-22");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a RiskAssessment with performer = 'Practitioner/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment_with_device" })
	public void testRiskAssessmentQuery_DevCompmt_1() throws Exception {
		List<Resource> resources = runQueryTest("Device", "d001", RiskAssessment.class, persistence, "performer", "Device/d001");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getPerformer().getReference().getValue(),"Device/d001");
	}
	
	/**
	 * Tests a query for a RiskAssessment with date = "0010-11-22" which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized-broken" }, dependsOnMethods = { "testCreateRiskAssessment_with_device" })
	public void testRiskAssessmentQuery_DevCompmt_2() throws Exception {
		List<Resource> resources = runQueryTest("Device", "d001", RiskAssessment.class, persistence, "date", "0010-11-22");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
}
