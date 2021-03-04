/*
 * (C) Copyright IBM Corp. 2021
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

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.ibm.cloud.objectstorage.ApacheHttpClientConfig;
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.export.writer.SparkParquetWriter;
import com.ibm.fhir.bulkdata.jbatch.export.data.TransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * S3Provider for ibm-cos and aws-s3
 */
public class S3Provider implements Provider {

    private static final Logger logger = Logger.getLogger(S3Provider.class.getName());

    private static final int COS_PART_MINIMALSIZE = ConfigurationFactory.getInstance().getCoreCosMinSize();

    private ImportTransientUserData transientUserData = null;
    private TransientUserData chunkData = null;

    private long parseFailures = 0l;

    private List<Resource> resources = new ArrayList<>();
    private AmazonS3 client = null;
    private SparkParquetWriter parquetWriter = null;

    private String bucketName = null;

    private boolean create = false;

    private boolean isExportPublic = false;
    private long executionId = -1;

    private String cosBucketPathPrefix = null;
    private String fhirResourceType = null;

    public S3Provider(String source) throws FHIRException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

        boolean useFhirServerTrustStore = adapter.shouldCoreCosUseServerTruststore();

        String cosLocation = adapter.getStorageProviderLocation(source);
        String cosEndpointUrl = adapter.getStorageProviderEndpointInternal(source);
        String bucketName = adapter.getStorageProviderBucketName(source);

        boolean iam = adapter.isStorageProviderAuthTypeIam(source);
        isExportPublic = adapter.isStorageProviderExportPublic(source);

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
            logger.fine("Connecting to S3: " + cosEndpointUrl + " [" + cosLocation + "]");
        }

        create = adapter.shouldStorageProviderCreate(source);

        // Create a COS/S3 client if it's not created yet.
        client = getClient(iam, apiKey, resourceId, cosEndpointUrl, cosLocation, useFhirServerTrustStore);

        if (client == null) {
            logger.warning("Failed to get client!");
            throw new FHIRException("Failed to get client!!");
        } else {
            logger.fine("Succeed get client!");
        }

        if (bucketName == null) {
            logger.warning("Failed to get BucketName!");
        } else {
            logger.fine("Succeed get BucketName!");
        }
        this.bucketName = bucketName.trim().toLowerCase();

        if (adapter.isStorageProviderParquetEnabled(source)) {
            try {
                Class.forName("org.apache.spark.sql.SparkSession");
                parquetWriter = new SparkParquetWriter(adapter.isStorageProviderAuthTypeIam(source), cosEndpointUrl, apiKey, resourceId);
            } catch (ClassNotFoundException e) {
                logger.info("No SparkSession in classpath; skipping SparkParquetWriter initialization");
            }
        }
    }

    private static AmazonS3 getClient(boolean iam, String cosApiKeyProperty, String cosSrvinstId,
        String cosEndpointUrl, String cosLocation, boolean useFhirServerTrustStore) {
        ConfigurationAdapter configAdapter = ConfigurationFactory.getInstance();

        AWSCredentials credentials;
        if (iam) {
            SDKGlobalConfiguration.IAM_ENDPOINT = configAdapter.getCoreIamEndpoint();
            credentials = new BasicIBMOAuthCredentials(cosApiKeyProperty, cosSrvinstId);
        } else {
            credentials = new BasicAWSCredentials(cosApiKeyProperty, cosSrvinstId);
        }

        ClientConfiguration clientConfig =
                new ClientConfiguration()
                    .withRequestTimeout(configAdapter.getCoreCosRequestTimeout())
                    .withTcpKeepAlive(configAdapter.getCoreCosTcpKeepAlive())
                    .withSocketTimeout(configAdapter.getCoreCosSocketTimeout());

        if (useFhirServerTrustStore) {
            ApacheHttpClientConfig apacheClientConfig = clientConfig.getApacheHttpClientConfig();
            // The following line configures COS/S3 SDK to use SSLConnectionSocketFactory of liberty server,
            // it makes sure the certs added in fhirTrustStore.p12 can be used for SSL connection with any S3
            // compatible object store, e.g, minio object store with self signed cert.
            apacheClientConfig.setSslSocketFactory(SSLConnectionSocketFactory.getSystemSocketFactory());
        }

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(cosEndpointUrl, cosLocation))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig)
                .build();
    }

    /**
     * checks to see if the bucket exists.
     *
     * @return
     */
    public boolean exists() {
        boolean ex = client != null && client.doesBucketExistV2(bucketName);
        if (ex == false) {
            logger.warning("Bucket '" + bucketName + "' not found! Client [" + (client != null) + "]");
        }
        return ex;
    }

    /**
     * lists the buckets when logging
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
     * @param continuationToken
     *            null or string indicating a continuation token.
     * @return
     * @throws FHIRException
     */
    public ListObjectsV2Result getListObject(String continuationToken) throws FHIRException {
        if (client != null) {
            ListObjectsV2Request request =
                    new ListObjectsV2Request()
                        .withBucketName(bucketName)
                        .withMaxKeys(1000)
                        .withContinuationToken(continuationToken);
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
                    transientUserData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(client, bucketName, transientUserData.getUniqueIDForImportOperationOutcomes(), true));
                }

                transientUserData.getDataPacksForOperationOutcomes().add(BulkDataUtils.multiPartUpload(client, bucketName, transientUserData.getUniqueIDForImportOperationOutcomes(), transientUserData.getUploadIdForOperationOutcomes(), new ByteArrayInputStream(transientUserData.getBufferStreamForImport().toByteArray()), transientUserData.getBufferStreamForImport().size(), (int) transientUserData.getPartNumForOperationOutcomes()));

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
                    transientUserData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(client, bucketName, transientUserData.getUniqueIDForImportFailureOperationOutcomes(), true));
                }

                transientUserData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(client, bucketName, transientUserData.getUniqueIDForImportFailureOperationOutcomes(), transientUserData.getUploadIdForFailureOperationOutcomes(), new ByteArrayInputStream(transientUserData.getBufferStreamForImportError().toByteArray()), transientUserData.getBufferStreamForImportError().size(), (int) transientUserData.getPartNumForFailureOperationOutcomes()));
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

    public SparkParquetWriter getParquetWriter() {
        return parquetWriter;
    }

    @Override
    public void close() throws Exception {
        logger.fine("closing the S3Wrapper");
        if (parquetWriter != null) {
            parquetWriter.close();
        }
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        for (ReadResultDTO dto : dtos) {
            switch (mediaType) {
            case FHIRMediaType.APPLICATION_PARQUET:
                boolean isTimeToWriteParquet = chunkData.getPageNum() > chunkData.getLastPageNum()
                        || chunkData.isFinishCurrentUpload();
                if (isTimeToWriteParquet) {
                    pushFhirParquetToCos(dto.getResources());
                    chunkData.setLastWritePageNum(chunkData.getPageNum());
                }
                break;
            case FHIRMediaType.APPLICATION_NDJSON:
            default:
                boolean isTimeToWrite = chunkData.getPageNum() > chunkData.getLastPageNum()
                        || chunkData.getBufferStream().size() > COS_PART_MINIMALSIZE
                        || chunkData.isFinishCurrentUpload();
                if (isTimeToWrite && chunkData.getBufferStream().size() > 0) {
                    // TODO try PipedOutputStream -> PipedInputStream instead?
                    pushFhirJsonsToCos(new ByteArrayInputStream(chunkData.getBufferStream().toByteArray()), chunkData.getBufferStream().size());
                    chunkData.setLastWritePageNum(chunkData.getPageNum());
                }
            }
        }
    }

    @Override
    public void registerTransient(long executionId, TransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType,
        boolean isExportPublic) throws Exception {
        if (transientUserData == null) {
            logger.warning("registerTransient: chunkData is null, this should never happen!");
            throw new Exception("registerTransient: chunkData is null, this should never happen!");
        }

        this.executionId = executionId;
        this.chunkData = transientUserData;
        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.fhirResourceType = fhirResourceType;
        this.isExportPublic = isExportPublic;
    }

    private void pushFhirJsonsToCos(InputStream in, int dataLength) throws Exception {
        String itemName;
        if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
            itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        } else {
            itemName = "system_export_" + executionId + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        }

        if (chunkData.getUploadId() == null) {
            chunkData.setUploadId(BulkDataUtils.startPartUpload(client, bucketName, itemName, isExportPublic));
        }

        chunkData.getCosDataPacks().add(BulkDataUtils.multiPartUpload(client, bucketName, itemName, chunkData.getUploadId(), in, dataLength, chunkData.getPartNum()));
        logger.info("pushFhirJsonsToCos: " + dataLength + " bytes were successfully appended to COS object - "
                + itemName);
        chunkData.setPartNum(chunkData.getPartNum() + 1);
        chunkData.getBufferStream().reset();

        if (chunkData.getPageNum() > chunkData.getLastPageNum() || chunkData.isFinishCurrentUpload()) {
            BulkDataUtils.finishMultiPartUpload(client, bucketName, itemName, chunkData.getUploadId(), chunkData.getCosDataPacks());
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            if (chunkData.getResourceTypeSummary() == null) {
                chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            } else {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            }

            if (chunkData.getPageNum() <= chunkData.getLastPageNum()) {
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

    private void pushFhirParquetToCos(List<Resource> resources) throws Exception {
        if (chunkData == null) {
            logger.warning("pushFhirParquetToCos: chunkData is null, this should never happen!");
            throw new Exception("pushFhirParquetToCos: chunkData is null, this should never happen!");
        }

        String itemName;
        if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
            itemName = "cos://" + bucketName + ".fhir/" + cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".parquet";
        } else {
            itemName = "cos://" + bucketName + ".fhir/job" + executionId + "/" + fhirResourceType + "_" + chunkData.getUploadCount()
                    + ".parquet";
        }

        parquetWriter.writeParquet(resources, itemName);

        // Partition status for the exported resources, e.g, Patient[1000,1000,200]
        if (chunkData.getResourceTypeSummary() == null) {
            chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
            if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
            }
        } else {
            chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
            if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");

            }
        }
        if (chunkData.getPageNum() <= chunkData.getLastPageNum()) {
            chunkData.setPartNum(1);
            chunkData.setUploadId(null);
            chunkData.setCurrentUploadResourceNum(0);
            chunkData.setCurrentUploadSize(0);
            chunkData.setFinishCurrentUpload(false);
            chunkData.setUploadCount(chunkData.getUploadCount() + 1);
        }
    }
}