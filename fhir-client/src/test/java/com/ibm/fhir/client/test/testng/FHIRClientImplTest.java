/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test.testng;

import static org.testng.Assert.assertNotNull;

import java.util.Properties;

import javax.ws.rs.client.WebTarget;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRClientFactory;

/**
 * Tests FHIR Client configurations.
 */
public class FHIRClientImplTest {

    @Test
    public void testClientImplWithoutKeyStore() throws Exception {
        String baseURL = "http://localhost:9443/test";
        Properties properties = new Properties();
        properties.put(FHIRClient.PROPNAME_BASE_URL, baseURL);
        FHIRClient client = FHIRClientFactory.getClient(properties);
        WebTarget wt = client.getWebTarget(baseURL + "/Patient?_page=2");
        assertNotNull(wt);
    }
}