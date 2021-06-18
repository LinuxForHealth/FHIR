package com.ibm.fhir.server.test.cqf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

import jakarta.json.JsonObject;

public class ServerCareGapsOperationTest extends FHIRServerTestBase {

    private static final String TEST_PATIENT_ID = "Patient/sally-fields";
    private static final String TEST_TOPIC = "test";
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
    public void testEvaluatePatientMeasure() {
        Response response =
                getWebTarget().path("/Measure/$care-gaps")
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .queryParam("topic", TEST_TOPIC)
                    .queryParam("subject", TEST_PATIENT_ID)
                    .request().get();
        assertResponse(response, 200);

        Bundle result = response.readEntity(Bundle.class);
        assertNotNull(result, "Null parameters list");
        assertEquals(result.getEntry().size(), 1);
    }
}
