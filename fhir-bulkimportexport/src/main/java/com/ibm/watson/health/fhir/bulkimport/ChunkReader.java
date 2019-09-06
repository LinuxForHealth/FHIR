/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkimport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
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
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;

/**
 * Bulk import Chunk implementation - the Reader.
 * 
 * @author Albert Wang
 */
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    private AmazonS3 cosClient = null;

    int pageNum = 0;
    String nextToken = "";

    /**
     * The IBM COS API key or S3 access key.
     */
    // eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--xxxxx"
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

    private String getItem(String bucketName, String itemName) {
        S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));

        try (InputStreamReader in = new InputStreamReader(item.getObjectContent())) {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();

            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            logger.log(Level.FINER, out.toString());
            return out.toString();
        } catch (IOException ioe) {
            log("getItem", "Error reading file " + itemName);
            return null;
        }
    }

    /**
     * @see AbstractItemReader#AbstractItemReader()
     */
    public ChunkReader() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @throws Exception
     * @see AbstractItemReader#readItem()
     */
    public Object readItem() throws Exception {
        List<String> resJsonList = new ArrayList<String>();
        log("readItem", "Begin get CosClient!");
        getCosClient();

        if (cosClient == null) {
            log("readItem", "Failed to get CosClient!");
            return null;
        } else {
            log("readItem", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }

        cosBucketName = cosBucketName.toLowerCase();

        if (!cosClient.doesBucketExist(cosBucketName)) {
            log("readItem", "Bucket not found!");
            return null;
        }

        // Control the number of COS objects to read in each "item".
        int maxKeys = 3;

        if (nextToken.contentEquals("ALLDONE")) {
            return null;
        }

        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(maxKeys)
                .withContinuationToken(nextToken);

        ListObjectsV2Result result = cosClient.listObjectsV2(request);
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            log("readItem", "Item: " + objectSummary.getKey() + ", Bytes: " + objectSummary.getSize());

            String objectContents = getItem(cosBucketName, objectSummary.getKey());
            if (objectContents != null) {
                resJsonList.add(objectContents);
            }

        }

        if (result.isTruncated()) {
            nextToken = result.getNextContinuationToken();
        } else {
            // Use this special token to sign the end of the import.
            nextToken = "ALLDONE";
        }

        return resJsonList;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            nextToken = (String) checkpoint;
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return nextToken;
    }

}
