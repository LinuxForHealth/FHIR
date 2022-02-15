/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.cqf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.StringReader;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.OperationOutcome;

public class ServerMeasureCollectDataOperationTest extends BaseMeasureOperationTest {

    @Test
    public void testEvaluatePatientMeasureResourceType() throws Exception {
        Response response =
                getWebTarget().path("/Measure/$collect-data")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("measure", TEST_MEASURE_URL)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        MeasureReport report = (MeasureReport) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertEquals(report.getSubject().getReference().getValue(), TEST_PATIENT_ID);
    }

    @Test
    public void testEvaluatePatientMeasureInstance() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{measureId}/$collect-data")
                    .resolveTemplate("measureId", TEST_MEASURE_ID)
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        MeasureReport report = (MeasureReport) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertEquals(report.getSubject().getReference().getValue(), TEST_PATIENT_ID);
    }
    
    @Test
    public void testCollectDataPatientMeasureInstanceLibraryNotFound() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{measureId}/$collect-data")
                    .resolveTemplate("measureId", "MissingLibrary")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        assertResponse(response, 500);

        OperationOutcome outcome = (OperationOutcome) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        String details = outcome.getIssue().get(0).getDetails().getText().getValue();
        assertTrue(details.contains("Failed to resolve"));
        assertTrue(details.contains("NotExists"), details);        
    }
}
