/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.ContactPointSystem;
import com.ibm.watsonhealth.fhir.model.type.ContactPointUse;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Meta;

public class FHIRValidateOperationTest extends FHIRServerTestBase {
    @Test(groups = { "validate-operation" })
    public void testValidatePatient() {
        Patient patient = buildPatient();
        WebTarget target = getWebTarget();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Resource/$validate").request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        assertEquals("All OK", text);
    }

    @Test(groups = { "validate-operation" })
    public void testValidateInvalidPatient() {
        Patient invalidPatient = buildInvalidPatient();

        WebTarget target = getWebTarget();
        Entity<Patient> entity = Entity.entity(invalidPatient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Resource/$validate").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals(5, operationOutcome.getIssue().size());

        assertTrue(operationOutcome.getIssue().get(0).getDiagnostics().getValue().startsWith("cvc-minLength-valid"));
        assertTrue(operationOutcome.getIssue().get(1).getDiagnostics().getValue().startsWith("cvc-attribute.3"));
        assertTrue(operationOutcome.getIssue().get(2).getDiagnostics().getValue().startsWith("cvc-pattern-valid"));
        assertTrue(operationOutcome.getIssue().get(3).getDiagnostics().getValue().startsWith("cvc-attribute.3"));
        assertTrue(operationOutcome.getIssue().get(4).getDiagnostics().getValue().startsWith("cpt-2"));
    }

    @Test(groups = { "validate-operation" })
    public void testValidatePatientWithUserDefinedSchematron() {
        Patient patient = buildPatient();

        patient = patient.toBuilder()
                .meta(Meta.builder().profile(Canonical.of("http://ibm.com/watsonhealth/fhir/profile/acme-sample"))
                        .build())
                .extension(Extension.builder("http://ibm.com/watsonhealth/fhir/extension/acme-sample/study_ID")
                        .value(string("abc-1234")).build())
                .build();

        WebTarget target = getWebTarget();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Resource/$validate").request().header("X-FHIR-TENANT-ID", "tenant1")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        assertEquals("All OK", text);
    }

    @Test(groups = { "validate-operation" })
    public void testValidateInvalidPatientWithUserDefinedSchematron() {
        Patient patient = buildPatient();

        patient = patient.toBuilder().meta(
                Meta.builder().profile(Canonical.of("http://ibm.com/watsonhealth/fhir/profile/acme-sample")).build())
                .build();

        // Perform validation using tenant1's user-defined rules (should fail
        // validation).
        WebTarget target = getWebTarget();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Resource/$validate").request().header("X-FHIR-TENANT-ID", "tenant1")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);
        assertEquals(1, operationOutcome.getIssue().size());
        assertTrue(operationOutcome.getIssue().get(0).getDiagnostics().getValue().startsWith("acme-sample-1"));

        // Perform validation using default tenant's user-defined rules (should pass
        // validation).
        target = getWebTarget();
        entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Resource/$validate").request().header("X-FHIR-TENANT-ID", "default").post(entity,
                Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        operationOutcome = response.readEntity(OperationOutcome.class);
        assertEquals(1, operationOutcome.getIssue().size());
        assertEquals("All OK", operationOutcome.getIssue().get(0).getDetails().getText().getValue());
    }

    private Patient buildPatient() {
        // build patient object using fluent API
        Patient patient = Patient.builder()
                .name(HumanName.builder().family(string("Doe")).given(string("John")).build())
                .birthDate(Date.of("1950-08-15")).telecom(ContactPoint.builder().system(ContactPointSystem.PHONE)
                        .use(ContactPointUse.HOME).value(string("555-1234")).build())
                .build();
        return patient;
    }

    private Patient buildInvalidPatient() {
        // build patient object using fluent API
        Patient patient = Patient.builder().name(HumanName.builder().family(string("")).given(string("John")).build())
                .birthDate(Date.of("19500815"))
                .telecom(ContactPoint.builder().use(ContactPointUse.HOME).value(string("555-1234")).build()).build();
        return patient;
    }
}
