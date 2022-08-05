/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.test.cqf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.StringReader;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.Period;

public class ServerEvaluateMeasureOperationTest extends BaseMeasureOperationTest {

    @Test
    public void testEvaluatePatientMeasureResourceType() throws Exception {
        Response response =
                getWebTarget().path("/Measure/$evaluate-measure")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("measure", TEST_MEASURE_URL)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        MeasureReport report = FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertEquals(report.getSubject().getReference().getValue(), TEST_PATIENT_ID);
    }
    
    @Test
    public void testEvaluatePatientMeasureInstance() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{measureId}/$evaluate-measure")
                    .resolveTemplate("measureId", TEST_MEASURE_ID)
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        MeasureReport report = FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertEquals(report.getSubject().getReference().getValue(), TEST_PATIENT_ID);
        
        Period expectedPeriod = getPeriod(TEST_PERIOD_START, TEST_PERIOD_END);
        assertEquals( report.getPeriod(), expectedPeriod );
    }
    
    @Test
    public void testEvaluatePatientMeasureMissingPatient() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{measureId}/$evaluate-measure")
                    .resolveTemplate("measureId", TEST_MEASURE_ID)
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", "Patient/not-exists")
                    .request().get();
        assertResponse(response, 500);

        String responseBody = response.readEntity(String.class);
        System.out.println(responseBody);
        assertTrue(responseBody.contains("Resource 'Patient/not-exists' not found."), responseBody);
    }
    
    @Test
    public void testEvaluatePatientMeasureInstanceLibraryNotFound() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{measureId}/$evaluate-measure")
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
        assertTrue(details.contains("Failed to resolve Library"));
        assertTrue(details.contains("NotExists"), details);
    }
    
    @Test
    public void testEvaluatePatientMeasureResourceTypeLibraryNotFound() throws Exception {
        Response response =
                getWebTarget().path("/Measure/$evaluate-measure")
                    .queryParam("measure", "Measure/MissingLibrary")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        assertResponse(response, 500);

        OperationOutcome outcome = (OperationOutcome) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        String details = outcome.getIssue().get(0).getDetails().getText().getValue();
        assertTrue(details.contains("Failed to resolve Library"), details);
        assertTrue(details.contains("NotExists"), details);        
    }
    
    @Test
    public void testEvaluatePatientMeasureResourceTypeNoLibrary() throws Exception {
        Response response =
                getWebTarget().path("/Measure/$evaluate-measure")
                    .queryParam("measure", "Measure/NoLibrary")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        assertResponse(response, 500);

        OperationOutcome outcome = (OperationOutcome) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        String details = outcome.getIssue().get(0).getDetails().getText().getValue();
        assertTrue(details.contains("Measures utilizing CQL SHALL reference one and only one CQL library"), details);        
    }
}
