/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Reference;

/**
 *  This class tests the persistence layer support for the FHIR _include and _revinclude search result parameters.
 *  @see https://www.hl7.org/fhir/R4/search.html#include
 */
public abstract class AbstractIncludeRevincludeTest extends AbstractPersistenceTest {
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
    private static Organization savedOrg1;

    /**
     * Loads up and saves a bunch of resources with various references to one another
     */
    @BeforeClass
    public void createResources() throws Exception {
        Organization org = TestUtil.readExampleResource("json/ibm/minimal/Organization-1.json");
        Encounter encounter = TestUtil.readExampleResource("json/ibm/minimal/Encounter-1.json");
        Observation observation = TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json");
        Patient patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        Device device = TestUtil.readExampleResource("json/ibm/minimal/Device-1.json");

        // an Organization that will be referenced by a Patient
        savedOrg1 = persistence.create(getDefaultPersistenceContext(), org).getResource();

        // an Encounter that will be used as a reference within an Observation
        savedEncounter1 = persistence.create(getDefaultPersistenceContext(), encounter).getResource();

        // an Observation that has no references to any other resource types
        savedObservation1 = persistence.create(getDefaultPersistenceContext(), observation).getResource();

        // a Patient that will be used as a reference within an Observation
        savedPatient1 = persistence.create(getDefaultPersistenceContext(), patient).getResource();

        // an Observation with a reference to a patient
        savedObservation2 = observation.toBuilder().subject(reference("Patient/" + savedPatient1.getId())).build();
        savedObservation2 = persistence.create(getDefaultPersistenceContext(), savedObservation2).getResource();

        // an Observation with a reference to a patient and a reference to an Encounter
        savedObservation3 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient1.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .build();
        savedObservation3 = persistence.create(getDefaultPersistenceContext(), savedObservation3).getResource();

        // a Patient that will be used as a reference within an Observation
        savedPatient2 = persistence.create(getDefaultPersistenceContext(), patient).getResource();
        // update the patient
        savedPatient2 = savedPatient2.toBuilder().name(humanName("Vito", "Corleone")).build();
        savedPatient2 = persistence.update(getDefaultPersistenceContext(), savedPatient2.getId(), savedPatient2).getResource();

        // an Observation with a reference to a patient and a reference to an Encounter
        savedObservation4 = observation.toBuilder()
                                       .subject(reference("Patient/" + savedPatient2.getId()))
                                       .encounter(reference("Encounter/" + savedEncounter1.getId()))
                                       .build();
        savedObservation4 = persistence.create(getDefaultPersistenceContext(), savedObservation4).getResource();

        // a Patient that will be used as a reference within an Observation and a Device
        // also, it will contain a reference to a managing organization
        savedPatient3 = patient.toBuilder().managingOrganization(reference("Organization/" + savedOrg1.getId())).build();
        savedPatient3 = persistence.create(getDefaultPersistenceContext(), savedPatient3).getResource();

        // an Observation with a reference to a patient
        savedObservation5 = observation.toBuilder().subject(reference("Patient/" + savedPatient3.getId())).build();
        savedObservation5 = persistence.create(getDefaultPersistenceContext(), savedObservation5).getResource();

        // a Device with a reference to a patient
        savedDevice1 = device.toBuilder().patient(reference("Patient/" + savedPatient3.getId())).build();
        savedDevice1 = persistence.create(getDefaultPersistenceContext(), savedDevice1).getResource();
        // update the device
        savedDevice1 = savedDevice1.toBuilder().manufacturer(string("Updated Manufacturer")).build();
        savedDevice1 = persistence.update(getDefaultPersistenceContext(), savedDevice1.getId(), savedDevice1).getResource();

        // a Patient that will have no other resources referencing it
        savedPatient4 = persistence.create(getDefaultPersistenceContext(), patient).getResource();

        // An Observation with versioned references to a device
        savedObservation6 = observation.toBuilder()
                .subject(reference("Device/" + savedDevice1.getId() + "/_history/1"))
                .focus(reference("Device/" + savedDevice1.getId() + "/_history/2"))
                .device(reference("Device/" + savedDevice1.getId() + "/_history/3"))
                .build();
        savedObservation6 = persistence.create(getDefaultPersistenceContext(), savedObservation6).getResource();
    }

    @AfterClass
    public void deleteResources() throws Exception {
        Resource[] resources = {savedPatient1, savedPatient2, savedPatient3, savedPatient4,
                savedObservation1, savedObservation2, savedObservation3, savedObservation4, savedObservation5,
                savedObservation6, savedEncounter1, savedDevice1, savedOrg1};

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
     * This test queries an Observation and requests the inclusion of a referenced Patient. The Observation does NOT
     * contain a referenced patient.
     * @throws Exception
     */
    @Test
    public void testNoIncludedData() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation1.getId()));
        queryParms.put("_include", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries an Observation and requests the inclusion of a referenced Patient.
     * The Observation does contain a referenced patient.
     * @throws Exception
     */
    @Test
    public void testIncludedData() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation2.getId()));
        queryParms.put("_include", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation2.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries an Observation and requests the inclusion of a referenced Device
     * where the reference is versioned and the specified version exists.
     * The Observation does contain a referenced device. The returned resources will
     * contain the specified version of the referenced device.
     * @throws Exception
     */
    @Test
    public void testIncludeWithValidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation6.getId()));
        queryParms.put("_include", Collections.singletonList("Observation:subject:Device"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation6.getId(), resource.getId());
            }
            else if (resource instanceof Device) {
                assertEquals(savedDevice1.getId(), resource.getId());
                assertEquals("1", resource.getMeta().getVersionId().getValue());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries an Observation and requests the inclusion of a referenced Device
     * where the reference is versioned and the specified version does not exist.
     * The Observation does contain a referenced device, but not with the version
     * specified. No included resource will be returned.
     * @throws Exception
     */
    @Test
    public void testIncludeWithInvalidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation6.getId()));
        queryParms.put("_include", Collections.singletonList("Observation:device:Device"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Observation", resources.get(0).getClass().getSimpleName());
        assertEquals(savedObservation6.getId(), resources.get(0).getId());
    }

    /**
     * This test queries an Observation and requests the inclusion of a referenced Patient and a referenced Encounter.
     * The Observation only contains a referenced patient.
     * @throws Exception
     */
    @Test
    public void testMultiInclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation2.getId()));
        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation2.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
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
    @Test
    public void testMultiInclude1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation3.getId()));
        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation3.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else if (resource instanceof Encounter) {
                assertEquals(savedEncounter1.getId(), resource.getId());
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
    @Test
    public void testMultiInclude2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation4.getId()));
        queryParms.put("_include", Arrays.asList(new String[] {"Observation:patient", "Observation:encounter"}));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation4.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient2.getId(), resource.getId());
                assertEquals("2", savedPatient2.getMeta().getVersionId().getValue());
            }
            else if (resource instanceof Encounter) {
                assertEquals(savedEncounter1.getId(), resource.getId());
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
    @Test
    public void testIncludedDataMultiTarget() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedObservation2.getId()));
        queryParms.put("_include", Collections.singletonList("Observation:subject"));
        List<Resource> resources = runQueryTest(Observation.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation2.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Patient and requests the reverse inclusion of an Observation that references the Patient.
     * The Observation does contain a referenced patient.
     * @throws Exception
     */
    @Test
    public void testRevIncludedData() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient1.getId()));
        queryParms.put("_revinclude", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertTrue(savedObservation2.getId().equals(resource.getId()) ||
                           savedObservation3.getId().equals(resource.getId()));
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Device and requests the reverse inclusion of an Observation that references the Device
     * where the reference is a versioned reference which specifies the device's current version.
     * The Observation does contain a referenced device.
     * @throws Exception
     */
    @Test
    public void testRevIncludeWithValidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedDevice1.getId()));
        queryParms.put("_revinclude", Collections.singletonList("Observation:focus"));
        List<Resource> resources = runQueryTest(Device.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation6.getId(), resource.getId());
            }
            else if (resource instanceof Device) {
                assertEquals(savedDevice1.getId(), resource.getId());
                assertEquals("2", resource.getMeta().getVersionId().getValue());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Device and requests the reverse inclusion of an Observation that references the Device
     * where the reference is a versioned reference which does not specify the device's current version.
     * The Observation does contain a referenced device, but the version is not the current version so the
     * Observation is not included.
     * @throws Exception
     */
    @Test
    public void testRevIncludeWithInvalidVersionedReference() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedDevice1.getId()));
        queryParms.put("_revinclude", Collections.singletonList("Observation:subject"));
        List<Resource> resources = runQueryTest(Device.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Device", resources.get(0).getClass().getSimpleName());
        assertEquals(savedDevice1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries a Patient, and requests the reverse inclusion of the resource referred to by Observation.subject.
     * Observation.subject can represent multiple resource types. But on a reverse include, the only resource type applicable for subject is the "root" type being
     * queried, in this case, Patient.
     * @throws Exception
     */
    @Test
    public void testRevIncludedDataMultiTarget() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient1.getId()));
        queryParms.put("_revinclude", Collections.singletonList("Observation:subject"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertTrue(savedObservation2.getId().equals(resource.getId()) ||
                           savedObservation3.getId().equals(resource.getId()));
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices.
     * There are 2 Observations that refer back to the Patient. There are no Devices referencing the Patient.
     * @throws Exception
     */
    @Test
    public void testMultiRevInclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient1.getId()));
        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertTrue(savedObservation2.getId().equals(resource.getId()) ||
                           savedObservation3.getId().equals(resource.getId()));
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient1.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices.
     * There is 1 Observation1 that refers back to the Patient and one Device referencing the Patient.
     * @throws Exception
     */
    @Test
    public void testMultiRevInclude1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient3.getId()));
        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation5.getId(), resource.getId());
            }
            else if (resource instanceof Device) {
                assertEquals(savedDevice1.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient3.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Patient and requests the reverse inclusion of referenced Observations and referenced Devices.
     * There is 1 Observation1 that refers back to the Patient. There are two versions of a single Device referencing
     * the Patient. The referenced Observation should be returned along with only the latest version of the Device.
     * @throws Exception
     */
    @Test
    public void testMultiRevInclude2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient3.getId()));
        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation5.getId(), resource.getId());
            }
            else if (resource instanceof Device) {
                assertEquals(savedDevice1.getId(), resource.getId());
                assertEquals("2", savedDevice1.getMeta().getVersionId().getValue());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient3.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    /**
     * This test queries a Patient and requests the reverse inclusion of referencing Observations and Devices.
     * The Patient is NOT referenced from any other resources.
     * @throws Exception
     */
    @Test
    public void testNoRevIncludedData() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient4.getId()));
        queryParms.put("_revinclude", Arrays.asList(new String[] {"Observation:patient", "Device:patient"}));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        if (resources.get(0) instanceof Patient) {
            Patient patient = (Patient) resources.get(0);
            assertEquals(savedPatient4.getId(), patient.getId());
        }
        else {
            fail("Unexpected resource type");
        }
    }

    /**
     * This test queries a Patient and requests the inclusion of the referenced managing organization.
     * Also, it requests the reverse inclusion of Observations.
     * @throws Exception
     */
    @Test
    public void testIncludeAndRevInclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedPatient3.getId()));
        queryParms.put("_include", Collections.singletonList("Patient:organization"));
        queryParms.put("_revinclude", Collections.singletonList("Observation:patient"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                assertEquals(savedObservation5.getId(), resource.getId());
            }
            else if (resource instanceof Organization) {
                assertEquals(savedOrg1.getId(), resource.getId());
            }
            else if (resource instanceof Patient) {
                assertEquals(savedPatient3.getId(), resource.getId());
            }
            else {
                fail("Unexpected resource type returned.");
            }
        }
    }

    private void checkIncludeAndRevIncludeResources(List<Resource> resources, int numOfPatients) {
        HashSet<String> foundPatientOrgIds = new HashSet<String>();
        HashSet<String> foundPatientIds = new HashSet<String>();
        int numOfMatchedOrgs = 0;
        for (Resource resource : resources) {
            if (resource instanceof Patient) {
                if (((Patient)resource).getManagingOrganization() != null) {
                    foundPatientOrgIds.add(((Patient)resource).getManagingOrganization()
                            .getReference().getValue().substring(13));
                }
                foundPatientIds.add(resource.getId());
            }
        }
        // verify the number of patients in the result page.
        assertTrue(numOfPatients == foundPatientIds.size());
        for (Resource resource : resources) {
            if (resource instanceof Observation) {
                // verify the returned observations are related to the patients of the same page
                assertTrue(foundPatientIds.contains(((Observation)resource).getSubject()
                        .getReference().getValue().substring(8)));
            } else if (resource instanceof Organization) {
                // verify the returned managing organizations are related to the patients of the same page
                assertTrue(foundPatientOrgIds.contains(resource.getId()));
                numOfMatchedOrgs++;
            } else if (resource instanceof Patient) {
                assertTrue(foundPatientIds.contains(resource.getId()));
            } else {
                fail("Unexpected resource type returned.");
            }
        }
        // verify all related managing organizations are in the same page.
        assertTrue(numOfMatchedOrgs == foundPatientOrgIds.size());
    }
    /**
     * This test queries page of Patient with inclusion of the referenced managing organizations.
     * and also requests the reverse inclusion of Observations.
     * <p>It verifies:
     * <ol>
     * <li>the number of patients in the result page;
     * <li>the returned managing organizations and observations are related to the patients of the same page;
     * <li>all related managing organizations are in the same page.
     * </ol>
     * @throws Exception
     */
    @Test
    public void testIncludeAndRevIncludeMultiplePatients() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include", Collections.singletonList("Patient:organization"));
        queryParms.put("_revinclude", Collections.singletonList("Observation:patient"));
        queryParms.put("_count", Collections.singletonList("2"));
        queryParms.put("_page", Collections.singletonList("1"));
        List<Resource> resources = runQueryTest(Patient.class, queryParms);
        assertNotNull(resources);
        // check the first page
        checkIncludeAndRevIncludeResources(resources, 2);
        queryParms.put("_page", Collections.singletonList("2"));
        resources = runQueryTest(Patient.class, queryParms);
        // check the second page
        checkIncludeAndRevIncludeResources(resources, 2);
    }

    private Reference reference(String reference) {
        return Reference.builder().reference(string(reference)).build();
    }

    private HumanName humanName(String firstName, String lastName) {
        return HumanName.builder().given(string(firstName)).family(string(lastName)).build();
    }
}