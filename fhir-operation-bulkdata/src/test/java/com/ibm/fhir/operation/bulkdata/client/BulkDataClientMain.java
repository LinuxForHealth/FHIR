/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.client;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;

/**
 * Client that Runs the BulkData Client Manually. 
 */
public class BulkDataClientMain {
    public static void main(String... args) throws Exception {
        Map<String,String> properties = new LinkedHashMap<>();
        properties.put(BulkDataConfigUtil.BATCH_URL, "https://localhost:9443/ibm/api/batch/jobinstances");
        properties.put(BulkDataConfigUtil.BATCH_USER, "fhiradmin");
        properties.put(BulkDataConfigUtil.BATCH_USER_PASS, "change-password");
                
        properties.put(BulkDataConfigUtil.BATCH_TRUSTSTORE,
                "/Users/paulbastide/git/wffh/wlp/wlp/usr/servers/defaultServer/resources/security/fhirTruststore.p12");
        properties.put(BulkDataConfigUtil.BATCH_TRUSTSTORE_PASS, "change-password");
        
        BulkDataClient client = new BulkDataClient(properties);
        
        // client.submit(null, null, Arrays.asList("Patient"), properties);
        
        client.status("9");
    }
}