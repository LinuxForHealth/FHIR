/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Practitioner.Qualification;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.server.util.FHIRRestHelper;

public class FHIRRestHelperTest {

    /**
     * Test transaction bundle post single.
     */
    @Test
    public void testTransactionBundlePostSingle() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
        assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
    }

    /**
     * Test transaction bundle post with local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePostWithDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/generated-0", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with backward local reference dependency.
     * Procedure has local reference to Patient, but Procedure is first in bundle.
     */
    @Test
    public void testTransactionBundlePostWithBackwardDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2, bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/generated-0", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with circular local reference dependency.
     * Procedure has local reference to Encounter and Encounter has local reference to Procedure.
     */
    @Test
    public void testTransactionBundlePostWithCircularDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("Patient/1"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/generated-1", returnedEncounter.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/generated-0", returnedProcedure.getEncounter().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with conditional local reference dependency.
     * Procedure has local reference to Patient, which has a conditional create.
     */
    @Test
    public void testTransactionBundlePostWithConditionalDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .ifNoneExist(string("_id=1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/generated-0", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with backwards conditional local reference dependency.
     * Procedure has local reference to Patient, which has a conditional create,
     * but Procedure is first in bundle.
     */
    @Test
    public void testTransactionBundlePostWithBackwardsConditionalDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .ifNoneExist(string("_id=1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2, bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                // local references to conditional creates not found if backward dependency
                assertEquals("urn:1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with multiple local reference dependencies.
     * Encounter has local references to Procedure, Condition.
     * Procedure has local references to Patient, Encounter, Condition
     * Patient has local reference to Practitioner.
     * Practitioner has no local references.
     * Condition has local references to Patient, Encounter, Procedure.
     */
    @Test
    public void testTransactionBundlePostWithMultipleDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build(),
                    Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("urn:4"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest3 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry3 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:3"))
                .resource(patient)
                .request(bundleEntryRequest3)
                .build();
        
        Practitioner practitioner = Practitioner.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest4 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Practitioner"))
                .build();
        Bundle.Entry bundleEntry4 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:4"))
                .resource(practitioner)
                .request(bundleEntryRequest4)
                .build();
        
        Condition condition = Condition.builder()
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .evidence(Condition.Evidence.builder()
                    .detail(Reference.builder()
                        .reference(string("urn:2"))
                        .build())
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest5 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry5 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:5"))
                .resource(condition)
                .request(bundleEntryRequest5)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2, bundleEntry3, bundleEntry4, bundleEntry5)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(5, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/generated-1", returnedEncounter.getReasonReference().get(0).getReference().getValue());
                assertEquals("Condition/generated-4", returnedEncounter.getReasonReference().get(1).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/generated-0", returnedProcedure.getEncounter().getReference().getValue());
                assertEquals("Patient/generated-2", returnedProcedure.getSubject().getReference().getValue());
                assertEquals("Condition/generated-4", returnedProcedure.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-2/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals("Practitioner/generated-3", returnedPatient.getGeneralPractitioner().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals("Practitioner/generated-3/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals("Condition/generated-4/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals("Encounter/generated-0", returnedCondition.getEncounter().getReference().getValue());
                assertEquals("Patient/generated-2", returnedCondition.getSubject().getReference().getValue());
                assertEquals("Procedure/generated-1", returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle post with put local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePostWithPutDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put single.
     */
    @Test
    public void testTransactionBundlePutSingle() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals("Patient/1/_history/2", response.getLocation().getValue());
        assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
    }

    /**
     * Test transaction bundle put with local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePutWithDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with backward local reference dependency.
     * Procedure has local reference to Patient, but Procedure is first in bundle.
     */
    @Test
    public void testTransactionBundlePutWithBackwardDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2, bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with circular local reference dependency.
     * Procedure has local reference to Encounter and Encounter has local reference to Procedure.
     */
    @Test
    public void testTransactionBundlePutWithCircularDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Encounter/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("Patient/1"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/2", returnedEncounter.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/1", returnedProcedure.getEncounter().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with conditional local reference dependency and id set.
     * Procedure has local reference to Patient, which has a conditional update.
     */
    @Test
    public void testTransactionBundlePutWithConditionalDependencyAndIdSet() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient?_id=1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with conditional dependency and id not set.
     * Procedure has local reference to Patient, which has a conditional update.
     */
    @Test
    public void testTransactionBundlePutWithConditionalDependencyAndIdNotSet() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient?_id=1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/generated-0", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with backwards conditional local reference dependency and id not set.
     * Procedure has local reference to Patient, which has a conditional update, but Procedure is first in bundle.
     */
    @Test
    public void testTransactionBundlePutWithBackwardsConditionalDependencyAndIdNotSet() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient?_id=1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2, bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                // local references to conditional updates not found if backward dependency and id not set 
                assertEquals("urn:1", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with multiple local reference dependencies.
     * Encounter has local references to Procedure, Condition.
     * Procedure has local references to Patient, Encounter, Condition
     * Patient has local reference to Practitioner.
     * Practitioner has no local references.
     * Condition has local references to Patient, Encounter, Procedure.
     */
    @Test
    public void testTransactionBundlePutWithMultipleDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build(),
                    Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Encounter/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Patient patient = Patient.builder()
                .id("3")
                .generalPractitioner(Reference.builder()
                    .reference(string("urn:4"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest3 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/3"))
                .build();
        Bundle.Entry bundleEntry3 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:3"))
                .resource(patient)
                .request(bundleEntryRequest3)
                .build();
        
        Practitioner practitioner = Practitioner.builder()
                .id("4")
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest4 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Practitioner/4"))
                .build();
        Bundle.Entry bundleEntry4 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:4"))
                .resource(practitioner)
                .request(bundleEntryRequest4)
                .build();
        
        Condition condition = Condition.builder()
                .id("5")
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .evidence(Condition.Evidence.builder()
                    .detail(Reference.builder()
                        .reference(string("urn:2"))
                        .build())
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest5 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Condition/5"))
                .build();
        Bundle.Entry bundleEntry5 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:5"))
                .resource(condition)
                .request(bundleEntryRequest5)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2, bundleEntry3, bundleEntry4, bundleEntry5)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(5, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/2", returnedEncounter.getReasonReference().get(0).getReference().getValue());
                assertEquals("Condition/5", returnedEncounter.getReasonReference().get(1).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/1", returnedProcedure.getEncounter().getReference().getValue());
                assertEquals("Patient/3", returnedProcedure.getSubject().getReference().getValue());
                assertEquals("Condition/5", returnedProcedure.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/3/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals("Practitioner/4", returnedPatient.getGeneralPractitioner().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals("Practitioner/4/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals("Condition/5/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals("Encounter/1", returnedCondition.getEncounter().getReference().getValue());
                assertEquals("Patient/3", returnedCondition.getSubject().getReference().getValue());
                assertEquals("Procedure/2", returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with post local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePutWithPostDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Patient patient = Patient.builder()
                .id("1")
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Procedure/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/2/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Patient/generated-0", returnedProcedure.getSubject().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with post circular local reference dependency.
     * Procedure has local reference to Encounter and Encounter has local reference to Procedure.
     */
    @Test
    public void testTransactionBundlePutWithPostCircularDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Encounter/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .id("2")
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("Patient/1"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/generated-0", returnedEncounter.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/1", returnedProcedure.getEncounter().getReference().getValue());
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put and post with multiple local reference dependencies.
     * Encounter has local references to Procedure, Condition.
     * Procedure has local references to Patient, Encounter, Condition
     * Patient has local references to Practitioner, Organization.
     * Practitioner has local reference to Organization.
     * Condition has local references to Patient, Encounter, Procedure.
     * Organization has no local references.
     */
    @Test
    public void testTransactionBundlePutAndPostWithMultipleDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence);
        
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build(),
                    Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Encounter/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();
        
        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();
        
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("urn:4"))
                    .build())
                .managingOrganization(Reference.builder()
                    .reference(string("urn:6"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest3 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry3 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:3"))
                .resource(patient)
                .request(bundleEntryRequest3)
                .build();
        
        Practitioner practitioner = Practitioner.builder()
                .id("4")
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .qualification(Qualification.builder()
                    .code(CodeableConcept.builder()
                        .coding(Coding.builder()
                            .code(Code.of("MD"))
                            .system(Uri.of("test"))
                            .build())
                        .build())
                    .issuer(Reference.builder()
                        .reference(string("urn:6"))
                        .build())
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest4 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Practitioner/4"))
                .build();
        Bundle.Entry bundleEntry4 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:4"))
                .resource(practitioner)
                .request(bundleEntryRequest4)
                .build();
        
        Condition condition = Condition.builder()
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .evidence(Condition.Evidence.builder()
                    .detail(Reference.builder()
                        .reference(string("urn:2"))
                        .build())
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest5 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry5 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:5"))
                .resource(condition)
                .request(bundleEntryRequest5)
                .build();
        
        Organization organization = Organization.builder()
                .id("6")
                .name(string("test"))
                .build();
        Bundle.Entry.Request bundleEntryRequest6 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Organization/6"))
                .build();
        Bundle.Entry bundleEntry6 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:6"))
                .resource(organization)
                .request(bundleEntryRequest6)
                .build();
        
        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2, bundleEntry3, bundleEntry4, bundleEntry5, bundleEntry6)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.REPRESENTATION);
        Bundle responseBundle = helper.doBundle(requestBundle, null);
        
        // Validate results
        assertNotNull(responseBundle);
        assertEquals(6, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals("Encounter/1/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals("Procedure/generated-0", returnedEncounter.getReasonReference().get(0).getReference().getValue());
                assertEquals("Condition/generated-2", returnedEncounter.getReasonReference().get(1).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals("Procedure/generated-0/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals("Encounter/1", returnedProcedure.getEncounter().getReference().getValue());
                assertEquals("Patient/generated-1", returnedProcedure.getSubject().getReference().getValue());
                assertEquals("Condition/generated-2", returnedProcedure.getReasonReference().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals("Patient/generated-1/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals("Practitioner/4", returnedPatient.getGeneralPractitioner().get(0).getReference().getValue());
                assertEquals("Organization/6", returnedPatient.getManagingOrganization().getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals("Practitioner/4/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Practitioner returnedPractitioner = (Practitioner) entry.getResource();
                assertEquals("Organization/6", returnedPractitioner.getQualification().get(0).getIssuer().getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals("Condition/generated-2/_history/1", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals("Encounter/1", returnedCondition.getEncounter().getReference().getValue());
                assertEquals("Patient/generated-1", returnedCondition.getSubject().getReference().getValue());
                assertEquals("Procedure/generated-0", returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue());
            } else if (response.getLocation().getValue().startsWith("Organization")) {
                assertEquals("Organization/6/_history/2", response.getLocation().getValue());
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
            } else {
                fail();
            }
        }
    }

}