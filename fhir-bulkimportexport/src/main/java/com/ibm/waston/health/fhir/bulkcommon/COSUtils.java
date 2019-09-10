/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.waston.health.fhir.bulkcommon;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadResult;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartResult;

/**
 * Utility functions for IBM COS.
 * 
 * @author Albert Wang
 */

public class COSUtils {
    private final static Logger logger = Logger.getLogger(COSUtils.class.getName());

    /**
     * Logging helper.
     */
    private static void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }
    
    public static String startPartUpload(AmazonS3 cosClient, String bucketName, String itemName) throws Exception {
        try {
            log("startPartUpload", "Start multi-part upload for " + itemName + " to bucket:" + bucketName);
    
            InitiateMultipartUploadResult mpResult = cosClient.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, itemName));
            return mpResult.getUploadId();
        } catch (Exception sdke) {
            log("startPartUpload", "Upload start Error: " + sdke.getMessage());
            throw sdke;
        }
    }
    
    
    public static AmazonS3 getCosClient(String cosCredentialIbm, String cosApiKeyProperty, String cosSrvinstId, 
            String cosEndpintUrl, String cosLocation) {
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
        AWSCredentials credentials;
        if (cosCredentialIbm.equalsIgnoreCase("Y")) {
            credentials = new BasicIBMOAuthCredentials(cosApiKeyProperty, cosSrvinstId);
        } else {
            credentials = new BasicAWSCredentials(cosApiKeyProperty, cosSrvinstId);
        }

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(8000);
        clientConfig.setUseTcpKeepAlive(true);

        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(cosEndpintUrl, cosLocation))
                .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
    }
    
    
    public static PartETag multiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID, InputStream dataStream, int partSize, int partNum) throws Exception {
        try {
            log("multiPartUpload", "Upload part " + partNum + " to " + itemName);  

            UploadPartRequest upRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(itemName)
                    .withUploadId(uploadID)
                    .withPartNumber(partNum)
                    .withInputStream(dataStream)
                    .withPartSize(partSize);

            UploadPartResult upResult = cosClient.uploadPart(upRequest);
            return upResult.getPartETag();
        } catch (Exception sdke) {
            log("multiPartUpload", "Upload part Error: " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }
    
    
    
    public static void finishMultiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID, List<PartETag> dataPacks) throws Exception {
        try {  
            cosClient.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
            log("finishMultiPartUpload", "Upload finished for " + itemName);
        } catch (Exception sdke) {
            log("finishMultiPartUpload", "Upload finish Error: " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }
    
    
    public static void listBuckets(AmazonS3 cosClient) {
        if (cosClient == null)
            return;

        log("listBuckets", "Buckets:");
        final List<Bucket> bucketList = cosClient.listBuckets();

        for (final Bucket bucket : bucketList) {
            log("listBuckets", bucket.getName());
        }
    }

}
