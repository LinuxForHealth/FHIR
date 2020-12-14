/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.testng.annotations.Test;

import com.ibm.fhir.audit.beans.ApiParameters;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.Batch;
import com.ibm.fhir.audit.beans.ConfigData;
import com.ibm.fhir.audit.beans.Context;
import com.ibm.fhir.audit.beans.Data;
import com.ibm.fhir.audit.beans.FHIRContext;
import com.ibm.fhir.exception.FHIRException;

public class AuditBeansTest {

    @Test
    public void testBatch() throws IOException, FHIRException {
        /*
         * Batch tests Builder, Parser and Writer
         */
        Batch batch = Batch.builder().build();
        String jsonString = Batch.Writer.generate(batch);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        batch = Batch.Parser.parse(bais);
        assertNotNull(batch);
        assertNull(batch.getStatus());

        batch = Batch.builder().resourcesCreated(10).resourcesRead(11).resourcesUpdated(12).status("200").build();
        assertNotNull(batch);
        assertNotNull(batch.getStatus());
        assertNotNull(batch.getResourcesCreated());
        assertNotNull(batch.getResourcesRead());
        assertNotNull(batch.getResourcesUpdated());
        assertEquals(batch.getStatus(), "200");
        assertEquals(batch.getResourcesCreated().intValue(), 10);
        assertEquals(batch.getResourcesRead().intValue(), 11);
        assertEquals(batch.getResourcesUpdated().intValue(), 12);
    }

    @Test
    public void testConfigData() throws FHIRException, IOException {
        ConfigData data = ConfigData.builder().build();
        String jsonString = ConfigData.Writer.generate(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        data = ConfigData.Parser.parse(bais);
        assertNotNull(data);
        assertNull(data.getServerStartupParms());

        data.setServerStartupParms("test");
        jsonString = ConfigData.Writer.generate(data);
        bais       = new ByteArrayInputStream(jsonString.getBytes());
        data       = ConfigData.Parser.parse(bais);
        assertNotNull(data);
        assertNotNull(data.getServerStartupParms());

        data       = ConfigData.builder().serverStartupParameters("test12345").build();
        jsonString = ConfigData.Writer.generate(data);
        bais       = new ByteArrayInputStream(jsonString.getBytes());
        data       = ConfigData.Parser.parse(bais);
        assertNotNull(data);
        assertNotNull(data.getServerStartupParms());

        assertEquals(data.getServerStartupParms(), "test12345");
    }

    @Test
    public void testApiParametersFull() throws IOException, FHIRException {
        ApiParameters parameters = ApiParameters.builder().request("request").status(200).build();
        assertNotNull(parameters);
        assertNotNull(parameters.getRequest());
        assertNotNull(parameters.getStatus());

        assertEquals(parameters.getRequest(), "request");
        int auto = parameters.getStatus();
        assertEquals(auto, 200);

        String jsonString = ApiParameters.Writer.generate(parameters);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        parameters = ApiParameters.Parser.parse(bais);

        assertNotNull(parameters);
        assertNotNull(parameters.getRequest());
        assertNotNull(parameters.getStatus());

        assertEquals(parameters.getRequest(), "request");
        auto = parameters.getStatus();
        assertEquals(auto, 200);
    }

    @Test
    public void testApiParametersEmptyNull() throws IOException, FHIRException {
        ApiParameters parameters = ApiParameters.builder().build();
        assertNotNull(parameters);
        assertNull(parameters.getRequest());
        assertNull(parameters.getStatus());

        String jsonString = ApiParameters.Writer.generate(parameters);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        parameters = ApiParameters.Parser.parse(bais);

        assertNotNull(parameters);
        assertNull(parameters.getRequest());
        assertNull(parameters.getStatus());
    }

    @Test
    public void testContextEmptyNull() throws FHIRException, IOException {
        Context ctx = Context.builder().build();
        String jsonString = Context.Writer.generate(ctx);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        ctx = Context.Parser.parse(bais);
        assertNotNull(ctx);
        assertNull(ctx.getAction());
        assertNull(ctx.getApiParameters());
        assertNull(ctx.getBatch());
        assertNull(ctx.getData());
        assertNull(ctx.getEndTime());
        assertNull(ctx.getOperationName());
        assertNull(ctx.getPurpose());
        assertNull(ctx.getQueryParameters());
        assertNull(ctx.getRequestUniqueId());
        assertNull(ctx.getStartTime());
    }

    @Test
    public void testDataFull() throws FHIRException, IOException {
        Data data = Data.builder().id("id").resourceType("resourceType").versionId("versionId").build();
        String jsonString = Data.Writer.generate(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        data = Data.Parser.parse(bais);
        assertNotNull(data);
        assertEquals(data.getId(), "id");
        assertEquals(data.getResourceType(), "resourceType");
        assertEquals(data.getVersionId(), "versionId");
    }

    @Test
    public void testDataNullEmpty() throws FHIRException, IOException {
        Data data = Data.builder().build();
        String jsonString = Data.Writer.generate(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        data = Data.Parser.parse(bais);
        assertNotNull(data);
        assertNull(data.getId());
        assertNull(data.getResourceType());
        assertNull(data.getVersionId());
    }

    @Test
    public void testContext() throws FHIRException, IOException {
        ApiParameters apiParameters = ApiParameters.builder().request("request").status(200).build();
        Context.Builder builder = Context.builder();
        builder.apiParameters(apiParameters);
        builder.action("action");
        builder.endTime("endTime");

        Batch batch = Batch.builder().resourcesCreated(10).resourcesRead(11).resourcesUpdated(12).status("200").build();
        builder.batch(batch);

        Data data = Data.builder().id("id").resourceType("resourceType").versionId("versionId").build();
        builder.data(data);
        builder.operationName("operationName");
        builder.purpose("purpose");
        builder.queryParameters("queryParameters");
        builder.requestUniqueId("requestUniqueId");
        builder.startTime("startTime");

        Context ctx = builder.build();
        String jsonString = Context.Writer.generate(ctx);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        ctx = Context.Parser.parse(bais);
        assertNotNull(ctx);

        assertNotNull(ctx.getAction());
        assertEquals(ctx.getAction(), "action");

        assertNotNull(ctx.getApiParameters());
        assertNotNull(ctx.getBatch());
        assertNotNull(ctx.getData());
        assertNotNull(ctx.getQueryParameters());

        assertNotNull(ctx.getEndTime());
        assertEquals(ctx.getEndTime(), "endTime");

        assertNotNull(ctx.getOperationName());
        assertEquals(ctx.getOperationName(), "operationName");

        assertNotNull(ctx.getPurpose());
        assertEquals(ctx.getPurpose(), "purpose");

        assertNotNull(ctx.getRequestUniqueId());
        assertEquals(ctx.getRequestUniqueId(), "requestUniqueId");

        assertNotNull(ctx.getStartTime());
        assertEquals(ctx.getStartTime(), "startTime");

        Context newContext = new Context(ctx);
        jsonString = Context.Writer.generate(newContext);
        bais       = new ByteArrayInputStream(jsonString.getBytes());
        ctx        = Context.Parser.parse(bais);
        assertNotNull(ctx);

        assertNotNull(ctx.getAction());
        assertEquals(ctx.getAction(), "action");

        assertNotNull(ctx.getApiParameters());
        assertNotNull(ctx.getBatch());
        assertNotNull(ctx.getData());
        assertNotNull(ctx.getQueryParameters());

        assertNotNull(ctx.getEndTime());
        assertEquals(ctx.getEndTime(), "endTime");

        assertNotNull(ctx.getOperationName());
        assertEquals(ctx.getOperationName(), "operationName");

        assertNotNull(ctx.getPurpose());
        assertEquals(ctx.getPurpose(), "purpose");

        assertNotNull(ctx.getRequestUniqueId());
        assertEquals(ctx.getRequestUniqueId(), "requestUniqueId");

        assertNotNull(ctx.getStartTime());
        assertEquals(ctx.getStartTime(), "startTime");
    }

    @Test
    public void testAuditLogEntry() {
        AuditLogEntry logEntry = new AuditLogEntry("componentId", "eventType", "timestamp", "componentIp", "tenantId");
        assertNotNull(logEntry);

        assertNotNull(logEntry.getComponentId());
        assertEquals(logEntry.getComponentId(), "componentId");

        assertNotNull(logEntry.getEventType());
        assertEquals(logEntry.getEventType(), "eventType");

        assertNotNull(logEntry.getTimestamp());
        assertEquals(logEntry.getTimestamp(), "timestamp");

        assertNotNull(logEntry.getComponentId());
        assertEquals(logEntry.getComponentId(), "componentId");

        assertNotNull(logEntry.getComponentIp());
        assertEquals(logEntry.getComponentIp(), "componentIp");

        assertNotNull(logEntry.getTenantId());
        assertEquals(logEntry.getTenantId(), "tenantId");

        ConfigData cData = ConfigData.builder().serverStartupParameters("test9").build();
        logEntry.setConfigData(cData);
        assertEquals(logEntry.getConfigData().getServerStartupParms(), "test9");

        logEntry.setClientCertCn("clientCertCn");
        assertEquals(logEntry.getClientCertCn(), "clientCertCn");

        logEntry.setClientCertIssuerOu("clientCertOu");
        assertEquals(logEntry.getClientCertIssuerOu(), "clientCertOu");

        logEntry.setDescription("setDescription");
        assertEquals(logEntry.getDescription(), "setDescription");

        logEntry.setUserName("setUserName");
        assertEquals(logEntry.getUserName(), "setUserName");

        logEntry.setPatientId("setPatientId");
        assertEquals(logEntry.getPatientId(), "setPatientId");

        logEntry.setCorrelationId("setCorrelationId");
        assertEquals(logEntry.getCorrelationId(), "setCorrelationId");

        logEntry.setLocation("setLocation");
        assertEquals(logEntry.getLocation(), "setLocation");

        Context ctx = Context.builder().action("test-action").build();
        logEntry.setContext(ctx);
        assertEquals(logEntry.getContext().getAction(), "test-action");
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testContextForcedException() throws FHIRException {
        Context.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testBatchForcedException() throws FHIRException {
        Batch.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testDataForcedException() throws FHIRException {
        Data.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testConfigDataForcedException() throws FHIRException {
        ConfigData.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testApiParametersForcedException() throws FHIRException {
        ApiParameters.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testFHIRContextForcedException() throws FHIRException {
        FHIRContext.FHIRParser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test
    public void testFHIRContext() throws IOException, FHIRException {
        FHIRContext emptyContext = FHIRContext.fhirBuilder().build();
        assertNotNull(emptyContext);

        FHIRContext ctx = new FHIRContext(emptyContext);
        assertNotNull(ctx);

        ApiParameters apiParameters = ApiParameters.builder().request("request").status(200).build();
        FHIRContext.FHIRBuilder builder = FHIRContext.fhirBuilder();
        builder.apiParameters(apiParameters);
        builder.action("action");
        builder.endTime("endTime");

        Batch batch = Batch.builder().resourcesCreated(10).resourcesRead(11).resourcesUpdated(12).status("200").build();
        builder.batch(batch);

        Data data = Data.builder().id("id").resourceType("resourceType").versionId("versionId").build();
        builder.data(data);
        builder.operationName("operationName");
        builder.purpose("purpose");
        builder.queryParameters("queryParameters");
        builder.requestUniqueId("requestUniqueId");
        builder.startTime("startTime");

        builder.clientCertCn("clientCertCn");
        builder.clientCertIssuerOu("clientCertIssuerOu");
        builder.description("description");
        builder.eventType("eventType");
        builder.location("location");

        FHIRContext fhirCtx = builder.build();
        assertNotNull(fhirCtx);

        String jsonString = FHIRContext.FHIRWriter.generate(fhirCtx);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        fhirCtx = FHIRContext.FHIRParser.parse(bais);
        assertNotNull(fhirCtx);

        assertNotNull(fhirCtx.getAction());
        assertEquals(fhirCtx.getAction(), "action");

        assertNotNull(fhirCtx.getApiParameters());
        assertNotNull(fhirCtx.getBatch());
        assertNotNull(fhirCtx.getData());
        assertNotNull(fhirCtx.getQueryParameters());

        assertNotNull(fhirCtx.getEndTime());
        assertEquals(fhirCtx.getEndTime(), "endTime");

        assertNotNull(fhirCtx.getOperationName());
        assertEquals(fhirCtx.getOperationName(), "operationName");

        assertNotNull(fhirCtx.getPurpose());
        assertEquals(fhirCtx.getPurpose(), "purpose");

        assertNotNull(fhirCtx.getRequestUniqueId());
        assertEquals(fhirCtx.getRequestUniqueId(), "requestUniqueId");

        assertNotNull(fhirCtx.getStartTime());
        assertEquals(fhirCtx.getStartTime(), "startTime");

        assertNotNull(fhirCtx.getDescription());
        assertEquals(fhirCtx.getDescription(), "description");

        assertNotNull(fhirCtx.getClient_cert_cn());
        assertEquals(fhirCtx.getClient_cert_cn(), "clientCertCn");

        assertNotNull(fhirCtx.getClient_cert_issuer_ou());
        assertEquals(fhirCtx.getClient_cert_issuer_ou(), "clientCertIssuerOu");

        assertNotNull(fhirCtx.getEventType());
        assertEquals(fhirCtx.getEventType(), "eventType");

        assertNotNull(fhirCtx.getLocation());
        assertEquals(fhirCtx.getLocation(), "location");
    }
}