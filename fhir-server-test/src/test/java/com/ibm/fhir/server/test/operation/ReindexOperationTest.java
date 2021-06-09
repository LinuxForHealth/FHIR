/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This class tests the $reindex operation.
 *
 */
public class ReindexOperationTest extends FHIRServerTestBase {

    @Test
    public void testReindex() {
        WebTarget target = getWebTarget();
        target = target.path("/$reindex");

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
            Parameter.builder()
                .name(string("resourceCount"))
                .value(of(5))
                .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();


        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        assertFalse(true);
    }
}