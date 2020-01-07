/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;

/**
 * Conditional read tests of the FHIR Server.
 */
public class ConditionalReadTest extends FHIRServerTestBase {
    private static final String HEADERNAME_IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final String HEADERNAME_IF_NONE_MATCH = "If-None-Match";
    private Patient savedCreatedPatient;
    private Boolean conditionalReadSupported = null;

    /**
     * Retrieve the server's conformance statement to determine the status of
     * certain runtime options.
     * 
     * @throws Exception
     */
    @BeforeClass
    public void retrieveConfig() throws Exception {
        conditionalReadSupported = isConditionalReadSupported();
        System.out.println("Conditional read enabled?: " + conditionalReadSupported.toString());
    }

    /**
     * Create a Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "conditiaonal-read" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatient = responsePatient;

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Tests the update of the original patient that was previously created.
     */
    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testCreatePatient" })
    public void testUpdatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Create a fresh copy of the mock Patient.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        // Be sure to set the saved patient's id as well.
        // And add an additional contact phone number
        patient = patient.toBuilder().id(savedCreatedPatient.getId()).telecom(ContactPoint.builder()
                .system(ContactPointSystem.PHONE).use(ContactPointUse.WORK).value(string("999-111-1111")).build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);

        // Now call the 'update' API.
        String targetPath = "Patient/" + patient.getId();
        Response response = target.path(targetPath).request().put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceTrue() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Sat, 28 Sep 2019 16:11:14 GMT").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceTrue_RFC850() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Saturday, 28-Sep-99 16:11:14 GMT").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceTrue_RFC850_2() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Saturday, 28-Sep-2018 16:11:14 GMT").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceTrue_ANSI() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Sat Sep 28 16:11:14 2019").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceTrue_TouchStone() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Sat, 28-Sep-19 16:11:14").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceFalse() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Tue, 28 Sep 2100 16:11:14 GMT").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceFalse_RFC850() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Tuesday, 28-Sep-2100 16:11:14 GMT").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceFalse_RFC850_2() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Wednesday, 28-Sep-39 16:11:14 GMT").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceFalse_ANSI() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Tue Sep 28 16:11:14 2100").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceFalse_TouchStone() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API to retrieve the updated patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Wed, 28-Sep-39 16:11:14").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfNoneMatchTrue() throws Exception {
        WebTarget target = getWebTarget();

        // Call the 'read' API with different valid ETag formats to retrieve the updated
        // patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_NONE_MATCH, "1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "\"1\"").get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "W/\"1\"").get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "W/1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Sat, 28 Sep 2019 16:11:14 GMT")
                .header(HEADERNAME_IF_NONE_MATCH, "W/1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Tuesday, 28-Sep-2100 16:11:14 GMT")
                .header(HEADERNAME_IF_NONE_MATCH, "W/1").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfNoneMatchFalse() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();

        // Call the 'read' API with different valid ETag formats to retrieve the updated
        // patient and verify it.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_NONE_MATCH, "2").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "\"2\"").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "W/\"2\"").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_NONE_MATCH, "W/2").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());

        response = target.path("Patient/" + savedCreatedPatient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header(HEADERNAME_IF_MODIFIED_SINCE, "Tue, 28 Sep 2100 16:11:14 GMT")
                .header(HEADERNAME_IF_NONE_MATCH, "W/2").get();
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfNoneMatchInvalid() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();
        // Call the 'read' API with invalid ETag format to retrieve the updated patient.
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_NONE_MATCH, "2A").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "conditiaonal-read" }, dependsOnMethods = { "testUpdatePatient" })
    public void testIfModifiedSinceInvalid() throws Exception {
        assertNotNull(conditionalReadSupported);
        if (!conditionalReadSupported.booleanValue()) {
            return;
        }
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Tue").get();
        // Server should always ignore the invalid input - incomplete date time.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14").get();
        // Server should always ignore the invalid input - incomplete date time.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14, 28-Sep-39 16:11:14").get();
        // Server should always ignore the invalid input - with one incomplete date time and one correct date time.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed, 28-Sep-39 16:11:14 Sep 28 16:11:14").get();
        // Server should always ignore the invalid input - with one incomplete date time and one correct date time.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14 2039, 27-Sep-39 16:11:14").get();
        // Server should always ignore the invalid input - the duplicated date times are not align with each other.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14 2100").get();
        // Server should always ignore the invalid input - weekday doesn't align with the date
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14 2039 28-Sep-39 16:11:14").get();
        // Server should always ignore the invalid input - duplicated date times and one doesn't match any of the patterns.
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        response = target.path("Patient/" + savedCreatedPatient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).header(HEADERNAME_IF_MODIFIED_SINCE, "Wed Sep 28 16:11:14 2039, 28-Sep-39 16:11:14").get();
        // Server allows duplicated input if the date times are aligned with each other and each is correct by itself.
        assertResponse(response, Response.Status.NOT_MODIFIED.getStatusCode());
    }

}
