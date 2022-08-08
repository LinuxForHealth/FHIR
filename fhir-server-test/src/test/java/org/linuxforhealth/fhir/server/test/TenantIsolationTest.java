/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRRequestHeader;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.AdministrativeGender;
import org.linuxforhealth.fhir.model.type.code.CodeSystemContentMode;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.profile.ProfileBuilder;

public class TenantIsolationTest extends FHIRServerTestBase {
    // use random UUID values for the canonical urls in case the cleanup ever fails
    String codeSystemUrl = UUID.randomUUID().toString();
    String profileUrl = UUID.randomUUID().toString();

    @BeforeClass
    public void createArtifactsInDefaultTenant() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Patient.class, profileUrl, "0.0.1")
                .cardinality("Patient.active", 1, "1")
                .build();
        createResourceAndReturnTheLogicalId("StructureDefinition", profile);

        CodeSystem codeSystem = TestUtil.getMinimalResource(CodeSystem.class).toBuilder()
                .url(Uri.of(codeSystemUrl))
                .version("0.0.1")
                .content(CodeSystemContentMode.COMPLETE)
                .concept(Concept.builder()
                    .code(Code.of("a"))
                    .display("A")
                    .build())
                .build();
        createResourceAndReturnTheLogicalId("CodeSystem", codeSystem);
    }

    @Test
    public void testCodeSystemIsolation() throws Exception {
        Parameters paramsA = buildLookupParams("a");
        Parameters paramsB = buildLookupParams("b");

        // First, confirm that the CodeSystem is used for lookups in the default tenant
        FHIRResponse response = client.invoke("CodeSystem", "$lookup", paramsA);
        assertEquals(response.getStatus(), 200);
        Resource resource = response.getResource(Resource.class);
        assertTrue(resource instanceof Parameters);

        // Next, confirm that tenant1 cannot see the CodeSystem created under the default tenant
        response = client.invoke("CodeSystem", "$lookup", paramsA, tenantAndDatastoreHeaders("tenant1", "profile"));
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        Issue firstIssue = oo.getIssue().get(0);
        assertEquals(firstIssue.getSeverity(), IssueSeverity.FATAL, oo.getIssue().get(0).getDetails().toString());

        // Finally, create a conflicting CodeSystem in tenant1/reference and confirm we use that instead of the default tenant one
        // The alternate dsid is used to avoid the negative lookup cached in the ServerRegistryResourceProvider in the prior step
        String codeSystemResourceId = null;
        try {
            CodeSystem codeSystem = TestUtil.getMinimalResource(CodeSystem.class).toBuilder()
                    .url(Uri.of(codeSystemUrl))
                    .version("0.0.1")
                    .content(CodeSystemContentMode.COMPLETE)
                    .concept(Concept.builder()
                        .code(Code.of("b"))
                        .display("B")
                        .build())
                    .build();
            codeSystemResourceId = createResourceInTenantDatastoreAndReturnId("tenant1", "reference", codeSystem);

            response = client.invoke("CodeSystem", "$lookup", paramsA, tenantAndDatastoreHeaders("tenant1", "reference"));
            assertEquals(response.getStatus(), 400);
            resource = response.getResource(Resource.class);
            assertTrue(resource instanceof OperationOutcome);
            firstIssue = ((OperationOutcome)resource).getIssue().get(0);
            assertEquals(firstIssue.getSeverity(), IssueSeverity.ERROR,
                    "Unexpected issue: " + firstIssue.getDetails().getText().toString() + ";");
            assertEquals(firstIssue.getDetails().getText().getValue(), "Code 'a' not found in system '" + codeSystemUrl + "'");

            response = client.invoke("CodeSystem", "$lookup", paramsB, tenantAndDatastoreHeaders("tenant1", "reference"));
            assertEquals(response.getStatus(), 200);
            resource = response.getResource(Resource.class);
            assertTrue(resource instanceof Parameters);
        } finally {
            if (codeSystemResourceId != null) {
                client.delete("CodeSystem", codeSystemResourceId, tenantAndDatastoreHeaders("tenant1", "reference"));
            }
        }
    }

    private Parameters buildLookupParams(String code) {
        return Parameters.builder()
            .parameter(Parameter.builder()
                .name("system")
                .value(Uri.of(codeSystemUrl))
                .build())
            .parameter(Parameter.builder()
                .name("code")
                .value(Code.of(code))
                .build())
            .build();
    }

    @Test
    public void testProfileIsolation() throws Exception {
        Patient patientWithActive = buildPatientWithActive();
        Patient patientWithGender = buildPatientWithGender();

        // First, confirm that the StructureDefinition is used for validation in the default tenant
        FHIRResponse response = client.validate(patientWithActive);
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.INFORMATION,
                "Unexpected issue: " + oo.getIssue().get(0).getDetails().getText().toString() + ";");

        response = client.validate(patientWithGender);
        oo = response.getResource(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.ERROR);

        // Next, confirm that tenant1 cannot see the profile created under the default tenant
        response = client.validate(patientWithActive, tenantAndDatastoreHeaders("tenant1", "profile"));
        oo = response.getResource(OperationOutcome.class);
        Issue firstIssue = oo.getIssue().get(0);
        assertNotNull(firstIssue);
        assertEquals(firstIssue.getSeverity(), IssueSeverity.WARNING,
                "Unexpected issue: " + firstIssue.getDetails().getText().toString() + ";");
        assertEquals(firstIssue.getDetails().getText().getValue(), "Profile '" + profileUrl +"' is not supported");

        // Finally, create a conflicting profile in tenant1/reference and confirm we use that instead of the default tenant one
        // The alternate dsid is used to avoid the negative lookup cached in the ServerRegistryResourceProvider in the prior step
        String profileResourceId = null;
        try {
            StructureDefinition profile = new ProfileBuilder(Patient.class, profileUrl, "0.0.1")
                    // require "gender" and not "active"
                    .cardinality("Patient.gender", 1, "1")
                    .build();
            profileResourceId = createResourceInTenantDatastoreAndReturnId("tenant1", "reference", profile);

            response = client.validate(patientWithGender, tenantAndDatastoreHeaders("tenant1", "reference"));
            oo = response.getResource(OperationOutcome.class);
            assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.INFORMATION,
                    "Unexpected issue: " + oo.getIssue().get(0).getDetails().getText().getValue() + ";");

            response = client.validate(patientWithActive, tenantAndDatastoreHeaders("tenant1", "reference"));
            oo = response.getResource(OperationOutcome.class);
            assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.ERROR,
                    "Unexpected issue: " +  oo.getIssue().get(0).getDetails().getText().getValue() + ";");
        } finally {
            if (profileResourceId != null) {
                client.delete("StructureDefinition", profileResourceId, tenantAndDatastoreHeaders("tenant1", "reference"));
            }
        }
    }

    private String createResourceInTenantDatastoreAndReturnId(String tenant, String datastore, Resource resource) throws Exception {
        FHIRResponse response = client.create(resource, tenantAndDatastoreHeaders(tenant, datastore));
        assertEquals(response.getStatus(), 201);
        return response.getLocation().substring(response.getLocation().indexOf("/_history/") + 10);
    }

    private FHIRRequestHeader[] tenantAndDatastoreHeaders(String tenant, String datastore) throws Exception {
        return new FHIRRequestHeader[] {
            FHIRRequestHeader.header("X-FHIR-TENANT-ID", tenant),
            FHIRRequestHeader.header("X-FHIR-DSID", datastore)
        };
    }

    private Patient buildPatientWithActive() throws Exception {
        return TestUtil.getMinimalResource(Patient.class).toBuilder()
                .meta(Meta.builder()
                    .profile(Canonical.of(profileUrl))
                    .build())
                .text(Narrative.EMPTY)
                .active(true)
                .build();
    }

    private Patient buildPatientWithGender() throws Exception {
        return TestUtil.getMinimalResource(Patient.class).toBuilder()
                .meta(Meta.builder()
                    .profile(Canonical.of(profileUrl))
                    .build())
                .text(Narrative.EMPTY)
                .gender(AdministrativeGender.OTHER)
                .build();
    }
}
