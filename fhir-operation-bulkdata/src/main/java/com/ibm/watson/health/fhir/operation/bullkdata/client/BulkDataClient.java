/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.client;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.operation.bullkdata.config.BulkDataConfigUtil;
import com.ibm.watson.health.fhir.operation.bullkdata.model.BulkExportJobInstanceRequest;
import com.ibm.watson.health.fhir.operation.bullkdata.model.BulkExportJobInstanceResponse;

/**
 * BulkData Client to connect to the other server. 
 * 
 * @author pbastide 
 *
 */
public class BulkDataClient {
    
    public WebTarget getWebTarget(String baseURL) throws Exception {
       
        ClientBuilder cb =
                ClientBuilder.newBuilder();
        
        /*
         * uses the jvm's default keystore/truststore. 
         */
        
        Client client = cb.build();
        return client.target(baseURL);
    }

    /**
     * @param outputFormat
     * @param since
     * @param types
     * @param properties
     * @return
     * @throws Exception 
     */
    public String submit(MediaType outputFormat, Instant since, List<String> types,
        Map<String, String> properties) throws Exception {
        String baseUrl = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT);
        String bucket = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET);
        
        // Need to push this into a property. 
        WebTarget target = getWebTarget("https://localhost:9443/ibm/api/batch/jobinstances");
        
        BulkExportJobInstanceRequest.Builder builder = BulkExportJobInstanceRequest.builder();
        
        Entity<BulkExportJobInstanceRequest> entity =  Entity.entity(builder.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response r = target.request().post(entity);
        
        BulkExportJobInstanceResponse response = r.readEntity(BulkExportJobInstanceResponse.class);
        
        // From the response
        String jobName = response.getJobName();
        String resourceType = types.get(0);
        
        return baseUrl + "/" + bucket + "/" + jobName + "_" + resourceType + ".ndjson";
    }
}
