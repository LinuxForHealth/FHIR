/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test.oauth;

import static org.testng.AssertJUnit.assertNotNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.test.FHIRClientTestBase;
import com.ibm.fhir.model.test.TestUtil;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

/**
 * OAuth 2.0 tests used to register a client and generate an access token
 */
public class FHIROAuth2Test extends FHIRClientTestBase {
    private static boolean ON = false;
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    private String accessToken = null;
    private String clientID = null;
    private String clientSecret = null;

    private final String oidcRegURL = "https://localhost:9443/oidc/endpoint/oidc-provider/registration";
    private final String tokenURL = "https://localhost:9443/oauth2/endpoint/oauth2-provider/token";

    @BeforeClass
    public void checkIfOn() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.client.oauth.enabled", "false"));
    }

    @Test
    public void testRegisterClient() throws Exception {
        if (ON) {
            // Open the file.
            Reader reader = new InputStreamReader(TestUtil.resolveFileLocation("SampleClientForOpenIDRegistration.json"));

            int data = reader.read();
            StringBuffer jsonStr = new StringBuffer();
            while (data != -1) {
                jsonStr.append((char) data);
                data = reader.read();
            }

            reader.close();

            JsonObject jsonObj;
            try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(new StringReader(jsonStr.toString()))) {
                jsonObj = jsonReader.readObject();
                jsonReader.close();
                assertNotNull(jsonObj);
            }

            WebTarget endpoint = clientOAuth2.getWebTarget(oidcRegURL);
            Entity<JsonObject> entity = Entity.entity(jsonObj, "application/json");
            Invocation.Builder builder = endpoint.request("application/json");
            Response response = builder.post(entity);

            assertNotNull(response);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            JsonObject resJson = response.readEntity(JsonObject.class);
            assertNotNull(resJson);
            assertNotNull(resJson.get("client_id"));
            assertNotNull(resJson.get("client_secret"));
            clientID = resJson.get("client_id").toString();
            clientSecret = resJson.get("client_secret").toString();

            System.out.println("clientID = " + clientID);
            System.out.println("clientSecret = " + clientSecret);
        } else {
            System.out.println("testTokenRequest Disabled, Skipping");
        }
    }

    @Test(dependsOnMethods = { "testRegisterClient" })
    public void testTokenRequest() throws Exception {
        if (ON) {
            WebTarget endpoint = clientOAuth2.getWebTarget(tokenURL);
            Form form = new Form();
            form.param("grant_type", "client_credentials").param("client_id", clientID.replaceAll("\"", "")).param("client_secret", clientSecret.replaceAll("\"", ""));

            Entity<Form> entity = Entity.form(form);
            Invocation.Builder builder = endpoint.request("application/json");
            Response res = builder.post(entity);
            assertResponse(res, Response.Status.OK.getStatusCode());
            JsonObject resJson = res.readEntity(JsonObject.class);
            assertNotNull(resJson.get("access_token"));
            accessToken = resJson.get("access_token").toString();
            System.out.println("accessToken = " + accessToken);
        } else {
            System.out.println("testTokenRequest Disabled, Skipping");
        }
    }
}