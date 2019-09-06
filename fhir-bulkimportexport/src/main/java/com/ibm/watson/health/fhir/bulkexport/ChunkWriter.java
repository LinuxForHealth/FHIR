/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;

/**
 * Bulk export Chunk implementation - the Writer.
 * 
 * @author Albert Wang
 */
public class ChunkWriter extends AbstractItemWriter {
    private final static Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    private AmazonS3 cosClient = null;
    /**
     * The IBM COS API key or S3 access key.
     */
    // eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--xxxx"
    @Inject
    @BatchProperty(name = "cos.api.key")
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    // eg
    // "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-xxxx-xxxx-xxxx-xxxxxxxxxxxx::"
    @Inject
    @BatchProperty(name = "cos.srvinst.id")
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.endpointurl")
    String cosEndpintUrl;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.location")
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.name")
    String cosBucketName;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.pathprefix")
    String cosBucketPathPrefix;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    private void getCosClient() {
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
        AWSCredentials credentials;
        if (cosCredentialIbm.equalsIgnoreCase("Y")) {
            credentials = new BasicIBMOAuthCredentials(cosApiKeyProperty, cosSrvinstId);
        } else {
            credentials = new BasicAWSCredentials(cosApiKeyProperty, cosSrvinstId);
        }

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(8000);
        clientConfig.setUseTcpKeepAlive(true);

        cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(cosEndpintUrl, cosLocation))
                .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
    }

    /**
     * @see AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    private void pushFhirJsons2Cos(String combinedJsons) throws Exception {

        if (cosClient == null)
            return;

        InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(combinedJsons.getBytes(StandardCharsets.UTF_8).length);

        String itemName = cosBucketPathPrefix + "/" + UUID.randomUUID().toString();

        PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, newStream, metadata);
        cosClient.putObject(req);

        log("pushFhirJsons2Cos", itemName + " was successfully written to COS");

    }

    /**
     * @see AbstractItemWriter#writeItems(List<java.lang.Object>)
     */
    @SuppressWarnings("unchecked")
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        getCosClient();

        if (cosClient == null) {
            log("BulkImportBatchLet", "Failed to get CosClient!");
        } else {
            log("process", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }

        cosBucketName = cosBucketName.toLowerCase();

        if (cosBucketPathPrefix == null) {
            cosBucketPathPrefix = UUID.randomUUID().toString();
        }

        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }

        for (Object resListObject : arg0) {
            List<String> resList = ((List<String>) resListObject);
            for (String resJsons : resList) {
                pushFhirJsons2Cos(resJsons);
            }

        }

    }

}
