package com.ibm.fhir.server.test.cqf;

import static org.testng.Assert.assertEquals;

import java.io.StringReader;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

import jakarta.json.JsonObject;

public class ServerEvaluateMeasureOperationTest extends FHIRServerTestBase {

    private static final String TEST_PATIENT_ID = "Patient/sally-fields";
    private static final String TEST_MEASURE_ID = "8-auq9X1o0TxTYLhRGlt8ISNC1w7ELS5sKyhmUTj4SE";
    private static final String TEST_MEASURE_URL = "http://ibm.com/health/Measure/EXM74";
    private static final String TEST_PERIOD_START = "2010-01-01";
    private static final String TEST_PERIOD_END = "2010-12-31";

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        setUp(testProperties);

        JsonObject jsonObject = TestUtil.readJsonObject("testdata/Patient_SallyFields.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = getWebTarget().path("/" + TEST_PATIENT_ID).request().put(entity);
        assertResponse(response, Response.Status.Family.SUCCESSFUL);
        
        jsonObject = TestUtil.readJsonObject("testdata/Bundle-EXM74-10.2.000.json");
        entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = getWebTarget().request().post( entity );
        assertResponse( response, Response.Status.Family.SUCCESSFUL );
    }

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
        assertEquals(report.getPeriod().getStart().toString(), TEST_PERIOD_START);
        assertEquals(report.getPeriod().getEnd().toString(), TEST_PERIOD_END);
    }
}
