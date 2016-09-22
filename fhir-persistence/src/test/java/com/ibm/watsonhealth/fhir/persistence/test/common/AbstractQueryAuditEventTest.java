/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.AuditEvent;
import com.ibm.watsonhealth.fhir.model.Resource;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryAuditEventTest extends AbstractPersistenceTest {
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateAuditEvent() throws Exception {
    	AuditEvent auditEvt = readResource(AuditEvent.class, "auditevent-example-disclosure.canonical.json");

    	persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateAuditEvent_patient() throws Exception {
    	AuditEvent auditEvt = readResource(AuditEvent.class, "AuditEvent_patient.json");

    	persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a AuditEvent.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateAuditEvent_participant_patient() throws Exception {
    	AuditEvent auditEvt = readResource(AuditEvent.class, "AuditEvent_participant_patient.json");

    	persistence.create(getDefaultPersistenceContext(), auditEvt);
        assertNotNull(auditEvt);
        assertNotNull(auditEvt.getId());
        assertNotNull(auditEvt.getId().getValue());
        assertNotNull(auditEvt.getMeta());
        assertNotNull(auditEvt.getMeta().getVersionId().getValue());
        assertEquals("1", auditEvt.getMeta().getVersionId().getValue());
    }
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
	public void testAuditEventQuery_noParams() throws Exception {
		List<Resource> resources = runQueryTest(AuditEvent.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for an AuditEvent with action = 'R' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
	public void testAuditEventQuery_action() throws Exception {
		List<Resource> resources = runQueryTest(AuditEvent.class, persistence, "action", "R");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((AuditEvent)resources.get(0)).getEvent().getAction().getValue().toString(),"R");
	}
	
	/**
	 * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
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
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
	public void testAEQuery_noParams_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", AuditEvent.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}
	
	/**
	 * Tests a query for an AuditEvent with action = 'R' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
	public void testAuditEventQuery_action_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", AuditEvent.class, persistence, "action", "R");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((AuditEvent)resources.get(0)).getEvent().getAction().getValue().toString(),"R");
	}
	
	/**
	 * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_patient" })
	public void testAuditEventQuery_action_noResults_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "example", AuditEvent.class, persistence, "action", "Error!!!");
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
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
	public void testAEQuery_noParams_reference_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'R' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
	public void testAuditEventQuery_action_reference_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "R");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((AuditEvent)resources.get(0)).getEvent().getAction().getValue().toString(),"R");
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent" })
	public void testAuditEventQuery_action_noResults_reference_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "Error!!!");
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
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_participant_patient" })
	public void testAEQuery_noParams_participant_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, null, null);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'R' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_participant_patient" })
	public void testAuditEventQuery_action_participant_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "R");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((AuditEvent)resources.get(0)).getEvent().getAction().getValue().toString(),"R");
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent_participant_patient" })
	public void testAuditEventQuery_action_noResults_participant_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "Error!!!");
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
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
	public void testMutiInc_AEQuery_noParams_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, null, null);
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
		expectedIdList.add("example-disclosure");
		expectedIdList.add("example-disclosure-1");
		expectedIdList.add("example-disclosure-2");
				
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'R' which should yield correct results
	 * @throws Exception
	 */
	/*@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
	public void testAuditEventQuery_action_participant_patient_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "R");
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
		expectedIdList.add("example-disclosure");
		expectedIdList.add("example-disclosure-1");
		expectedIdList.add("example-disclosure-2");
				
		//Ensure that all the expected ids were returned correctly in search results
		assertTrue(resultSetIds.containsAll(expectedIdList));
	}*/
	
	/**
	 * Tests a query for an AuditEvent with action = 'Error!!!' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "jpa" }, dependsOnMethods = { "testCreateAuditEvent", "testCreateAuditEvent_patient", "testCreateAuditEvent_participant_patient" })
	public void testMutiInc_AEQuery_action_PatCompmt() throws Exception {
		List<Resource> resources = runQueryTest("Patient", "1", AuditEvent.class, persistence, "action", "Error!!!");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
}
