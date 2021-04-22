/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
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
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class ProfileValidationConfigTest {

    FHIRPersistence persistence;
    FHIRRestHelper helper;

    public static final OperationOutcome ALL_OK = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text(string("All OK"))
                    .build())
                .build())
            .build();

    @BeforeClass
    void setup() throws FHIRException {
        FHIRConfiguration.setConfigHome("src/test/resources");
        FHIRRequestContext.get().setTenantId("profileValidationConfigTest");
        FHIRRegistry.getInstance().addProvider(new MockRegistryResourceProvider());
        persistence = new MockPersistenceImpl();
        helper = new FHIRRestHelper(persistence);
    }

    @AfterClass
    void tearDown() throws FHIRException {
        FHIRConfiguration.setConfigHome("");
        FHIRRequestContext.get().setTenantId("default");
    }

    /**
     * Test a create with no profile specified but profile required.
     */
    @Test
    public void testCreateWithNoProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with a profile specified, but not a required profile.
     */
    @Test
    public void testCreateWithNonRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with a profile specified, but no version.
     */
    @Test
    public void testCreateWithRequiredProfileSpecifiedButNoVersion() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile2"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with an unsupported profile specified.
     */
    @Test
    public void testCreateWithUnsupportedProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"), Canonical.of("profile5"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("Profile 'profile5' is not supported", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_SUPPORTED, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with a valid required profile specified.
     */
    @Test
    public void testCreateWithRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

    /**
     * Test a create with a valid required profile with version specified.
     */
    @Test
    public void testCreateWithRequiredProfileWithVersionSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1|3"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

    /**
     * Test a create with a valid version specific required profile specified.
     */
    @Test
    public void testCreateWithVersionSpecificRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile2|1"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, true);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

    /**
     * Test a create with no profile specified but default config profile required.
     */
    @Test
    public void testCreateWithNoProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Encounter", encounter, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Encounter' must declare conformance to at least one of the following profiles: [profile4]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with a profile specified, but not a default config required profile.
     */
    @Test
    public void testCreateWithNonRequiredProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"), Canonical.of("badprofile"))
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Encounter", encounter, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Encounter' must declare conformance to at least one of the following profiles: [profile4]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with an unsupported default config profile specified.
     */
    @Test
    public void testCreateWithUnsupportedProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("badprofile"))
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Encounter", encounter, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("Profile 'badprofile' is not supported", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_SUPPORTED, issues.get(0).getCode());
        }
    }

    /**
     * Test a create with a valid default config required profile specified.
     */
    @Test
    public void testCreateWithRequiredProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"))
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Encounter", encounter, null, true);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

    /**
     * Test a create for resource with empty required list.
     */
    @Test
    public void testCreateForResourceWithEmptyRequiredList() throws Exception {
        Procedure procedure = Procedure.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
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

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doCreate("Procedure", procedure, null, true);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test an update with no profile specified but profile required.
     */
    @Test
    public void testUpdateWithNoProfileSpecified() throws Exception {
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

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Patient", "1", patient, null, null, false, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update with a profile specified, but not a required profile.
     */
    @Test
    public void testUpdateWithNonRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Patient", "1", patient, null, null, true, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update with an unsupported profile specified.
     */
    @Test
    public void testUpdateWithUnsupportedProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"), Canonical.of("profile5"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Patient", "1", patient, null, null, false, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("Profile 'profile5' is not supported", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_SUPPORTED, issues.get(0).getCode());
        }
    }

    /**
     * Test an update with a valid required profile specified.
     */
    @Test
    public void testUpdateWithRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Patient", "1", patient, null, null, false, true);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

    /**
     * Test bundle with no profile specified but profile required.
     */
    @Test
    public void testBundleWithNoProfileSpecified() throws Exception {
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
        try {
            helper.doBundle(requestBundle);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(2, issues.size());
            assertEquals("One or more errors were encountered while validating a 'transaction' request bundle.", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.FATAL, issues.get(0).getSeverity());
            assertEquals(IssueType.INVALID, issues.get(0).getCode());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(1).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(1).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(1).getCode());
        }
    }

    /**
     * Test bundle with a profile specified, but not a required profile.
     */
    @Test
    public void testBundleWithNonRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"))
                    .build())
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

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(2, issues.size());
            assertEquals("One or more errors were encountered while validating a 'transaction' request bundle.", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.FATAL, issues.get(0).getSeverity());
            assertEquals(IssueType.INVALID, issues.get(0).getCode());
            assertEquals("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [profile1, profile2|1, profile3]",
                issues.get(1).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(1).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(1).getCode());
        }
    }

    /**
     * Test bundle with an unsupported profile specified.
     */
    @Test
    public void testBundleWithUnsupportedProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"), Canonical.of("profile5"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
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

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(2, issues.size());
            assertEquals("One or more errors were encountered while validating a 'transaction' request bundle.", issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.FATAL, issues.get(0).getSeverity());
            assertEquals(IssueType.INVALID, issues.get(0).getCode());
            assertEquals("Profile 'profile5' is not supported", issues.get(1).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(1).getSeverity());
            assertEquals(IssueType.NOT_SUPPORTED, issues.get(1).getCode());
        }
    }

    /**
     * Test bundle with a valid required profile specified.
     */
    @Test
    public void testBundleWithRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1"))
                    .build())
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
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

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle);
            fail();
        } catch (FHIRValidationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals("An error occurred during validation", e.getMessage());
        }
    }

}
