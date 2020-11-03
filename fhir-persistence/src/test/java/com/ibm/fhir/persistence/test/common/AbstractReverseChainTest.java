/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.model.type.code.ResourceType;

/**
 *  This class tests the persistence layer support for the FHIR _has search parameter.
 *  @see https://www.hl7.org/fhir/R4/search.html#has
 */
public abstract class AbstractReverseChainTest extends AbstractPersistenceTest {
    private static Patient savedPatient1;
    private static Patient savedPatient2;
    private static Patient savedPatient3;
    private static Patient savedPatient4;
    private static Observation savedObservation1;
    private static Observation savedObservation2;
    private static Observation savedObservation3;
    private static Observation savedObservation4;
    private static Observation savedObservation5;
    private static Encounter savedEncounter1;
    private static Device savedDevice1;
    private static Organization savedOrg1;
    private static Organization savedOrg2;
    private static Organization savedOrg3;

    /**
     * Loads up and saves a bunch of resources with various references to one another
     */
    @BeforeClass
    public void createResources() throws Exception {
        Organization org = TestUtil.getMinimalResource(ResourceType.ORGANIZATION, Format.JSON);
        Encounter encounter = TestUtil.getMinimalResource(ResourceType.ENCOUNTER, Format.JSON);
        Observation observation = TestUtil.getMinimalResource(ResourceType.OBSERVATION, Format.JSON);
        Patient patient = TestUtil.getMinimalResource(ResourceType.PATIENT, Format.JSON);
        Device device = TestUtil.getMinimalResource(ResourceType.DEVICE, Format.JSON);

        // Organizations that will be referenced by a Patient
        savedOrg1 = persistence.create(getDefaultPersistenceContext(), org).getResource();
        savedOrg2 = persistence.create(getDefaultPersistenceContext(), org).getResource();
        savedOrg3 = persistence.create(getDefaultPersistenceContext(), org).getResource();

        // an Encounter that will be referenced by Observations
        savedEncounter1 = persistence.create(getDefaultPersistenceContext(), encounter).getResource();

        // an Observation that has no references to any other resource types
        savedObservation1 = persistence.create(getDefaultPersistenceContext(), observation).getResource();

        // a Patient that will be referenced by Observations and references an Organization
        savedPatient1 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg2.getId())).build();
        savedPatient1 = persistence.create(getDefaultPersistenceContext(), savedPatient1).getResource();

        // an Observation with a reference to a Patient
        savedObservation2 = observation.toBuilder().subject(reference("Patient/" + savedPatient1.getId())).build();
        savedObservation2 = persistence.create(getDefaultPersistenceContext(), savedObservation2).getResource();

        // an Observation with a reference to a Patient and a reference to an Encounter
        savedObservation3 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient1.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .build();
        savedObservation3 = persistence.create(getDefaultPersistenceContext(), savedObservation3).getResource();

        // a Patient that will be referenced by an Observation and references an Organization
        savedPatient2 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg3.getId())).build();
        savedPatient2 = persistence.create(getDefaultPersistenceContext(), savedPatient2).getResource();
        savedPatient2 = savedPatient2.toBuilder().name(humanName("Vito", "Corleone")).build();
        savedPatient2 = persistence.update(getDefaultPersistenceContext(), savedPatient2.getId(), savedPatient2).getResource();

        // an Observation with a reference to a Patient and a reference to an Encounter
        savedObservation4 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient2.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .value(com.ibm.fhir.model.type.String.of("test"))
                                       .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("code")).build()).build())
                                       .build();
        savedObservation4 = persistence.create(getDefaultPersistenceContext(), savedObservation4).getResource();

        // a Patient that references an Organization and is referenced by an Observation and a Device
        savedPatient3 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg1.getId())).build();
        savedPatient3 = persistence.create(getDefaultPersistenceContext(), savedPatient3).getResource();

        // an Observation with a reference to a Patient
        savedObservation5 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient3.getId()))
                                       .status(ObservationStatus.FINAL)
                                       .build();
        savedObservation5 = persistence.create(getDefaultPersistenceContext(), savedObservation5).getResource();

        // a Device with a reference to a Patient
        savedDevice1 = device.toBuilder().patient(reference("Patient/" + savedPatient3.getId())).build();
        savedDevice1 = persistence.create(getDefaultPersistenceContext(), savedDevice1).getResource();
        savedDevice1 = savedDevice1.toBuilder().manufacturer(string("Updated Manufacturer")).build();
        savedDevice1 = persistence.update(getDefaultPersistenceContext(), savedDevice1.getId(), savedDevice1).getResource();

        // a Patient that will have no other resources referencing it
        savedPatient4 = persistence.create(getDefaultPersistenceContext(), patient).getResource();
    }

    @AfterClass
    public void deleteResources() throws Exception {
        Resource[] resources = {savedPatient1, savedPatient2, savedPatient3, savedPatient4,
                savedObservation1, savedObservation2, savedObservation3, savedObservation4, savedObservation5,
                savedEncounter1, savedDevice1, savedOrg1};

        if (persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }

            try {
                for (Resource resource : resources) {
                    persistence.delete(getDefaultPersistenceContext(), resource.getClass(), resource.getId());
                }
            } catch (Throwable t) {
                if (persistence.isTransactional()) {
                    persistence.getTransaction().setRollbackOnly();
                }
                throw t;
            } finally {
                if (persistence.isTransactional()) {
                    persistence.getTransaction().end();
                }
            }
        }
    }

    /**
     * This test queries for Patients which are referenced by Observations with a specified code.
     * No Observations are found containing the code, thus no Patients are returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainNoResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:code", Collections.singletonList("test"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Patients which are referenced by Observations with a specified status.
     * One observation is found containing the status, thus one Patient is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:status", Collections.singletonList(ObservationStatus.FINAL.getValue()));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Patient", resources.get(0).getClass().getSimpleName());
        assertEquals(savedPatient3.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Patients which are referenced by Observations with a specified encounter.
     * Two observations are found containing the encounter reference, thus two Patients are returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainMultipleResults() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:encounter", Collections.singletonList("Encounter/" + savedEncounter1.getId()));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedPatient1.getId()));
        assertTrue(resourceIds.contains(savedPatient2.getId()));
    }

    /**
     * This test queries for Patients which are referenced by Observations which in turn are
     * referenced by Encounters with a specific status.
     * No Encounters are found, thus no Observations are found, thus no Patients are returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainNoResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:_has:Encounter:reason-reference:status", Collections.singletonList("test"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Organizations which are referenced by Patients which in turn are
     * referenced by Observations with a specified status.
     * One observation is found containing the status, thus one Patient is found, thus one
     * Organization is returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainSingleResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Patient:organization:_has:Observation:subject:status", Collections.singletonList(ObservationStatus.FINAL.getValue()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Organization", resources.get(0).getClass().getSimpleName());
        assertEquals(savedOrg1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Organizations which are referenced by Patients which in turn are
     * referenced by Observations with a specified encounter.
     * Two observations are found containing the encounter, thus two Patients are found, thus two
     * Organizations are returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainMultipleResults() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Patient:organization:_has:Observation:subject:encounter", Collections.singletonList("Encounter/" + savedEncounter1.getId()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedOrg2.getId()));
        assertTrue(resourceIds.contains(savedOrg3.getId()));
    }

    /**
     * This test queries for Patients which are referenced by Observations with an _id that is
     * one of two values.
     * Two observations are found, thus two Patients are returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainMultipleResultsFromMultipleValues() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:_id", Collections.singletonList(savedObservation2.getId() + "," + savedObservation4.getId()));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedPatient1.getId()));
        assertTrue(resourceIds.contains(savedPatient2.getId()));
    }

    /**
     * This test queries for Encounters which are referenced by Observations with an _id that is
     * one of two values.
     * Two observations are found. both referencing the same encounter, thus one Encounter is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleResultsFromMultipleQueries() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:encounter:_id", Arrays.asList(savedObservation3.getId(), savedObservation4.getId()));
        List<Resource> resources = runQueryTest(Encounter.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Encounter", resources.get(0).getClass().getSimpleName());
        assertEquals(savedEncounter1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Encounters which are referenced by Observations with a
     * reference to Patients with a specified name.
     * One patient is found, thus one observation, thus one encounter is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleResultWithChainedParm() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:encounter:subject:Patient.name", Collections.singletonList("Vito"));
        List<Resource> resources = runQueryTest(Encounter.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Encounter", resources.get(0).getClass().getSimpleName());
        assertEquals(savedEncounter1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Encounters which are referenced by Observations
     * matching on a string type search parameter.
     * One observation is found, thus one encounter is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleResultWithStringParm() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:encounter:value-string", Collections.singletonList("test"));
        List<Resource> resources = runQueryTest(Encounter.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Encounter", resources.get(0).getClass().getSimpleName());
        assertEquals(savedEncounter1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Encounters which are referenced by Observations
     * matching on a composite type search parameter.
     * One observation is found, thus one encounter is returned.
     * @throws Exception
     */
//    @Test
//    public void testReverseChainSingleResultWithCompositeParm() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        queryParms.put("_has:Observation:encounter:code-value-string", Collections.singletonList("code$test"));
//         List<Resource> resources = runQueryTest(Encounter.class, queryParms);
//        assertNotNull(resources);
//        assertEquals(1, resources.size());
//        assertEquals("Encounter", resources.get(0).getClass().getSimpleName());
//        assertEquals(savedEncounter1.getId(), resources.get(0).getId());
//    }

    private Reference reference(String reference) {
        return Reference.builder().reference(string(reference)).build();
    }

    private HumanName humanName(String firstName, String lastName) {
        return HumanName.builder().given(string(firstName)).family(string(lastName)).build();
    }
}