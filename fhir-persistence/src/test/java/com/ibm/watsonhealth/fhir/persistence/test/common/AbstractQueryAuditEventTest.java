/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.AuditEvent;
import com.ibm.watsonhealth.fhir.model.resource.Device;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryAuditEventTest extends AbstractPersistenceTest {
    
    private static AuditEvent savedAuditEvent;
    private static AuditEvent savedAuditEvent1;
    private static AuditEvent savedAuditEvent2;
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateAuditEvent() throws Exception {
        AuditEvent auditEvt = readResource(AuditEvent.class, "auditevent-example-disclosure.canonical.json");

        persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
        savedAuditEvent = auditEvt;
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateAuditEvent_patient() throws Exception {
        AuditEvent auditEvt = readResource(AuditEvent.class, "AuditEvent_patient.json");

        persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
        savedAuditEvent1 = auditEvt;
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateAuditEvent_participant_patient() throws Exception {
        AuditEvent auditEvt = readResource(AuditEvent.class, "AuditEvent_participant_patient.json");

        persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
        savedAuditEvent2 = auditEvt;
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Device.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateDeviceForAuditEvent() throws Exception {
        Device device = readResource(Device.class, "Device_with_patient.json");

        persistence.create(getDefaultPersistenceContext(), device);
        assertNotNull(device);
        assertNotNull(device.getId());
        assertNotNull(device.getId().getValue());
        assertNotNull(device.getMeta());
        assertNotNull(device.getMeta().getVersionId().getValue());
        assertEquals("1", device.getMeta().getVersionId().getValue());
    } 
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent" })
    public void testAuditEventQuery_noParams() throws Exception {
        List<Resource> resources = runQueryTest(AuditEvent.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'R' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent" })
    public void testAuditEventQuery_action() throws Exception {
        List<Resource> resources = runQueryTest(AuditEvent.class, persistence, "action", "R");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((AuditEvent)resources.get(0)).getAction().getValue().toString(),"R");
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent" })
    public void testAuditEventQuery_action_noResults() throws Exception {
        List<Resource> resources = runQueryTest(AuditEvent.class, persistence, "action", "Error!!!");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * 
     * Compartment search testcases
     * 
     */
    
    /**
     * Tests for Single Inclusion criteria
     */
    
    /**
     * Inclusion Criteria: patient
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
    public void testAEQuery_noParams_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'R' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
    public void testAuditEventQuery_action_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "R");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((AuditEvent)resources.get(0)).getAction().getValue().toString(),"R");
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
    public void testAuditEventQuery_action_noResults_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "Error!!!");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests for Chained Inclusion criteria
     */
    
    /**
     * Inclusion Criteria: reference.patient
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent" })
    public void testAEQuery_noParams_reference_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'R' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent" })
    public void testAuditEventQuery_action_reference_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "R");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((AuditEvent)resources.get(0)).getAction().getValue().toString(),"R");
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent" })
    public void testAuditEventQuery_action_noResults_reference_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "Error!!!");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Inclusion Criteria: participant.patient
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent_participant_patient" })
    public void testAEQuery_noParams_participant_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, null, null);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'R' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent_participant_patient" })
    public void testAuditEventQuery_action_participant_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "R");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((AuditEvent)resources.get(0)).getAction().getValue().toString(),"R");
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent_participant_patient" })
    public void testAuditEventQuery_action_noResults_participant_patient_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "Error!!!");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests for Multiple Inclusion criteria
     */
    
    /**
     * Inclusion Criteria: patient or participant.patient or reference.patient
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
    public void testMutiInc_AEQuery_noParams_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, null, null, Integer.MAX_VALUE);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        //Get all the ids returned from search results
        List<String> resultSetIds = new ArrayList<String>();
        for(Resource temp : resources) {
            String id = ((AuditEvent)temp).getId().getValue();
            resultSetIds.add(id);
        }
        //Create a list of expected ids
        List<String> expectedIdList = new ArrayList<String>();
        expectedIdList.add(savedAuditEvent.getId().getValue());
        expectedIdList.add(savedAuditEvent1.getId().getValue());
        expectedIdList.add(savedAuditEvent2.getId().getValue());
                
        //Ensure that all the expected ids were returned correctly in search results
        //assertTrue(resultSetIds.containsAll(expectedIdList));
        assertTrue(resultSetIds.contains(savedAuditEvent1.getId().getValue()));
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'R' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateDeviceForAuditEvent", "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
    public void testMutiInc_AEQuery_action_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "R", Integer.MAX_VALUE);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        //Get all the ids returned from search results
        List<String> resultSetIds = new ArrayList<String>();
        for(Resource temp : resources) {
            String id = ((AuditEvent)temp).getId().getValue();
            resultSetIds.add(id);
        }
        //Create a list of expected ids
        List<String> expectedIdList = new ArrayList<String>();
        expectedIdList.add(savedAuditEvent.getId().getValue());
        expectedIdList.add(savedAuditEvent1.getId().getValue());
        expectedIdList.add(savedAuditEvent2.getId().getValue());
                
        //Ensure that all the expected ids were returned correctly in search results
        //assertTrue(resultSetIds.containsAll(expectedIdList));
        assertTrue(resultSetIds.contains(savedAuditEvent1.getId().getValue()));
    }
    
    /**
     * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
    public void testMutiInc_AEQuery_noResults_PatCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "patientID", AuditEvent.class, persistence, "action", "Error!!!");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
}
