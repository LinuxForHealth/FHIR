/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.validate;

import static com.ibm.fhir.model.type.String.string;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIRRestHelper;

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

            validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Input parameter 'resource' is required for the $validate operation");
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
                        .text(string("All OK"))
                        .build())
                    .build();

            Parameters output = validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail();
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
                        .text(string("Profile 'unsupported' is not supported"))
                        .build())
                    .expression(string("Patient"))
                    .build();

            Parameters output = validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail();
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
                        .text(string("A required profile was not specified. Resources of type 'Patient' must declare conformance to at least one of the following profiles: [atLeastOne]"))
                        .build())
                    .build();

            Parameters output = validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail();
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
                        .text(string("A profile was specified which is not allowed. Resources of type 'Patient' are not allowed to declare conformance to any of the following profiles: [notAllowed]"))
                        .build())
                    .build();

            Parameters output = validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail();
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
                        .text(string("Profile 'unsupported' is not supported"))
                        .build())
                    .expression(string("Patient"))
                    .build();

            Parameters output = validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
            OperationOutcome oo = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            assertEquals(oo.getIssue().get(0), expectedOutput);
        } catch (Exception e) {
            fail();
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
        
        FHIROperationContext operationContext =
                FHIROperationContext.createInstanceOperationContext("validate");
        Parameters input = Parameters.builder()
                .build();
        
        Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.INFORMATION)
                    .code(IssueType.INFORMATIONAL)
                    .details(CodeableConcept.builder()
                        .text(string("All OK"))
                        .build())
                    .build();
        Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "test", null, input, resourceHelper, null);
        OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
    }
    
    /**
     * Test resource-instance level validate operation when the resource with the input logicalID is not available in the database.
     * @throws Exception 
     */
    @Test(expectedExceptions = { FHIROperationException.class } , expectedExceptionsMessageRegExp  = ".*Patient with id '1' was not found*")
    public void testResourceInstanceLevelValidateForNullResource() throws Exception {
        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        SingleResourceResult result = mock(SingleResourceResult.class);
        
        // mock and return null when resourceHelper.doRead() is invoked from validateOperation.doInvoke
        when(result.isSuccess()).thenReturn(false);
        when(result.getResource()).thenReturn(null);
        when(resourceHelper.doRead(eq("Patient"), anyString())).thenAnswer(x -> result);
        
        FHIROperationContext operationContext =
                FHIROperationContext.createInstanceOperationContext("validate");
        Parameters input = Parameters.builder()
                .build();

        validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);
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
                            .value(Code.of("random"))
                            .build())
                    .build();
           validateOperation.doInvoke(null, null, null, null, input, resourceHelper, null);
        
    }
    
    /**
     * Test validate operation with delete mode type code. 
     * Validate the outcome when the persistence layer implementation does not support the "delete" operation 
     *  
     */
    @Test
    public void testValidateOperationDeleteNotSupported() throws FHIROperationException {
        
           FHIRPersistence persistence = Mockito.mock(FHIRPersistence.class);
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
            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext("validate");
            operationContext.setProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL, persistence);
            
            // mock the persistence layer implementation not to support the "delete" operation
            when(persistence.isDeleteSupported()).thenReturn(false);
            
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.WARNING)
                    .code(IssueType.NOT_SUPPORTED)
                    .details(CodeableConcept.builder()
                        .text(string("Resource deletion of type 'Patient' with id '1' is not supported."))
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
        
        FHIRPersistence persistence = Mockito.mock(FHIRPersistence.class);
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
            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext("validate");
            operationContext.setProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL, persistence);
            
            // mock the persistence layer implementation to support the "delete" operation 
            when(persistence.isDeleteSupported()).thenReturn(true);
            
            Issue expectedOutput = Issue.builder()
                    .severity(IssueSeverity.INFORMATION)
                    .code(IssueType.INFORMATIONAL)
                    .details(CodeableConcept.builder()
                        .text(string("All OK"))
                        .build())
                    .build();
            
            Parameters output = validateOperation.doInvoke(operationContext, Patient.class, "1", null, input, resourceHelper, null);
            
            OperationOutcome operationOutcome = output.getParameter().get(0).getResource().as(OperationOutcome.class);
            assertEquals(operationOutcome.getIssue().size(), 1);
            assertEquals(operationOutcome.getIssue().get(0), expectedOutput);
        
    }
    
}