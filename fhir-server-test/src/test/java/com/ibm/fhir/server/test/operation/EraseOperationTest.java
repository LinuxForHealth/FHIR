/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static org.testng.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 *
 */
public class EraseOperationTest extends FHIRServerTestBase {

    @Test
    public void testForbidden() {
        WebTarget target = getWebTarget();
        target = target.path("/Patient/1/$erase");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .get(Response.class);
        assertEquals(r.getStatus(), Status.FORBIDDEN.getStatusCode());
    }
}
