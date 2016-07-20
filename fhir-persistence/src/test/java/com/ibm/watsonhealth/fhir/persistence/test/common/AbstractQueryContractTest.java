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

import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.Contract;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryContractTest extends AbstractPersistenceTest {
	
	private static Contract savedContract;
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Contract.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateContract() throws Exception {
    	Contract contract = readResource(Contract.class, "contract-example.canonical.json");

    	persistence.create(contract);
        assertNotNull(contract);
        assertNotNull(contract.getId());
        assertNotNull(contract.getId().getValue());
        assertNotNull(contract.getMeta());
        assertNotNull(contract.getMeta().getVersionId().getValue());
        assertEquals("1", contract.getMeta().getVersionId().getValue());
        savedContract = contract;
    } 
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractQuery_noParams() throws Exception {
		List<Resource> resources = runQueryTest(Contract.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}	
	
	/**
	 * Tests a query for a Contract with patient = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractQuery_patient() throws Exception {
		List<Resource> resources = runQueryTest(Contract.class, persistence, "patient", "Patient/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Contract)resources.get(0)).getSubject().get(0).getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for a Contract with subject = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractQuery_subject() throws Exception {
		List<Resource> resources = runQueryTest(Contract.class, persistence, "subject", "Patient/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Contract)resources.get(0)).getSubject().get(0).getReference().getValue(),"Patient/example");
	}
	
	/**
     * Tests the FHIRPersistenceCloudantImpl update API for a Contract.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
    public void testUpdateContract() throws Exception {
    	Contract contract = savedContract;
    	contract.setLanguage((new Code()).withValue("it"));
        persistence.update(contract.getId().getValue(), contract);
        assertNotNull(contract);
        assertEquals("2", contract.getMeta().getVersionId().getValue());

        // Now re-read the risk assessment and make sure we get back the correctly updated object
        Contract retrievedContract = (Contract) persistence.read(Contract.class, contract.getId().getValue());
        assertNotNull(retrievedContract);
        assertResourceEquals(contract, retrievedContract);
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl update API for a Contract.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract", "testUpdateContract" })
    public void testUpdateContractAgain() throws Exception {
    	Contract contract = savedContract;
    	contract.setLanguage((new Code()).withValue("pt-BR"));
        persistence.update(contract.getId().getValue(), contract);
        assertNotNull(contract);
        assertEquals("3", contract.getMeta().getVersionId().getValue());

        // Now re-read the risk assessment and make sure we get back the correctly updated object
        Contract retrievedContract = (Contract) persistence.read(Contract.class, contract.getId().getValue());
        assertNotNull(retrievedContract);
        assertResourceEquals(contract, retrievedContract);
    }
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Contract.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Contract.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Contract with subject = 'Patient/example' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractPagination_002() throws Exception {
		
		String parmName = "subject";
		String parmValue = "Patient/example";
		Class<? extends Resource> resourceType = Contract.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Contract.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Contract)resources.get(0)).getSubject().get(0).getReference().getValue(),"Patient/example");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for a Contract with subject = 'Patient/???' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractPagination_003() throws Exception {
		
		String parmName = "subject";
		String parmValue = "Patient/???";
		Class<? extends Resource> resourceType = Contract.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Contract.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
		int lastPgNum = context.getLastPageNumber();
		assertTrue((count == 0) && (lastPgNum == Integer.MAX_VALUE));
	}
	
	/*
	 * History Pagination Testcases
	 */
	
	/**
	 * Tests retrieval of update history of a Contract with a since parameter set. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractHistoryPgn_001() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_page", Collections.singletonList("1"));
		queryParms.put("_since", Collections.singletonList("2015-06-10T21:32:59.076Z"));
		FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParms);
		
		List<Resource> resources = persistence.history(Contract.class, savedContract.getId().getValue(), context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests retrieval of update history of a Contract without a since & count parameter set. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract" })
	public void testContractHistoryPgn_002() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_page", Collections.singletonList("1"));
		FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParms);
		
		List<Resource> resources = persistence.history(Contract.class, savedContract.getId().getValue(), context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests retrieval of update history of a Contract with a count parameter set. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateContract", "testUpdateContract", "testUpdateContractAgain" })
	public void testContractHistoryPgn_003() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_page", Collections.singletonList("1"));
        queryParms.put("_count", Collections.singletonList("2"));
		FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParms);
		
		List<Resource> resources = persistence.history(Contract.class, savedContract.getId().getValue(), context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 2) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
}
