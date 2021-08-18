/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.mapper.impl;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Date;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.audit.beans.ApiParameters;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.Batch;
import com.ibm.fhir.audit.beans.Context;
import com.ibm.fhir.audit.beans.Data;
import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.AuditEvent;
import com.ibm.fhir.model.type.Instant;

public class AuditEventMapperTest {
    private static final String COMPONENT_ID = "fhir-server";
    private static final String timestamp = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis()));
    private static final String timestamp2 = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis() + 5000));
    private static final String componentIp = "192.168.12.28";
    private static final String tenantId = "DummyTenant";
    private static final String clientCertCn = "";
    private static final String clientCertIssuerOu = "Watson Health";
    private static final String correlationId = "8c909d89-e7d9-4acb-8ffd-0205432a592a";
    private static final String eventType = "fhir-create";
    private static final String userName = "8c909d89-e7d9-4acb-8ffd-0205432a592a";
    private static final String request = "hostname";
    private static final int status = 201;
    private static final String resourceType = "Observation";
    private static final String resourceId = "0e837440-64eb-4f16-878e-12879a4cfe2d";
    private static final String versionId = "1";
    private static final String patientId = "8c909d89-e7d9-4acb-8ffd-0205432a592b";
    private static final String reqUniqueId = "417b2105-bc6b-44f7-9f23-4c784a17d24b";
    private static final String description = "FHIR Create request";

    private static final String operationName = "UnitTest";

    private AuditLogEntry logEntry = null;

    @BeforeClass
    public void setUp() throws Exception {
        logEntry = new AuditLogEntry(COMPONENT_ID, eventType, timestamp, componentIp, tenantId);
        logEntry.setClientCertCn(clientCertCn);
        logEntry.setClientCertIssuerOu(clientCertIssuerOu);
        logEntry.setCorrelationId(correlationId);
        logEntry.setUserName(userName);

        logEntry.setContext(new Context());
        logEntry.getContext().setApiParameters(ApiParameters.builder().request(request).status(status).build());

        logEntry.getContext().setStartTime(timestamp);
        logEntry.getContext().setEndTime(timestamp2);
        logEntry.getContext().setData(Data.builder().resourceType(resourceType).id(resourceId).versionId(versionId).build());

        logEntry.setPatientId(patientId);
        logEntry.getContext().setRequestUniqueId(reqUniqueId);
        logEntry.getContext().setAction("R");
        logEntry.getContext().setBatch(Batch.builder().resourcesCreated(5).resourcesRead(10).resourcesUpdated(2).build());
        logEntry.setDescription(description);
        logEntry.getContext().setOperationName(operationName);
        logEntry.setLocation("Location");
    }

    @Test
    public void testAuditEventMapper() throws Exception {
        AuditEventMapper mapper = new AuditEventMapper();
        String eventString = mapper.init(null).map(logEntry).serialize();
        ByteArrayInputStream bais = new ByteArrayInputStream(eventString.getBytes());
        AuditEvent auditEvent = FHIRParser.parser(Format.JSON).parse(bais);

        Instant now = Instant.now();
        auditEvent = auditEvent.toBuilder().recorded(now).build();

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON, false).generate(auditEvent, writer);
        String actual = writer.toString();
        String expected = "{\"resourceType\":\"AuditEvent\",\"type\":{\"system\":\"http://terminology.hl7.org/CodeSystem/audit-event-type\",\"code\":\"rest\",\"display\":\"Restful Operation\"},\"subtype\":[{\"system\":\"http://hl7.org/fhir/restful-interaction\",\"code\":\"create\",\"display\":\"create\"}],\"action\":\"C\",\"period\":{\"start\":\"2020-12-10T14:35:57.752Z\",\"end\":\"2020-12-10T14:36:02.779Z\"},\"recorded\":\"TIME\",\"outcome\":\"0\",\"outcomeDesc\":\"success\",\"purposeOfEvent\":[{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ActReason\",\"code\":\"PurposeOfUse\",\"display\":\"PurposeOfUse\"}]}],\"agent\":[{\"role\":[{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/extra-security-role-type\",\"code\":\"datacollector\",\"display\":\"datacollector\"}]}],\"name\":\"fhir-server\",\"requestor\":true,\"network\":{\"address\":\"192.168.12.28\",\"type\":\"1\"}}],\"source\":{\"site\":\"Location\",\"observer\":{\"reference\":\"Device/fhir-server\"},\"type\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/security-source-type\",\"code\":\"4\",\"display\":\"Application Server\"}]},\"entity\":[{\"securityLabel\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-Confidentiality\",\"code\":\"N\",\"display\":\"normal\"}],\"description\":\"FHIR Create request\",\"detail\":[{\"type\":\"FHIR Context\",\"valueBase64Binary\":\"CnsKICAgICJyZXF1ZXN0X3VuaXF1ZV9pZCI6ICI0MTdiMjEwNS1iYzZiLTQ0ZjctOWYyMy00Yzc4NGExN2QyNGIiLAogICAgImFjdGlvbiI6ICJSIiwKICAgICJvcGVyYXRpb25fbmFtZSI6ICJVbml0VGVzdCIsCiAgICAic3RhcnRfdGltZSI6ICIyMDIwLTEyLTEwIDE0OjM1OjU3Ljc1MiIsCiAgICAiZW5kX3RpbWUiOiAiMjAyMC0xMi0xMCAxNDozNjowMi43NzkiLAogICAgImFwaV9wYXJhbWV0ZXJzIjogewogICAgICAgICJyZXF1ZXN0IjogImhvc3RuYW1lIiwKICAgICAgICAicmVxdWVzdF9zdGF0dXMiOiAyMDEKICAgIH0sCiAgICAiZGF0YSI6IHsKICAgICAgICAicmVzb3VyY2VfdHlwZSI6ICJPYnNlcnZhdGlvbiIsCiAgICAgICAgImlkIjogIjBlODM3NDQwLTY0ZWItNGYxNi04NzhlLTEyODc5YTRjZmUyZCIsCiAgICAgICAgInZlcnNpb25faWQiOiAiMSIKICAgIH0sCiAgICAiYmF0Y2giOiB7CiAgICAgICAgInJlc291cmNlc19yZWFkIjogMTAsCiAgICAgICAgInJlc291cmNlc19jcmVhdGVkIjogNSwKICAgICAgICAicmVzb3VyY2VzX3VwZGF0ZWQiOiAyCiAgICB9LAogICAgImV2ZW50X3R5cGUiOiAiZmhpci1jcmVhdGUiLAogICAgImRlc2NyaXB0aW9uIjogIkZISVIgQ3JlYXRlIHJlcXVlc3QiLAogICAgImNsaWVudF9jZXJ0X2NuIjogIiIsCiAgICAiY2xpZW50X2NlcnRfaXNzdWVyX291IjogIldhdHNvbiBIZWFsdGgiLAogICAgImxvY2F0aW9uIjogIkxvY2F0aW9uIgp9\"}]}]}".replace("TIME", now.getValue().format(Instant.PARSER_FORMATTER));

        JSONAssert.assertEquals(expected, actual,
            new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("entity[0].detail[0].valueBase64Binary", (o1, o2) -> true),
                new Customization("entity[description=FHIR Create request].detail[type=FHIR Context].valueBase64Binary", (o1, o2) -> true),
                new Customization("period.end", (o1, o2) -> true),
                new Customization("period.start", (o1, o2) -> true)));
    }
}
