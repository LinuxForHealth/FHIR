/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.server.resources.WellKnown;

import jakarta.json.JsonObject;

public class WellKnownTest {

    @BeforeClass
    void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @AfterClass
    void tearDown() throws FHIRException {
        FHIRConfiguration.setConfigHome("");
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    void testSmartCapabilities_smart_enabled() throws Exception {
        FHIRRequestContext.get().setTenantId("smart-enabled");
        FHIRRequestContext.get().setOriginalRequestUri("http://example.com/.well-known/smart-configuration");
        WellKnownChild c = new WellKnownChild();

        Response capabilities = c.smartConfig();
        JsonObject smartSupport = capabilities.readEntity(JsonObject.class);
        assertTrue(!smartSupport.getString("authorization_endpoint").isEmpty());
        assertTrue(!smartSupport.getString("token_endpoint").isEmpty());
        assertEquals(smartSupport.getJsonArray("scopes_supported").size(), 6, "Number of SMART scopes");
        assertEquals(smartSupport.getJsonArray("capabilities").size(), 7, "Number of SMART capabilities");
    }

    @Test
    void testSmartCapabilities_smart_omitted() throws Exception {
        FHIRRequestContext.get().setTenantId("omitted");
        FHIRRequestContext.get().setOriginalRequestUri("http://example.com/.well-known/smart-configuration");
        WellKnownChild c = new WellKnownChild();

        Response capabilities = c.smartConfig();
        JsonObject smartSupport = capabilities.readEntity(JsonObject.class);
        assertTrue(smartSupport.getString("authorization_endpoint").isEmpty());
        assertTrue(smartSupport.getString("token_endpoint").isEmpty());
        assertNull(smartSupport.getJsonArray("scopes_supported"));
        assertEquals(smartSupport.getJsonArray("capabilities").size(), 0, "Number of SMART capabilities");
    }

    @Test
    void testSmartCapabilities_smart_empty() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");
        FHIRRequestContext.get().setOriginalRequestUri("http://example.com/.well-known/smart-configuration");
        WellKnownChild c = new WellKnownChild();

        Response capabilities = c.smartConfig();
        JsonObject smartSupport = capabilities.readEntity(JsonObject.class);
        System.out.println(smartSupport);
        assertTrue(smartSupport.getString("authorization_endpoint").isEmpty());
        assertTrue(smartSupport.getString("token_endpoint").isEmpty());
        assertNull(smartSupport.getJsonArray("scopes_supported"));
        assertEquals(smartSupport.getJsonArray("capabilities").size(), 0, "Number of SMART capabilities");
    }

    /**
     * This class is required because the WellKnown class uses a few protected fields
     * that are normally injected by JAX-RS and so this is the only way to set them.
     */
    private static class WellKnownChild extends WellKnown {
        public WellKnownChild() throws Exception {
            super();
            this.context = new MockServletContext();
        }

        @Override
        public Response smartConfig() throws ClassNotFoundException {
            httpServletRequest = new MockHttpServletRequest(FHIRVersionParam.VERSION_43);
            return super.smartConfig();
        }
    }
}


