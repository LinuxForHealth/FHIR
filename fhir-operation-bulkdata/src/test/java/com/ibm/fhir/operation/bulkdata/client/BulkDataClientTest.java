/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client;

import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;

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
        Map<String, String> props = BulkDataConfigUtil.getBatchJobConfig();
        BulkDataClient client = new BulkDataClient(props);
        assertNotNull(client);
    }
}