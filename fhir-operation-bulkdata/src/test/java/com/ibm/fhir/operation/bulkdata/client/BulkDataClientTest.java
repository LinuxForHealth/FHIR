/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;

/**
 * Test Bulk Data Client
 */
public class BulkDataClientTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes/testdata");
    }

    @Test
    public void testBulkDataClient() throws Exception {
        BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();
        String fileName = cache.getCacheEntryFilename("default");
        assertEquals(fileName, "target/test-classes/testdata/config/default/bulkdata.json");
        Map<String, String> props = cache.getCachedObjectForTenant("default");
        BulkDataClient client = new BulkDataClient(props);
        assertNotNull(client);
    }
}