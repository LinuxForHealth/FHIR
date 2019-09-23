/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.impl.FHIRHistoryContextImpl;

/**
 *   This test class was inspired by Defect 195430.
 *   The test replicates the logic of the failing jMeter script referred to in that defect.
 *   The logical flaws in that test, while themselves problematic, also revealed other errors in the update
 *   logic in the Cloudant persistence layer. The purpose of this test is not to test fixes to the flawed jMeter script,
 *   but rather to test fixes to the flaws discovered in the Cloudant persistence layer update() method.
 *   This test should also run cleanly when using JPA persistence.
 */
public abstract class AbstractUpdateGroupTest extends AbstractPersistenceTest {
    private static final Logger log = java.util.logging.Logger.getLogger(AbstractUpdateGroupTest.class.getName());

     /**
     *  This test method does the following:
     *  1. Establishes a "base" empty Group.
     *  2. Creates and simultaneously executes multiple threads, each of which create a number of Patients, and then
     *     adds those patients to the existing Group.
     *  3. Makes some assertions regarding the expected data 'consistency' for the most recent version of the Group
     *     and its prior versions.
     * @throws Exception
     * TODO fix
     */
//    @Test(groups = { "cloudant", "jpa" })
//    public void testUpdateGroup() throws Exception {
//
//        List<Callable<Group>> concurrentUpdates = new ArrayList<>();
//        List<Future<Group>> futureResults = new ArrayList<>();
//        int maxThreads = 10;
//        int patientGroupSize = 10;
//        int patientListSize = 7;
//           List<List<Patient>> patientGroups = null;
//
//           // Initialize multi-thread Executor Service.
//        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
//
//           // Create a new group. Include timestamp in the Group's name
//        Group group = new Group().withMember(new ArrayList<Group.Member>());
//        group.setName(FHIRUtil.string("UpdateGroupTest-"  + FHIRUtilities.formatTimestamp(new Date())));
//        group.setQuantity(new UnsignedInt().withValue(BigInteger.valueOf(0)));
//        persistence.create(getDefaultPersistenceContext(), group);
//        assertNotNull(group);
//        assertNotNull(group.getId());
//        assertNotNull(group.getId().getValue());
//        assertNotNull(group.getMeta());
//        assertNotNull(group.getMeta().getVersionId().getValue());
//        assertEquals("1", group.getMeta().getVersionId().getValue());
//        //log.info("Created Group id=" + group.getId().getValue());
//
//        // Generate groups of Patients to add to group.
//        patientGroups = this.buildPatientGroups(patientGroupSize, patientListSize);
//
//        // Prepare GroupUpdater instances for running each on its own thread.
//        for (List<Patient> patientList : patientGroups) {
//            concurrentUpdates.add(new GroupUpdater(patientList, group.getId().getValue()));
//        }
//
//        // Run each GroupUpdater on its own thread.
//        futureResults = executor.invokeAll(concurrentUpdates);
//        assertNotNull(futureResults);
//
//        // Retrieve the most recent version of the updated group
//        Group updatedGroup = (Group)persistence.read(getDefaultPersistenceContext(), Group.class, group.getId().getValue());
//        //log.info("Final Group: " + "\n" + FHIRUtil.toJsonObject(updatedGroup).toString());
//        assertNotNull(updatedGroup);
//
//        // Do a search using the group's logical id. Make sure only 1 group is returned.
//        // This will ensure that fhirSearchIndex segments
//        // (in the Cloudant persistence implementation) in older versions have been removed.
//        List<Resource> searchGroups = runQueryTest(Group.class, persistence, "_id", updatedGroup.getId().getValue());
//        assertNotNull(searchGroups);
//        assertEquals(1, searchGroups.size());
//        assertEquals(updatedGroup.getId().getValue(), searchGroups.get(0).getId().getValue());
//
//        // Retrieve history and then check:
//        // 1. Check for uniqueVersionIds.
//        // 2. Check that quantity matches the count of members in the group.
//        //FHIRPersistenceContext persistenceContext = this.getDefaultPersistenceContext();
//        FHIRHistoryContext historyContext = new FHIRHistoryContextImpl();
//        historyContext.setPageSize(100);
//        FHIRPersistenceContext persistenceContext = this.getPersistenceContextForHistory(historyContext);
//
//        List<Resource> resourceHistory = persistence.history(persistenceContext, Group.class, updatedGroup.getId().getValue());
//        assertNotNull(resourceHistory);
//        int currentVersionId;
//        int priorVersionId = Integer.MAX_VALUE;
//        for (Resource retrievedResource : resourceHistory) {
//            currentVersionId = Integer.valueOf((retrievedResource.getMeta().getVersionId().getValue()));
//            Group retrievedGroup = (Group) retrievedResource;
//            assertTrue(currentVersionId < priorVersionId);
//            priorVersionId = currentVersionId;
//            assertEquals(retrievedGroup.getQuantity().getValue().intValue(), retrievedGroup.getMember().size());
//        }
//    }

    /**
     * Builds and returns a List of Patient Lists, to be used for creating patients with unique
     * given names on multiple threads.
     * @param groupSize - The maximum number of patient lists to create.
     * @param listSize - The maximum number patients in a list.
     * @return List<List<Patient>> - A List of patient lists.
     * TODO fix
     */
//    private List<List<Patient>> buildPatientGroups(int groupSize, int listSize) {
//
//        Patient patient;
//        String givenName = "Pete";
//        String familyName = "Clemenza-";
//        HumanName generatedName;
//        List<HumanName> generatedNames;
//        List<List<Patient>> patientGroups = new ArrayList<>();
//        List<Patient> patientList;
//
//        for (int i = 0; i < groupSize; i++) {
//            patientList = new ArrayList<>();
//            for (int j = 0; j < listSize; j++) {
//                generatedName = FHIRUtil.humanName(givenName, familyName + (i+1) + ":" + (j+1));
//                generatedNames = new ArrayList<>();
//                generatedNames.add(generatedName);
//                patient = new Patient().withName(generatedNames);
//                patientList.add(patient);
//            }
//            patientGroups.add(patientList);
//        }
//        return patientGroups;
//    }

    /**
     * Instance of this inner class are run on separate threads. Each instance creates a collection of new
     * Patient resources, and then adds those resources to a pre-existing Group.
     */
//    public class GroupUpdater implements Callable<Group> {
//
//        private List<Patient> patients;
//        private String groupId;
//
//        public GroupUpdater(List<Patient> patients, String groupId) {
//            this.patients = patients;
//            this.groupId = groupId;
//        }
//
//        @Override
//        public Group call() throws Exception {
//            Group group;
//            com.ibm.fhir.model.type.String patientRef;
//            com.ibm.fhir.model.type.String patientGivenName;
//            com.ibm.fhir.model.type.String patientFamilyName;
//            com.ibm.fhir.model.type.String patientDisplayName;
//            UnsignedInt oldQuantity;
//            UnsignedInt newQuantity;
//
//            // First persist all of the Patients in the DB. This will populate the id attribute of each patient.
//            for (Patient patient : this.patients) {
//                persistence.create(getDefaultPersistenceContext(), patient);
//            }
//
//            // Add the patients as members of the group.
//            group = (Group)persistence.read(getDefaultPersistenceContext(), Group.class, this.groupId);
//            assertNotNull(group);
//            oldQuantity = group.getQuantity();
//            for (Patient patient : this.patients) {
//                patientRef =  com.ibm.fhir.model.type.String.of("Patient/" + patient.getId().getValue());
//                patientGivenName = patient.getName().get(0).getGiven().get(0);
//                patientFamilyName = patient.getName().get(0).getFamily().get(0);
//                patientDisplayName = com.ibm.fhir.model.type.String.of(
//                                patientGivenName.getValue() + " " + patientFamilyName.getValue());
//                group.getMember().add(new GroupMember()
//                                    .withEntity(
//                                            new Reference().withReference(patientRef)
//                                                           .withDisplay(patientDisplayName)));
//            }
//            newQuantity = new UnsignedInt().withValue
//                                (BigInteger.valueOf(oldQuantity.getValue().intValue() + this.patients.size())) ;
//            group.setQuantity(newQuantity);
//            persistence.update(getDefaultPersistenceContext(), this.groupId, group);
//            //log.info("Added patients to Group id=" + group.getId().getValue() +
//            //         " oldQuantity=" + oldQuantity.getValue() + "  newQuantity=" + newQuantity.getValue());
//            return group;
//        }
//
//    }
}
