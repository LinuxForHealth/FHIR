/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;

public class FHIRHealthcheckOperationTest extends FHIRServerTestBase {
    @Test
    public void testHealthcheck() {
        WebTarget target = getWebTarget();
        Response response = target.path("$healthcheck").request().get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testHealthcheckLegacy() {
        WebTarget target = getWebTarget();
        Response response = target.path("$healthcheck")
                .request()
                .header("Prefer", "return=OperationOutcome")
                .get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals(operationOutcome.getIssue().size(), 1);
        assertEquals(operationOutcome.getIssue().get(0).getSeverity(), IssueSeverity.INFORMATION);
    }

}
