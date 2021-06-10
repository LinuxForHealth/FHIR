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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 * These reindex test always run.
 */
public class ReindexOperationTest extends FHIRServerTestBase {

    @Test(groups = { "reindex" })
    public void testReindex() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampInstant() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string(Instant.now().toString()))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampDayTimeFormat() {
        ZonedDateTime zdt = ZonedDateTime.now();
        String tstamp =
                zdt.getYear()
                + "-"
                + String.format("%02d", zdt.getMonthValue())
                + "-"
                + zdt.getDayOfMonth();

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string(tstamp))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampMalformed() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string("BAD_STRING"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexUsingUnsupportedGet() {
        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithLogicalIdDoesntExist() {
        /*
         * Note, this still passes, and ends up reindexing the default number of resources.
         */
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/" + UUID.randomUUID()+"Doesntexist"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeLogicalId() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/1-2-3-4"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeMissingLogicalId() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeThatDoesntExist() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("BadType"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithPatientResourceType() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }
}