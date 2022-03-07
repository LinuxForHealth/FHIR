/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.model.type.String.string;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Practitioner.Qualification;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.ResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.interceptor.FHIRPersistenceInterceptorMgr;
import com.ibm.fhir.server.spi.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.test.MockPersistenceImpl;
import com.ibm.fhir.server.test.MockTransactionAdapter;

public class FHIRRestHelperTest {

    public static final OperationOutcome ALL_OK = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text(string("All OK"))
                    .build())
                .build())
            .build();
    public static final OperationOutcome ID_SPECIFIED = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text(string("The create request resource included id: '1'; this id has been replaced"))
                    .build())
                .build())
            .build();
    public static final OperationOutcome NO_NARRATIVE = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.WARNING)
                .code(IssueType.INVARIANT)
                .details(CodeableConcept.builder()
                    .text(string("dom-6: A resource should have narrative for robust management"))
                    .build())
                .expression(string("Patient"))
                .build())
            .build();

    private SearchHelper searchHelper;

    @BeforeClass
    public void initializeSearchUtil() {
        searchHelper = new SearchHelper();
    }

    /**
     * Test transaction bundle post single.
     */
    @Test
    public void testTransactionBundlePostSingle() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ALL_OK);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
        assertEquals(response.getStatus().getValue(), "201");
    }

    /**
     * Test transaction bundle post single with validate warning.
     */
    @Test
    public void testTransactionBundlePostSingleWithValidateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ALL_OK);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
        assertEquals(response.getStatus().getValue(), "201");
        assertEquals(response.getOutcome(), NO_NARRATIVE);
    }

    /**
     * Test transaction bundle post single with create warning.
     */
    @Test
    public void testTransactionBundlePostSingleWithCreateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .id("1")
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ID_SPECIFIED);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
        assertEquals(response.getStatus().getValue(), "201");
    }

    /**
     * Test transaction bundle post single with validate amd create warning.
     */
    @Test
    public void testTransactionBundlePostSingleWithValidateAndCreateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .id("1")
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ID_SPECIFIED);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
        assertEquals(response.getStatus().getValue(), "201");
        assertEquals(response.getOutcome(), NO_NARRATIVE);
    }

    /**
     * Test transaction bundle post with local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePostWithDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results. ".entry(bundleEntry2, bundleEntry)" - Procedure create is processed first, then Patient create
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-1/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-0/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/generated-1");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-1/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/generated-0");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), "201");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-1/_history/1");
                assertEquals(response.getStatus().getValue(), "201");
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-0");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-1/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                // Since issue 1869 this reference can be resolved correctly:
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(5, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/generated-0/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/generated-1");
                assertEquals(returnedEncounter.getReasonReference().get(1).getReference().getValue(), "Condition/generated-4");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-1/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-2");
                assertEquals(returnedProcedure.getReasonReference().get(0).getReference().getValue(), "Condition/generated-4");
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-2/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals(returnedPatient.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/generated-3");
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals(response.getLocation().getValue(), "Practitioner/generated-3/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals(response.getLocation().getValue(), "Condition/generated-4/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals(returnedCondition.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedCondition.getSubject().getReference().getValue(), "Patient/generated-2");
                assertEquals(returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue(), "Procedure/generated-1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .id("1")
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(ALL_OK, entry.getResource());
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals("Patient/1/_history/2", response.getLocation().getValue());
        assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
    }

    /**
     * Test transaction bundle put single with validate warning.
     */
    @Test
    public void testTransactionBundlePutSingleWithValidateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ALL_OK);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/1/_history/2");
        assertEquals(response.getStatus().getValue(), "200");
        assertEquals(response.getOutcome(), NO_NARRATIVE);
    }

    /**
     * Test transaction bundle put single with update warning.
     */
    @Test
    public void testTransactionBundlePutSingleWithUpdateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .id("1")
                .language(Code.of("en-US"))
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ID_SPECIFIED);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/1/_history/2");
        assertEquals(response.getStatus().getValue(), "200");
    }

    /**
     * Test transaction bundle put single with validate and update warning.
     */
    @Test
    public void testTransactionBundlePutSingleWithValidateAndUpdateWarning() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .id("1")
                .language(Code.of("en-US"))
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
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertEquals(entry.getResource(), ID_SPECIFIED);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getLocation().getValue(), "Patient/1/_history/2");
        assertEquals(response.getStatus().getValue(), "200");
        assertEquals(response.getOutcome(), NO_NARRATIVE);
    }

    /**
     * Test transaction bundle put with local reference dependency.
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundlePutWithDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/1/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/2/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/1/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/2");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/2/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        // Interesting that this ends up as a patient search not a read
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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/1/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/2/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/2/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-0");
            } else {
                fail();
            }
        }
    }

    /**
     * Test transaction bundle put with backwards conditional local reference dependency and id not set.
     * Condition has local reference to Patient, which has a conditional update, but Condition gets sorted
     * to first in bundle, so local reference not resolved.
     */
    @Test
    public void testTransactionBundlePutWithBackwardsConditionalDependencyAndIdNotSet() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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

        Condition condition = Condition.builder()
                .id("2")
                .subject(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
               .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Condition/2"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(condition)
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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals(response.getLocation().getValue(), "Condition/2/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Condition returnedCondition = (Condition) entry.getResource();
                // local references to conditional updates are now resolved even if backward dependency and id not set
                assertEquals(returnedCondition.getSubject().getReference().getValue(), "Patient/generated-0");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(2, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/1/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/generated-0");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-0/_history/1");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/1");
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
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(6, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/1/_history/2");
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/generated-0");
                assertEquals(returnedEncounter.getReasonReference().get(1).getReference().getValue(), "Condition/generated-2");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-0/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/1");
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-1");
                assertEquals(returnedProcedure.getReasonReference().get(0).getReference().getValue(), "Condition/generated-2");
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-1/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals(returnedPatient.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/4");
                assertEquals(returnedPatient.getManagingOrganization().getReference().getValue(), "Organization/6");
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals(response.getLocation().getValue(), "Practitioner/4/_history/2");
                assertEquals(Integer.toString(Response.Status.OK.getStatusCode()), response.getStatus().getValue());
                Practitioner returnedPractitioner = (Practitioner) entry.getResource();
                assertEquals(returnedPractitioner.getQualification().get(0).getIssuer().getReference().getValue(), "Organization/6");
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals(response.getLocation().getValue(), "Condition/generated-2/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals(returnedCondition.getEncounter().getReference().getValue(), "Encounter/1");
                assertEquals(returnedCondition.getSubject().getReference().getValue(), "Patient/generated-1");
                assertEquals(returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue(), "Procedure/generated-0");
            } else if (response.getLocation().getValue().startsWith("Organization")) {
                assertEquals(response.getLocation().getValue(), "Organization/6/_history/2");
                assertEquals(response.getStatus().getValue(), Integer.toString(Response.Status.OK.getStatusCode()));
            } else {
                fail();
            }
        }
    }

    /**
     * Test building search bundle with null rsrc and rsrc with no id.
     */
    @Test
    public void testBundleSearchBundleWithNullRsrcAndNoId() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);
        FHIRSearchContext context = FHIRSearchContextFactory.createSearchContext();

        Patient patientNoId = Patient.builder()
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .meta(Meta.builder()
                    .lastUpdated(Instant.now())
                    .versionId(Id.of("1"))
                    .build())
                .build();

        List<ResourceResult<? extends Resource>> resourceResults = new ArrayList<>();
        resourceResults.add(ResourceResult.builder().version(1).build());
        resourceResults.add(ResourceResult.builder().resource(patientNoId).build());
        Bundle responseBundle = helper.createSearchResponseBundle(resourceResults, context, "Patient");

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(3, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(2);
        OperationOutcome operationOutcome = (OperationOutcome) entry.getResource();
        assertEquals(2, operationOutcome.getIssue().size());
        assertEquals("A resource with no data was found.", operationOutcome.getIssue().get(0).getDetails().getText().getValue());
        assertEquals("A resource with no id was found.", operationOutcome.getIssue().get(1).getDetails().getText().getValue());
    }

    /**
     * Ensure delete event contains the required resources.
     */
    @Test
    public void testResourcesInDeleteEvent() throws Exception {
        FHIRPersistenceInterceptor interceptor = new FHIRPersistenceInterceptor() {
            @Override
            public void beforeDelete(FHIRPersistenceEvent event) {
                assertNotNull(event.getPrevFhirResource());
                assertNull(event.getFhirResource());
            }

            @Override
            public void afterDelete(FHIRPersistenceEvent event) {
                try {
                    assertNotNull(event.getPrevFhirResource());
                    int currentVersion = FHIRPersistenceSupport.getMetaVersionId(event.getPrevFhirResource());

                    // The event contains a version of the resource with the lastUpdated time set
                    assertNotNull(event.getFhirResource());
                    assertNotNull(event.getFhirResource().getMeta());
                    assertNotNull(event.getFhirResource().getMeta().getLastUpdated());
                    int newVersion = FHIRPersistenceSupport.getMetaVersionId(event.getFhirResource());
                    assertEquals(newVersion, currentVersion+1);
                } catch (FHIRPersistenceException x) {
                    fail("afterDelete(event)", x);
                }
            }
        };
        FHIRPersistenceInterceptorMgr.getInstance().addInterceptor(interceptor);

        Patient patient = Patient.builder()
            .name(HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build())
            .id("123")
            .meta(Meta.builder()
                .lastUpdated(Instant.now())
                .versionId(Id.of("1"))
                .build())
            .build();

        FHIRPersistence persistence = Mockito.mock(FHIRPersistence.class);
        @SuppressWarnings("unchecked")
        SingleResourceResult<Resource> mockResult = Mockito.mock(SingleResourceResult.class);
        when(mockResult.getResource()).thenReturn(patient);

        when(persistence.generateResourceId()).thenReturn("generated-0");
        when(persistence.getTransaction()).thenReturn(new MockTransactionAdapter());
        when(persistence.read(any(), any(), any())).thenReturn(mockResult);
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        // Call doDelete, the interceptor will check if the events are set
        helper.doDelete("Patient", "123", null);
    }

    /**
     * Ensure delete event contains the required resources.
     */
    @Test
    public void testDeleteDeleted() throws Exception {


        // Mock up the result that the persistence layer will return from its read call
        @SuppressWarnings("unchecked")
        SingleResourceResult<Resource> mockResult = Mockito.mock(SingleResourceResult.class);
        when(mockResult.getResource()).thenReturn(null);
        when(mockResult.getVersion()).thenReturn(2);
        when(mockResult.isDeleted()).thenReturn(true);

        // Mock up the persistence impl
        FHIRPersistence persistence = Mockito.mock(FHIRPersistence.class);
        when(persistence.generateResourceId()).thenReturn("generated-0");
        when(persistence.getTransaction()).thenReturn(new MockTransactionAdapter());
        when(persistence.read(any(), any(), any())).thenReturn(mockResult);
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        // Call doDelete, check that the response contains the version of the deleted resource
        // even though it doesn't contain the resource.
        FHIRRestOperationResponse response = helper.doDelete("Patient", "123", null);
        assertNotNull(response);
        assertNull(response.getResource());
        assertEquals(response.getVersionForETag(), 2);
    }

    /**
     * Test an interceptor that modifies the resource
     */
    @Test
    public void testResourceModifyingInterceptor() throws Exception {
        Coding TAG = Coding.builder().code(Code.of("test")).build();

        FHIRPersistenceInterceptor interceptor = new FHIRPersistenceInterceptor() {
            @Override
            public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
                event.setFhirResource(addTag(event.getFhirResource()));
            }

            @Override
            public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
                event.setFhirResource(addTag(event.getFhirResource()));
            }

            @Override
            public void beforePatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
                event.setFhirResource(addTag(event.getFhirResource()));
            }

            private Resource addTag(Resource r) throws FHIRPersistenceInterceptorException {
                try {
                    Meta.Builder metaBuilder = r.getMeta() != null ? r.getMeta().toBuilder()
                            : Meta.builder().source(Uri.of("interceptor"));
                    return r.toBuilder().meta(metaBuilder.tag(TAG).build()).build();
                } catch (Exception e) {
                    throw new FHIRPersistenceInterceptorException("Unexpected error while adding a tag", e);
                }
            }
        };
        FHIRPersistenceInterceptorMgr.getInstance().addInterceptor(interceptor);

        Patient patientNoId = Patient.builder()
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .build();
        Patient patientWithId = patientNoId.toBuilder()
                .id("123")
                .meta(Meta.builder()
                    .lastUpdated(Instant.now())
                    .versionId(Id.of("1"))
                    .build())
                .build();

        FHIRPersistence persistence = Mockito.mock(FHIRPersistence.class);
        @SuppressWarnings("unchecked")
        SingleResourceResult<Resource> mockResult = Mockito.mock(SingleResourceResult.class);
        when(mockResult.getResource()).thenReturn(patientWithId);

        when(persistence.generateResourceId()).thenReturn("generated-0");
        when(persistence.getTransaction()).thenReturn(new MockTransactionAdapter());
        when(persistence.read(any(), any(), any())).thenReturn(mockResult);
        // when(persistence.create(any(), any())).thenReturn(mockResult);
        when(persistence.create(any(), any())).thenReturn(mockResult);
        when(persistence.update(any(), any())).thenReturn(mockResult);
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        // The helper must pass the resource updated by the interceptor to the persistence#create method
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        helper.doCreate("Patient", patientNoId, null);
        Mockito.verify(persistence).create(any(), patientCaptor.capture());
        assertEquals(patientCaptor.getValue().getMeta().getTag().get(0), TAG);

        helper.doUpdate("Patient", "123", patientWithId, null, null, false, null);
        Mockito.verify(persistence).update(any(), patientCaptor.capture());
        assertEquals(patientCaptor.getValue().getMeta().getTag().get(0), TAG);
    }

    /**
     * Test processing for a batch bundle where the resource does not match
     * the given URL endpoint
     */
    @Test
    public void testBatchBundleResourceUrlMismatch() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Observation")) // should be Patient
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.BATCH)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(1, responseBundle.getEntry().size());
        Bundle.Entry entry = responseBundle.getEntry().get(0);
        assertNotEquals(entry.getResource(), ALL_OK);
        Bundle.Entry.Response response = entry.getResponse();
        assertEquals(response.getStatus().getValue(), "400");
    }

    /**
     * Test transaction bundle post with multiple local reference dependencies. The
     * local references, as well as the fullUrls, are a mix of absolute and relative URLs.
     *
     * Encounter has local references to Procedure and Condition, and a RESTful server fullUrl.
     * The reference to Condition is a relative reference to a bundle entry with a RESTful
     * server fullUrl (match). The reference to Procedure is a relative reference to a bundle entry
     * with a relative fullUrl (no match).
     *
     * Procedure has local references to Patient, Encounter, Condition, and a relative fullUrl.
     * The reference to Patient is a urn reference to a bundle entry with a urn fullUrl (match).
     * The reference to Encounter is an absolute reference to a bundle entry with a RESTful server
     * fullUrl (match). The reference to Condition is a urn reference to a bundle entry with a
     * RESTful server fullUrl (no match).
     *
     * Patient has local reference to Practitioner and a urn fullUrl. The reference to Practitioner
     * is a non-urn, non-RESTful server absolute reference to a bundle entry with a non-urn,
     * non-RESTful server fullUrl (match).
     *
     * Practitioner has no local references and a non-urn, non-RESTful server fullUrl.
     *
     * Condition has local references to Patient, Encounter, Procedure, and a RESTful server
     * fullUrl. The reference to Patient is a urn reference to a bundle entry with a urn
     * fullUrl (match). The reference to Encounter is a relative reference to a bundle entry
     * with a RESTful server fullUrl (match). The reference to Procedure is a urn reference
     * to a bundle entry with a relative fullUrl (no match).
     */
    @Test
    public void testTransactionBundlePostWithAbsoluteFullUrlsMultipleDependency() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("Procedure/1"))
                    .build(),
                    Reference.builder()
                    .reference(string("Condition/1"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("https://test.com/fhir-server/api/v4/Encounter/1"))
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();

        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("https://test.com/fhir-server/api/v4/Encounter/1"))
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
                .fullUrl(Uri.of("Procedure/1"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("https://test.com/test"))
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
                .fullUrl(Uri.of("https://test.com/test"))
                .resource(practitioner)
                .request(bundleEntryRequest4)
                .build();

        Condition condition = Condition.builder()
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("Encounter/1"))
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
                .fullUrl(Uri.of("https://test.com/fhir-server/api/v4/Condition/1"))
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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(5, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/generated-0/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/1");
                assertEquals(returnedEncounter.getReasonReference().get(1).getReference().getValue(), "Condition/generated-4");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-1/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-2");
                assertEquals(returnedProcedure.getReasonReference().get(0).getReference().getValue(), "urn:5");
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-2/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals(returnedPatient.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/generated-3");
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals(response.getLocation().getValue(), "Practitioner/generated-3/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals(response.getLocation().getValue(), "Condition/generated-4/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals(returnedCondition.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedCondition.getSubject().getReference().getValue(), "Patient/generated-2");
                assertEquals(returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue(), "urn:2");
            } else {
                fail();
            }
        }
    }

    /**
     * A copy of testTransactionBundlePostWithAbsoluteFullUrlsMultipleDependency above
     * except that all references use the "resource:" scheme in their fullUrl and references
     */
    @Test
    public void testTransactionBundlePostWithResourceSchemeReference() throws Exception {
        FHIRPersistence persistence = new MockPersistenceImpl();
        FHIRRestHelper helper = new FHIRRestHelper(persistence, searchHelper);

        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("resource:1"))
                    .build(),
                    Reference.builder()
                    .reference(string("resource:4"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("resource:0")) // 0-indexed to match the MockPersistenceImpl's id generator
                .resource(encounter)
                .request(bundleEntryRequest)
                .build();

        Procedure procedure = Procedure.builder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("resource:2"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("resource:0"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("resource:4"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Procedure"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("resource:1"))
                .resource(procedure)
                .request(bundleEntryRequest2)
                .build();

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("resource:3"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest3 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry3 = Bundle.Entry.builder()
                .fullUrl(Uri.of("resource:2"))
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
                .fullUrl(Uri.of("resource:3"))
                .resource(practitioner)
                .request(bundleEntryRequest4)
                .build();

        Condition condition = Condition.builder()
                .subject(Reference.builder()
                    .reference(string("resource:2"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("resource:0"))
                    .build())
                .evidence(Condition.Evidence.builder()
                    .detail(Reference.builder()
                        .reference(string("resource:1"))
                        .build())
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest5 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry5 = Bundle.Entry.builder()
                .fullUrl(Uri.of("resource:4"))
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
        Bundle responseBundle = helper.doBundle(requestBundle, false);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(5, responseBundle.getEntry().size());
        for (Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response.getLocation().getValue().startsWith("Encounter")) {
                assertEquals(response.getLocation().getValue(), "Encounter/generated-0/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Encounter returnedEncounter = (Encounter) entry.getResource();
                assertEquals(returnedEncounter.getReasonReference().get(0).getReference().getValue(), "Procedure/generated-1");
                assertEquals(returnedEncounter.getReasonReference().get(1).getReference().getValue(), "Condition/generated-4");
            } else if (response.getLocation().getValue().startsWith("Procedure")) {
                assertEquals(response.getLocation().getValue(), "Procedure/generated-1/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Procedure returnedProcedure = (Procedure) entry.getResource();
                assertEquals(returnedProcedure.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedProcedure.getSubject().getReference().getValue(), "Patient/generated-2");
                assertEquals(returnedProcedure.getReasonReference().get(0).getReference().getValue(), "Condition/generated-4");
            } else if (response.getLocation().getValue().startsWith("Patient")) {
                assertEquals(response.getLocation().getValue(), "Patient/generated-2/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Patient returnedPatient = (Patient) entry.getResource();
                assertEquals(returnedPatient.getGeneralPractitioner().get(0).getReference().getValue(), "Practitioner/generated-3");
            } else if (response.getLocation().getValue().startsWith("Practitioner")) {
                assertEquals(response.getLocation().getValue(), "Practitioner/generated-3/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
            } else if (response.getLocation().getValue().startsWith("Condition")) {
                assertEquals(response.getLocation().getValue(), "Condition/generated-4/_history/1");
                assertEquals(Integer.toString(Response.Status.CREATED.getStatusCode()), response.getStatus().getValue());
                Condition returnedCondition = (Condition) entry.getResource();
                assertEquals(returnedCondition.getEncounter().getReference().getValue(), "Encounter/generated-0");
                assertEquals(returnedCondition.getEvidence().get(0).getDetail().get(0).getReference().getValue(), "Procedure/generated-1");
                assertEquals(returnedCondition.getSubject().getReference().getValue(), "Patient/generated-2");
            } else {
                fail();
            }
        }
    }
}
