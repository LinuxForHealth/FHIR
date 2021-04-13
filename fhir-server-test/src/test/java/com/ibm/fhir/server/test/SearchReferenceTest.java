/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;

public class SearchReferenceTest extends FHIRServerTestBase {

    @SuppressWarnings("unused")
    private String patientResourceIdWithReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientResourceIdWithReferenceUrl;
    @SuppressWarnings("unused")
    private String patientWithLiteralReference;
    @SuppressWarnings("unused")
    private String patientResourceIdWithPractitionerReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientResourceIdWithPractitionerRoleReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientWithLogicalReference;
    @SuppressWarnings("unused")
    private String patientWithLiteralAndLogicalReference;

    /*
     * creates the various test cases for a patient with a reference
     */
    public String createPatientWithReference(String field, String reference, Identifier logicalReference) throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Reference.Builder referenceBuilder = Reference.builder();
        if (reference != null) {
            referenceBuilder.reference(com.ibm.fhir.model.type.String.of(reference));
        }
        if (logicalReference != null) {
            referenceBuilder.identifier(logicalReference);
        }

        if ("organization".equals(field)) {
            patient = patient.toBuilder()
                    .gender(AdministrativeGender.MALE)
                    .managingOrganization(referenceBuilder.display(com.ibm.fhir.model.type.String.of("Test Organization")).build()).build();
        } else if ("general-practitioner".equals(field)) {
            patient = patient.toBuilder()
                    .gender(AdministrativeGender.MALE)
                    .generalPractitioner(referenceBuilder.display(com.ibm.fhir.model.type.String.of("Test Practitioner")).build()).build();
        }
        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String tmpPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + tmpPatientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
        return tmpPatientId;
    }

    @Test(groups = { "server-search-reference" })
    public void testCreatePatient() throws Exception {
        // references without an explicit type will be interpreted as literals (e.g. externals) not local references
        // we will not infer their type, which means that searches specifying a type will not match them by design
        // Not allowed by default (see: `fhirServer/core/checkReferenceTypes` config parameter)
        // patientResourceIdWithReferenceId = createPatientWithReference("organization", "3001", null);

        // relative reference
        patientResourceIdWithReferenceTypeId = createPatientWithReference("organization", "Organization/3002", null);

        // relative reference because the url aligns with the server base address
        patientResourceIdWithReferenceUrl = createPatientWithReference("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003", null);

        // patientResourceIdWithReferenceCanonicalUri = createPatientWithReference("https://localhost:9443/fhir-server/api/v4/Organization/3004|1.0.0", null);

        // Literal reference to another server
        patientWithLiteralReference = createPatientWithReference("organization", "https://an.example.com/Organization/3004", null);

        // relative reference to Practitioner for general practitioner
        patientResourceIdWithPractitionerReferenceTypeId = createPatientWithReference("general-practitioner", "Practitioner/3002", null);

        // relative reference to PractitionerRole for general practitioner
        patientResourceIdWithPractitionerRoleReferenceTypeId = createPatientWithReference("general-practitioner", "PractitionerRole/3002", null);

        // logical reference
        patientWithLogicalReference = createPatientWithReference("organization", null, Identifier.builder().system(Uri.of("https://an.example.com/OrgId")).value(string("3005")).build());

        // literal and logical reference
        patientWithLiteralAndLogicalReference = createPatientWithReference("organization", "Organization/3006", Identifier.builder().system(Uri.of("https://an.example.com/OrgId")).value(string("3007")).build());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingTypeId() {
        // no match for this search because Organization/3001 because the patient 3001 reference is a literal not relative reference
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3001").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingUrl() {
        // no match for this search because Organization/3001 because the patient 3001 reference is a literal not relative reference
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3001").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeUrl() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingId() {
        // We expect a match here, because 3003 will match Organization/3003 even though
        // no resource type is given.
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingTypeId() {
        WebTarget target = getWebTarget();

        Response response =
                target.path("Patient").queryParam("organization", "Organization/3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingUrl() {
        WebTarget target = getWebTarget();

        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReference() {
        // not expected to find a match because the reference is an external literal
        WebTarget target = getWebTarget();

        Response response =
                target.path("Patient").queryParam("organization", "3004").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReferenceWithType() {
        // not expected to find a match because the reference is an external literal
        WebTarget target = getWebTarget();

        Response response =
                target.path("Patient").queryParam("organization", "Organization/3004").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReferenceWithUrl() {
        // should find a match here because the search parameter is an exact match of the reference value
        WebTarget target = getWebTarget();

        Response response =
                target.path("Patient").queryParam("organization", "https://an.example.com/Organization/3004").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://an.example.com/Organization/3004", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingIdMultipleTypes() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("general-practitioner", "3002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Multiple resource type matches found for logical ID '3002' for search parameter 'general-practitioner'.");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceUsingId() {
        // not expected to find a match here because the search parameter only searches identifier of reference
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization:identifier", "3001").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceWithIdentifier() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization:identifier", "https://an.example.com/OrgId|3005").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://an.example.com/OrgId", p.getManagingOrganization().getIdentifier().getSystem().getValue());
        assertEquals("3005", p.getManagingOrganization().getIdentifier().getValue().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceWithIdentifierValue() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization:identifier", "3005").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://an.example.com/OrgId", p.getManagingOrganization().getIdentifier().getSystem().getValue());
        assertEquals("3005", p.getManagingOrganization().getIdentifier().getValue().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralRefForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3006").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3006", p.getManagingOrganization().getReference().getValue());
        assertEquals("https://an.example.com/OrgId", p.getManagingOrganization().getIdentifier().getSystem().getValue());
        assertEquals("3007", p.getManagingOrganization().getIdentifier().getValue().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalRefForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization:identifier", "https://an.example.com/OrgId|3007").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3006", p.getManagingOrganization().getReference().getValue());
        assertEquals("https://an.example.com/OrgId", p.getManagingOrganization().getIdentifier().getSystem().getValue());
        assertEquals("3007", p.getManagingOrganization().getIdentifier().getValue().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralAndLogicalRefsForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3006")
                .queryParam("organization:identifier", "3007").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3006", p.getManagingOrganization().getReference().getValue());
        assertEquals("https://an.example.com/OrgId", p.getManagingOrganization().getIdentifier().getSystem().getValue());
        assertEquals("3007", p.getManagingOrganization().getIdentifier().getValue().getValue());
    }
}