/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

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

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 *  This class tests the persistence layer support for the FHIR _include and _revinclude search result parameters.
 *  @see https://www.hl7.org/fhir/DSTU2/search.html#include
 *  
 */
public abstract class AbstractQueryIncludeTest extends AbstractPersistenceTest {
    
//    private static Patient savedPatient1;
//    private static Patient savedPatient2;
//    private static Patient savedPatient3;
//    private static Patient savedPatient4;
//    private static Observation savedObservation1;
//    private static Observation savedObservation2;
//    private static Observation savedObservation3;
//    private static Observation savedObservation4;
//    private static Observation savedObservation5;
//    private static Encounter savedEncounter1;
//    private static Device savedDevice1;
//    private static Organization savedOrg1;
//    
// 
//
//    /**
//     *  Loads up and saves an Observation that has no references to any other resource types.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createObservation1() throws Exception {
//        Observation observation = readResource(Observation.class, "includeTest.observation1.json");
//
//        persistence.create(getDefaultPersistenceContext(), observation);
//        assertNotNull(observation);
//        assertNotNull(observation.getId());
//        assertNotNull(observation.getId().getValue());
//        assertNotNull(observation.getMeta());
//        assertNotNull(observation.getMeta().getVersionId().getValue());
//        assertEquals("1", observation.getMeta().getVersionId().getValue());
//        savedObservation1 = observation;
//    }
//    
//    /**
//     * Loads up and saves a Patient that will be used as a reference within an Observation
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createPatient1() throws Exception {
//         
//           Patient patient = readResource(Patient.class, "includeTest.patient1.json");
//
//        persistence.create(getDefaultPersistenceContext(), patient);
//        assertNotNull(patient);
//        assertNotNull(patient.getId());
//        assertNotNull(patient.getId().getValue());
//        assertNotNull(patient.getMeta());
//        assertNotNull(patient.getMeta().getVersionId().getValue());
//        assertEquals("1", patient.getMeta().getVersionId().getValue());
//        savedPatient1 = patient;
//         
//    }
//    
//    /**
//     * Loads up and saves a Patient that will be used as a reference within an Observation
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createPatient2() throws Exception {
//         
//           Patient patient = readResource(Patient.class, "includeTest.patient1.json");
//
//        persistence.create(getDefaultPersistenceContext(), patient);
//        assertNotNull(patient);
//        assertNotNull(patient.getId());
//        assertNotNull(patient.getId().getValue());
//        assertNotNull(patient.getMeta());
//        assertNotNull(patient.getMeta().getVersionId().getValue());
//        assertEquals("1", patient.getMeta().getVersionId().getValue());
//        savedPatient2 = patient;
//         
//    }
//    
//    /**
//     * Loads up and saves a Patient that will be used as a reference within an Observation and a Device.
//     * Also, it will contain a reference to a managing organization.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createOrganization1" })
//    public void createPatient3() throws Exception {
//         
//           Patient patient = readResource(Patient.class, "includeTest.patient1.json");
//           patient.setManagingOrganization(reference("Organization/" + savedOrg1.getId().getValue()));
//
//        persistence.create(getDefaultPersistenceContext(), patient);
//        assertNotNull(patient);
//        assertNotNull(patient.getId());
//        assertNotNull(patient.getId().getValue());
//        assertNotNull(patient.getMeta());
//        assertNotNull(patient.getMeta().getVersionId().getValue());
//        assertEquals("1", patient.getMeta().getVersionId().getValue());
//        savedPatient3 = patient;
//    }
//    
//    /**
//     * Loads up and saves a Patient that will have no other resources referencing it.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createPatient4() throws Exception {
//         
//           Patient patient = readResource(Patient.class, "includeTest.patient1.json");
//
//        persistence.create(getDefaultPersistenceContext(), patient);
//        assertNotNull(patient);
//        assertNotNull(patient.getId());
//        assertNotNull(patient.getId().getValue());
//        assertNotNull(patient.getMeta());
//        assertNotNull(patient.getMeta().getVersionId().getValue());
//        assertEquals("1", patient.getMeta().getVersionId().getValue());
//        savedPatient4 = patient;
//    }
//    
//    /**
//     * Creates a new version of a previously created Patient,  that will be used as a reference within an Observation
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient2" })
//    public void updatePatient2() throws Exception {
//         
//           savedPatient2.getName().clear();
//           HumanName newName = FHIRUtil.humanName("Vito", "Corleone");
//           savedPatient2.withName(Collections.singletonList(newName));
//
//        persistence.update(getDefaultPersistenceContext(), savedPatient2.getId().getValue(), savedPatient2);
//        assertNotNull(savedPatient2);
//        assertNotNull(savedPatient2.getId());
//        assertNotNull(savedPatient2.getId().getValue());
//        assertNotNull(savedPatient2.getMeta());
//        assertNotNull(savedPatient2.getMeta().getVersionId().getValue());
//        assertEquals("2", savedPatient2.getMeta().getVersionId().getValue());
//    }
//    
//    /**
//     * Creates a new version of a previously created Patient,  that will be used as a reference within an Observation
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testMultiRevInclude1" })
//    public void updateDevice1() throws Exception {
//         
//           savedDevice1.setManufacturer(string("Updated Manufacturer"));
//
//        persistence.update(getDefaultPersistenceContext(), savedDevice1.getId().getValue(), savedDevice1);
//        assertNotNull(savedDevice1);
//        assertNotNull(savedDevice1.getId());
//        assertNotNull(savedDevice1.getId().getValue());
//        assertNotNull(savedDevice1.getMeta());
//        assertNotNull(savedDevice1.getMeta().getVersionId().getValue());
//        assertEquals("2", savedDevice1.getMeta().getVersionId().getValue());
//    }
//    
//    /**
//     *  Loads up and saves an Observation with a reference to a patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient1" })
//    public void createObservation2() throws Exception {
//        Observation observation = buildObservation(savedPatient1.getId().getValue(), "includeTest.observation1.json");
//        
//        persistence.create(getDefaultPersistenceContext(), observation);
//        assertNotNull(observation);
//        assertNotNull(observation.getId());
//        assertNotNull(observation.getId().getValue());
//        assertNotNull(observation.getMeta());
//        assertNotNull(observation.getMeta().getVersionId().getValue());
//        assertEquals("1", observation.getMeta().getVersionId().getValue());
//        savedObservation2 = observation;
//    }
//    
//    /**
//     *  Loads up and saves an Encounter that will be used as a reference within an Observation.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createEncounter1() throws Exception {
//        Encounter encounter = readResource(Encounter.class, "includeTest.encounter1.json");
//                
//        persistence.create(getDefaultPersistenceContext(), encounter);
//        assertNotNull(encounter);
//        assertNotNull(encounter.getId());
//        assertNotNull(encounter.getId().getValue());
//        assertNotNull(encounter.getMeta());
//        assertNotNull(encounter.getMeta().getVersionId().getValue());
//        assertEquals("1", encounter.getMeta().getVersionId().getValue());
//        savedEncounter1 = encounter;
//    }
//    
//    /**
//     *  Loads up and saves an Observation with a reference to a patient and a reference to an Encounter.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient1", "createEncounter1" })
//    public void createObservation3() throws Exception {
//        Observation observation = buildObservation(savedPatient1.getId().getValue(), "includeTest.observation1.json");
//        observation.setEncounter(reference("Encounter/" + savedEncounter1.getId().getValue()));
//        
//        persistence.create(getDefaultPersistenceContext(), observation);
//        assertNotNull(observation);
//        assertNotNull(observation.getId());
//        assertNotNull(observation.getId().getValue());
//        assertNotNull(observation.getMeta());
//        assertNotNull(observation.getMeta().getVersionId().getValue());
//        assertEquals("1", observation.getMeta().getVersionId().getValue());
//        savedObservation3 = observation;
//    }
//    
//    /**
//     *  Loads up and saves an Observation with a reference to a patient and a reference to an Encounter.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "updatePatient2", "createEncounter1" })
//    public void createObservation4() throws Exception {
//        Observation observation = buildObservation(savedPatient2.getId().getValue(), "includeTest.observation1.json");
//        observation.setEncounter(FHIRUtil.reference("Encounter/" + savedEncounter1.getId().getValue()));
//        
//        persistence.create(getDefaultPersistenceContext(), observation);
//        assertNotNull(observation);
//        assertNotNull(observation.getId());
//        assertNotNull(observation.getId().getValue());
//        assertNotNull(observation.getMeta());
//        assertNotNull(observation.getMeta().getVersionId().getValue());
//        assertEquals("1", observation.getMeta().getVersionId().getValue());
//        savedObservation4 = observation;
//    }
//    
//    /**
//     *  Loads up and saves an Observation with a reference to a patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient3"})
//    public void createObservation5() throws Exception {
//        Observation observation = buildObservation(savedPatient3.getId().getValue(), "includeTest.observation1.json");
//                        
//        persistence.create(getDefaultPersistenceContext(), observation);
//        assertNotNull(observation);
//        assertNotNull(observation.getId());
//        assertNotNull(observation.getId().getValue());
//        assertNotNull(observation.getMeta());
//        assertNotNull(observation.getMeta().getVersionId().getValue());
//        assertEquals("1", observation.getMeta().getVersionId().getValue());
//        savedObservation5 = observation;
//    }
//    
//    /**
//     *  Loads up and saves a Device with a reference to a patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient3"})
//    public void createDevice1() throws Exception {
//        Device device = readResource(Device.class, "includeTest.device1.json");
//        device.withPatient(reference("Patient/" + savedPatient3.getId().getValue()));
//                
//        persistence.create(getDefaultPersistenceContext(), device);
//        assertNotNull(device);
//        assertNotNull(device.getId());
//        assertNotNull(device.getId().getValue());
//        assertNotNull(device.getMeta());
//        assertNotNull(device.getMeta().getVersionId().getValue());
//        assertEquals("1", device.getMeta().getVersionId().getValue());
//        savedDevice1 = device;
//    }
//    
//    /**
//     *  Loads up and saves an Organization, that will be referenced by a Patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"})
//    public void createOrganization1() throws Exception {
//        Organization org = readResource(Organization.class, "includeTest.organization1.json");
//                        
//        persistence.create(getDefaultPersistenceContext(), org);
//        assertNotNull(org);
//        assertNotNull(org.getId());
//        assertNotNull(org.getId().getValue());
//        assertNotNull(org.getMeta());
//        assertNotNull(org.getMeta().getVersionId().getValue());
//        assertEquals("1", org.getMeta().getVersionId().getValue());
//        savedOrg1 = org;
//    }
//    
//    /**
//     * This test queries an Observation and requests the inclusion of a referenced Patient. The Observation does NOT
//     * contain a referenced patient. 
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation1" })
//    public void testNoIncludedData() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation1.getId().getValue()).append("&_include=Observation:patient");
//        queryParms.put("_id", Collections.singletonList(savedObservation1.getId().getValue()));
//        queryParms.put("_include", Collections.singletonList("Observation:patient"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(1, resources.size());
//        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
//        assertEquals(savedObservation1.getId().getValue(), resources.get(0).getId().getValue());
//    }
//    
//    /**
//     * This test queries an Observation and requests the inclusion of a referenced Patient. The Observation does
//     * contain a referenced patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2" })
//    public void testIncludedData() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:patient");
//        queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
//        queryParms.put("_include", Collections.singletonList("Observation:patient"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(2, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
//     * The Observation only contains a referenced patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2" })
//    public void testMultiInclude() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:patient")
//                   .append("&_include=Observation:encounter");
//        queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
//        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(2, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
//     * The Observation contains BOTH a referenced patient and encounter.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation3" })
//    public void testMultiInclude1() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation3.getId().getValue()).append("&_include=Observation:patient")
//                   .append("&_include=Observation:encounter");
//        queryParms.put("_id", Collections.singletonList(savedObservation3.getId().getValue()));
//        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation3.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Encounter) {
//                assertEquals(savedEncounter1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter. 
//     * The Observation contains BOTH a referenced patient and encounter. The patient referenced has 2 versions; only the 
//     * latest version should be returned.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation4" })
//    public void testMultiInclude2() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation4.getId().getValue()).append("&_include=Observation:patient")
//                   .append("&_include=Observation:encounter");
//        queryParms.put("_id", Collections.singletonList(savedObservation4.getId().getValue()));
//        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation4.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient2.getId().getValue(), resource.getId().getValue());
//                assertEquals("2", savedPatient2.getMeta().getVersionId().getValue());
//            }
//            else if (resource instanceof Encounter) {
//                assertEquals(savedEncounter1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries an Observation, and requests the inclusion of all referenced subjects, which consists of 
//     * resource types: Device, Location, Patient, Group. The Observation contains one referenced patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2" })
//    public void testIncludedDataMultiTarget() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedObservation2.getId().getValue()).append("&_include=Observation:subject");
//        queryParms.put("_id", Collections.singletonList(savedObservation2.getId().getValue()));
//        queryParms.put("_include", Collections.singletonList("Observation:subject"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Observation.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(2, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation2.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the reverse inclusion of an Observation that references the Patient. 
//     * The Observation does contain a referenced patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2", "createObservation3" })
//    public void testRevIncludedData() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient1.getId().getValue()).append("&_revinclude=Observation:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient1.getId().getValue()));
//        queryParms.put("_revinclude", Collections.singletonList("Observation:patient"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertTrue(savedObservation2.getId().getValue().equals(resource.getId().getValue()) ||
//                           savedObservation3.getId().getValue().equals(resource.getId().getValue()));
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient, and requests the reverse inclusion of the resource referred to by Observation.subject.   
//     * Observation.subject can represent multiple resource types. But on a reverse include, the only resource type applicable for subject is the "root" type being 
//     * queried, in this case, Patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2", "createObservation3" })
//    public void testRevIncludedDataMultiTarget() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient1.getId().getValue()).append("&_revinclude=Observation:subject");
//        queryParms.put("_id", Collections.singletonList(savedPatient1.getId().getValue()));
//        queryParms.put("_revinclude", Collections.singletonList("Observation:subject"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertTrue(savedObservation2.getId().getValue().equals(resource.getId().getValue()) ||
//                           savedObservation3.getId().getValue().equals(resource.getId().getValue()));
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices. 
//     * There are 2 Observations that refer back to the Patient. There are no Devices referencing the Patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createObservation2", "createObservation3" })
//    public void testMultiRevInclude() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient1.getId().getValue())
//                   .append("&_revinclude=Observation:patient&_revinclude=Device:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient1.getId().getValue()));
//        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertTrue(savedObservation2.getId().getValue().equals(resource.getId().getValue()) ||
//                           savedObservation3.getId().getValue().equals(resource.getId().getValue()));
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient1.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices. 
//     * There is 1 Observation1 that refers back to the Patient and one Device referencing the Patient.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createDevice1", "createObservation5" })
//    public void testMultiRevInclude1() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient3.getId().getValue())
//                   .append("&_revinclude=Observation:patient&_revinclude=Device:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient3.getId().getValue()));
//        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation5.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Device) {
//                assertEquals(savedDevice1.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient3.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices. 
//     * There is 1 Observation1 that refers back to the Patient. There are two versions of a single Device referencing 
//     * the Patient. The referenced Observation should be returned along with only the latest version of the Device.
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "updateDevice1", "createObservation5" })
//    public void testMultiRevInclude2() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient3.getId().getValue())
//                   .append("&_revinclude=Observation:patient&_revinclude=Device:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient3.getId().getValue()));
//        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation5.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Device) {
//                assertEquals(savedDevice1.getId().getValue(), resource.getId().getValue());
//                assertEquals("2", savedDevice1.getMeta().getVersionId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient3.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the reverse inclusion of referencing Observations and Devices. 
//     * The Patient is NOT referenced from any other resources. 
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient4"})
//    public void testNoRevIncludedData() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient4.getId().getValue())
//                   .append("&_revinclude=Observation:patient&_revinclude=Device:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient4.getId().getValue()));
//        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(1, resources.size());
//        if (resources.get(0) instanceof Patient) {
//            Patient patient = (Patient) resources.get(0);
//            assertEquals(savedPatient4.getId().getValue(), patient.getId().getValue());
//        }
//        else {
//            fail("Unexpected resource type");
//        }
//    }
//    
//    /**
//     * This test queries a Patient and requests the inclusion of the referenced managing organization. Also, it requests the
//     * reverse inclusion of Observations.  
//     *  
//     * @throws Exception
//     */
//    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "createPatient3", "createObservation5"})
//    public void testIncludeAndRevInclude() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        StringBuilder queryString = new StringBuilder();
//        
//        queryString.append("_id=").append(savedPatient3.getId().getValue())
//                   .append("&_include=Patient:organization&_revinclude=Observation:patient");
//        queryParms.put("_id", Collections.singletonList(savedPatient3.getId().getValue()));
//        queryParms.put("_include", Collections.singletonList("Patient:organization"));
//        queryParms.put("_revinclude", Collections.singletonList("Observation:patient"));
//        List<Resource> resources = runQueryTest(queryString.toString(), Patient.class, persistence, queryParms);
//        assertNotNull(resources);
//        assertEquals(3, resources.size());
//        for (Resource resource : resources) {
//            if (resource instanceof Observation) {
//                assertEquals(savedObservation5.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Organization) {
//                assertEquals(savedOrg1.getId().getValue(), resource.getId().getValue());
//            }
//            else if (resource instanceof Patient) {
//                assertEquals(savedPatient3.getId().getValue(), resource.getId().getValue());
//            }
//            else {
//                fail("Unexpected resource type returned.");
//            }
//        }
//         
//    }
        
}
