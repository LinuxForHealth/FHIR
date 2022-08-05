/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.test.cpg;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.code.IssueType;

import jakarta.json.JsonObject;

public class ServerCqlOperationTest extends BaseCPGOperationTest {

    private static final String TEST_PATIENT_ID = "Patient/sally-fields";

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        setUp(testProperties);
        
        JsonObject jsonObject = TestUtil.readJsonObject("testdata/Patient_SallyFields.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = getWebTarget().path("/" + TEST_PATIENT_ID).request().put( entity );
        assertResponse( response, Response.Status.Family.SUCCESSFUL );
    }
    
    @Test
    public void testEvaluateArbitraryCql() {
        Response response = getWebTarget().path("$cql").queryParam("expression", "Patient.gender").queryParam("subject", TEST_PATIENT_ID).request().get();
        assertResponse( response, 200 );
        
        Parameters parameters = response.readEntity(Parameters.class);
        assertNotNull(parameters.getParameter(), "Null parameters list");
        assertEquals(parameters.getParameter().size(), 1);
        
        Parameter pReturn = parameters.getParameter().get(0);
        assertEquals(pReturn.getName().getValue(), "return");
    }
    
    @Test
    public void testEvaluateArbitraryCqlGenderComparison() {
        Response response = getWebTarget().path("$cql").queryParam("expression", "[Patient] p where p.gender = 'female'").queryParam("subject", TEST_PATIENT_ID).request().get();
        assertResponse( response, 200 );
        
        Parameters parameters = response.readEntity(Parameters.class);
        assertNotNull(parameters.getParameter(), "Null parameters list");
        assertEquals(parameters.getParameter().size(), 1);
        
        Parameter pReturn = parameters.getParameter().get(0);
        assertEquals(pReturn.getName().getValue(), "return");
    }
    
    @Test
    public void testEvaluateArbitraryCqlUsesResourceID() {
        Response response = getWebTarget().path("$cql").queryParam("expression", "[Patient] p return Last(Split(p.id,'/'))").queryParam("subject", TEST_PATIENT_ID).request().get();
        assertResponse( response, 200 );
        
        Parameters parameters = response.readEntity(Parameters.class);
        assertNotNull(parameters.getParameter(), "Null parameters list");
        assertEquals(parameters.getParameter().size(), 1);
        
        Parameter pReturn = parameters.getParameter().get(0);
        assertEquals(pReturn.getName().getValue(), "return");
        
        Parameter pPart = pReturn.getPart().get(0);
        assertEquals(pPart.getValue(), org.linuxforhealth.fhir.model.type.String.of("sally-fields"));
    }
    
    @Test
    public void testEvaluateArbitraryCqlCompileError() {
        Response response = getWebTarget().path("$cql").queryParam("expression", "[NonResource]").queryParam("subject", TEST_PATIENT_ID).request().get();
        assertResponse( response, 400 );
        
        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        System.out.println(outcome.toString());
        
        assertEquals( outcome.getIssue().get(0).getCode(), IssueType.INVALID);
    }
    
    @Test
    public void testEvaluateArbitraryCqlWithDebug() {
        Response response = getWebTarget().path("$cql").queryParam("expression", "Patient.gender").queryParam("subject", TEST_PATIENT_ID).queryParam("debug", "true").request().get();
        assertResponse( response, 200 );
        
        Parameters parameters = response.readEntity(Parameters.class);
        assertNotNull(parameters.getParameter(), "Null parameters list");
        assertEquals(parameters.getParameter().size(), 2);
    }
    
    @Test
    public void testUsefulDetailIncludedInMissingPatientResponse() {
        Response response = getWebTarget()
                .path("$cql")
                .queryParam("expression", "Patient.gender")
                .queryParam("subject", "Patient/does-not-exist")
                .request()
                .get();
        assertResponse(response, 500);

        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("Resource 'Patient/does-not-exist' not found."), responseBody);
    }
}
