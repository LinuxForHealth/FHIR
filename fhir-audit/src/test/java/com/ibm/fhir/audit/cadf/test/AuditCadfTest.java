/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.audit.AuditLogServiceConstants;
import com.ibm.fhir.audit.beans.ApiParameters;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.Batch;
import com.ibm.fhir.audit.beans.Context;
import com.ibm.fhir.audit.beans.Data;
import com.ibm.fhir.audit.cadf.CadfEvent;
import com.ibm.fhir.audit.configuration.type.IBMEventStreamsType;
import com.ibm.fhir.audit.configuration.type.IBMEventStreamsType.EventStreamsCredentials;
import com.ibm.fhir.audit.impl.KafkaService;
import com.ibm.fhir.audit.mapper.impl.CADFMapper;
import com.ibm.fhir.config.ConfigurationService;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRUtilities;

public class AuditCadfTest {
    private static final boolean debug = false;

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
    private static final String testAPIKey = "eM1QRXSDl3IsblND_8y1Si_Ll5UW03KvuTPMxYYYYYY";
    private static final String eventStreamBinding =
            "{\r\n" + "  \"api_key\": \"" + testAPIKey + "\",\r\n"
                    + "  \"apikey\": \"" + testAPIKey + "\",\r\n"
                    + "  \"iam_apikey_description\": \"Auto-generated for key BLANK\",\r\n"
                    + "  \"iam_apikey_name\": \"Service credentials-1\",\r\n"
                    + "  \"iam_role_crn\": \"crn:v1:bluemix:public:iam::::serviceRole:Writer\",\r\n"
                    + "  \"iam_serviceid_crn\": \"crn:v1:bluemix:public:iam-identity::a/BLANK::serviceid:BLANK\",\r\n"
                    + "  \"instance_id\": \"BLANK\",\r\n"
                    + "  \"kafka_admin_url\": \"https://HOSTNAME\",\r\n"
                    + "  \"kafka_brokers_sasl\": [\r\n"
                    + "    \"broker-1:9093\",\r\n"
                    + "    \"broker-2:9093\"\r\n" + "  ],\r\n"
                    + "  \"kafka_http_url\": \"https://hostname\",\r\n"
                    + "  \"password\": \"invalid\",\r\n" + "  \"user\": \"token\"\r\n"
                    + "}\r\n";
    private static final String operationName = "UnitTest";

    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);

    private JsonObject jsonObjKafkaMapper = null;

    private AuditLogEntry TestFhirLog1 = null;

    public AuditCadfTest() {

    }

    @BeforeClass
    public void setUp() throws Exception {
        TestFhirLog1 = new AuditLogEntry(COMPONENT_ID, eventType, timestamp, componentIp, tenantId);
        TestFhirLog1.setClientCertCn(clientCertCn);
        TestFhirLog1.setClientCertIssuerOu(clientCertIssuerOu);
        TestFhirLog1.setCorrelationId(correlationId);
        TestFhirLog1.setUserName(userName);

        TestFhirLog1.setContext(new Context());
        TestFhirLog1.getContext()
                .setApiParameters(ApiParameters.builder().request(request).status(status).build());

        TestFhirLog1.getContext().setStartTime(timestamp);
        TestFhirLog1.getContext().setEndTime(timestamp2);
        TestFhirLog1.getContext()
                .setData(Data.builder().resourceType(resourceType).id(resourceId).versionId(versionId).build());

        TestFhirLog1.setPatientId(patientId);
        TestFhirLog1.getContext().setRequestUniqueId(reqUniqueId);
        TestFhirLog1.getContext().setAction("R");
        TestFhirLog1.getContext().setBatch(
                Batch.builder().resourcesCreated(5).resourcesRead(10).resourcesUpdated(2).build());
        TestFhirLog1.setDescription(description);
        TestFhirLog1.getContext().setOperationName(operationName);

        //@formatter:off
        jsonObjKafkaMapper = BUILDER_FACTORY.createObjectBuilder()
                            .add("auditTopic","auditTopicValue")
                            .add("mapper","cadf")
                            .add("kafka",
                                BUILDER_FACTORY.createObjectBuilder()
                                    .build())
                        .build();
        //@formatter:on
    }

    // Enable this only if you have kafka properly configured in fhirConfig.json
    @Test(enabled = false)
    public void testEventStream() throws Exception {

        PropertyGroup pg;
        try {
            pg = ConfigurationService.loadConfiguration("fhirConfig.json");
            assertNotNull(pg);

            PropertyGroup AuditProps = pg.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
            assertNotNull(AuditProps);

            if (debug) {
                System.out.println(
                        AuditProps.getStringProperty(AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS));
                System.out.println(AuditProps.getStringProperty(AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_APIKEY));
            }

            KafkaService logService = new KafkaService();
            logService.initialize(AuditProps);

            logService.logEntry(TestFhirLog1);

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to send log to eventStream!");
        }
    }

    @Test(groups = { "parser" })
    public void testEventParser() throws Exception {
        CadfEvent eventObject = null;

        try {
            CADFMapper mapper = new CADFMapper();
            mapper = (CADFMapper) mapper.init(new PropertyGroup(jsonObjKafkaMapper));
            eventObject = mapper.createCadfEvent(TestFhirLog1);

            assertNotNull(eventObject);

            if (eventObject != null) {
                String eventString = CadfEvent.Writer.generate(eventObject);

                assertNotNull(eventString);
                if (debug) {
                    System.out.println(eventString);
                }
            }

            TestFhirLog1.getContext().setEndTime(timestamp);
            eventObject = mapper.createCadfEvent(TestFhirLog1);

            assertNotNull(eventObject);

            if (eventObject != null) {
                String eventString = CadfEvent.Writer.generate(eventObject);

                assertNotNull(eventString);
                if (debug) {
                    System.out.println(eventString);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to parse event!");
        }
    }

    @Test(groups = { "parser" })
    public void testConfigParser() throws Exception {
        IBMEventStreamsType.EventStreamsCredentials esBinding = null;
        try {
            esBinding = EventStreamsCredentials.Parser.parse(eventStreamBinding);
            assertNotNull(esBinding);

            String bootstrapServers = IBMEventStreamsType.stringArrayToCSV(esBinding.getKafkaBrokersSasl());
            String apiKey = esBinding.getApiKey();

            assertNotNull(bootstrapServers);
            assertEquals(apiKey, testAPIKey);

            if (debug) {
                System.out.println(bootstrapServers);
                System.out.println(apiKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to parse eventStreamBinding!");
        }
    }
}