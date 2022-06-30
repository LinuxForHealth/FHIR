/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.config.FHIRModelConfig;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;
import com.ibm.fhir.search.exception.FHIRSearchException;

/**
 * This class contains a collection of tests that will be run against
 * each of the various persistence layer implementations.
 * There will be a subclass in each persistence project.
 */
public abstract class AbstractCompartmentTest extends AbstractPersistenceTest {
    private static Patient savedPatient;
    private static Device savedDevice;
    private static Encounter savedEncounter;
    private static Practitioner savedPractitioner;
    private static RelatedPerson savedRelatedPerson;
    private static Observation savedObservation;
    private static Observation savedObservation2;
    private static boolean checkReferenceTypes = true;

    /**
     * Builds and saves an Observation with the following references:
     *
     * Observation.subject = Patient
     * Observation.device = Device
     * Observation.encounter = Encounter
     * Observation.performer[0] = Patient
     * Observation.performer[1] = Practitioner
     * Observation.performer[2] = RelatedPerson
     *
     * Builds and saves an Observation with the following logical ID-only reference:
     *
     * Observation.subject = Patient
     */
    @BeforeClass
    public void createResources() throws Exception {
        checkReferenceTypes = FHIRModelConfig.getCheckReferenceTypes();
        FHIRModelConfig.setCheckReferenceTypes(false);
        Observation.Builder observationBuilder = TestUtil.getMinimalResource(Observation.class).toBuilder();
        Observation.Builder observation2Builder = TestUtil.getMinimalResource(Observation.class).toBuilder();

        Patient patient = TestUtil.getMinimalResource(Patient.class);
        savedPatient = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), patient).getResource();
        observationBuilder.subject(buildReference(savedPatient));
        observationBuilder.performer(buildReference(savedPatient));
        // a logical ID-only reference to a patient
        observation2Builder.subject(Reference.builder().reference(string(savedPatient.getId())).build());

        Device device = TestUtil.getMinimalResource(Device.class);
        savedDevice = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), device).getResource();
        observationBuilder.device(buildReference(savedDevice));

        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        savedEncounter = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), encounter).getResource();
        observationBuilder.encounter(buildReference(savedEncounter));

        Practitioner practitioner = TestUtil.getMinimalResource(Practitioner.class);
        savedPractitioner = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), practitioner).getResource();
        observationBuilder.performer(buildReference(savedPractitioner));

        RelatedPerson relatedPerson = TestUtil.getMinimalResource(RelatedPerson.class);
        savedRelatedPerson = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), relatedPerson).getResource();
        observationBuilder.performer(buildReference(savedRelatedPerson));

        savedObservation = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), observationBuilder.build()).getResource();
        assertNotNull(savedObservation);
        assertNotNull(savedObservation.getId());
        assertNotNull(savedObservation.getMeta());
        assertNotNull(savedObservation.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation.getMeta().getVersionId().getValue());

        savedObservation2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), observation2Builder.build()).getResource();
        assertNotNull(savedObservation2);
        assertNotNull(savedObservation2.getId());
        assertNotNull(savedObservation2.getMeta());
        assertNotNull(savedObservation2.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation2.getMeta().getVersionId().getValue());
    }

    @AfterClass
    public void deleteResources() throws Exception {
        Resource[] resources = {savedPatient, savedDevice, savedEncounter, savedPractitioner,
                savedRelatedPerson, savedObservation, savedObservation2};

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

    private Reference buildReference(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());

        String resourceTypeName = ModelSupport.getTypeName(resource.getClass());
        return Reference.builder()
                        .reference(string(resourceTypeName + "/" + resource.getId()))
                        .build();
    }

    @Test
    public void testPatientCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Patient", savedPatient.getId(),
                Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testPatientCompartmentViaLogicalId() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Patient", savedPatient.getId(),
                Observation.class, "_id", savedObservation2.getId());
        assertEquals(0, results.size());
    }

    /**
     * Per https://github.com/LinuxForHealth/FHIR/issues/3091 a Patient resource should be in its own compartment
     */
    @Test
    public void testPatientCompartmentIdentity() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Patient", savedPatient.getId(),
                Patient.class, "_id", savedPatient.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testDeviceCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Device", savedDevice.getId(),
                Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    /**
     * Per https://github.com/LinuxForHealth/FHIR/issues/3091 a Device resource should be in its own compartment.
     * However, Device is the one compartment definition where this resource type isn't valid for its own compartment.
     */
    @Test(expectedExceptions = FHIRSearchException.class)
    public void testDeviceCompartmentIdentity() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Device", savedDevice.getId(),
                Device.class, "_id", savedDevice.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testEncounterCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Encounter", savedEncounter.getId(),
                Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    /**
     * Per https://github.com/LinuxForHealth/FHIR/issues/3091 an Encounter resource should be in its own compartment
     */
    @Test
    public void testEncounterCompartmentIdentity() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Encounter", savedEncounter.getId(),
                Encounter.class, "_id", savedEncounter.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testPractitionerCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Practitioner", savedPractitioner.getId(),
                Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    /**
     * Per https://github.com/LinuxForHealth/FHIR/issues/3091 a Practitioner resource should be in its own compartment
     */
    @Test
    public void testPractitionerCompartmentIdentity() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Practitioner", savedPractitioner.getId(),
                Practitioner.class, "_id", savedPractitioner.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testRelatedPersonCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("RelatedPerson", savedRelatedPerson.getId(),
                Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    /**
     * Per https://github.com/LinuxForHealth/FHIR/issues/3091 a Practitioner resource should be in its own compartment
     */
    @Test
    public void testRelatedPersonCompartmentIdentity() throws Exception {
        List<Resource> results = runCompartmentQueryTest("RelatedPerson", savedRelatedPerson.getId(),
                RelatedPerson.class, "_id", savedRelatedPerson.getId());
        assertEquals(1, results.size());
    }
}
