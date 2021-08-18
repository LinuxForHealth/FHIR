/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.patch.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.annotations.Test;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

/**
 * Tests against the FHIRPathPatch helper methods in FHIRPathUtil
 */
public class FHIRPathUtilTest {
    Bundle bundle = Bundle.builder().type(BundleType.COLLECTION).build();
    Patient patient = Patient.builder().id("test").build();
    Practitioner practitioner = Practitioner.builder().id("test").build();

    @Test
    private void testAddListResource() throws FHIRPathException, FHIRPatchException {
        Patient modifiedPatient = FHIRPathUtil.add(patient, "Patient", "contained", practitioner);
        assertEquals(modifiedPatient.getContained().get(0), practitioner);
    }

    @Test
    private void testAddSingleResource() throws FHIRPathException, FHIRPatchException {
        Entry emptyEntry = Entry.builder().fullUrl(Uri.of("test")).build();

        Bundle modifiedBundle = FHIRPathUtil.add(bundle, "Bundle", "entry", emptyEntry);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle", "entry", emptyEntry);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle.entry[0]", "resource", patient);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle.entry[1]", "resource", practitioner);

        assertEquals(modifiedBundle.getEntry().get(0).getResource(), patient);
        assertEquals(modifiedBundle.getEntry().get(1).getResource(), practitioner);
    }

    @Test
    private void testConcurrentPatches() throws FHIRPathException, FHIRPatchException, InterruptedException, ExecutionException {
        List<Callable<Bundle>> concurrentUpdates = new ArrayList<>();
        int maxThreads = 12;
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        // 1. Execute a set of adds in parallel.
        Entry addEntry = Entry.builder().fullUrl(Uri.of("add")).build();
        for (int i = 0; i < maxThreads; i++) {
            concurrentUpdates.add(new Add(bundle, addEntry));
        }
        List<Future<Bundle>> futureResults = executor.invokeAll(concurrentUpdates);
        for (Future<Bundle> future : futureResults) {
            assertEquals(future.get().getEntry().size(), 1, "Each bundle should have 1 entry.");
        }


        // 2. Execute a set of inserts in parallel.
        concurrentUpdates = new ArrayList<>();
        Entry insertEntry = Entry.builder().fullUrl(Uri.of("insert")).build();

        for (Future<Bundle> future : futureResults) {
            concurrentUpdates.add(new Insert(future.get(), insertEntry));
        }
        futureResults = executor.invokeAll(concurrentUpdates);
        for (Future<Bundle> future : futureResults) {
            assertEquals(future.get().getEntry().size(), 2, "Each bundle should have 2 entries.");
        }


        // 3. Execute a set of replacements in parallel.
        concurrentUpdates = new ArrayList<>();
        Entry replaceEntry = Entry.builder().fullUrl(Uri.of("replace")).build();

        for (Future<Bundle> future : futureResults) {
            concurrentUpdates.add(new Replace(future.get(), replaceEntry));
        }
        futureResults = executor.invokeAll(concurrentUpdates);
        for (Future<Bundle> future : futureResults) {
            assertEquals(future.get().getEntry().size(), 2, "Each bundle should have 2 entries.");
        }


        // 4. Execute a set of moves in parallel.
        concurrentUpdates = new ArrayList<>();

        for (Future<Bundle> future : futureResults) {
            concurrentUpdates.add(new Move(future.get()));
        }
        futureResults = executor.invokeAll(concurrentUpdates);
        for (Future<Bundle> future : futureResults) {
            assertEquals(future.get().getEntry().size(), 2, "Each bundle should have 2 entries.");
        }


        // 5. Execute a set of deletes in parallel.
        concurrentUpdates = new ArrayList<>();

        for (Future<Bundle> future : futureResults) {
            concurrentUpdates.add(new Delete(future.get()));
        }
        futureResults = executor.invokeAll(concurrentUpdates);
        assertEquals(futureResults.size(), maxThreads);
        for (Future<Bundle> future : futureResults) {
            assertEquals(future.get().getEntry().size(), 1, "Each bundle should have 1 entry.");
        }
    }

    private class Add implements Callable<Bundle> {
        private Bundle bundle;
        private Entry entry;

        Add(Bundle bundle, Entry entry) {
            this.bundle = bundle;
            this.entry = entry;
        }

        @Override
        public Bundle call() throws Exception {
            return FHIRPathUtil.add(bundle, "Bundle", "entry", entry);
        }
    }

    private class Insert implements Callable<Bundle> {
        private Bundle bundle;
        private Entry entry;

        Insert(Bundle bundle, Entry entry) {
            this.bundle = bundle;
            this.entry = entry;
        }

        @Override
        public Bundle call() throws Exception {
            return FHIRPathUtil.insert(bundle, "Bundle.entry", 0, entry);
        }
    }

    private class Replace implements Callable<Bundle> {
        private Bundle bundle;
        private Entry entry;

        Replace(Bundle bundle, Entry entry) {
            this.bundle = bundle;
            this.entry = entry;
        }

        @Override
        public Bundle call() throws Exception {
            return FHIRPathUtil.replace(bundle, "Bundle.entry[0]", entry);
        }
    }

    private class Move implements Callable<Bundle> {
        private Bundle bundle;

        Move(Bundle bundle) {
            this.bundle = bundle;
        }

        @Override
        public Bundle call() throws Exception {
            return FHIRPathUtil.move(bundle, "Bundle.entry", 0, 1);
        }
    }

    private class Delete implements Callable<Bundle> {
        private Bundle bundle;

        Delete(Bundle bundle) {
            this.bundle = bundle;
        }

        @Override
        public Bundle call() throws Exception {
            return FHIRPathUtil.delete(bundle, "Bundle.entry[0]");
        }
    }
}
