/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.cos;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
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
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectResult;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.bucket.api.CosItem;
import com.ibm.fhir.bucket.api.FileType;

/**
 * Encapsulates the AmazonS3 client for interaction with IBM Cloud Object Storage (COS)
 */
public class COSClient {
    private static final Logger logger = Logger.getLogger(COSClient.class.getName());

    // Switch on to write to the local filesystem instead of COS
    private static final boolean DEBUG = false;
    
    // The client connection established by calling #connect()
    private AmazonS3 client;

    private COSPropertiesAdapter propertiesAdapter;
    
    // Set to false to tell a scan to return early
    private volatile boolean running = true;
    
    
    static {
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
    }
    
    /**
     * Public constructor
     * @param cosProperties
     */
    public COSClient(Properties cosProperties) {
        // properties to configure COS connection
        this.propertiesAdapter = new COSPropertiesAdapter(cosProperties);

        AWSCredentials credentials;
        if (propertiesAdapter.isCredentialIBM()) {
            credentials = new BasicIBMOAuthCredentials(propertiesAdapter.getApiKey(), propertiesAdapter.getSrvInstId());
        } else {
            credentials = new BasicAWSCredentials(propertiesAdapter.getApiKey(), propertiesAdapter.getSrvInstId());
        }
    
        ClientConfiguration clientConfig = new ClientConfiguration()
                .withRequestTimeout(propertiesAdapter.getRequestTimeout())
                .withTcpKeepAlive(true)
                .withSocketTimeout(propertiesAdapter.getSocketTimeout());

        // connect to the configured endpoint/location
        this.client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new EndpointConfiguration(propertiesAdapter.getEndpointUrl(), propertiesAdapter.getLocation()))
            .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
    }

    /**
     * Tell the scanner to stop. Can be used to get scan() to finish early
     */
    public void signalStop() {
        this.running = false;
    }
    
    /**
     * Read the object using the given function
     * @param <T>
     * @param bucketName
     * @param itemName
     * @param fn
     * @return
     */
    public <T> T read(String bucketName, String itemName, Function<PushbackInputStream, T> fn) {
        
        S3Object item = client.getObject(new GetObjectRequest(bucketName, itemName));
        try (S3ObjectInputStream s3InStream = item.getObjectContent()) {
            // decorate the stream to make it easier to handle NDJSON
            // objects (the main reason we are here)
            return fn.apply(new PushbackInputStream(s3InStream));
        } catch (IOException x) {
            logger.log(Level.SEVERE, "error closing stream for '" + bucketName + ":" + itemName + "'");
            throw new IllegalStateException("error closing object stream", x);
        }
    }

    /**
     * Read and process the object, feeding the content to the given consumer as a
     * BufferedReader. We keep control of the stream, and close it when the consumer
     * accept call returns
     * @param bucketName
     * @param itemName
     * @param consumer
     */
    public void process(String bucketName, String itemName, Consumer<BufferedReader> consumer) {

        logger.info("Reading: " + bucketName + ":" + itemName);
        S3Object item = client.getObject(new GetObjectRequest(bucketName, itemName));
        try (InputStream is = item.getObjectContent()) {
            // TODO: Assume UTF-8 for now. Should check item.getObjectMetadata().getContentEncoding();
            consumer.accept(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
        } catch (IOException x) {
            logger.log(Level.SEVERE, "error closing stream for '" + bucketName + ":" + itemName + "'");
            throw new IllegalStateException("error closing object stream", x);
        }
    }

    
    /**
     * Scan the COS bucket, feeding each returned item to the given consumer
     * @param bucketName
     * @param fileType function to derive fileType from the item key value
     * @param consumer target for each non-empty CosItem we find in the bucket
     */
    public void scan(String bucketName, String pathPrefix, Function<String, FileType> fileTyper, Consumer<CosItem> consumer) {
        logger.info("Scanning bucket: '" + bucketName + "'");
        ListObjectsV2Result result = null;
        String nextToken = null;
        do {
            if (!running) {
                return; // stopped by another thread
            }
            
            if (result != null) {
                nextToken = result.getNextContinuationToken();
            }
            ListObjectsV2Request request =
                    new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(propertiesAdapter.getMaxKeys())
                            .withContinuationToken(nextToken);
            if (pathPrefix != null) {
                request.withPrefix(pathPrefix);
            }
            result = client.listObjectsV2(request);

            if (running && result != null) {
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("COS Item: {bucket=" + bucketName + ", item=" + objectSummary.getKey()
                                + ", bytes=" + objectSummary.getSize() + "}");
                    }
    
                    if (objectSummary.getSize() > 0) {
                        // Use the fileTyper function to determine the file type. We use create a
                        // CosItem to represent the item so we can hide ListObjectsV2Request for
                        // better separation of concerns.
                        FileType ft = fileTyper.apply(objectSummary.getKey());
                        consumer.accept(new CosItem(bucketName, objectSummary.getKey(), objectSummary.getSize(), ft,
                            objectSummary.getETag(), objectSummary.getLastModified()));
                    }
                }
            }
        } while (running && result != null && result.isTruncated());
    }

    /**
     * Write the payload to the given bundleName as key
     * @param bundleName
     * @param payload
     */
    public void write(String bucketName, String objectName, String payload) {
        byte[] raw = payload.getBytes(StandardCharsets.UTF_8);
        
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentLength(raw.length);
        omd.setContentEncoding("application/json");
        
        logger.info("Writing to COS '" + bucketName + ":" + objectName + "', bytes: " + raw.length);

        if (DEBUG) {
            try (FileOutputStream fos = new FileOutputStream(objectName)) {
                fos.write(raw);
                fos.flush();
            } catch (IOException x) {
                logger.log(Level.SEVERE, "Writing " + objectName, x);
            }
        } else {
            // Write the object to the target key (objectName) in the given bucket
            InputStream inputStream = new ByteArrayInputStream(raw);
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, objectName, inputStream, omd));
            if (result != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Wrote [" + bucketName + "]/" + objectName + ", ETag: " + result.getETag());
                }
            } else {
                logger.warning("Writing failed for [" + bucketName + "]/" + objectName + ", bytes: " + raw.length);
                throw new RuntimeException("Write to COS failed");
            }
        }
    }
}