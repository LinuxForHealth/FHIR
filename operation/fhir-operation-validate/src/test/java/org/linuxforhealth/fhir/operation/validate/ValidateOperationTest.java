/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.validate;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.server.util.FHIRRestHelper;

/**
 * Tests the Java code for the ValidateOperation
 */
public class ValidateOperationTest {

    private ValidateOperation validateOperation;
    private FHIRRestHelper resourceHelper = new FHIRRestHelper(null, null);

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
        validateOperation = new ValidateOperation();
    }

    /**
     * Test validate with no resource.
     */
    @Test
    public void testNoResource() {
        try {
            Parameters input = Parameters.builder()
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Input parameter 'resource' must be present unless the mode is 'delete' or 'profile'");
        }
    }

    /**
     * Test validate with no asserted profile.
     */
    @Test
    public void testNoAssertedProfile() {
        try {
            Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build())
                    .build();

            // No profile asserted, so no validation errors - expected output is All OK
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.INFORMATION)
                    .code(IssueType.INFORMATIONAL)
                    .details(CodeableConcept.builder()
                        .text("All OK")
                        .build())
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    /**
     * Test validate with unsupported asserted profile.
     */
    @Test
    public void testAssertedProfileUnsupportedWarning() {
        try {
            Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .meta(Meta.builder()
                                .profile(Canonical.of("unsupported"))
                                .build())
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build())
                    .build();

            // Profile asserted that is unsupported - expected output is warning
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.WARNING)
                    .code(IssueType.NOT_SUPPORTED)
                    .details(CodeableConcept.builder()
                        .text("Profile 'unsupported' is not supported")
                        .build())
                    .expression("Patient")
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    /**
     * Test validate with asserted profile and create mode and 'atLeastOne' fail.
     */
    @Test
    public void testAssertedProfileAtLeastOneFail() {
        try {
            Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .meta(Meta.builder()
                                .profile(Canonical.of("unsupported"))
                                .build())
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build(),
                        Parameter.builder()
                            .name("mode")
                            .value(Code.of("create"))
                            .build())
                    .build();

            // Since 'mode' parameter is specified with value of 'create', expect to validate
            // against server config profile properties - expected output is 'atLeastOne' error
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.ERROR)
                    .code(IssueType.BUSINESS_RULE)
                    .details(CodeableConcept.builder()
                        .text("A required profile was not specified. Resources of type 'Patient' must declare conformance "
                                + "to at least one of the following profiles: [atLeastOne]")
                        .build())
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    /**
     * Test validate with asserted profile and update mode and 'notAllowed' fail.
     */
    @Test
    public void testAssertedProfileNotAllowedFail() {
        try {
            Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .meta(Meta.builder()
                                .profile(Canonical.of("atLeastOne"), Canonical.of("notAllowed"))
                                .build())
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build(),
                        Parameter.builder()
                            .name("mode")
                            .value(Code.of("update"))
                            .build())
                    .build();

            // Since 'mode' parameter is specified with value of 'update', expect to validate
            // against server config profile properties - expected output is 'notAllowed' error
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.ERROR)
                    .code(IssueType.BUSINESS_RULE)
                    .details(CodeableConcept.builder()
                        .text("A profile was specified which is not allowed. Resources of type 'Patient' are not allowed "
                                + "to declare conformance to any of the following profiles: [notAllowed]")
                        .build())
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    /**
     * Test validate with unsupported profile parameter profile.
     */
    @Test
    public void testProfileParmProfileUnsupportedError() {
        try {
            Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .meta(Meta.builder()
                                .profile(Canonical.of("unsupportedAsserted"))
                                .build())
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build(),
                        Parameter.builder()
                            .name("profile")
                            .value(Uri.of("unsupported"))
                            .build())
                    .build();

            // Since 'profile' parameter is specified, asserted profile should be ignored and only 'profile'
            // value should be validated against - expected output is unsupported error for 'profile' value
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.ERROR)
                    .code(IssueType.NOT_SUPPORTED)
                    .details(CodeableConcept.builder()
                        .text("Profile 'unsupported' is not supported")
                        .build())
                    .expression("Patient")
                    .build();

            FHIROperationContext operationContext = mock(FHIROperationContext.class);
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    /**
     * Test resource-instance level validate operation when the resource with the input logicalID is available in the database.
     * @throws Exception
     */
    @Test
    public void testResourceInstanceLevelValidate() throws Exception {
        Patient patient = Patient.builder().id("test")
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(patient);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);

        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);
        Parameters input =  Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build())
                .build();

        Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.INFORMATION)
                    .code(IssueType.INFORMATIONAL)
                    .details(CodeableConcept.builder()
                        .text("All OK")
                        .build())
                    .build();
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "test", null, input, resourceHelper, null);
        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test resource-instance level validate operation when the resource with the input logicalId already exists in the database.
     * @throws Exception
     */
    @Test
    public void testResourceInstanceLevelValidateForAlreadyExistingResource() throws Exception {
        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult result = mock(SingleResourceResult.class);
        Patient patient = Patient.builder()
                .id("1")
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // mock and return null when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(false);
        when(result.getResource()).thenReturn(patient);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);

        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("1")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("create"))
                        .build())
                .build();

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text("Patient with id '1' already exists")
                    .build())
                .build();

        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation with a invalid mode type code input.
     * The validate operation will fail with FHIROperationException
     * @throws FHIROperationException
     */
    @Test(expectedExceptions = { FHIROperationException.class } , expectedExceptionsMessageRegExp  = ".*'random' is not a valid resource validation mode*")
    public void testInvalidValidModeTypes() throws FHIROperationException {
           Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build(),
                        Parameter.builder()
                            .name("mode")
                            .value(Code.of("random"))
                            .build())
                    .build();

           FHIROperationContext operationContext = mock(FHIROperationContext.class);
           validateOperation.doInvoke(operationContext, Patient.class, null, null, input, resourceHelper, null);

    }

    /**
     * Test validate operation with delete mode type code.
     * Validate the outcome when the persistence layer implementation does not support the "delete" operation
     *
     */
    @Test
    public void testValidateOperationDeleteNotSupported() throws FHIROperationException {

           Parameters input = Parameters.builder()
                    .parameter(Parameter.builder()
                        .name("resource")
                        .resource(Patient.builder()
                            .text(Narrative.builder()
                                .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                                .status(NarrativeStatus.GENERATED)
                                .build())
                            .build())
                        .build(),
                        Parameter.builder()
                            .name("mode")
                            .value(Code.of("delete"))
                            .build())
                    .build();

           // mock the persistence layer implementation not to support the "delete" operation
           FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true, false);

           Issue expectedOutput = Issue.builder()
                   .severity(IssueSeverity.ERROR)
                   .code(IssueType.NOT_SUPPORTED)
                   .details(CodeableConcept.builder()
                           .text("Resource deletion, of type 'Patient', with id '1', is not supported.")
                           .build())
                   .build();
           Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);
           OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
           assertEquals(operationOutcome.getIssue().size(), 1);
           assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation with delete mode type code.
     * Validate the outcome when the persistence layer implementation supports the "delete" operation
     *
     */
    @Test
    public void testValidateOperationDeleteSupported() throws FHIROperationException {

        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .meta(Meta.builder()
                            .profile(Canonical.of("atLeastOne"), Canonical.of("notAllowed"))
                            .build())
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("delete"))
                        .build())
                .build();

        // mock the persistence layer implementation to support the "delete" operation
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true, true);

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text("All OK")
                    .build())
                .build();

        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }


    /**
     * Test validate operation with update mode type code and  "update/create" enabled.
     * Validate the outcome when the persistence layer implementation supports the "update/create" operation
     * @throws Exception
     */
    @Test
    public void testValidateOperationWithUpdateCreateEnabled() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("1")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text("All OK")
                    .build())
                .build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation at the instance level with mode=update where the passed resource
     * is missing the Resource.id element.
     * @throws Exception
     */
    @Test
    public void testValidateOperationWithMissingId() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text("Input resource 'id' field is not set")
                    .build())
                .build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation at the instance level with mode=update where the passed resource
     * has a Resource.id element which doesn't match the id passed in the URL.
     * @throws Exception
     */
    @Test
    public void testValidateOperationWithWrongId() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("xyz")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text("Input resource 'id' field must match the 'id' path parameter.")
                    .build())
                .build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation with update mode type code and "update/create" disabled.
     * Validate the outcome when the persistence layer implementation does not support the "update/create" operation
     * @throws Exception
     */
    @Test
    public void testValidateOperationWithUpdateCreateDisabled() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("1")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        // mock the persistence layer implementation not to support the "update/create" operation
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(false);

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        Issue expectedOutput = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.NOT_SUPPORTED)
                .details(CodeableConcept.builder()
                    .text("Resource update, of type 'Patient', is not supported.")
                    .build())
                .build();

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }

    /**
     * Test validate operation with create mode type code and when the resource parameter is null.
     * @throws Exception
     */
    @Test(expectedExceptions = { FHIROperationException.class } , expectedExceptionsMessageRegExp  = ".*Input parameter 'resource' must be present unless the mode is 'delete' or 'profile'*")
    public void testValidateOperationWithNoResourceParameter() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("mode")
                    .value(Code.of("create"))
                    .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);

        validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);
    }

    /**
     * Test validate operation with resource type level and update mode type code.
     * @throws Exception
     */
    @Test(expectedExceptions = { FHIROperationException.class } , expectedExceptionsMessageRegExp  = ".*Modes update and delete can only be used when the operation is invoked at the resource instance level.*")
    public void testValidateOperationWithTypeLevel() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        FHIROperationContext operationContext =
                FHIROperationContext.createResourceTypeOperationContext("validate");

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);
    }

    /**
     * Test validate operation for valid interaction when mode = update and no resource already exists in DB.
     * @throws Exception
     */
    @Test
    public void testValidateOperationDisAllowedInteractionForUpdateMode() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("1")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("update"))
                        .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        FHIRResourceHelpers fhirResourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult<?> result = mock(SingleResourceResult.class);

        // mock and return a resource when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(null);
        when(fhirResourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        Issue expectedInteractionOutcome =
                OperationOutcome.Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.BUSINESS_RULE)
                .details(CodeableConcept.builder()
                    .text("The requested interaction of type 'update' is not allowed for resource type 'Patient'")
                    .build())
                .build();
        FHIROperationException ex =
                new FHIROperationException("The requested interaction of type 'update' is not allowed for resource type 'Patient'")
                .withIssue(expectedInteractionOutcome);
        doThrow(ex).when(fhirResourceHelper).validateInteraction(any(), anyString());
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, fhirResourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedInteractionOutcome);
    }

    /**
     * Test validate operation for valid interaction when mode = create.
     * @throws Exception
     */
    @Test
    public void testValidateOperationDisAllowedInteractionForCreateMode() throws Exception {
        Parameters input = Parameters.builder()
                .parameter(Parameter.builder()
                    .name("resource")
                    .resource(Patient.builder()
                        .id("1")
                        .text(Narrative.builder()
                            .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                            .status(NarrativeStatus.GENERATED)
                            .build())
                        .build())
                    .build(),
                    Parameter.builder()
                        .name("mode")
                        .value(Code.of("create"))
                        .build())
                .build();
        FHIROperationContext operationContext = opInstanceContextWithMockPersistence(true);

        FHIRResourceHelpers fhirResourceHelper = mock(FHIRResourceHelpers.class);
        Issue expectedInteractionOutcome =
                OperationOutcome.Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.BUSINESS_RULE)
                .details(CodeableConcept.builder()
                    .text("The requested interaction of type 'create' is not allowed for resource type 'Patient'")
                    .build())
                .build();

        FHIROperationException ex =
                new FHIROperationException("The requested interaction of type 'update' is not allowed for resource type 'Patient'")
                .withIssue(expectedInteractionOutcome);
        doThrow(ex).when(fhirResourceHelper).validateInteraction(any(), anyString());
        when(fhirResourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> mock(SingleResourceResult.class));

        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, fhirResourceHelper, null);

        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedInteractionOutcome);
    }

    private FHIROperationContext opInstanceContextWithMockPersistence(boolean isUpdateCreateEnbaled) {
        return opInstanceContextWithMockPersistence(isUpdateCreateEnbaled, true);
    }

    private FHIROperationContext opInstanceContextWithMockPersistence(boolean isUpdateCreateEnbaled, boolean isDeleteSupported) {
        FHIROperationContext operationContext =
                FHIROperationContext.createInstanceOperationContext("validate");
        FHIRPersistence persistence = mock(FHIRPersistence.class);

        when(persistence.isUpdateCreateEnabled()).thenReturn(isUpdateCreateEnbaled);

        when(persistence.isDeleteSupported()).thenReturn(isDeleteSupported);

        operationContext.setProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL, persistence);

        return operationContext;
    }
}