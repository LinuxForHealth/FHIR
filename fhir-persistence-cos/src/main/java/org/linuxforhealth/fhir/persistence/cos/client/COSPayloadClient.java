/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cos.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.AmazonServiceException;
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
import com.ibm.cloud.objectstorage.services.s3.model.DeleteObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectResult;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.payload.PayloadReader;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

/**
 * Encapsulates the AmazonS3 client for handling COS payloads
 */
public class COSPayloadClient {
    private static final Logger logger = Logger.getLogger(COSPayloadClient.class.getName());

    // The client connection established by calling #connect()
    private final AmazonS3 client;

    private final CosPropertyGroupAdapter propertyAdapter;

    // The tenant this client is tied to
    private final String tenantId;
    
    // The datastoreId
    private final String dsId;

    static {
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
    }

    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param propertyAdapter
     */
    public COSPayloadClient(String tenantId, String dsId, CosPropertyGroupAdapter propertyAdapter) {

        this.tenantId = tenantId;
        this.dsId = dsId;
        this.propertyAdapter = propertyAdapter;

        AWSCredentials credentials;
        if (propertyAdapter.isCredentialIBM()) {
            credentials = new BasicIBMOAuthCredentials(propertyAdapter.getApiKey(), propertyAdapter.getSrvInstId());
        } else {
            credentials = new BasicAWSCredentials(propertyAdapter.getApiKey(), propertyAdapter.getSrvInstId());
        }

        ClientConfiguration clientConfig = new ClientConfiguration()
                .withRequestTimeout(propertyAdapter.getRequestTimeout())
                .withTcpKeepAlive(true)
                .withSocketTimeout(propertyAdapter.getSocketTimeout());

        // connect to the configured endpoint/location
        // VirtualHosts not supported
        this.client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new EndpointConfiguration(propertyAdapter.getEndpointUrl(), propertyAdapter.getLocation()))
            .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
    }


    /**
     * Read the object using the given function
     * @param <T>
     * @param objectName
     * @param fn
     * @return
     */
    public <T extends Resource> T read(Class<T> resourceType, String objectName, PayloadReader payloadReader) throws FHIRPersistenceException {
        final String bucketName = getBucketName();

        S3Object item = client.getObject(new GetObjectRequest(bucketName, objectName));
        if (item != null) {
            try (S3ObjectInputStream s3InStream = item.getObjectContent()) {
                // Delegate actual reading of the resource to the PayloadReader implementation
                return payloadReader.read(resourceType, s3InStream);
            } catch (IOException x) {
                logger.log(Level.SEVERE, "error closing stream for '" + bucketName + ":" + objectName + "'");
                throw new IllegalStateException("error closing object stream", x);
            }
        } else {
            // This is hopefully unusual because we shouldn't be trying to access stuff which
            // doesn't exist
            logger.log(Level.WARNING, "failed to find '" + bucketName + ":" + objectName + "'");
            throw new FHIRPersistenceDataAccessException("Resource not found");
        }
    }

    /**
     * Write the payload to the given objectName as key
     * @param objectName
     * @param compressedPayload the serialized payload (already compressed)
     */
    public void write(String objectName, byte[] compressedPayload) throws FHIRPersistenceException {
        final String bucketName = getBucketName();

        // Set up the metadata for the call to S3/COS
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentLength(compressedPayload.length);
        omd.setContentType("binary");

        if (logger.isLoggable(Level.FINE)) {
            logger.info("Writing to COS '" + bucketName + ":" + objectName + "', bytes: " + compressedPayload.length);
        }

        // Write the object to the target key (objectName) in the given bucket
        InputStream inputStream = new ByteArrayInputStream(compressedPayload);
        PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, objectName, inputStream, omd));
        if (result != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Wrote [" + bucketName + "]/" + objectName + ", ETag: " + result.getETag());
            }
        } else {
            logger.warning("Writing failed for [" + bucketName + "]/" + objectName + ", bytes: " + compressedPayload.length);
            throw new RuntimeException("Write to COS failed");
        }
    }

    /**
     * Write the payload held in the ioStream object to the given objectName as key
     * @param objectName
     * @param ioStream
     * @throws FHIRPersistenceException
     */
    public void write(String objectName, InputOutputByteStream ioStream) throws FHIRPersistenceException {
        final String bucketName = getBucketName();

        // Set up the metadata for the call to S3/COS
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentLength(ioStream.size());
        omd.setContentType("binary");

        if (logger.isLoggable(Level.FINE)) {
            logger.info("Writing to COS '" + bucketName + ":" + objectName + "', bytes: " + ioStream.size());
        }

        // Write the object to the target key (objectName) in the given bucket
        PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, objectName, ioStream.inputStream(), omd));
        if (result != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Wrote [" + bucketName + "]/" + objectName + ", ETag: " + result.getETag());
            }
        } else {
            logger.warning("Writing failed for [" + bucketName + "]/" + objectName + ", bytes: " + ioStream.size());
            throw new RuntimeException("Write to COS failed");
        }
    }

    /**
     * Create the bucket if it doesn't yet exist
     * @param bucketName
     */
    public void createBucketIfNeeded(String bucketName) throws FHIRPersistenceException {
        if (!client.doesBucketExistV2(bucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(bucketName);
            try {
                client.createBucket(req);
            } catch (AmazonServiceException x) {
                try {
                    // suppress the exception if the bucket now exists (to cover a race condition)
                    if (client.doesBucketExistV2(bucketName)) {
                        logger.info("Bucket exists: " + bucketName);
                    } else {
                        throw x;
                    }
                } catch (Exception x2) {
                    // throw the original exception
                    throw x;
                }
            }
        } else {
            logger.info("Bucket exists: " + bucketName);
        }
    }

    /**
     * Delete the object
     * @param objectName
     * @throws FHIRPersistenceException
     */
    public void delete(String objectName) throws FHIRPersistenceException {
        final String bucketName = getBucketName();

        try {
            DeleteObjectRequest delete = new DeleteObjectRequest(bucketName, objectName);
            client.deleteObject(delete);
        } catch (Exception x) {
            throw new FHIRPersistenceException("delete failed", x);
        }
    }

    /**
     * Get the bucket name for the tenant associated with the current request context
     * @return
     */
    private String getBucketName() {
        String bucketName = propertyAdapter.getBucketName();
        if (bucketName == null) {
            // try using the default bucket name for this tenant
            bucketName = COSPayloadHelper.makeTenantBucketName(tenantId, dsId);
        }
        return bucketName;
    }
}