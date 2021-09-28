/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.resource.StructureDefinition.Context;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CodeSystemContentMode;
import com.ibm.fhir.model.type.code.ExtensionContextType;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.profile.ExtensionBuilder;

public class TenantIsolationTest extends FHIRServerTestBase {
    @BeforeClass
    public void createArtifactsInDefaultTenant() throws Exception {
        StructureDefinition extension = new ExtensionBuilder("http://ibm.com/fhir/StructureDefinition/favorite-team", "0.0.1", "string")
                .context(Context.builder()
                    .type(ExtensionContextType.ELEMENT)
                    .expression("Element")
                    .build())
                .build();
        createResourceAndReturnTheLogicalId("StructureDefinition", extension);

        CodeSystem codeSystem = TestUtil.getMinimalResource(CodeSystem.class).toBuilder()
                .url(Uri.of("http://ibm.com/fhir/StructureDefinition/teams"))
                .version("0.0.1")
                .content(CodeSystemContentMode.COMPLETE)
                .concept(Concept.builder()
                    .code(Code.of("Packers"))
                    .build())
                .concept(Concept.builder()
                    .code(Code.of("Brewers"))
                    .build())
                .concept(Concept.builder()
                    .code(Code.of("Bucks"))
                    .build())
                .concept(Concept.builder()
                    .code(Code.of("Badgers"))
                    .build())
                .build();
        createResourceAndReturnTheLogicalId("CodeSystem", codeSystem);
    }

    @Test()
    public void testCodeSystemIsolation() throws Exception {
        Parameters params = Parameters.builder()
            .parameter(Parameter.builder()
                .name("system")
                .value(Uri.of("http://ibm.com/fhir/StructureDefinition/teams"))
                .build())
            .parameter(Parameter.builder()
                .name("code")
                .value(Code.of("Packers"))
                .build())
            .build();

        FHIRResponse response = client.invoke("CodeSystem", "$lookup", params);
        Resource resource = response.getResource(Resource.class);
        assertTrue(resource instanceof Parameters);
        System.out.println(resource);

        response = client.invoke("CodeSystem", "$lookup", params,
            FHIRRequestHeader.header("X-FHIR-TENANT-ID", "tenant1"),
            FHIRRequestHeader.header("X-FHIR-DSID", "reference"));
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.FATAL, oo.getIssue().get(0).getDetails().toString());
    }

    @Test()
    public void testExtensionIsolation() throws Exception {
        Patient patient = buildPatient();

        FHIRResponse response = client.validate(patient);
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.INFORMATION);

        response = client.validate(patient,
            FHIRRequestHeader.header("X-FHIR-TENANT-ID", "tenant1"),
            FHIRRequestHeader.header("X-FHIR-DSID", "profile"));
        oo = response.getResource(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.WARNING, oo.getIssue().get(0).getDetails().toString());
    }

    private Patient buildPatient() throws Exception {
        return TestUtil.getMinimalResource(Patient.class).toBuilder()
                .text(Narrative.EMPTY)
                .extension(Extension.builder()
                    .url("http://ibm.com/fhir/StructureDefinition/favorite-team")
                    .value("Packers")
                    .build())
                .build();
    }
}
