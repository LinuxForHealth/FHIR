/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scale.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.config.ConfigurationService;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.persistence.cos.client.COSConfigAdapter;
import com.ibm.fhir.persistence.cos.client.COSPropertiesAdapter;
import com.ibm.fhir.persistence.cos.client.CosPropertyGroupAdapter;

/**
 * Unit test for COS payload configuration
 */
public class ConfigTest {
    private static final String TEST_CONFIG_FILE = "fhir-server-config.json";
    private static final String TEST_PROPERTIES_FILE = "cos-config-test.properties";

    @Test
    public void test1() throws Exception {
        PropertyGroup fhirServerConfig = ConfigurationService.loadConfiguration(TEST_CONFIG_FILE);
        assertNotNull(fhirServerConfig);
        
        PropertyGroup pg = fhirServerConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/default/connectionProperties");
        assertNotNull(pg);
        COSConfigAdapter adapter = new CosPropertyGroupAdapter(pg);
        assertEquals(adapter.getApiKey(), "apiKey1");
        assertEquals(adapter.getBucketName(), "bucket1");
        assertEquals(adapter.getEndpointUrl(), "endpoint1");
        assertEquals(adapter.getLocation(), "location1");
        assertEquals(adapter.getSrvInstId(), "srvInstId1");
        assertEquals(adapter.isCredentialIBM(), true);
        assertEquals(adapter.getSocketTimeout(), 7);
        assertEquals(adapter.getRequestTimeout(), 8);
        assertEquals(adapter.getMaxKeys(), 9);
        
        // Use the wrong path here on purpose so we can check the default
        COSConfigAdapter adapter2 = new CosPropertyGroupAdapter(fhirServerConfig);
        assertEquals(adapter2.getMaxKeys(), 1000); // check default
    }
    
    @Test
    public void testProperties() throws Exception {
        Properties props = new Properties();
        COSConfigAdapter adapter = new COSPropertiesAdapter(props);
        assertEquals(adapter.getMaxKeys(), 1000); // check default
        
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_PROPERTIES_FILE)) {
            props.load(is);
        }
        
        assertEquals(adapter.getApiKey(), "apiKey1");
        assertEquals(adapter.getBucketName(), "bucketName1");
        assertEquals(adapter.getEndpointUrl(), "endpointUrl1");
        assertEquals(adapter.getLocation(), "location1");
        assertEquals(adapter.getSrvInstId(), "srvInstid1");
        assertEquals(adapter.isCredentialIBM(), true);
        assertEquals(adapter.getSocketTimeout(), 7);
        assertEquals(adapter.getRequestTimeout(), 8);
        assertEquals(adapter.getMaxKeys(), 9);
    }
}