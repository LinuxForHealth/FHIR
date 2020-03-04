/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkcommon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.ibm.cloud.objectstorage.services.s3.model.CannedAccessControlList;
import com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadResult;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartResult;
import com.ibm.fhir.bulkimport.ImportTransientUserData;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;

/**
 * Utility functions for IBM COS.
 *
 */

public class BulkDataUtils {
    private final static Logger logger = Logger.getLogger(BulkDataUtils.class.getName());

    /**
     * Logging helper.
     */
    private static void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    public static String startPartUpload(AmazonS3 cosClient, String bucketName, String itemName, boolean isPublicAccess) throws Exception {
        try {
            log("startPartUpload", "Start multi-part upload for " + itemName + " to bucket - " + bucketName);

            InitiateMultipartUploadRequest initMultipartUploadReq = new InitiateMultipartUploadRequest(bucketName, itemName);
            if (isPublicAccess) {
                initMultipartUploadReq.setCannedACL(CannedAccessControlList.PublicRead);
            }

            InitiateMultipartUploadResult mpResult = cosClient.initiateMultipartUpload(initMultipartUploadReq);
            return mpResult.getUploadId();
        } catch (Exception sdke) {
            log("startPartUpload", "Upload start Error - " + sdke.getMessage());
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

    public static PartETag multiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID,
            InputStream dataStream, int partSize, int partNum) throws Exception {
        try {
            log("multiPartUpload", "Upload part " + partNum + " to " + itemName);

            UploadPartRequest upRequest = new UploadPartRequest().withBucketName(bucketName).withKey(itemName)
                    .withUploadId(uploadID).withPartNumber(partNum).withInputStream(dataStream).withPartSize(partSize);

            UploadPartResult upResult = cosClient.uploadPart(upRequest);
            return upResult.getPartETag();
        } catch (Exception sdke) {
            log("multiPartUpload", "Upload part Error - " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }

    public static void finishMultiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID,
            List<PartETag> dataPacks) throws Exception {
        try {
            cosClient.completeMultipartUpload(
                    new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
            log("finishMultiPartUpload", "Upload finished for " + itemName);
        } catch (Exception sdke) {
            log("finishMultiPartUpload", "Upload finish Error: " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }

    public static void listBuckets(AmazonS3 cosClient) {
        if (cosClient == null) {
            return;
        }
        log("listBuckets", "Buckets:");
        final List<Bucket> bucketList = cosClient.listBuckets();

        for (final Bucket bucket : bucketList) {
            log("listBuckets", bucket.getName());
        }
    }

    private static int getFhirResourceFromBufferReader(BufferedReader resReader, int numOfLinesToSkip, List<Resource> fhirResources) throws Exception {
        int exported = 0;
        int lineRed = 0;
        while (true) {
            String resLine = resReader.readLine();
            lineRed++;
            if (resLine == null) {
                break;
            }
            if (lineRed <= numOfLinesToSkip) {
                continue;
            }
            fhirResources.add(FHIRParser.parser(Format.JSON).parse(new StringReader(resLine)));
            exported++;
            if (exported == Constants.IMPORT_NUMOFFHIRRESOURCES_PERREAD) {
                break;
            }
        }
        return exported;
    }

    public static int readFhirResourceFromObjectStore(AmazonS3 cosClient, String bucketName, String itemName,
           int numOfLinesToSkip, List<Resource> fhirResources, boolean isReuseInput, ImportTransientUserData transientUserData) {
        int exported = 0;
        if (isReuseInput) {
            if (transientUserData.getBufferReader() == null) {
                S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));
                S3ObjectInputStream s3InStream = item.getObjectContent();
                transientUserData.setInputStream(s3InStream);
                BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream));
                transientUserData.setBufferReader(resReader);
            }
            try {
                exported = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), 0, fhirResources);
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromObjectStore: " + "Error proccesing file " + itemName + " - " + ioe.getMessage());
                exported = 0;
            }

        } else {
            S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));
            try (S3ObjectInputStream s3InStream = item.getObjectContent();
                 BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
                exported = getFhirResourceFromBufferReader(resReader, numOfLinesToSkip, fhirResources);
                // Notify s3 client to abort and prevent the server from keeping on sending data.
                s3InStream.abort();
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromObjectStore: " + "Error proccesing file " + itemName + " - " + ioe.getMessage());
                exported = 0;
            }
        }

        return exported;
    }


    public static int readFhirResourceFromLocalFile(String filePath, int numOfLinesToSkip, List<Resource> fhirResources,
            boolean isReuseInput, ImportTransientUserData transientUserData) {
        int exported = 0;
        if (isReuseInput) {
            try {
                if (transientUserData.getBufferReader() == null) {
                    BufferedReader resReader = Files.newBufferedReader(Paths.get(filePath));
                    transientUserData.setBufferReader(resReader);
                }
                exported = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), 0, fhirResources);
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromLocalFile: " + "Error proccesing file " + filePath + " - " + ioe.getMessage());
                exported = 0;
            }
        } else {
            try (BufferedReader resReader = Files.newBufferedReader(Paths.get(filePath))) {
                exported = getFhirResourceFromBufferReader(resReader, numOfLinesToSkip, fhirResources);
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromLocalFile: " + "Error proccesing file " + filePath + " - " + ioe.getMessage());
                exported = 0;
            }
        }
        return exported;
    }


    public static int readFhirResourceFromHttps(String dataUrl, int numOfLinesToSkip, List<Resource> fhirResources,
            boolean isReuseInput, ImportTransientUserData transientUserData) {
        int exported = 0;
        if (isReuseInput) {
            try {
                if (transientUserData.getBufferReader() == null) {
                    InputStream inputStream = new URL(dataUrl).openConnection().getInputStream();
                    transientUserData.setInputStream(inputStream);
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(inputStream));
                    transientUserData.setBufferReader(resReader);
                }
                exported = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), 0, fhirResources);
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromHttps: " + "Error proccesing file " + dataUrl + " - " + ioe.getMessage());
                exported = 0;
            }
        } else {
            try (BufferedReader resReader = new BufferedReader(new InputStreamReader(new URL(dataUrl).openConnection().getInputStream()))) {
                exported = getFhirResourceFromBufferReader(resReader, numOfLinesToSkip, fhirResources);
            } catch (Exception ioe) {
                logger.warning("readFhirResourceFromHttps: " + "Error proccesing file " + dataUrl + " - " + ioe.getMessage());
                exported = 0;
            }
        }
        return exported;
    }
}
