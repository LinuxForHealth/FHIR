/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.client.S3ClientGenerator;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.s3.S3HostStyle;

/**
 * S3Provider for ibm-cos and aws-s3
 */
public class S3Provider implements Provider {

    private static final Logger logger = Logger.getLogger(S3Provider.class.getName());

    private static final long COS_PART_MINIMALSIZE = ConfigurationFactory.getInstance().getCoreCosPartUploadTriggerSize();
    
    private static final S3ClientGenerator GENERATOR = new S3ClientGenerator();

    private ImportTransientUserData transientUserData = null;
    private ExportTransientUserData chunkData = null;

    private long parseFailures = 0l;

    private List<Resource> resources = new ArrayList<>();
    private AmazonS3 client = null;

    private String bucketName = null;
    private boolean pathStyle;

    private boolean create = false;

    private long executionId = -1;

    private String cosBucketPathPrefix = null;
    private String fhirResourceType = null;

    /**
     * The provider loads the Client based on the Source.
     * The Source (storageProvider) must be s3 compatible.
     * @param source
     * @throws FHIRException
     */
    public S3Provider(String source) throws FHIRException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

        boolean useFhirServerTrustStore = adapter.shouldCoreCosUseServerTruststore();

        String cosLocation = adapter.getStorageProviderLocation(source);
        String cosEndpointUrl = adapter.getStorageProviderEndpointInternal(source);
        bucketName = adapter.getStorageProviderBucketName(source);

        boolean iam = adapter.isStorageProviderAuthTypeIam(source);

        String apiKey = null;
        String resourceId = null;
        String authType = adapter.getStorageProviderAuthType(source);
        if (authType == null) {
            logger.warning("S3Wrapper: No Auth Type Found");
        } else if ("hmac".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeHmacAccessKey(source);
            resourceId = adapter.getStorageProviderAuthTypeHmacSecretKey(source);
        } else if ("basic".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeUsername(source);
            resourceId = adapter.getStorageProviderAuthTypePassword(source);
        } else if ("iam".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeIamApiKey(source);
            resourceId = adapter.getStorageProviderAuthTypeIamApiResourceInstanceId(source);
        }

        // Set up the connection to Cos
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Connecting to S3: [" + cosEndpointUrl + "] [" + cosLocation + "]");
        }

        create = adapter.shouldStorageProviderCreate(source);

        // There are two styles PATH/VIRTUAL_HOST
        S3HostStyle kind = adapter.getS3HostStyleByStorageProvider(source);
        pathStyle = S3HostStyle.PATH.equals(kind);

        // Create a COS/S3 client if it's not created yet.
        client = getClient(iam, apiKey, resourceId, cosEndpointUrl, cosLocation, useFhirServerTrustStore);

        if (client == null) {
            logger.warning("Failed to get client!");
            throw new FHIRException("Failed to get client!!");
        } else {
            logger.fine("Succeed to get client!");
        }

        if (bucketName != null) {
            logger.fine("Succeed to get BucketName!");
            // Naming convention has it as - The bucket name can be between 3 and 63 characters long, and can contain only lower-case characters, numbers, periods, and dashes.
            this.bucketName = bucketName.trim().toLowerCase();
        }
    }

    /**
     * gets the s3 client used with ibm/aws/minio
     * @param iam
     * @param cosApiKeyProperty
     * @param cosSrvinstId
     * @param cosEndpointUrl
     * @param cosLocation
     * @param useFhirServerTrustStore
     * @return
     */
    private AmazonS3 getClient(boolean iam, String cosApiKeyProperty, String cosSrvinstId, String cosEndpointUrl, String cosLocation, boolean useFhirServerTrustStore) {
        return GENERATOR.getClient(iam, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl, cosLocation, useFhirServerTrustStore, pathStyle);
    }

    /**
     * checks to see if the bucket exists.
     *
     * @return
     */
    public boolean exists() {
        // Only valid with a path access style, else we short circuit
        return client != null &&
                (!pathStyle || !client.doesBucketExistV2(bucketName));
    }

    /**
     * lists the buckets when logging out bucket details
     */
    public void listBuckets() {
        if (client != null && logger.isLoggable(Level.FINE)) {
            final List<Bucket> bucketList = client.listBuckets();
            int bucketCount = 1;
            for (final Bucket bucket : bucketList) {
                logger.fine("[" + bucketCount + "] - '" + bucket.getName() + "'");
                bucketCount++;
            }
        }
    }

    /**
     * get the list of objects based on the continuation token.
     *
     * @param prefix the search key
     * @param continuationToken
     *            null or string indicating a continuation token.
     * @return
     * @throws FHIRException
     */
    public ListObjectsV2Result getListObject(String prefix, String continuationToken) throws FHIRException {
        if (client != null) {
            ListObjectsV2Request request =
                    new ListObjectsV2Request()
                            .withPrefix(prefix)
                            .withMaxKeys(1000)
                            .withContinuationToken(continuationToken)
                            .withBucketName(bucketName);
            return client.listObjectsV2(request);
        } else {
            throw new FHIRException("Client is not created");
        }
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        try {
            S3Object item = client.getObject(new GetObjectRequest(bucketName, workItem));
            return item.getObjectMetadata().getContentLength();
        } catch (Exception e) {
            throw new FHIRException("Error Getting File Size '" + bucketName + "/" + workItem + "'", e);
        }
    }

    @Override
    public List<Resource> getResources() throws FHIRException {
        return resources;
    }

    @Override
    public long getNumberOfParseFailures() throws FHIRException {
        return parseFailures;
    }

    @Override
    public void registerTransient(ImportTransientUserData transientUserData) {
        this.transientUserData = transientUserData;
    }

    @Override
    public long getNumberOfLoaded() throws FHIRException {
        return this.resources.size();
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        try {
            parseFailures = BulkDataUtils.readFhirResourceFromObjectStore(client, bucketName, workItem, (int) numOfLinesToSkip, resources, transientUserData);
        } catch (Exception e) {
            throw new FHIRException("Unable to read from S3 File", e);
        }
    }

    @Override
    public void pushOperationOutcomes() throws FHIRException {
        try {
            // Upload OperationOutcomes in buffer if it reaches the minimal size for multiple-parts upload.
            if (transientUserData.getBufferStreamForImport().size() > COS_PART_MINIMALSIZE) {
                if (transientUserData.getUploadIdForOperationOutcomes() == null) {
                    transientUserData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(client,
                            bucketName, transientUserData.getUniqueIDForImportOperationOutcomes()));
                }

                transientUserData.getDataPacksForOperationOutcomes().add(BulkDataUtils.multiPartUpload(client,
                        bucketName,
                        transientUserData.getUniqueIDForImportOperationOutcomes(),
                        transientUserData.getUploadIdForOperationOutcomes(),
                        new ByteArrayInputStream(transientUserData.getBufferStreamForImport().toByteArray()),
                        transientUserData.getBufferStreamForImport().size(),
                        transientUserData.getPartNumForOperationOutcomes()));

                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("pushImportOperationOutcomesToCOS: " + transientUserData.getBufferStreamForImport().size()
                            + " bytes were successfully appended to COS object - " + transientUserData.getUniqueIDForImportOperationOutcomes());
                }
                transientUserData.setPartNumForOperationOutcomes(transientUserData.getPartNumForOperationOutcomes() + 1);
                transientUserData.getBufferStreamForImport().reset();
            }

            // Upload OperationOutcomes in failure buffer if it reaches the minimal size for multiple-parts upload.
            if (transientUserData.getBufferStreamForImportError().size() > COS_PART_MINIMALSIZE) {
                if (transientUserData.getUploadIdForFailureOperationOutcomes() == null) {
                    transientUserData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(client,
                            bucketName, transientUserData.getUniqueIDForImportFailureOperationOutcomes()));
                }

                transientUserData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(client,
                        bucketName,
                        transientUserData.getUniqueIDForImportFailureOperationOutcomes(),
                        transientUserData.getUploadIdForFailureOperationOutcomes(),
                        new ByteArrayInputStream(transientUserData.getBufferStreamForImportError().toByteArray()),
                        transientUserData.getBufferStreamForImportError().size(),
                        transientUserData.getPartNumForFailureOperationOutcomes()));
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("pushImportOperationOutcomesToOS: " + transientUserData.getBufferStreamForImportError().size()
                            + " bytes were successfully appended to COS object - " + transientUserData.getUniqueIDForImportFailureOperationOutcomes());
                }
                transientUserData.setPartNumForFailureOperationOutcomes(transientUserData.getPartNumForFailureOperationOutcomes() + 1);
                transientUserData.getBufferStreamForImportError().reset();
            }
        } catch (Exception e) {
            throw new FHIRException("Unable to write to S3 OperationOutcome File", e);
        }
    }

    @Override
    public void createSource() throws FHIRException {
        if (client != null) {
            if (create && !this.exists()) {
                CreateBucketRequest req = new CreateBucketRequest(bucketName);
                client.createBucket(req);
            }
        } else {
            throw new FHIRException("Client is not created");
        }
    }

    public AmazonS3 getClient() {
        return client;
    }

    @Override
    public void close() throws Exception {
        logger.fine("closing the S3Wrapper");
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        // Only if we're greater than zero, otherwise there is nothing to upload.
        if (chunkData.getBufferStream().size() > 0) {
            // TODO try PipedOutputStream -> PipedInputStream instead?  Or maybe a ByteBuffer with a flip instead?
            pushFhirJsonsToCos(new ByteArrayInputStream(chunkData.getBufferStream().toByteArray()), chunkData.getBufferStream().size());
        }
        chunkData.setLastWrittenPageNum(chunkData.getPageNum());
    }

    @Override
    public void registerTransient(long executionId, ExportTransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType) throws Exception {
        if (transientUserData == null) {
            logger.warning("registerTransient: chunkData is null, this should never happen!");
            throw new Exception("registerTransient: chunkData is null, this should never happen!");
        }

        this.executionId = executionId;
        this.chunkData = transientUserData;
        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.fhirResourceType = fhirResourceType;
    }

    private void pushFhirJsonsToCos(InputStream in, int dataLength) throws Exception {
        String itemName;
        if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
            itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        } else {
            itemName = "system_export_" + executionId + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        }

        String uploadId = chunkData.getUploadId();
        if (uploadId == null) {
            uploadId = BulkDataUtils.startPartUpload(client, bucketName, itemName);
            chunkData.setUploadId(uploadId);
        }

        chunkData.getCosDataPacks().add(BulkDataUtils.multiPartUpload(client, bucketName, itemName, uploadId, in, dataLength, chunkData.getPartNum()));
        logger.info("pushFhirJsonsToCos: '" + dataLength + "' bytes were successfully appended to COS object - '" + itemName + "' uploadId='" + uploadId + "'");
        chunkData.setPartNum(chunkData.getPartNum() + 1);
        chunkData.getBufferStream().reset();

        // Close it out...
        if (chunkData.isFinishCurrentUpload()) {
            BulkDataUtils.finishMultiPartUpload(client, bucketName, itemName, uploadId, chunkData.getCosDataPacks());
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            if (chunkData.getResourceTypeSummary() == null) {
                chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() >= chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            } else {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() >= chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            }

            if (chunkData.getPageNum() < chunkData.getLastPageNum()) {
                chunkData.setPartNum(1);
                chunkData.setUploadId(null);
                chunkData.setCurrentUploadResourceNum(0);
                chunkData.setCurrentUploadSize(0);
                chunkData.setFinishCurrentUpload(false);
                chunkData.getCosDataPacks().clear();
                chunkData.setUploadCount(chunkData.getUploadCount() + 1);
            }
        }
    }
}