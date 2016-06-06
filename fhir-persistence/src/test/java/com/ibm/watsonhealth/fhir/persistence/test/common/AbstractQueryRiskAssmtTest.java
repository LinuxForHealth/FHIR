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
    @Test(groups = { "persistence", "create", "riskAssessment" })
    public void testCreateRiskAssessment1() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "PrognosisRiskAssessment.json");

        persistence.create(riskAssmt);
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
    @Test(groups = { "persistence", "create", "riskAssessment" })
    public void testCreateRiskAssessment2() throws Exception {
        RiskAssessment riskAssmt = readResource(RiskAssessment.class, "riskassessment-example-cardiac.canonical.json");

        persistence.create(riskAssmt);
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
	@Test(groups = { "persistence", "search", "riskAssessment", "referenceParam" }, dependsOnMethods = { "testCreateRiskAssessment1" })
	public void testRiskAssessmentQuery_001() throws Exception {
		
		String parmName = "condition";
		String parmValue = "Condition/stroke";
		Class<? extends Resource> resourceType = RiskAssessment.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(RiskAssessment.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getCondition().getReference().getValue(),"Condition/stroke");
	}
	
	/**
	 * Tests a query for a RiskAssessment with performer = 'Practitioner/f001' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "persistence", "search", "riskAssessment", "referenceParam" }, dependsOnMethods = { "testCreateRiskAssessment2" })
	public void testRiskAssessmentQuery_002() throws Exception {
		
		String parmName = "performer";
		String parmValue = "Practitioner/f001";
		Class<? extends Resource> resourceType = RiskAssessment.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(RiskAssessment.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RiskAssessment)resources.get(0)).getPerformer().getReference().getValue(),"Practitioner/f001");
	}
}
