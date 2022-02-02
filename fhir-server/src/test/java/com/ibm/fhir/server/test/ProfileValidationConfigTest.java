/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Device;
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
import com.ibm.fhir.model.type.code.CarePlanIntent;
import com.ibm.fhir.model.type.code.CarePlanStatus;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRRestHelper;

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

    public static final OperationOutcome PROFILE_NOT_SUPPORTED = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.WARNING)
                .code(IssueType.NOT_SUPPORTED)
                .details(CodeableConcept.builder()
                    .text(string("Profile 'profile6' is not supported"))
                    .build())
                .expression("CarePlan")
                .build())
            .build();

    @BeforeClass
    void setup() throws FHIRException {
        FHIRConfiguration.setConfigHome("target/test-classes");
        FHIRRequestContext.get().setTenantId("profileValidationConfigTest");
        FHIRRegistry.getInstance().addProvider(new MockRegistryResourceProvider());
        persistence = new MockPersistenceImpl();
        helper = new FHIRRestHelper(persistence, new SearchHelper(), FHIRVersionParam.VERSION_43);
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
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
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
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
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
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with an unsupported profile specified and allow unknown true.
     */
    @Test
    public void testCreateWithUnsupportedProfileSpecifiedAndAllowUnknownTrue() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile7"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            FHIRRestOperationResponse response = helper.doCreate("CarePlan", carePlan, null, true);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile7' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
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
        } catch (FHIROperationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
        }
    }

    /**
     * Test a create with a valid required profile with version specified.
     */
    @Test
    public void testCreateWithRequiredProfileWithVersionSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile1|1"))
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
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
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
        } catch (FHIROperationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
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
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                "A required profile was not specified. Resources of type 'Encounter' must declare conformance to at least one of the following profiles: [profile4]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
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
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                "A required profile was not specified. Resources of type 'Encounter' must declare conformance to at least one of the following profiles: [profile4]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
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
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
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
        } catch (FHIROperationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
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
            assertEquals(response.getOperationOutcome(), ALL_OK);
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
            helper.doUpdate("Patient", "1", patient, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
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
            helper.doUpdate("Patient", "1", patient, null, null, true, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test an update with an unsupported profile specified and allow unknown true.
     */
    @Test
    public void testUpdateWithUnsupportedProfileSpecifiedAndAllowUnknownTrue() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile7"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            FHIRRestOperationResponse response = helper.doUpdate("CarePlan", "1", carePlan, null, null, false, true, null);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile7' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
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
            helper.doUpdate("Patient", "1", patient, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results.
            // Profile assertion validation successful.
            // Expected FHIRValidator error due to profile not actually being loaded.
            assertEquals(e.getMessage(), "Error validating resource.");
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertTrue(issues.get(1).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertTrue(issues.get(1).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test bundle with an unsupported profile specified and allow unknown true.
     */
    @Test
    public void testBundleWithUnsupportedProfileSpecifiedAndAllowUnknownTrue() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile7"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("CarePlan/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(carePlan)
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
            Bundle responseBundle = helper.doBundle(requestBundle, false);
            assertEquals(responseBundle.getEntry().get(0).getResource().as(OperationOutcome.class), ALL_OK);
            List<Issue> issues = responseBundle.getEntry().get(0).getResponse().getOutcome().as(OperationOutcome.class).getIssue();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile7' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
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
            Bundle responseBundle = helper.doBundle(requestBundle, false);
            assertEquals(responseBundle.getEntry().get(0).getResource().as(OperationOutcome.class), ALL_OK);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a create with a profile specified, but not an allowed profile.
     */
    @Test
    public void testCreateWithNonAllowedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("CarePlan", carePlan, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with a versioned profile specified, but not an allowed profile.
     */
    @Test
    public void testCreateWithNonAllowedVersionedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8|1"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("CarePlan", carePlan, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with a non-versioned profile specified for a versioned non-allowed profile.
     */
    @Test
    public void testCreateWithNonVersionedProfileSpecifiedForVersionedNonAllowedProfile() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile6"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            FHIRRestOperationResponse response = helper.doCreate("CarePlan", carePlan, null, true);
            assertEquals(response.getOperationOutcome(), PROFILE_NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a create with a profile specified, but a not allowed default config profile.
     */
    @Test
    public void testCreateWithNonAllowedProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile7"))
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
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                "A profile was specified which is not allowed. Resources of type 'Encounter' are not allowed to declare conformance to any of the following profiles: [profile7]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create for resource with empty not allowed list.
     */
    @Test
    public void testCreateForResourceWithEmptyNotAllowedList() throws Exception {
        Condition condition = Condition.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doCreate("Condition", condition, null, true);
            assertEquals(response.getOperationOutcome(), ALL_OK);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test an update with a profile specified, but not an allowed profile.
     */
    @Test
    public void testUpdateWithNonAllowedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doUpdate("CarePlan", "1", carePlan, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test an update with a versioned profile specified, but not an allowed profile.
     */
    @Test
    public void testUpdateWithNonAllowedVersionedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8|1"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doUpdate("CarePlan", "1", carePlan, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertTrue(issues.get(0).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test an update with a non-versioned profile specified for a versioned non-allowed profile.
     */
    @Test
    public void testUpdateWithNonVersionedProfileSpecifiedForVersionedNonAllowedProfile() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile6"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
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
            FHIRRestOperationResponse response = helper.doUpdate("CarePlan", "1", carePlan, null, null, false, true, null);
            assertEquals(response.getOperationOutcome(), PROFILE_NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test an update with a profile specified, but a not allowed default config profile.
     */
    @Test
    public void testUpdateWithNonAllowedProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile7"))
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
            helper.doUpdate("Encounter", "1", encounter, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                "A profile was specified which is not allowed. Resources of type 'Encounter' are not allowed to declare conformance to any of the following profiles: [profile7]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test an update for resource with empty not allowed list.
     */
    @Test
    public void testUpdateForResourceWithEmptyNotAllowedList() throws Exception {
        Condition condition = Condition.builder()
                .id("1")
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doUpdate("Condition", "1", condition, null, null, false, true, null);
            assertEquals(response.getOperationOutcome(), ALL_OK);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a bundle with a profile specified, but not an allowed profile.
     */
    @Test
    public void testBundleWithNonAllowedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("CarePlan"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(carePlan)
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertTrue(issues.get(1).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a bundle with a versioned profile specified, but not an allowed profile.
     */
    @Test
    public void testBundleWithNonAllowedVersionedProfileSpecified() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile8|1"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("CarePlan"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(carePlan)
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertTrue(issues.get(1).getDetails().getText().getValue().startsWith(
                "A profile was specified which is not allowed. Resources of type 'CarePlan' are not allowed to declare conformance to any of the following profiles: ["));
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a bundle with a non-versioned profile specified for a versioned non-allowed profile.
     */
    @Test
    public void testBundleWithNonVersionedProfileSpecifiedForVersionedNonAllowedProfile() throws Exception {
        CarePlan carePlan = CarePlan.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile6"))
                    .build())
                .status(CarePlanStatus.COMPLETED)
                .intent(CarePlanIntent.PLAN)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("CarePlan/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(carePlan)
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
            Bundle responseBundle = helper.doBundle(requestBundle, false);
            assertEquals(responseBundle.getEntry().get(0).getResource().as(OperationOutcome.class), ALL_OK);
            assertEquals(responseBundle.getEntry().get(0).getResponse().getOutcome().as(OperationOutcome.class), PROFILE_NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a bundle with a profile specified, but a not allowed default config profile.
     */
    @Test
    public void testBundleWithNonAllowedProfileSpecifiedDefault() throws Exception {
        Encounter encounter = Encounter.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile7"))
                    .build())
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
                .resource(encounter)
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(),
                "A profile was specified which is not allowed. Resources of type 'Encounter' are not allowed to declare conformance to any of the following profiles: [profile7]");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a bundle for resource with empty not allowed list.
     */
    @Test
    public void testBundleForResourceWithEmptyNotAllowedList() throws Exception {
        Condition condition = Condition.builder()
                .id("1")
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Condition/1"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(condition)
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
            Bundle responseBundle = helper.doBundle(requestBundle, false);
            assertEquals(responseBundle.getEntry().get(0).getResource().as(OperationOutcome.class), ALL_OK);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a create with a profile specified, but not an allowed profile or required profile.
     */
    @Test
    public void testCreateWithNonAllowedNonRequiredProfileSpecified() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile5"))
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
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                "A profile was specified which is not allowed. Resources of type 'Patient' are not allowed to declare conformance to any of the following profiles: [profile5]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
            assertTrue(issues.get(1).getDetails().getText().getValue().startsWith(
                "A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: ["));
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testCreateWithUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile9"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("AllergyIntolerance", allergyIntolerance, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        }
    }

    /**
     * Test a create with no required and an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testCreateWithNonRequiredAndUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile9"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("AllergyIntolerance", allergyIntolerance, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
            assertEquals(issues.get(1).getDetails().getText().getValue(),
                    "A required profile was not specified. Resources of type 'AllergyIntolerance' must declare conformance to at least one of the following profiles: [profile4]");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with not allowed and an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testCreateWithNonAllowedAndUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile9"), Canonical.of("profile7"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("AllergyIntolerance", allergyIntolerance, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 3);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
            assertEquals(issues.get(1).getDetails().getText().getValue(),
                    "A profile was specified which is not allowed. Resources of type 'AllergyIntolerance' are not allowed to declare conformance to any of the following profiles: [profile7]");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
            assertEquals(issues.get(2).getDetails().getText().getValue(), "Profile 'profile7' is not supported");
            assertEquals(issues.get(2).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(2).getCode(), IssueType.NOT_SUPPORTED);
        }
    }

    /**
     * Test a create with not allowed and not required and an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testCreateWithNonAllowedAndNotRequiredAndUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile9"), Canonical.of("profile7"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("AllergyIntolerance", allergyIntolerance, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 4);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
            assertEquals(issues.get(1).getDetails().getText().getValue(),
                    "A profile was specified which is not allowed. Resources of type 'AllergyIntolerance' are not allowed to declare conformance to any of the following profiles: [profile7]");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.BUSINESS_RULE);
            assertEquals(issues.get(2).getDetails().getText().getValue(), "Profile 'profile7' is not supported");
            assertEquals(issues.get(2).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(2).getCode(), IssueType.NOT_SUPPORTED);
            assertEquals(issues.get(3).getDetails().getText().getValue(),
                    "A required profile was not specified. Resources of type 'AllergyIntolerance' must declare conformance to at least one of the following profiles: [profile4]");
            assertEquals(issues.get(3).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(3).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test an update with an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testUpdateWithUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .id("1")
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile9"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doUpdate("AllergyIntolerance", "1", allergyIntolerance, null, null, false, true, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        }
    }

    /**
     * Test a bundle with an unsupported profile specified and allowUnknown false.
     */
    @Test
    public void testBundleWithUnsupportedProfileSpecifiedAndAllowUnknownFalse() throws Exception {
        AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"), Canonical.of("profile9"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("AllergyIntolerance"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .resource(allergyIntolerance)
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
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Profile 'profile9' is not supported");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(1).getCode(), IssueType.NOT_SUPPORTED);
        }
    }

    /**
     * Test a create with a valid default version required profile specified which is unsupported.
     */
    @Test
    public void testCreateWithRequiredProfileAndDefaultVersionSpecifiedAndUnsupported() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile3"))
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
            FHIRRestOperationResponse response = helper.doCreate("Patient", patient, null, true);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile3|2' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a create with a valid default version required profile specified for versioned profile.
     */
    @Test
    public void testCreateWithRequiredProfileAndDefaultVersionSpecifiedForVersionedProfile() throws Exception {
        Device device = Device.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile10"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            FHIRRestOperationResponse response = helper.doCreate("Device", device, null, true);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile10|1' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test a create with a default version not allowed profile specified for versioned profile.
     */
    @Test
    public void testCreateWithNonAllowedProfileAndDefaultVersionSpecifiedForVersionedProfile() throws Exception {
        Device device = Device.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile10"), Canonical.of("profile11"))
                    .build())
                .patient(Reference.builder()
                    .reference(string("urn:3"))
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
            helper.doCreate("Device", device, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(),
                    "A profile was specified which is not allowed. Resources of type 'Device' are not allowed to declare conformance to any of the following profiles: [profile11|1]");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.BUSINESS_RULE);
        }
    }

    /**
     * Test a create with a default version unsupported profile and allow unknown false.
     */
    @Test
    public void testCreateWithUnsupportedProfileDefaultVersionAndAllowUnknownFalse() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("profile4"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Condition", condition, null, true);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Profile 'profile4|2' is not supported");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
            assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
        }
    }

}
