/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader.util;

import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;

/*
 * This is a helper class capable of retrieving cloud object store (COS) objects. It relies on environment variables for configuration.
 */
public class COSObject {
    private static final Logger LOG = Logger.getLogger(COSObject.class.getName());

    private static final String COS_AUTH_ENDPOINT = "TERM_COS_AUTH_ENDPOINT";
    private static final String COS_API_KEY_ID = "TERM_COS_API_KEY_ID";
    private static final String COS_BUCKET_LOCATION = "TERM_COS_BUCKET_LOCATION";
    private static final String COS_ENDPOINT = "TERM_COS_ENDPOINT";
    private static final String COS_SERVICE_CRN = "TERM_COS_SERVICE_CRN";
    
    /**
     * Create client connection
     */
    private static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url,
            String location) {
        AWSCredentials credentials;
        credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint_url, location))
                .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
        return cosClient;
    }

    private static final String getEnvVar(String name) throws IllegalArgumentException {
        String value = System.getenv(name);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("No value for: " + name);
        }
        return value;
    }
    
    /**
     * Retrieve a COS object as InputStreamReader. 
     * NOTE: Caller is responsible for closing reader to avoid connection leak
     */
    public static InputStreamReader getItem(String bucketName, String itemName) {
        LOG.log(Level.INFO, "Retrieving item from bucket: " + bucketName + ", key: " + itemName + "\n");

        SDKGlobalConfiguration.IAM_ENDPOINT = getEnvVar(COS_AUTH_ENDPOINT); // e.g. "https://iam.cloud.ibm.com/identity/token"

        try {
            String cosAPIKeyId = getEnvVar(COS_API_KEY_ID); // e.g. "KrgAwCqAIxyRI855y0oVaeQopHokkqFNiReyQuwHQnDY" (not a real key)
            String cosBucketLocation = getEnvVar(COS_BUCKET_LOCATION); // e.g. "us"
            String cosEndPoint = getEnvVar(COS_ENDPOINT); // e.g. "https://s3.us-east.cloud-object-storage.appdomain.cloud"
            String cosServiceCRN = getEnvVar(COS_SERVICE_CRN); // e.g. "crn:v1:bluemix:public:cloud-object-storage:global:a/6694a1bda7d84197b130c3ea87ef3e77:131a8d10-c8ff-4e05-b344-921abee60bcc::" (not a real CRN)
            
            AmazonS3 _cos = createClient(cosAPIKeyId, cosServiceCRN, cosEndPoint, cosBucketLocation);
            S3Object item = _cos.getObject(new GetObjectRequest(bucketName, itemName));
            return new InputStreamReader(item.getObjectContent());
        } catch (SdkClientException e) {
            LOG.log(Level.SEVERE, "Error getting object from cloud object storage", e);
        }
        return null;
    }
}