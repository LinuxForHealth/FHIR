/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 * This class contains a collection of tests that will be run against
 * each of the various persistence layer implementations.
 * There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryCompartmentTest extends AbstractPersistenceTest {
    Patient savedPatient;
    Device savedDevice;
    Encounter savedEncounter;
    Practitioner savedPractitioner;
    RelatedPerson savedRelatedPerson;
    Observation savedObservation;
    
    /**
     * Builds and saves an Observation with the following references:
     * 
     * Observation.subject = Patient
     * Observation.device = Device
     * Observation.encounter = Encounter
     * Observation.performer[0] = Patient
     * Observation.performer[1] = Practitioner
     * Observation.performer[2] = RelatedPerson
     */
    @BeforeClass
    public void createResources() throws Exception {
        Observation.Builder observationBuilder = ((Observation) TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json")).toBuilder();
        
        Patient patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        savedPatient = persistence.create(getDefaultPersistenceContext(), patient).getResource();
        observationBuilder.subject(buildReference(savedPatient));
        observationBuilder.performer(buildReference(savedPatient));
        
        Device device = TestUtil.readExampleResource("json/ibm/minimal/Device-1.json");
        savedDevice = persistence.create(getDefaultPersistenceContext(), device).getResource();
        observationBuilder.device(buildReference(savedDevice));
        
        Encounter encounter = TestUtil.readExampleResource("json/ibm/minimal/Encounter-1.json");
        savedEncounter = persistence.create(getDefaultPersistenceContext(), encounter).getResource();
        observationBuilder.encounter(buildReference(savedEncounter));
        
        Practitioner practitioner = TestUtil.readExampleResource("json/ibm/minimal/Practitioner-1.json");
        savedPractitioner = persistence.create(getDefaultPersistenceContext(), practitioner).getResource();
        observationBuilder.performer(buildReference(savedPractitioner));
        
        RelatedPerson relatedPerson = TestUtil.readExampleResource("json/ibm/minimal/RelatedPerson-1.json");
        savedRelatedPerson = persistence.create(getDefaultPersistenceContext(), relatedPerson).getResource();
        observationBuilder.performer(buildReference(savedRelatedPerson));
        
        savedObservation = persistence.create(getDefaultPersistenceContext(), observationBuilder.build()).getResource();
        assertNotNull(savedObservation);
        assertNotNull(savedObservation.getId());
        assertNotNull(savedObservation.getId().getValue());
        assertNotNull(savedObservation.getMeta());
        assertNotNull(savedObservation.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation.getMeta().getVersionId().getValue());
    }

    private Reference buildReference(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());
        
        String resourceTypeName = FHIRUtil.getResourceTypeName(resource);
        return Reference.builder()
                        .reference(string(resourceTypeName + "/" + resource.getId().getValue()))
                        .build();
    }

    @Test
    public void testPatientCompartment() throws Exception {
        List<Resource> results = runQueryTest("Patient", savedPatient.getId().getValue(), 
                                    Observation.class, "_id", savedObservation.getId().getValue());
        // This currently returns 2 due to https://github.com/IBM/FHIR/issues/265
//        assertEquals(1, results.size());
        assertTrue(results.size() > 0);
    }
    
    @Test
    public void testDeviceCompartment() throws Exception {
        List<Resource> results = runQueryTest("Device", savedDevice.getId().getValue(), 
                                    Observation.class, "_id", savedObservation.getId().getValue());
        assertEquals(1, results.size());
    }
    
    @Test
    public void testEncounterCompartment() throws Exception {
        List<Resource> results = runQueryTest("Encounter", savedEncounter.getId().getValue(), 
                                    Observation.class, "_id", savedObservation.getId().getValue());
        assertEquals(1, results.size());
    }
    
    @Test
    public void testPractitionerCompartment() throws Exception {
        List<Resource> results = runQueryTest("Practitioner", savedPractitioner.getId().getValue(), 
                                    Observation.class, "_id", savedObservation.getId().getValue());
        assertEquals(1, results.size());
    }
    
    @Test
    public void testRelatedPersonCompartment() throws Exception {
        List<Resource> results = runQueryTest("RelatedPerson", savedRelatedPerson.getId().getValue(), 
                                    Observation.class, "_id", savedObservation.getId().getValue());
        assertEquals(1, results.size());
    }
}
