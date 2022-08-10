/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.test.common;

import static org.linuxforhealth.fhir.model.type.Code.code;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.type.Uri.uri;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.config.FHIRModelConfig;
import org.linuxforhealth.fhir.model.resource.Device;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.ObservationStatus;
import org.linuxforhealth.fhir.persistence.util.FHIRPersistenceTestSupport;

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
    private static Observation savedObservation6;
    private static Encounter savedEncounter1;
    private static Device savedDevice1;
    private static Device savedDevice2;
    private static Organization savedOrg1;
    private static Organization savedOrg2;
    private static Organization savedOrg3;
    private static Library savedLibrary1;
    private static boolean checkReferenceTypes = true;
    private static Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    /**
     * Loads up and saves a bunch of resources with various references to one another
     */
    @BeforeClass
    public void createResources() throws Exception {
        checkReferenceTypes = FHIRModelConfig.getCheckReferenceTypes();
        FHIRModelConfig.setCheckReferenceTypes(false);
        Organization organization = TestUtil.getMinimalResource(Organization.class);
        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        Observation observation = TestUtil.getMinimalResource(Observation.class);
        Patient patient = TestUtil.getMinimalResource(Patient.class);
        Device device = TestUtil.getMinimalResource(Device.class);
        Library library = TestUtil.getMinimalResource(Library.class);
        Coding uniqueTag = Coding.builder().system(uri("http://example.com/fhir/tag")).code(code(now.toString())).build();
        Coding uniqueSecurity = Coding.builder().system(uri("http://example.com/fhir/security")).code(code(now.toString())).build();

        startTrx();
        // Organizations that will be referenced by a Patient
        savedOrg1 = organization.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.of(true)).build();
        savedOrg1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedOrg1).getResource();
        savedOrg2 = organization.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.of(true)).name(org.linuxforhealth.fhir.model.type.String.of("org2")).build();
        savedOrg2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedOrg2).getResource();
        savedOrg3 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), organization).getResource();
        savedOrg3 = savedOrg3.toBuilder().name(org.linuxforhealth.fhir.model.type.String.of("org3")).build();
        savedOrg3 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), savedOrg3.getId(), savedOrg3).getResource();

        // an Encounter that will be referenced by Observations
        savedEncounter1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), encounter).getResource();

        // an Observation that has no references to any other resource types
        savedObservation1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), observation).getResource();

        // a Patient that will be referenced by Observations and references an Organization
        savedPatient1 = patient.toBuilder()
                .meta(Meta.builder()
                    .tag(uniqueTag)
                    .security(uniqueSecurity)
                    .profile(Canonical.of("http://example.com/fhir/profile/" + now.toString())).build())
                .managingOrganization(reference("Organization/" + savedOrg2.getId()))
                .build();
        savedPatient1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedPatient1).getResource();

        // an Observation with a reference to a Patient and a logical ID-only reference to another observation
        savedObservation2 = observation.toBuilder()
                                        .subject(reference("Patient/" + savedPatient1.getId()))
                                        .hasMember(reference(savedObservation1.getId()))
                                        .build();
        savedObservation2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedObservation2).getResource();

        // an Observation with a reference to a Patient and a reference to an Encounter
        savedObservation3 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient1.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .build();
        savedObservation3 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedObservation3).getResource();

        // a Patient that will be referenced by an Observation and references an Organization
        savedPatient2 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg3.getId())).build();
        savedPatient2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedPatient2).getResource();
        savedPatient2 = savedPatient2.toBuilder().name(humanName("Vito", "Corleone")).build();
        savedPatient2 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), savedPatient2.getId(), savedPatient2).getResource();

        // an Observation with a reference to a Patient and a reference to an Encounter
        savedObservation4 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient2.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .value(org.linuxforhealth.fhir.model.type.String.of("test"))
                                       .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("code")).build()).build())
                                       .build();
        savedObservation4 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedObservation4).getResource();

        // a Patient that references an Organization and is referenced by an Observation and a Device
        savedPatient3 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg1.getId())).build();
        savedPatient3 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedPatient3).getResource();

        // a Library that is referenced by an Observation
        savedLibrary1 = library.toBuilder().url(Uri.of("http://example.com/fhir/Library/abc")).version(string("1.0")).build();
        savedLibrary1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedLibrary1).getResource();

        // an Observation with a reference to a Patient and a Library
        savedObservation5 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient3.getId()))
                                       .status(ObservationStatus.FINAL)
                                       .focus(reference("Library/" + savedLibrary1.getId()))
                                       .build();
        savedObservation5 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedObservation5).getResource();

        // a Device with a reference to a Patient and a versioned reference to an Organization
        savedDevice1 = device.toBuilder().patient(reference("Patient/" + savedPatient3.getId())).build();
        savedDevice1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedDevice1).getResource();
        savedDevice1 = savedDevice1.toBuilder()
                                   .manufacturer(string("Updated Manufacturer"))
                                   .owner(reference("Organization/" + savedOrg3.getId() + "/_history/2"))
                                   .build();
        savedDevice1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), savedDevice1.getId(), savedDevice1).getResource();

        // a Device with a versioned reference to an Organization
        savedDevice2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), device).getResource();
        savedDevice2 = savedDevice2.toBuilder().owner(reference("Organization/" + savedOrg3.getId() + "/_history/1")).build();
        savedDevice2 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), savedDevice2.getId(), savedDevice2).getResource();

        // a Patient that will have no other resources referencing it
        savedPatient4 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), patient).getResource();

        // An Observation with versioned references to a Device
        savedObservation6 = observation.toBuilder()
                .subject(reference("Device/" + savedDevice1.getId() + "/_history/1"))
                .focus(reference("Device/" + savedDevice1.getId() + "/_history/2"))
                .device(reference("Device/" + savedDevice2.getId() + "/_history/2"))
                .build();
        savedObservation6 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), savedObservation6).getResource();
        commitTrx();
    }

    @AfterClass
    public void deleteResources() throws Exception {
        Resource[] resources = {savedPatient1, savedPatient2, savedPatient3, savedPatient4,
                savedObservation1, savedObservation2, savedObservation3, savedObservation4, savedObservation5,
                savedObservation6, savedEncounter1, savedDevice1, savedDevice2, savedOrg1, savedOrg2, savedOrg3,
                savedLibrary1};

        if (persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }

            try {
                for (Resource resource : resources) {
                    FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), resource);
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
        FHIRModelConfig.setCheckReferenceTypes(checkReferenceTypes);
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
     * This test queries for Organizations which are referenced by Patients with a specified profile.
     * One patient is found containing the profile, thus one Organization is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainWithProfile() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Patient:organization:_profile", Collections.singletonList("http://example.com/fhir/profile/" + now.toString()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Organization", resources.get(0).getClass().getSimpleName());
        assertEquals(savedOrg2.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Organizations which are referenced by Patients with a specified tag.
     * One patient is found containing the tag, thus one Organization is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainWithTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Patient:organization:_tag", Collections.singletonList("http://example.com/fhir/tag|" + now.toString()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Organization", resources.get(0).getClass().getSimpleName());
        assertEquals(savedOrg2.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Organizations which are referenced by Patients with a specified security.
     * One patient is found containing the security, thus one Organization is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainWithSecurity() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Patient:organization:_security", Collections.singletonList("http://example.com/fhir/security|" + now.toString()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Organization", resources.get(0).getClass().getSimpleName());
        assertEquals(savedOrg2.getId(), resources.get(0).getId());
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
    @Test
    public void testReverseChainSingleResultWithCompositeParm() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:encounter:code-value-string", Collections.singletonList("code$test"));
         List<Resource> resources = runQueryTest(Encounter.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Encounter", resources.get(0).getClass().getSimpleName());
        assertEquals(savedEncounter1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Patients which are referenced by Devices
     * matching on a _lastUpdated search parameter.
     * One device is found, thus one patient is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleResultWithLastUpdatedParm() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Device:patient:_lastUpdated", Collections.singletonList("gt" + now.toString()));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Patient", resources.get(0).getClass().getSimpleName());
        assertEquals(savedPatient3.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Devices which are referenced by Observations with a specified _id
     * where the reference is versioned and the specified version is the current version of
     * the device being referenced.
     * One observation is found, thus one Device is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleWithValidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:focus:_id", Collections.singletonList(savedObservation6.getId()));
        List<Resource> resources = runQueryTest(Device.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Device", resources.get(0).getClass().getSimpleName());
        assertEquals(savedDevice1.getId(), resources.get(0).getId());
        assertEquals("2", resources.get(0).getMeta().getVersionId().getValue());
    }

    /**
     * This test queries for Devices which are referenced by Observations with a specified _id
     * where the reference is versioned and the specified version is not the current version of
     * the device being referenced.
     * One observation is found, but the reference version is not the current version,
     * thus no Device is returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleWithInvalidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:subject:_id", Collections.singletonList(savedObservation6.getId()));
        List<Resource> resources = runQueryTest(Device.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Organizations which are referenced by Devices where the reference
     * is versioned and the specified version is the current version of the organization, and in turn
     * the device is referenced by Observations with a specified _id where the reference
     * is versioned and the specified version is the current version of the device.
     * One observation is found, with a valid versioned reference, thus one Device is found, thus one
     * Organization is returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainSingleWithValidVersionedReferences() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Device:organization:_has:Observation:focus:_id", Collections.singletonList(savedObservation6.getId()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Organization", resources.get(0).getClass().getSimpleName());
        assertEquals(savedOrg3.getId(), resources.get(0).getId());
        assertEquals("2", resources.get(0).getMeta().getVersionId().getValue());
    }

    /**
     * This test queries for Organizations which are referenced by Devices where the reference
     * is versioned and the specified version is not the current version of the organization, and in turn
     * the device is referenced by Observations with a specified _id where the reference
     * is versioned and the specified version is the current version of the device.
     * One observation is found, with a valid versioned reference, thus one Device is found, but
     * the Device's reference version is not the current version of the Organization, thus no
     * Organization is returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainSingleWithInvalidSecondVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Device:organization:_has:Observation:device:_id", Collections.singletonList(savedObservation6.getId()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Organizations which are referenced by Devices where the reference
     * is versioned and the specified version is the current version of the organization, and in turn
     * the device is referenced by Observations with a specified _id where the reference
     * is versioned and the specified version is not the current version of the device.
     * One observation is found, with a reference version that is not the current version of the
     * Device, this no Device is found, thus no Organization is returned.
     * @throws Exception
     */
    @Test
    public void testMultiReverseChainSingleWithInvalidFirstVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Device:organization:_has:Observation:subject:_id", Collections.singletonList(savedObservation6.getId()));
        List<Resource> resources = runQueryTest(Organization.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Observations which reference Patients with a specified name.
     * No Patients are found containing the name, thus no Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainNoResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.name", Collections.singletonList("test"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Observations which reference Patients with a specified name.
     * One patient is found containing the name, thus one Observation is returned.
     * @throws Exception
     */
    @Test
    public void testChainSingleResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.name", Collections.singletonList("Vito"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation4.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Observations which reference Patients with a specified organization.
     * Two patients are found containing the organization reference, thus two Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainMultipleResults() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.organization", Collections.singletonList("Organization/" + savedOrg2.getId()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
    }

    /**
     * This test queries for Observations which reference Patients with a specified profile.
     * One patient is found containing the profile, thus two Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainWithProfile() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient._profile", Collections.singletonList("http://example.com/fhir/profile/" + now.toString()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
    }

    /**
     * This test queries for Observations which reference Patients with a specified tag.
     * One patient is found containing the tag, thus two Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainWithTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient._tag", Collections.singletonList("http://example.com/fhir/tag|" + now.toString()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
    }

    /**
     * This test queries for Observations which reference Patients with a specified security.
     * One patient is found containing the security, thus two Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainWithSecurity() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient._security", Collections.singletonList("http://example.com/fhir/security|" + now.toString()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
    }

    /**
     * This test queries for Observations which reference Libraries with a specified url.
     * One library is found containing the url, thus one Observation is returned.
     * @throws Exception
     */
    @Test
    public void testChainWithUrl() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("focus:Library.url", Collections.singletonList("http://example.com/fhir/Library/abc|1.0"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation5.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Observations which reference Patients which in turn
     * reference Organizations with a specific name.
     * No Organizations are found, thus no Patients are found, thus no Observations are returned.
     * @throws Exception
     */
    @Test
    public void testMultiChainNoResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.organization.name", Collections.singletonList("test"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Observations which reference Patients which in turn
     * reference Organizations with a specific name.
     * One organization is found containing the name, thus one Patient is found, thus one
     * Observation is returned.
     * @throws Exception
     */
    @Test
    public void testMultiChainSingleResult() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.organization.name", Collections.singletonList("org3"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation4.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Observations which reference Patients which in turn
     * reference Organizations with an active status of true.
     * Two organizations are found, thus two Patients are found, thus three
     * Observations are returned.
     * @throws Exception
     */
    @Test
    public void testMultiChainMultipleResults() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.organization.active", Collections.singletonList("true"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
        assertTrue(resourceIds.contains(savedObservation5.getId()));
    }

    /**
     * This test queries for Observations which reference Patients which in turn
     * reference Organizations with a name which is one of two values.
     * Two organizations are found, thus two Patients are found, thus three
     * Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainMultipleResultsFromMultipleValues() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("patient:Patient.organization.name", Collections.singletonList("org2,org3"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> resourceIds = new ArrayList<>();
        for (Resource resource : resources) {
            resourceIds.add(resource.getId());
        }
        assertTrue(resourceIds.contains(savedObservation2.getId()));
        assertTrue(resourceIds.contains(savedObservation3.getId()));
        assertTrue(resourceIds.contains(savedObservation4.getId()));
    }

    /**
     * This test queries for Observations which reference Devices with a specified _id
     * where the reference is versioned and the specified version is not the current version of
     * the device being referenced.
     * One device is found, thus one Observations is returned.
     * @throws Exception
     */
    @Test
    public void testChainSingleWithVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("subject:Device._id", Collections.singletonList(savedDevice1.getId()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation6.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Observations which reference Observations with a specified id.
     * There is one Observation which references the Observation with the specified id, but
     * the reference is a logical ID only, thus no Observations are returned.
     * @throws Exception
     */
    @Test
    public void testChainSingleWithLogicalIdReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("has-member:Observation._id", Collections.singletonList(savedObservation1.getId()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    /**
     * This test queries for Observations which are referenced by Observations with a specified id.
     * There is one Observation which is referenced by the Observation with the specified id, but
     * the reference is a logical ID only, thus no Observations are returned.
     * @throws Exception
     */
    @Test
    public void testReverseChainSingleWithLogicalIdReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Observation:has-member:_id", Collections.singletonList(savedObservation2.getId()));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size());
    }

    private Reference reference(String reference) {
        return Reference.builder().reference(string(reference)).build();
    }

    private HumanName humanName(String firstName, String lastName) {
        return HumanName.builder().given(string(firstName)).family(string(lastName)).build();
    }
}