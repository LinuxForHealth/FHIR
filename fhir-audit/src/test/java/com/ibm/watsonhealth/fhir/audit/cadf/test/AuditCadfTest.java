/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.cadf.test;

import static org.testng.AssertJUnit.*;

import java.util.Date;

//import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.*;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfEvent;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfParser;
import com.ibm.watsonhealth.fhir.audit.kafka.Environment;
import com.ibm.watsonhealth.fhir.audit.kafka.EventStreamsCredentials;
import com.ibm.watsonhealth.fhir.audit.logging.beans.ApiParameters;
import com.ibm.watsonhealth.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Batch;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Context;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Data;
import com.ibm.watsonhealth.fhir.audit.logging.impl.WhcAuditCadfLogService;
import com.ibm.watsonhealth.fhir.config.ConfigurationService;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;

public class AuditCadfTest {
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
    private static final String eventStreamBinding = "{\r\n" + "  \"api_key\": \"" + testAPIKey + "\",\r\n"
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
        TestFhirLog1.getContext().setApiParameters(new ApiParameters().withRequest(request).withStatus(status));

        TestFhirLog1.getContext().setStartTime(timestamp);
        TestFhirLog1.getContext().setEndTime(timestamp2);
        TestFhirLog1.getContext()
                .setData(new Data().withResourceType(resourceType).withId(resourceId).withVersionId(versionId));

        TestFhirLog1.setPatientId(patientId);
        TestFhirLog1.getContext().setRequestUniqueId(reqUniqueId);
        TestFhirLog1.getContext().setAction("R");
        TestFhirLog1.getContext().setBatch(
                new Batch().withResourcesCreated((long) 5).withResourcesRead((long) 10).withResourcesUpdated((long) 2));
        TestFhirLog1.setDescription(description);      
        TestFhirLog1.getContext().setOperationName(operationName);
    }

    // Enable this only if you have kafka properly configured in fhirConfig.json
    @Test(enabled=false) 
    public void testEventStream() throws Exception {

        PropertyGroup pg;
        try {
            pg = ConfigurationService.loadConfiguration("fhirConfig.json");
            assertNotNull(pg);

            PropertyGroup AuditProps = pg.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
            assertNotNull(AuditProps);

            System.out.println(
                    AuditProps.getStringProperty(WhcAuditCadfLogService.PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS));
            System.out.println(AuditProps.getStringProperty(WhcAuditCadfLogService.PROPERTY_AUDIT_KAFKA_APIKEY));

            WhcAuditCadfLogService logService = new WhcAuditCadfLogService();
            logService.initialize(AuditProps);
            
            logService.logEntry(TestFhirLog1);

        } catch (Exception e) {
            e.printStackTrace();
             fail("failed to send log to enventStream!");
        }
    }

    @Test(groups = { "parser" })
    public void testEventParser() throws Exception {
        CadfEvent eventObject = null;

        try {
            eventObject = WhcAuditCadfLogService.createCadfEvent(TestFhirLog1);

            assertNotNull(eventObject);

            if (eventObject != null) {
                CadfParser parser = new CadfParser();
                String eventString = parser.cadf2Json(eventObject);

                assertNotNull(eventString);
                System.out.println(eventString);
            }
            
            TestFhirLog1.getContext().setEndTime(timestamp);
            eventObject = WhcAuditCadfLogService.createCadfEvent(TestFhirLog1);
            
            assertNotNull(eventObject);

            if (eventObject != null) {
                CadfParser parser = new CadfParser();
                String eventString = parser.cadf2Json(eventObject);

                assertNotNull(eventString);
                System.out.println(eventString);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to parser event!");
        }
    }

    @Test(groups = { "parser" })
    public void testConfigParser() throws Exception {
        EventStreamsCredentials ESbinding = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ESbinding = mapper.readValue(eventStreamBinding, EventStreamsCredentials.class);
            assertNotNull(ESbinding);

            if (ESbinding != null) {
                String bootstrapServers = Environment.stringArrayToCSV(ESbinding.getKafkaBrokersSasl());
                String apiKey = ESbinding.getApiKey();

                assertNotNull(bootstrapServers);
                assertEquals(apiKey, testAPIKey);

                System.out.println(bootstrapServers);
                System.out.println(apiKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to parser eventStreamBinding!");
        }
    }

}
