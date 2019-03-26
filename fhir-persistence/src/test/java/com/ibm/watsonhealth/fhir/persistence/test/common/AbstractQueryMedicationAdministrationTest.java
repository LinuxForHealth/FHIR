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

import com.ibm.watsonhealth.fhir.model.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMedicationAdministrationTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a MedicationAdministration.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateMedicationAdministration() throws Exception {
        MedicationAdministration medAdmin = readResource(MedicationAdministration.class, "medicationadministrationexample1.canonical.json");

        persistence.create(getDefaultPersistenceContext(), medAdmin);
        assertNotNull(medAdmin);
        assertNotNull(medAdmin.getId());
        assertNotNull(medAdmin.getId().getValue());
        assertNotNull(medAdmin.getMeta());
        assertNotNull(medAdmin.getMeta().getVersionId().getValue());
        assertEquals("1", medAdmin.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a MedicationAdministration.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateMedicationAdministration_with_device() throws Exception {
        MedicationAdministration medAdmin = readResource(MedicationAdministration.class, "medicationadministration_with_device.json");

        persistence.create(getDefaultPersistenceContext(), medAdmin);
        assertNotNull(medAdmin);
        assertNotNull(medAdmin.getId());
        assertNotNull(medAdmin.getId().getValue());
        assertNotNull(medAdmin.getMeta());
        assertNotNull(medAdmin.getMeta().getVersionId().getValue());
        assertEquals("1", medAdmin.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_noParams() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a MedicationAdministration with patient = 'Patient/example' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_patient() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "patient", "Patient/example");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getPatient().getReference().getValue(),"Patient/example");
    }
    
    /**
     * Tests a query for a MedicationAdministration with prescription = 'MedicationOrder/medrx005' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_prescription() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "prescription", "MedicationOrder/medrx005");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getPrescription().getReference().getValue(),"MedicationOrder/medrx005");
    }
    
    /**
     * Tests a query for a MedicationAdministration with practitioner = 'Practitioner/example' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_practitioner() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "practitioner", "Practitioner/example");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getPractitioner().getReference().getValue(),"Practitioner/example");
    }
    
    /**
     * Tests a query for a MedicationAdministration with medication = 'Medication/medicationexample6' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_medication() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "medication", "Medication/medicationexample6");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getMedicationReference().getReference().getValue(),"Medication/medicationexample6");
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2015-01-15T14:30:00+01:00' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_effectivetime() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "effectivetime", "2015-01-15T14:30:00+01:00");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getEffectiveTimePeriod().getEnd().getValue(),"2015-01-15T14:30:00+01:00");
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2025-01-15T14:30:00+01:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_effectivetime_noResults() throws Exception {
        List<Resource> resources = runQueryTest(MedicationAdministration.class, persistence, "effectivetime", "2025-01-15T14:30:00+01:00");
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
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationPagination_001() throws Exception {
        
        Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationAdministration.class);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        long count = context.getTotalCount();
        int pageSize = context.getPageSize();
        int lastPgNum = context.getLastPageNumber();
        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2015-01-15T14:30:00+01:00' which should yield correct results using pagination
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationPagination_002() throws Exception {
        
        String parmName = "effectivetime";
        String parmValue = "2015-01-15T14:30:00+01:00";
        Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        
        queryParms.put(parmName, Collections.singletonList(parmValue));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationAdministration.class);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getEffectiveTimePeriod().getEnd().getValue(),"2015-01-15T14:30:00+01:00");
        long count = context.getTotalCount();
        int pageSize = context.getPageSize();
        int lastPgNum = context.getLastPageNumber();
        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2025-01-15T14:30:00+01:00' which should yield no results using pagination
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationPagination_003() throws Exception {
        
        String parmName = "effectivetime";
        String parmValue = "2025-01-15T14:30:00+01:00";
        Class<? extends Resource> resourceType = MedicationAdministration.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        
        queryParms.put(parmName, Collections.singletonList(parmValue));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), MedicationAdministration.class);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        long count = context.getTotalCount();
//        int lastPgNum = context.getLastPageNumber();
        assertTrue((count == 0)/* && (lastPgNum == Integer.MAX_VALUE)*/);
    }
    
    /*
     * 
     * Compartment search testcases
     * 
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_noParams_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "example", MedicationAdministration.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a MedicationAdministration with patient = 'Patient/example' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_patient_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "example", MedicationAdministration.class, persistence, "patient", "Patient/example");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getPatient().getReference().getValue(),"Patient/example");
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2025-01-15T14:30:00+01:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_effectivetime_noResults_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "example", MedicationAdministration.class, persistence, "effectivetime", "2025-01-15T14:30:00+01:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_noParams_DevCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Device", "devID", MedicationAdministration.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a MedicationAdministration with patient = 'Patient/example' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_patient_DevCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Device", "devID", MedicationAdministration.class, persistence, "patient", "Patient/example");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((MedicationAdministration)resources.get(0)).getPatient().getReference().getValue(),"Patient/example");
    }
    
    /**
     * Tests a query for a MedicationAdministration with effectivetime = '2025-01-15T14:30:00+01:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testMedicationAdministrationQuery_effectivetime_noResults_DevCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Device", "devID", MedicationAdministration.class, persistence, "effectivetime", "2025-01-15T14:30:00+01:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
}
