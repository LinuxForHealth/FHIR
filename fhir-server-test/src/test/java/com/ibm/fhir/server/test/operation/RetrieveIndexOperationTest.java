/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.time.Instant;
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
 * This class tests the $retrieve-index operation.
 */
public class RetrieveIndexOperationTest extends FHIRServerTestBase {

    @Test
    public void testRetrieveIndex() {
        WebTarget target = getWebTarget();
        target = target.path("/$retrieve-index");

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
            Parameter.builder()
                .name(string("_count"))
                .value(of(5))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("notModifiedAfter"))
                .value(string(Instant.now().toString()))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("afterIndexId"))
                .value(string("8"))
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
    }

    @Test
    public void testRetrieveIndex_GET() {
        WebTarget target = getWebTarget();
        target = target.path("/$retrieve-index")
                .queryParam("_count", "5")
                .queryParam("notModifiedAfter", Instant.now().toString())
                .queryParam("afterIndexId", "8");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get();

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test
    public void testRetrieveIndex_type() {
        WebTarget target = getWebTarget();
        target = target.path("/Patient/$retrieve-index");

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
            Parameter.builder()
                .name(string("_count"))
                .value(of(5))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("notModifiedAfter"))
                .value(string(Instant.now().toString()))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("afterIndexId"))
                .value(string("8"))
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
    }

    @Test
    public void testRetrieveIndex_invalidType() {
        WebTarget target = getWebTarget();
        target = target.path("/Resource/$retrieve-index");

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
            Parameter.builder()
                .name(string("_count"))
                .value(of(5))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("notModifiedAfter"))
                .value(string(Instant.now().toString()))
                .build());
        parameters.add(
            Parameter.builder()
                .name(string("afterIndexId"))
                .value(string("8"))
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

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }
}