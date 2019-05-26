/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.cadf.test;

import static org.testng.AssertJUnit.*;

import java.util.Date;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfEvent;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfParser;
import com.ibm.watsonhealth.fhir.audit.logging.beans.ApiParameters;
import com.ibm.watsonhealth.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Batch;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Context;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Data;
import com.ibm.watsonhealth.fhir.audit.logging.impl.WhcAuditCadfLogService;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;

public class AuditCadfTest {
    private static final String COMPONENT_ID = "fhir-server";
    private static final String timestamp = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis()));
    private static final String timestamp2 = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis() + 5000));
    private static final String componentIp = "192.168.12.28";
    private static final String tenantId = "WHC";
CODE_REMOVED
    private static final String clientCertIssuerOu = "Watson Health";
    private static final String correlationId = "8c909d89-e7d9-4acb-8ffd-0205432a592a";
    private static final String eventType = "fhir-create";
    private static final String userName = "8c909d89-e7d9-4acb-8ffd-0205432a592a";
CODE_REMOVED
    private static final int status = 201;
    private static final String resourceType = "Observation";
    private static final String resourceId = "0e837440-64eb-4f16-878e-12879a4cfe2d";
    private static final String versionId = "1";
    private static final String patientId = "8c909d89-e7d9-4acb-8ffd-0205432a592b";
    private static final String reqUniqueId = "417b2105-bc6b-44f7-9f23-4c784a17d24b";
    private static final String description = "FHIR Create request";

    private AuditLogEntry TestFhirLog1 = null;
    private AuditLogEntry TestFhirLog2 = null;

    public AuditCadfTest() {
        CadfEvent eventObject = null;
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

        try {
            eventObject = WhcAuditCadfLogService.createCadfEvent(TestFhirLog1);

            assertNotNull(eventObject);

            if (eventObject != null) {
                CadfParser parser = new CadfParser();
                String eventString = parser.cadf2Json(eventObject);

                assertNotNull(eventString);

                System.out.println(eventString);

            }

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @BeforeClass
    public void setUp() throws Exception {

    }

    @Test(groups = { "retrieval" })
    public void testRetrieve() throws Exception {

        assertEquals(1, 1);
    }

    @Test(groups = { "retrieval" })
    public void testRetrieveWithPagination() throws Exception {
        assertEquals(1, 1);

    }

    @Test(groups = { "retrieval" })
    public void testRetrieveIncludeDataDetails() throws Exception {
        assertEquals(1, 1);
    }

}
