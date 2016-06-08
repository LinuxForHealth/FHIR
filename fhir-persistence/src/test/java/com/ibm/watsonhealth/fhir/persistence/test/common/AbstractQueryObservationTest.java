/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
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

import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.ObservationComponent;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryObservationTest extends AbstractPersistenceTest {
	
	private static Patient savedPatient;
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreatePatient() throws Exception {
    	try {
   		Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

    	persistence.create(patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        savedPatient = patient;
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation1() throws Exception {
        Observation observation = buildObservation(savedPatient.getId().getValue(), "Observation1.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation2() throws Exception {
        Observation observation = buildObservation("example", "observation-example.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation3() throws Exception {
        Observation observation = buildObservation("blood-pressure", "observation-example-bloodpressure.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation4() throws Exception {
        Observation observation = buildObservation("blood-pressure", "obs-uslab-example8.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    	
	/**
	 * Tests a query for an Observation with component-value-string = 'Systolic' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_001() throws Exception {
		
		String parmName = "component-value-string";
		String parmValue = "Systolic";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<ObservationComponent> compList = ((Observation)resources.get(0)).getComponent();
		assertEquals(compList.get(0).getValueString().getValue(),"Systolic");
	}
	
	/**
	 * Tests a query for an Observation with component-value-string = 'Diastolic' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_002() throws Exception {
		
		String parmName = "component-value-string";
		String parmValue = "Diastolic";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<ObservationComponent> compList = ((Observation)resources.get(0)).getComponent();
		assertEquals(compList.get(1).getValueString().getValue(),"Diastolic");
	}
	
	/**
	 * Tests a query for an Observation with value-string = 'Diastolic' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_003() throws Exception {
		
		String parmName = "value-string";
		String parmValue = "Diastolic";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for an Observation with encounter = 'Encounter/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_004() throws Exception {
		
		String parmName = "encounter";
		String parmValue = "Encounter/example";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getEncounter().getReference().getValue(),"Encounter/example");
	}
	
	/**
	 * Tests a query for an Observation with patient = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_005() throws Exception {
		
		String parmName = "patient";
		String parmValue = "Patient/example";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSubject().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for an Observation with subject = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_006() throws Exception {
		
		String parmName = "subject";
		String parmValue = "Patient/example";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSubject().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for an Observation with performer = 'Practitioner/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationQuery_007() throws Exception {
		
		String parmName = "performer";
		String parmValue = "Practitioner/example";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getPerformer().get(0).getReference().getValue(),"Practitioner/example");
	}
	
	/**
	 * Tests a query for an Observation with specimen = 'Specimen/spec-uslab-example2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_008() throws Exception {
		
		String parmName = "specimen";
		String parmValue = "Specimen/spec-uslab-example2";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSpecimen().getReference().getValue(),"Specimen/spec-uslab-example2");
	}
	
	/**
	 * Tests a query for an Observation with date = '2012-09-17' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationQuery_009() throws Exception {
		
		String parmName = "date";
		String parmValue = "2012-09-17";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getEffectiveDateTime().getValue(),"2012-09-17");
	}
	
	/**
	 * Tests a query for an Observation with value-date = '2014-12-04T15:42:15-08:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_0010() throws Exception {
		
		String parmName = "value-date";
		String parmValue = "2014-12-04T15:42:15-08:00";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueDateTime().getValue(),"2014-12-04T15:42:15-08:00");
	}
	
	/**
	 * Tests a query for an Observation with value-date (valuePeriod - start) = '2014-11-04T15:42:15-08:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_0011() throws Exception {
		
		String parmName = "value-date";
		String parmValue = "2014-11-04T15:42:15-08:00";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValuePeriod().getStart().getValue(),"2014-11-04T15:42:15-08:00");
	}
}
