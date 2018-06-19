/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Encounter;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

/**
 *  This class tests the persistence layer support for the FHIR _include search result parameter.
 *  @see https://www.hl7.org/fhir/DSTU2/search.html#include
 *  
 */
public abstract class AbstractQueryIncludeTest extends AbstractPersistenceTest {
	
	private static Patient savedPatient1;
	private static Patient savedPatient2;
	private static Observation savedObservation1;
	private static Observation savedObservation2;
	private static Observation savedObservation3;
	private static Observation savedObservation4;
	private static Encounter savedEncounter1;
	
 

    /**
     *  Loads up and saves an Observation that has no references to any other resource types.
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"})
    public void testCreateObservation1() throws Exception {
        Observation observation = readResource(Observation.class, "includeTest.observation1.json");

        persistence.create(getDefaultPersistenceContext(), observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        savedObservation1 = observation;
    }
    
    /**
     * Loads up and saves a Patient that will be used as a reference within an Observation
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"})
    public void testCreatePatient1() throws Exception {
    	 
   		Patient patient = readResource(Patient.class, "includeTest.patient1.json");

    	persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        savedPatient1 = patient;
    	 
    }
    
    /**
     * Loads up and saves a Patient that will be used as a reference within an Observation
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"})
    public void testCreatePatient2() throws Exception {
    	 
   		Patient patient = readResource(Patient.class, "includeTest.patient1.json");

    	persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        savedPatient2 = patient;
    	 
    }
    
    /**
     * Creates a new version of a previously created Patient,  that will be used as a reference within an Observation
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient2" })
    public void testUpdatePatient2() throws Exception {
    	 
   		savedPatient2.getName().clear();
   		HumanName newName = FHIRUtil.humanName("Vito", "Corleone");
   		savedPatient2.withName(Collections.singletonList(newName));

    	persistence.update(getDefaultPersistenceContext(), savedPatient2.getId().getValue(), savedPatient2);
        assertNotNull(savedPatient2);
        assertNotNull(savedPatient2.getId());
        assertNotNull(savedPatient2.getId().getValue());
        assertNotNull(savedPatient2.getMeta());
        assertNotNull(savedPatient2.getMeta().getVersionId().getValue());
        assertEquals("2", savedPatient2.getMeta().getVersionId().getValue());
    }
    
    /**
     *  Loads up and saves an Observation with a reference to a patient.
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient1" })
    public void testCreateObservation2() throws Exception {
        Observation observation = buildObservation(savedPatient1.getId().getValue(), "includeTest.observation1.json");
    	
        persistence.create(getDefaultPersistenceContext(), observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        savedObservation2 = observation;
    }
    
    /**
     *  Loads up and saves an Encounter that will be used as a reference within an Observation.
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"})
    public void testCreateEncounter1() throws Exception {
        Encounter encounter = readResource(Encounter.class, "includeTest.encounter1.json");
            	
        persistence.create(getDefaultPersistenceContext(), encounter);
        assertNotNull(encounter);
        assertNotNull(encounter.getId());
        assertNotNull(encounter.getId().getValue());
        assertNotNull(encounter.getMeta());
        assertNotNull(encounter.getMeta().getVersionId().getValue());
        assertEquals("1", encounter.getMeta().getVersionId().getValue());
        savedEncounter1 = encounter;
    }
    
    /**
     *  Loads up and saves an Observation with a reference to a patient and a reference to an Encounter.
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient1", "testCreateEncounter1" })
    public void testCreateObservation3() throws Exception {
        Observation observation = buildObservation(savedPatient1.getId().getValue(), "includeTest.observation1.json");
        observation.setEncounter(FHIRUtil.reference("Encounter/" + savedEncounter1.getId().getValue()));
    	
        persistence.create(getDefaultPersistenceContext(), observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        savedObservation3 = observation;
    }
    
    /**
     *  Loads up and saves an Observation with a reference to a patient and a reference to an Encounter.
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testUpdatePatient2", "testCreateEncounter1" })
    public void testCreateObservation4() throws Exception {
        Observation observation = buildObservation(savedPatient2.getId().getValue(), "includeTest.observation1.json");
        observation.setEncounter(FHIRUtil.reference("Encounter/" + savedEncounter1.getId().getValue()));
    	
        persistence.create(getDefaultPersistenceContext(), observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        savedObservation4 = observation;
    }
    
    /**
	 * This test queries an Observation and requests the inclusion of a referenced Patient. The Observation does NOT
	 * contain a referenced patient. 
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation1" })
	public void testNoIncludedData() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation1.getId().getValue()).append("&_include=Observation:patient");
    	queryParms.put("_id", Collections.singletonList(savedObservation1.getId().getValue()));
    	queryParms.put("_include", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 1);
		assertEquals("Observation", resources.get(0).getClass().getSimpleName());
		assertEquals(savedObservation1.getId().getValue(), resources.get(0).getId().getValue());
	}
	
    /**
	 * This test queries an Observation and requests the inclusion of a referenced Patient. The Observation does
	 * contain a referenced patient.
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation2" })
	public void testIncludedData() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:patient");
    	queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
    	queryParms.put("_include", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 2);
		for (Resource resource : resources) {
			if (resource instanceof Observation) {
				assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Patient) {
				assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
			}
			else {
				fail("Unexpected resource type returned.");
			}
		}
	}
	
    /**
	 * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
	 * The Observation only contains a referenced patient.
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation2" })
	public void testMultiInclude() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:patient")
		           .append("&_include=Observation:encounter");
    	queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
    	queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 2);
		for (Resource resource : resources) {
			if (resource instanceof Observation) {
				assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Patient) {
				assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
			}
			else {
				fail("Unexpected resource type returned.");
			}
		}
	}
	
    /**
	 * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
	 * The Observation contains BOTH a referenced patient and encounter.
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation3" })
	public void testMultiInclude1() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation3.getId().getValue()).append("&_include=Observation:patient")
		           .append("&_include=Observation:encounter");
    	queryParms.put("_id", Collections.singletonList(savedObservation3.getId().getValue()));
    	queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 3);
		for (Resource resource : resources) {
			if (resource instanceof Observation) {
				assertEquals(savedObservation3.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Patient) {
				assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Encounter) {
				assertEquals(savedEncounter1.getId().getValue(), resource.getId().getValue());
			}
			else {
				fail("Unexpected resource type returned.");
			}
		}
	}
	
    /**
	 * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
	 * The Observation contains BOTH a referenced patient and encounter. The patient referenced has 2 versions; only the 
	 * latest version should be returned.
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation4" })
	public void testMultiInclude2() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation4.getId().getValue()).append("&_include=Observation:patient")
		           .append("&_include=Observation:encounter");
    	queryParms.put("_id", Collections.singletonList(savedObservation4.getId().getValue()));
    	queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 3);
		for (Resource resource : resources) {
			if (resource instanceof Observation) {
				assertEquals(savedObservation4.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Patient) {
				assertEquals(savedPatient2.getId().getValue(), resource.getId().getValue());
				assertEquals("2", savedPatient2.getMeta().getVersionId().getValue());
			}
			else if (resource instanceof Encounter) {
				assertEquals(savedEncounter1.getId().getValue(), resource.getId().getValue());
			}
			else {
				fail("Unexpected resource type returned.");
			}
		}
	}
	
    /**
	 * This test queries an Observation, and requests the inclusion of all referenced subjects, which consists of 
	 * resource types: Device, Location, Patient, Group. The Observation contains one referenced patient.
	 * @throws Exception
	 */
	@Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreateObservation2" })
	public void testIncludedDataMultiTarget() throws Exception {
		Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:subject");
    	queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
    	queryParms.put("_include", Collections.singletonList("Observation:subject"));
        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
		assertNotNull(resources);
		assertTrue(resources.size() == 2);
		for (Resource resource : resources) {
			if (resource instanceof Observation) {
				assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
			}
			else if (resource instanceof Patient) {
				assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
			}
			else {
				fail("Unexpected resource type returned.");
			}
		}
	}
    
}
