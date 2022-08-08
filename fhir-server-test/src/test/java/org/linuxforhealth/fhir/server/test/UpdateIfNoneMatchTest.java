/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;

import java.util.UUID;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRRequestHeader;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.code.IssueType;

/**
 * Test conditional create-on-update using If-None-Match
 */
public class UpdateIfNoneMatchTest extends FHIRServerTestBase {
    private static final String HEADERNAME_IF_NONE_MATCH = "If-None-Match";

    @Test
    public void testCreateOnUpdate() throws Exception {

        if (this.isUpdateCreateSupported()) {

            // Read a JSON Patient and set the id.
            String patientLogicalId = UUID.randomUUID().toString();
            Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
            patient = patient.toBuilder().id(patientLogicalId).build();

            FHIRResponse response = client.update(patient);
            int status = response.getStatus();
            assertEquals(status, 201);
            assertEquals(response.getETag(), "W/\"1\"");

            // Now try an update with If-None-Match defined
            final FHIRRequestHeader ifNoneMatch = new FHIRRequestHeader(HEADERNAME_IF_NONE_MATCH, "*");
            response = client.update(patient, ifNoneMatch);
            status = response.getStatus();

            // by default, hitting If-None-Match is considered a 412 Precondition Failed error
            assertEquals(status, 412);

            // Verify no expression field is set
            OperationOutcome oo = response.getResource(OperationOutcome.class);
            assertEquals(oo.getIssue().size(), 1);
            Issue issue = oo.getIssue().get(0);
            assertEquals(issue.getCode().getValue(), IssueType.CONFLICT.getValue());
            assertEquals(issue.getExpression().size(), 0);

            // Read back the patient and make sure it is still at version 1
            response = client.read(Patient.class.getSimpleName(), patientLogicalId);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

            patient =  response.getResource(Patient.class);
            assertEquals(patient.getId(), patientLogicalId);
            assertEquals(patient.getMeta().getVersionId().getValue(), "1");
        }
    }



    /**
     * Perform a Patient create-on-update with If-None-Match and check
     * the OperationOutcome matches the request.
     * @throws Exception
     */
    @Test
    public void testCreateOnUpdateAfterDelete() throws Exception {

        if (this.isUpdateCreateSupported()) {

            // Read a JSON Patient and set the id.
            String patientLogicalId = UUID.randomUUID().toString();
            Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
            patient = patient.toBuilder().id(patientLogicalId).build();

            FHIRResponse response = client.update(patient);
            int status = response.getStatus();
            assertEquals(status, 201);
            assertEquals(response.getETag(), "W/\"1\"");

            // Delete the resource. The deletion marker will have version 2
            response = client.delete(Patient.class.getSimpleName(), patientLogicalId);
            assertEquals(response.getStatus(), 200);

            // Now try an update with If-None-Match defined resulting in version 3
            final FHIRRequestHeader ifNoneMatch = new FHIRRequestHeader(HEADERNAME_IF_NONE_MATCH, "*");
            response = client.update(patient, ifNoneMatch);
            status = response.getStatus();
            assertEquals(status, 201); // Undeleted the resource, treated as Created (per Touchstone)
            assertEquals(response.getETag(), "W/\"3\"");
        }
    }
}