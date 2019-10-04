/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;
import java.util.Map;

import org.testng.annotations.Test;
/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMedicationOrderTest extends AbstractPersistenceTest {
	// TODO MedicationDispense?
//    private static MedicationOrder savedMedicationOrder;
//    
//    /**
//     * Tests the FHIRPersistenceCloudantImpl create API for a MedicationOrder.
//     * 
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
//    public void testCreateMedicationOrder() throws Exception {
//        MedicationOrder medOrder = readResource(MedicationOrder.class, "medicationorderexample1.canonical.json");
//
//        persistence.create(getDefaultPersistenceContext(), medOrder);
//        assertNotNull(medOrder);
//        assertNotNull(medOrder.getId());
//        assertNotNull(medOrder.getId().getValue());
//        assertNotNull(medOrder.getMeta());
//        assertNotNull(medOrder.getMeta().getVersionId().getValue());
//        assertEquals("1", medOrder.getMeta().getVersionId().getValue());
//        savedMedicationOrder = medOrder;
//    }
//    
//    /**
//     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_noParams() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, null, null);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//    }    
//    
//    /**
//     * Tests a query for a MedicationOrder with encounter = 'Encounter/f002' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_encounter() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "encounter", "Encounter/f002");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getEncounter().getReference().getValue(),"Encounter/f002");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with prescriber = 'Practitioner/f007' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_prescriber() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "prescriber", "Practitioner/f007");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getPrescriber().getReference().getValue(),"Practitioner/f007");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with patient = 'Practitioner/f007' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_patient() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "patient", "Patient/f001");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getPatient().getReference().getValue(),"Patient/f001");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with medication = 'Medication/MedicationExample2' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_medication() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "medication", "Medication/MedicationExample2");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/MedicationExample2");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2015-01-15' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "datewritten", "2015-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getDateWritten().getValue(),"2015-01-15");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_noResults() throws Exception {
//        List<Resource> resources = runQueryTest(MedicationOrder.class, persistence, "datewritten", "2025-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() == 0);
//    }
//    
//    /*
//     * Search Pagination Testcases
//     */
//    
//    /**
//     * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
//     * 
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderPagination_001() throws Exception {
//        
//        Class<? extends Resource> resourceType = MedicationOrder.class;
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
//        context.setPageNumber(1);
//        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationOrder.class);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        long count = context.getTotalCount();
//        int pageSize = context.getPageSize();
//        int lastPgNum = context.getLastPageNumber();
//        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
//        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with medication = 'Medication/MedicationExample2' which should yield correct results using pagination
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderPagination_002() throws Exception {
//        
//        String parmName = "medication";
//        String parmValue = "Medication/MedicationExample2";
//        Class<? extends Resource> resourceType = MedicationOrder.class;
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        
//        queryParms.put(parmName, Collections.singletonList(parmValue));
//        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
//        context.setPageNumber(1);
//        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationOrder.class);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/MedicationExample2");
//        long count = context.getTotalCount();
//        int pageSize = context.getPageSize();
//        int lastPgNum = context.getLastPageNumber();
//        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
//        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results using pagination
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderPagination_003() throws Exception {
//        
//        String parmName = "datewritten";
//        String parmValue = "2025-01-15";
//        Class<? extends Resource> resourceType = MedicationOrder.class;
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        
//        queryParms.put(parmName, Collections.singletonList(parmValue));
//        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
//        context.setPageNumber(1);
//        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationOrder.class);
//        assertNotNull(resources);
//        assertTrue(resources.size() == 0);
//        long count = context.getTotalCount();
////        int lastPgNum = context.getLastPageNumber();
//        assertTrue((count == 0)/* && (lastPgNum == Integer.MAX_VALUE)*/);
//    }
//    
//    /*
//     * History Pagination Testcases
//     */
//    
//    /**
//     * Tests retrieval of update history of a MedicationOrder. This should yield correct results using pagination
//     * 
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderHistoryPgn_001() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        queryParms.put("_page", Collections.singletonList("1"));
//        queryParms.put("_since", Collections.singletonList("2015-06-10T21:32:59.076Z"));
//        FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParms);
//        
//        List<Resource> resources = persistence.history(getPersistenceContextForHistory(context), MedicationOrder.class, savedMedicationOrder.getId().getValue());
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        long count = context.getTotalCount();
//        int pageSize = context.getPageSize();
//        int lastPgNum = context.getLastPageNumber();
//        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
//        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
//    }
//    
//    /*
//     * 
//     * Compartment search testcases
//     * 
//     */
//    
//    /**
//     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_noParams_EncCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Encounter", "f002", MedicationOrder.class, persistence, null, null);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2015-01-15' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_EncCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Encounter", "f002", MedicationOrder.class, persistence, "datewritten", "2015-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getDateWritten().getValue(),"2015-01-15");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_noResults_EncCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Encounter", "f002", MedicationOrder.class, persistence, "datewritten", "2025-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() == 0);
//    }
//    
//    /**
//     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_noParams_PatCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "f001", MedicationOrder.class, persistence, null, null);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2015-01-15' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_PatCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "f001", MedicationOrder.class, persistence, "datewritten", "2015-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getDateWritten().getValue(),"2015-01-15");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_noResults_PatCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "f001", MedicationOrder.class, persistence, "datewritten", "2025-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() == 0);
//    }
//    
//    /**
//     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_noParams_PractCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Practitioner", "f007", MedicationOrder.class, persistence, null, null);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2015-01-15' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_PractCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Practitioner", "f007", MedicationOrder.class, persistence, "datewritten", "2015-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((MedicationOrder)resources.get(0)).getDateWritten().getValue(),"2015-01-15");
//    }
//    
//    /**
//     * Tests a query for a MedicationOrder with datewritten = '2025-01-15' which should yield no results
//     * @throws Exception
//     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationOrder" })
//    public void testMedicationOrderQuery_datewritten_noResults_PractCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Practitioner", "f007", MedicationOrder.class, persistence, "datewritten", "2025-01-15");
//        assertNotNull(resources);
//        assertTrue(resources.size() == 0);
//    }
}
